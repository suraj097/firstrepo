package com.auto.framework.utils;

import java.awt.Toolkit;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.auto.framework.base.BaseTestCase;
import com.auto.framework.base.TestProperties;

/**
 * Utility to invoke Browser - Firefox, Chrome, IE, Edge
 * 
 * @author naini.ghai
 *
 */
public class BrowserHandler
{
	private static final String		JENKINS_LABEL		= "jenkins.label";
	private static final String		GRID_ENABLED		= "GRID_ENABLED";
	private static final String		USER_DIR			= "user.dir";
	private ThreadLocal<WebDriver>	threadWebDriver		= new ThreadLocal<WebDriver>();
	private int						nImplicitTimeOut	= 30;
	private static final String		DRIVER_PATH			= "/src/main/resources/drivers/";
	private String					strFileDownloadPath	= "";
	private TestProperties			prop				= null;
	private String					strJenkinsLabel		= "";
	private String					strGridMaster		= "";
	private String					strGridMasterPort	= "";

	private enum _eDriver
	{
		FIREFOX,
		CHROME,
		IE,
		EDGE,
		SAFARI,
		HEADLESS
	}

	/**
	 * Launch Browser
	 * 
	 * @param strBrowser
	 * @return
	 * @throws Exception
	 */
	@Parameters({"browser"})
	public WebDriver createUIDriver(BaseTestCase baseTestCase, @Optional("CHROME") String strBrowser)
	{
		this.prop = baseTestCase.getProperties();
		strGridMaster = prop.getRootPropertyValue("GRID_MASTER_HOST");
		strGridMasterPort = prop.getRootPropertyValue("GRID_MASTER_PORT");
		strJenkinsLabel = (baseTestCase.getJenkinsLabel() != null &&
			!baseTestCase.getJenkinsLabel().equalsIgnoreCase("")) ? baseTestCase.getJenkinsLabel() : "";
		this.strFileDownloadPath = baseTestCase.getFileDownloadPath();
		_eDriver driver = _eDriver.valueOf(_eDriver.class, strBrowser);
		switch (driver)
		{
			case FIREFOX:
				threadWebDriver.set(createFirefoxDriver());
				break;
			case CHROME:
			case HEADLESS:
				boolean bHeadless = false;
				if (driver.toString().trim().equalsIgnoreCase("HEADLESS"))
				{
					bHeadless = true;
				}
				threadWebDriver.set(createChromeDriver(bHeadless));
				break;
			case IE:
				threadWebDriver.set(createIEDriver());
				break;
			case EDGE:
				threadWebDriver.set(createEdgeDriver());
				break;
			case SAFARI:
				threadWebDriver.set(createSafariDriver());
				break;
		}

		deleteAllCookies();

		try
		{
			threadWebDriver.get().manage().window().maximize();
		}
		catch (Exception ex)
		{
			// Handling Maximize issue in MAC for Chrome
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenResolution = new Dimension((int) toolkit.getScreenSize().getWidth(),
				(int) toolkit.getScreenSize().getHeight());
			threadWebDriver.get().manage().window().setSize(screenResolution);
		}

		threadWebDriver.get().manage().timeouts().implicitlyWait(nImplicitTimeOut, TimeUnit.SECONDS);
		return threadWebDriver.get();
	}

	private String getOS()
	{
		return System.getProperty("os.name");
	}

	private void deleteAllCookies()
	{

		threadWebDriver.get().manage().deleteAllCookies();
		try
		{
			Thread.sleep(5000);
		}
		catch (Exception ex)
		{
			// do nothing
		}
	}

	public int getWeDriverTimeOut()
	{
		return nImplicitTimeOut;
	}

	private WebDriver createFirefoxDriver()
	{
		if (!getOS().toLowerCase(Locale.ROOT).contains("win"))
			System.setProperty("webdriver.gecko.driver",
				System.getProperty(USER_DIR) + DRIVER_PATH + "geckodriver");
		else
			System.setProperty("webdriver.gecko.driver",
				System.getProperty(USER_DIR) + DRIVER_PATH + "geckodriver.exe");
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		firefoxOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
		firefoxOptions.addPreference("marionette", true);
		firefoxOptions.addPreference("browser.download.folderList", 2);
		firefoxOptions.addPreference("browser.download.manager.showWhenStarting", false);
		firefoxOptions.addPreference("browser.download.dir", strFileDownloadPath);
		firefoxOptions.addPreference("browser.helperApps.neverAsk.saveToDisk",
			"application/pdf,text/plain,application/vnd.ms-excel,application/rtfimage/tiff,multipart/related");
		firefoxOptions.addPreference("pdfjs.disabled", true);
		if (prop.getRootPropertyValue(GRID_ENABLED) != null &&
			prop.getRootPropertyValue(GRID_ENABLED).equals("Y") &&
			isGridAvailalbe(strGridMaster, strGridMasterPort))
		{
			try
			{
				if (!strJenkinsLabel.equalsIgnoreCase(""))
				{
					firefoxOptions.setCapability(JENKINS_LABEL, strJenkinsLabel);
				}
				return new RemoteWebDriver(new URL(getHubUrl()), firefoxOptions);
			}
			catch (MalformedURLException e)
			{
				return null;
			}
		}
		else
		{
			return new FirefoxDriver(firefoxOptions);
		}
	}

