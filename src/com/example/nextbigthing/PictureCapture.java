package com.example.nextbigthing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class PictureCapture extends Activity {

    private static final int ACTION_TAKE_PHOTO_B_THIS = 1;
    private static final int ACTION_TAKE_PHOTO_B_THAT = 2;
    private static final int ACTION_POST_PICTURES = 3;
	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	
	private String mCurrentPhotoPath; 
	
	private ImageView mImageView1;
	private ImageView mImageView2;
	private Bitmap mImageBitmap1;
	private Bitmap mImageBitmap2;
	private byte[] data_this;
	private byte[] data_that;
	
	
	
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	
	//Added stuff public
	public byte[] getBytesFromBitmap(Bitmap bitmap) {
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bitmap.compress(CompressFormat.JPEG, 70, stream); //70 --> quality of the image can take a number from 0 to 100
	    return stream.toByteArray();
	}
	
	public void sendPicToParse(){
		ParseFile file_this = new ParseFile("westpunjab1.bmp", data_this);		
		ParseFile file_that = new ParseFile("westpunjab2.bmp", data_that);
		file_this.saveInBackground();
		file_that.saveInBackground();
		
		//associate a ParseFile onto a ParseObject
		ParseObject post_data = new ParseObject("IMG");
		post_data.put("Title", "doubling");
		post_data.put("img1", file_this);
		post_data.put("img2", file_that);
		post_data.put("imgName1", "Check yourself");
		post_data.put("imgName2", "Before you wreck yourself");
		
		post_data.saveInBackground(new SaveCallback() {
			public void done(ParseException e){
				if (e == null){
					Log.d("PictureCapture", "Saved picture successfully");
				}
				else{
					Log.d("PictureCapture", "Could not save picture");
				}
			}
			
		});
		
	}
		
	private String getAlbumName(){
		return getString(R.string.album_name);
	}
	
	private File getAlbumDir(){
		File storageDir = null;
		
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
			
			if (storageDir != null){
				if (! storageDir.mkdirs()){
					if (! storageDir.exists()){
						Log.d("PictureCapture","Failed to create directory");
					}
				}
			}
		
		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE");
		}
		
		return storageDir;
	}
	
	private File createImageFile() throws IOException {
		//Create an image file name
		String timeStamp = new  SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}
	
	private File setUpPhotoFile() throws IOException {
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
        return f;
	}
	
	private void setPic(int picture){
	
		if (picture == 1)
		{
			int targetH = mImageView1.getHeight();
			int targetW = mImageView1.getWidth();
			
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
			int photoH = bmOptions.outHeight;
			int photoW = bmOptions.outWidth;
			
			int scaleFactor = 1;
			if ((targetW > 0) || (targetH > 0)) {	
				scaleFactor = Math.min(photoH/targetH, photoW/targetW);
			}
			
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;
			
			Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
							
			mImageView1.setImageBitmap(bitmap);
			mImageView1.setVisibility(View.VISIBLE);
			data_this = getBytesFromBitmap(bitmap);
			
			//sendPicToParse(data);
		}
		else if (picture == 2)
		{
			int targetH = mImageView2.getHeight();
			int targetW = mImageView2.getWidth();
			
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
			int photoH = bmOptions.outHeight;
			int photoW = bmOptions.outWidth;
			
			int scaleFactor = 1;
			if ((targetW > 0) || (targetH > 0)) {	
				scaleFactor = Math.min(photoH/targetH, photoW/targetW);
			}
			
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;
			
			Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
							
			mImageView2.setImageBitmap(bitmap);
			mImageView2.setVisibility(View.VISIBLE);
			data_that = getBytesFromBitmap(bitmap);
			
			//sendPicToParse(data);
		}
	
	}
	
	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_FILE");
		File f = new File(mCurrentPhotoPath);

		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}
	
	private void dispatchTakePictureIntent(int actionCode){
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		if (actionCode == ACTION_TAKE_PHOTO_B_THIS){
			File f = null;
			
			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e){
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;
			}
		}
		else if (actionCode == ACTION_TAKE_PHOTO_B_THAT){
			File f = null;
			
			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e){
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;
			}
		}
		
		startActivityForResult(takePictureIntent, actionCode);
	}
	
	private void dispatchPostPicturesIntent(int actionCode){
		if (actionCode == ACTION_POST_PICTURES){
			sendPicToParse();
		}
		
		//startActivityForResult(takePictureIntent, actionCode);
	}
	
	private void handleBigCameraPhoto(int picture){
		if (mCurrentPhotoPath != null){		
			setPic(picture);
			galleryAddPic();
			mCurrentPhotoPath =  null;
		}
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture_capture);
		
		mImageView1 = (ImageView) findViewById(R.id.imageView1);
		mImageView1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B_THIS);
			}
		});
		
		mImageView2 = (ImageView) findViewById(R.id.imageView2);
		mImageView2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B_THAT);
			}
		});
		
		mImageBitmap1 = null;
		mImageBitmap2 = null;
		
		Button picBtn = (Button) findViewById(R.id.btnIntend);
		picBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dispatchPostPicturesIntent(ACTION_POST_PICTURES);
			}
		});
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTION_TAKE_PHOTO_B_THIS) {
			if (resultCode == RESULT_OK) {
				int picture = 1;
				handleBigCameraPhoto(picture);
			}
		}
		else if (requestCode == ACTION_TAKE_PHOTO_B_THAT) {
			if (resultCode == RESULT_OK) {
				int picture = 2;
				handleBigCameraPhoto(picture);
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
	
	public static boolean isIntentAvailable(Context context, String action){
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = 
				packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	
	/*
	private void setViewListenerOrDisable(
			ImageView v,
			View.OnClickListener onClickListener,
			String intentName
	) {
		if (isIntentAvailable(this, intentName)) {
			v.setOnClickListener(onClickListener);
		} else {
			v.setClickable(false);
		}
	}
	*/
	/*
	private void setBtnListenerOrDisable(
			Button btn,
			Button.OnClickListener onClickListener,
			String intentName
	) {
		if (isIntentAvailable(this, intentName)) {
			btn.setOnClickListener(onClickListener);
		} else {
			btn.setText(
				getText(R.string.cannot).toString() + " " + btn.getText());
			btn.setClickable(false);
		}
	}
	*/
}

