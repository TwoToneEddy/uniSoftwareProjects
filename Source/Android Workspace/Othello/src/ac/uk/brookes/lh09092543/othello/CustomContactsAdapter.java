package ac.uk.brookes.lh09092543.othello;

import java.io.IOException;
import java.io.InputStream;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class CustomContactsAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    public CustomContactsAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context); 
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.contact_details_layout, parent, false);
        return v;
    }


    @Override
    public void bindView(View v, Context context, Cursor c) {
        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,Long.parseLong(id));
        String photo = photoUri.toString();
      

        TextView nameTextView = (TextView) v.findViewById(R.id.name_text);
        if (nameTextView != null) {
            nameTextView.setText(name);
        }


        ImageView playerPhotoImageView = (ImageView) v.findViewById(R.id.player_photo);
        playerPhotoImageView.setVisibility(ImageView.INVISIBLE);
        if (photo != null && photo.length() != 0 && playerPhotoImageView != null) {
     
        	InputStream photoStream1 = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), photoUri);
    	    if (photoStream1 != null) {
    	          Bitmap playerImage = BitmapFactory.decodeStream(photoStream1);
    	          playerPhotoImageView.setImageBitmap(playerImage);
    	         try {
    	         	photoStream1.close();
    	         } catch (IOException e) { 
    		        e.printStackTrace();
    	         }
    	    }else{
    	    	playerPhotoImageView.setImageResource(android.R.drawable.sym_def_app_icon);
    	    }
    	    
            playerPhotoImageView.setVisibility(ImageView.VISIBLE);
        }

    }
}