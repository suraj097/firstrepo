package com.auto.framework.base;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

public class DataProviderFactory
{
	private DataProviderFactory()
	{
		// do nothing
	}

	@DataProvider(name = "json")
	public static Iterator<Object[]> jsonDataProvider(ITestContext context, Method method) throws Exception
	{
		ArrayList<HashMap<String, Object>> testdataList;
		JsonFilePath parameters = method.getAnnotation(JsonFilePath.class);
		Path resourceDirectory = Paths.get("src", "test", "resources", "jsontestdata", parameters.path());
		String strInputFile = System.getProperty("user.dir") + "/" + resourceDirectory.toString();
		JsonParser.getJsonParser(strInputFile);
		Collection<Object[]> dp = new ArrayList<Object[]>();
		testdataList = JsonParser.getTestData(strInputFile + "/" + method.getName());

		for (HashMap<String, Object> testdata : testdataList)
		{
			dp.add(new Object[] {testdata});
		}
		return dp.iterator();
	}
}
