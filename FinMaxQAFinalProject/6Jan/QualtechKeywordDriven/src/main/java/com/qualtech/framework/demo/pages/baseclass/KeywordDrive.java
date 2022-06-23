package com.qualtech.framework.demo.pages.baseclass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qualtech.framework.demo.genricmethod.login.BaseMethod;

public class KeywordDrive {

	public static WebDriver driver;
	public Properties proop;
	public static Workbook workbookobj;
	public static Sheet sheetobj;
	public BaseMethod baseobj;
	public static WebElement element;
	String prospectId="";
	public final String SCENARIO_SHEET_PATH=System.getProperty("user.dir")+"\\TestData\\keywordTestData.xlsx";

	public void startExcution(String LocatorSheetname,String validationSheetName) {

		FileInputStream file=null;
		baseobj=new BaseMethod();
		try {
			file=new FileInputStream(SCENARIO_SHEET_PATH);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		try {
			workbookobj=WorkbookFactory.create(file);
		}catch(InvalidFormatException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		sheetobj=workbookobj.getSheet(LocatorSheetname);
		int k=0;

		BaseMethod base=new BaseMethod();
		int rowNumberBeforeFor=base.getRowNumberByRowID(sheetobj, "Action on element", "for");
		int rowNumberNext=base.getRowNumberByRowID(sheetobj, "Action on element", "next");

		for(int i=0;i<=rowNumberBeforeFor-1;i++) {
			keywordSelection(i, k, null);
		}

		List<Map<String, String>>mapDataList=base.getTestCaseData("TestData/validation.xlsx", validationSheetName);
		for(int j=0; j<=mapDataList.size()-1;j++) {
			Map<String, String>dataMap=mapDataList.get(j);
			for(int i=rowNumberBeforeFor+1;i<=rowNumberNext-1;i++) {
				keywordSelection(i, k, dataMap);
			}
		}

	}



	public WebElement setTextBox(String strlocatorName) {
		element=driver.findElement(By.name(strlocatorName));
		return element;
	}

	/*public WebElement clickonButton(By by,String strlocatorName) {

		if(by.getClass().equals("xpath"))
		element=driver.findElement(By.name(strlocatorName));
		return element;
	}*/


	public void keywordSelection(int i, int k, Map<String, String>dataMap) {
		String locatorName=null;
		String locatorValue=null;
		try {
			String locatorcolvalue=sheetobj.getRow(i+1).getCell(k+1).toString().trim();
			if(!locatorcolvalue.equalsIgnoreCase("NA")) {
				locatorName =locatorcolvalue.split("#")[0].trim();
				locatorValue =locatorcolvalue.split("#")[1].trim();
			}
			String actionobj=sheetobj.getRow(i+1).getCell(k+2).toString().trim();

			String value=sheetobj.getRow(i+1).getCell(k+3).toString().trim();
			if(!value.trim().equalsIgnoreCase("NA") && !value.trim().equalsIgnoreCase("")) {
				if(dataMap==null) {

					if(value.startsWith("raw_")) {
						value=value.replaceAll("raw_", "");	
					}
				}else {
					value=sheetobj.getRow(i+1).getCell(k+3).toString().trim();
					if(value.startsWith("raw_")) {
						value=value.replaceAll("raw_", "");	
					}else {
						value=dataMap.get(value);
					}
				}
			}
		


			switch(actionobj) {
			case "open browser":
				//

				baseobj.readproPerties();
				if(value.isEmpty() || value.equals("NA")) {
					driver=baseobj.LaunchBrowse(proop.getProperty("browserName"));
                    
				}else {
					driver=baseobj.LaunchBrowse(value);

				}
				break;
			case "enter url":
				if(value.isEmpty() || value.equals("NA")) {
					driver.get(proop.getProperty("url"));

				}else {
					driver.get(value);

				}
				break;
			case"quit":
				driver.quit();
				break;
				////////////////////////////////////////////xpath Locatar////////////////////////	 
			case"sendkeys":

				baseobj.sendkeys(locatorName, locatorValue,value);
				//driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);

				break;

			case"click":
				baseobj.click(locatorName, locatorValue);
				break;

			case"linkText":
				element=driver.findElement(By.linkText(locatorValue));
				element.click();
				locatorName=null;
				break;

			case"handleWindowByTitle":
				baseobj.switchWindowFocusByTitle(value);
				break;

			case"selectDropDwnValueByVisibleText":
				baseobj.selectByVisibleText(locatorName, locatorValue,value);
				break;

				
			case"selectDropDwnValueByIndex":
				baseobj.selectByIndex(locatorName, locatorValue,value);
				break;

			case"sendkeysForPan":
				String panValue = baseobj.getRandomPANNo();
				baseobj.sendkeys(locatorName, locatorValue,panValue);
				break;
			case"sendkeysForAadhar":
				String aadharValue = baseobj.getRandomAadharNo();
				baseobj.sendkeys(locatorName, locatorValue,aadharValue);
				break;


			case"sleep":
				Thread.sleep(2000);
				break;

			case"acceptAlert":
				driver.switchTo().alert().accept();
				break;

				
			case"saveProspectId":
				String text =driver.switchTo().alert().getText();
				String[] code=text.split("CODE");
				prospectId=code[1];
				System.out.println(code[1]);
				break;	
				
			case"searchProspectId":
				baseobj.sendkeys(locatorName, locatorValue,prospectId);
				break;	
				
				
				
			}
		}catch(Exception e) {

		}

	}

}
