package ac.uk.brookes.lh09092543.reversi;

//changed

import com.example.drawtest.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.util.Log;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.graphics.Bitmap;

public class MainActivity extends Activity {

	private final int CALL_SETTINGS = 1;
	
	int chipSet,boardType,timeLimit;
	boolean singlePlayer,timed;
	String[] playerName;
	String[] playerID;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {        
    	super.onCreate(savedInstanceState);        // Set full screen view
    	
    	chipSet = 0;
    	boardType = 0;
    	timeLimit = 30;
    	singlePlayer = true;
    	timed = false;
    	playerName = new String[2];
    	playerID = new String[2];
    	
		setContentView(R.layout.activity_main);
        
    	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void startGameActivity(View view){
		
		Bundle gameVariables = new Bundle();
		
		gameVariables.putInt("chipSet",chipSet);
		gameVariables.putInt("boardType",boardType);
		gameVariables.putInt("timeLimit",timeLimit);
		
		gameVariables.putBoolean("singlePlayer",singlePlayer);
		gameVariables.putBoolean("timed",timed);
		
		gameVariables.putStringArray("playerName",playerName);
		gameVariables.putStringArray("playerID",playerID);
		
		Intent intent = new Intent(this,GameActivity.class);
		intent.putExtra("gameVariables",gameVariables);
		startActivity(intent);
		
		
	}
	
	public void startSettingsActivity(View view){
		Intent intent = new Intent(this,SettingsActivity.class);
		startActivityForResult(intent,CALL_SETTINGS);
	}
	
	public void startScoreboardActivity(View view){
		Intent intent = new Intent(this,ScoreboardActivity.class);
		startActivity(intent);
	}
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
         if (resultCode == RESULT_OK) {
              if (requestCode == CALL_SETTINGS) {
            	  Bundle settingsBundle = data.getBundleExtra("settingsBundle");
              	  chipSet = settingsBundle.getInt("chipSet");
            	  boardType = settingsBundle.getInt("boardType");
            	  timeLimit = settingsBundle.getInt("timeLimit");
            	  singlePlayer = settingsBundle.getBoolean("singlePlayer");
              	  timed = settingsBundle.getBoolean("timed");
            	  playerName = settingsBundle.getStringArray("playerName");
            	  playerID = settingsBundle.getStringArray("playerID");
              }
         }
    }

}
