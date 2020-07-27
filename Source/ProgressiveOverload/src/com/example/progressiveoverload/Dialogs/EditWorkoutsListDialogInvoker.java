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
public class EditWorkoutsListDialogInvoker {
	
	private Activity hostActivity;
	private int dialogLayout, listViewID,textViewID,selectedWorkoutID;
	private String dialogDesc;
	public ContentProviderAssistant contentProviderAssistant;
	Dialog dialog;
	static final int EXERCISE_LIST_DIALOG = 0;
	static final int CREATE_WORKOUT_DIALOG = 1;
	static final int REMOVE_WORKOUT_DIALOG = 2;
	static final int REMOVE_EXERCISE_DIALOG = 3;
        
	/**
         * @param hostActivity  The activity the dialog will be displayed in
         * @param dialogLayout  The ID for the dialog layout file
         * @param listViewID    The ID of the ListView within the dialog layout
         * @param textViewID    The title TextView of the row of the list
         * @param dialogDesc    Dialog title
         */
	public EditWorkoutsListDialogInvoker(Activity hostActivity, int dialogLayout,int listViewID,int textViewID, String dialogDesc, Dialog dialog){
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
			case EXERCISE_LIST_DIALOG:
				setParameters();

				//Prepare ListView in dialog
				ListView dialog_ListView = (ListView)dialog.findViewById(listViewID);
				  
				ContentResolver myContentResolver = hostActivity.getContentResolver();
				Cursor cursor = myContentResolver.query(ExerciseContentProvider.CONTENT_URI,null, null, null, ExerciseContentProvider.KEY_EXERCISE_NAME+" DESC");
				CustomExerciseListAdapter myAdapter;
				myAdapter = new CustomExerciseListAdapter(hostActivity,cursor);
				if(dialog_ListView != null)
					dialog_ListView.setAdapter(myAdapter);
		       
				dialog_ListView.setOnItemClickListener(new OnItemClickListener(){

		    	   @Override
		    	   public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						int resultID = Integer.parseInt((String)((Cursor)parent.getItemAtPosition(position)).getString(0));
						
						if(contentProviderAssistant.alreadyExists(hostActivity, SchedulerContentProvider.CONTENT_URI, 
								new String[]{SchedulerContentProvider.KEY_WORKOUT_ID, SchedulerContentProvider.KEY_EXERCISE_ID}, 
								new String[]{Integer.toString(selectedWorkoutID),Integer.toString(resultID)})){
							Toast.makeText(hostActivity.getApplicationContext(), "Exercise already exists for selected workout", Toast.LENGTH_SHORT).show();
						}else{
							ContentResolver myContentResolver = hostActivity.getContentResolver();
							Cursor cursor = myContentResolver.query(SchedulerContentProvider.CONTENT_URI,null, null, null, null);
							ContentValues values = new ContentValues();
							values.put(SchedulerContentProvider.KEY_WORKOUT_ID, selectedWorkoutID);
							values.put(SchedulerContentProvider.KEY_EXERCISE_ID, resultID);
							myContentResolver.insert(SchedulerContentProvider.CONTENT_URI, values);
						}
						
						hostActivity.dismissDialog(EXERCISE_LIST_DIALOG);
		    	   }});
				break;
		          
			case CREATE_WORKOUT_DIALOG:
				setParameters();

				Button createWorkoutButton = (Button)dialog.findViewById(R.id.createWorkoutCreateButton);
				Button cancelCreateWorkoutButton = (Button)dialog.findViewById(R.id.createWorkoutCancelButton);
				final Spinner createWorkoutDayIn = (Spinner)dialog.findViewById(R.id.createWorkoutDayField);
				final EditText createWorkoutGroupIn = (EditText)dialog.findViewById(R.id.createWorkoutMuscleGroupField);
			    
				if(createWorkoutDayIn != null){
			        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(hostActivity,
			        		R.array.days, android.R.layout.simple_spinner_item);
			        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        createWorkoutDayIn.setAdapter(adapter);
				}
			     
				cancelCreateWorkoutButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						hostActivity.dismissDialog(CREATE_WORKOUT_DIALOG);
					}
				});
			       
				createWorkoutButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
			 		    	
						String day = "";
						String muscle = "";
			 		    	  
						if(createWorkoutDayIn!=null)
							day =createWorkoutDayIn.getSelectedItem().toString(); 
			 		   	if(createWorkoutGroupIn!=null)
			 		   		muscle = createWorkoutGroupIn.getText().toString();
			 		    	 
			 		   	if((day.length()>0)&&(muscle.length()>0)){
			 		   		if(contentProviderAssistant.alreadyExists(hostActivity, WorkoutContentProvider.CONTENT_URI,new String[] {WorkoutContentProvider.KEY_WORKOUT_DAY},
			 		   				new String[]{day})){
				 		   		Toast.makeText(hostActivity.getApplicationContext(), "Workout already exists for that day", Toast.LENGTH_SHORT).show();
			 		   		}
			 		   		else{
								ContentResolver myContentResolver = hostActivity.getContentResolver();
								ContentValues values = new ContentValues();
								
								values.put(WorkoutContentProvider.KEY_WORKOUT_DAY, day);
								values.put(WorkoutContentProvider.KEY_MUSCLE_GROUP, muscle);
								myContentResolver.insert(WorkoutContentProvider.CONTENT_URI, values);
			 		   		}
			 		   	}else{
			 		   		Toast.makeText(hostActivity.getApplicationContext(), "Invalid Day or Muscle Group", Toast.LENGTH_SHORT).show();
			 		   	}
			 		   	hostActivity.dismissDialog(CREATE_WORKOUT_DIALOG);
					}
				});
			       
				break;
			          
			case REMOVE_WORKOUT_DIALOG:
				
				setParameters();
				   //Prepare ListView in dialog
				ListView workoutDialog_ListView = (ListView)dialog.findViewById(listViewID);
				  
				ContentResolver myContentResolver1 = hostActivity.getContentResolver();
				Cursor cursor1 = myContentResolver1.query(WorkoutContentProvider.CONTENT_URI,null, null, null, null);
				CustomWorkoutListAdapter myAdapter1;
				myAdapter1 = new CustomWorkoutListAdapter(hostActivity,cursor1);
				if(workoutDialog_ListView != null)
					workoutDialog_ListView.setAdapter(myAdapter1);
				workoutDialog_ListView.setOnItemClickListener(new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		    		   
						int resultID = Integer.parseInt((String)((Cursor)parent.getItemAtPosition(position)).getString(0));
						
						ContentResolver myContentResolver = hostActivity.getContentResolver();
						myContentResolver.delete(WorkoutContentProvider.CONTENT_URI,WorkoutContentProvider.KEY_WORKOUT_ID+"="+resultID, null);
						myContentResolver.delete(SchedulerContentProvider.CONTENT_URI,SchedulerContentProvider.KEY_WORKOUT_ID+"="+resultID, null);
						
						
						hostActivity.dismissDialog(REMOVE_WORKOUT_DIALOG);
					}});
		       
				break; 
			case REMOVE_EXERCISE_DIALOG:
				setParameters();

				//Prepare ListView in dialog
				ListView remove_dialog_ListView = (ListView)dialog.findViewById(listViewID);

				ContentResolver myContentResolver2 = hostActivity.getContentResolver();
				
				String selection = SchedulerContentProvider.KEY_WORKOUT_ID +" == ? ";
			    String[] selectionArgs = new String[] {Integer.toString(selectedWorkoutID)};
			    String[] colomnNames ={"_id",ExerciseContentProvider.KEY_EXERCISE_NAME};
			    Cursor selectionCursor = myContentResolver2.query(SchedulerContentProvider.CONTENT_URI,null, selection, selectionArgs, SchedulerContentProvider.KEY_WORKOUT_ID+" DESC");
				
			    CursorSelector exerciseCursorSelector = new CursorSelector(hostActivity,selectionCursor,ExerciseContentProvider.CONTENT_URI,colomnNames);
				exerciseCursorSelector.setColomns(SchedulerContentProvider.KEY_EXERCISE_ID);
			    MatrixCursor matrixCursor = exerciseCursorSelector.generateMatrixCursor();

			    CustomExerciseListAdapter exerciseListAdapter = new CustomExerciseListAdapter(hostActivity,matrixCursor);
			    
				if(remove_dialog_ListView != null)
					remove_dialog_ListView.setAdapter(exerciseListAdapter);
		       
				remove_dialog_ListView.setOnItemClickListener(new OnItemClickListener(){

		    	   @Override
		    	   public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						int resultID = Integer.parseInt((String)((Cursor)parent.getItemAtPosition(position)).getString(0));
						
						ContentResolver myContentResolver = hostActivity.getContentResolver();
						myContentResolver.delete(SchedulerContentProvider.CONTENT_URI,SchedulerContentProvider.KEY_EXERCISE_ID+"="+resultID, null);
						hostActivity.dismissDialog(REMOVE_EXERCISE_DIALOG);
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
