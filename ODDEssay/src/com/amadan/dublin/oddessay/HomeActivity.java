
/*** HomeActivity Class
 * 
 * 	@author Paul Clune
 * 
 *   Main activity - supports view first seen when app is started.
 *   Responsible for acquiring sensor data; launching camera intent;
 *   and writing to database.
 *   
 *   	Copyright 2015 Paul Clune
 
    	Licensed under the Apache License, Version 2.0 (the "License");
   		you may not use this file except in compliance with the License.
   		You may obtain a copy of the License at

       		http://www.apache.org/licenses/LICENSE-2.0

   		Unless required by applicable law or agreed to in writing, software
   		distributed under the License is distributed on an "AS IS" BASIS,
   		WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   		See the License for the specific language governing permissions and
   		limitations under the License.

 */

package com.amadan.dublin.oddessay;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends BaseActivity implements LocationListener, SensorEventListener{

	//used to manage if media file type is a video or image
	private static final int MEDIA_TYPE_IMAGE = 1;
	private static final int MEDIA_TYPE_VIDEO = 2;
	//directory to hold media files; for this app
	private static String mediaDirName = "ODDEssayAppMedia";
	//codes to manage intent
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	//image file information
	private Uri fileUri;
	private static String fileName;
	//GeoData
	private LocationManager mLocationManager;
	TextView tvLongitude;
	TextView tvLatitude;
	//device orientation
	private SensorManager senSensorManager;
	private Sensor senAccelerometer;
	private long lastUpdate = 0;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 600;
	TextView _tvX_axis;
	TextView _tvY_axis;
	TextView _tvZ_axis;
	//
	private String defaultVal = "default"; 
	// Date and Time
	String _dateStamp = defaultVal;
	String _timeStamp = defaultVal;

	//Button to capture image
	private Button bCapture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		//Logger
		Log.d(logTag, "HomeActivity: onCreate");
		//call onCreate in BaseActivity for NavigationDrawer
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.activity_main, frameLayout);

		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerArray[position]);

		//text view for location data
		tvLongitude = (TextView)findViewById(R.id.tvLongitude);
		tvLatitude = (TextView)findViewById(R.id.tvLatitude);
		_tvX_axis = (TextView)findViewById(R.id.tvX_axis);
		_tvY_axis = (TextView)findViewById(R.id.tvY_axis);
		_tvZ_axis = (TextView)findViewById(R.id.tvZ_axis);

		//get the button on our layout
		bCapture = (Button) findViewById(R.id.bCapture);
		bCapture.setOnClickListener(new View.OnClickListener() {
			
			/* onClick
			 * When button is selected, the camera is 
			 * called via explicit intent
			 */
			@Override
			public void onClick(View v) {
				//logger
				Log.d(logTag,"HomeAct: onCreate: Button bCapture clicked.");				
				captureImage();
			}
		});

		// Data displayed
		this.displayData();

	}

	/******
	 * Display data
	 */
	private void displayData(){

		////////// Date ///////////////
		_dateStamp = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
		_timeStamp = new SimpleDateFormat("h:mm a").format(Calendar.getInstance().getTime());

		TextView text = (TextView)findViewById(R.id.tvDate);
		text.setText("" + _dateStamp);

		////////// Location //////////////	    
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location != null) {
			// Do something with the recent location fix
			//  otherwise wait for the update below
			// logger
			Log.d(logTag,"HomeAct: displayData: Location: " +location.getLatitude() + " and " + location.getLongitude());

			tvLongitude.setText("" + location.getLongitude());	    	   
			tvLatitude.setText("" + location.getLatitude());
		}
		else {
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		}

		////////// Device Orientation //////////
		senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);    	   
	}

	/*** onLocationChanged
	 * 
	 * utility required to handle any change from sensor for location
	 * 
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			Log.d(logTag, "Location: " +location.getLatitude() + " and " + location.getLongitude());
			//TextView tvLongitude = (TextView)findViewById(R.id.tvLongitude);
			tvLongitude.setText("" + location.getLongitude());
			//TextView tvLatitude = (TextView)findViewById(R.id.tvLatitude);
			tvLatitude.setText("" + location.getLatitude());
		}
	}
	
	/***
	 * captureImage()
	 * responsible to fire intent to camera app;
	 * then, on return from camera app, store the
	 * image in a defined location in the filesystem,
	 * while also writing the file path to the stored
	 * image into database.
	 */
	private void captureImage(){
		// create Intent to take a picture and return control to the calling application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// create a file to save the image
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); 
		// Logger
		Log.d(logTag,"HomeAct: captureImage: File path and name is " + fileUri.toString());
		//set the image file name
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); 
		// start the image capture Intent
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

	}

	
	/*** onActivityResult
	 * 
	 * handle return from camera intent - image name along with sensor
	 * data captured at time of return on camera intent is written
	 * to database at this time
	 * 
	 */
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				//image captured and written to file location
				// logger
				Log.d(logTag,"HomeAct: onActivityResult: File Name: " + fileName);
				//TODO refactor to use an async background task

				AsyncTask<Object, Object, Object> saveContactTask = 
						new AsyncTask<Object, Object, Object>() 
						{
							@Override
							protected Object doInBackground(Object... params) 
							{
								// logger
								Log.d(logTag,"HomeAct: AsyncTask add to database:  " + fileName);
								addImageToDataBase(fileName); // save contact to the database
								return null;
							} // end method doInBackground
		
							@Override
							protected void onPostExecute(Object result) 
							{
								//finish(); // return to the previous Activity
								// logger
								Log.d(logTag,"HomeAct: AsyncTask add to database:  " + fileName);
							} // end method onPostExecute
						}; // end AsyncTask

				// save the contact to the database using a separate thread
				saveContactTask.execute((Object[]) null); 

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
		}

		if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// Video captured and saved to fileUri specified in the Intent
				Toast.makeText(this, "Video saved to:\n" +
						data.getData(), Toast.LENGTH_LONG).show();
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the video capture
			} else {
				// Video capture failed, advise user
			}
		}
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){

		//using local storage at this time for saving images
		// Logger
		Log.d(logTag,"HomeAct: getOutputMediaFileUri: creating media file");
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), mediaDirName);
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.
		// Logger
		Log.d(logTag,"HomeAct: getOutputMediaFileUri: attempt to create directory");
		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				// Logger
				Log.d(logTag, "HomeAct: getOutputMediaFileUri: failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE){
			fileName = "IMG_"+ timeStamp + ".jpg";
			mediaFile = new File(mediaStorageDir.getPath() + 
					File.separator + fileName);
			//mediaFile = new File(mediaStorageDir.getPath() + File.separator +
			//"IMG_"+ timeStamp + ".jpg");
		} else if(type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"VID_"+ timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	/***addImageToDataBase
	 * 
	 * code to add images to database
	 * 
	 */

	private void addImageToDataBase(String fileName){
		// Logger
		Log.d(logTag,"HomeActivity: addImageToDataBase");
		//ODDEssayDBHandler dbHandler = new ODDEssayDBHandler(this, null, null, 2);
		ODDEssayDBHandler dbHandler = new ODDEssayDBHandler(this);
		// Create new record to add to database
		Record record = new Record();

		//add filename
		record.setRecFileName(fileName);
		// add date
		record.setRecDate(_dateStamp);
		// add time
		record.setRecTime(_timeStamp);
		
		// add longitude
		record.setRecLongitude(tvLongitude.getText().toString());
		// add latitude
		record.setRecLatitude(tvLatitude.getText().toString());
		// add X-axis
		record.setRecXaxis(_tvX_axis.getText().toString());
		// add Y-axis
		record.setRecYaxis(_tvY_axis.getText().toString());
		// add Z-axis
		record.setRecZaxis(_tvZ_axis.getText().toString());
		
		/*-->
		// add longitude
		record.setRecLongitude("3030");
		// add latitude
		record.setRecLatitude("3030");
		// add X-axis
		record.setRecXaxis("3030");
		// add Y-axis
		record.setRecYaxis("3030");
		// add Z-axis
		record.setRecZaxis("3030");
		<--*/
		// Gets the data repository in write mode		
		dbHandler.addRecord(record);


		// Add image file to media database to allow other apps to view image		
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		//File f = new File(mCurrentPhotoPath);
		//Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(fileUri);
		this.sendBroadcast(mediaScanIntent);

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	/*** onSensorChanged
	 * 
	 * utility required to handle any change from sensor for accelerometer
	 * 
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor mySensor = event.sensor;
		float x = 0000;
		float y = 0000;
		float z = 0000;


		if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				x = event.values[0];
				y = event.values[1];
				z = event.values[2];

				long curTime = System.currentTimeMillis();

				if ((curTime - lastUpdate) > 100) {
					long diffTime = (curTime - lastUpdate);
					lastUpdate = curTime;

					//Log.d(logTag, "Orientation: x:" + x + " y:" + y + "z: " + z);
					//TextView x_axis = (TextView)findViewById(R.id.tvX_axis);
					_tvX_axis.setText("" + x);
					//TextView y_axis = (TextView)findViewById(R.id.tvY_axis);
					_tvY_axis.setText("" + y);
					//TextView z_axis = (TextView)findViewById(R.id.tvZ_axis);
					_tvZ_axis.setText("" + z);

					float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

					if (speed > SHAKE_THRESHOLD) {

					}

					last_x = x;
					last_y = y;
					last_z = z;
				}	        
			}
		}
	}
	//override??
	protected void onPause() {
		super.onPause();
		senSensorManager.unregisterListener(this);
	}

	protected void onResume() {
		super.onResume();
		this.displayData();
		//senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
}


/***** -->>

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

-->> ******/
