/*
 * ODDEssayDBHandler
 * 
 * Database Handler - responsible for creation of table in database; methods
 * for CRUD operations; 
 * 
 * Some problems so plenty of debugger code for logger. 
 * 
 * 
 *  @author Paul Clune
 *   
 *      Copyright 2015 Paul Clune
 
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

/*
 * CRUD operations
 * 	create 	- 	addRecord
 *  read	-	findRecord (single record)
 *  		-	getListOfRecords (multiple records)
 *  update	- 	updateRecord
 *  delete	-	deleteRecord
 *  
 */


package com.amadan.dublin.oddessay;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ODDEssayDBHandler{

	//LogCat Tag
	protected static String logTag = "ODDEssay :";
	//database details
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "oddesssayDB.db";
	private static final String TABLE_RECORDS = "records";
	//database columns
	//TODO refactor to put into an enum class ??
	private static final String COLUMN_ID = "ID";
	private static final String COLUMN_FILENAME = "FileName";
	private static final String COLUMN_DATE = "Date";
	private static final String COLUMN_TIME = "Time";
	private static final String COLUMN_LOCATION = "Location";
	private static final String COLUMN_LONGITUDE = "Longitude";
	private static final String COLUMN_LATITUDE = "Latitude";
	private static final String COLUMN_ALTITUDE = "Altitude";
	private static final String COLUMN_HEADING = "Heading";
	private static final String COLUMN_X_AXIS = "Xaxis";
	private static final String COLUMN_Y_AXIS = "Yaxis";
	private static final String COLUMN_Z_AXIS = "Zaxis";

	private SQLiteDatabase database; // database object
	private DatabaseOpenHelper databaseOpenHelper; // database helper

	//default constructor
	public ODDEssayDBHandler(Context context) {

		// create a new DatabaseOpenHelper
		databaseOpenHelper = 
				new DatabaseOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		// Logger
		Log.d(logTag, "dbHandler: Default Constructor");
	}


	// open the database connection
	public void open() throws SQLException 
	{
		// create or open a database for reading/writing
		database = databaseOpenHelper.getWritableDatabase();
	} // end method open

	// close the database connection
	public void close() 
	{
		if (database != null)
			database.close(); // close the database connection
	} // end method close

	
	// insert a single, new record into the database
	
	public void addRecord(Record record) {

		//logger
		Log.d(logTag,"dbHandler: addRecord()");

		ContentValues newRecord = new ContentValues();
		//values
		newRecord.put(COLUMN_FILENAME, record.getRecFileName());       
		newRecord.put(COLUMN_DATE, record.getRecDate());        
		newRecord.put(COLUMN_TIME, record.getRecTime());
		newRecord.put(COLUMN_LOCATION, record.getRecLocation());
		newRecord.put(COLUMN_LONGITUDE, record.getRecLongitude());
		newRecord.put(COLUMN_LATITUDE, record.getRecLatitude());
		newRecord.put(COLUMN_ALTITUDE, record.getRecAltitude());
		newRecord.put(COLUMN_HEADING, record.getRecHeading());
		newRecord.put(COLUMN_X_AXIS, record.getRecXaxis());
		newRecord.put(COLUMN_Y_AXIS, record.getRecYaxis());
		newRecord.put(COLUMN_Z_AXIS, record.getRecZaxis());

		//database
		open(); // open the database
		database.insert(TABLE_RECORDS, null, newRecord);
		close();

	}

	
	
	public Cursor findRecord(String filename){
				
		return database.query(
				TABLE_RECORDS, 
				null,
				COLUMN_FILENAME + "='" + filename + "'",
				null,null,null,null);		
	}
	
	
	
	/*
	 * to get a record based on id
	 */
	//TODO - refactor for new data object
	public Record getOneRecord(String filename) {
		//logger
		Log.d(logTag,"dbHandler: findRecord: filename: " + filename);
		
		
		Cursor cursor = database.query(
										TABLE_RECORDS, 
										null,
										COLUMN_FILENAME + "=" + filename ,
										null,null,null,null);
		//logger
		Log.d(logTag, "dbHandler: findRecord: Cursor number of rows: " + cursor.getCount());

		Record record = new Record();		

		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			Log.d(logTag, "dbHandler: findRecord: Inside cursor move to first");

			//add filename
			record.setRecFileName(cursor.getString(1));
			// add date
			record.setRecDate(cursor.getString(2));
			// add time
			record.setRecTime(cursor.getString(3));			
			// add longitude
			record.setRecLongitude(cursor.getString(5));
			Log.d(logTag, "dbHandler: findRecord: long: " + cursor.getString(5));
			// add latitude
			record.setRecLatitude(cursor.getString(6));
			Log.d(logTag, "dbHandler: findRecord: lat: " + cursor.getString(6));
			// add X-axis
			record.setRecXaxis(cursor.getString(9));
			// add Y-axis
			record.setRecYaxis(cursor.getString(10));
			// add Z-axis
			record.setRecZaxis(cursor.getString(11));
			/* Column Ordering (why did I make this s0 difficult??)
			 * 
					0 COLUMN_ID + " INTERGER PRIMARY KEY, " +
					1 COLUMN_FILENAME + " TEXT, " +
					2 COLUMN_DATE + " TEXT, " +
					3 COLUMN_TIME + " TEXT, " +
					4 COLUMN_LOCATION + " TEXT, " +
					5 COLUMN_LONGITUDE + " TEXT, " +
					6 COLUMN_LATITUDE + " TEXT, " +
					7 COLUMN_ALTITUDE + " TEXT, " +
					8 COLUMN_HEADING + " TEXT" + 
					9 COLUMN_X_AXIS + " TEXT" +
					10 COLUMN_Y_AXIS + " TEXT" +
					11 COLUMN_Z_AXIS + " TEXT" + ")";

			 */			
			cursor.close();
		} else {
			// Logger
			Log.d(logTag, "dbHandler: findRecord: cursor returning false");
			record = null;
		}
		close();
		return record;
	}


	/*
	 *to return a list of all records
	 * 
	 */
	//TODO - refactor for new data object
	/* -->>
	public List<Record> getListOfRecords(){

		ArrayList<Record> recordsList = new ArrayList<Record>();

		SQLiteDatabase db = this.getReadableDatabase();
		try{
			//query on database to get all records - with specific columns
			Cursor cursor = db.query(TABLE_RECORDS, new String[] {COLUMN_ID, COLUMN_FILENAME},
					null,null,null,null,null);
			try{	
				if(cursor != null){
					//int count = cursor.getCount();
					//loop through rows and add column values to new record
					//which is then added to the list
					if(cursor.moveToFirst()){
						do{
							Record tmpObj = new Record();
							tmpObj.setRecFileName(cursor.getString(1));
							recordsList.add(tmpObj);

						}while(cursor.moveToNext());
					}
				}

			} finally {
				try { cursor.close(); } catch (Exception ignore) {}
			}

		} finally {
			try { db.close(); } catch (Exception ignore) {}
		}
		return recordsList;
	}
	-->> */

	/*
	 *to delete a record 
	 */

	//TODO complete method to delete record

	/********************************************
	 * Actual class for DatabaseOpenHelper
	 */
	
	
	
	private class DatabaseOpenHelper extends SQLiteOpenHelper{

		// public constructor
		public DatabaseOpenHelper(Context context, String name,
				CursorFactory factory, int version) 
		{
			super(context, name, factory, version);
		} // end DatabaseOpenHelper constructor

		// creates the contacts table when the database is created
		@Override
		public void onCreate(SQLiteDatabase db) 
		{

			// Logger
			Log.d(logTag, "dbHandler: onCreate()");

			String CREATE_RECORDS_TABLE = "CREATE TABLE " +
					TABLE_RECORDS + "(" +
					COLUMN_ID + " INTERGER PRIMARY KEY, " +
					COLUMN_FILENAME + " TEXT, " +
					COLUMN_DATE + " TEXT, " +
					COLUMN_TIME + " TEXT, " +
					COLUMN_LOCATION + " TEXT, " +
					COLUMN_LONGITUDE + " TEXT, " +
					COLUMN_LATITUDE + " TEXT, " +
					COLUMN_ALTITUDE + " TEXT, " +
					COLUMN_HEADING + " TEXT," + 
					COLUMN_X_AXIS + " TEXT," +
					COLUMN_Y_AXIS + " TEXT," +
					COLUMN_Z_AXIS + " TEXT" + ");";

			db.execSQL(CREATE_RECORDS_TABLE); // execute the query
		} // end method onCreate

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, 
				int newVersion) 
		{

			db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
			onCreate(db);		
		} // end method onUpgrade

	} // class DBOpenHelper

}
