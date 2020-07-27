package com.example.progressiveoverload.Utilities;

import java.io.InputStream;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.Adapters.CustomExerciseListAdapter;
import com.example.progressiveoverload.Adapters.CustomWorkoutListAdapter;
import com.example.progressiveoverload.ContentProviders.*;
import com.example.progressiveoverload.Utilities.BitmapHandler;
import com.example.progressiveoverload.Utilities.CursorSelector;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.*;
import android.text.format.*;
import android.util.Log;

/**
 * 
 * @author Lee hudson
 * 
 * This class is used to keep all code regarding updating the display in a single class. As the main application
 * uses fragments both the first and second activity will be responsible for populating the same views. Rather
 * than repeat the code in each activity it will be encapsulated here.
 *
 */
public class DisplayPopulator {

	protected ContentResolver myContentResolver;
	protected Activity hostActivity;
	protected CustomExerciseListAdapter exerciseListAdapter;
	protected CustomWorkoutListAdapter workoutListAdapter;
	protected BitmapHandler bitmapHandler;
	protected ContentProviderAssistant contentProviderAssistant;
	
	/**
	 * 
	 * @param hostActivity	The activity the DisplayPopulator is assisting
	 */
	public DisplayPopulator(Activity hostActivity){
		myContentResolver = hostActivity.getContentResolver();
		this.hostActivity=hostActivity;
		bitmapHandler = new BitmapHandler();
		contentProviderAssistant = new ContentProviderAssistant();
		
	}
	
	/**
	 * This method returns a listview populated with the exercises assigned to a particular
	 * workout via the parameter selectedWorkoutID.
	 * 
	 * @param selectedWorkoutID	ID of the workout
	 * @return					A listview populated with the exercises assigned to the workout
	 */
	public ListView populateExerciseList(int selectedWorkoutID){
		
		String selection = SchedulerContentProvider.KEY_WORKOUT_ID +" == ? ";
	    String[] selectionArgs = new String[] {Integer.toString(selectedWorkoutID)};
	    String[] colomnNames ={"_id",ExerciseContentProvider.KEY_EXERCISE_NAME};
	    Cursor selectionCursor = myContentResolver.query(SchedulerContentProvider.CONTENT_URI,null, selection, selectionArgs, SchedulerContentProvider.KEY_WORKOUT_ID+" DESC");
	    
	    CursorSelector exerciseCursorSelector = new CursorSelector(hostActivity,selectionCursor,ExerciseContentProvider.CONTENT_URI,colomnNames);
		exerciseCursorSelector.setColomns(SchedulerContentProvider.KEY_EXERCISE_ID);
	    MatrixCursor matrixCursor = exerciseCursorSelector.generateMatrixCursor();

	    exerciseListAdapter = new CustomExerciseListAdapter(hostActivity,matrixCursor);
		ListView exerciseListView = (ListView) hostActivity.findViewById(R.id.exerciseList);
		if(exerciseListView != null){
			  exerciseListView.setAdapter(exerciseListAdapter);
			  return exerciseListView;
		}else
			return null;
		
	}
	
	/**
	 * This method returns a listview populated with all exercises
	 * 
	 * @return	A listview populated with all exercises
	 */
	public ListView populateExerciseList(){
		
	    Cursor selectionCursor = myContentResolver.query(ExerciseContentProvider.CONTENT_URI,null, null, null, ExerciseContentProvider.KEY_EXERCISE_NAME+" ASC");
	    
	    exerciseListAdapter = new CustomExerciseListAdapter(hostActivity,selectionCursor);
		ListView exerciseListView = (ListView) hostActivity.findViewById(R.id.exerciseList);
		if(exerciseListView != null){
			exerciseListView.setAdapter(exerciseListAdapter);
			return exerciseListView;
		}
		return null;
	}
	
