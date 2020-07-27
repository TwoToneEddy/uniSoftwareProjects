package com.example.progressiveoverload.DoWorkouts;

import java.util.ArrayList;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.R.id;
import com.example.progressiveoverload.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.Intent;

public class DoWorkoutsDetailActivity extends Activity {
	DoWorkoutsListPopulator listPopulator;
	public ArrayList<Spinner> repsSpinners;
	public ArrayList<Spinner> weightSpinners;
	TextView restCounterText;
	RestCounter restCounter;

	int selectedExerciseID,restTime,selectedWorkoutID;
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	  //Lock to portrait if used on a small screen
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

	    // Need to check if Activity has been switched to landscape mode
	    // If yes, finished and go back to the start Activity
	    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
	      finish();
	      return;
	    }
	    setContentView(R.layout.activity_do_workouts_detail);

	    Intent intent = getIntent();
	    selectedExerciseID = intent.getIntExtra("selectedExerciseID", -1);
	    selectedWorkoutID = intent.getIntExtra("selectedWorkoutID", -1);
		
	    listPopulator = new DoWorkoutsListPopulator(this);
	    setSpinners();
		listPopulator.setSpinnerArrays(repsSpinners, weightSpinners);
		listPopulator.populateTextFields(selectedExerciseID);
		restTime=-1;
		restCounter = new RestCounter(this,R.id.restCounterTextView,restTime);
		restTime=listPopulator.getRestTime(selectedExerciseID);
    	restCounter.setRestTime(restTime);
    	
		
		Button saveButton = (Button) this.findViewById(R.id.saveButton);
		if(saveButton != null){
			saveButton.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
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
