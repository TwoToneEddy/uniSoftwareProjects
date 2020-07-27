package com.example.progressiveoverload.DoWorkouts;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.ContentProviders.*;
import com.example.progressiveoverload.Utilities.*;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.widget.*;

import java.util.List;
import java.util.ArrayList;

public class DoWorkoutsListPopulator extends DisplayPopulator {
	public ArrayList<Spinner> repsSpinners;
	public ArrayList<Spinner> weightSpinners;
	
	public DoWorkoutsListPopulator(Activity hostActivity){
		super(hostActivity);
		
	}
	
	public void setSpinnerArrays(ArrayList<Spinner> repsSpinners,ArrayList<Spinner> weightSpinners){
		this.repsSpinners=repsSpinners;
		this.weightSpinners=weightSpinners;
		
	}
	
	@Override
	public void populateTextFields(int selectedExerciseID){
		
		ImageView photoIcon = (ImageView) hostActivity.findViewById(R.id.exercisePhoto);
		TextView previousDate = (TextView) hostActivity.findViewById(R.id.lastPerformedField);
		TextView exerciseTitle = (TextView) hostActivity.findViewById(R.id.exerciseTitleTextView);
		if(exerciseTitle!=null)
			exerciseTitle.setText(getExerciseName(selectedExerciseID));
		if(previousDate==null)
			return;
		//Create arraylists of all textviews to allow easy access later
		int[] preRepsTextID = {R.id.preRepsText1,R.id.preRepsText2,R.id.preRepsText3,R.id.preRepsText4,R.id.preRepsText5};
		ArrayList<TextView> preRepsTextViews = new ArrayList<TextView>();
		int[] preWeightTextID = {R.id.preWeightText1,R.id.preWeightText2,R.id.preWeightText3,R.id.preWeightText4,R.id.preWeightText5};
		ArrayList<TextView> preWeightTextViews = new ArrayList<TextView>();
		for(int id: preRepsTextID){
			TextView textView = (TextView) hostActivity.findViewById(id);
			if(textView != null)
				preRepsTextViews.add(textView);
		}
		for(int id: preWeightTextID){
			TextView textView = (TextView) hostActivity.findViewById(id);
			if(textView != null)
				preWeightTextViews.add(textView);
		}
		
		if(selectedExerciseID < 0){
			if(photoIcon != null)
				photoIcon.setImageResource(R.drawable.ic_launcher);
			
			for(TextView id: preRepsTextViews){
				if(id != null)
					id.setText("");
			}
			for(TextView id: preWeightTextViews){
				if(id != null)
					id.setText("");
			}
			previousDate.setText("");
		}else{
			setImageView(photoIcon, selectedExerciseID);
			populatePreviousResults(selectedExerciseID,previousDate,preRepsTextViews,preWeightTextViews);
			
			
		}
		
	}
	
	
	
