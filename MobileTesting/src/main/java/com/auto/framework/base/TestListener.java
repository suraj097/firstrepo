package com.auto.framework.base;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.auto.framework.utils.ReportHandler;

/**
 * Tracks the test execution of the Test Suite
 * 
 * @author naini.ghai
 *
 */
public class TestListener implements ISuiteListener, ITestListener
{
	private static ThreadLocal<ISuite>	threadSuite	= new ThreadLocal<ISuite>();
	private static int					nSuiteCount	= 0;

	public static ISuite getAccess()
	{
		return threadSuite.get();
	}

	public static int getSuiteCount()
	{
		return nSuiteCount;
	}

	public void onFinish(ISuite suite)
	{
		threadSuite.set(null);
		TestListener.nSuiteCount--;
	}

	public void onStart(ISuite arg0)
	{
		threadSuite.set(arg0);
		TestListener.nSuiteCount++;
	}

	public void onTestStart(ITestResult result)
	{
	}

	public void onTestSuccess(ITestResult result)
	{
	}

	public void onTestFailure(ITestResult result)
	{
		logTestFailure(result);
	}

	public void onTestSkipped(ITestResult result)
	{
		logTestFailure(result);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result)
	{
	}

	public void onStart(ITestContext context)
	{
	}

	public void onFinish(ITestContext context)
	{
	}

	private void logTestFailure(ITestResult result)
	{
		BaseTestCase baseTestCase = (BaseTestCase) result.getTestContext()
			.getAttribute(Thread.currentThread().getName());
		ReportHandler reportHandler = baseTestCase.getReportHandler();
		if (reportHandler != null)
		{
			reportHandler.endLogger(result, baseTestCase);
		}
	}
}
