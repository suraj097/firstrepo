package com.auto.framework.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.log4j.LogManager;

/**
 * Load the property file for once and stores its in the memory
 * 
 * @author naini.ghai
 * 
 */
public class TestProperties
{
	private TestProperties		instance		= null;
	private Properties			prop			= null;
	private static final String	PROPERTIES		= ".properties";
	private static final String	CONFIG			= "config";
	private static final String	LOCAL_CONFIG	= "local.config";
	private String				strEnvConfig	= "";

	public synchronized TestProperties getInstance()
	{
		if (instance == null)
		{
			instance = new TestProperties();
		}
		return instance;
	}

	public TestProperties()
	{
		if (prop == null)
		{
			prop = getProperties();
			this.strEnvConfig = prop.getProperty("ENV_CONFIG");
		}
	}

	private Properties getProperties()
	{
		Properties properties = new Properties();
		try
		{
			InputStream in = getClass().getClassLoader().getResourceAsStream(CONFIG + PROPERTIES);
			properties.load(in);
			in.close();
			storeProperies(properties);

		}
		catch (FileNotFoundException ex)
		{
			LogManager.getLogger(this.getClass().getName() + " property file not found in the classpath");
		}
		catch (IOException ex)
		{
			LogManager.getLogger(this.getClass().getName() + " Error reading file");
		}
		return properties;
	}

	private void storeProperies(Properties prop)
	{
		try
		{
			InputStream inLocal = getClass().getClassLoader().getResourceAsStream(LOCAL_CONFIG + PROPERTIES);
			Properties properties = new Properties();
			properties.load(inLocal);
			inLocal.close();
			Enumeration<Object> enumKeys = properties.keys();
			while (enumKeys.hasMoreElements())
			{
				String strLocalKey = enumKeys.nextElement().toString();
				if (prop.containsKey(strLocalKey))
				{
					prop.setProperty(strLocalKey, properties.getProperty(strLocalKey));
				}
				else
				{
					prop.put(strLocalKey, properties.getProperty(strLocalKey));
				}
			}
		}
		catch (Exception ex)
		{
			// do nothing
		}
	}

	/**
	 * Return the value of the property name specified else empty string
	 * 
	 * @param strPropertyName
	 * @return
	 */
	public String getPropertyValue(String strPropertyName)
	{
		return getPropertyValue(strPropertyName, strEnvConfig);
	}

	private String getPropertyValue(String strPropertyName, String strEnvConfig)
	{
		if (strEnvConfig != null && !strEnvConfig.equals(""))
		{
			strPropertyName = strPropertyName + "[" + strEnvConfig + "]";
		}
		return ((prop.getProperty(strPropertyName) != null) ? prop.getProperty(strPropertyName).trim() : "");
	}

	/**
	 * Get Root property
	 * @param strPropertyName
	 * @return
	 */
	public String getRootPropertyValue(String strPropertyName)
	{
		return prop.getProperty(strPropertyName).trim();
	}

	/**
	 * Set Root property
	 * @param strPropertyName
	 * @param strPropertyValue
	 */
	public void setRootPropertyValue(String strPropertyName, String strPropertyValue)
	{
		prop.setProperty(strPropertyName, strPropertyValue);
		if (strPropertyName.equalsIgnoreCase("ENV_CONFIG"))
		{
			this.strEnvConfig = strPropertyValue;
		}
	}

	/**
	 * Set property
	 * @param strPropertyName
	 * @param strPropertyValue
	 */
	public void setPropertyValue(String strPropertyName, String strPropertyValue)
	{
		prop.setProperty(strPropertyName, strPropertyValue);
	}

	/**
	 * Check whether the specified property exist; return true if exist otherwise false
	 * 
	 * @param strPropertyName
	 * @return
	 */
	public boolean isPropertyExists(String strPropertyName)
	{
		return (prop.getProperty(strPropertyName) != null);
	}

	public Enumeration<?> getPropertyNames()
	{
		return prop.propertyNames();
	}

}
