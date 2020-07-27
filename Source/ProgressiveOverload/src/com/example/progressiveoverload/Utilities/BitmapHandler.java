package com.example.progressiveoverload.Utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.content.*;
import android.database.Cursor;

/**
 * 
 * This class is used to return a scaled bitmap from a Uri
 *
 */
public class BitmapHandler {
	
	BitmapFactory.Options options;
	InputStream inputStream = null;
	ContentResolver mContentResolver;
	
	public BitmapHandler(){
		
		
	}

	/**
	 * This method returns a bitmap scaled as near as possible to the desired width and height
	 * whilst maintaining aspect ratio
	 * 
	 * @param hostActivity	Host activity
	 * @param imageUri		Image Uri to get the bitmap from
	 * @param width			Desired width
	 * @param height		Desired height
	 * @return				A roughly scaled bitmap to avoid pointless memory usage
	 */
	public Bitmap generateBitmap(Activity hostActivity, Uri imageUri, int width, int height){
		mContentResolver = hostActivity.getContentResolver();
		Log.d("debugLee_2","6");
		try {
			Log.d("debugLee_2","7");
			Log.d("debugLee_2","URI = "+ imageUri.toString());
			//Decode image without returning a bitmap, just used to get projected size.
			inputStream = mContentResolver.openInputStream(imageUri);
			Log.d("debugLee_2","8");
			options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(inputStream,null,options);
			inputStream.close();
			
			//Re open input stream.
			inputStream = mContentResolver.openInputStream(imageUri);
			
			//This time return a bitmap
			options.inJustDecodeBounds = false;
			
			//options already contains projected size, use this method to calclate scale factor
			//for desired size. Set scale factor in options.inSampleSize
			options.inSampleSize = calculateInSampleSize(options, width, height);
			
			//Now decode and return bitmap scaled up or down
			Log.d("debugLee_2","9");
			Bitmap bitmap  = BitmapFactory.decodeStream(inputStream,null,options);
			Log.d("debugLee_2","10");
			inputStream.close();
			Log.d("debugLee_2","11");
			return bitmap;
		} catch (FileNotFoundException e) {
			Log.d("debugLee_2","fnfe");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("debugLee_2","ioe");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
			
	}
	
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
	}
	
}
