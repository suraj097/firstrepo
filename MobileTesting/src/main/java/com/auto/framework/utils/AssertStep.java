package com.auto.framework.utils;

import org.testng.Assert;
import com.auto.framework.base.BaseTestCase;
import com.auto.framework.test.utils.TestStep;
import com.auto.framework.tools.Tool;
import com.aventstack.extentreports.Status;

/**
 * This class provides a set of assertion methods, useful for verify tests and enables logging into reporting
 * framework
 * 
 * @author naini.ghai
 * 
 */
public class AssertStep extends Assert
{

	private static final String	BR			= "<br>";
	private static final String	EXPECTED	= "<FONT COLOR='GREEN'><b>Result := </b>";
	private static final String	ACTION		= "<FONT COLOR='BLUE'><b>Action := </b>";

	/**
	 * Logs the Step into Report
	 * 
	 * @param baseTestCase
	 * @param strAction
	 */
	public static void assertStep(BaseTestCase baseTestCase, String strAction)
	{
		logStepDetails(baseTestCase, null, strAction, null, false);
	}

	/**
	 * Capture the screenshot and logs the Step into Report
	 * 
	 * @param baseTestCase
	 * @param strAction
	 */
	public static void assertStepWithSnapshot(BaseTestCase baseTestCase, String strAction)
	{
		logStepDetails(baseTestCase, null, strAction, null, true);
	}

	/**
	 * Capture the screenshot and logs the Step into Report
	 * 
	 * @param baseTestCase
	 * @param tool - instance of UITestTool or MobileTestTool
	 * @param strAction
	 */
	public static void assertStepWithSnapshot(BaseTestCase baseTestCase, Tool tool, String strAction)
	{
		logStepDetails(baseTestCase, tool, strAction, null, true);
	}

	/**
	 * Asserts condition is true and logs the Step into Report
	 * 
	 * @param bFlag
	 * @param baseTestCase
	 * @param strAction
	 * @param strExpected
	 */
	public static void assertTrue(boolean bFlag, BaseTestCase baseTestCase, String strAction, String strExpected)
	{
		Assert.assertTrue(bFlag);
		logStepDetails(baseTestCase, null, strAction, strExpected, false);
	}

	/**
	 * Asserts condition is true and logs the Step into Report
	 * 
	 * @param bFlag
	 * @param baseTestCase
	 * @param strAction
	 * @param strExpected
	 * @param strOptional
	 */
	public static void assertTrue(boolean bFlag, BaseTestCase baseTestCase, String strAction, String strExpected,
		String strOptional)
	{
		if (strOptional != null && !strOptional.equals(""))
		{
			setOptionalContext(baseTestCase, strOptional);
		}
		assertTrue(bFlag, baseTestCase, strAction, strExpected);
	}

	/**
	 * Asserts condition is true, capture screenshot and logs the Step into Report
	 * 
	 * @param bFlag
	 * @param baseTestCase
	 * @param strAction
	 * @param strExpected
	 */
	public static void assertTrueWithSnapshot(boolean bFlag, BaseTestCase baseTestCase, String strAction,
		String strExpected)
	{
		Assert.assertTrue(bFlag);
		logStepDetails(baseTestCase, null, strAction, strExpected, true);
	}

	/**
	 * Asserts condition is true, capture screenshot and logs the Step into Report
	 * 
	 * @param bFlag
	 * @param baseTestCase
	 * @param strAction
	 * @param strExpected
	 * @param strOptional
	 */
	public static void assertTrueWithSnapshot(boolean bFlag, BaseTestCase baseTestCase, String strAction,
		String strExpected, String strOptional)
	{
		if (strOptional != null && !strOptional.equals(""))
		{
			setOptionalContext(baseTestCase, strOptional);
		}
		assertTrueWithSnapshot(bFlag, baseTestCase, strAction, strExpected);
	}

	/**
	 * Asserts condition is true, capture screenshot and logs the Step into Report
	 * 
	 * @param bFlag
	 * @param baseTestCase
	 * @param tool - instance of UITestTool or MobileTestTool
	 * @param strAction
	 * @param strExpected
	 */
	public static void assertTrueWithSnapshot(boolean bFlag, BaseTestCase baseTestCase, Tool tool,
		String strAction, String strExpected)
	{
		Assert.assertTrue(bFlag);
		logStepDetails(baseTestCase, tool, strAction, strExpected, true);
	}

