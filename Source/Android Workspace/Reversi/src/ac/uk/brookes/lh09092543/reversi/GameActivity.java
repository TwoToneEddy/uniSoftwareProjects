package ac.uk.brookes.lh09092543.reversi;

import com.example.drawtest.R;

import android.os.Bundle;
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
import android.content.Intent;
import android.widget.Toast;

public class GameActivity extends Activity {

	DrawView drawView;
	int x,y;
	
	int chipSet,boardType,timeLimit;
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
    	
    	//Pass these variables to the draw view
    	drawView = new DrawView(this,gameVariables);
        drawView.setOnTouchListener(new DrawView.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					x = (int)event.getX();
					y = (int)event.getY();
					if(drawView.beenTouched(x,y) == 0){
						finish();
					}
				}
				return true;
			}
        });
        setContentView(drawView);        
    	drawView.requestFocus();
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
		setContentView(drawView);
		drawView.requestFocus();
	}

}
