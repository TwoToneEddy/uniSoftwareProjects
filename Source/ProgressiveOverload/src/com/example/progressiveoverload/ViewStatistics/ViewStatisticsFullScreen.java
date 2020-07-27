package com.example.progressiveoverload.ViewStatistics;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.R.id;
import com.example.progressiveoverload.R.layout;
import com.example.progressiveoverload.R.menu;
import com.jjoe64.graphview.GraphView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ViewStatisticsFullScreen extends Activity {

	ViewStatisticsListPopulator listPopulator;
	GraphView graph;
	int selectedExerciseID,plotType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_statistics_full_screen);
		
		listPopulator = new ViewStatisticsListPopulator(this);
		
		Intent intent = getIntent();
		selectedExerciseID = intent.getIntExtra("selectedExerciseID", 0);
		plotType = intent.getIntExtra("plotType", 0);
		
		listPopulator.populateGraph(selectedExerciseID, plotType);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

}
