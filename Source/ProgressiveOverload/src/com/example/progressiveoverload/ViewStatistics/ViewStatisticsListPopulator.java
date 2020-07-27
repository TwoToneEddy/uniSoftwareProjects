package com.example.progressiveoverload.ViewStatistics;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.ContentProviders.*;
import com.example.progressiveoverload.Utilities.*;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import com.jjoe64.*;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.view.View;
import android.widget.TextView;

public class ViewStatisticsListPopulator extends DisplayPopulator {
	
	public ViewStatisticsListPopulator(Activity hostActivity){
		super(hostActivity);
	}
	
	/**
	 * 
	 * @param exerciseID	ID of the exercise to plot
	 * @param plotType		What to plot. 	0 = Average Weight
	 * 										1 = Average Reps
	 * 										2 = Work done ( sum(weight*reps))
	 * @return				GraphView displaying data
	 */
	public GraphView populateGraph(int exerciseID, int plotType){
		
		int noOfRecords,noOfSets; 
		int[][]  repsResult,weightResult;
		double[] resultData_1,resultData_2;
		DataPoint[] dataPoints;
		String legend="";
		
		GraphView graph = (GraphView) hostActivity.findViewById(R.id.graph);
		TextView exerciseTextView = (TextView) hostActivity.findViewById(R.id.selectedExerciseTextView);
		exerciseTextView.setText(contentProviderAssistant.getExerciseName(hostActivity, exerciseID));
		
		String[] preRepsColTitle = {ExerciseRecordContentProvider.KEY_SET_A,ExerciseRecordContentProvider.KEY_SET_B,
				ExerciseRecordContentProvider.KEY_SET_C,ExerciseRecordContentProvider.KEY_SET_D,
				ExerciseRecordContentProvider.KEY_SET_E,};
		String[] preWeightColTitle = {ExerciseRecordContentProvider.KEY_WEIGHT_A,ExerciseRecordContentProvider.KEY_WEIGHT_B,
				ExerciseRecordContentProvider.KEY_WEIGHT_C,ExerciseRecordContentProvider.KEY_WEIGHT_D,
				ExerciseRecordContentProvider.KEY_WEIGHT_E,};
		
		noOfSets = preRepsColTitle.length;
		int[] preRepsCols  = new int[preRepsColTitle.length];
	    int[] preWeightCols = new int[preWeightColTitle.length];
		
		String selection = ExerciseRecordContentProvider.KEY_EXERCISE_ID +" == ? ";
	    String[] selectionArgs = new String[] {Integer.toString(exerciseID)};
	    Cursor selectionCursor = myContentResolver.query(ExerciseRecordContentProvider.CONTENT_URI,null, selection, selectionArgs, ExerciseRecordContentProvider.KEY_DATE + " ASC");
		
	    for(int i = 0; i < preRepsColTitle.length; i++ ){
	    	preRepsCols[i] = selectionCursor.getColumnIndexOrThrow(preRepsColTitle[i]);
	    	preWeightCols[i] = selectionCursor.getColumnIndexOrThrow(preWeightColTitle[i]);
	    }
	    
	    noOfRecords = selectionCursor.getCount();
	    
	    dataPoints = new DataPoint[noOfRecords];
	    resultData_1 = new double[noOfRecords];
	    resultData_2 = new double[noOfRecords];
	   
	    repsResult = new int[noOfRecords][noOfSets];
	    weightResult = new int[noOfRecords][noOfSets];
	    
	    int counter = 0;
	    while(selectionCursor.moveToNext()){
	    	for(int j = 0; j < noOfSets; j++){
	    		repsResult[counter][j] = Integer.parseInt(selectionCursor.getString(preRepsCols[j]));
	    		weightResult[counter][j] = Integer.parseInt(selectionCursor.getString(preWeightCols[j]));
	    	}
	    	counter++;
	    }
	    
	    switch (plotType){
	    case 0:
	    	resultData_1 = getAverage(weightResult,noOfRecords,noOfSets);
	    	legend = "Average Weight (kgs)";
	    	break;
	    case 1:
	    	resultData_1 = getAverage(repsResult,noOfRecords,noOfSets);
	    	legend = "Average Reps";
	    	break;
	    case 2:
	    	resultData_1 = getAverage(weightResult,noOfRecords,noOfSets);
	    	resultData_2 = getAverage(repsResult,noOfRecords,noOfSets);
	    	legend = "Work Done";
	    	break;
	    default:
	    	break;		
	    }
	    /*
	    dataPoints = new DataPoint[resultData_1.length];
	    for(int i = 0; i < resultData_1.length; i++){
	    	if(resultData_1.length>0){
		    	if(plotType == 2 )
		    		dataPoints[i]=new DataPoint(i,resultData_1[i]*resultData_2[i]);
		    	else
		    		dataPoints[i]=new DataPoint(i,resultData_1[i]);
	    	}
	    }*/
	    
	    dataPoints = new DataPoint[resultData_1.length+1];
	    if(resultData_1.length==0){
	    	dataPoints[0]=new DataPoint(0,0);
	    }else{
		    for(int i = 0; i < resultData_1.length; i++){
		    	if(i==0){
		    		dataPoints[i]=new DataPoint(0,0);
		    	}
		    	if(resultData_1.length>0){
			    	if(plotType == 2 )
			    		dataPoints[i+1]=new DataPoint(i+1,resultData_1[i]*resultData_2[i]);
			    	else
			    		dataPoints[i+1]=new DataPoint(i+1,resultData_1[i]);
		    	}
		    }
	    }
	    
	    if(dataPoints.length>0){
	    	graph.removeAllSeries();
	    	LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
	    	series.setTitle(legend);
	    	graph.getLegendRenderer().setVisible(true);
	    	
	    	graph.addSeries(series);
	    }else{
	    	graph.removeAllSeries();
	    	LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(0,0)});
			graph.addSeries(series);
	    }
		
		return null;
	}
	
	/**
	 * This method returns a single dimension array from a 2d array where the value of each item in the single
	 * dimension  array is the average of the second dimension of the 2d array. This gets the average of a set of 5
	 * reps
	 * 
	 * @param inputArray
	 * @param outerSize
	 * @param innerSize
	 * @return
	 */
	public double[] getAverage(int[][] inputArray,int outerSize, int innerSize){
		
		double output[] = new double[outerSize];
    	for(int i = 0; i < outerSize; i++){
    		double sum = 0;
    		for(int j = 0; j < innerSize; j++){
    			sum = sum + inputArray[i][j];
    		}
    		output[i]= sum/innerSize;
    	}
		return output;
	}
	

}
