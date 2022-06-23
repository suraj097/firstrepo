package com.auto.framework.tools;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.auto.framework.base.BaseTestCase;
import com.auto.framework.utils.BrowserHandler;
import com.auto.framework.utils.TestClassUtils;
import com.auto.framework.utils.TestUtils;
import com.google.common.collect.Iterables;

/**
 * UTTestTools is wrapper over selenium webdriver and provides functionality to interact with browser
 * 
 * @author naini.ghai
 * 
 */
public class UITestTool extends Tool implements ITool
{
	protected WebDriver	webDriver;
	private int			nImplicitTimeOut;

	public UITestTool()
	{
		// empty
	}

	/**
	 * Get instance of the UI Test Tool
	 * 
	 * @throws Exception
	 */
	public static UITestTool getInstance(BaseTestCase baseTestCase)
	{
		return createDriver(baseTestCase, null);
	}

	public static UITestTool getInstance(BaseTestCase baseTestCase, String strBrowser)
	{
		return createDriver(baseTestCase, strBrowser);
	}

	private static UITestTool createDriver(BaseTestCase baseTestCase, String strBrowser)
	{
		UITestTool ttUI = new UITestTool();
		BrowserHandler browserHandler = new BrowserHandler();
		if (strBrowser == null)
		{
			strBrowser = baseTestCase.getProperties().getRootPropertyValue("BROWSER");
			String[] arrBrowsers = null;
			arrBrowsers = strBrowser.split(",");
			if (arrBrowsers != null && arrBrowsers.length > 0)
			{
				strBrowser = arrBrowsers[(new Random()).nextInt(arrBrowsers.length)];
			}
		}
		ttUI.webDriver = browserHandler.createUIDriver(baseTestCase, strBrowser);
		ttUI.nImplicitTimeOut = browserHandler.getWeDriverTimeOut();
		baseTestCase.setTool(ttUI);
		return ttUI;
	}

	/**
	 * Launch URL
	 * 
	 * @param strURL
	 */
	public void accessURL(String strURL)
	{
		webDriver.navigate().to(strURL);
	}

	/**
	 * Close Browser
	 */
	public void closeDriver()
	{
		webDriver.close();
	}

	/**
	 * Kill Web Driver
	 */
	public void quitDriver()
	{
		webDriver.quit();
		webDriver = null;
	}

	/**
	 * return Web Driver Instance
	 * 
	 * @return
	 */
	public WebDriver getWebDriver()
	{
		return webDriver;
	}

	/**
	 * Click on the Web Element
	 * 
	 * @param _btnAddToCart
	 */
	public void clickElement(WebElement we)
	{
		TestClassUtils.assertNotNull(we);
		try
		{
			we.click();
		}
		catch (Exception ex)
		{
			JavascriptExecutor executor = (JavascriptExecutor) webDriver;
			executor.executeScript("arguments[0].click();", we);
		}
	}

	/**
	 * Click on the Web Element
	 * 
	 * @param we
	 */
	public void clickElement(By by)
	{
		TestClassUtils.assertNotNull(by);
		WebElement we = findAndFocusOn(by);
		try
		{
			we.click();
		}
		catch (Exception ex)
		{
			JavascriptExecutor executor = (JavascriptExecutor) webDriver;
			executor.executeScript("arguments[0].click();", we);
		}
	}

	/**
	 * Set value to the Web Element
	 * 
	 * @param we
	 * @param strValue
	 */
	public void setInputValue(WebElement we, String strValue)
	{
		TestClassUtils.assertNotNull(we);
		TestClassUtils.assertNotNullString(strValue);
		we.clear();
		we.sendKeys(strValue);
	}

	/**
	 * Set value to the Web Element
	 * 
	 * @param by By
	 * @param strValue String
	 */
	public void setInputValue(By by, String strValue)
	{
		TestClassUtils.assertNotNull(by);
		WebElement we = webDriver.findElement(by);
		setInputValue(we, strValue);
	}

	/**
	 * Waits for element to be visible, the hovers on it.
	 */
	public void hoverOnElement(WebElement we)
	{
		// Now hover on it
		new Actions(webDriver).moveToElement(we).perform();
	}

	/**
	 * This drags and drops the source element over to the target element
	 * 
	 * @param bySource the element to be moved
	 * @param byTarget the target where the element will be dropped
	 */
	public void dragAndDropElementTo(By bySource, By byTarget)
	{
		TestClassUtils.assertNotNull(bySource);
		TestClassUtils.assertNotNull(byTarget);
		WebElement weSource = webDriver.findElement(bySource);
		WebElement weTarget = webDriver.findElement(byTarget);
		dragAndDropElementTo(weSource, weTarget);
	}

