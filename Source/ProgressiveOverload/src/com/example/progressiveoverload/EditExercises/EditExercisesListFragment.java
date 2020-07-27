package com.example.progressiveoverload.EditExercises;



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

public class EditExercisesListFragment extends Fragment {
	  
	 // private OnItemSelectedListener listener;
	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
			View view = (ViewGroup)inflater.inflate(R.layout.fragment_edit_exercises_list,container, false);
		
	    
	    
	    return view;
	  }
	  
	  /*
	  public interface OnItemSelectedListener {
	      public void onCreateExerciseButtonPressed(String link);
	  }*/
	  
	  /*
	  @Override
	    public void onAttach(Activity activity) {
	      super.onAttach(activity);
	      if (activity instanceof OnItemSelectedListener) {
	        listener = (OnItemSelectedListener) activity;
	      } else {
	        throw new ClassCastException(activity.toString()
	            + " must implemenet MyListFragment.OnItemSelectedListener");
	      }
	    }*/
	  /*
	//May also be triggered from the Activity
	 public void updateDetail() {
	   // create fake data
	   String newTime = String.valueOf(System.currentTimeMillis());
	   // Send data to Activity
	   listener.onCreateExerciseButtonPressed(newTime);
	 }*/
	}
