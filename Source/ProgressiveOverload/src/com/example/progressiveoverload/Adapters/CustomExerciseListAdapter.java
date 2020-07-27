package com.example.progressiveoverload.Adapters;

import com.example.progressiveoverload.R;
import com.example.progressiveoverload.ContentProviders.*;
import com.example.progressiveoverload.R.id;
import com.example.progressiveoverload.R.layout;

import java.io.IOException;
import java.io.InputStream;

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

public class CustomExerciseListAdapter extends CursorAdapter {
	private LayoutInflater mLayoutInflater;
    private Context mContext;
    public CustomExerciseListAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context); 
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.exercise_listview_contents, parent, false);
        return v;
    }


    @Override
    public void bindView(View v, Context context, Cursor c) {
        String exerciseName = c.getString(c.getColumnIndexOrThrow(ExerciseContentProvider.KEY_EXERCISE_NAME));

        TextView nameTextView = (TextView) v.findViewById(R.id.exercise_name_textview);
        if (nameTextView != null) {
            nameTextView.setText(exerciseName);
        }
     
    }

}