	/**
	 * This drags and drops the source element over to the target element
	 * 
	 * @param weSource the element to be moved
	 * @param weTarget the target where the element will be dropped
	 */
	public void dragAndDropElementTo(WebElement weSource, WebElement weTarget)
	{
		TestClassUtils.assertNotNull(weSource);
		TestClassUtils.assertNotNull(weTarget);
		// Create a new Action
		Actions builder = new Actions(webDriver);
		// Build definition on how the drag and drop movement will be executed, and execute it
		builder.dragAndDrop(weSource, weTarget).build().perform();
	}

	/**
	 * Returns true in-case Web Element is present else returns false
	 * 
	 * @param by
	 * @return
	 */
	public boolean isElementExists(By by)
	{
		try
		{
			TestClassUtils.assertNotNull(by);
			webDriver.findElement(by);
			return true;
		}
		catch (Exception ex)
		{
			// do nothing
		}
		return false;
	}

	/**
	 * Returns true in-case Web Element is present else returns false
	 * 
	 * @param we, WebElement
	 * @return
	 */
	public boolean isElementExists(WebElement we)
	{
		try
		{
			TestClassUtils.assertNotNull(we);
			return true;
		}
		catch (Exception ex)
		{
			// do nothing
		}
		return false;
	}

	/**
	 * Get Attribute value of the Web Element
	 * 
	 * @param we
	 * @param strAttribute
	 * @return
	 */
	public String getElementAttribute(WebElement we, String strAttribute)
	{
		TestClassUtils.assertNotNull(we);
		TestClassUtils.assertNotNullString(strAttribute);
		return we.getAttribute(strAttribute);
	}

	/**
	 * Wait for a certain condition to happen. Waiting has a standard timeout with 500ms polling frequency.
	 * 
	 * @param condition
	 * @throws TimeoutException
	 */
	@SuppressWarnings("hiding")
	public <T> T waitFor(ExpectedCondition<T> condition)
	{
		return waitFor(condition, nImplicitTimeOut, TimeUnit.SECONDS);
	}