	/**
	 * Asserts condition is true, capture screenshot and logs the Step into Report
	 * 
	 * @param bFlag
	 * @param baseTestCase
	 * @param tool - instance of UITestTool or MobileTestTool
	 * @param strAction
	 * @param strExpected
	 * @param strOptional
	 */
	public static void assertTrueWithSnapshot(boolean bFlag, BaseTestCase baseTestCase, Tool tool,
		String strAction, String strExpected, String strOptional)
	{
		if (strOptional != null && !strOptional.equals(""))
		{
			setOptionalContext(baseTestCase, strOptional);
		}
		assertTrueWithSnapshot(bFlag, baseTestCase, tool, strAction, strExpected);
	}

	/**
	 * Asserts condition is false and logs the Step into Report
	 * 
	 * @param bFlag
	 * @param baseTestCase
	 * @param strAction
	 * @param strExpected
	 */
	public static void assertFalse(boolean bFlag, BaseTestCase baseTestCase, String strAction, String strExpected)
	{
		Assert.assertFalse(bFlag);
		logStepDetails(baseTestCase, null, strAction, strExpected, false);
	}

	/**
	 * Asserts condition is false and logs the Step into Report
	 * 
	 * @param bFlag
	 * @param baseTestCase
	 * @param strAction
	 * @param strExpected
	 * @param strOptional
	 */
	public static void assertFalse(boolean bFlag, BaseTestCase baseTestCase, String strAction, String strExpected,
		String strOptional)
	{
		if (strOptional != null && !strOptional.equals(""))
		{
			setOptionalContext(baseTestCase, strOptional);
		}
		assertFalse(bFlag, baseTestCase, strAction, strExpected);
	}

	/**
	 * Asserts condition is false, capture screenshot and logs the Step into Report
	 * 
	 * @param bFlag
	 * @param baseTestCase
	 * @param strAction
	 * @param strExpected
	 */
	public static void assertFalseWithSnapshot(boolean bFlag, BaseTestCase baseTestCase, String strAction,
		String strExpected)
	{
		Assert.assertFalse(bFlag);
		logStepDetails(baseTestCase, null, strAction, strExpected, true);
	}

	/**
	 * Asserts condition is false, capture screenshot and logs the Step into Report
	 * 
	 * @param bFlag
	 * @param baseTestCase
	 * @param strAction
	 * @param strExpected
	 */
	public static void assertFalseWithSnapshot(boolean bFlag, BaseTestCase baseTestCase, String strAction,
		String strExpected, String strOptional)
	{
		if (strOptional != null && !strOptional.equals(""))
		{
			setOptionalContext(baseTestCase, strOptional);
		}
		assertFalseWithSnapshot(bFlag, baseTestCase, strAction, strExpected);
	}

	/**
	 * Asserts condition is false, capture screenshot and logs the Step into Report
	 * 
	 * @param bFlag
	 * @param baseTestCase
	 * @param tool - instance of UITestTool or MobileTestTool
	 * @param strAction
	 * @param strExpected
	 */
	public static void assertFalseWithSnapshot(boolean bFlag, BaseTestCase baseTestCase, Tool tool,
		String strAction, String strExpected)
	{
		Assert.assertFalse(bFlag);
		logStepDetails(baseTestCase, tool, strAction, strExpected, true);
	}

	/**
	 * Asserts condition is false, capture screenshot and logs the Step into Report
	 * 
	 * @param bFlag
	 * @param baseTestCase
	 * @param tool - instance of UITestTool or MobileTestTool
	 * @param strAction
	 * @param strExpected
	 * @param strOptional
	 */
	public static void assertFalseWithSnapshot(boolean bFlag, BaseTestCase baseTestCase, Tool tool,
		String strAction, String strExpected, String strOptional)
	{
		if (strOptional != null && !strOptional.equals(""))
		{
			setOptionalContext(baseTestCase, strOptional);
		}
		assertFalseWithSnapshot(bFlag, baseTestCase, tool, strAction, strExpected);
	}

	/**
	 * Asserts two strings are equal and logs the Step into Report
	 * @param strActual
	 * @param strExpected
	 * @param baseTestCase
	 * @param strAction
	 */
	public static void assertEquals(String strActual, String strExpected, BaseTestCase baseTestCase,
		String strAction)
	{
		Assert.assertEquals(strActual, strExpected);
		logStepDetails(baseTestCase, null, strAction, "[" + strExpected + "] <b>equals</b> [" + strActual + "]",
			false);
	}

