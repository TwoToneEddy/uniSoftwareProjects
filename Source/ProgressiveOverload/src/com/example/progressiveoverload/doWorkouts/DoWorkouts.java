package com.example.progressiveoverload.DoWorkouts;




import com.example.progressiveoverload.R;
import com.example.progressiveoverload.EditWorkouts.EditWorkoutsDetailActivity;
import com.example.progressiveoverload.EditWorkouts.EditWorkoutsDetailFragment;
import com.example.progressiveoverload.EditWorkouts.EditWorkoutsListPopulator;
import com.example.progressiveoverload.R.id;
import com.example.progressiveoverload.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.text.format.*;
import android.media.MediaPlayer;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DoWorkouts extends Activity  {

	//Object to assist with list population
	DoWorkoutsListPopulator listPopulator;
	public ArrayList<Spinner> repsSpinners;
	public ArrayList<Spinner> weightSpinners;
	TextView restCounterText;
	RestCounter restCounter;
	
	int selectedWorkoutID,selectedExerciseID,restTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_do_workouts);
		
		//Lock to portrait if used on a small screen
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        
        
		selectedExerciseID = -1;
		restTime=-1;
		
		Intent intent = getIntent();
		selectedWorkoutID = intent.getIntExtra("selectedWorkoutID", -1);
		restCounter = new RestCounter(this,R.id.restCounterTextView,restTime);
		
		
		listPopulator = new DoWorkoutsListPopulator(this);
		listPopulator.populateTextFields(selectedExerciseID);
		ListView exerciseList = listPopulator.populateExerciseList(selectedWorkoutID);
		
		exerciseList.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	        	
	        	//Get item in first colomn of clicked workout (ID)
	        	selectedExerciseID = Integer.parseInt((String)((Cursor)parent.getItemAtPosition(position)).getString(0));
	        	restTime=listPopulator.getRestTime(selectedExerciseID);
	        	restCounter.setRestTime(restTime);
	        	DoWorkoutsDetailFragment fragment = (DoWorkoutsDetailFragment) getFragmentManager()
	                    .findFragmentById(R.id.doWorkoutsDetailFrag);
	                if (fragment != null && fragment.isInLayout()) {
	                	listPopulator.populateTextFields(selectedExerciseID);
	                } else {
	                	Intent intent = new Intent(getApplicationContext(),DoWorkoutsDetailActivity.class);
	  			  	  	intent.putExtra("selectedExerciseID", selectedExerciseID);
	  			  	  	intent.putExtra("selectedWorkoutID", selectedWorkoutID);
	  			  	  	startActivity(intent);
	                }
	        }
	    });
		
		setSpinners();
		listPopulator.setSpinnerArrays(repsSpinners, weightSpinners);
		Button saveButton = (Button) this.findViewById(R.id.saveButton);
		if(saveButton != null){
			saveButton.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  Log.d("debugLee_2",""+repsSpinners.get(0).getSelectedItem().toString());
		    	  listPopulator.saveRecordToDB(selectedExerciseID,selectedWorkoutID,repsSpinners,weightSpinners);
		      }
		    });
		}
		
		
		Button restButton = (Button) this.findViewById(R.id.restButton);
		if(restButton != null){
			restButton.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  restTime=listPopulator.getRestTime(selectedExerciseID);
		    	  restCounter.setRestText(restTime);
		    	  restCounter.stoptimertask();
		    	  restCounter.resetTime();
		    	  restCounter.startTimer();
		    	  
		      }
		    });
		}
		
		
		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		restCounter.stoptimertask();
  	  	restCounter.resetTime();
		
	}
	 
	
	public void setSpinners(){
		
		int[] repsSpinnerIds = {R.id.repsSpinner1,R.id.repsSpinner2,R.id.repsSpinner3,R.id.repsSpinner4,R.id.repsSpinner5};
		repsSpinners = listPopulator.setSpinners(repsSpinnerIds,R.array.reps_array);
		int[] weightSpinnerIds = {R.id.weightSpinner1,R.id.weightSpinner2,R.id.weightSpinner3,R.id.weightSpinner4,R.id.weightSpinner5};
		weightSpinners = listPopulator.setSpinners(weightSpinnerIds,R.array.weight_array);
		
	}
	

}
