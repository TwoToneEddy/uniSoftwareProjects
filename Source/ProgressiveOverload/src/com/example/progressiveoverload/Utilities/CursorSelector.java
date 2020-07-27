package com.example.progressiveoverload.Utilities;

import com.example.progressiveoverload.ContentProviders.ExerciseContentProvider;

import android.net.*;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.res.Configuration;
import android.database.*;
import android.widget.CursorAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
/*
 * Selects everything from cursor2 that is found in cursor 1 and returns a matrix cursor
 */

public class CursorSelector {
  
	
	Activity hostActivity;
	ContentResolver contentResolver;
	Cursor cursor1;
	Cursor cursor2; //Cursor for body of data to be selected from
	Uri cursor2Uri;
	
	int cursor1KeyCol;
	int [] cursor2Colomns;
	
	String[] outputColomnNames;
	
	
	/**
	 * 
	 * @param hostActivity	The activity that will use the cursor
	 * @param cursor1		The cursor determine what must be in found in the second cursor. (must have 1 colomn)
	 * @param cursor2Uri	The URI of the cursor that is to be selected from.
	 * @param outputColomnNames	The output colomns of the returned marix cursor
	 */
	public CursorSelector(Activity hostActivity,Cursor cursor1, Uri cursor2Uri, String[] outputColomnNames){
		this.hostActivity=hostActivity;
		contentResolver = hostActivity.getContentResolver();
		this.cursor1=cursor1;
		this.cursor2Uri=cursor2Uri;
		this.outputColomnNames=outputColomnNames;
		cursor2 = contentResolver.query(cursor2Uri,null, null, null, null);
		
	}
	
	public void setColomns(String cursor1KeyColName ){
		cursor1KeyCol = cursor1.getColumnIndexOrThrow(cursor1KeyColName);
		cursor2Colomns = new int[outputColomnNames.length];
		for(int i = 0; i < outputColomnNames.length; i++){
			cursor2Colomns[i] = cursor2.getColumnIndexOrThrow(outputColomnNames[i]);
		}
	}
	
	public MatrixCursor generateMatrixCursor(){
		
		 MatrixCursor matrixCursor = new MatrixCursor(outputColomnNames);
		 while(cursor1.moveToNext()){
			 cursor2 = contentResolver.query(cursor2Uri,null, null, null, null);
			 while(cursor2.moveToNext()){
					if(Integer.parseInt(cursor1.getString(cursor1KeyCol)) == Integer.parseInt(cursor2.getString(cursor2Colomns[0]))){
						matrixCursor.addRow(addAllCols());
						
					}
			 }
		 
		
		 }
		 
		 return matrixCursor;
	}
	 public String[] addAllCols(){
		int length = outputColomnNames.length;
		String[] output  =  new String[length];
		
		for(int i = 0; i < length; i++){
			output[i]=cursor2.getString(cursor2Colomns[i]);;
		}
		
		return output;
	 }
}	
   

  