	/**
	 * Check for a certain condition to happen and return true else return false
	 * 
	 * @param condition
	 * @return boolean
	 */
	public boolean checkFor(ExpectedCondition<T> condition)
	{
		try
		{
			waitFor(condition, nImplicitTimeOut, TimeUnit.SECONDS);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Check for a certain condition to happen in specified time and return true else return false
	 * 
	 * @param condition
	 * @param nTime
	 * @param unit - TimeUnit in seconds
	 * @throws TimeoutException
	 */
	public boolean checkFor(ExpectedCondition<?> condition, int nTime, TimeUnit unit)
	{
		try
		{
			waitFor(condition, nTime, unit);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Wait for a certain condition to happen with a specified timeout.
	 * 
	 * @param condition
	 * @param nTime
	 * @param unit
	 * @return
	 * @throws TimeoutException
	 */

	@SuppressWarnings("hiding")
	public <T> T waitFor(ExpectedCondition<T> condition, int nTime, TimeUnit unit)
	{
		try
		{
			// Override implicit wait temporarily to allow for explicit wait
			webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			// Wait for standard amount of time for defined condition.
			return new FluentWait<WebDriver>(webDriver).withTimeout(Duration.ofSeconds((long) nTime))
				.pollingEvery(Duration.ofMillis(500)).until(condition);
		}
		finally
		{
			webDriver.manage().timeouts().implicitlyWait(nImplicitTimeOut, TimeUnit.SECONDS);
		}
	}

	/**
	 * Waits for new window triggered by a click to completely load before returning. After this method is called,
	 * driver will be switched to the secondary window, so do not forget to switch back.
	 */
	public void waitForAndSwitchToSecondaryWindow()
	{
		for (String strWinHandle : webDriver.getWindowHandles())
		{
			webDriver.switchTo().window(strWinHandle);
		}

		waitFor(new ExpectedCondition<Boolean>()
		{
			public Boolean apply(WebDriver webDriver)
			{
				try
				{
					String strUrl = webDriver.getCurrentUrl();
					if (strUrl.length() > 0)
					{
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
				catch (Exception e)
				{
					return Boolean.FALSE;
				}
			}
		}, nImplicitTimeOut, TimeUnit.SECONDS);
	}

	/**
	 * If you open up a link that goes to a new tab, this method will make that the active tab for your web driver.
	 */
	public void switchToCurrentWindow()
	{
		String strHandle = Iterables.getLast(webDriver.getWindowHandles());
		webDriver.switchTo().window(strHandle);
	}

	/**
	 * Switch to main window
	 */
	public void switchToMainWindow()
	{
		webDriver.switchTo().defaultContent();
	}

	/**
	 * Switch to frame
	 */
	public void switchToFrame()
	{
		webDriver.switchTo().frame(0);
	}

	/**
	 * Switch to frame
	 * 
	 * @param by By
	 */
	public void switchToFrame(By by)
	{
		TestClassUtils.assertNotNull(by);
		WebElement we = webDriver.findElement(by);
		switchToFrame(we);
	}

	/**
	 * Switch to frame
	 * 
	 * @param we WebElement
	 */
	public void switchToFrame(WebElement weElement)
	{
		webDriver.switchTo().frame(weElement);
	}

	public void switchToAlert()
	{
		Alert alert = webDriver.switchTo().alert();
		alert.accept();
	}

	/**
	 * Return current URL
	 * 
	 * @return
	 */
	public String getCurrentUrl()
	{
		return webDriver.getCurrentUrl();
	}

	/**
	 * Return Page Title
	 * 
	 * @return
	 */
	public String getTitle()
	{
		return webDriver.getTitle();
	}

	/**
	 * Return Page URL
	 * 
	 * @return
	 */
	public String getUrl()
	{
		return webDriver.getCurrentUrl();
	}

	/**
	 * Waits until the page gets loaded
	 */
	public void waitForPageLoaded()
	{
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>()
		{
			public Boolean apply(WebDriver webDriver)
			{
				return (((JavascriptExecutor) webDriver).executeScript("return document.readyState")
					.equals("complete") ||
					((JavascriptExecutor) webDriver).executeScript("return document.readystatechange")
						.equals("interactive"));
			}
		};
		Wait<WebDriver> wait = new WebDriverWait(webDriver, nImplicitTimeOut);
		try
		{
			wait.until(expectation);
		}
		catch (Exception ex)
		{
			// do nothing
		}
	}

	/**
	 * Get value stored in the specified Cookie
	 * 
	 * @param strCookieName
	 */
	public String getCookieValue(String strCookieName)
	{
		Cookie cookie = webDriver.manage().getCookieNamed(strCookieName);
		if (cookie != null)
		{
			return cookie.getValue();
		}
		return null;
	}

	/**
	 * Get expiry date of the specified cookie
	 * 
	 * @param strCookieName
	 */
	public Date getCookieExpiry(String strCookieName)
	{
		Cookie cookie = webDriver.manage().getCookieNamed(strCookieName);
		if (cookie != null)
		{
			return cookie.getExpiry();
		}
		return null;
	}

	/**
	 * Get specified cookie path
	 * 
	 * @param strCookieName
	 */
	public String getCookiePath(String strCookieName)
	{
		Cookie cookie = webDriver.manage().getCookieNamed(strCookieName);
		if (cookie != null)
		{
			return cookie.getPath();
		}
		return null;
	}

	/**
	 * Delete specified cookie
	 * 
	 * @param strCookieName
	 */
	public void deleteCookieNamed(String strCookieName)
	{
		webDriver.manage().deleteCookieNamed(strCookieName);
		// wait for cookie to get deleted
		TestUtils.sleep(10000);
		webDriver.navigate().refresh();
	}

	/**
	 * Refresh browser
	 * 
	 */
	public void refresh()
	{
		webDriver.navigate().refresh();
	}

	/**
	 * Is Web Element displayed
	 * 
	 * @param we
	 * @return boolean
	 */
	public boolean isDisplayed(WebElement we)
	{
		TestClassUtils.assertNotNull(we);
		try
		{
			return we.isDisplayed();
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Is Web Element displayed
	 * 
	 * @param we
	 * @return boolean
	 */
	public boolean isDisplayed(By by)
	{
		TestClassUtils.assertNotNull(by);
		WebElement we = webDriver.findElement(by);
		return isDisplayed(we);
	}

	/**
	 * Is Web Element enabled
	 * 
	 * @param by
	 * @return boolean
	 */
	public boolean isEnabled(By by)
	{
		TestClassUtils.assertNotNull(by);
		WebElement we = webDriver.findElement(by);
		return isEnabled(we);
	}

	/**
	 * Is Web Element enabled
	 * 
	 * @param we
	 * @return boolean
	 */
	public boolean isEnabled(WebElement we)
	{
		TestClassUtils.assertNotNull(we);
		TestClassUtils.assertNotNull(we);
		return we.isEnabled();
	}

	/**
	 * Get Object text
	 * 
	 * @return
	 */
	public String getText(WebElement we)
	{
		TestClassUtils.assertNotNull(we);
		return we.getText();
	}

	/**
	 * Get Object text
	 * 
	 * @return
	 */
	public String getText(By by)
	{
		TestClassUtils.assertNotNull(by);
		return webDriver.findElement(by).getText();
	}

	/**
	 * Is Web Element selected
	 * 
	 * @param by
	 * @return boolean
	 */
	public boolean isSelected(By by)
	{
		TestClassUtils.assertNotNull(by);
		WebElement we = webDriver.findElement(by);
		return isSelected(we);
	}

	/**
	 * Is Web Element selected
	 * 
	 * @param we
	 * @return boolean
	 */
	public boolean isSelected(WebElement we)
	{
		TestClassUtils.assertNotNull(we);
		return we.isSelected();
	}

	/**
	 * Find Element
	 * 
	 * @param by
	 * @return WebElement
	 */
	public WebElement findElement(By by)
	{
		TestClassUtils.assertNotNull(by);
		return webDriver.findElement(by);
	}

	/**
	 * Find Elements
	 * 
	 * @param by
	 * @return List<WebElement>
	 */
	public List<WebElement> findElements(By by)
	{
		TestClassUtils.assertNotNull(by);
		return webDriver.findElements(by);
	}

	private WebElement findAndFocusOn(By by)
	{
		return webDriver.findElement(by);
	}

	/**
	 * Select the option by visible text
	 * 
	 * @param by By
	 * @param strText String
	 */
	public void selectOptionByText(By by, String strText)
	{
		TestClassUtils.assertNotNull(by);
		WebElement we = webDriver.findElement(by);
		selectOptionByText(we, strText);
	}

	/**
	 * Select the option by visible text
	 * 
	 * @param we WebElement
	 * @param strText String
	 */
	public void selectOptionByText(WebElement we, String strText)
	{
		TestClassUtils.assertNotNull(we);
		Select objSelect = new Select(we);
		List<WebElement> list = objSelect.getOptions();

		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).getText().trim().equals(strText))
			{
				objSelect.selectByIndex(i);
				break;
			}
		}
	}

	/**
	 * Select the option by value
	 * 
	 * @param by By
	 * @param strValue String
	 */
	public void selectOptionByValue(By by, String strValue)
	{
		TestClassUtils.assertNotNull(by);
		WebElement we = webDriver.findElement(by);
		selectOptionByValue(we, strValue);
	}

	/**
	 * Select the option by value
	 * 
	 * @param we WebElement
	 * @param strValue String
	 */
	public void selectOptionByValue(WebElement we, String strValue)
	{
		TestClassUtils.assertNotNull(we);
		Select objSelect = new Select(we);
		objSelect.selectByValue(strValue);
	}

	/**
	 * Scroll to Element
	 * 
	 * @param by By
	 */
	public void scrollToElement(By by)
	{
		TestClassUtils.assertNotNull(by);
		WebElement we = webDriver.findElement(by);
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", we);
		TestUtils.sleep(500);
	}

	/**
	 * scroll to Element
	 * 
	 * @param we WebElement
	 */
	public void scrollToElement(WebElement we)
	{
		TestClassUtils.assertNotNull(we);
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", we);
		TestUtils.sleep(500);
	}

	/**
	 * Return the value of the specified attribute of the web element
	 * 
	 * @param by By
	 * @param strAttributeName String
	 * 
	 * @return String
	 */
	public String getAttribute(By by, String strAttributeName)
	{
		TestClassUtils.assertNotNull(by);
		WebElement we = webDriver.findElement(by);
		return getAttribute(we, strAttributeName);
	}

	/**
	 * Return the value of the specified attribute of the web element
	 * 
	 * @param we WebElement
	 * @param strAttributeName String
	 * 
	 * @return String
	 */
	public String getAttribute(WebElement we, String strAttributeName)
	{
		TestClassUtils.assertNotNull(we);
		return we.getAttribute(strAttributeName);
	}

	/**
	 * Wait for Ajax 'Ex.Ajax' to finish
	 * 
	 * @author Naini.Ghai
	 */
	public void waitAjaxLoaded()
	{
		// wait until Ajax scripts are finish on the page
		JavascriptExecutor ajaxExe = (JavascriptExecutor) webDriver;
		Boolean loading = true;
		int counter = 1;

		if (ajaxExe == null)
			return; // if there is no JavaScript on the page

		while (loading)
		{
			try
			{
				loading = ((Boolean) ajaxExe.executeScript("return Ext.Ajax.isLoading();"));
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
				LogManager.getLogger().error(e);
			}
			catch (WebDriverException e)
			{
				return;
			}
			counter++;
			if (counter == 120)
			{
				loading = false;
				LogManager.getLogger().error("Wait threshold for Ajax scripts was exceeded.");
				throw new NoSuchElementException(
					"Some SBO Ajax script took more than " + counter / 2 + " seconds to finish.");
			}
		}
	}
}