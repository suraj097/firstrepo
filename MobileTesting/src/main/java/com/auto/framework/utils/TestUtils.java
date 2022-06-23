package com.auto.framework.utils;

public class TestUtils
{
	private TestUtils()
	{
		// do nothing
	}

	/**
	 * Hold test execution for specified duration (milliseconds)
	 * 
	 * @param nMiliSeconds
	 */
	public static void sleep(int nMilliSeconds)
	{
		try
		{
			Thread.sleep(nMilliSeconds);
		}
		catch (Exception ex)
		{
			// do nothing
		}
	}
}
