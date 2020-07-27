package ac.uk.brookes.lh09092543.othello;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.widget.CheckBox;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.EditText;


public class SettingsActivity extends Activity{

	private final int SELECT_PLAYER = 2;
	
	int chipSet,boardType,timeLimit,playerSelected;
	boolean singlePlayer,timed;
	String[] playerName;
	String[] playerID;
	CheckBox timedChk;
	RadioGroup chipSetRadioGroup,boardTypeRadioGroup,playerRadioGroup;
	RadioButton chipSetButton;
	EditText timeLimitInput;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


    	playerSelected = 0;
    	
    	//Get variables from main activity, these could be defaults or they
    	//could be settings from a previous game
    	Intent intent = getIntent();
    	playerName = intent.getStringArrayExtra("playerName");
    	playerID = intent.getStringArrayExtra("playerID");
    	chipSet = intent.getIntExtra("chipSet",0);
    	boardType = intent.getIntExtra("boardType",0);
    	timeLimit = intent.getIntExtra("timeLimit",10);
    	singlePlayer = intent.getBooleanExtra("singlePlayer",true);
    	timed = intent.getBooleanExtra("timed",false);
  
    	
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		timedChk = (CheckBox) findViewById(R.id.timedCheckBox);
		chipSetRadioGroup = (RadioGroup) findViewById(R.id.chipSetRadioGroup);
		boardTypeRadioGroup = (RadioGroup) findViewById(R.id.boardTypeRadioGroup);
		playerRadioGroup = (RadioGroup) findViewById(R.id.playerRadioGroup);
		timeLimitInput = (EditText) findViewById(R.id.timeLimitText);
		
		timeLimitInput.setText(Integer.toString(timeLimit));
		timedChk.setChecked(timed);
		
		//Set the state of the radio / check boxes depending on their last or default setting, this is 
		//determined by the variables from the main activity.
		if(chipSet == 0)
			chipSetRadioGroup.check(R.id.set1RB);
		else
			chipSetRadioGroup.check(R.id.set2RB);
		
		if(boardType == 0)
			boardTypeRadioGroup.check(R.id.boardType1);
		else
			boardTypeRadioGroup.check(R.id.boardType2);
		
		if(singlePlayer)
			playerRadioGroup.check(R.id.singlePlayer);
		else
			playerRadioGroup.check(R.id.twoPlayer);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_settings, menu);
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

		//Pass the states of the checkboxes back to the main activity.
		if(chipSetRadioGroup.getCheckedRadioButtonId()==R.id.set1RB){
			chipSet=0;
		}else{
			chipSet=1;
		}
		
		if(boardTypeRadioGroup.getCheckedRadioButtonId()==R.id.boardType1){
			boardType=0;
		}else{
			boardType=1;
		}
		
		if(playerRadioGroup.getCheckedRadioButtonId()==R.id.singlePlayer){
			singlePlayer=true;
		}else{
			singlePlayer=false;
		}

		if(timedChk.isChecked()){
			timed = true;
		}
		
		try {
			timeLimit = Integer.parseInt(timeLimitInput.getText().toString());
			
	    } catch (NumberFormatException nfe) {
	    	
	    }

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
	public void player1Details(View view){
		//When the player 1 details is pressed start the player select activity
		//with the player selected variable
		Intent intent = new Intent(this,PlayerSelect.class);
		playerSelected = 1;
		intent.putExtra("playerSelected",playerSelected);
		startActivityForResult(intent,SELECT_PLAYER);
		playerSelected = 0;
	}
	
	public void player2Details(View view){
		//When the player 2 details is pressed start the player select activity
		//with the player selected variable
		Intent intent = new Intent(this,PlayerSelect.class);
		playerSelected = 2;
		intent.putExtra("playerSelected",playerSelected);
		startActivityForResult(intent,SELECT_PLAYER);
		playerSelected = 0;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			if(requestCode ==SELECT_PLAYER ){
				Bundle playerDetails = data.getBundleExtra("playerDetails");
				playerSelected = playerDetails.getInt("playerSelected");
				
				//Only take the player name and ID if they are not null.
				if(playerSelected==1){
					if(playerDetails.getString("playerName")!= null)
						playerName[0] = playerDetails.getString("playerName");
					if(playerDetails.getString("playerID")!=null)
						playerID[0] = playerDetails.getString("playerID");
				}
				if(playerSelected == 2){
					if(playerDetails.getString("playerName")!= null)
						playerName[1] = playerDetails.getString("playerName");
					if(playerDetails.getString("playerID")!=null)
						playerID[1] = playerDetails.getString("playerID");
				}
			}
		}
	
	}
	



}
