/*** GalleryActivity Class
 * 
 * 	@author Paul Clune
 * 
 *	Gallery Activity - will display image file,
 *	that has been selected by user, along with
 *	the relevant saved data for that image.
 *
 *	The image file name is used, to query the
 *	database to find the record for that image. 
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
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract.Columns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryActivity extends BaseActivity {

	private final int SELECT_PHOTO = 100;
	private ImageView imageView;
	private BitmapFactory.Options options;
	
	//image data
	private String imageFileName = "unknown";

	String dataDefault = "no data";
	//data display items
	TextView _tvImageNameName;
	TextView _tvDate;
	TextView _tvTime;
	TextView _tvLongitude;
	TextView _tvLatitude;
	TextView _tvX_axis;
	TextView _tvY_axis;
	TextView _tvZ_axis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//LogCat output
		Log.d(logTag, "GalleryActivity: onCreate");

		getLayoutInflater().inflate(R.layout.activity_gallery, frameLayout);

		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerArray[position]);

		//setContentView(R.layout.activity_gallery);

		imageView = (ImageView)findViewById(R.id.imageViewG);

		Button pickImage = (Button) findViewById(R.id.btn_pick);
		pickImage.setOnClickListener(new OnClickListener() {

			/* onClick
			 * When button is selected, the photo picker is 
			 * called via explicit intent
			 */
			@Override
			public void onClick(View view) {				
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, SELECT_PHOTO);
			}
		});

		//textviews for database results
		_tvImageNameName = (TextView)findViewById(R.id.tvImageName);
		_tvDate = (TextView)findViewById(R.id.tvDate);
		_tvTime = (TextView)findViewById(R.id.tvTime);
		_tvLongitude = (TextView)findViewById(R.id.tvLongitude);
		_tvLatitude  = (TextView)findViewById(R.id.tvLatitude);
		_tvX_axis  = (TextView)findViewById(R.id.tvX_axis);
		_tvY_axis = (TextView)findViewById(R.id.tvY_axis);
		_tvZ_axis = (TextView)findViewById(R.id.tvZ_axis);

		//dataDefault
		_tvDate.setText(dataDefault);
		_tvTime.setText(dataDefault);
		_tvLongitude.setText(dataDefault);
		_tvLatitude.setText(dataDefault);
		_tvX_axis.setText(dataDefault);
		_tvY_axis.setText(dataDefault);
		_tvZ_axis.setText(dataDefault);

		// configure BitmapFactory.Options for loading images
		options = new BitmapFactory.Options(); 
		options.inSampleSize = 4; // sample at 1/4 original width/height 

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	

	/* onActivityResult
	 * 
	 * Need to handle return from intent to photo picker - 
	 * should be an image file. The file name is got from the returning
	 * data of the intent. Some processing is required, in the background,
	 * to handle the image as a bitmap - and also, the database is queried with
	 * the file name.	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
		//super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

		//Bitmap selectedImage = null;
		String path = "";

		if (requestCode == SELECT_PHOTO ) {
			if(resultCode == RESULT_OK){

				// logger
				Log.d(logTag,"GalleryAct: onActivityResult: Image returned from picker");
				final Uri imageUri = imageReturnedIntent.getData();
				path = this.getRealPathFromURI(imageUri);
				//get file name for database
				File tmpFile = new File(path);
				imageFileName = tmpFile.getName();
				// logger
				Log.d(logTag,"GalleryAct: onActivityResult: FileName: " + imageFileName);
				//display filename
				//_tvImageNameName.setText(imageFileName);
				// logger
				Log.d(logTag,"GalleryAct: onActivityResult: Path to image: " + path);    			
				new LoadImageTask().execute(Uri.parse("file://" + path));
				//display filename
				_tvImageNameName.setText(imageFileName);
				
				AsyncTask<Object, Object, Cursor> readRecordTask = 
						new AsyncTask<Object, Object, Cursor>() 
						{
							ODDEssayDBHandler dbHandler = new ODDEssayDBHandler(GalleryActivity.this);
					
							@Override
							protected Cursor doInBackground(Object... params) 
				
							{
								dbHandler.open();
								return dbHandler.findRecord(imageFileName);
								
								//lookupProduct(imageFileName); 
								//return null;
							} // end method doInBackground
		
							@Override
							protected void onPostExecute(Cursor result) 
							{
								result.moveToFirst();
								Log.d(logTag, "Product is not null");

								
								// get column index value based on column name
								int date = result.getColumnIndex("Date");
								int time = result.getColumnIndex("Time");
								int longitude = result.getColumnIndex("Longitude");
								int latitude = result.getColumnIndex("Latitude");
								int xaxis = result.getColumnIndex("Xaxis");
								int yaxis = result.getColumnIndex("Yaxis");
								int zaxis = result.getColumnIndex("Zaxis");
								
								
								//_tvImageNameName.setText(record.getRecFileName());
								_tvDate.setText(result.getString(date));
								_tvTime.setText(result.getString(time));
								_tvLongitude.setText(result.getString(longitude));
								_tvLatitude.setText(result.getString(latitude));
								_tvX_axis.setText(result.getString(xaxis));
								_tvY_axis.setText(result.getString(yaxis));
								_tvZ_axis.setText(result.getString(zaxis));
								
								result.close(); // close the result cursor
						        dbHandler.close(); // close database connection
								
								//finish(); // return to the previous Activity
							} // end method onPostExecute
						}; // end AsyncTask

				// save the contact to the database using a separate thread
				readRecordTask.execute((Object[]) null); 

			}
		}
	}

	/* LoadImageTask
	 * 
	 * Processing Bitmaps can be time consuming; therefore running the
	 * code in a background task - hence have to use AsyncTask.
	 */
	private class LoadImageTask extends AsyncTask<Uri, Object, Bitmap>
	{
		// load images
		@Override
		protected Bitmap doInBackground(Uri... params)
		{
			return getBitmap(params[0], getContentResolver(), options);
			//return getBitMap2(params[0], getContentResolver(), options);

		}

		// set thumbnail on ListView
		@Override
		protected void onPostExecute(Bitmap result)
		{        
			Log.d(logTag,"GalleryAct: onPostExecute: trying to set bitmap");       	
			imageView.setImageBitmap(result);        	
			//display related data for image
			//lookupProduct(imageFileName);
		}

	}

	// utility method to get a Bitmap
	public Bitmap getBitMap2(Uri uri, ContentResolver cr, 
			BitmapFactory.Options options){

		String myJpgPath = getRealPathFromURI(uri);

		return BitmapFactory.decodeFile(myJpgPath, options);
	}

	// utility method to get a Bitmap from a Uri
	public Bitmap getBitmap(Uri uri, ContentResolver cr, 
			BitmapFactory.Options options)
	{
		Bitmap bitmap = null;

		// get the image
		try
		{
			// Logger
			Log.d(logTag,"GalleryAct: getBitmap: trying to openinpputstream");
			InputStream input = cr.openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(input, null, options);            
		} // end try
		catch (FileNotFoundException e) 
		{
			// Logger
			Log.d(logTag,"GalleryAct: getBitmap: outputstream error");
			Log.d(logTag, e.toString());
		} // end catch

		return bitmap;
	} // end method getBitmap

	// utility method to get a Path in String form
	public String getRealPathFromURI(Uri contentUri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
		if(cursor.moveToFirst()){;
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		res = cursor.getString(column_index);
		}        

		cursor.close();
		return res;
	}

	/* lookupProduct
	 * 
	 * The code responsible for query on database; uses the image filename.
	 * Not working at this time....aaaagh! so lots of debug code for logger
	 */	
	public void lookupProduct (String filename) {
		//logger
		Log.d(logTag, "GalleryActivity: lookupProduct: filename: " + filename);
		//ODDEssayDBHandler dbHandler = new ODDEssayDBHandler(this, null, null, 2);
		ODDEssayDBHandler dbHandler = new ODDEssayDBHandler(this);

		dbHandler.open();

		Cursor cursor = dbHandler.findRecord(filename);

		if(cursor != null){
			cursor.moveToFirst();
			Log.d(logTag, "Product is not null");

			
			// get column index value based on column name
			int date = cursor.getColumnIndex("Date");
			int time = cursor.getColumnIndex("Time");
			int longitude = cursor.getColumnIndex("Longitude");
			int latitude = cursor.getColumnIndex("Latitude");
			int xaxis = cursor.getColumnIndex("Xaxis");
			int yaxis = cursor.getColumnIndex("Yaxis");
			int zaxis = cursor.getColumnIndex("Zaxis");
			
			
			//_tvImageNameName.setText(record.getRecFileName());
			_tvDate.setText(cursor.getString(date));
			_tvTime.setText(cursor.getString(time));
			_tvLongitude.setText(cursor.getString(longitude));
			_tvLatitude.setText(cursor.getString(latitude));
			_tvX_axis.setText(cursor.getString(xaxis));
			_tvY_axis.setText(cursor.getString(yaxis));
			_tvZ_axis.setText(cursor.getString(zaxis));

			
		} else {
			//Logger
			Log.d(logTag, "GalleryActivity: read record from database: cursor is null");
			_tvImageNameName.setText("unknown");
		} 	
	}

}

