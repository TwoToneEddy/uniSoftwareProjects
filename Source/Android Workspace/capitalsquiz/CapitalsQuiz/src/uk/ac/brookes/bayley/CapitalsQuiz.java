package uk.ac.brookes.bayley;

import java.util.Random;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CapitalsQuiz extends Activity {

	/** Called when the activity is first created. */
	private EditText answer;
	private TextView question;
	private TextView list;

	private ContentResolver cr;
	private int numberCountries;
	private int currentQuestion;
	private String currentAnswer;
	private boolean isRandomNotSequential;
	private Random random;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capitals_quiz);

		question = (TextView) findViewById(R.id.question);
		answer = (EditText) findViewById(R.id.answer);
		list = (TextView) findViewById(R.id.list);

		random = new Random();

		cr = getContentResolver();
		Cursor c = cr.query(CapitalsProvider.CONTENT_URI, null, null, null,
				null);
		numberCountries = c.getCount();
		if (numberCountries == 0) {
			populateProvider();
			c = cr.query(CapitalsProvider.CONTENT_URI, null, null, null, null);
			numberCountries = c.getCount();
		}

		String table = "";
		if (c.moveToFirst()) {
			do {
				table += c.getPosition() + "  " +  c.getString(CapitalsProvider.COUNTRY_COLUMN) + "-"
						+ c.getString(CapitalsProvider.CAPITAL_COLUMN) + "\n";
			} while (c.moveToNext());
		}

		list.setText(table + "total: " + numberCountries);

	}

	private void populateProvider() {
		String[] countries = { "France", "Germany", "Italy", "Spain",
				"Belgium", "Portugal", "Norway", "Sweden", "Poland", "Hungary" };
		String[] capitals = { "Paris", "Berlin", "Rome", "Madrid", "Brussels",
				"Lisbon", "Oslo", "Stockholm", "Warsaw", "Budapest" };
		ContentResolver cr = getContentResolver();

		ContentValues values = new ContentValues();
		for (int i = 0; i < countries.length; i++) {
			values.put(CapitalsProvider.KEY_COUNTRY, countries[i]);
			values.put(CapitalsProvider.KEY_CAPITAL, capitals[i]);
			cr.insert(CapitalsProvider.CONTENT_URI, values);
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Sequential Quiz");
		menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "Random Quiz");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		if (item.getItemId() == Menu.FIRST) {
			isRandomNotSequential = false;
			list.setText("");
			currentQuestion = 0;
			showNextQuestion();
		} else if (item.getItemId() == Menu.FIRST + 1) {
			isRandomNotSequential = true;
			currentQuestion = random.nextInt(numberCountries);
			list.setText("");
			showNextQuestion();
		} else {
			return false;
		}
		return true;
	}

	public void showNextQuestion() {
		Uri newUri = CapitalsProvider.CONTENT_URI;
		Cursor cursor = cr.query(CapitalsProvider.CONTENT_URI, null, null, null, null);

		if (cursor.moveToPosition(currentQuestion)) {
			question.setText("What is the capital of "
					+ cursor.getString(CapitalsProvider.COUNTRY_COLUMN) + "?");
			currentAnswer = cursor.getString(CapitalsProvider.CAPITAL_COLUMN);

			answer.setOnKeyListener(new View.OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					String text;
					if ((event.getAction() == KeyEvent.ACTION_DOWN)
							&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
						String answerGiven = answer.getText().toString();
						answer.setText("");
						if (answerGiven.equalsIgnoreCase(currentAnswer))
							text = "Correct";
						else
							text = "Wrong - " + currentAnswer;
						Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_SHORT).show();
						if (isRandomNotSequential) {
							currentQuestion = (currentQuestion + 1)
									% numberCountries;
						} else {
							currentQuestion = random.nextInt(numberCountries);
						}
						showNextQuestion();
					}
					return false;
				}
			});
		}
		else {
			Toast.makeText(getApplicationContext(), "Could not move to position",
					Toast.LENGTH_LONG).show();
		}

	}

}
