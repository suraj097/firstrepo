package com.auto.framework.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Function Library that contains methods that provide various string utilities.
 * 
 * @author naini.ghai
 * 
 */
public class TestStringUtils
{
	private static final String		LENGTH_MUST_BE_GREATER_THAN_0	= "Length must be greater than 0";
	public static final String[]	EMPTY_STRING_ARRAY				= new String[0];

	public static class MatchExact
	{
		private final String strValue;

		public MatchExact(String str)
		{
			strValue = str;
		}

		public boolean passes(String str)
		{
			return (str == null || strValue.equals(str));
		}
	}

	public static class MatchTrimmed
	{
		private final String strValue;

		public MatchTrimmed(String str)
		{
			strValue = (str == null ? null : str.trim());
		}

		public boolean passes(String str)
		{
			return (strValue == null || strValue.equals(str.trim()));
		}
	}

	public static class MatchIgnoreCase
	{
		private final String strValue;

		public MatchIgnoreCase(String str)
		{
			strValue = str;
		}

		public boolean passes(String str)
		{
			return (strValue == null || strValue.equals(str.trim()));
		}
	}

	public static class MatchTrimmedIgnoreCase
	{
		private final String strValue;

		public MatchTrimmedIgnoreCase(String str)
		{
			strValue = (str == null ? null : str.trim());
		}

		public boolean passes(String str)
		{
			if (strValue == null)
			{
				return (str == null);
			}

			if (str == null)
			{
				return false;
			}

			return (strValue.equalsIgnoreCase(str.trim()));
		}
	}

	private TestStringUtils()
	{
		// Prevent constructor from being called by outsiders
	}

	/**
	 * Returns true IFF the specified string contains at least one non-whitespace char.
	 * 
	 * @param str A string, may be null
	 * @return boolean
	 */
	public static boolean isNotEmpty(String str)
	{
		return !isEmpty(str);
	}

