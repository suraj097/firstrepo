package com.auto.framework.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JSWaiter
{

	private static WebDriver			jsWaitDriver;
	private static WebDriverWait		jsWait;
	private static JavascriptExecutor	jsExec;
	private static final long			TIMEOUT_IN_SECONDS	= 30;

	private JSWaiter()
	{
	}

	// Get the driver
	public static void setDriver(WebDriver driver)
	{
		jsWaitDriver = driver;
		jsWait = new WebDriverWait(jsWaitDriver, TIMEOUT_IN_SECONDS);
		jsExec = (JavascriptExecutor) jsWaitDriver;
	}

	// Wait for JQuery Load
	public static void waitForJQueryLoad()
	{
		// Wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = driver -> ((Long) ((JavascriptExecutor) jsWaitDriver)
			.executeScript("return jQuery.active") == 0);
		// Get JQuery is Ready
		boolean jqueryReady = (Boolean) jsExec.executeScript("return jQuery.active==0");
		// Wait JQuery until it is Ready!
		if (!jqueryReady)
		{
			// Wait for jQuery to load
			jsWait.until(jQueryLoad);
		}
	}

	// Wait for Angular Load
	public static void waitForAngularLoad()
	{
		WebDriverWait wait = new WebDriverWait(jsWaitDriver, TIMEOUT_IN_SECONDS);
		JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;
		String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";
		// Wait for ANGULAR to load
		ExpectedCondition<Boolean> angularLoad = driver -> Boolean
			.valueOf(((JavascriptExecutor) driver).executeScript(angularReadyScript).toString());
		// Get Angular is Ready
		boolean angularReady = Boolean.parseBoolean(jsExec.executeScript(angularReadyScript).toString());
		// Wait ANGULAR until it is Ready!
		if (!angularReady)
		{
			// Wait for Angular to load
			wait.until(angularLoad);
		}
	}

	// Wait Until JS Ready
	public static void waitUntilJSReady()
	{
		waitUntilJSReady(jsWaitDriver);
	}

	// Wait Until JS Ready
	public static void waitUntilJSReady(WebDriver webDriver)
	{
		if (webDriver != null)
		{
			jsWaitDriver = webDriver;
			jsWait = new WebDriverWait(jsWaitDriver, TIMEOUT_IN_SECONDS);
		}
		WebDriverWait wait = new WebDriverWait(jsWaitDriver, 15);
		JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;
		// Wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) jsWaitDriver)
			.executeScript("return document.readyState").toString().equals("complete");
		// Get JS is Ready
		boolean jsReady = (Boolean) jsExec.executeScript("return document.readyState").toString()
			.equals("complete");
		// Wait Javascript until it is Ready!
		if (!jsReady)
		{
			// Wait for Javascript to load
			wait.until(jsLoad);
		}
	}

	// Wait Until JQuery and JS Ready
	public static void waitUntilJQueryReady()
	{
		JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;
		// First check that JQuery is defined on the page. If it is, then wait AJAX
		Boolean jQueryDefined = (Boolean) jsExec.executeScript("return typeof jQuery != 'undefined'");
		if (jQueryDefined)
		{
			// Pre Wait for stability (Optional)
			sleep(5);
			// Wait JQuery Load
			waitForJQueryLoad();
			// Wait JS Load
			waitUntilJSReady();
			// Post Wait for stability (Optional)
			sleep(5);
		}
	}

	// Wait Until Angular and JS Ready
	public static void waitUntilAngularReady()
	{
		JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;
		// First check that ANGULAR is defined on the page. If it is, then wait ANGULAR
		Boolean angularUnDefined = (Boolean) jsExec.executeScript("return window.angular === undefined");
		if (!angularUnDefined)
		{
			Boolean angularInjectorUnDefined = (Boolean) jsExec
				.executeScript("return angular.element(document).injector() === undefined");
			if (!angularInjectorUnDefined)
			{
				// Pre Wait for stability (Optional)
				sleep(20);
				// Wait Angular Load
				waitForAngularLoad();
				// Wait JS Load
				waitUntilJSReady();
				// Post Wait for stability (Optional)
				sleep(20);
			}
		}
	}

	// Wait Until JQuery Angular and JS is ready
	public static void waitJQueryAngular(WebDriver driver)
	{
		setDriver(driver);
		waitUntilJQueryReady();
		waitUntilAngularReady();
	}

	public static void sleep(Integer seconds)
	{
		long secondsLong = (long) seconds;
		try
		{
			Thread.sleep(secondsLong);
		}
		catch (Exception e)
		{
			LogManager.getLogger(ExceptionUtils.getStackTrace(e));
		}
	}
}
