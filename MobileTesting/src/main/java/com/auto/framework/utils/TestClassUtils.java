package com.auto.framework.utils;

/**
 * Function library that deals with class name formatting, etc.
 * 
 * @author naini.ghai
 */
public class TestClassUtils
{

	private TestClassUtils()
	{
		// do nothing
	}

	/**
	 * NOTE: This method intentionally takes no String "label" argument. We are using line numbers from the stack
	 * trace to identify which argument to a method is null, which saves developers from having to type strings
	 * into the assert methods, and it saves us from having to intern all those unique strings, forever taking up
	 * memory in the JVM.
	 * 
	 * @throws NullPointerException if obj is null
	 */
	public static void assertNotNull(Object obj)
	{
		if (obj == null)
		{
			throw new NullPointerException("obj is null");
		}
	}

	/**
	 * @throws NullPointerException if str is null
	 */
	public static void assertNotNullString(String str)
	{
		if (str == null)
		{
			throw new NullPointerException("str is null");
		}
	}

	/**
	 * @throws NullPointerException if str is null
	 * @throws IllegalArgumentException if <tt>str.trim().length() == 0</tt> returns true
	 */
	public static void assertNotEmpty(String str)
	{
		if (TestStringUtils.isEmpty(str))
		{
			if (str == null)
			{
				throw new NullPointerException("str is null");
			}

			throw new IllegalArgumentException("str[" + str + "] is empty");
		}
	}
}