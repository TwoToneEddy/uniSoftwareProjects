package com.example.progressiveoverload.ContentProviders;

import com.example.progressiveoverload.*;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

//Just workout record so far
public class ExerciseContentProvider extends ContentProvider {

	public static final String AUTHORITY = "com.example.progressiveoverload.ContentProviders.exerciseprovider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/exerciseprovider");

	private SQLiteDatabase exerciseDB;
	private static final String EXERCISE_PROVIDER = "exerciseprovider";
	public static final String KEY_EXERCISE_ID = "_id";
	public static final String KEY_EXERCISE_NAME = "Exercise_name";
	public static final String KEY_REST_PERIOD = "Rest_period";
	public static final String IMAGE_URI = "Image";

	private static final int NAME = 1;
	private static final int REST = 2;
	private static final UriMatcher uriMatcher;

	private static final String DATABASE_NAME = "exerciseprovider.db";
	private static final int DATABASE_VERSION = 1;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "exerciseprovider", NAME);
		uriMatcher.addURI(AUTHORITY, "exerciseprovider/#", REST);
	}

	private String makeNewWhere(String where, Uri uri, int matchResult) {

		if (matchResult != REST) {
			return where;
		} else {
			String newWhereSoFar = KEY_EXERCISE_ID + "=" + uri.getPathSegments().get(1);
			if (TextUtils.isEmpty(where))
				return newWhereSoFar;
			else
				return newWhereSoFar + " AND (" + where + ')';
		}
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {

		return exerciseDB.delete(EXERCISE_PROVIDER, where, whereArgs);
		
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case NAME:
			return "vnd.android.cursor.dir/vnd.brookes.lh09092543.othello.PerformanceRecord";
		case REST:
			return "vnd.android.cursor.item/vnd.brookes.lh09092543.othello.PerformanceRecord";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = exerciseDB.insert(EXERCISE_PROVIDER, KEY_EXERCISE_NAME, values);
		if (rowID > 0) {
			Uri newuri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(newuri, null);
			return uri;
		} else{
			throw new SQLException("Failed to insert row into " + uri);
		}
	}

	@Override
	public boolean onCreate() {
		ScoresDatabaseHelper helper = new ScoresDatabaseHelper(
				this.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
		this.exerciseDB = helper.getWritableDatabase();
		return (exerciseDB != null);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sort) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(EXERCISE_PROVIDER);

		if (uriMatcher.match(uri) == REST) {
			qb.appendWhere(KEY_EXERCISE_ID + "=" + uri.getPathSegments().get(1));
		}

		Cursor c = qb.query(exerciseDB, projection, selection, selectionArgs,
				null, null, sort);
		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {

		int matchResult = uriMatcher.match(uri);
		String newWhere = makeNewWhere(where, uri, matchResult);

		if (matchResult == REST || matchResult == NAME) {
			int count = exerciseDB.update(EXERCISE_PROVIDER, values, newWhere,
					whereArgs);

			getContext().getContentResolver().notifyChange(uri, null);
			return count;
		} else
			throw new IllegalArgumentException("Unknown URI " + uri);
	}

	private static class ScoresDatabaseHelper extends SQLiteOpenHelper {

		public ScoresDatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + EXERCISE_PROVIDER + " (" + KEY_EXERCISE_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_EXERCISE_NAME
					+ " TEXT," + KEY_REST_PERIOD + " INTEGER," + IMAGE_URI + " TEXT);");
		}


		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + EXERCISE_PROVIDER);
			onCreate(db);
		}

	}
	
	
}