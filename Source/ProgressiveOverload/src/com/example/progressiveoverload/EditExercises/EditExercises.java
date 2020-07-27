package com.example.progressiveoverload.EditExercises;


import java.io.FileNotFoundException;
import java.io.IOException;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.EditWorkouts.EditWorkoutsDetailActivity;
import com.example.progressiveoverload.ContentProviders.ExerciseContentProvider;
import com.example.progressiveoverload.Dialogs.EditExercisesListDialogInvoker;
import com.example.progressiveoverload.Dialogs.EditWorkoutsListDialogInvoker;
import com.example.progressiveoverload.EditWorkouts.EditWorkoutsDetailFragment;
import com.example.progressiveoverload.EditWorkouts.EditWorkoutsListPopulator;
import com.example.progressiveoverload.R.id;
import com.example.progressiveoverload.R.layout;
import com.example.progressiveoverload.Utilities.*;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.provider.MediaStore;
import android.graphics.Bitmap;

public class EditExercises extends Activity {

	int selectedExerciseID = -1;
	static final int REMOVE_EXERCISE_DIALOG = 0;
	static final int SELECT_PICTURE = 1;
	
	Uri selectedImageUri = null;
	BitmapHandler bitmapHandler;
	EditExercisesListPopulator listPopulator;
	EditExercisesListDialogInvoker removeExerciseDialogInvoker;
	Dialog removeExerciseListDialog;
	ContentProviderAssistant contentProviderAssistant;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_exercises);
		
		//Lock to portrait if used on a small screen
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
		
		listPopulator = new EditExercisesListPopulator(this);
		
		bitmapHandler = new BitmapHandler();
		contentProviderAssistant = new ContentProviderAssistant();
		
		removeExerciseListDialog = new Dialog(this);
		removeExerciseDialogInvoker = new EditExercisesListDialogInvoker(this,R.layout.select_exercise_dialog,
				R.id.exercise_dialog_list,R.id.exercise_name_textview,"Custom Dialog",removeExerciseListDialog);
		
		removeExerciseListDialog.setOnDismissListener(new OnDismissListener(){
		     @Override
		     public void onDismiss(DialogInterface dialog) {
		    	 listPopulator.populateExerciseList();
		    	 populateTextFields(-1);
		    	 removeExerciseDialogInvoker.invoke(REMOVE_EXERCISE_DIALOG);
		     }});

	
		ListView exerciseListView = listPopulator.populateExerciseList();
		exerciseListView.setOnItemClickListener(new OnItemClickListener() {
			
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	        	selectedExerciseID = Integer.parseInt((String)((Cursor)parent.getItemAtPosition(position)).getString(0));
	        	EditExercisesDetailFragment fragment = (EditExercisesDetailFragment) getFragmentManager()
	                    .findFragmentById(R.id.editExercisesDetailFrag);
	        	if (fragment != null && fragment.isInLayout()) {
                	populateTextFields(selectedExerciseID);
                  
                } else {
					Intent intent = new Intent(getApplicationContext(),
					EditExercisesDetailActivity.class);
					intent.putExtra("selectedExerciseID", selectedExerciseID);
					startActivity(intent);

                      }
	        	
	        }
	    });
		
		EditExercisesDetailFragment fragment = (EditExercisesDetailFragment) getFragmentManager()
                .findFragmentById(R.id.editExercisesDetailFrag);
    	if (fragment != null && fragment.isInLayout()) 
        	populateTextFields(selectedExerciseID);

		Button removeExercise = (Button) this.findViewById(R.id.removeExerciseButton);
		Button createExercise = (Button) this.findViewById(R.id.createExerciseButton);
		Button saveExercise = (Button) this.findViewById(R.id.saveExerciseButton);
		Button selectPhoto = (Button) this.findViewById(R.id.selectExercisePhotoButton);
		
		
		if(removeExercise != null){
			removeExercise.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
				  	showDialog(REMOVE_EXERCISE_DIALOG);
		    	 
		      }
		    });
		}
		
		if(createExercise != null){
			createExercise.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  EditExercisesDetailFragment fragment = (EditExercisesDetailFragment) getFragmentManager()
		    			  .findFragmentById(R.id.editExercisesDetailFrag);
		              if (fragment != null && fragment.isInLayout()){
		            	  populateTextFields(-1);
		            	  selectedExerciseID=-1;
		            	  selectedImageUri=null;
		            	 
		              }else{
		            	  Intent intent = new Intent(getApplicationContext(),
		            			  EditExercisesDetailActivity.class);
		            	  intent.putExtra("selectedExerciseID", -1);
		            	  startActivity(intent);
		              }
		    	 
		      }
		    });
		}
		
		
		if(saveExercise != null){
			saveExercise.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  EditText titleTextView = (EditText) findViewById(R.id.titleField);
		    	  EditText restTextView = (EditText) findViewById(R.id.restGroupField);
		    	  
		    	  String title = titleTextView.getText().toString();
		    	  int restPeriod;
		    	  
		    	  try {
		    		  restPeriod = Integer.parseInt(restTextView.getText().toString());
		    		} catch (NumberFormatException e) {
		    		    restPeriod = 30;
		    		}
		    	  
		    	  if((titleTextView != null)&&(restTextView!=null)){
		    		  if(contentProviderAssistantAlreadyExistsInterface(ExerciseContentProvider.CONTENT_URI,
		    				  new String[]{ExerciseContentProvider.KEY_EXERCISE_NAME},new String[]{title})&&(selectedExerciseID<0)){
		    			  Toast.makeText(getApplicationContext(), "Exercise already exists", Toast.LENGTH_SHORT).show();
		    		  }else
		    			  listPopulator.saveExerciseToDB(selectedExerciseID,title, restPeriod, selectedImageUri);
		    		  
		    	  }
		      }
		    });
		}
		
		if(selectPhoto != null){
			selectPhoto.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  Intent intent;
		    	  if(android.os.Build.VERSION.SDK_INT < 19){
		    		  intent = new Intent();
	                  intent.setType("image/*");
	                  intent.setAction(Intent.ACTION_GET_CONTENT);
		    	  }else{
		    		  intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			    	  intent.addCategory(Intent.CATEGORY_OPENABLE);
			    	  intent.setType("image/*");
		    	  }
		    	  
                  startActivityForResult(Intent.createChooser(intent,
                          "Select Picture"), SELECT_PICTURE);
		    	 
		      }
		    });
		}
		
	}
	
	//This is needed as the ContentProviderAssistant needs an activity as a parameter
	public boolean contentProviderAssistantAlreadyExistsInterface(Uri contentProviderUri, String[] colomn, String[] comparison){
		return contentProviderAssistant.alreadyExists(this, contentProviderUri,colomn,comparison);
	}
	
	public void populateTextFields(int selectedExerciseID){
		listPopulator.populateTextFields(selectedExerciseID);
		
	}
	
	@Override
	  protected Dialog onCreateDialog(int id){
		if(id == REMOVE_EXERCISE_DIALOG)
			return removeExerciseDialogInvoker.invoke(id);
		return null;
	  }
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                
                Bitmap bitmap = bitmapHandler.generateBitmap(this, selectedImageUri,R.integer.edit_exercise_photo_size,R.integer.edit_exercise_photo_size);
                ImageView photoIcon = (ImageView) this.findViewById(R.id.exercisePhoto);
				if(photoIcon != null){
					photoIcon.setImageBitmap(bitmap);
                
				}
            }
        }
	
	}
}
