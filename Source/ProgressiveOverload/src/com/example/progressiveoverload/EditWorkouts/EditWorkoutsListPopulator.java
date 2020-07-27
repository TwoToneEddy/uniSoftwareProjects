package com.example.progressiveoverload.EditWorkouts;

import android.database.Cursor;
import android.widget.TextView;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.ContentProviders.WorkoutContentProvider;
import com.example.progressiveoverload.Utilities.*;
import android.app.Activity;

public class EditWorkoutsListPopulator extends DisplayPopulator{
	
	public EditWorkoutsListPopulator(Activity hostActivity){
		super(hostActivity);
		
	}
	
	
	@Override
	public void populateTextFields(int selectedWorkoutID){
		TextView dayTextView = (TextView) hostActivity.findViewById(R.id.dayField);
		TextView muscleGroupTextView = (TextView) hostActivity.findViewById(R.id.muscleGroupField);
		
		int dayCol,muscleCol;
		
		String selection = WorkoutContentProvider.KEY_WORKOUT_ID +" == ? ";
	    String[] selectionArgs = new String[] {Integer.toString(selectedWorkoutID)};
	    Cursor selectionCursor = myContentResolver.query(WorkoutContentProvider.CONTENT_URI,null, selection, selectionArgs, null);
	    
	    
		dayCol = selectionCursor.getColumnIndexOrThrow(WorkoutContentProvider.KEY_WORKOUT_DAY);
		muscleCol = selectionCursor.getColumnIndexOrThrow(WorkoutContentProvider.KEY_MUSCLE_GROUP);
		
		if(selectionCursor.moveToNext()){
			dayTextView.setText(selectionCursor.getString(dayCol));
			muscleGroupTextView.setText(selectionCursor.getString(muscleCol));
		}
		
		
	}

}
