package com.example.nextbigthing;



import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;




import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.*;

public class LoadPicture extends Activity  {
	  
	private static int count = 0;
	private static ParseObject obj;
	// GestureDetector gesturedetector = null;
	//OnSwipeTouchListener swipe = null;
	View layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_picture);
		
		ImageView click1 = (ImageView) findViewById(R.id.imageView3);
		ImageView click2 = (ImageView) findViewById(R.id.imageView4);
		
		click1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(LoadPicture.this, "click1", Toast.LENGTH_SHORT).show();
				//ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("IMG");  
				
				if (LoadPicture.obj.isDataAvailable()){
					
					Log.d("OBJECTNAME onClick1", LoadPicture.obj.getObjectId());
					String objectId = LoadPicture.obj.getObjectId();
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("img_uid",objectId);
					params.put("tot", 1);
					ParseCloud.callFunctionInBackground("upvote", params, new FunctionCallback<Object>() {
					   public void done(Object o, ParseException e) {
					
					       if (e == null) {
					    	   Log.d("UPvote", "called");
					       }
					       else{
					    	    Log.d("UpVote", "Not called on the server"); 
					       }
					   }
					});
				}
    		   
			}
		});
		
		click2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(LoadPicture.this, "click2", Toast.LENGTH_SHORT).show();
				if (LoadPicture.obj.isDataAvailable()){
					
					Log.d("OBJECTNAME onClick2", LoadPicture.obj.getObjectId());
					String objectId = LoadPicture.obj.getObjectId();
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("img_uid",objectId);
					params.put("tot", 2);
					ParseCloud.callFunctionInBackground("upvote", params, new FunctionCallback<Object>() {
					   public void done(Object o, ParseException e) {
					
					       if (e == null) {
					    	   Log.d("UPvote", "Successfully called");
					       }
					       else{
					    	    Log.d("UpVote", "Function Upvote is not getting called on the server"); 
					       }
					   }
					});
				}
			}
		});
		
		layout = (GestureOverlayView) findViewById(R.id.gestures_overlay);
		//swipe = new OnSwipeTouchListener();
		layout.setOnTouchListener(new OnSwipeTouchListener(){
//			public void onSwipeTop() {
//	        Toast.makeText(LoadPicture.this, "top", Toast.LENGTH_SHORT).show();
//		    }
		    public void onSwipeRight() {
		        Toast.makeText(LoadPicture.this, "right", Toast.LENGTH_SHORT).show();
		        if(LoadPicture.count>0){
		        	LoadPicture.count--;
		        	LoadPic(LoadPicture.count);   	
		        }	
		    
		    }
		    
		    public void onSwipeLeft() {
		        
		    	Toast.makeText(LoadPicture.this, "left", Toast.LENGTH_SHORT).show();
		    	LoadPicture.count++;
		        LoadPic(LoadPicture.count);
		    }
		    
//		    public void onSwipeBottom() {
//		        Toast.makeText(LoadPicture.this, "bottom", Toast.LENGTH_SHORT).show();
//		    }
		});
		
	    
		//Button load = (Button) findViewById(R.id.btnFuck);	
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.load_picture, menu);
		LoadPic(LoadPicture.count); //passing an index of the picture
		//LoadPicture.count++;
		return true;
	}

	public void LoadPic(final int index){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
			ParseCloud.callFunctionInBackground("popular", params, new FunctionCallback<Object>() {
			   public void done(Object id_list, ParseException e) {
			
			       if (e == null) {
			    	
			    	   String idList_cvs = id_list.toString();
			    	   String []idList = idList_cvs.split(",");
			    	   try {
			    		    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("IMG");  
			    		    
			    		    LoadPicture.obj = (ParseObject) query.get(idList[index]);
			 
							Log.d("OBJECTNAME", obj.getObjectId());
							//Works Till here 

							ParseFile image1 = (ParseFile) obj.get("img1");
							Log.d("image", image1.getUrl());
							
							ParseFile image2 = (ParseFile) obj.get("img2");
							Log.d("image", image2.getUrl());
							
						       image1.getDataInBackground(new GetDataCallback() {
			                        @Override
			                        public void done(byte[] bytes, ParseException e) {
			                            if (e == null) {
			                                Log.d("Data", "We have data successfully");
			                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			                                ImageView img = (ImageView) findViewById(R.id.imageView3);
			                                img.setImageBitmap(bmp);
			                                Log.d("IMAGES", "IMAGES LOADED SUCCESSFULLY");
			                               
			                            } else {
			                                Log.d("ERROR: ", "" + e.getMessage());
			                            }
		                        }
		                    });
						       
						       image2.getDataInBackground(new GetDataCallback() {
			                        @Override
			                        public void done(byte[] bytes, ParseException e) {
			                            if (e == null) {
			                                Log.d("Data", "We have data successfully");
			                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			                                ImageView img = (ImageView) findViewById(R.id.imageView4);
			                                img.setImageBitmap(bmp);
			                                Log.d("IMAGES", "IMAGES LOADED SUCCESSFULLY");
			                               
			                            } else {
			                                Log.d("ERROR: ", "" + e.getMessage());
			                            }
		                        }
		                    });  
							
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			       }   
			       else{
			    	    Log.d("PictureCapture", "Could not save load picture"); 
			       }
			   }
			});
	}	
}




