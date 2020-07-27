package com.example.progressiveoverload;




import com.example.progressiveoverload.ContentProviders.SchedulerContentProvider;
import com.example.progressiveoverload.EditExercises.EditExercises;
import com.example.progressiveoverload.EditWorkouts.EditWorkouts;
import com.example.progressiveoverload.SelectWorkout.SelectWorkouts;
import com.example.progressiveoverload.ViewStatistics.ViewStatistics;
import com.example.progressiveoverload.Utilities.*;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class TitleActivity extends Activity {
	
	ContentProviderAssistant myAssistant;
	ContentProviderPrinter myPrinter;
	int defaultCounter;
	Timer timer;
	TimerTask timerTask;
	final Handler handler = new Handler();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultCounter = 0;
        
        myAssistant = new ContentProviderAssistant();
        myPrinter = new ContentProviderPrinter(this);
        
        //Lock to portrait if used on a small screen
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        
        setContentView(R.layout.activity_title);
    }
    
    
    public void selectWorkoutButtonPressed(View view){
    	Bundle selectWorkoutBundle = new Bundle();
    	
    	Intent intent = new Intent(this,SelectWorkouts.class);
    	//intent.putExtra("selectWorkoutBundle",startWorkoutBundle);
		startActivity(intent);
    	
    }
    
    public void editWorkoutsButtonPressed(View view){
    	Bundle editWorkoutsBundle = new Bundle();
    	Intent intent = new Intent(this,EditWorkouts.class);
		startActivity(intent);
    	
    }
    
    public void editExercisesButtonPressed(View view){
    	Bundle editExercisesBundle = new Bundle();
    	
    	Intent intent = new Intent(this,EditExercises.class);
    	//intent.putExtra("editExercisesBundle",editWorkoutsBundle);
		startActivity(intent);
    	
    }
    
    
    public void viewStatisticsButtonPressed(View view){
    	Bundle viewStatisticsBundle = new Bundle();
    	
    	Intent intent = new Intent(this,ViewStatistics.class);
		startActivity(intent);
    	
    }
    
    public void loadDefaultsButtonPressed(View view){
    	//myPrinter.printProvider();
    	stoptimertask();
    	if(defaultCounter==0){
    		startTimer();
    		Toast.makeText(this, "Careful, this will erase all data,\npress again within 2 seconds to continue", Toast.LENGTH_SHORT).show();
    		defaultCounter++;
    	}else{
    		myAssistant.loadDefaultWorkouts(this);
    		defaultCounter = 0;
    	}
    }
    
	public void startTimer() {
		//set a new Timer
		timer = new Timer();
		
		//initialize the TimerTask's job
		initializeTimerTask();
		
		//schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
		timer.schedule(timerTask, 2500, 2500); //
	}
	
	public void stoptimertask() {
		//stop the timer, if it's not already null
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
    
	public void initializeTimerTask() {
		
		timerTask = new TimerTask() {
			public void run() {
				
				//use a handler to run a toast that shows the current timestamp
				handler.post(new Runnable() {
					public void run() {
						defaultCounter = 0;
						stoptimertask();
					}
				});
			}
		};
	}
    

}
