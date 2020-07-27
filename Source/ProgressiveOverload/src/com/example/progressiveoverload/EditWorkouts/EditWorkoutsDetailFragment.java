package com.example.progressiveoverload.EditWorkouts;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.R.id;
import com.example.progressiveoverload.R.layout;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import android.app.Activity;

public class EditWorkoutsDetailFragment extends Fragment {
	
	 
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = (ViewGroup)inflater.inflate(R.layout.fragment_edit_workouts_detail,
	        container, false);
	    return view;
	  }
	
	  
}