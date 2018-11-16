package edu.ycp.cs481.model;

import java.util.Date;

public class Shift{
	private Date timeIn, timeOut;
	private int hours;
	
	public Shift(Date timeIn, Date timeOut, int hours){
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.hours = hours;
	}
	
	public Date getTimeIn(){
		return timeIn;
	}
	
	public Date getTimeOut(){
		return timeOut;
	}
	
	public int getHours(){
		return hours;
	}
}
