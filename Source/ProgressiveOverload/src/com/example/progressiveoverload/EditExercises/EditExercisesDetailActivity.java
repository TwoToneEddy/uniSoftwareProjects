package com.example.progressiveoverload.EditExercises;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.ContentProviders.ExerciseContentProvider;
import com.example.progressiveoverload.EditWorkouts.EditWorkoutsListPopulator;
import com.example.progressiveoverload.R.id;
import com.example.progressiveoverload.R.layout;
import com.example.progressiveoverload.Utilities.*;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;

public class EditExercisesDetailActivity extends Activity {

	int selectedExerciseID =  -2;
	static final int SELECT_PICTURE = 1;
	public static final int KITKAT_VALUE = 1002;
	
	Uri selectedImageUri = null;
	BitmapHandler bitmapHandler;
	EditExercisesListPopulator listPopulator;
	ContentProviderAssistant contentProviderAssistant;

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    //Lock to portrait if used on a small screen
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        
	    listPopulator = new EditExercisesListPopulator(this);
	    bitmapHandler = new BitmapHandler();
		contentProviderAssistant = new ContentProviderAssistant();
	    
	    Bundle extras = getIntent().getExtras();
	    if (extras != null) {
	    	selectedExerciseID=extras.getInt("selectedExerciseID");
	    }

	    // Need to check if Activity has been switched to landscape mode
	    // If yes, finished and go back to the start Activity
	    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
	      finish();
	      return;
	    }
	    setContentView(R.layout.activity_edit_exercises_detail);
	    
	    listPopulator.populateTextFields(selectedExerciseID);
	    

		Button saveExercise = (Button) this.findViewById(R.id.saveExerciseButton);
		Button selectPhoto = (Button) this.findViewById(R.id.selectExercisePhotoButton);
		
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
