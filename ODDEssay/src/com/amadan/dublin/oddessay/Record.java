/**
 * Record class
 * 
 * @author Paul Clune 
 * 
 * Responsible for defining record structure used in database.
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
 * 
 */

package com.amadan.dublin.oddessay;

public class Record {
		
	//Record elements, with default values
	// At this time keeping everything as a String
	private String recDefaultVal = "default";
	//recID will act as a unique primary key, based on date\time stamp
	private String recID = recDefaultVal;
	//image file name
	private String recFileName = recDefaultVal;
	//record date
	private String recDate = recDefaultVal;
	//record time
	private String recTime = recDefaultVal;
	//record location
	private String recLocation = recDefaultVal;
	//record longitude
	private String recLongitude = recDefaultVal;
	//record latitude
	private String recLatitude = recDefaultVal;
	//record altitude
	private String recAltitude = recDefaultVal;
	//record heading
	private String recHeading = recDefaultVal;
	//device orientation - x-axis
	private String recXaxis = recDefaultVal;
	//device orientation - x-axis
	private String recYaxis = recDefaultVal;
	//device orientation - x-axis
	private String recZaxis = recDefaultVal;

	/*
	 * constructors
	 */
	

	/*
	 * Default
	 */
	public Record(){
		
	}		
	
	/**
	 * @param recID
	 * @param recFileName
	 * @param recDate
	 * @param recTime
	 * @param recLocation
	 * @param recLongitude
	 * @param recLatitude
	 * @param recAltitude
	 * @param recHeading
	 * @param recXaxis TODO
	 * @param recYaxis TODO
	 * @param recZaxis TODO
	 */
	public Record(String recID, String recFileName, String recDate,
			String recTime, String recLocation, String recLongitude,
			String recLatitude, String recAltitude, String recHeading, 
			String recXaxis, String recYaxis, String recZaxis) {
		this.recID = recID;
		this.recFileName = recFileName;
		this.recDate = recDate;
		this.recTime = recTime;
		this.recLocation = recLocation;
		this.recLongitude = recLongitude;
		this.recLatitude = recLatitude;
		this.recAltitude = recAltitude;
		this.recHeading = recHeading;
		this.recXaxis = recXaxis;
		this.recYaxis = recYaxis;
		this.recZaxis = recZaxis;
	}
	
	//Getters and Setters
	
	/**
	 * @return the recXaxis
	 */
	String getRecXaxis() {
		return recXaxis;
	}

	/**
	 * @param recXaxis the recXaxis to set
	 */
	void setRecXaxis(String recXaxis) {
		this.recXaxis = recXaxis;
	}

	/**
	 * @return the recYaxis
	 */
	String getRecYaxis() {
		return recYaxis;
	}

	/**
	 * @param recYaxis the recYaxis to set
	 */
	void setRecYaxis(String recYaxis) {
		this.recYaxis = recYaxis;
	}

	/**
	 * @return the recZaxis
	 */
	String getRecZaxis() {
		return recZaxis;
	}

	/**
	 * @param recZaxis the recZaxis to set
	 */
	void setRecZaxis(String recZaxis) {
		this.recZaxis = recZaxis;
	}

	/**
	 * @return the recID
	 */
	String getRecID() {
		return recID;
	}
	/**
	 * @param recID the recID to set
	 */
	void setRecID(String recID) {
		this.recID = recID;
	}
	/**
	 * @return the recFileName
	 */
	String getRecFileName() {
		return recFileName;
	}
	/**
	 * @param recFileName the recFileName to set
	 */
	void setRecFileName(String recFileName) {
		this.recFileName = recFileName;
	}
	/**
	 * @return the recDate
	 */
	String getRecDate() {
		return recDate;
	}
	/**
	 * @param recDate the recDate to set
	 */
	void setRecDate(String recDate) {
		this.recDate = recDate;
	}
	/**
	 * @return the recTime
	 */
	String getRecTime() {
		return recTime;
	}
	/**
	 * @param recTime the recTime to set
	 */
	void setRecTime(String recTime) {
		this.recTime = recTime;
	}
	/**
	 * @return the recLocation
	 */
	String getRecLocation() {
		return recLocation;
	}
	/**
	 * @param recLocation the recLocation to set
	 */
	void setRecLocation(String recLocation) {
		this.recLocation = recLocation;
	}
	/**
	 * @return the recLongitude
	 */
	String getRecLongitude() {
		return recLongitude;
	}
	/**
	 * @param recLongitude the recLongitude to set
	 */
	void setRecLongitude(String recLongitude) {
		this.recLongitude = recLongitude;
	}
	/**
	 * @return the recLatitude
	 */
	String getRecLatitude() {
		return recLatitude;
	}
	/**
	 * @param recLatitude the recLatitude to set
	 */
	void setRecLatitude(String recLatitude) {
		this.recLatitude = recLatitude;
	}
	/**
	 * @return the recAltitude
	 */
	String getRecAltitude() {
		return recAltitude;
	}
	/**
	 * @param recAltitude the recAltitude to set
	 */
	void setRecAltitude(String recAltitude) {
		this.recAltitude = recAltitude;
	}
	/**
	 * @return the recHeading
	 */
	String getRecHeading() {
		return recHeading;
	}
	/**
	 * @param recHeading the recHeading to set
	 */
	void setRecHeading(String recHeading) {
		this.recHeading = recHeading;
	}	

}
