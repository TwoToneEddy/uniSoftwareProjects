package com.example.progressiveoverload;

import android.content.ContentResolver;
import android.util.Log;

import com.example.progressiveoverload.ContentProviders.*;
import android.app.Activity;
import android.database.Cursor;

public class ContentProviderPrinter {
	Activity hostActivity;
	Cursor cursor;
	
	public ContentProviderPrinter(Activity hostActivity){
		this.hostActivity=hostActivity;
		
	}

	/*
	public void printProvider(){
		ContentResolver myContentResolver = hostActivity.getContentResolver();
		cursor = myContentResolver.query(ExerciseRecordContentProvider.CONTENT_URI,null, null, null,ExerciseRecordContentProvider.KEY_DATE + " DESC");
		Log.d("debugLee_2","Count Before = "+ cursor.getCount());
		
		int idCol = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_EXERCISE_RECORD_ID);
		Log.d("debugLee_2","idCol = "+idCol);
		int exIDCol = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_EXERCISE_ID);
		Log.d("debugLee_2","exIDCol = "+exIDCol);
		int dateCol = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_DATE);
		Log.d("debugLee_2","dateCol = "+dateCol);
		
		int set1Col = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_SET_A);
		Log.d("debugLee_2","set1Col = "+set1Col);
		int set2Col = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_SET_B);
		Log.d("debugLee_2","set2Col = "+set2Col);
		int set3Col = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_SET_C);
		Log.d("debugLee_2","set3Col = "+set3Col);
		int set4Col = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_SET_D);
		Log.d("debugLee_2","set4Col = "+set4Col);
		int set5Col = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_SET_E);
		Log.d("debugLee_2","set5Col = "+set5Col);
		
		int w1Col = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_WEIGHT_A);
		Log.d("debugLee_2","w1Col = "+w1Col);
		int w2Col = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_WEIGHT_B);
		Log.d("debugLee_2","w2Col = "+w2Col);
		int w3Col = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_WEIGHT_C);
		Log.d("debugLee_2","w3Col = "+w3Col);
		int w4Col = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_WEIGHT_D);
		Log.d("debugLee_2","w4Col = "+w4Col);
		int w5Col = cursor.getColumnIndexOrThrow(ExerciseRecordContentProvider.KEY_WEIGHT_E);
		Log.d("debugLee_2","w5Col = "+w5Col);
		
		Log.d("debugLee_2","Count Before = "+ cursor.getCount());
		
		while(cursor.moveToNext()){
			Log.d("debugLee_2","ID = "+cursor.getString(idCol)+", "+"Exercise = "+cursor.getString(exIDCol)+", "+"Date = "+cursor.getString(dateCol));
			Log.d("debugLee_2","Set1 = "+cursor.getString(set1Col)+", "+"Set2 = "+cursor.getString(set2Col)+", "+"Set3 = "+cursor.getString(set3Col)+", "
					+"Set4 = "+cursor.getString(set4Col)+", "+"Set5 = "+cursor.getString(set5Col));
			Log.d("debugLee_2","W1 = "+cursor.getString(w1Col)+", "+"W2 = "+cursor.getString(w2Col)+", "+"W3 = "+cursor.getString(w3Col)+", "
					+"W4 = "+cursor.getString(w4Col)+", "+"W5 = "+cursor.getString(w5Col));
		}
		
		Log.d("debugLee_2","Count = "+ cursor.getCount());
		
	}*/
	public void printProvider(){
		ContentResolver myContentResolver = hostActivity.getContentResolver();
		cursor = myContentResolver.query(WorkoutContentProvider.CONTENT_URI,null, null, null,null);
		
		int idCol = cursor.getColumnIndexOrThrow(WorkoutContentProvider.KEY_WORKOUT_ID);
		int dayCol = cursor.getColumnIndexOrThrow(WorkoutContentProvider.KEY_WORKOUT_DAY);
		int groupCol = cursor.getColumnIndexOrThrow(WorkoutContentProvider.KEY_MUSCLE_GROUP);
		int dateCol = cursor.getColumnIndexOrThrow(WorkoutContentProvider.KEY_DATE);
		
		
		Log.d("debugLee_2","Count Before = "+ cursor.getCount());
		
		while(cursor.moveToNext()){
			Log.d("debugLee_2","ID = "+cursor.getString(idCol)+", "+"Day = "+cursor.getString(dayCol)+", "+"Group = "+cursor.getString(groupCol));
			String dateTime = cursor.getString(dateCol);
			if(dateTime != null){
				Log.d("debugLee_2","Date = "+cursor.getString(dateCol));
				
			}
		}
		
		
	}
	
}