	/**
	 * Asserts two strings are equal, capture screenshot and logs the Step into Report
	 * 
	 * @param strActual
	 * @param strExpected
	 * @param baseTestCase
	 * @param strAction
	 */
	public static void assertEqualsWithSnapshot(String strActual, String strExpected, BaseTestCase baseTestCase,
		String strAction)
	{
		Assert.assertEquals(strActual, strExpected);
		logStepDetails(baseTestCase, null, strAction, "[" + strExpected + "] <b>equals</b> [" + strActual + "]",
			true);
	}

	/**
	 * Asserts two strings are equal, capture screenshot and logs the Step into Report
	 * 
	 * @param strActual
	 * @param strExpected
	 * @param baseTestCase
	 * @param tool - instance of UITestTool or MobileTestTool
	 * @param strAction
	 */
	public static void assertEqualsWithSnapshot(String strActual, String strExpected, BaseTestCase baseTestCase,
		Tool tool, String strAction)
	{
		Assert.assertEquals(strActual, strExpected);
		logStepDetails(baseTestCase, tool, strAction, "[" + strExpected + "] <b>equals</b> [" + strActual + "]",
			true);
	}

	/**
	 * Asserts two strings are not equal and logs the Step into Report
	 * 
	 * @param strActual
	 * @param strExpected
	 * @param baseTestCase
	 * @param strAction
	 */
	public static void assertNotEquals(String strActual, String strExpected, BaseTestCase baseTestCase,
		String strAction)
	{
		Assert.assertNotEquals(strActual, strExpected);
		logStepDetails(baseTestCase, null, strAction,
			"[" + strExpected + "] <b>not equals</b> [" + strActual + "]", false);
	}

	/**
	 * Asserts two strings are not equal, capture screenshot and logs the Step into Report
	 * 
	 * @param strActual
	 * @param strExpected
	 * @param baseTestCase
	 * @param strAction
	 */
	public static void assertNotEqualsWithSnapshot(String strActual, String strExpected, BaseTestCase baseTestCase,
		String strAction)
	{
		Assert.assertNotEquals(strActual, strExpected);
		logStepDetails(baseTestCase, null, strAction,
			"[" + strExpected + "] <b>not equals</b> [" + strActual + "]", true);
	}

	/**
	 * Asserts two strings are not equal, capture screenshot and logs the Step into Report
	 * 
	 * @param strActual
	 * @param strExpected
	 * @param baseTestCase
	 * @param tool - instance of UITestTool or MobileTestTool
	 * @param strAction
	 */
	public static void assertNotEqualsWithSnapshot(String strActual, String strExpected, BaseTestCase baseTestCase,
		Tool tool, String strAction)
	{
		Assert.assertNotEquals(strActual, strExpected);
		logStepDetails(baseTestCase, tool, strAction,
			"[" + strExpected + "] <b>not equals</b> [" + strActual + "]", true);
	}

	/**
	 * Asserts text exists and logs the Step into Report
	 * 
	 * @param strActual
	 * @param strExpected
	 * @param baseTestCase
	 * @param strAction
	 */
	public static void assertTextExists(String strActual, String strExpected, BaseTestCase baseTestCase,
		String strAction)
	{
		Assert.assertTrue((strActual.indexOf(strExpected) != -1));
		logStepDetails(baseTestCase, null, strAction,
			"[" + strActual + "] <b> contains </b> [" + strExpected + "]", false);
	}

	/**
	 * Asserts text exists, capture screenshot and logs the Step into Report
	 * 
	 * @param strActual
	 * @param strExpected
	 * @param baseTestCase
	 * @param strAction
	 */
	public static void assertTextExistsWithSnapshot(String strActual, String strExpected,
		BaseTestCase baseTestCase, String strAction)
	{
		Assert.assertTrue((strActual.indexOf(strExpected) != -1));
		logStepDetails(baseTestCase, null, strAction,
			"[" + strActual + "] <b> contains </b> [" + strExpected + "]", true);
	}

	/**
	 * Asserts text exists, capture screenshot and logs the Step into Report
	 * 
	 * @param strActual
	 * @param strExpected
	 * @param baseTestCase
	 * @param tool - instance of UITestTool or MobileTestTool
	 * @param strAction
	 */
	public static void assertTextExistsWithSnapshot(String strActual, String strExpected,
		BaseTestCase baseTestCase, Tool tool, String strAction)
	{
		Assert.assertTrue((strActual.indexOf(strExpected) != -1));
		logStepDetails(baseTestCase, tool, strAction,
			"[" + strActual + "] <b> contains </b> [" + strExpected + "]", true);
	}

