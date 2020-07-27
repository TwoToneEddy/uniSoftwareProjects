package com.example.progressiveoverload.ViewStatistics;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.R.id;
import com.example.progressiveoverload.R.layout;
import com.jjoe64.graphview.GraphView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.Intent;

public class ViewStatisticsDetailActivity extends Activity {

	ViewStatisticsListPopulator listPopulator;
	GraphView graph;
	int selectedExerciseID,plotType;

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
	    setContentView(R.layout.activity_view_statistics_detail);
	    
		listPopulator = new ViewStatisticsListPopulator(this);
		
		Intent intent = getIntent();
		selectedExerciseID = intent.getIntExtra("selectedExerciseID", -1);
		plotType = intent.getIntExtra("plotType", 0);
		listPopulator.populateGraph(selectedExerciseID, plotType);
		
		Button fullScreeButton = (Button) findViewById(R.id.viewStatisticsFSButton);
		if(fullScreeButton != null){
			fullScreeButton.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  Intent intent = new Intent(getApplicationContext(),
            			  ViewStatisticsFullScreen.class);
            	  intent.putExtra("selectedExerciseID", selectedExerciseID);
            	  intent.putExtra("plotType", plotType);
            	  startActivity(intent);
		    	 
		      }
		    });
		}
	    
	  }
}
