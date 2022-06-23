package com.qualtech.keywordframework.core;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * This class is used to generate the Random Numbers, Strings, AlphaNumeric values for the Test scenarios
 * @author Suresh Yadav
 *
 */
public class RandomUtil {
	//private static final Logger logger = Logger.getLogger(RandomUtil.class);

	/**
	 * This method is used to get the Random String value limited to the length provided, like we need 8 character long random string.
	 * @param length This signifies that how many characters random string is required.
	 * @return It will return the String value.
	 */
	public  String generateRandomString(String length) {
		int randomAlphabeticNum=Integer.parseInt(length);
		StringBuilder str = new StringBuilder(RandomStringUtils.randomAlphabetic(randomAlphabeticNum));
		int idx = str.length()-8;

		while (idx > 0) {
			str.insert(idx, "");
			idx = idx-8;
		}
		
		return str.toString();
	}
	
	/**
	 * This method is used to get the Random Numeric value limited to the length provided, like we need 8 digit random number.
	 * @param length This signifies that how many digit number is required
	 * @return It will return the String value
	 */
	public  String generateRandomNumber(String length) {
		int randomNumber=Integer.parseInt(length);
		StringBuilder str = new StringBuilder(RandomStringUtils.randomNumeric(randomNumber));
		int idx = str.length()-8;
		while (idx > 1) {
			str.insert(idx, "");
			idx = idx-8;
		}
		return str.toString();
	}
	
	/**
	 * This method is used to generate the Random number in the specific range, like I need the random number between 100 to 1000.
	 * @param min This signifies the starting value for the range.
	 * @param max This signifies the Ending value for the range.
	 * @return it will return the Integer value.
	 */
	public  int generateRandomNumber(int min,int max){ 
		int randomNo=0;
		if(max==min){
			randomNo=min;
		}else if(max<min){
			//logger.debug("Invalid Range for Random Numbers, Max should be greater than min");
		}
		else{
		Random rn = new Random();		
		randomNo=rn.nextInt(max) + min;
		}
		
		return randomNo;
	}
	
	/**
	 * This method is used to get the Random Alpha-Numeric value limited to the length provided, like we need 8 character long random value.
	 * @param length This signifies that how many character alpha-numeric value is required.
	 * @return It will return the String value
	 */
	public String generateRandomAlphaNumeric(String length) {
		
		int randomAlphaNumeric=Integer.parseInt(length);
		StringBuilder str = new StringBuilder(
				RandomStringUtils.randomAlphabetic(5));
		RandomStringUtils.randomAlphanumeric(randomAlphaNumeric);
		int idx = str.length()-8;

		while (idx > 0) {
			str.insert(idx, " ");
			idx = idx-8;
		}
		
		return str.toString();
	}
	


public String generateRandomAlphaNumericWithSpecialChar(String length) {
	
	int randomAlphaNumeric=Integer.parseInt(length);
	StringBuilder str = new StringBuilder(
			RandomStringUtils.randomAlphabetic(randomAlphaNumeric));
	//RandomStringUtils.;
	int idx = str.length()-8;

	while (idx > 0) {
		str.insert(idx, "");
		idx = idx-8;
	}
	System.out.println(str.toString());
	return str.toString();
}

private static final String symbols = 
"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz$&@?<>~!%#";

public static String genPassword(Random r) {
while(true) {
   char[] password = new char[r.nextBoolean()?12:13];
   boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
   for(int i=0; i<password.length; i++) {
       char ch = symbols.charAt(r.nextInt(symbols.length()));
       if(Character.isUpperCase(ch))
           hasUpper = true;
       else if(Character.isLowerCase(ch))
           hasLower = true;
       else if(Character.isDigit(ch))
           hasDigit = true;
       else
           hasSpecial = true;
       password[i] = ch;
   }
   if(hasUpper && hasLower && hasDigit && hasSpecial) {
       return new String(password);
   }
}
}

}


