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
public class WorkoutContentProvider extends ContentProvider {

	public static final String AUTHORITY = "com.example.progressiveoverload.ContentProviders.workoutprovider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/workoutprovider");

	private SQLiteDatabase workoutDB;
	private static final String WORKOUT_PROVIDER = "workoutprovider";
	public static final String KEY_WORKOUT_ID = "_id";
	public static final String KEY_WORKOUT_DAY = "Day";
	public static final String KEY_MUSCLE_GROUP = "Muscle_group";
	public static final String KEY_DATE = "Last_performed";

	private static final int DAY = 1;
	private static final int GROUP = 2;
	private static final UriMatcher uriMatcher;

	private static final String DATABASE_NAME = "workoutprovider.db";
	private static final int DATABASE_VERSION = 8;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "workoutprovider", DAY);
		uriMatcher.addURI(AUTHORITY, "workoutprovider/#", GROUP);
	}

	private String makeNewWhere(String where, Uri uri, int matchResult) {

		if (matchResult != GROUP) {
			return where;
		} else {
			String newWhereSoFar = KEY_WORKOUT_ID + "=" + uri.getPathSegments().get(1);
			if (TextUtils.isEmpty(where))
				return newWhereSoFar;
			else
				return newWhereSoFar + " AND (" + where + ')';
		}
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {

		return workoutDB.delete(WORKOUT_PROVIDER, where, whereArgs);
		
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case DAY:
			return "vnd.android.cursor.dir/vnd.brookes.lh09092543.othello.PerformanceRecord";
		case GROUP:
			return "vnd.android.cursor.item/vnd.brookes.lh09092543.othello.PerformanceRecord";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = workoutDB.insert(WORKOUT_PROVIDER, KEY_WORKOUT_DAY, values);
		Log.d("debugLee_2","rowID = "+rowID);
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
		this.workoutDB = helper.getWritableDatabase();
		return (workoutDB != null);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sort) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(WORKOUT_PROVIDER);

		if (uriMatcher.match(uri) == GROUP) {
			qb.appendWhere(KEY_WORKOUT_ID + "=" + uri.getPathSegments().get(1));
		}

		Cursor c = qb.query(workoutDB, projection, selection, selectionArgs,
				null, null, sort);
		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {

		int matchResult = uriMatcher.match(uri);
		String newWhere = makeNewWhere(where, uri, matchResult);

		if (matchResult == GROUP || matchResult == DAY) {
			int count = workoutDB.update(WORKOUT_PROVIDER, values, newWhere,
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
			db.execSQL("CREATE TABLE " + WORKOUT_PROVIDER + " (" + KEY_WORKOUT_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_WORKOUT_DAY
					+ " TEXT," + KEY_MUSCLE_GROUP + " TEXT," + KEY_DATE + " DATETIME);");
		}


		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + WORKOUT_PROVIDER);
			onCreate(db);
		}

	}
	
	
}