package com.example.progressiveoverload.ViewStatistics;



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

public class ViewStatisticsListFragment extends Fragment {
	  
	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
			View view = (ViewGroup)inflater.inflate(R.layout.fragment_view_statistics_list,container, false);
	
	    
	    return view;
	  }
	  
	}
