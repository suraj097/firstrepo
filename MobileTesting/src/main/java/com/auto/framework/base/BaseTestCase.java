package com.auto.framework.base;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.auto.framework.test.utils.TestCase;
import com.auto.framework.tools.DBTestTool;
import com.auto.framework.tools.MobileTestTool;
import com.auto.framework.tools.Tool;
import com.auto.framework.tools.UITestTool;
import com.auto.framework.utils.CalendarUtils;
import com.auto.framework.utils.IOUtils;
import com.auto.framework.utils.ReportHandler;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.utils.ExceptionUtil;

/**
 * This is the base class that takes care of initializing reporting engine and loggers and destroying the same at
 * the end of the test execution and closing any open browsers. <br>
 * <br>
 * <i><u>Please note</u></i> - Every test should extend this class <b>For. Ex.</b> public class LoginTest extends
 * BaseTestCase
 * 
 * @author Suresh Yadav.
 * 
 */
@Listeners(TestListener.class)
public class BaseTestCase
{
	private ThreadLocal<String>			threadMethod		= new ThreadLocal<String>();
	public static ReportHandler			reportHandler		= null;
	protected ThreadLocal<Logger>		threadLogger		= ThreadLocal.withInitial(LogManager::getRootLogger);
	private ThreadLocal<TestCase>		threadTestCase		= ThreadLocal.withInitial(TestCase::new);
	protected ThreadLocal<ExtentTest>	threadTestLogger	= new ThreadLocal<ExtentTest>();
	private ThreadLocal<List<Tool>>		threadTools			= ThreadLocal
		.withInitial(CopyOnWriteArrayList<Tool>::new);
	private ThreadLocal<TestProperties>	threadProperties	= ThreadLocal.withInitial(TestProperties::new);
	private final ReentrantLock			reetrantLock		= new ReentrantLock(true);
	private TestCase					testCase			= new TestCase();
	private String						strFileDownloadPath	= "";
	private String						strJenkinsLabel		= "";
	private ITestContext				testContext			= null;
	private String						strApp				= "";

	@BeforeSuite(alwaysRun = true)
	@Parameters({"application"})
	public void onSuiteStart(ITestContext context, @Optional("") String strApplication)
	{
		try
		{
			synchronized (reetrantLock)
			{
				threadProperties.set(threadProperties.get().getInstance());
				setContext(context);

				reetrantLock.lock();
				if (reportHandler == null)
				{
					System.setProperty("log4j.configurationFile",
						System.getProperty("user.dir") + "/src/main/resources/log4j.properties");
					reportHandler = new ReportHandler();
					reportHandler.initiateReport(this, strApplication);
					threadLogger.set(LogManager.getRootLogger());
					threadLogger.get().info("Test Execution Started");
				}
			}
		}
		catch (Exception ex)
		{
			threadLogger.get().error(ExceptionUtil.getStackTrace(ex));
		}
		finally
		{
			reetrantLock.unlock();
		}
	}

	
	@BeforeClass(alwaysRun = true)
	public void onSetup(ITestContext context)
	{
		try
		{
			synchronized (reetrantLock)
			{
				reetrantLock.lock();
				setContext(context);
			}
		}
		catch (Exception ex)
		{
			threadLogger.get().error(ExceptionUtil.getStackTrace(ex));
		}
		finally
		{
			reetrantLock.unlock();
		}

	
		if (context.getCurrentXmlTest().getParameter("export") != null)
		{
			File file = IOUtils.createDirectory(
				System.getProperty("user.dir") + "/test-output/" + ReportHandler.OUTPUT_DIRECTORY, "download");
			File fileIntemediatePath = IOUtils.createDirectory(file.getAbsolutePath(), this.getClass().getName());
			File fileDownloadPath = IOUtils.createDirectory(fileIntemediatePath.getAbsolutePath(),
				context.getCurrentXmlTest().getParameter("merchant"));
			this.strFileDownloadPath = fileDownloadPath.getAbsolutePath();
		}
		testCase.release = "Release1";
		testCase.start_time = CalendarUtils.getTimeStamp();
		threadTestCase.set(testCase);
		threadLogger.get().info("[" + this.getClass().getName() + "] :: Started");
	}

	@BeforeMethod(alwaysRun = true)
	public void onStart(ITestContext context, Method method)
	{
		this.testContext = context;
		List<Tool> lstTools = threadTools.get();
		for (Tool tool : lstTools)
		{
			if (tool != null)
			{
				tool.setInstanceClassLevel();
			}
		}
		String strMethod = method.getName();
		threadLogger.set(LogManager.getLogger(strMethod));
		threadMethod.set(strMethod);
		threadTestLogger.set(reportHandler.startLogger(strMethod, this));
		threadLogger.get().info("[" + this.getClass().getName() + "][" + strMethod + "] :: Test Method Started");
		testCase = threadTestCase.get();
		testCase.testId = this.getClass().getName() + "." + strMethod;
		threadTestCase.set(testCase);
	}

	@AfterMethod(alwaysRun = true)
	public void onEnd(ITestResult result, Method method)
	{
		closeTools(false);
		reportHandler.flushReport();
		String strTestResult = (result.getStatus() == 1) ? "[PASS]" : "[FAIL]";
		threadLogger.get().info(
			"[" + this.getClass().getName() + "][" + method.getName() + "] :: Test Method Ended " + strTestResult);
	}

	@AfterClass(alwaysRun = true)
	public void onTearDown(ITestContext context)
	{
		closeTools(true);
		context.removeAttribute(Thread.currentThread().getName());
		threadLogger.get().info("[" + this.getClass().getName() + "] :: Ended");
		reportHandler.flushReport();
	}

