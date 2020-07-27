package com.example.progressiveoverload.EditExercises;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.EditText;
import android.net.Uri;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.Adapters.CustomExerciseListAdapter;
import com.example.progressiveoverload.ContentProviders.ExerciseContentProvider;
import com.example.progressiveoverload.Utilities.*;
import android.app.Activity;

public class EditExercisesListPopulator extends DisplayPopulator {
	
	
	public EditExercisesListPopulator(Activity hostActivity){
		super(hostActivity);
		
	}

	@Override
	public void populateTextFields(int selectedExerciseID){
		EditText titleTextView = (EditText) hostActivity.findViewById(R.id.titleField);
		EditText restTextView = (EditText) hostActivity.findViewById(R.id.restGroupField);
		ImageView photoIcon = (ImageView) hostActivity.findViewById(R.id.exercisePhoto);
		int titleCol,restCol,uriCol;
		
		if(selectedExerciseID < 0){
			//No exercise selected so populate text fields with blanks
			if(titleTextView!=null)
				titleTextView.setText("");
			if(restTextView!=null)
				restTextView.setText("");
			if(photoIcon!=null)
				photoIcon.setImageResource(R.drawable.ic_launcher);
			
		}else{
			String selection = ExerciseContentProvider.KEY_EXERCISE_ID +" == ? ";
		    String[] selectionArgs = new String[] {Integer.toString(selectedExerciseID)};
		    Cursor selectionCursor = myContentResolver.query(ExerciseContentProvider.CONTENT_URI,null, selection, selectionArgs, null);
		    
		    
		    titleCol = selectionCursor.getColumnIndexOrThrow(ExerciseContentProvider.KEY_EXERCISE_NAME);
		    restCol = selectionCursor.getColumnIndexOrThrow(ExerciseContentProvider.KEY_REST_PERIOD);
		    uriCol = selectionCursor.getColumnIndexOrThrow(ExerciseContentProvider.IMAGE_URI);
			
			if(selectionCursor.moveToNext()){
				if(titleTextView!=null)
					titleTextView.setText(selectionCursor.getString(titleCol));
				if(restTextView!=null)
				restTextView.setText(selectionCursor.getString(restCol));
				String imageUriString = selectionCursor.getString(uriCol);
				
				if(imageUriString != null){
					Uri photoUri = Uri.parse(selectionCursor.getString(uriCol));
					//Bitmap bitmap = bitmapHandler.generateBitmap(hostActivity, photoUri,R.integer.edit_exercise_photo_size,R.integer.edit_exercise_photo_size);
					Bitmap bitmap = bitmapHandler.generateBitmap(hostActivity, photoUri,200,200);
					if(photoIcon != null){
						photoIcon.setImageBitmap(bitmap);
					}
					
				}else{
					photoIcon.setImageResource(R.drawable.ic_launcher);
					
				}
			}
			
		}	
	}

	
}
