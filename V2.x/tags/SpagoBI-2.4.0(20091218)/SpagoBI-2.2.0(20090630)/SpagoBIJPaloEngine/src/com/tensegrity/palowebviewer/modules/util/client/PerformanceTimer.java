package com.tensegrity.palowebviewer.modules.util.client;



/**
 * Timer to measure performance of a part of code.
 *
 */
public class PerformanceTimer
{

    private static long treshhold = 20;
    private static long slowTreshhold = 1000;

    private final String name;
    private long start;
    private long stop;
    
    public static void setTreshhold(long value) {
        treshhold = value;
    }

    public static void setSlowTreshhold(long value) {
        slowTreshhold = value;
    }

    public void start() {
        stop = 0;
        start = System.currentTimeMillis();
    }

    public void stop() {
        if(stop == 0)
            stop = System.currentTimeMillis();
        else
            Logger.warn(this+" warn: stop called two times without start.");
    }

    public long getDuration() {
        return stop - start;
    }

    public void report(String result) {
        stop();
        long duration = getDuration();
        if(duration > treshhold){
            String message =  getName();
            if(result != null)
            	message += "{result: " + result + "}";
            message += " = "+ duration +"ms";
            if(duration <= slowTreshhold)
                Logger.info(message);
            else
                Logger.warn("[SLOW]"+message);
        }
    }
    
    public void report() {
    	report(null);
    }


    public String getName() {
        return name;
    }

    public PerformanceTimer (String name) {
        this.name = name;
    }

    public String toString() {
        return "PerformanceTimer["+getName()+"]";
    }

}
