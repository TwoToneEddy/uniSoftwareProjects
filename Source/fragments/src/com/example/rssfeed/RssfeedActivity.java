package com.example.rssfeed;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;
import android.util.Log;

public class RssfeedActivity extends Activity implements MyListFragment.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssfeed);
        
    }

    @Override
    public void onRssItemSelected(String link) {
      DetailFragment fragment = (DetailFragment) getFragmentManager()
              .findFragmentById(R.id.detailFragment);
          if (fragment != null && fragment.isInLayout()) {
            fragment.setText(link);
            Log.d("mydebug","detailFragment is in layout (not portrait)");
            
          } else {
        	  Log.d("mydebug","detailFragment is not in layout (portrait)");
              Intent intent = new Intent(getApplicationContext(),
                      DetailActivity.class);
                  intent.putExtra(DetailActivity.EXTRA_URL, link);
                  startActivity(intent);

                }
    }
}