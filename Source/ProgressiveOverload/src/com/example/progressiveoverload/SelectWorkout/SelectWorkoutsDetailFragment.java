package com.example.progressiveoverload.SelectWorkout;

import com.example.progressiveoverload.R;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectWorkoutsDetailFragment extends Fragment {
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = (ViewGroup)inflater.inflate(R.layout.fragment_select_workouts_detail,
	        container, false);
	    return view;
	  }

}
