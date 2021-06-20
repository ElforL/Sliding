package Elfor;

public class StopWatch {
	long startTime;
	long stopTime;
	boolean isRunning;
	
	public StopWatch() {
		startTime = 0;
		stopTime = 0;
		isRunning = false;
	}
	
	public void start() {
			startTime = System.currentTimeMillis();
			stopTime = 0;
			isRunning = true;
	}
	
	public void stop() {
			stopTime = System.currentTimeMillis();
			isRunning = false;
	}
	
	public long getElapsedTime() {
		long elapsed;
		if(isRunning) {
			elapsed = (System.currentTimeMillis() - startTime) / 1000;
		}else {
			elapsed = (stopTime - startTime)/1000;
		}
		return elapsed;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
}
