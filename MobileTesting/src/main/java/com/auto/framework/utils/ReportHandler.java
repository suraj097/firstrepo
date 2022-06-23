package com.auto.framework.utils;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.testng.ITestResult;
import com.auto.framework.base.BaseTestCase;
import com.auto.framework.tools.Tool;
import com.auto.framework.tools.UITestTool;
import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;

/**
 * Utility to display Test Automation Dashboard and generate test summary report
 * 
 * @author naini.ghai
 * 
 */
public class ReportHandler
{
	private static final String			FONT_COLOR_TEAL		= "<FONT COLOR=TEAL>";
	private static final String			STACKTRACE			= "<FONT COLOR=GRAY><U><b>StackTrace</b></U>:= ";
	private static final String			TEST_OUTPUT			= "/test-output/";
	private static final String			USER_DIR			= "user.dir";
	private static final String			END_FONT			= "</FONT>";
	private static final String			BR					= "<BR>";
	private ExtentReports				extent;
	protected ThreadLocal<ExtentTest>	threadTestLogger	= new ThreadLocal<ExtentTest>();
	public static final String			OUTPUT_DIRECTORY	= new SimpleDateFormat(
		CalendarUtils.TIME_FORMAT_REPORT_NAME).format(new GregorianCalendar().getTime());
	private String						strReportPath		= null;

	/**
	 * Initiate Report
	 * @param strApplication
	 */
	public void initiateReport(BaseTestCase baseTestCase, String strApplication)
	{
		// ExtentReports(String filePath,Boolean replaceExisting)
		// filepath - path of the file, in .htm or .html format - path where your report needs to generate.
		// replaceExisting - Setting to overwrite (TRUE) the existing file or append to it
		// True (default): the file will be replaced with brand new markup, and all existing data will be lost. Use
		// this option to create a brand new report
		// False: existing data will remain, new tests will be appended to the existing report. If the the supplied
		// path does not exist, a new file will be created.
		if (extent == null)
		{
			IOUtils.createDirectory(System.getProperty(USER_DIR) + TEST_OUTPUT, OUTPUT_DIRECTORY);
			strReportPath = TEST_OUTPUT + OUTPUT_DIRECTORY + "/AutomationReport" + strApplication + ".html";
			String strAbsolutePath = System.getProperty(USER_DIR) + strReportPath;
			ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(strAbsolutePath);
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
			htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
			htmlReporter.config().setChartVisibilityOnOpen(true);
			htmlReporter.config().setCSS("css-string");
			htmlReporter.config().setJS("js-string");
			htmlReporter.config().setDocumentTitle("AutomationReport" + strApplication);
			htmlReporter.config().setEncoding("UTF-8");
			htmlReporter.config().setReportName("AutomationReport" + strApplication);
			extent.setAnalysisStrategy(AnalysisStrategy.TEST);
			String strBuildNumber = System.getProperty("BUILD_NUMBER");
			extent.setSystemInfo("BUILD NUMBER", (strBuildNumber != null) ? strBuildNumber : "SNAPSHOT");
			String strBrowser = baseTestCase.getProperties().getRootPropertyValue("BROWSER");
			extent.setSystemInfo("BROWSER", strBrowser);
			String strEnv = baseTestCase.getProperties().getRootPropertyValue("ENV_CONFIG");
			extent.setSystemInfo("ENVIRONMENT", strEnv);
			extent.setSystemInfo("OS", System.getProperty("os.name"));
			extent.setSystemInfo("USER", System.getProperty("user.name"));
		}
	}

	/**
	 * Start Logger before every test
	 * @param method
	 */
	public ExtentTest startLogger(String strMethod, BaseTestCase baseTestCase)
	{
		// TestCaseName ï¿½ Name of the test
		// Description ï¿½ Description of the test
		// Starting test
		threadTestLogger.set(extent.createTest("<FONT COLOR='#85C1E9'>" + baseTestCase.getClass().getName() + "." +
			END_FONT + "<FONT COlOR='#2E86C1'>" + strMethod + END_FONT));
		return threadTestLogger.get();
	}

