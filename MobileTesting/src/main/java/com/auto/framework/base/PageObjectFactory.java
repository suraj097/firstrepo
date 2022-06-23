package com.auto.framework.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import com.auto.framework.tools.MobileTestTool;
import com.auto.framework.tools.Tool;
import com.auto.framework.tools.UITestTool;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

/**
 * Customizable Factory class that extends PageFactory. It makes using Page Objects simpler and easier via custom
 * selenium webdriver wrapper UITestTool
 * 
 * @author naini.ghai
 * 
 */
public class PageObjectFactory extends PageFactory
{
	public static <T> T initElements(UITestTool ttUI, Class<T> pageClassToProxy)
	{
		WebDriver webDriver = ttUI.getWebDriver();
		T page = instantiatePage(ttUI, UITestTool.class, pageClassToProxy);
		initElements(webDriver, page);
		return page;
	}

	public static <T> T initElements(MobileTestTool ttMob, Class<T> pageClassToProxy)
	{
		AppiumDriver<MobileElement> appiumDriver = ttMob.getAppiumDriver();
		T page = instantiatePage(ttMob, MobileTestTool.class, pageClassToProxy);
		initElements(new AppiumFieldDecorator(appiumDriver), page);
		return page;
	}

	private static <T> T instantiatePage(Tool tool, Class<?> toolType, Class<T> pageClassToProxy)
	{
		try
		{
			return getPageInstance(tool, toolType, pageClassToProxy);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private static <T> T getPageInstance(Tool tool, Class<?> toolType, Class<T> pageClassToProxy)
		throws InstantiationException, IllegalAccessException, InvocationTargetException
	{
		try
		{
			Constructor<T> constructor = pageClassToProxy.getConstructor(toolType);
			return constructor.newInstance(tool);
		}
		catch (NoSuchMethodException e)
		{
			return pageClassToProxy.newInstance();
		}
	}
}
