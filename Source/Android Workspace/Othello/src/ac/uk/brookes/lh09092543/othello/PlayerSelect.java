package ac.uk.brookes.lh09092543.othello;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.provider.ContactsContract;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import java.io.IOException;
import java.io.InputStream;
import android.net.Uri;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;


public class PlayerSelect extends Activity {

	int playerSelected,nameIdx,idIdx;
	String playerName,playerID;
	Bitmap playerPhoto;
	private Cursor cursor;
	private ContentResolver myContentResolver;
    private ImageView contactImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		playerSelected = intent.getIntExtra("playerSelected",0);
		
		setContentView(R.layout.activity_player_select);
		
		myContentResolver = getContentResolver();
		
		cursor = myContentResolver.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
	//	String [] from = new String[] {ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME};
	//	int[] to = new int[] {R.id.id_text, R.id.name_text};
		
		@SuppressWarnings("deprecation")
	//	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.contact_details_layout, cursor, from, to);
		ListView listView = (ListView) findViewById(R.id.contacts_list);
		CustomContactsAdapter myCustomContactsAdapter = new CustomContactsAdapter(this,cursor);
		listView.setAdapter(myCustomContactsAdapter);
		
		nameIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
		idIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
	    contactImage = (ImageView) findViewById(R.id.contact_image);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				cursor.moveToPosition(position);
				playerName = cursor.getString(nameIdx);
				playerID = cursor.getString(idIdx);
				
				Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,Long.parseLong(playerID));
						
			    InputStream photoStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), person);
			    
			     //Get the contact photo   
			    if (photoStream != null) {
			    	Bitmap photo = BitmapFactory.decodeStream(photoStream);
			        contactImage.setImageBitmap(photo);
			        try {
			        	photoStream.close();
			        } catch (IOException e) { 
			        	e.printStackTrace();
			        }
			    } else {
			    	//If there is no photo use the default android icon
			    	contactImage.setImageResource(android.R.drawable.sym_def_app_icon);
			    }
			}
		});
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_player_select, menu);
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
	
	public void exit(View view){
		Bundle playerDetails = new Bundle();
		playerDetails.putInt("playerSelected",playerSelected);
		playerDetails.putString("playerName",playerName);
		playerDetails.putString("playerID",playerID);
		
		Intent exitIntent = new Intent();
		exitIntent.putExtra("playerDetails",playerDetails);
		setResult(RESULT_OK, exitIntent);
        finish();
		
	}

}
