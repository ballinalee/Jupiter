/*** CalenderActivity Class
 * 
 * 	@author Paul Clune
 * 
 *	Class for the Calendar Activity - 
 *	TODO Complete class
 *   
 *     	Copyright 2015 Paul Clune
 
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

public class CalenderActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.activity_calender, frameLayout);
		
		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerArray[position]);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