	/**
	 * This method returns a listview populated with all workouts
	 * 
	 * @return	ListView populated with all workouts in the content provider
	 */
	public ListView populateWorkoutList(){
		Cursor cursor = myContentResolver.query(WorkoutContentProvider.CONTENT_URI,null, null, null, WorkoutContentProvider.KEY_WORKOUT_DAY+" DESC");
		workoutListAdapter = new CustomWorkoutListAdapter(hostActivity,cursor);
		ListView workoutListView = (ListView)hostActivity.findViewById(R.id.workoutList);
		workoutListView.setAdapter(workoutListAdapter);
		return workoutListView;
	}
	
	/**
	 * To be implemented by subclasses
	 * @param id
	 */
	public void populateTextFields(int id){
		return;
	}
	
	/**
	 * Saves an exercise to the content provider
	 * 
	 * @param currentExerciseID		ID of exercise to be saved
	 * @param title					Title of exercise to be saved
	 * @param rest					Rest period in seconds of exercise to be saved
	 * @param photoUri				Photo Uri of exercise to be saved
	 */
	public void saveExerciseToDB(int currentExerciseID, String title, int rest, Uri photoUri){
		
		
		//If exercise ID < 0 no exercise has been selected so insert a new one
		if(currentExerciseID < 0){
			ContentValues values = new ContentValues();
			values.put(ExerciseContentProvider.KEY_EXERCISE_NAME, title);
			values.put(ExerciseContentProvider.KEY_REST_PERIOD, rest);
			if(photoUri != null){
				values.put(ExerciseContentProvider.IMAGE_URI, photoUri.toString());
			}
			
			myContentResolver.insert(ExerciseContentProvider.CONTENT_URI, values);
			
		}else{
			//If exercise ID >=0 exercise has been selected
			String oldImageUriString = null;
			Uri oldImageUri = null;
			
			ContentResolver myContentResolver = hostActivity.getContentResolver();
			String selection = ExerciseContentProvider.KEY_EXERCISE_ID +" == ? ";
		    String[] selectionArgs = new String[] {Integer.toString(currentExerciseID)};
		    Cursor selectionCursor = myContentResolver.query(ExerciseContentProvider.CONTENT_URI,null, selection, selectionArgs, null);
		    int uriCol = selectionCursor.getColumnIndexOrThrow(ExerciseContentProvider.IMAGE_URI);
		    
		    if(selectionCursor.moveToFirst()){
		    	oldImageUriString = selectionCursor.getString(uriCol);
		    	if(oldImageUriString!=null)
		    		oldImageUri = Uri.parse(oldImageUriString);
		    }
		    
			myContentResolver.delete(ExerciseContentProvider.CONTENT_URI,ExerciseContentProvider.KEY_EXERCISE_ID+"="+currentExerciseID, null);
			
			ContentValues values = new ContentValues();
			values.put(ExerciseContentProvider.KEY_EXERCISE_ID, currentExerciseID);
			values.put(ExerciseContentProvider.KEY_EXERCISE_NAME, title);
			values.put(ExerciseContentProvider.KEY_REST_PERIOD, rest);
			if(photoUri != null){
				values.put(ExerciseContentProvider.IMAGE_URI, photoUri.toString());
			}else{
				if(oldImageUri != null)
					values.put(ExerciseContentProvider.IMAGE_URI, oldImageUri.toString());
			}
			myContentResolver.insert(ExerciseContentProvider.CONTENT_URI, values);
			
		}
		
	}
	
