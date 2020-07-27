package ac.uk.brookes.lh09092543.reversi;

import com.example.drawtest.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.content.Intent;
import android.view.View;

public class SettingsActivity extends Activity {

	int chipSet,boardType,timeLimit;
	boolean singlePlayer,timed;
	String[] playerName;
	String[] playerID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
    	chipSet = 0;
    	boardType = 0;
    	timeLimit = 50;
    	singlePlayer = true;
    	timed = false;
    	playerName = new String[2];
    	playerID = new String[2];
    	
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void exitToMainMenu(View view){
		Bundle settingsBundle = new Bundle();
		
		
		settingsBundle.putInt("chipSet",chipSet);
		settingsBundle.putInt("boardType",boardType);
		settingsBundle.putInt("timeLimit",timeLimit);
		
		settingsBundle.putBoolean("singlePlayer",singlePlayer);
		settingsBundle.putBoolean("timed",timed);
		
		settingsBundle.putStringArray("playerName",playerName);
		settingsBundle.putStringArray("playerID",playerID);
		
		Intent exitIntent = new Intent();
		exitIntent.putExtra("settingsBundle",settingsBundle);
        setResult(RESULT_OK, exitIntent);
        finish();
	}

}
