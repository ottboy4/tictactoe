package ott.time;

public class Stopwatch
{
	long time;
	long diff;
	
	public void start()
	{
		diff = 0;
		time = System.nanoTime();
	}
	
	/**
	 * sets time since last start was called
	 * @return
	 * the time in nano seconds
	 */
	public long stop()
	{
		diff = System.nanoTime() - time;
		return diff;
	}
	
	public long getNano()
	{
		return diff;
	}
	
	public double getMillis()
	{
		return diff / 1000000.0;
	}
	
	public double getSecond()
	{
		return getMillis() / 1000.0;
	}
	
	public void printTime(String timed)
	{
		stop();
		System.out.println(timed + " took: " + getSecond());
	}
	
	private static Stopwatch staticWatch = new Stopwatch();
	
	public static void staticStart()
	{
		staticWatch.start();
	}
	
	public static long staticStop()
	{
		return staticWatch.stop();
	}
	
	public static void staticPrintTime(String timed)
	{
		staticWatch.printTime(timed);
	}
	
}
