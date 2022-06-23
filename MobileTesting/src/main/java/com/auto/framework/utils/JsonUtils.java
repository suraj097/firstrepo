package com.auto.framework.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Utility of serializing/de-serializing JSON
 * 
 * @author Naini.Ghai
 *
 */
public class JsonUtils
{
	/**
	 * Converts Java Object to JSON format
	 * 
	 * @param object
	 * @param cType
	 * @return JSON format representation for Java Object
	 */
	public String toJson(Object object, Class<?> cType)
	{
		return new Gson().toJson(object, cType);
	}

	/**
	 * Converts Java Object to JSON format
	 * 
	 * @param object
	 * @param cType
	 * @return JSON format representation for Java Object
	 */
	public String toJson(Object object)
	{
		return new Gson().toJson(object);
	}

	/**
	 * Converts JSON string to Java Object
	 * @param strJson
	 * @param cType
	 * @return Java representation of JSON
	 */
	public <T> T fromJson(String strJson, Class<T> cType)
	{
		return new Gson().fromJson(strJson, cType);
	}

	/**
	 * Convert Json String to Map<String, Object>
	 * 
	 * @param strJsonString
	 * @return
	 */
	public Map<String, Object> jsonToMap(String strJsonString)
	{
		Type type = new TypeToken<HashMap<String, Object>>()
		{
		}.getType();
		return new Gson().fromJson(strJsonString, type);
	}

	/**
	 * Convert Json File to Map<String, Object>
	 * 
	 * @param strFilePath
	 * @return
	 * @throws FileNotFoundException
	 */
	public Map<String, Object> jsonFileToMap(String strFilePath) throws FileNotFoundException
	{
		BufferedReader br = new BufferedReader(new FileReader(strFilePath));
		return jsonToMap(new Gson().toJson(br, Object.class));
	}
}
