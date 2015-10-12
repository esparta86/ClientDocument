package com.mitratech.resources;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Time {
	
	private Timer timer = new Timer(); 
    private int seconds=0;
    private ArrayList<Integer> ArraytimePerDocument = new ArrayList<Integer>();
    private long hours,minutes,secondsL;
    
    
    
    class Count extends TimerTask {
        public void run() {
            seconds++;
            String msj = "Transaction time per Document (Seconds):"+seconds+" \r";
            System.out.print(msj);
        }
    }
    
    public void setTimeInArray(int seconds){
    	this.ArraytimePerDocument.add(seconds);
    }
    
    public ArrayList<Integer> getArraytimePerDocument(){
    	return this.ArraytimePerDocument;    	
    }
    
    public Integer getTotalUploadTime(){
        int totalSeconds = 0;
    	for(Integer sec : this.ArraytimePerDocument){
        	totalSeconds = totalSeconds + sec;
        }
    	return totalSeconds;
    }
    
    public void Calculate(){
        this.seconds=0;
        timer = new Timer();
        timer.schedule(new Count(), 0, 1000);
        
    }
    
    public void stop() {
        timer.cancel();
        setTimeInArray(getSeconds());
    }
    
    public int getSeconds(){
        return this.seconds;
    }
    
    public String getStringFormat(){
    	 hours = getTotalUploadTime() / 3600;
	     minutes = (getTotalUploadTime() % 3600) / 60;
	     secondsL = getTotalUploadTime() % 60;
	     return String.format("%02d:%02d:%02d", hours, minutes, secondsL);	     
    }
 }
