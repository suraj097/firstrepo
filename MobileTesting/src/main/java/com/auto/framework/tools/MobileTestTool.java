package com.auto.framework.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.auto.framework.base.BaseTestCase;
import com.auto.framework.base.Device;
import com.auto.framework.base.TestProperties;
import com.auto.framework.utils.ExceptionUtil;
import com.auto.framework.utils.TestUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class MobileTestTool extends UITestTool implements ITool
{
	private static final String				USER_DIR			= "user.dir";
	private static final String				GRID_ENABLED		= "GRID_ENABLED";
	private static final String				WEB_APP				= "WEB_APP";
	private static final String				ANDROID				= "ANDROID";
	private static final String				BROWSER_NAME		= "browserName";
	private static ReentrantLock			mobRetrantLock		= new ReentrantLock(true);
	private AppiumDriverLocalService		service				= null;
	private AndroidDriver<MobileElement>	androidDriver		= null;
	private IOSDriver<MobileElement>		iOSDriver			= null;
	private AppiumDriver<MobileElement>		appiumDriver		= null;
	private static List<Device>				lstDevice			= null;
	private Device							device				= null;
	private TestProperties					prop				= null;
	private String							strGridMaster		= "";
	private String							strGridMasterPort	= "";

	private enum eLogLevel
	{
		error,
		warn,
		info,
		debug
	}

	/**
	 * Get instance of Mobile Test Tool. Launches WEB_APP by default
	 * 
	 * @param baseTestCase
	 * @return
	 */
	public static MobileTestTool getInstance(BaseTestCase baseTestCase)
	{
		return getInstance(baseTestCase, null);
	}

	/**
	 * Get instance of Mobile Test Tool. Launches WEB APP or NATIVE APP, as per the strAppType provided.
	 * 
	 * @param baseTestCase - instance of BaseTestCase
	 * @param strAppType - WEB_APP or NATIVE_PP
	 * @return
	 */
	public static MobileTestTool getInstance(BaseTestCase baseTestCase, String strAppType)
	{
		MobileTestTool ttMob = new MobileTestTool();
		ttMob.prop = baseTestCase.getProperties();
		ttMob.strGridMaster = ttMob.prop.getRootPropertyValue("GRID_MASTER_HOST");
		ttMob.strGridMasterPort = ttMob.prop.getRootPropertyValue("GRID_MASTER_PORT");
		try
		{
			synchronized (mobRetrantLock)
			{
				mobRetrantLock.lock();
				ttMob.device = ttMob.getDeviceFromDeviceFarm(ttMob, baseTestCase.getApp());
				String strMobileOS = System.getProperty("os.name").toLowerCase().contains("win") ? ANDROID
					: ttMob.device.MOB_OS.trim().toUpperCase();
				ttMob.startAppium(ttMob, baseTestCase, strMobileOS);
				if (strMobileOS.equalsIgnoreCase(ANDROID))
				{
					ttMob.androidDriver = ttMob.createAndroidDriver(baseTestCase,
						((strAppType != null) ? strAppType : WEB_APP));
					ttMob.webDriver = ttMob.androidDriver;
					ttMob.appiumDriver = ttMob.androidDriver;
				}
				else
				{
					ttMob.iOSDriver = ttMob.createiOSDriver(baseTestCase,
						((strAppType != null) ? strAppType : WEB_APP));
					ttMob.webDriver = ttMob.iOSDriver;
					ttMob.appiumDriver = ttMob.iOSDriver;
				}
				// Set Mobile Device INUSE = "Y"
				ttMob.device.MOB_INUSE = "Y";
				baseTestCase.setTool(ttMob);
			}
		}
		catch (Exception ex)
		{
			return null;
		}
		finally
		{
			mobRetrantLock.unlock();
		}
		return ttMob;
	}

	public AppiumDriver<MobileElement> getAppiumDriver()
	{
		return appiumDriver;
	}

	public AndroidDriver<MobileElement> getAndroidDriver()
	{
		return androidDriver;
	}

	public IOSDriver<MobileElement> getIOSDriver()
	{
		return iOSDriver;
	}

	private Device getDeviceFromDeviceFarm(MobileTestTool ttMob, String strApp)
	{
		if (MobileTestTool.lstDevice == null)
		{
			MobileTestTool.lstDevice = ttMob.getDeviceFarmDetails(ttMob.prop.getPropertyValue("DEVICE_FARM"));
		}

		for (Device currentDevice : MobileTestTool.lstDevice)
		{
			if (currentDevice.MOB_INUSE.equalsIgnoreCase("N") &&
				currentDevice.APPLICATION.equalsIgnoreCase(strApp))
			{
				ttMob.device = currentDevice;
				break;
			}
		}
		return ttMob.device;
	}

	private void startAppium(MobileTestTool ttMob, BaseTestCase baseTestCase, String strMobileOS)
	{
		try
		{

			if ((ttMob.prop.getRootPropertyValue(GRID_ENABLED) != null &&
				ttMob.prop.getRootPropertyValue(GRID_ENABLED).equals("N")) ||
				!ttMob.isGridAvailalbe(ttMob.strGridMaster, ttMob.strGridMasterPort))
			{
				// Build the Appium service AppiumServiceBuilder
				AppiumServiceBuilder builder = new AppiumServiceBuilder();
				builder.withIPAddress(device.MOB_APPIUM_HOST);
				builder.usingAnyFreePort();
				builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
				builder.withArgument(GeneralServerFlag.LOG_LEVEL, getAppiumLogLevel(baseTestCase));
				// Start the server with the builder service
				service = AppiumDriverLocalService.buildService(builder);
				baseTestCase.getLogger().info("Starting APPIUM as Service");
				service.start();
				baseTestCase.getLogger().info("APPIUM started : " + service.getUrl());
				baseTestCase.getLogger().info("Appium Running : " + ttMob.isAppiumRunning());
			}
			else
			{
				baseTestCase.getLogger().info("Appium Running on GRID");
			}
		}
		catch (Exception ex)
		{
			baseTestCase.getLogger().error("Appium not started" + ExceptionUtil.stackTraceToString(ex));
		}
	}

	public boolean isAppiumRunning()
	{
		return service.isRunning();
	}

	public void close()
	{
		try
		{
			// close and quit driver
			if (isAndroidDriverRunning())
			{
				androidDriver.closeApp();
				TestUtils.sleep(5000);
				androidDriver.quit();
				TestUtils.sleep(5000);
				androidDriver = null;
				iOSDriver = null;
			}

			stopAppium();
		}
		catch (Exception ex)
		{
			// do nothing
		}

	}

	private void stopAppium()
	{
		// close Appium service
		if (service != null && isAppiumRunning())
		{
			service.stop();
			TestUtils.sleep(30000);
			service = null;
		}
	}

	public boolean isAndroidDriverRunning()
	{
		return (androidDriver != null);
	}

	/**
	 * 
	 * @param baseTestCase
	 * @param strAppType "WEB_APP" or "NATIVE_APP"
	 * @return AndroidDriver <br>
	 *         <b><u>For Example</u></b>- <br>
	 *         capabilities.setCapability("platformName", "Android");<br>
	 *         capabilities.setCapability("platformVersion", "6.0");<br>
	 *         capabilities.setCapability("device", "Android");<br>
	 *         capabilities.setCapability("deviceReadyTimeout", "180");<br>
	 *         capabilities.setCapability("deviceName", "Android Emulator");<br>
	 *         <br>
	 *         capabilities.setCapability("browserName", "Chrome");<br>
	 *         or<br>
	 *         capabilities.setCapability("browserName", "");<br>
	 *         capabilities.setCapability("app", "<absolute path to .app file>");<br>
	 *         capabilities.setCapability("appPackage", "<appPackage>");<br>
	 *         capabilities.setCapability("appActivity", "<appActivity>");<br>
	 */

	private AndroidDriver<MobileElement> createAndroidDriver(BaseTestCase baseTestCase, String strAppType)
	{
		DesiredCapabilities capabilities = DesiredCapabilities.android();
		// Set Platfrom Capabilities
		setAndroidPlatformCapabilities(device, capabilities);
		// Set Android App Capabilities
		setAndroidAppCapabilities(baseTestCase, device, strAppType, capabilities);
		baseTestCase.getLogger().info("Instantiating Mobile Driver");
		// Instantiating Mobile Driver
		return instantiateAndroidDriver(device, baseTestCase, capabilities);
	}

	private void setAndroidPlatformCapabilities(Device device, DesiredCapabilities capabilities)
	{
		// Set Capabilities
		if (device.MOB_IS_REAL_DEVICE.equalsIgnoreCase("N"))
		{
			if (System.getProperty("os.name").toLowerCase().contains("mac"))
				capabilities.setCapability("chromedriverExecutable",
					System.getProperty(USER_DIR) + "/src/main/resources/mobile-drivers/chromedriver");
			else
				capabilities.setCapability("chromedriverExecutable",
					System.getProperty(USER_DIR) + "/src/main/resources/mobile-drivers/chromedriver.exe");
			// "udid"
			capabilities.setCapability(MobileCapabilityType.UDID, device.MOB_AND_EMU_DEVICE_ID);
		}
		else
		{
			// "udid"
			capabilities.setCapability(MobileCapabilityType.UDID, device.MOB_AND_REAL_DEVICE_ID);
		}
		// "newCommandTimeout" - set to 5 mins
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);
		// "platformName"
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, device.MOB_AND_PLATFORM);
		capabilities.setCapability("device", device.MOB_AND_DEVICE);
		capabilities.setCapability("unicodeKeyboard", true);
		if (device.MOB_IS_REAL_DEVICE.equalsIgnoreCase("N"))
		{
			// "platformVersion"
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, device.MOB_AND_EMU_PLATFORM_VERSION);
			// "deviceName"
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device.MOB_AND_EMU_DEVICE_NAME);
			// "avd"
			capabilities.setCapability("avd", device.MOB_AND_EMU_AVD);
		}
		else
		{
			// "platformVersion"
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,
				device.MOB_AND_REAL_PLATFORM_VERSION);
			// "deviceName"
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device.MOB_AND_REAL_DEVICE_NAME);
		}
	}

	private void setAndroidAppCapabilities(BaseTestCase baseTestCase, Device device, String strAppType, DesiredCapabilities capabilities)
	{
		if (strAppType != null && strAppType.equalsIgnoreCase(WEB_APP))
		{
			if (device.MOB_IS_REAL_DEVICE.equalsIgnoreCase("N"))
			{
				// "browsrName"
				capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Browser");
			}
			else
			{
				// "browsrName"
				capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
			}
		}
		else
		{
			capabilities.setCapability(BROWSER_NAME, "");
			setResetAppCapabilities(baseTestCase, capabilities);
			capabilities.setCapability("autoGrantPermissions", true);
			// "app"
			capabilities.setCapability(MobileCapabilityType.APP, device.MOB_AND_APP);
			capabilities.setCapability("appPackage", device.MOB_AND_APP_PACKAGE);
			capabilities.setCapability("appActivity", device.MOB_AND_APP_ACTIVITY);
		}
		// "UIAutomator2"
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
	}

	private AndroidDriver<MobileElement> instantiateAndroidDriver(Device device, BaseTestCase baseTestCase,
		DesiredCapabilities capabilities)
	{
		String strAppiumUrl = "";
		boolean bGridEnabled = false;
		AndroidDriver<MobileElement> driver = null;
		if (prop.getRootPropertyValue(GRID_ENABLED) != null &&
			prop.getRootPropertyValue(GRID_ENABLED).equals("Y") &&
			isAppiumNodeAvailable(device.MOB_APPIUM_HOST, device.MOB_APPIUM_PORT))
		{
			strAppiumUrl = "http://" + device.MOB_APPIUM_HOST + ":" + device.MOB_APPIUM_PORT + "/wd/hub";
			bGridEnabled = true;
		}
		for (int i = 5; i > 0; i--)
		{
			try
			{
				driver = new AndroidDriver<MobileElement>(
					(bGridEnabled) ? new URL(strAppiumUrl) : service.getUrl(), capabilities);
				TestUtils.sleep(10000);
				// If we successfully attach to appium, exit the loop.
				break;
			}
			catch (Exception ex)
			{
				LogManager.getRootLogger().error(ExceptionUtil.stackTraceToString(ex));
				TestUtils.sleep(20000);
			}
		}
		if (driver != null)
		{
			baseTestCase.getLogger().info("Mobile Driver instantiated");
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}
		return driver;
	}

	private boolean isAppiumNodeAvailable(String strAppiumHost, String strAppiumPort)
	{
		boolean bReachable = false;
		try
		{
			InetAddress.getByName(strAppiumHost).isReachable(10);
			URL u = new URL("http://" + strAppiumHost + ":" + strAppiumPort + "/wd/hub/");
			HttpURLConnection huc = (HttpURLConnection) u.openConnection();
			huc.setRequestMethod("GET");
			huc.connect();
			bReachable = true;
		}
		catch (Exception e)
		{
			LogManager.getRootLogger()
				.info("Unable to connect to http://" + strAppiumHost + ":" + strAppiumPort + "/wd/hub/", e);
		}
		return bReachable;
	}

	private IOSDriver<MobileElement> createiOSDriver(BaseTestCase baseTestCase, String strAppType)
	{
		// Get capability for IPHONE or IPAD
		DesiredCapabilities capabilities = getCapabilities(device);
		setIOSPlatformCapabilities(baseTestCase, device, capabilities);
		setIOSAppCapabilities(device, strAppType, capabilities);
		baseTestCase.getLogger().info("Instantiating Mobile Driver");
		return instantiateIOSDriver(baseTestCase, capabilities);
	}

	private DesiredCapabilities getCapabilities(Device device)
	{
		DesiredCapabilities capabilities;
		if (device.MOB_IOS_DEVICE_TYPE.equalsIgnoreCase("IPHONE"))
		{
			capabilities = DesiredCapabilities.iphone();
		}
		else
		{
			capabilities = DesiredCapabilities.ipad();
		}
		return capabilities;
	}

	private void setIOSPlatformCapabilities(BaseTestCase baseTestCase, Device device,
		DesiredCapabilities capabilities)
	{
		// "newCommandTimeout" - set to 5 mins
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
		// "platformName"
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, device.MOB_IOS_PLATFORM);
		capabilities.setCapability("device", device.MOB_IOS_DEVICE);
		setResetAppCapabilities(baseTestCase, capabilities);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
		if (device.MOB_IS_REAL_DEVICE.equalsIgnoreCase("N"))
		{
			// "udid"
			capabilities.setCapability(MobileCapabilityType.UDID, device.MOB_IOS_SIM_DEVICE_ID);
			// "platformVersion"
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, device.MOB_IOS_SIM_PLATFORM_VERSION);
			// "deviceName"
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device.MOB_IOS_SIM_DEVICE_NAME);
		}
		else
		{
			// "platformVersion"
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,
				device.MOB_IOS_REAL_PLATFORM_VERSION);
			// "deviceName"
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device.MOB_IOS_REAL_DEVICE_NAME);
			// "udid"
			capabilities.setCapability(MobileCapabilityType.UDID, device.MOB_IOS_REAL_DEVICE_ID);
			// "xcode org id"
			capabilities.setCapability("xcodeOrgId", device.MOB_IOS_REAL_XCODE_ORG_ID);
			capabilities.setCapability("xcodeSigningId", "iPhone Developer");
		}
	}

	private void setIOSAppCapabilities(Device device, String strAppType, DesiredCapabilities capabilities)
	{
		if (strAppType != null && strAppType.equalsIgnoreCase(WEB_APP))
		{
			capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
		}
		else
		{
			// app
			capabilities.setCapability(MobileCapabilityType.APP, device.MOB_IOS_APP);
			capabilities.setCapability("launchTimeout", "200000");
		}
	}

	private IOSDriver<MobileElement> instantiateIOSDriver(BaseTestCase baseTestCase,
		DesiredCapabilities capabilities)
	{
		String strAppiumUrl = "";
		boolean bGridEnabled = false;
		IOSDriver<MobileElement> driver = null;
		if (prop.getRootPropertyValue(GRID_ENABLED) != null &&
			prop.getRootPropertyValue(GRID_ENABLED).equals("Y") &&
			isAppiumNodeAvailable(device.MOB_APPIUM_HOST, device.MOB_APPIUM_PORT))
		{
			strAppiumUrl = "http://" + device.MOB_APPIUM_HOST + ":" + device.MOB_APPIUM_PORT + "/wd/hub";
			bGridEnabled = true;
		}

		for (int i = 5; i > 0; i--)
		{
			try
			{
				driver = new IOSDriver<MobileElement>((bGridEnabled) ? new URL(strAppiumUrl) : service.getUrl(),
					capabilities);
				TestUtils.sleep(10000);
				// If we successfully attach to appium, exit the loop.
				break;
			}
			catch (Exception ex)
			{
				TestUtils.sleep(20000);
			}
		}
		if (driver != null)
		{
			baseTestCase.getLogger().info("Mobile Driver instantiated");
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}
		return driver;
	}

	private List<Device> getDeviceFarmDetails(String strDeviceFarm)
	{
		StringBuilder strbDevice = new StringBuilder("");
		try (BufferedReader br = new BufferedReader(
			new FileReader(System.getProperty(USER_DIR) + "/src/test/resources/" + strDeviceFarm)))
		{
			String strLine = "";
			while ((strLine = br.readLine()) != null)
			{
				strbDevice.append(strLine);
			}
		}
		catch (IOException e1)
		{
			LogManager.getRootLogger().error(ExceptionUtil.stackTraceToString(e1));
			return null;
		}
		TypeToken<List<Device>> token = new TypeToken<List<Device>>()
		{
			private static final long serialVersionUID = 1L;
		};
		return new Gson().fromJson(strbDevice.toString(), token.getType());
	}

	public Device getDevice()
	{
		return device;
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

	private String getAppiumLogLevel(BaseTestCase baseTestCase)
	{
		String strLogLevel = baseTestCase.getProperties().getRootPropertyValue("APPIUM_LOG_LEVEL");
		eLogLevel logLevel = eLogLevel.valueOf(eLogLevel.class,
			((strLogLevel != null && !strLogLevel.equals("")) ? strLogLevel.toLowerCase().trim() : "error"));
		switch (logLevel)
		{
			case info:
				strLogLevel = "info";
				break;
			case debug:
				strLogLevel = "debug";
				break;
			case error:
				strLogLevel = "error";
				break;
			case warn:
				strLogLevel = "warn";
				break;
			default:
				strLogLevel = "error";
		}
		return strLogLevel;
	}
	
	private void setResetAppCapabilities(BaseTestCase baseTestCase, DesiredCapabilities capabilities)
	{
		if (baseTestCase.getProperties().getRootPropertyValue("RESET_NATIVE_APP") != null &&
			!baseTestCase.getProperties().getRootPropertyValue("RESET_NATIVE_APP").equals("N"))
		{
			capabilities.setCapability(MobileCapabilityType.FULL_RESET, true);
			capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
		}
	}
}
