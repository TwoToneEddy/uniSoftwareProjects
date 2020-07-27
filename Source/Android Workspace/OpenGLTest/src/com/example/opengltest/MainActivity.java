package com.example.opengltest;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.opengl.GLSurfaceView;
import android.os.Build;

//test
public class MainActivity extends ActionBarActivity {

	private GLSurfaceView glView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    glView = new GLSurfaceView(this);           // Allocate a GLSurfaceView
	    glView.setRenderer(new MyGLRenderer(this)); // Use a custom renderer
	    this.setContentView(glView);                // This activity sets to GLSurfaceView
	}

	 // Call back when the activity is going into the background
	   @Override
	   protected void onPause() {
	      super.onPause();
	      glView.onPause();
	   }
	   
	   // Call back after onPause()
	   @Override
	   protected void onResume() {
	      super.onResume();
	      glView.onResume();
	   }

}