	@AfterSuite(alwaysRun = true)
	public void onSuiteEnd(ITestContext context)
	{
		try
		{
			synchronized (reetrantLock)
			{
				reetrantLock.lock();
				if (TestListener.getSuiteCount() <= 1)
				{
					reportHandler.endReport(this);
					threadLogger.get().info("Test Execution Ended");
				}
			}
			testCase = threadTestCase.get();
			testCase.end_time = CalendarUtils.getTimeStamp();
			testCase.draft_run = "N";
			threadTestCase.set(testCase);
		}
		finally
		{
			reetrantLock.unlock();
		}
	}

	private void setProperties(ITestContext context)
	{
		Map<String, String> mapTestParameters = context.getCurrentXmlTest().getAllParameters();
		TestProperties testProp = threadProperties.get();
		for (Map.Entry<String, String> entry : mapTestParameters.entrySet())
		{
			boolean bExists = false;
			Enumeration<?> enumProperties = testProp.getPropertyNames();
			while (enumProperties.hasMoreElements() && !bExists)
			{
				if (entry.getKey().contains("[") && entry.getKey().contains("]") &&
					entry.getKey().equalsIgnoreCase(enumProperties.nextElement().toString()))
				{
					testProp.setPropertyValue(entry.getKey(), entry.getValue());
					bExists = true;
				}
				else if (entry.getKey().equalsIgnoreCase(enumProperties.nextElement().toString()))
				{
					testProp.setRootPropertyValue(entry.getKey(), entry.getValue());
					bExists = true;
				}
			}
		}
		this.threadProperties.set(testProp);
		strJenkinsLabel = context.getCurrentXmlTest().getParameter("LABEL");
		strApp = context.getCurrentXmlTest().getParameter("APP");
	}

	private void closeTools(boolean bInstanceClassLevel)
	{
		List<Tool> lstTools = threadTools.get();
		for (Tool tool : lstTools)
		{
			if (tool != null && tool.isInstanceClassLevel() == bInstanceClassLevel)
			{
				closeUITestTool(tool);
			//	closeRestTestTool(tool);
				closeMobileTestTool(tool);
				closeDBTestTool(tool);
				lstTools.set(lstTools.indexOf(tool), null);
			}
		}
		threadTools.set(lstTools);
		lstTools = threadTools.get();
		for (Tool tool : lstTools)
		{
			if (tool == null)
			{
				lstTools.remove(lstTools.indexOf(tool));
			}
		}
		threadTools.set(lstTools);
	}

	private void closeUITestTool(Tool tool)
	{
		if (tool instanceof UITestTool)
		{
			UITestTool ttUI = (UITestTool) tool;
			closeBrowser(ttUI);
		}
	}

	/*private void closeRestTestTool(Tool tool)
	{
		if (tool instanceof RestTestTool)
		{
			RestTestTool ttRest = (RestTestTool) tool;
			ttRest.close();
		}
	}*/

	private void closeMobileTestTool(Tool tool)
	{
		if (tool instanceof MobileTestTool)
		{
			MobileTestTool ttMob = (MobileTestTool) tool;
			ttMob.getDevice().MOB_INUSE = "N";
			ttMob.close();
		}
	}

	private void closeDBTestTool(Tool tool)
	{
		if (tool instanceof DBTestTool)
		{
			DBTestTool ttDB = (DBTestTool) tool;
			ttDB.closeConnection();
		}
	}

	public ExtentTest getTestLogger()
	{
		return threadTestLogger.get();
	}

	public Logger getLogger()
	{
		return threadLogger.get();
	}

	public TestProperties getProperties()
	{
		return threadProperties.get();
	}

	public void setFileDownloadPath(String strFilePath)
	{
		this.strFileDownloadPath = strFilePath;
	}

	public String getFileDownloadPath()
	{
		return this.strFileDownloadPath;
	}

	public TestCase getTestCase()
	{
		return threadTestCase.get();
	}

	public void setTestCase(TestCase testCase)
	{
		threadTestCase.set(testCase);
	}

	public void setTool(Tool tool)
	{
		List<Tool> lstTools = threadTools.get();
		lstTools.add(tool);
		threadTools.set(lstTools);
	}

	public Tool getTool()
	{
		List<Tool> lstTool = getTools();
		for (Tool tool : lstTool)
		{
			if (tool instanceof UITestTool)
			{
				return tool;
			}
		}
		return null;
	}

	public List<Tool> getTools()
	{
		return threadTools.get();
	}

	public ReportHandler getReportHandler()
	{
		return reportHandler;
	}

	public String getEnvConfig()
	{
		return getProperties().getPropertyValue("ENV_CONFIG");
	}

	public String getCurrentMethod()
	{
		if (threadMethod.get() != null)
		{
			return threadMethod.get();
		}
		return null;
	}

	public String getJenkinsLabel()
	{
		return strJenkinsLabel;
	}

	public ITestContext getTestContext()
	{
		return this.testContext;
	}

	public String getApp()
	{
		return strApp;
	}

	private void closeBrowser(UITestTool ttUI)
	{
		try
		{
			if (ttUI != null)
			{
				ttUI.closeDriver();
				ttUI.quitDriver();
			}
		}
		catch (Exception ex)
		{
			// do nothing
		}
	}
	
	private void setContext(ITestContext context)
	{
		this.testContext = context;
		context.setAttribute(Thread.currentThread().getName(), this);
		setProperties(context);
	}

}
