/*** BaseActivity Class
 * 
 *   @author Paul Clune
 *   
 *   Holds common code for all activity classes - 
 *   such as NavigationDrawer and Menus code.
 *   Includes instantiation of SQLite Database object,
 *   for use by application;
 *   
    	Copyright 2015 Paul Clune
 
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

public class BaseActivity extends Activity {

	//LogCat Tag
	protected static String logTag = "ODDEssay :";
		
	// required for structure of navigation drawer
	protected FrameLayout frameLayout;
	protected ListView mDrawerList;
	protected String[] mDrawerArray;

	protected static int position;
	private static boolean isLaunch = true;

	private DrawerLayout mDrawerLayout;	
	private ActionBarDrawerToggle mDrawerToggle;

	//Database
	//protected static ODDEssayDBHandler dbHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		//LogCat output - Logger
		Log.d(logTag, "BaseActivity: onCreate");

		setContentView(R.layout.drawer);

		frameLayout = (FrameLayout)findViewById(R.id.content_frame);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		//array of strings to populate drawer
		mDrawerArray = getResources().getStringArray(R.array.drawer_array);		

		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mDrawerArray));

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer,R.string.drawer_open,
				R.string.drawer_close){

			@Override
			public void onDrawerClosed(View drawerView){
				getActionBar().setTitle(mDrawerArray[position]);
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView){
				getActionBar().setTitle(getString(R.string.app_name));
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);	

		if(isLaunch){
			isLaunch = false;
			selectItem(0);
		}

		//Instantiate our database, if one does not already exist!
		//if(dbHandler == null){		
		//	dbHandler = new ODDEssayDBHandler(this, null, null, 2);
		//dbHandler = new ODDEssayDBHandler(this);
		//}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(mDrawerToggle.onOptionsItemSelected(item)){
			return true;
		}

		return super.onOptionsItemSelected(item);		
	};

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update selected item and title, then close the drawer
		mDrawerLayout.closeDrawer(mDrawerList);
		//Setting currently selected position in this field so that it will be available in our child activities.
		BaseActivity.position = position;  

		switch(position)
		{
		case 0:	//home
			Toast.makeText(getApplicationContext(), "Some Activities MAY take time to load. Please be Patient.", Toast.LENGTH_LONG).show();   
			Intent a = new Intent(BaseActivity.this, HomeActivity.class);
			startActivity(a);
			break;
		case 1: //gallery
			Toast.makeText(getApplicationContext(), "Some Activities MAY take time to load. Please be Patient.", Toast.LENGTH_LONG).show();   
			Intent b = new Intent(BaseActivity.this, GalleryActivity.class);
			startActivity(b);
			break;
		case 2: //calendar
			Toast.makeText(getApplicationContext(), "Some Activities MAY take time to load. Please be Patient.", Toast.LENGTH_LONG).show();   
			Intent c = new Intent(BaseActivity.this, CalenderActivity.class);
			startActivity(c);
			break;
		case 3:	//map
			//Toast.makeText(getApplicationContext(), "Some Activities MAY take time to load. Please be Patient.", Toast.LENGTH_LONG).show();   
			Intent d = new Intent(BaseActivity.this, MapActivity.class);
			startActivity(d);
			break;
		default:
			break;

		}
		/*
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerArray[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
		 */
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/* We can override onBackPressed method to toggle navigation drawer*/
	@Override
	public void onBackPressed() {
		if(mDrawerLayout.isDrawerOpen(mDrawerList)){
			mDrawerLayout.closeDrawer(mDrawerList);
		}else {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	}

}

