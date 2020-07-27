package com.example.progressiveoverload.EditWorkouts;



import com.example.progressiveoverload.R;
import com.example.progressiveoverload.R.id;
import com.example.progressiveoverload.R.layout;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.TextView;
import android.provider.ContactsContract;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;

public class EditWorkoutsListFragment extends Fragment {
	  
	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
			View view = (ViewGroup)inflater.inflate(R.layout.fragment_edit_workouts_list,container, false);
	    return view;
	  }
	  
}
