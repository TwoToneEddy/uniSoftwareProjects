package com.example.progressiveoverload.Utilities;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.ContentProviders.*;
import com.example.progressiveoverload.Utilities.CursorSelector;
import com.example.progressiveoverload.Adapters.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.net.Uri;
import android.widget.*;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.widget.CursorAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ContentProviderAssistant {
	
	public ContentProviderAssistant(){
		
	}
	/**
	 * This method determines if an entry already exists in a content provider to avoid duplicate entries. If
	 * the string in the parameter "comparison" exists in the colomn specified by the string in the
	 * parameter "colomn" in the content provider specified by the parameter "contentProviderUri" the method returns
	 * true.
	 * 
	 * @param hostActivity	
	 * @param contentProviderUri	Content provider Uri in question
	 * @param colomn				The colomns for the comparison
	 * @param comparison			The values of the comparison
	 * @return						True if this entry already exists in the content provider
	 */
	public boolean alreadyExists(Activity hostActivity,Uri contentProviderUri, String[] colomn, String[] comparison){
		
		if(colomn.length==1){
			
			String selection = colomn[0] +" == ? ";
		    ContentResolver contentResolver = hostActivity.getContentResolver();
			Cursor cursor = contentResolver.query(contentProviderUri,null, selection, comparison, null);
			if(cursor.getCount()>0)
				return true;
			else
				return false;
		}else{
			String selection = "";
			
			for(int i = 0; i < colomn.length; i++ ){
				if(i == colomn.length-2)
					selection += colomn[i] + " ==? AND ";
				else
					selection += colomn[i] + " ==? ";
		    }
			
			ContentResolver contentResolver = hostActivity.getContentResolver();
			Cursor cursor = contentResolver.query(contentProviderUri,null, selection, comparison, null);
			if(cursor.getCount()>0)
				return true;
			else
				return false;
			
		}
		
	}
	
	public String getExerciseName(Activity hostActivity, int exerciseID){
		int titleCol;
		String result="";
		ContentResolver contentResolver = hostActivity.getContentResolver();
		String selection = ExerciseContentProvider.KEY_EXERCISE_ID +" == ? ";
	    String[] selectionArgs = new String[] {Integer.toString(exerciseID)};
	    Cursor selectionCursor = contentResolver.query(ExerciseContentProvider.CONTENT_URI,null, selection, selectionArgs, null);
	    titleCol = selectionCursor.getColumnIndexOrThrow(ExerciseContentProvider.KEY_EXERCISE_NAME);
	    
	    if(selectionCursor.moveToNext())
	    	result = selectionCursor.getString(titleCol);
	    
		return result;
	}
	
	public void loadDefaultWorkouts(Activity hostActivity){
		
		clearContentProvider(hostActivity,SchedulerContentProvider.CONTENT_URI);
		clearContentProvider(hostActivity,ExerciseContentProvider.CONTENT_URI);
		clearContentProvider(hostActivity,WorkoutContentProvider.CONTENT_URI);
		clearContentProvider(hostActivity,ExerciseRecordContentProvider.CONTENT_URI);
		
		addExercise(hostActivity,"Dumbbell Curls", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.dumbbell_curls));
		addExercise(hostActivity,"Barbell Curls", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.barbell_curls));
		addExercise(hostActivity,"Dumbbell Press", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.dumbell_press));
		addExercise(hostActivity,"Dumbbell Flys", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.dumbell_flys));
		addExercise(hostActivity,"Single Arm Row", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.dumbbell_rows));
		addExercise(hostActivity,"Inverse Flys", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.inverse_flys));
		addExercise(hostActivity,"Dumbbell Shrugs", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.dumbbell_shrug));
		addExercise(hostActivity,"Incline Press", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.incline_press));
		addExercise(hostActivity,"Chin Ups", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.chin_ups));
		addExercise(hostActivity,"Pull Ups", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.pull_ups));
		addExercise(hostActivity,"Dumbbell Squats", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.dumbbell_squats));
		addExercise(hostActivity,"Dumbbell Lunges", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.dumbbell_lunges));
		addExercise(hostActivity,"Dumbbell Toe Raises", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.dumbbell_toe_raises));
		addExercise(hostActivity,"Crunches", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.crunches));
		addExercise(hostActivity,"Tricep Kickbacks", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.tricep_kickback));
		addExercise(hostActivity,"Tricep Extensions", 30,Uri.parse("android.resource://com.example.progressiveoverload/" + R.drawable.tricep_extensions));
		

		addWorkout(hostActivity,"Monday", "Back and Biceps");
		addWorkout(hostActivity,"Tuesday", "Chest and Triceps");
		addWorkout(hostActivity,"Thursday", "Legs and Abs");
		addWorkout(hostActivity,"Friday", "All");
		
		addExerciseToWorkout(hostActivity,"Monday","Dumbbell Curls");
		addExerciseToWorkout(hostActivity,"Monday","Barbell Curls");
		addExerciseToWorkout(hostActivity,"Monday","Single Arm Row");
		addExerciseToWorkout(hostActivity,"Monday","Chin Ups");
		addExerciseToWorkout(hostActivity,"Monday","Dumbbell Shrugs");
		addExerciseToWorkout(hostActivity,"Monday","Inverse Flys");
		

		addExerciseToWorkout(hostActivity,"Tuesday","Dumbbell Press");
		addExerciseToWorkout(hostActivity,"Tuesday","Dumbbell Flys");
		addExerciseToWorkout(hostActivity,"Tuesday","Incline Press");
		addExerciseToWorkout(hostActivity,"Tuesday","Dumbbell Press");
		addExerciseToWorkout(hostActivity,"Tuesday","Tricep Kickbacks");
		addExerciseToWorkout(hostActivity,"Tuesday","Tricep Extensions");

		addExerciseToWorkout(hostActivity,"Thursday","Dumbbell Squats");
		addExerciseToWorkout(hostActivity,"Thursday","Dumbbell Lunges");
		addExerciseToWorkout(hostActivity,"Thursday","Dumbbell Toe Raises");
		addExerciseToWorkout(hostActivity,"Thursday","Crunches");
		addExerciseToWorkout(hostActivity,"Thursday","Pull Ups");
		
		addExerciseToWorkout(hostActivity,"Friday","Dumbbell Press");
		addExerciseToWorkout(hostActivity,"Friday","Dumbbell Flys");
		addExerciseToWorkout(hostActivity,"Friday","Chin Ups");
		addExerciseToWorkout(hostActivity,"Friday","Dumbbell Shrugs");
		addExerciseToWorkout(hostActivity,"Friday","Barbell Curls");
		
		
	}
	
	public void clearContentProvider(Activity hostActivity,Uri contentProviderUri){
		ContentResolver myContentResolver = hostActivity.getContentResolver();
		myContentResolver.delete(contentProviderUri,null,null);
		
	}
	
	
	public void addExercise(Activity hostActivity,String title, int rest, Uri imageUri){
		ContentResolver myContentResolver = hostActivity.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(ExerciseContentProvider.KEY_EXERCISE_NAME, title);
		values.put(ExerciseContentProvider.KEY_REST_PERIOD, rest);

		if(imageUri != null){
			values.put(ExerciseContentProvider.IMAGE_URI, imageUri.toString());
		}
		
		myContentResolver.insert(ExerciseContentProvider.CONTENT_URI, values);
	}
	
	public void addWorkout(Activity hostActivity,String day, String muscle){
		ContentResolver myContentResolver = hostActivity.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(WorkoutContentProvider.KEY_WORKOUT_DAY, day);
		values.put(WorkoutContentProvider.KEY_MUSCLE_GROUP, muscle);
		myContentResolver.insert(WorkoutContentProvider.CONTENT_URI, values);
	}
	
	public void addExerciseToWorkout(Activity hostActivity,String workoutDay,String exerciseName){
		
		int workoutID=-1,exerciseID=-1;
		ContentResolver myContentResolver = hostActivity.getContentResolver();
		
		String workoutSelection = WorkoutContentProvider.KEY_WORKOUT_DAY +" == ? ";
	    String[] workoutSelectionArgs = new String[] {workoutDay};
	    
	    Cursor workoutSelectionCursor = myContentResolver.query(WorkoutContentProvider.CONTENT_URI,null, workoutSelection, workoutSelectionArgs, null);
		
	    if(workoutSelectionCursor.moveToFirst()){
			workoutID = Integer.parseInt(workoutSelectionCursor.getString(0));
		}
	    
		String exerciseSelection = ExerciseContentProvider.KEY_EXERCISE_NAME +" == ? ";
	    String[] exerciseSelectionArgs = new String[] {exerciseName};
	    Cursor exerciseSelectionCursor = myContentResolver.query(ExerciseContentProvider.CONTENT_URI,null, exerciseSelection, exerciseSelectionArgs, null);
		if(exerciseSelectionCursor.moveToFirst()){
			exerciseID = Integer.parseInt(exerciseSelectionCursor.getString(0));
		}
		
		
		
		if((workoutID < 0)||(exerciseID<0)){
		}else{
			ContentValues values = new ContentValues();
			values.put(SchedulerContentProvider.KEY_WORKOUT_ID, workoutID);
			values.put(SchedulerContentProvider.KEY_EXERCISE_ID, exerciseID);
			myContentResolver.insert(SchedulerContentProvider.CONTENT_URI, values);
		}
	}

}
