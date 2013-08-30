package com.example.nextbigthing;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseAnalytics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity
{
	private static final int ACTION_START_APP = 1;
	
	public static boolean isIntentAvailable(Context context, String action){
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = 
				packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	
	private void setBtnListenerOrDisable(
			Button btn,
			Button.OnClickListener onClickListener,
			String intentName
	) {
		/*
		if (isIntentAvailable(this, intentName)) {
			btn.setOnClickListener(onClickListener);
		} else {
			btn.setText(
				getText(R.string.cannot).toString() + " " + btn.getText());
			btn.setClickable(false);
		}*/
		btn.setOnClickListener(onClickListener);
	}
	
	Button.OnClickListener mStartApp = 
			new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatchStartAppIntent(ACTION_START_APP);
			}
		};
		
	private void dispatchStartAppIntent(int actionCode){
		Intent startApp = new Intent(this, PictureCapture.class);
		
		if (actionCode == ACTION_START_APP){
			startActivityForResult(startApp, actionCode);
		}
		
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button startBtn = (Button) findViewById(R.id.btnStart);
		setBtnListenerOrDisable(
				startBtn,
				mStartApp,
				"PICTURE_CAPTURE");
		
		//Parse stuff
		Parse.initialize(this, "vQm6jpJhvdC7DJavE1aOFcb7ytTBUV1wPden4jmy", "Oj1hVCxed731RsvGMExhbS5TjVWoAL2nR71FpqLZ");
		ParseAnalytics.trackAppOpened(getIntent());
		
	}
	
	
	
	
/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTION_TAKE_PHOTO_B) {
			if (resultCode == RESULT_OK) {
				handleBigCameraPhoto();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap1);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap1 != null));
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mImageBitmap1 = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
		mImageView1.setImageBitmap(mImageBitmap1);
		mImageView1.setVisibility(
			savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
					ImageView.VISIBLE : ImageView.INVISIBLE
		);
		
	}
	*/
}
	