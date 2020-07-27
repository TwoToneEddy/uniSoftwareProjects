package com.example.progressiveoverload.Dialogs;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.ContentProviders.*;
import com.example.progressiveoverload.Utilities.*;
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

/*
This class is a specialised class for creating a dialog for selecting from
the list of existing exercises. This is used by the EditWorkouts activites
*/
public class EditExercisesListDialogInvoker {
	
	private Activity hostActivity;
	private int dialogLayout, listViewID,textViewID,selectedWorkoutID;
	private String dialogDesc;
	public ContentProviderAssistant contentProviderAssistant;
	Dialog dialog;
	static final int REMOVE_EXERCISE_DIALOG = 0;
	static final int CREATE_WORKOUT_DIALOG = 1;
	static final int REMOVE_WORKOUT_DIALOG = 2;
	static final int EDIT_EXERCISE_DIALOG = 3;
        
	/**
         * @param hostActivity  The activity the dialog will be displayed in
         * @param dialogLayout  The ID for the dialog layout file
         * @param listViewID    The ID of the ListView within the dialog layout
         * @param textViewID    The title TextView of the row of the list
         * @param dialogDesc    Dialog title
         */
	public EditExercisesListDialogInvoker(Activity hostActivity, int dialogLayout,int listViewID,int textViewID, String dialogDesc, Dialog dialog){
		this.hostActivity=hostActivity;
		this.dialogLayout=dialogLayout;
		this.listViewID=listViewID;
		this.dialogDesc=dialogDesc;
		this.textViewID=textViewID;
		this.dialog=dialog;
		selectedWorkoutID = -2;
		contentProviderAssistant = new ContentProviderAssistant();
	}
	
	public Dialog invoke(int id) {

		switch(id) {
			case REMOVE_EXERCISE_DIALOG:
				setParameters();

				//Prepare ListView in dialog
				ListView remove_dialog_ListView = (ListView)dialog.findViewById(listViewID);

				ContentResolver myContentResolver2 = hostActivity.getContentResolver();
				
			    Cursor selectionCursor = myContentResolver2.query(ExerciseContentProvider.CONTENT_URI,null, null, null, ExerciseContentProvider.KEY_EXERCISE_NAME+" ASC");

			    CustomExerciseListAdapter exerciseListAdapter = new CustomExerciseListAdapter(hostActivity,selectionCursor);
			    
				if(remove_dialog_ListView != null)
					remove_dialog_ListView.setAdapter(exerciseListAdapter);
		       
				remove_dialog_ListView.setOnItemClickListener(new OnItemClickListener(){

		    	   @Override
		    	   public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						int resultID = Integer.parseInt((String)((Cursor)parent.getItemAtPosition(position)).getString(0));
						
						/*
						 * Must remove from scheduler too!!
						 */
						
						ContentResolver myContentResolver = hostActivity.getContentResolver();
						myContentResolver.delete(ExerciseContentProvider.CONTENT_URI,ExerciseContentProvider.KEY_EXERCISE_ID+"="+resultID, null);
						myContentResolver.delete(ExerciseRecordContentProvider.CONTENT_URI,ExerciseRecordContentProvider.KEY_EXERCISE_ID+"="+resultID, null);
						myContentResolver.delete(SchedulerContentProvider.CONTENT_URI,SchedulerContentProvider.KEY_EXERCISE_ID+"="+resultID, null);
						hostActivity.dismissDialog(REMOVE_EXERCISE_DIALOG);
						Log.d("debugLee_2","Exercise Deleted");
		    	   }});
				break;
		          
		}
		   
		return dialog;
	}
	public void setParameters(){
		dialog.setContentView(dialogLayout);
		dialog.setTitle(dialogDesc);
       
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
	}
	
	public void setSelectedWorkoutID(int workoutID){
		selectedWorkoutID=workoutID;
	}
	
	
}