package edu.ycp.cs481.model;

import java.util.Date;

public class ClockTime{
	private Date time;
	private boolean in;
	
	public ClockTime(Date time, boolean in){
		this.time = time;
		this.in = in;
	}
	
	public Date getTime(){
		return time;
	}
	
	public boolean getIn(){
		return in;
	}
}
