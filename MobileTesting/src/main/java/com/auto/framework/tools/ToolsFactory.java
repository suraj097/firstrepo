package com.auto.framework.tools;

import com.auto.framework.base.BaseTestCase;

/**
 * Tools Factory, ensures common way to get Tool instance.
 * 
 * @author naini.ghai
 *
 */
public class ToolsFactory
{
	private enum eTool
	{
		UITESTTOOL,
		RESTTESTTOOL,
		MOBILETESTTOOL,
		RESTTOOL,
		DBTESTTOOL
	}

	private ToolsFactory()
	{
		// cannot be instantiated from outside.
	}
	
	/**
	 * Return the instance of the specific tool
	 * 
	 * @param baseTestCase - instance of BaseTestCase
	 * @param toolClassToProxy - Class reference of different tools
	 * @return instance of Tool that is being instantiated 
	 * <br>
	 * <br>
	 * <b><u>For Example-</b></u>
	 * <br>
	 * UITestTool ttUI = ToolsFactory.getInstance(baseTestCase, UITestTool.class)
	 * <br>
	 * RestTestTool ttRest = ToolsFactory.getInstance(baseTestCase, RestTestTool.class)
	 * <br>
	 * MobileTestTool ttMob = ToolsFactory.getInstance(baseTestCase, MobileTestTool.class)
	 * <br>
	 * DBTestTool ttDB = ToolsFactory.getInstance(baseTestCase, DBTestTool.class)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(BaseTestCase baseTestCase, Class<T> toolClassToProxy)
	{
		if (toolClassToProxy == null)
		{
			return null;
		}

		String strClassName = String.valueOf(toolClassToProxy);
		String strToolName = strClassName.substring(strClassName.lastIndexOf('.') + 1).toUpperCase();
		eTool etool = eTool.valueOf(eTool.class, strToolName);
		switch (etool)
		{
			case UITESTTOOL:
				return (T) UITestTool.getInstance(baseTestCase);
			
			case MOBILETESTTOOL:
				return (T) MobileTestTool.getInstance(baseTestCase);
			case DBTESTTOOL:
				return (T) DBTestTool.getInstance(baseTestCase);
			default:
				return null;
		}
	}
	
	/**
	 * Return the instance of the specific tool
	 * 
	 * @param baseTestCase
	 * @param toolClassToProxy
	 * @param strTestConfig
	 * @return instance of Tool that is being instantiated
	 * <br>
	 * <br>
	 * <b><u>For Example-</b></u>
	 * <br>
	 * UITestTool ttUI = ToolsFactory.getInstance(baseTestCase, UITestTool.class, "<browser name - CHROME or FIREFOX or IE or EDGE")
	 * <br>
	 * RestTestTool ttRest = ToolsFactory.getInstance(baseTestCase, RestTestTool.class, null)
	 * <br>
	 * MobileTestTool ttMob = ToolsFactory.getInstance(baseTestCase, MobileTestTool.class, "<mobile os name - ANDROID or iOS>")
	 * <br>
	 * DBTestTool ttDB = ToolsFactory.getInstance(baseTestCase, DBTestTool.class, "<path to property file holding DB credentials>")
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(BaseTestCase baseTestCase, Class<T> toolClassToProxy, String strTestConfig)
	{
		if (toolClassToProxy == null)
		{
			return null;
		}

		String strClassName = String.valueOf(toolClassToProxy);
		String strToolName = strClassName.substring(strClassName.lastIndexOf('.') + 1);
		eTool etool = eTool.valueOf(eTool.class, strToolName.toUpperCase());
		switch (etool)
		{
			case UITESTTOOL:
				return (T) UITestTool.getInstance(baseTestCase, strTestConfig);
			
			case MOBILETESTTOOL:
				return (T) MobileTestTool.getInstance(baseTestCase, strTestConfig);
			case DBTESTTOOL:
				return (T) DBTestTool.getInstance(baseTestCase, strTestConfig);
			default:
				return null;
		}
	}
}
