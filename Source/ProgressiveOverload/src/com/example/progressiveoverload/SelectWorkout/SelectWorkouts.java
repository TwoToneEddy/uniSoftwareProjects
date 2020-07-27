package com.example.progressiveoverload.SelectWorkout;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.ContentProviders.ExerciseContentProvider;
import com.example.progressiveoverload.ContentProviders.ExerciseRecordContentProvider;
import com.example.progressiveoverload.ContentProviders.WorkoutContentProvider;
import com.example.progressiveoverload.DoWorkouts.DoWorkouts;
import com.example.progressiveoverload.EditExercises.EditExercises;
import com.example.progressiveoverload.EditWorkouts.EditWorkoutsDetailActivity;
import com.example.progressiveoverload.EditWorkouts.EditWorkoutsDetailFragment;
import com.example.progressiveoverload.EditWorkouts.EditWorkoutsListPopulator;
import com.example.progressiveoverload.R.layout;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class SelectWorkouts extends Activity {

	
	//Object to assist with list population
	EditWorkoutsListPopulator listPopulator;
	
	//Default value for the workout selected (-1 is none selected)
	int selectedWorkoutID = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_workouts);
	
		//Lock to portrait if used on a small screen
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
		
		//Use class intented for edit workouts...
		
		listPopulator = new EditWorkoutsListPopulator(this);
		
		
		Button startWorkoutButton = (Button) this.findViewById(R.id.startWorkoutButton);
		startWorkoutButton.setVisibility(View.GONE);
		
		if(startWorkoutButton != null){
			startWorkoutButton.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  Intent intent = new Intent(getApplicationContext(),DoWorkouts.class);
			  	  intent.putExtra("selectedWorkoutID", selectedWorkoutID);
			  	  startActivity(intent);
		      }
		    });
		}
		
		setMostRecentText();
	    
		
		//Populate the listview and act on any interaction with it
				ListView workoutListView = listPopulator.populateWorkoutList();
				
				workoutListView.setOnItemClickListener(new OnItemClickListener() {
			        public void onItemClick(AdapterView<?> parent, View view,
			                int position, long id) {
			        	
			        	//Get item in first colomn of clicked workout (ID)
			        	selectedWorkoutID = Integer.parseInt((String)((Cursor)parent.getItemAtPosition(position)).getString(0));
			        	
			        	Button startWorkoutButton = (Button)findViewById(R.id.startWorkoutButton);
			        	startWorkoutButton.setVisibility(View.VISIBLE);
			        	
			        	
			        	
			        	String selection = WorkoutContentProvider.KEY_WORKOUT_ID +" == ? ";
					    String[] selectionArgs = new String[] {Integer.toString(selectedWorkoutID)};
					    ContentResolver myContentResolver = getContentResolver();
					    Cursor selectionCursor = myContentResolver.query(WorkoutContentProvider.CONTENT_URI,null, selection, selectionArgs, null);
			        	
					    int dayCol = selectionCursor.getColumnIndexOrThrow(WorkoutContentProvider.KEY_WORKOUT_DAY);
					    int muscleCol = selectionCursor.getColumnIndexOrThrow(WorkoutContentProvider.KEY_MUSCLE_GROUP);
					    
					    if(selectionCursor.moveToFirst()){
					    	startWorkoutButton.setText("Start "+selectionCursor.getString(dayCol)+": "+selectionCursor.getString(muscleCol));
					    }
					    
			        	SelectWorkoutsDetailFragment fragment = (SelectWorkoutsDetailFragment) getFragmentManager()
			                    .findFragmentById(R.id.selectWorkoutsDetailFrag);
			                if (fragment != null && fragment.isInLayout()) {
			                	populateExerciseList(selectedWorkoutID);
			        
			                }
			                	
			        }
			    });
		
}

	
	
	public void populateExerciseList(int selectedWorkoutID){
		listPopulator.populateExerciseList(selectedWorkoutID);
		
	}
	
	public void populateTextFields(int selectedWorkoutID){
		listPopulator.populateTextFields(selectedWorkoutID);
		
	}
	
	public void setMostRecentText(){
		
		TextView lastPerformed = (TextView)findViewById(R.id.lastWorkoutField);
		String lastPerformedDay="";
		ContentResolver myContentResolver = getContentResolver();
		Cursor selectionCursor = myContentResolver.query(WorkoutContentProvider.CONTENT_URI,null, null, null, WorkoutContentProvider.KEY_DATE + " DESC");
	    int dayCol = selectionCursor.getColumnIndexOrThrow(WorkoutContentProvider.KEY_WORKOUT_DAY); 
	    int groupCol = selectionCursor.getColumnIndexOrThrow(WorkoutContentProvider.KEY_MUSCLE_GROUP); 
	    if(selectionCursor.moveToFirst())
	    	if(getResources().getBoolean(R.bool.portrait_only)){
	    		lastPerformedDay = selectionCursor.getString(dayCol);
	        }else{
	        	lastPerformedDay = selectionCursor.getString(dayCol) + ", "+selectionCursor.getString(groupCol);
	        }
	    	
	    lastPerformed.setText(lastPerformedDay);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		setMostRecentText();
	}
	

}
