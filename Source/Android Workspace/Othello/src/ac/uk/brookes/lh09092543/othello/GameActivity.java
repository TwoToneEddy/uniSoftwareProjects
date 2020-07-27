package ac.uk.brookes.lh09092543.othello;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.util.Log;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

public class GameActivity extends Activity {

	public static final int END_GAME_DIALOG = 1;
	public static final int EXIT_GAME_DIALOG = 2;
	
	GameView gameView;
	int x,y;
	
	int chipSet,boardType,timeLimit,numberOfScores,winningPlayer;
	boolean singlePlayer, timed;
	String[] playerName = new String[2];
	String[] playerID = new String[2];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,                                          
    			WindowManager.LayoutParams.FLAG_FULLSCREEN);        
    	requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	
    	//Get variables from main activity
    	Intent intent = getIntent();
    	Bundle gameVariables = intent.getBundleExtra("gameVariables");
    	chipSet = gameVariables.getInt("chipSet");
    	boardType = gameVariables.getInt("boardType");
    	timeLimit = gameVariables.getInt("timeLimit");
    	singlePlayer = gameVariables.getBoolean("singlePlayer");
    	timed = gameVariables.getBoolean("timed");
    	playerName = gameVariables.getStringArray("playerName");
    	playerID = gameVariables.getStringArray("playerID");
    	winningPlayer = 0;
    
    	
    	//Pass these variables to the draw view
    	gameView = new GameView(this,gameVariables);
    	
    	//Set the onTouchListener
        gameView.setOnTouchListener(new GameView.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int beenTouchedResult;
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					x = (int)event.getX();
					y = (int)event.getY();
					
					//Query the board and pass the touched coordinates
					beenTouchedResult=gameView.beenTouched(x,y);
					
					if(beenTouchedResult == 0){
						showDialog(EXIT_GAME_DIALOG);
					}
					if(beenTouchedResult == 2){
						updateHighScores(playerName[0],gameView.playerScores[0],playerID[0]);
						updateHighScores(playerName[1],gameView.playerScores[1],playerID[1]);
						showDialog(END_GAME_DIALOG);
					}
				}
				return true;
			}
        });
        setContentView(gameView);        
    	gameView.requestFocus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game, menu);
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
	
	public void startGame(View view){
		setContentView(gameView);
		gameView.requestFocus();
	}

	
	@Override
	public void onPause(){
		super.onPause();
		gameView.timer.cancel();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(timed)
			gameView.timer.start();
	}
	
	public void exitToMainMenu(View view){
		//Exits to the main menu and passes some variables so the game settings are retained.
		Bundle gameVariables = new Bundle();
		
		gameVariables.putInt("chipSet",chipSet);
		gameVariables.putInt("boardType",boardType);
		gameVariables.putInt("timeLimit",timeLimit);
		
		gameVariables.putBoolean("singlePlayer",singlePlayer);
		gameVariables.putBoolean("timed",timed);
		
		gameVariables.putStringArray("playerName",playerName);
		gameVariables.putStringArray("playerID",playerID);
		
		Intent exitIntent = new Intent();
		exitIntent.putExtra("gameVariables",gameVariables);
        setResult(RESULT_OK, exitIntent);
        finish();
	}
	
	@SuppressWarnings("deprecation")
	@Override
    public Dialog onCreateDialog(int id) {
         switch (id) {
	     case END_GAME_DIALOG:
	          Builder endBuilder = new AlertDialog.Builder(this);
	          
	          if(gameView.playerScores[0]<gameView.playerScores[1])
	        	  winningPlayer = 1;
	          else
	        	  winningPlayer = 0;
	          
	          if(gameView.playerScores[0]==gameView.playerScores[1])
	        	  endBuilder.setMessage("Game Over! It's a draw!");
	          else
	        	  endBuilder.setMessage("Game Over!" +playerName[winningPlayer]+" wins!");
	          
	          endBuilder.setCancelable(true);
	          endBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
		         @Override
		         public void onClick(DialogInterface dialog, int which) {
			          exitToMainMenu(null);
		              dialog.cancel();
			}
		});

		endBuilder.setNegativeButton("New Game", new DialogInterface.OnClickListener() {
	        @Override
		    public void onClick(DialogInterface dialog, int which) {
	        	gameView.restartGame();
		        dialog.cancel();
                        }
	           });
              AlertDialog dialog = endBuilder.create();
              dialog.show();
         break;     
	     case EXIT_GAME_DIALOG:
	          Builder exitBuilder = new AlertDialog.Builder(this);
	          
	          exitBuilder.setMessage("Are you sure you want to exit?");
	          exitBuilder.setCancelable(true);
	          exitBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
		         @Override
		         public void onClick(DialogInterface dialog, int which) {
		              dialog.cancel();
			}
		});

	        exitBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
	        @Override
		    public void onClick(DialogInterface dialog, int which) {
	            exitToMainMenu(null);
		        dialog.cancel();
                       }
	           });
             AlertDialog exitDialog = exitBuilder.create();
             exitDialog.show();
         break;
         }
         return super.onCreateDialog(id);
    }

	public void updateHighScores(String playerName, int playerScore,String playerID){
		//If the player is in the top 10 high scores in the content provider this method
		//deletes the current lowest in the provider and adds a new entry for this player.
		
		int lowestScore = 0;
		int lowestScoreId = 0;
		
		ContentResolver myContentResolver = getContentResolver();
		Cursor cursor = myContentResolver.query(ScoreProvider.CONTENT_URI,null, null, null, ScoreProvider.KEY_SCORE+" ASC");
		int scoreId;
		int lowestScoreIdx;
		Uri photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,Long.parseLong(playerID));
		
			scoreId = cursor.getColumnIndexOrThrow(ScoreProvider.KEY_SCORE);
			lowestScoreIdx = cursor.getColumnIndexOrThrow(ScoreProvider.KEY_ID);
			
			//If the score content provider already has 10 entries we need to delete the lowest
			if(cursor.getCount()==10){
				if(cursor.moveToFirst()){
					lowestScore = Integer.parseInt(cursor.getString(scoreId));
					lowestScoreId = Integer.parseInt(cursor.getString(lowestScoreIdx));
				}
				//Does our score qualify?
				if(playerScore>lowestScore){
					//Delete the lowest score
					myContentResolver.delete(ScoreProvider.CONTENT_URI, "_id='"+lowestScoreId+"'",null);
					//Add our score to the provider
					ContentValues values = new ContentValues();
					values.put(ScoreProvider.KEY_NAME, playerName);
					values.put(ScoreProvider.KEY_SCORE,playerScore);
					values.put(ScoreProvider.IMAGE_URI,photoUri.toString());
					myContentResolver.insert(ScoreProvider.CONTENT_URI, values);
				}
			}else{
				//If the score content provider has less than 10 entries just add this score
				ContentValues values = new ContentValues();
				values.put(ScoreProvider.KEY_NAME, playerName);
				values.put(ScoreProvider.KEY_SCORE,playerScore);
				values.put(ScoreProvider.IMAGE_URI,photoUri.toString());
				myContentResolver.insert(ScoreProvider.CONTENT_URI, values);
			}
		}
		
	
}
