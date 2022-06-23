package com.auto.framework.base;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * The class is loading test data from JSON files. Once a file is loaded, the data are stored in @allDataMap to
 * prevent reopening the same file over and over. The test data are in loaded in HashMap<String, Object> format.
 */
public class JsonParser
{

	private static JsonParser											INSTANCE		= null;
	private static HashMap<String, ArrayList<HashMap<String, Object>>>	hmapAllDataMap	= new HashMap<>();
	private static ArrayList<String>									lstFile			= new ArrayList<>();
	private static ReentrantLock										rLock			= new ReentrantLock(true);

	private JsonParser()
	{
		// do nothing
	}

	public static JsonParser getJsonParser(String strFilePath) throws Exception
	{
		try
		{
			synchronized (rLock)
			{
				rLock.lock();
				if (INSTANCE == null)
				{
					INSTANCE = new JsonParser();
				}
			}

			if (!lstFile.contains(strFilePath))
			{
				lstFile.add(strFilePath);
				parseJsonFile(strFilePath);
			}
		}
		finally
		{
			rLock.unlock();
		}
		return INSTANCE;
	}

	public static ArrayList<HashMap<String, Object>> getTestData(String strMethodName)
	{
		ArrayList<HashMap<String, Object>> lstTestData = hmapAllDataMap.get(strMethodName);
		if (lstTestData == null)
		{
			LogManager.getLogger()
				.info("Data for the method '" + strMethodName + "' was not found. The test will be skipped.");
		}
		return lstTestData;
	}

	@SuppressWarnings("unchecked")
	private static HashMap<String, ArrayList<HashMap<String, Object>>> parseJsonFile(String filepath)
		throws Exception
	{
		JSONParser parser = new JSONParser();
		try
		{
			Object obj;
			obj = parser.parse(new FileReader(filepath));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray dataList = (JSONArray) jsonObject.get("data");
			for (Object data : dataList)
			{
				JSONObject jsonObject1 = (JSONObject) data;
				JSONArray jsonArrTestData = (JSONArray) jsonObject1.get("testdata");
				String mname = (String) jsonObject1.get("methodName");
				String[] arrMethod = null;
				if (mname.contains(","))
				{
					arrMethod = mname.split(",");
				}
				ArrayList<HashMap<String, Object>> lstTestDataMap = new ArrayList<>();
				for (Object testdata : jsonArrTestData)
				{
					JSONObject testdatajsonObject = (JSONObject) testdata;
					String testdataString = testdatajsonObject.toJSONString();
					HashMap<String, Object> resultMap = new Gson().fromJson(testdataString,
						new TypeToken<HashMap<String, Object>>()
						{
						}.getType());
					lstTestDataMap.add(resultMap);
				}
				if (arrMethod != null)
				{
					for (String key : arrMethod)
					{
						hmapAllDataMap.put(filepath + "/" + key, jsonArrTestData);
					}
				}
				else
				{
					hmapAllDataMap.put(filepath + "/" + mname, jsonArrTestData);
				}
			}
		}
		catch (IOException e)
		{
			throw new Exception("FileNotFoundException: Not able to find or parse Json " + e.getMessage(), e);
		}
		catch (ParseException e)
		{
			throw new Exception("Not able to find or parse Json" + e.getMessage(), e);
		}
		catch (NullPointerException e)
		{
			throw new Exception(
				"Verify that you have data inside the file and there are 'methodName' and 'testdata'=", e);
		}
		return hmapAllDataMap;
	}
	
}
