package com.example.progressiveoverload.EditWorkouts;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.Dialogs.*;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

public class EditWorkoutsDetailActivity extends Activity {

	EditWorkoutsListPopulator listPopulator;
	static final int EXERCISE_LIST_DIALOG = 0;
	static final int REMOVE_EXERCISE_DIALOG = 3;
	
	EditWorkoutsListDialogInvoker addExerciseDialogInvoker,removeExerciseDialogInvoker;
	int selectedWorkoutID;
	Dialog addExerciseListDialog,removeExerciseListDialog;
	
	
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    //Lock to portrait if used on a small screen
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
	    
	    listPopulator = new EditWorkoutsListPopulator(this);
	    addExerciseListDialog=new Dialog(this);
	    removeExerciseListDialog = new Dialog(this);
	    
	    addExerciseListDialog.setOnDismissListener(new OnDismissListener(){
		     @Override
		     public void onDismiss(DialogInterface dialog) {
		    	 listPopulator.populateExerciseList(selectedWorkoutID);
		    	 removeExerciseDialogInvoker.invoke(REMOVE_EXERCISE_DIALOG);
		     }});
	    
	    removeExerciseListDialog.setOnDismissListener(new OnDismissListener(){
		     @Override
		     public void onDismiss(DialogInterface dialog) {
		    	 listPopulator.populateExerciseList(selectedWorkoutID);
		    	 removeExerciseDialogInvoker.invoke(REMOVE_EXERCISE_DIALOG);
		     }});
	    
		addExerciseDialogInvoker = new EditWorkoutsListDialogInvoker(this,R.layout.select_exercise_dialog,R.id.exercise_dialog_list,R.id.exercise_name_textview,"Custom Dialog",addExerciseListDialog);
		removeExerciseDialogInvoker = new EditWorkoutsListDialogInvoker(this,R.layout.select_exercise_dialog,R.id.exercise_dialog_list,R.id.exercise_name_textview,"Custom Dialog",removeExerciseListDialog);
	    
	    // Need to check if Activity has been switched to landscape mode
	    // If yes, finished and go back to the start Activity
	    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
	      finish();
	      return;
	    }
		
	    setContentView(R.layout.activity_edit_workouts_detail);
	    
	    Bundle extras = getIntent().getExtras();
	    if (extras != null) {
	     selectedWorkoutID=extras.getInt("selectedWorkoutID");
	    }
	    
	    Button addExercise = (Button) findViewById(R.id.addExerciseButton);
		Button removeExercise = (Button) this.findViewById(R.id.removeExerciseButton);
	    
	    addExercise.setOnClickListener(new View.OnClickListener() {
	      @Override
	      public void onClick(View v) {
	    	  if(selectedWorkoutID > -1){
	    		  addExerciseDialogInvoker.setSelectedWorkoutID(selectedWorkoutID);
			  	showDialog(EXERCISE_LIST_DIALOG);
	    	  }else{
	    		  Toast.makeText(getApplicationContext(), "No workout selected!", Toast.LENGTH_SHORT).show();
	    	  }
	      }
	    });
	    
	    if(removeExercise != null){
			removeExercise.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  if(selectedWorkoutID > -1){
		    		  removeExerciseDialogInvoker.setSelectedWorkoutID(selectedWorkoutID);
				  	showDialog(REMOVE_EXERCISE_DIALOG);
		    	  }else{
		    		  Toast.makeText(getApplicationContext(), "No workout selected!", Toast.LENGTH_SHORT).show();
		    	  }
		      }
		    });
		}
	    
	    listPopulator.populateExerciseList(selectedWorkoutID);
	    listPopulator.populateTextFields(selectedWorkoutID);
	    
	  }
	  
	  @Override
	  protected Dialog onCreateDialog(int id){
			if(id == EXERCISE_LIST_DIALOG)
				return addExerciseDialogInvoker.invoke(id);
			if(id == REMOVE_EXERCISE_DIALOG)
				return removeExerciseDialogInvoker.invoke(id);
			return null;
	  }
	  
	  
}