	/**
	 * Returns true if the specified string is null, empty, or just whitespace.
	 * 
	 * @param str A string, may be null
	 * @return boolean
	 */
	public static boolean isEmpty(String str)
	{
		if (str == null)
		{
			return true;
		}

		for (int ndx = 0; ndx < str.length(); ndx++)
		{
			if (str.charAt(ndx) > ' ')
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Trim (remove spaces) from the pass in string. Null will return null;
	 * 
	 * @param str A string
	 * @return string - the trimmed string or null if the string supplied is null
	 */
	public static String trim(String str)
	{
		// If the string passed in is null, return null
		if (str == null)
		{
			return null;
		}

		// we don't have a null string, return the 'trimmed' string
		return str.trim();
	}

	/**
	 * Trim (remove spaces) from the passed in string. Null or and empty string will return null
	 * 
	 * @param str A string, may be null or empty
	 * @return string - a non-empty trimmed string, or null
	 */
	public static String trimToNull(String str)
	{
		// If the string passed in is null, return null
		if (str == null)
		{
			return null;
		}

		// we don't have a null string, trim the string
		String strTrimmed = str.trim();

		return (strTrimmed.equals("") ? null : strTrimmed);
	}

	/**
	 * Trim (remove spaces) from the passed in string. Null or and empty string will return empty string.
	 * 
	 * @param str A string, may be null or empty
	 * @return string - the trimmed string or empty string, will not be null
	 */
	public static String trimToEmpty(String str)
	{
		// If the string passed in is null, return the empty string
		if (str == null)
		{
			return "";
		}

		// we don't have a null string, trim the string
		return str.trim();
	}

	/**
	 * Remove all spaces from a string.
	 * 
	 * @param strInArg Input string (may be null)
	 * @return String Input string with spaces removed
	 */
	public static String removeAllSpaces(String strInArg)
	{
		String strIn = strInArg;
		if (strIn == null)
		{
			return null;
		}

		strIn = strIn.trim();
		int ndx = -1;
		while ((ndx = strIn.indexOf(' ')) != -1)
		{
			strIn = strIn.substring(0, ndx) + strIn.substring(ndx + 1);
		}

		return strIn;
	}

	/**
	 * Initial capitalization on the first letter of the passed in string and lowercase everthing else
	 * 
	 * @param strInput Input string (may be null)
	 * @return String Input string with the first letter capitalized
	 */
	public static String formatInitialCap(String strInput)
	{
		String strTempInput = strInput;
		if ((strTempInput != null) && (strTempInput.length() > 0))
		{
			strTempInput = strTempInput.substring(0, 1).toUpperCase() +
				strTempInput.substring(1, strTempInput.length()).toLowerCase();
		}
		return strTempInput;
	}

	/**
	 * Force the first letter of the passed in string to be lower case leaving the rest of the input unchanged.
	 * 
	 * @param strInput Input string (may be null)
	 * @return String Input string with the first letter lower cased
	 */
	public static String formatInitialLowerCase(String strInput)
	{
		String strTempInput = strInput;
		if ((strTempInput != null) && (strTempInput.length() > 0))
		{
			strTempInput = strTempInput.substring(0, 1).toLowerCase() + strTempInput.substring(1);
		}
		return strTempInput;
	}

	/**
	 * Returns a string formed from the specified array using the specified delimiter.
	 * 
	 * @param strDelim may not be null but may be empty string
	 * @param aT may be null or contain nulls, null entries will be skipped
	 */
	@SafeVarargs
	public static <T> String arrayToString(String strDelim, T... aT)
	{
		TestClassUtils.assertNotNullString(strDelim);

		if (aT == null)
		{
			return null;
		}

		StringBuilder buff = new StringBuilder();
		boolean bFirst = true;
		for (T el : aT)
		{
			if (el == null)
			{
				continue;
			}

			if (!bFirst)
			{
				buff.append(strDelim);
			}
			else
			{
				bFirst = false;
			}
			buff.append(el.toString());
		}

		return buff.toString();
	}

	/**
	 * Take an array of objects and print them in a list using the provided delimitter.
	 * 
	 * @param aT array of objects (any type, may be null)
	 * @param strDelim Delimitter
	 * @return Elements concatenated separated by the delimitter.
	 */
	public static String arrayToString(Object[] aN, String strDelim)
	{
		if (aN == null)
		{
			return null;
		}

		StringBuilder buff = new StringBuilder();
		boolean bFirst = true;
		for (Object n : aN)
		{
			if (!bFirst)
			{
				buff.append(strDelim);
			}
			else
			{
				bFirst = false;
			}
			buff.append(String.valueOf(n));
		}

		return buff.toString();
	}

	/**
	 * Take an array of objects and print them in a list using the provided delimitter.
	 * 
	 * @param aT array of objects (any type, may be null)
	 * @param strDelim Delimitter
	 * @return Elements concatenated separated by the delimitter.
	 */
	public static String arrayToString(int[] aN, String strDelim)
	{
		if (aN == null)
		{
			return null;
		}

		StringBuilder buff = new StringBuilder();
		boolean bFirst = true;
		for (int n : aN)
		{
			if (!bFirst)
			{
				buff.append(strDelim);
			}
			else
			{
				bFirst = false;
			}
			buff.append(String.valueOf(n));
		}

		return buff.toString();
	}

	/**
	 * Returns a string formed from the specified collection using the specified delimiter.
	 * 
	 * @param col may be null but may not contain nulls
	 * @param strDelim may not be null but may be empty string
	 */
	public static String arrayToString(Iterable<?> col, String strDelim)
	{
		TestClassUtils.assertNotNullString(strDelim);

		if (col == null)
		{
			return null;
		}

		StringBuilder strb = new StringBuilder();
		boolean bFirst = true;
		for (Object el : col)
		{
			if (!bFirst)
			{
				strb.append(strDelim);
			}
			else
			{
				bFirst = false;
			}
			strb.append(el.toString());
		}

		return strb.toString();
	}

	/**
	 * Return a special UI based null check on a string.
	 * 
	 * @param str String to check
	 * @return boolean true if an empty string or "null".
	 */
	public static boolean isNullUIString(String str)
	{
		return (isEmpty(str) || "null".equals(str));
	}

	/**
	 * Returns a Map from a string encoded as "key1=val1,key2=val2" The case of keys and values are not changed
	 */
	public static Map<String, String> stringToMap(String str)
	{
		return stringToMap(str, false);
	}

	/**
	 * Returns a Map from a string encoded as "key1=val1,key2=val2". Keys and values will be lower cased.
	 */
	public static Map<String, String> stringToLowerCaseMap(String str)
	{
		return stringToMap(str, true);
	}

	/**
	 * Returns a Map from a string encoded as "key1=val1,key2=val2"
	 */
	private static Map<String, String> stringToMap(String str, boolean bLowerCase)
	{
		Map<String, String> map = new HashMap<String, String>();

		StringTokenizer st = new StringTokenizer(str, ",");
		while (st.hasMoreTokens())
		{
			String strToken = st.nextToken();
			if (bLowerCase)
			{
				strToken = strToken.toLowerCase();
			}
			String[] astrToken = strToken.split("=");
			map.put(astrToken[0], astrToken.length > 1 ? astrToken[1] : null);
		}

		return map;
	}

	/**
	 * Returns a string encoded as "key1=val1,key2=val2"
	 */
	public static String mapToString(Map<String, String> map)
	{
		StringBuilder strb = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			if (strb.length() > 0)
			{
				strb.append(",");
			}

			strb.append(entry.getKey()).append("=").append(entry.getValue() == null ? "" : entry.getValue());
		}

		return strb.toString();
	}

	public static void replaceAll(StringBuilder buff, String strOld, String strNew)
	{
		int nPos = buff.indexOf(strOld);
		while (nPos > -1)
		{
			buff.replace(nPos, nPos + strOld.length(), strNew);
			nPos = buff.indexOf(strOld);
		}
	}

	/**
	 * Conform the input string (after trimming) to the specific length. A null string will returns null.
	 * 
	 * @param str Input string
	 * @param nLen Length constraint (must be > 0)
	 * @return trimmed string not greater than nLen
	 */
	public static String conformTrimmedLength(String str, int nLen)
	{
		if (nLen < 1)
		{
			throw new IllegalArgumentException(LENGTH_MUST_BE_GREATER_THAN_0);
		}
		if (str == null)
		{
			return null;
		}
		String strTrimmed = str.trim();
		if (strTrimmed.length() <= nLen)
		{
			return strTrimmed;
		}
		return strTrimmed.substring(0, nLen);
	}

	/**
	 * Conform the input string to the specific length. A null string will returns null.
	 * 
	 * @param str Input string
	 * @param nLen Length constraint (must be > 0)
	 * @return input string or first nLen characters if > nLen
	 */
	public static String conformLength(String str, int nLen)
	{
		if (nLen < 1)
		{
			throw new IllegalArgumentException(LENGTH_MUST_BE_GREATER_THAN_0);
		}
		if (str == null)
		{
			return null;
		}
		if (str.length() <= nLen)
		{
			return str;
		}
		return str.substring(0, nLen);
	}

	/**
	 * Enforce that the string conforms to the specific length. If it doesn't an IllegalArgumentException exception
	 * is thrown. A null string will returns null. No trimming is performed.
	 * 
	 * @param str Input string
	 * @param nLen Length constraint (must be > 0)
	 * @return The input string when not greater than nLen
	 */
	public static String enforceLength(String str, int nLen)
	{
		if (nLen < 1)
		{
			throw new IllegalArgumentException(LENGTH_MUST_BE_GREATER_THAN_0);
		}
		if (str == null)
		{
			return null;
		}

		if (str.length() <= nLen)
		{
			return str;
		}
		enformentException(str, nLen);
		return null; // We'll never reach this
	}

	private static void enformentException(String str, int nLen)
	{
		String strMsg = str;
		if (strMsg.length() > 100)
		{
			strMsg = strMsg.substring(0, 100) + "...";
		}
		throw new IllegalArgumentException(
			"String " + strMsg + " (length = " + str.length() + ") is longer than constraint (" + nLen + ")");

	}

	/**
	 * Enforce that the string (after trimming) conforms to the specific length. If it doesn't an
	 * IllegalArgumentException exception is thrown. A null string will returns null.
	 * 
	 * @param str Input string
	 * @param nLen Length constraint (must be > 0)
	 * @return trimmed string not greater than nLen
	 */
	public static String enforceTrimmedLength(String str, int nLen)
	{
		if (nLen < 1)
		{
			throw new IllegalArgumentException(LENGTH_MUST_BE_GREATER_THAN_0);
		}
		if (str == null)
		{
			return null;
		}
		String strTrimmed = str.trim();
		if (strTrimmed.length() <= nLen)
		{
			return strTrimmed;
		}
		enformentException(strTrimmed, nLen);
		return null; // We'll never reach this
	}

	/**
	 * Returns null if str is null, empty, just whitespace, or not a valid float, otherwise returns the Float
	 * value. Will not throw an exception.
	 */
	public static Float parseFloat(String str)
	{
		if (str == null)
		{
			return null;
		}

		try
		{
			return Float.valueOf(str);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	/**
	 * Convert a list of values into a comma separated string.
	 * 
	 * @param lstVals object where toString is meaningful
	 */
	public static <T> String toCommaSeparatedString(List<T> lstVals)
	{
		StringBuilder buff = new StringBuilder();
		boolean bFirst = true;
		for (T val : lstVals)
		{
			if (!bFirst)
			{
				buff.append(",");
			}
			buff.append(val.toString());
			bFirst = false;
		}

		return buff.toString();
	}

	/**
	 * Convert a list of key into the mapped values as a comma separated string.
	 */
	@SafeVarargs
	public static <T, V> String toCommaSeparatedString(Map<T, V> mapValues, T... lstKeys)
	{
		StringBuilder buff = new StringBuilder();
		boolean bFirst = true;
		for (T key : lstKeys)
		{
			if (!bFirst)
			{
				buff.append(",");
			}
			buff.append(mapValues.get(key).toString());
			bFirst = false;
		}

		return buff.toString();
	}

	/**
	 * Returns null if str is null, empty, just whitespace, or not a valid double, otherwise returns the Double
	 * value. Will not throw an exception.
	 */
	public static Double parseDouble(String str)
	{
		if (str == null)
		{
			return null;
		}

		try
		{
			return Double.valueOf(str);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	/**
	 * Returns null if str is null, empty, just whitespace, or not a valid Integer, otherwise returns the Integer
	 * value. Will not throw an exception.
	 */
	public static Integer parseInt(String str)
	{
		if (str == null)
		{
			return null;
		}

		try
		{
			return Integer.valueOf(str);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	/**
	 * Break a string buffer into a list of lines.
	 */
	public static List<String> parseLines(StringBuilder buff)
	{
		List<String> lstLines = new ArrayList<String>();
		int nPos = 0;
		while (nPos >= 0)
		{
			int nNewPos = buff.indexOf("\r\n", nPos);
			if (nNewPos < 0)
			{
				lstLines.add(buff.substring(nPos));
				break;
			}
			lstLines.add(buff.substring(nPos, nNewPos));

			nPos = nNewPos + 2;
		}

		return lstLines;
	}

	/**
	 * Returns the number of args that are not empty
	 */
	public static int countNonEmpty(String... astrArgs)
	{
		int nCount = 0;
		for (String str : astrArgs)
		{
			if (isNotEmpty(str))
			{
				nCount++;
			}
		}

		return nCount;
	}

	/**
	 * Remove suffix from String. Ex. TestStringUtils.removeSuffix("\\.0*$");
	 * 
	 * @param strToBeRemoved
	 * @return
	 */
	public static String convertDecimalToWholeNumber(String strToBeRemoved)
	{
		return strToBeRemoved.replaceAll("\\.0*$", "");
	}

	/**
	 * String to Array (default delimiter: \)
	 */
	public static String[] stringToArray(String strValues)
	{
		return strValues.split("\\|");
	}

	/**
	 * String to Array as per specified delimiter otherwise default delimiter "\"
	 */
	public static String[] stringToArray(String strValues, String strdelimiter)
	{
		if (strdelimiter != null && !strdelimiter.equals(""))
		{
			return strValues.split(strdelimiter);
		}
		return stringToArray(strValues);
	}
}
