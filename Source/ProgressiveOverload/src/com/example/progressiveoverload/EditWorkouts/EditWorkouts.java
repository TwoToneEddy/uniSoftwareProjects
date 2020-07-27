package com.example.progressiveoverload.EditWorkouts;




import com.example.progressiveoverload.R;
import com.example.progressiveoverload.Adapters.CustomWorkoutListAdapter;
import com.example.progressiveoverload.Dialogs.*;

import com.example.progressiveoverload.*;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.*;
import android.database.Cursor;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.util.Log;
import android.view.View;
import android.app.Dialog;

public class EditWorkouts extends Activity {

	//Identifiers for the different types of dialogs
	static final int EXERCISE_LIST_DIALOG = 0;
	static final int CREATE_WORKOUT_DIALOG = 1;
	static final int REMOVE_WORKOUT_DIALOG = 2;
	static final int REMOVE_EXERCISE_DIALOG = 3;
	
	//Object to assist with list population
	EditWorkoutsListPopulator listPopulator;
	
	//Objects to assist with dialog creation
	EditWorkoutsListDialogInvoker addExerciseDialogInvoker,createWorkoutDialogInvoker,deleteWorkoutDialogInvoker,removeExerciseDialogInvoker;
	
	//Instances of dialog created locally to allow access to event listners
	Dialog addExerciseListDialog,createWorkoutDialog,deleteWorkoutDialog,removeExerciseListDialog;
	
	//Default value for the workout selected (-1 is none selected)
	int selectedWorkoutID = -1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_workouts);
		
		//Lock to portrait if used on a small screen
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
		
		listPopulator = new EditWorkoutsListPopulator(this);
		
		addExerciseListDialog = new Dialog(this);
		createWorkoutDialog = new Dialog(this);
		deleteWorkoutDialog = new Dialog(this);
	    removeExerciseListDialog = new Dialog(this);
		
		
		
		addExerciseListDialog.setOnDismissListener(new OnDismissListener(){
		     @Override
		     public void onDismiss(DialogInterface dialog) {
		    	 listPopulator.populateExerciseList(selectedWorkoutID);
		    	 removeExerciseDialogInvoker.invoke(REMOVE_EXERCISE_DIALOG);
		     }});
		
		createWorkoutDialog.setOnDismissListener(new OnDismissListener(){
		     @Override
		     public void onDismiss(DialogInterface dialog) {
		    	 
		     }});
		
		deleteWorkoutDialog.setOnDismissListener(new OnDismissListener(){
		     @Override
		     public void onDismiss(DialogInterface dialog) {
		    	 listPopulator.populateWorkoutList();
		     }});
		
		removeExerciseListDialog.setOnDismissListener(new OnDismissListener(){
		     @Override
		     public void onDismiss(DialogInterface dialog) {
		    	 listPopulator.populateExerciseList(selectedWorkoutID);
		    	 removeExerciseDialogInvoker.invoke(REMOVE_EXERCISE_DIALOG);
		     }});
		
		
		addExerciseDialogInvoker = new EditWorkoutsListDialogInvoker(this,R.layout.select_exercise_dialog,R.id.exercise_dialog_list,R.id.exercise_name_textview,"Custom Dialog",addExerciseListDialog);
		removeExerciseDialogInvoker = new EditWorkoutsListDialogInvoker(this,R.layout.select_exercise_dialog,R.id.exercise_dialog_list,R.id.exercise_name_textview,"Custom Dialog",removeExerciseListDialog);
		createWorkoutDialogInvoker = new EditWorkoutsListDialogInvoker(this,R.layout.create_workout_dialog,0,0,"Create Workout",createWorkoutDialog);
		deleteWorkoutDialogInvoker = new EditWorkoutsListDialogInvoker(this,R.layout.delete_workout_dialog,R.id.delete_workout_dialog_list,0,"Delete Workout",deleteWorkoutDialog);
		
		
		
		//Identify all buttons and set up listeners
		Button addExercise = (Button) this.findViewById(R.id.addExerciseButton);
		Button createWorkout = (Button) this.findViewById(R.id.createWorkoutButton);
		Button removeWorkout = (Button) this.findViewById(R.id.removeWorkoutButton);
		Button removeExercise = (Button) this.findViewById(R.id.removeExerciseButton);
		
		if(addExercise != null){
		    addExercise.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  if(selectedWorkoutID > -1){
		    		  addExerciseDialogInvoker.setSelectedWorkoutID(selectedWorkoutID);
				  	showDialog(EXERCISE_LIST_DIALOG);
		    	  }else{
		    		  Toast.makeText(getApplicationContext(), "No workout selected!", Toast.LENGTH_SHORT).show();
		    	  }
		      }
		    });
		}
		
		if(createWorkout != null){
			createWorkout.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  showDialog(CREATE_WORKOUT_DIALOG);
		      }
		    });
		}
		
		if(removeWorkout != null){
			removeWorkout.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  deleteWorkoutDialogInvoker.invoke(REMOVE_WORKOUT_DIALOG);
		    	  showDialog(REMOVE_WORKOUT_DIALOG);
		      }
		    });
		}
		
		if(removeExercise != null){
			removeExercise.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  if(selectedWorkoutID > -1){
		    		  removeExerciseDialogInvoker.setSelectedWorkoutID(selectedWorkoutID);
				  	showDialog(REMOVE_EXERCISE_DIALOG);
		    	  }else{
		    		  Toast.makeText(getApplicationContext(), "No workout selected!", Toast.LENGTH_SHORT).show();
		    	  }
		      }
		    });
		}
		
		
		//Populate the listview and act on any interaction with it
		ListView workoutListView = listPopulator.populateWorkoutList();
		
		workoutListView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	        	
	        	//Get item in first colomn of clicked workout (ID)
	        	selectedWorkoutID = Integer.parseInt((String)((Cursor)parent.getItemAtPosition(position)).getString(0));
	        	
	        	EditWorkoutsDetailFragment fragment = (EditWorkoutsDetailFragment) getFragmentManager()
	                    .findFragmentById(R.id.editWorkoutsDetailFrag);
	                if (fragment != null && fragment.isInLayout()) {
	                	populateExerciseList(selectedWorkoutID);
	                	populateTextFields(selectedWorkoutID);
	                  
	                } else {
						Intent intent = new Intent(getApplicationContext(),
						EditWorkoutsDetailActivity.class);
						intent.putExtra("selectedWorkoutID", selectedWorkoutID);
						startActivity(intent);

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
	
	@Override
	  protected Dialog onCreateDialog(int id){
		if(id == EXERCISE_LIST_DIALOG)
			return addExerciseDialogInvoker.invoke(id);
		if(id == CREATE_WORKOUT_DIALOG)
			return createWorkoutDialogInvoker.invoke(id);
		if(id == REMOVE_WORKOUT_DIALOG)
			return deleteWorkoutDialogInvoker.invoke(id);
		if(id == REMOVE_EXERCISE_DIALOG)
			return removeExerciseDialogInvoker.invoke(id);
		return null;
	  }
	
	
	

}
