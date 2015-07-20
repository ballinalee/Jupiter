/*** MapActivity Class
 * 
 *	Class for the Map Activity - 
 *	TODO Complete class
 *   
 *   @author Paul Clune
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

import android.os.Bundle;

public class MapActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.activity_map, frameLayout);
		
		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerArray[position]);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
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
	*/
}
