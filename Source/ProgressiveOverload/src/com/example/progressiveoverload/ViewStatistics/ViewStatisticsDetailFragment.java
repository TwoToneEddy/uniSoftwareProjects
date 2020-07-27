package com.example.progressiveoverload.ViewStatistics;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.R.id;
import com.example.progressiveoverload.R.layout;
import com.example.progressiveoverload.ViewStatistics.*;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;

public class ViewStatisticsDetailFragment extends Fragment {
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = (ViewGroup)inflater.inflate(R.layout.fragment_view_statistics_detail,
	        container, false);
	    return view;
	  }

	
}