	/**
	 * Asserts text not exists and logs the Step into Report
	 * 
	 * @param strActual
	 * @param strExpected
	 * @param baseTestCase
	 * @param strAction
	 */
	public static void assertTextNotExists(String strActual, String strExpected, BaseTestCase baseTestCase,
		String strAction)
	{
		Assert.assertTrue((strActual.indexOf(strExpected) == -1));
		logStepDetails(baseTestCase, null, strAction,
			"[" + strActual + "] <b> does not contains </b> [" + strExpected + "]", false);
	}

	/**
	 * Asserts text not exists, capture screenshot and logs the Step into Report
	 * 
	 * @param strActual
	 * @param strExpected
	 * @param baseTestCase
	 * @param strAction
	 */
	public static void assertTextNotExistsWithSnapshot(String strActual, String strExpected,
		BaseTestCase baseTestCase, String strAction)
	{
		Assert.assertTrue((strActual.indexOf(strExpected) == -1));
		logStepDetails(baseTestCase, null, strAction,
			"[" + strActual + "] <b> does not contains </b> [" + strExpected + "]", true);
	}

	/**
	 * Asserts text not exists, capture screenshot and logs the Step into Report
	 * 
	 * @param strActual
	 * @param strExpected
	 * @param baseTestCase
	 * @param tool - instance of UITestTool or MobileTestTool
	 * @param strAction
	 */
	public static void assertTextNotExistsWithSnapshot(String strActual, String strExpected,
		BaseTestCase baseTestCase, Tool tool, String strAction)
	{
		Assert.assertTrue((strActual.indexOf(strExpected) == -1));
		logStepDetails(baseTestCase, tool, strAction,
			"[" + strActual + "] <b> does not contains </b> [" + strExpected + "]", true);
	}

	private static String getSnapshot(BaseTestCase baseTestCase)
	{
		return getSnapshot(baseTestCase, null);
	}

	private static String getSnapshot(BaseTestCase baseTestCase, Tool tool)
	{
		ScreenshotHandler screenshotHandler = new ScreenshotHandler();
		return screenshotHandler.takeSpanshot(baseTestCase, tool);
	}

	private static void logStepDetails(BaseTestCase baseTestCase, Tool tool, String strAction, String strExpected,
		boolean bSnapshot)
	{
		TestStep testStep = new TestStep();
		StringBuilder strbStepDetails = new StringBuilder();
		strbStepDetails.append(ACTION);
		strbStepDetails.append(strAction + "</FONT>");
		strbStepDetails.append(
			((strExpected != null && !strExpected.equals("")) ? BR + EXPECTED + strExpected + "</FONT>" : ""));
		String strOptionalMessage = getOptionalContext(baseTestCase);
		strbStepDetails.append((strOptionalMessage != null && !strOptionalMessage.equals(""))
			? BR + "<FONT COLOR='TEAL'><i>" + strOptionalMessage + "</i>" : "");
		if (bSnapshot)
		{
			String strScreenshot = (tool == null) ? getSnapshot(baseTestCase) : getSnapshot(baseTestCase, tool);
			String strRelativeScreenshot = "." + strScreenshot.substring(strScreenshot.indexOf("/com"));
			strbStepDetails.append(BR + "<A HREF='" + strRelativeScreenshot +
				"' target='_blank'><FONT COLOR='BLACK'><b><u>Screenshot</u></b></A>");
		}
		testStep.step_details = strbStepDetails.toString();
		testStep.step_status = "PASS";
		baseTestCase.getTestCase().steps.add(testStep);
		baseTestCase.getTestLogger().log(Status.PASS, strbStepDetails.toString());
		baseTestCase.getLogger()
			.info("[" + baseTestCase.getClass().getName() + "][" + baseTestCase.getCurrentMethod() + "] PASS - " +
				strAction + ((strExpected != null && !strExpected.equals("")) ? " || " + strExpected : ""));
	}

	private static void setOptionalContext(BaseTestCase baseTestCase, String strOptional)
	{
		baseTestCase.getTestContext().setAttribute(
			Thread.currentThread().getName() + "." + baseTestCase.getCurrentMethod() + "_OPTIONAL_MESSAGE",
			strOptional);
	}

	private static String getOptionalContext(BaseTestCase baseTestCase)
	{
		return (baseTestCase.getTestContext()
			.getAttribute(Thread.currentThread().getName() + "." + baseTestCase.getCurrentMethod() +
				"_OPTIONAL_MESSAGE") != null)
					? baseTestCase.getTestContext().getAttribute(Thread.currentThread().getName() + "." +
						baseTestCase.getCurrentMethod() + "_OPTIONAL_MESSAGE").toString()
					: "";
	}
}
