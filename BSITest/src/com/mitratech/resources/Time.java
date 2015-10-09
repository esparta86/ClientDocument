package com.mitratech.resources;

import java.util.Timer;
import java.util.TimerTask;

public class Time {
	
	private Timer timer = new Timer(); 
    private int seconds=0;
    
    class Count extends TimerTask {
        public void run() {
            seconds++;
            System.out.println("segundo: " + seconds);
        }
    }
    
    public void Calculate()
    {
        this.seconds=0;
        timer = new Timer();
        timer.schedule(new Count(), 0, 1000);
    }
    
    public void stop() {
        timer.cancel();
    }
    
    public int getSeconds()
    {
        return this.seconds;
    }
    
    
    
    

}
