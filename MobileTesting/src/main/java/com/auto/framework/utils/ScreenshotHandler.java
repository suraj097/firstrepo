package com.auto.framework.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.auto.framework.base.BaseTestCase;
import com.auto.framework.tools.MobileTestTool;
import com.auto.framework.tools.Tool;
import com.auto.framework.tools.UITestTool;

/**
 * Utility to take screenshot while test execution
 * 
 * @author naini.ghai
 * 
 */
public class ScreenshotHandler
{

	private static final String	FILE_SEPERATOR	= "/";
	private static final String	ESCAPE_PROPERTY	= "org.uncommons.reportng.escape-output";

	public String takeSpanshot(BaseTestCase baseTestCase, Tool tool)
	{
		UITestTool ttUI = null;
		UITestTool ttMob = null;
		String strFileLocation = "";
		if (tool == null)
		{
			ttUI = (UITestTool) baseTestCase.getTool();
		}
		else
		{
			if (tool instanceof MobileTestTool)
			{
				ttMob = (MobileTestTool) tool;
			}

			if (tool instanceof UITestTool)
			{
				ttUI = (UITestTool) tool;
			}
		}

		System.setProperty(ESCAPE_PROPERTY, "false");
		String strFileName = "";
		if (ttUI != null || ttMob != null)
		{
			String strPackagesStructure = baseTestCase.getClass().getPackage().getName();
			String[] aPackages = strPackagesStructure.split("\\.");
			StringBuilder strbScreenshotPath = new StringBuilder("");
			for (String strPackage : aPackages)
			{
				strbScreenshotPath.append(FILE_SEPERATOR + strPackage);
			}
			String strClassName = baseTestCase.getClass().getName();
			strbScreenshotPath.append(FILE_SEPERATOR + strClassName.substring(strClassName.lastIndexOf('.') + 1) +
				FILE_SEPERATOR + baseTestCase.getCurrentMethod());
			// Image File Name
			strFileName = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS")
				.format(new GregorianCalendar().getTime());
			// Image File Location
			strFileLocation = System.getProperty("user.dir").replace("\\", FILE_SEPERATOR) + FILE_SEPERATOR +
				"test-output" + FILE_SEPERATOR + ReportHandler.OUTPUT_DIRECTORY + strbScreenshotPath.toString();
			try
			{
				// Take screenshot
				File scrFile = ((TakesScreenshot) ((ttMob != null) ? ttMob.getWebDriver() : ttUI.getWebDriver()))
					.getScreenshotAs(OutputType.FILE);
				// The below method will save the screen shot
				FileUtils.copyFile(scrFile, new File(strFileLocation + FILE_SEPERATOR + strFileName + ".png"));
			}
			catch (Exception ex)
			{
				// do nothing
			}
			return strFileLocation + FILE_SEPERATOR + strFileName + ".png";
		}
		return "";
	}

}
