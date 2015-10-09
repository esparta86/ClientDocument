package com.mitratech.resources;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Time {
	
	private Timer timer = new Timer(); 
    private int seconds=0;
    private ArrayList<Integer> ArraytimePerDocument = new ArrayList<Integer>();
    
    class Count extends TimerTask {
        public void run() {
            seconds++;
            System.out.println("transaction time (Seconds): " + seconds);
        }
    }
    
    public void setTimeInArray(int seconds){
    	this.ArraytimePerDocument.add(seconds);
    }
    
    public Integer getTotalUploadTime(){
        int totalSeconds = 0;
    	for(Integer sec : this.ArraytimePerDocument){
        	totalSeconds = totalSeconds + sec;
        }
    	return totalSeconds;
    }
    
    public void Calculate()
    {
        this.seconds=0;
        timer = new Timer();
        timer.schedule(new Count(), 0, 1000);
    }
    
    public void stop() {
        timer.cancel();
        setTimeInArray(getSeconds());
    }
    
    public int getSeconds()
    {
        return this.seconds;
    }
    
 }
