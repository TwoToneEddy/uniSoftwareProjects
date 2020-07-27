package ac.uk.brookes.lh09092543.othello;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.TextView;
import android.provider.ContactsContract;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.util.Log;
import android.view.View;

public class ScoreBoardActivity extends Activity {

	private Cursor cursor;
	private ContentResolver myContentResolver;
	
	String contactList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scoreboard);
		
		myContentResolver = getContentResolver();
		
		//Adapt the score provider for a list view
		cursor = myContentResolver.query(ScoreProvider.CONTENT_URI,null, null, null, ScoreProvider.KEY_SCORE+" DESC");
		
		ListView listView = (ListView) findViewById(R.id.scoreboard_list);
		CustomScoreboardAdapter myCustomAdatper = new CustomScoreboardAdapter(this,cursor);	
		listView.setAdapter(myCustomAdatper);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_scoreboard, menu);
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
	
	public void exitToMain(View view){
		finish();
	}


}