	private WebDriver createIEDriver()
	{
		System.setProperty("webdriver.ie.driver",
			System.getProperty(USER_DIR) + DRIVER_PATH + "IEDriverServer.exe");
		InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
		internetExplorerOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, false);
		if (prop.getRootPropertyValue(GRID_ENABLED) != null &&
			prop.getRootPropertyValue(GRID_ENABLED).equals("Y") &&
			isGridAvailalbe(strGridMaster, strGridMasterPort))
		{
			try
			{
				if (!strJenkinsLabel.equalsIgnoreCase(""))
				{
					internetExplorerOptions.setCapability(JENKINS_LABEL, strJenkinsLabel);
				}
				return new RemoteWebDriver(new URL(getHubUrl()), internetExplorerOptions);
			}
			catch (MalformedURLException e)
			{
				return null;
			}
		}
		else
		{
			return new InternetExplorerDriver(internetExplorerOptions);
		}
	}

	private WebDriver createEdgeDriver()
	{
		System.setProperty("webdriver.edge.driver",
			System.getProperty(USER_DIR) + DRIVER_PATH + "MicrosoftWebDriver.exe");
		EdgeOptions edgeOptions = new EdgeOptions();
		edgeOptions.setPageLoadStrategy("eager");
		if (prop.getRootPropertyValue(GRID_ENABLED) != null &&
			prop.getRootPropertyValue(GRID_ENABLED).equals("Y") &&
			isGridAvailalbe(strGridMaster, strGridMasterPort))
		{
			try
			{
				if (!strJenkinsLabel.equalsIgnoreCase(""))
				{
					edgeOptions.setCapability(JENKINS_LABEL, strJenkinsLabel);
				}
				return new RemoteWebDriver(new URL(getHubUrl()), edgeOptions);
			}
			catch (MalformedURLException e)
			{
				return null;
			}
		}
		else
		{
			return new EdgeDriver(edgeOptions);
		}
	}

	private WebDriver createSafariDriver()
	{
		SafariOptions safariOptions = new SafariOptions();
		// if you wish safari to forget session everytime
		safariOptions.setCapability("ensureCleanSession", "true");

		DesiredCapabilities safariCapabilities = DesiredCapabilities.safari();
		safariCapabilities.setJavascriptEnabled(true);
		safariCapabilities.setAcceptInsecureCerts(true);
		safariCapabilities.setPlatform(Platform.MAC);
		safariOptions.merge(safariCapabilities);
		if (prop.getRootPropertyValue(GRID_ENABLED) != null &&
			prop.getRootPropertyValue(GRID_ENABLED).equals("Y") &&
			isGridAvailalbe(strGridMaster, strGridMasterPort))
		{
			try
			{
				if (!strJenkinsLabel.equalsIgnoreCase(""))
				{
					safariOptions.setCapability(JENKINS_LABEL, strJenkinsLabel);
				}
				return new RemoteWebDriver(new URL(getHubUrl()), safariOptions);
			}
			catch (MalformedURLException e)
			{
				return null;
			}
		}
		else
		{
			return new SafariDriver(safariOptions);
		}
	}

	private WebDriver createChromeDriver(boolean bHeadless)
	{
		if (!getOS().toLowerCase(Locale.ROOT).contains("win"))
			System.setProperty("webdriver.chrome.driver",
				System.getProperty(USER_DIR) + DRIVER_PATH + "chromedriver");
		else
			System.setProperty("webdriver.chrome.driver",
				System.getProperty(USER_DIR) + DRIVER_PATH + "chromedriver.exe");
		ChromeOptions chromeOptions = new ChromeOptions();
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("download.default_directory", strFileDownloadPath);
		chromeOptions.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
		chromeOptions.setCapability("download.default_directory", strFileDownloadPath);
		chromeOptions.setExperimentalOption("prefs", prefs);
		chromeOptions.addArguments("--test-type");
		chromeOptions.addArguments("--disable-extensions");
		chromeOptions.addArguments("--disable-infobars");
		chromeOptions.addArguments("--disable-save-password-bubble");
		chromeOptions.addArguments("--enable-geolocation");
		chromeOptions.addArguments("--start-maximized");
		if (bHeadless)
		{
			chromeOptions.addArguments("headless");
			chromeOptions.addArguments("window-size=1200x600");
		}
		chromeOptions.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		if (prop.getRootPropertyValue(GRID_ENABLED) != null &&
			prop.getRootPropertyValue(GRID_ENABLED).equals("Y") &&
			isGridAvailalbe(strGridMaster, strGridMasterPort))
		{
			try
			{
				if (!strJenkinsLabel.equalsIgnoreCase(""))
				{
					chromeOptions.setCapability(JENKINS_LABEL, strJenkinsLabel);
					chromeOptions.setCapability("jenkins.nodeName","(master)");
				}
				return new RemoteWebDriver(new URL(getHubUrl()), chromeOptions);
			}
			catch (MalformedURLException e)
			{
				return null;
			}
		}
		else
		{
			return new ChromeDriver(chromeOptions);
		}
	}

	private String getHubUrl()
	{
		return "http://" + strGridMaster + ":" + strGridMasterPort + "/wd/hub";
	}

	private boolean isGridAvailalbe(String strIPAddress, String strIPPort)
	{
		boolean bReachable = false;
		try
		{
			InetAddress.getByName(strIPAddress).isReachable(10);
			URL u = new URL("http://" + strIPAddress + ":" + strIPPort + "/grid/console/");
			HttpURLConnection huc = (HttpURLConnection) u.openConnection();
			huc.setRequestMethod("GET");
			huc.connect();
			bReachable = true;
		}
		catch (Exception e)
		{
			LogManager.getRootLogger().error("Unable to connect to the grid on IP=" + strIPAddress, e);
		}
		return bReachable;
	}

}