	/**
	 * End Logger after every test and prepare to create HTML Report
	 * @param result
	 */
	public void endLogger(ITestResult result, BaseTestCase baseTestCase)
	{
		Logger logger = baseTestCase.getLogger();
		String strOptional = (result.getTestContext()
			.getAttribute(Thread.currentThread().getName() + "." + baseTestCase.getCurrentMethod() +
				"_OPTIONAL_MESSAGE") != null)
					? result.getTestContext().getAttribute(Thread.currentThread().getName() + "." +
						baseTestCase.getCurrentMethod() + "_OPTIONAL_MESSAGE").toString()
					: "";
		if (result.getStatus() == ITestResult.FAILURE)
		{
			UITestTool ttUI = (UITestTool) baseTestCase.getTool();
			if (ttUI != null)
			{
				logFailureToReportWithSnapshot(result, baseTestCase, strOptional);
			}
			else
			{
				logFailureToReport(result, strOptional);
			}

			logger.info("[" + baseTestCase.getClass().getName() + "][" + baseTestCase.getCurrentMethod() +
				"] FAIL - Error!!! " +
				(result.getThrowable().getMessage() + END_FONT +
					((strOptional != null && !strOptional.equals(""))
						? FONT_COLOR_TEAL + strOptional + END_FONT + BR : "") +
					ExceptionUtils.getStackTrace(result.getThrowable())));
		}
		else if (result.getStatus() == ITestResult.SKIP)
		{
			threadTestLogger.get().log(Status.SKIP,
				"<b><FONT COLOR='DARKORANGE'>Skipped!!!</b> Test Case Skipped is " + result.getName() + END_FONT +
					BR + result.getThrowable().getMessage() + " <BR>" +
					((strOptional != null && !strOptional.equals(""))
						? FONT_COLOR_TEAL + strOptional + END_FONT + BR : "") +
					STACKTRACE + ExceptionUtils.getStackTrace(result.getThrowable()));
			logger.info("[" + baseTestCase.getClass().getName() + "][" + baseTestCase.getCurrentMethod() +
				"] FAIL - Test Case skipped<BR>" + result.getThrowable().getMessage() +
				BR + ((strOptional != null && !strOptional.equals(""))
					? FONT_COLOR_TEAL + strOptional + END_FONT + BR : "") +
				STACKTRACE + ExceptionUtils.getStackTrace(result.getThrowable()));
		}
		// ending test
		// endTest(logger) : It ends the current test and prepares to create HTML report
	}

	private void logFailureToReportWithSnapshot(ITestResult result, BaseTestCase baseTestCase, String strOptional)
	{
		ScreenshotHandler screenshotHandler = new ScreenshotHandler();
		StringBuilder strbStepLogger = new StringBuilder();
		strbStepLogger
			.append("<FONT COLOR=RED><b>Error!!!</b> " + result.getThrowable().getMessage() + END_FONT + BR);
		strbStepLogger.append(
			(strOptional != null && !strOptional.equals("")) ? FONT_COLOR_TEAL + strOptional + END_FONT + BR : "");
		strbStepLogger.append(STACKTRACE + ExceptionUtils.getStackTrace(result.getThrowable()) + END_FONT + BR);
		List<Tool> lstTool = baseTestCase.getTools();
		for (Tool tool : lstTool)
		{
			if (tool != null)
			{
				String strScreenshot = screenshotHandler.takeSpanshot(baseTestCase, tool);
				if (!strScreenshot.equals(""))
				{
					String strRelativeScreenshot = "." + strScreenshot.substring(strScreenshot.indexOf("/com"));
					strbStepLogger.append("<A HREF='" + strRelativeScreenshot +
						"' target='_blank'><FONT COlOR='BLACK'><b><u>Screenshot</u></b></A>&nbsp");
				}
			}
		}
		threadTestLogger.get().log(Status.FAIL, strbStepLogger.toString());
	}

	private void logFailureToReport(ITestResult result, String strOptional)
	{
		threadTestLogger.get().log(Status.FAIL,
			("<FONT COLOR=RED><b>Error!!!</b> " + result.getThrowable().getMessage() + END_FONT +
				BR + ((strOptional != null && !strOptional.equals(""))
					? FONT_COLOR_TEAL + strOptional + END_FONT + BR : "") +
				STACKTRACE + ExceptionUtils.getStackTrace(result.getThrowable()) + END_FONT));
	}

	/**
	 * Write everything to document, clear all resources and close the reporting operation
	 */
	public void endReport(BaseTestCase baseTestCase)
	{
		// writing everything to document
		// flush() - to write or update test information to your report.
		try
		{
			flushReport();
			Logger logger = baseTestCase.getLogger();
			logger.info("Generating Report .... ");
			// Call close() at the very end of your session to clear all resources.
			// If any of your test ended abruptly causing any side-affects (not all logs sent to ExtentReports,
			// information missing), this method will ensure that the test is still appended to the report with a
			// warning message.
			// You should call close() only once, at the very end (in @AfterSuite for example) as it closes the
			// underlying stream.
			// Once this method is called, calling any Extent method will throw an error.
			// close() - To close all the operation

			Thread.sleep(5000);
			logger.info("Report Generated .... " + System.getProperty(USER_DIR) + strReportPath);
		}
		catch (Exception ex)
		{
			// do nothing
		}

	}

	public ExtentTest getLogger()
	{
		return threadTestLogger.get();
	}

	public void flushReport()
	{
		if (extent != null)
		{
			extent.flush();
		}
	}

}