	/**
	 * This method determines the Uri of the photo assigned to a given exercise and
	 * converts it to a bitmap. It then sets the photo of the given ImageView to display
	 * this bitmap.
	 * 
	 * @param photoIcon				ImageView to be set
	 * @param selectedExerciseID	ID of the exercise to get the image from
	 */
	public void setImageView(ImageView photoIcon, int selectedExerciseID){
		if(photoIcon == null)
			return;
		int uriCol;
		String selection = ExerciseContentProvider.KEY_EXERCISE_ID +" == ? ";
	    String[] selectionArgs = new String[] {Integer.toString(selectedExerciseID)};
	    Cursor selectionCursor = myContentResolver.query(ExerciseContentProvider.CONTENT_URI,null, selection, selectionArgs, null);
	    
	    uriCol = selectionCursor.getColumnIndexOrThrow(ExerciseContentProvider.IMAGE_URI);
	    if(selectionCursor.moveToNext()){
			String imageUriString = selectionCursor.getString(uriCol);
			
			if(imageUriString != null){

				Uri photoUri = Uri.parse(selectionCursor.getString(uriCol));
				Bitmap bitmap = bitmapHandler.generateBitmap(hostActivity, photoUri,R.integer.edit_exercise_photo_size,R.integer.edit_exercise_photo_size);
				if(photoIcon != null){
					photoIcon.setImageBitmap(bitmap);
				}
				
			}else{
				photoIcon.setImageResource(R.drawable.ic_launcher);
				
			}
		}
		
	}
	
	/**
	 * This method gets the current time and formats it in the  SQL DATETIME format
	 * so it can be inserted straight into the content provider.
	 * 
	 * @return		Current date and time in SQL DATETIME format
	 */
	public String getDateTime(){
		
		Time now = new Time();
		now.setToNow();
		int year,month,day,hour,min,sec;
		String yearS,monthS,dayS,hourS,minS,secS;
		
		year = now.year;
		month = (now.month)+1;
		day = now.monthDay;
		hour = now.hour;
		min = now.minute;
		sec = now.second;	
		
		yearS = Integer.toString(year);
		
		if(month < 10)
			monthS = "0"+Integer.toString(month);
		else
			monthS = Integer.toString(month);
		
		if(day < 10)
			dayS = "0"+Integer.toString(day);
		else
			dayS = Integer.toString(day);
		
		if(hour < 10)
			hourS = "0"+Integer.toString(hour);
		else
			hourS = Integer.toString(hour);
		
		if(min < 10)
			minS = "0"+Integer.toString(min);
		else
			minS = Integer.toString(min);
		
		if(sec < 10)
			secS = "0"+Integer.toString(sec);
		else
			secS = Integer.toString(sec);
		
		return ""+yearS+"-"+monthS+"-"+dayS+" "+hourS+":"+minS+":"+secS;
		
	}
	
	public static String stripNonDigits(final CharSequence input){
	    final StringBuilder sb = new StringBuilder(input.length());
	    for(int i = 0; i < input.length(); i++){
	        final char c = input.charAt(i);
	        if(c > 47 && c < 58){
	            sb.append(c);
	        }
	    }
	    return sb.toString();
	}
	
	public int getRestTime(int selectedExerciseID){
		if(selectedExerciseID<0)
			return 0;
		String selection = ExerciseContentProvider.KEY_EXERCISE_ID +" == ? ";
	    String[] selectionArgs = new String[] {Integer.toString(selectedExerciseID)};
	    Cursor selectionCursor = myContentResolver.query(ExerciseContentProvider.CONTENT_URI,null, selection, selectionArgs, null);
	    int restCol = selectionCursor.getColumnIndexOrThrow(ExerciseContentProvider.KEY_REST_PERIOD);
	    if(selectionCursor.moveToFirst())
	    	return Integer.parseInt(selectionCursor.getString(restCol));
	    else
	    	return 0;
	}
	
	public String getExerciseName(int selectedExerciseID){
		if(selectedExerciseID<0)
			return "No exercise selected";
		String selection = ExerciseContentProvider.KEY_EXERCISE_ID +" == ? ";
	    String[] selectionArgs = new String[] {Integer.toString(selectedExerciseID)};
	    Cursor selectionCursor = myContentResolver.query(ExerciseContentProvider.CONTENT_URI,null, selection, selectionArgs, null);
	    int nameCol = selectionCursor.getColumnIndexOrThrow(ExerciseContentProvider.KEY_EXERCISE_NAME);
	    if(selectionCursor.moveToFirst())
	    	return selectionCursor.getString(nameCol);
	    else
	    	return "No exercise selected";
		
	}
	
}
