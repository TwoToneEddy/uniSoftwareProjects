package com.example.progressiveoverload.DoWorkouts;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.text.format.*;
import android.media.MediaPlayer;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.progressiveoverload.R;

public class RestCounter {
	TextView restCounterText;
	Activity hostActivity;
	int restCounterTextID,restTime,restTimeCounter;
	Timer timer;
	TimerTask timerTask;
	final Handler handler = new Handler();
	
	public RestCounter(Activity hostActivity, int restCounterTextID, int restTime){
		this.hostActivity=hostActivity;
		this.restCounterTextID=restCounterTextID;
		restCounterText = (TextView) hostActivity.findViewById(restCounterTextID);
		this.restTime=restTime;
		restTimeCounter=restTime;
		setRestText(restTimeCounter);
		
	}
	
	public void setRestTime(int restTime){
		this.restTime=restTime;
		restTimeCounter=restTime;
		setRestText(restTimeCounter);
		
	}
	public void resetTime(){
		restTimeCounter=restTime;
		setRestText(restTimeCounter);
	}
	public void startTimer() {
		//set a new Timer
		timer = new Timer();
		
		//initialize the TimerTask's job
		initializeTimerTask();
		
		//schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
		timer.schedule(timerTask, 1000, 1000); //
	}
	
	public void stoptimertask() {
		//stop the timer, if it's not already null
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	public void initializeTimerTask() {
			
			timerTask = new TimerTask() {
				public void run() {
					
					//use a handler to run a toast that shows the current timestamp
					handler.post(new Runnable() {
						public void run() {
							restTimeCounter = restTimeCounter-1;
							if(restTimeCounter<0){
								stoptimertask();
								resetTime();
							}else{
								if(restTimeCounter==0){
									playBeep();
									stoptimertask();
									resetTime();
								}
					        	setRestText(restTimeCounter);
							}
						}
					});
				}
			};
		}
	
	public void playBeep(){
		Thread t = new Thread(){
            public void run(){
                MediaPlayer player = null;
                player = MediaPlayer.create(hostActivity.getBaseContext(),R.raw.beep);
                player.start();
                try {
                	Thread.sleep(player.getDuration()+100);
                	player.release();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        t.start();
	}
	
	public void setRestText(int time){
		 if(restCounterText!=null){
			 if(time < 0){
				 restCounterText.setText("");
			 }else{
				 restCounterText.setText(Integer.toString(time)+":00");
			 }
			 
		 }
	 }

}
