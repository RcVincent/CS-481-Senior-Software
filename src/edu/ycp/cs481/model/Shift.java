package edu.ycp.cs481.model;

import java.sql.Timestamp;

public class Shift{
	private Timestamp timeIn, timeOut;
	private int hours;
	
	public Shift(Timestamp timeIn, Timestamp timeOut, int hours){
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.hours = hours;
	}
	
	public Timestamp getTimeIn(){
		return timeIn;
	}
	
	public Timestamp getTimeOut(){
		return timeOut;
	}
	
	public int getHours(){
		return hours;
	}
}
