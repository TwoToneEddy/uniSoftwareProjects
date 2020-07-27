package com.example.progressiveoverload.ViewStatistics;




import com.example.progressiveoverload.R;
import com.example.progressiveoverload.ContentProviders.WorkoutContentProvider;
import com.example.progressiveoverload.EditExercises.EditExercisesDetailActivity;
import com.example.progressiveoverload.EditExercises.EditExercisesDetailFragment;
import com.example.progressiveoverload.R.id;
import com.example.progressiveoverload.R.layout;
import com.example.progressiveoverload.SelectWorkout.SelectWorkoutsDetailFragment;
import com.example.progressiveoverload.Utilities.ContentProviderAssistant;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;



import com.jjoe64.*;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ViewStatistics extends Activity {
	
	final static int VIEW_WEIGHT = 0;
	final static int VIEW_REPS = 1;
	final static int VIEW_WORK = 2;
	final static int weightRadioID = R.id.weightRadio;
	final static int repsRadioID = R.id.repsRadio;
	final static int workRadioID = R.id.workRadio;
	
	ViewStatisticsListPopulator listPopulator;
	GraphView graph;
	int selectedExerciseID,plotType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_statistics);
		
		//Lock to portrait if used on a small screen
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
		
		selectedExerciseID=-1;
		
		listPopulator = new ViewStatisticsListPopulator(this);
		
		final RadioGroup plotTypeRadio = (RadioGroup) findViewById(R.id.plotTypeRadioGroup); 
		plotTypeRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            	switch (checkedId){
	        	case weightRadioID: plotType = VIEW_WEIGHT;
	        		break;
	        	case repsRadioID: plotType = VIEW_REPS;
	        		break;
	        	case workRadioID: plotType = VIEW_WORK;
	        		break;
	        	default: weightRadioID: plotType = VIEW_WEIGHT;
	        			break;
	        	}
            	
            	if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
	            	ViewStatisticsDetailFragment fragment = (ViewStatisticsDetailFragment) getFragmentManager()
			    			  .findFragmentById(R.id.viewStatisticsDetailFrag);
	            	if (fragment != null && fragment.isInLayout()){
		            	  listPopulator.populateGraph(selectedExerciseID, plotType);
		              }else{
		            	  Intent intent = new Intent(getApplicationContext(),
		            			  ViewStatisticsDetailActivity.class);
		            	  intent.putExtra("selectedExerciseID", selectedExerciseID);
		            	  intent.putExtra("plotType", plotType);
		            	  startActivity(intent);
		            	  
		              }
            	}
            	

            }

        });
		
		ListView exerciseList =   listPopulator.populateExerciseList();
		
		exerciseList.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	        	selectedExerciseID = Integer.parseInt((String)((Cursor)parent.getItemAtPosition(position)).getString(0));
	        	
	        	switch (plotTypeRadio.getCheckedRadioButtonId()){
	        	case weightRadioID: plotType = VIEW_WEIGHT;
	        		break;
	        	case repsRadioID: plotType = VIEW_REPS;
	        		break;
	        	case workRadioID: plotType = VIEW_WORK;
	        		break;
	        	default: weightRadioID: plotType = VIEW_WEIGHT;
	        			break;
	        	}
	        	
		    	ViewStatisticsDetailFragment fragment = (ViewStatisticsDetailFragment) getFragmentManager()
		    			  .findFragmentById(R.id.viewStatisticsDetailFrag);
		              if (fragment != null && fragment.isInLayout()){
		            	  listPopulator.populateGraph(selectedExerciseID, plotType);
		              }else{
		            	  Intent intent = new Intent(getApplicationContext(),
		            			  ViewStatisticsDetailActivity.class);
		            	  intent.putExtra("selectedExerciseID", selectedExerciseID);
		            	  intent.putExtra("plotType", plotType);
		            	  startActivity(intent);
		            	  
		              }
	        	
	        }
	    });
		
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