	public void populatePreviousResults(int selectedExerciseID,TextView previousDate,ArrayList<TextView> preRepsTextViews,ArrayList<TextView> preWeightTextViews){
		
		
		String selection = ExerciseRecordContentProvider.KEY_EXERCISE_ID +" == ? ";
	    String[] selectionArgs = new String[] {Integer.toString(selectedExerciseID)};
	    Cursor selectionCursor = myContentResolver.query(ExerciseRecordContentProvider.CONTENT_URI,null, selection, selectionArgs, ExerciseRecordContentProvider.KEY_DATE + " DESC");
	    
	    String[] preRepsColTitle = {ExerciseRecordContentProvider.KEY_SET_A,ExerciseRecordContentProvider.KEY_SET_B,
	    							ExerciseRecordContentProvider.KEY_SET_C,ExerciseRecordContentProvider.KEY_SET_D,
	    							ExerciseRecordContentProvider.KEY_SET_E,};
	    String[] preWeightColTitle = {ExerciseRecordContentProvider.KEY_WEIGHT_A,ExerciseRecordContentProvider.KEY_WEIGHT_B,
									ExerciseRecordContentProvider.KEY_WEIGHT_C,ExerciseRecordContentProvider.KEY_WEIGHT_D,
									ExerciseRecordContentProvider.KEY_WEIGHT_E,};
	    int[] preRepsCols  = new int[preRepsTextViews.size()];
	    int[] preWeightCols = new int[preWeightTextViews.size()];
	    int dateCol;
	    
	    for(int i = 0; i < preRepsColTitle.length; i++ ){
	    	preRepsCols[i] = selectionCursor.getColumnIndexOrThrow(preRepsColTitle[i]);
	    	preWeightCols[i] = selectionCursor.getColumnIndexOrThrow(preWeightColTitle[i]);
	    }
	    dateCol = selectionCursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_DATE);
	    //Check this exercise has been performed before
	    if(selectionCursor.getCount() > 0){
	    	if(selectionCursor.moveToFirst()){
	    		for(int j = 0; j < preRepsTextViews.size(); j++ ){
	    			preRepsTextViews.get(j).setText(selectionCursor.getString(preRepsCols[j]));
	    			repsSpinners.get(j).setSelection(getIndex(repsSpinners.get(j),selectionCursor.getString(preRepsCols[j])));
	    			
	    			preWeightTextViews.get(j).setText(selectionCursor.getString(preWeightCols[j])+"kgs");
	    			weightSpinners.get(j).setSelection(getIndex(weightSpinners.get(j),selectionCursor.getString(preWeightCols[j])+"kgs"));
	    			
	    		}
	    		if(previousDate!=null)
	    			previousDate.setText(selectionCursor.getString(dateCol));
	    	}
	    }else{
	    	for(TextView id: preRepsTextViews){
				id.setText("");
			}
	    	
	    	for(Spinner sp: repsSpinners){
	    		sp.setSelection(0);
	    	}
			for(TextView id: preWeightTextViews){
				id.setText("");
			}
			
			for(Spinner sp: weightSpinners){
				sp.setSelection(0);
			}
			if(previousDate!=null)
				previousDate.setText("");
	    	return;
	    }
	}
	
	private int getIndex(Spinner spinner, String myString){
	  int index = 0;

  		for (int i=0;i<spinner.getCount();i++){
  			if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
  				index = i;
  				break;
  			}
  		}
  		return index;
	 } 
	
	public void saveRecordToDB(int selectedExerciseID,int selectedWorkoutID,List<Spinner> repsSpinners, List<Spinner> weightSpinners){
		
		String dateTime = getDateTime();
		ContentValues values = new ContentValues();
		values.put(ExerciseRecordContentProvider.KEY_EXERCISE_ID, selectedExerciseID);
		values.put(ExerciseRecordContentProvider.KEY_DATE, dateTime);
		
		values.put(ExerciseRecordContentProvider.KEY_SET_A, stripNonDigits(repsSpinners.get(0).getSelectedItem().toString()));
		values.put(ExerciseRecordContentProvider.KEY_SET_B, stripNonDigits(repsSpinners.get(1).getSelectedItem().toString()));
		values.put(ExerciseRecordContentProvider.KEY_SET_C, stripNonDigits(repsSpinners.get(2).getSelectedItem().toString()));
		values.put(ExerciseRecordContentProvider.KEY_SET_D, stripNonDigits(repsSpinners.get(3).getSelectedItem().toString()));
		values.put(ExerciseRecordContentProvider.KEY_SET_E, stripNonDigits(repsSpinners.get(4).getSelectedItem().toString()));
		
		values.put(ExerciseRecordContentProvider.KEY_WEIGHT_A, stripNonDigits(weightSpinners.get(0).getSelectedItem().toString()));
		values.put(ExerciseRecordContentProvider.KEY_WEIGHT_B, stripNonDigits(weightSpinners.get(1).getSelectedItem().toString()));
		values.put(ExerciseRecordContentProvider.KEY_WEIGHT_C, stripNonDigits(weightSpinners.get(2).getSelectedItem().toString()));
		values.put(ExerciseRecordContentProvider.KEY_WEIGHT_D, stripNonDigits(weightSpinners.get(3).getSelectedItem().toString()));
		values.put(ExerciseRecordContentProvider.KEY_WEIGHT_E, stripNonDigits(weightSpinners.get(4).getSelectedItem().toString()));
		
		myContentResolver.insert(ExerciseRecordContentProvider.CONTENT_URI, values);
		
		ContentValues date = new ContentValues();
		date.put(WorkoutContentProvider.KEY_DATE, dateTime);
		
		String selection = WorkoutContentProvider.KEY_WORKOUT_ID +" == ? ";
	    String[] selectionArgs = new String[] {Integer.toString(selectedWorkoutID)};
		myContentResolver.update(WorkoutContentProvider.CONTENT_URI, date, selection, selectionArgs);
		populateTextFields(selectedExerciseID);
		
	}
	
	
	
	
	
	public ArrayList<Spinner> setSpinners(int[] repsSpinnerIds,int values){
		ArrayList<Spinner> spinnerArrayList;
		spinnerArrayList = new ArrayList<Spinner>();
		
		for(int id : repsSpinnerIds) {
	        Spinner spinner = (Spinner)hostActivity.findViewById(id);
	        if(spinner != null){
		        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(hostActivity,
		        		values, R.layout.spinner_layout);
		        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        spinner.setAdapter(adapter);
		        
		        spinnerArrayList.add(spinner);
	        }
	    }
		return spinnerArrayList;
		
	}

}
/*
String compareValue= "some value";
    ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.select_state, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    MySpinner.setAdapter(adapter);
    if (!compareValue.equals(null)) {
        int spinnerPostion = adapter.getPosition(compareValue);
        MySpinner.setSelection(spinnerPostion);
        spinnerPostion = 0;
    }*/