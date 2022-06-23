package com.qualtech.keywordframework.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.qualtech.framework.demo.genricmethod.login.BaseMethod;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.common.io.Files;

public class DataManager {
	public static String testCaseTestCaseColumnName="TestCaseName";
	public static String pageFunctionColumnName="PageName";
	public static String driverSheetPath="..\\QualtechKeywordDriven\\TestData\\DriverSheet.xlsx";
	public static String testCaseSheetPath=driverSheetPath;
	public static String driverSheetWorkSheetName="testCaseDriver";
	public static String driverSheetModuleColumnName="ScriptSheetName";
	public static String driverSheetTestCaseColumnName="TestCaseName";
	public static String testCaseSheetKeyWordColumnName="Keyword";
	public static String testCaseSheetKeyWordSeperater="##";
	public static String pageSheetFilePath="..\\QualtechKeywordDriven\\TestData\\PageWiseSheet.xlsx";
	public static String pageSheetFunctionColumnName="CreateCustomer";
	public static String testCaseSheetTestDataColumnName="TestData";
	public static String testDataSheetPath="..\\QualtechKeywordDriven\\TestData\\TestData.xlsx";
	public static String rowIdColumnName="RowID";
	public static boolean DataPassedStatus=true;
	public static  BaseMethod baseobj=new BaseMethod();
	public static  RandomUtil RandomObj=new RandomUtil();
	public  static  ExtentTest test;
	String reportPath = new File("").getAbsolutePath().toString().trim()+"/Reports/";
	public static String prospectId="";
	public static String customerId="";
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;	
	public static ExtentTest extentLogger;
	public static SoftAssert SoftAssertObj= new SoftAssert();
	public static HashMap<String, String> KeywordDataMap;
	public String driverSheetTestCaseName=null;
	public String panValue="";
	public String groupCode="";
	public String groupLimitId="";
	public String custLimitId="";
	public static String UploadId="";
	public String bsnsDate="";
	public static void configureExtentReport() {
		File file=new File("ExtentResult");
		if(!file.exists()) {
			file.mkdir();

		}
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/ExtentResult/QualtechAutomationReport.html");
		// Create an object of Extent Reports	
		extent = new ExtentReports(); 		
		extent.attachReporter(htmlReporter);		
		extent.setSystemInfo("Host Name", "QualTech");		
		extent.setSystemInfo("Environment", "QA");		
		extent.setSystemInfo("User Name", System.getProperty("user.name"));		
		htmlReporter.config().setDocumentTitle("Mifin Test Result"); 		
		// Name of the report		
		htmlReporter.config().setReportName("Mifin Test Automation Report"); 		// Dark Theme	
		htmlReporter.config().setTheme(Theme.STANDARD);	
	}
	public static void main(String[] args) {

		DataManager dm=new DataManager();
		configureExtentReport();
		List<Map<String, String>> driverSheetMapList=dm.getDriverSheetData(driverSheetPath, driverSheetWorkSheetName);
		for(int i=0; i<=driverSheetMapList.size()-1; i++) {
			KeywordDataMap=new HashMap<>();
			Map<String, String> driverSheetMap=driverSheetMapList.get(i);
			
			String driverSheetModuleName=driverSheetMap.get(driverSheetModuleColumnName);
			String driverSheetTestCaseName=driverSheetMap.get(driverSheetTestCaseColumnName);

			try {


				extentLogger=extent.createTest(driverSheetModuleName+"-"+driverSheetTestCaseName);
				List<Map<String, String>> testCaseStepMapsList=dm.getTestCaseSteps(testCaseSheetPath, driverSheetModuleName, driverSheetTestCaseName);
				for(int j=0; j<=testCaseStepMapsList.size()-1; j++) {
					Map<String, String> testCaseStepMap=testCaseStepMapsList.get(j);
					String testCaseSheetKeyWordName=testCaseStepMap.get(testCaseSheetKeyWordColumnName);
					if(testCaseSheetKeyWordName.contains(testCaseSheetKeyWordSeperater)) {
						extentLogger.log(Status.INFO,  "<b>"+testCaseSheetKeyWordName+" page functionality execution started</b>");
						String[] arrtestCaseSheetPageFunction=testCaseSheetKeyWordName.split("##");
						String testCaseSheetPageName=arrtestCaseSheetPageFunction[0];
						String testCaseSheetFunctionName=arrtestCaseSheetPageFunction[1];
						Workbook pageSheetWorkbookObject=dm.getWorkbook(pageSheetFilePath);		

						List<Map<String, String>> pageSheetFunctionStepMapsList=dm.getPageSteps(pageSheetWorkbookObject, testCaseSheetPageName, testCaseSheetFunctionName);
						String testDataDetail=testCaseStepMap.get(testCaseSheetTestDataColumnName).trim();

						///non blank test data column value of test case sheet 
						if(!testDataDetail.equalsIgnoreCase("")) {
							String[] testDataArrayDetail=testDataDetail.split(testCaseSheetKeyWordSeperater);
							String testDataSheetName=testDataArrayDetail[0];
							String testDataRowValue=testDataArrayDetail[1];


							//////execute with all data in one test data sheet 
							if(testDataRowValue.trim().equalsIgnoreCase("All")) {
								List<Map<String, String>> testDataMapList=dm.getTestCaseData(testDataSheetPath, testDataSheetName);
								for(int k=0; k<=testDataMapList.size()-1;k++) {
									DataPassedStatus=true;
									Map<String, String> testDataMap=testDataMapList.get(k);
									for(int l=0; l<=pageSheetFunctionStepMapsList.size()-1;l++) {
										Map<String, String> testStepMap=pageSheetFunctionStepMapsList.get(l);
										dm.executeKeyword(testStepMap, testDataMap);
									}
									int columnNumber=dm.getColumnNumberByColumnName(dm.getWorkbook(testDataSheetPath).getSheet(testDataSheetName), "DataExecutionResult");
									if(DataPassedStatus) {
										dm.writeStaus(testDataSheetName, k+1, columnNumber, "PASSED");
									}else {
										dm.writeStaus(testDataSheetName, k+1, columnNumber, "FAILED");
									}

								}
							}else {
								Map<String, String> testDataMap=dm.getTestCaseDataByRowId(testDataSheetPath, testDataSheetName, testDataRowValue);
								for(int l=0; l<=pageSheetFunctionStepMapsList.size()-1;l++) {
									Map<String, String> testStepMap=pageSheetFunctionStepMapsList.get(l);
									dm.executeKeyword(testStepMap, testDataMap);
								}
							}
							extentLogger.log(Status.INFO,  "<b>"+testCaseSheetKeyWordName+" page functionality execution completed</b>");
						}else {
							for(int l=0; l<=pageSheetFunctionStepMapsList.size()-1;l++) {
								Map<String, String> testStepMap=pageSheetFunctionStepMapsList.get(l);
								dm.executeKeyword(testStepMap);
							}
						}
					}else {
						dm.executeKeyword(testCaseStepMap);
					}
				}
				extentLogger.log(Status.PASS, MarkupHelper.createLabel(driverSheetTestCaseName+" Test Case PASSED", ExtentColor.GREEN));
			}catch(Throwable e) {
				e.printStackTrace();
				extentLogger.log(Status.FAIL, MarkupHelper.createLabel(driverSheetTestCaseName + " - Test Case Failed", ExtentColor.RED));		
				extentLogger.log(Status.FAIL, MarkupHelper.createLabel(e.getMessage() + " - Test Case Failed", ExtentColor.RED));		

				String screenshotPath =baseobj.takeScreenShot(driverSheetTestCaseName);			
				try {
					extentLogger.fail("Test Case Failed Snapshot is below " + extentLogger.addScreenCaptureFromPath(screenshotPath));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			

			}
		}
		extent.flush();
	}
	
	
	
	public void writeStaus(String SheetName, int rownum,int cellnum,String DataExecutionResult ) throws IOException {
		FileInputStream fis = new FileInputStream("..\\QualtechKeywordDriven\\TestData\\TestData.xlsx");
		 XSSFWorkbook workbook = new XSSFWorkbook(fis);
		 XSSFSheet sheet = workbook.getSheet(SheetName);
		  Row row = sheet.getRow(rownum);
		 Cell cell = row.createCell(cellnum);
		 cell.setCellValue(DataExecutionResult);
		 FileOutputStream fos = new FileOutputStream("..\\QualtechKeywordDriven\\TestData\\TestData.xlsx");
		 workbook.write(fos);
		 fos.close();
	
		 }
	public List<Map<String, String>> getDriverSheetData(String dataSheetPath, String sheetName) {

		Workbook wbook=getWorkbook(dataSheetPath);
		Sheet sheet=getSheet(wbook , sheetName);
		List<Map<String, String>> driverSheetMapList=new ArrayList<>();
		List<String> listColumns=getColumnsList(sheet);
		int columnNumber=getColumnNumberByColumnName(sheet, "Execute");
		int driverSheetTestCaseCount=getRowCount(sheet);
		boolean executionFlag=false;
		for(int i=1; i<=driverSheetTestCaseCount;i++) {
			
			Row row=sheet.getRow(i);
			if(row==null) {
				continue;
			}
			Map<String, String> driverSheetMap=new HashMap<>(); 
			String executeCondition=getCellData(row, columnNumber).trim();
			for(int j=0; j<=listColumns.size()-1;j++) {				
				
				if(executeCondition.equalsIgnoreCase("Y")) {
					String driverSheetDataValue=getCellData(row, j);
					driverSheetMap.put(listColumns.get(j), driverSheetDataValue);
					executionFlag=true;
				}else {
					executionFlag=false;
					break;
				}

			}
			if(executionFlag==true) {
				driverSheetMapList.add(driverSheetMap);
			}

		}
		return driverSheetMapList;

	}


	public List<Map<String, String>> getTestCaseSteps(String testCaseWorkbookPath, String moduleOrTestCaseSheetName, String rowIdTestCaseName) {

		Sheet sheet=getSheet(getWorkbook(testCaseWorkbookPath) , moduleOrTestCaseSheetName);
		List<Map<String, String>> testCaseStepsMapsList=new ArrayList<>();
		List<String> listColumns=getColumnsList(sheet);
		int[] arrStartEndRowNumber=getRowNumbersRangeByRowID(sheet, testCaseTestCaseColumnName, rowIdTestCaseName);
		int startRowNumber=arrStartEndRowNumber[0];
		int endRowNumber=arrStartEndRowNumber[1];
		for(int i=startRowNumber; i<=endRowNumber;i++) {
			Map<String, String> mapTestcaseStep=new HashMap<>(); 
			Row row=sheet.getRow(i);
			for(int j=0; j<=listColumns.size()-1;j++) {
				String testcaseStepsDataValue=getCellData(row, j);
				mapTestcaseStep.put(listColumns.get(j), testcaseStepsDataValue);
			}
			testCaseStepsMapsList.add(mapTestcaseStep);
		}
		return testCaseStepsMapsList;

	}



	public List<Map<String, String>> getPageSteps(Workbook wbook, String pageStepsSheetName, String rowIdPageFunctionName) {

		Sheet sheet=getSheet(wbook , pageStepsSheetName);
		List<Map<String, String>> mapPageStepsMapsList=new ArrayList<>();
		List<String> listColumns=getColumnsList(sheet);
		int[] arrStartEndRowNumber=getRowNumbersRangeByRowID(sheet, pageFunctionColumnName, rowIdPageFunctionName);
		int startRowNumber=arrStartEndRowNumber[0];
		int endRowNumber=arrStartEndRowNumber[1];
		for(int i=startRowNumber; i<=endRowNumber;i++) {
			Map<String, String> mapPageStep=new HashMap<>(); 
			Row row=sheet.getRow(i);
			if(row==null) {
				break;
			}
			for(int j=0; j<=listColumns.size()-1;j++) {
				String mapPageStepDataValue=getCellData(row, j);
				mapPageStep.put(listColumns.get(j), mapPageStepDataValue);
			}
			mapPageStepsMapsList.add(mapPageStep);
		}
		return mapPageStepsMapsList;

	}


	public int getRowCount(Sheet sheet) {
		return sheet.getLastRowNum();
	}

	public Workbook getWorkbook(String wbookPath) {
		Workbook wbook=null;
		try {
			wbook = new XSSFWorkbook(wbookPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wbook;
	}

	public Sheet getSheet(Workbook wbook, String sheetName) {

		Sheet sheet=wbook.getSheet(sheetName);
		return sheet;
	}

	public List<String> getColumnsList(Sheet sheet) {
		List<String> columnList=new ArrayList<>();		
		Row row= sheet.getRow(0);
		for(int i=0; i<row.getLastCellNum(); i++){
			String columnName=getCellData(row, i);
			if(!columnName.trim().equalsIgnoreCase("")) {
				columnList.add(columnName);
			}
			
		}
		return columnList;
	}


	public String getCellData(Row row, int cellNumber) {
		MissingCellPolicy MCP=Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;
		Cell cell=row.getCell(cellNumber, MCP);
		if(cell.getCellType()==CellType.NUMERIC) {
			Double dblValue=cell.getNumericCellValue();
			Long longValue=dblValue.longValue();
			return longValue.toString();
		}else {
			return cell.getStringCellValue();
		}

	}

	public  List<Map<String, String>> getTestCaseData(String dataSheetPath, String sheetName) {
		Workbook wbook=getWorkbook(dataSheetPath);
		Sheet sheet=getSheet(wbook , sheetName);
		FormulaEvaluator formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) wbook);
		MissingCellPolicy MCP=Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;
		List<Map<String, String>> dataMapsList=new ArrayList<>();
		for(int j=1; j<=sheet.getLastRowNum();j++) {
			Map<String, String> dataMap=new HashMap<String, String>();
			Row columnRow=sheet.getRow(0);
			Row dataRow=sheet.getRow(j);
			for(int i=0; i<columnRow.getLastCellNum(); i++){
				Cell cell=columnRow.getCell(i, MCP);
				String dataKeyName=cell.getStringCellValue();
				if(dataKeyName!=null && dataKeyName.trim().equals("")==false){
					DataFormatter df = new DataFormatter();
					Cell curCell = dataRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					switch (curCell.getCellTypeEnum()) {
					case NUMERIC:
						dataMap.put(dataKeyName, df.formatCellValue(curCell));
						break;
					case FORMULA:
						String keyValue = df.formatCellValue(curCell, formulaEvaluator);
						dataMap.put(dataKeyName, df.formatCellValue(curCell, formulaEvaluator));
						break;
					case STRING:
					default:
						dataMap.put(dataKeyName, curCell.getStringCellValue());
						break;
					}
				}
			}
			dataMapsList.add(dataMap);
		}
		return dataMapsList;
	}


	public  Map<String, String> getTestCaseDataByRowId(String dataSheetPath, String sheetName, String rowId) {
		Workbook wbook=getWorkbook(dataSheetPath);
		Sheet sheet=getSheet(wbook , sheetName);
		DataManager dm= new DataManager();
		List<String> listColumns=dm.getColumnsList(sheet);
		int rowNumber=dm.getRowNumberByRowID(sheet, rowIdColumnName, rowId);
		Row dataRow=sheet.getRow(rowNumber);
		Map<String, String> testDataMap=new HashMap<String, String>();
		for(int i=0; i<=listColumns.size()-1; i++){
			String testDataColumnName=listColumns.get(i);
			String testDataValue=getCellData(dataRow, i);
			testDataMap.put(testDataColumnName, testDataValue);

		}

		return testDataMap;
	}


	public  int getColumnNumberByColumnName(Sheet sheet, String columnName) {
		Row firstRowColumns=sheet.getRow(0);
		int columnNumber=-1;
		int columnCount=firstRowColumns.getLastCellNum();
		for(int i=0; i<=columnCount-1; i++){
			if(firstRowColumns.getCell(i).getStringCellValue().toLowerCase().trim().contains(columnName.toLowerCase().trim())){
				columnNumber=i;
				break;
			}
		}
		return columnNumber;
	}

	public  int getRowNumberByRowID(Sheet sheet, String columnName,String rowID){

		int rowCount=sheet.getLastRowNum();
		int columnNumber=getColumnNumberByColumnName(sheet, columnName);
		int rowNumber=-1;
		for(int i=1; i<=rowCount; i++){
			Cell cell=sheet.getRow(i).getCell(columnNumber);
			if(cell !=null && cell.getStringCellValue().trim().equalsIgnoreCase(rowID.trim())){
				rowNumber=i;
				break;
			}
		}
		return rowNumber;
	}

	public  int[] getRowNumbersRangeByRowID(Sheet sheet, String columnName,String rowID){

		int rowCount=sheet.getLastRowNum();
		int columnNumber=getColumnNumberByColumnName(sheet, columnName);
		int endRowNumber=-1;
		boolean startFlag=false;
		int startRowNumber=-1;
		for(int i=1; i<=rowCount; i++){
			Row row=sheet.getRow(i);
			if(row==null) {
				if(startFlag==false) {
					continue;
				}else {
					break;
				}

			}
			String testCaseName=getCellData(row, columnNumber);			
			if(testCaseName !=null && testCaseName.trim().equalsIgnoreCase(rowID.trim())){
				if(startFlag==false) {
					startFlag=true;
					startRowNumber=i;
				}
				endRowNumber=i;

			}else {
				if(startFlag==true) {
					break;
				}
			}
		}
		int[] arrRowNumbers= {startRowNumber, endRowNumber};
		return arrRowNumbers;
	}
	
	public void executeKeyword(Map<String, String> testStepMap, Map<String, String> testDataMap) {
		String keywordName=testStepMap.get("Keyword").trim();
		String locatorName=testStepMap.get("LocatorType").trim();
		String locatorValue=testStepMap.get("LocatorValue").trim();
		String testData=testStepMap.get("TestData").trim();
		if(!testData.equalsIgnoreCase("NA") && !testData.equalsIgnoreCase("")) {
			if(testData.startsWith("Var_")) {

			}else if(testData.startsWith("raw_")) {
				testData=testData.replace("raw_", "");
			}else {
				testData=testDataMap.get(testData);
			}
		}
		keywordSelection( keywordName,  locatorName,  locatorValue,  testData);


	}

	public void executeKeyword(Map<String, String> testStepMap) {
		String keywordName=testStepMap.get("Keyword").trim();
		String locatorName=testStepMap.get("LocatorType").trim();
		String locatorValue=testStepMap.get("LocatorValue").trim();
		String testData=testStepMap.get("TestData").trim();
		keywordSelection( keywordName,  locatorName,  locatorValue,  testData);


	}
	public void keywordSelection(String keywordName, String locatorName, String locatorValue, String testData) {
		switch(keywordName) {
		case "openBrowser":
			baseobj.LaunchBrowse(testData);
			extentLogger.log(Status.INFO, testData+" browser is launched sucessfully");
			break;
			
			
		case "debugger":
			System.out.println("dff");
			break;
			
		case "enterUrl":
			baseobj.openUrl(testData);
			extentLogger.log(Status.INFO, testData+" url is opened");
			break;
			
		case"closeBrowser":
			baseobj.quitDriver();
			extentLogger.log(Status.INFO, "all browser sessions are closed");
			break;
			////////////////////////////////////////////xpath Locatar////////////////////////	 
		
		case"GenerateAlphabet":
			String length=RandomObj.generateRandomString(testData);
			baseobj.sendkeys(locatorName, locatorValue,length);
			extentLogger.log(Status.INFO, testData+" is entered in "+locatorValue);
			break;
			
		case"generateRandomNumber":
			String RandomNumber=RandomObj.generateRandomNumber(testData);
			baseobj.sendkeys(locatorName, locatorValue,RandomNumber);
			extentLogger.log(Status.INFO, testData+" is entered in "+locatorValue);
			break;

		case"generateRandomAlphaNumeric":
			String RandomAlphaNumeric=RandomObj.generateRandomAlphaNumeric(testData);
			baseobj.sendkeys(locatorName, locatorValue,RandomAlphaNumeric);
			extentLogger.log(Status.INFO, testData+" is entered in "+locatorValue);
			break;
			
		case"TextBox":
			baseobj.sendkeys(locatorName, locatorValue,testData);
			extentLogger.log(Status.INFO, testData+" is entered in "+locatorValue);
			break;
			
		case"Button":
			baseobj.click(locatorName, locatorValue);
			break;
			
		case"Link":
			baseobj.click(locatorName, locatorValue);
			break;
			
		case"CheckBox":
			baseobj.click(locatorName, locatorValue);
			break;
			
		case"DropDownValue":
			baseobj.selectByVisibleText(locatorName, locatorValue,testData);
			break;
			
		case"DropDownIndex":
			baseobj.selectByIndex(locatorName, locatorValue,testData);
			break;
			
		case"handleWindowByTitle":
			baseobj.switchWindowFocusByTitle(testData);
			break;
			
		case"SwitchToNewOpenWindow":
			baseobj.switchToNewopenWindow();
			break;
	
		case"GetGroupCode":
			groupCode = baseobj.getText(locatorName, locatorValue);
			System.out.println(groupCode);
			break;
			
		case"SetGroupCode":
			baseobj.sendkeys(locatorName, locatorValue,groupCode);
			break;
			
		case"GetGroupLimitId":
			groupLimitId=baseobj.getGroupLimitCode();
			System.out.println(groupLimitId);
			break;
			
		case"Date":
			//String[] SplitDate = testData.split("�");
			bsnsDate=baseobj.enterCurrenBsnsDate();
			baseobj.sendkeys(locatorName, locatorValue,bsnsDate);
			extentLogger.log(Status.INFO, testData+" is entered in "+locatorValue);
			break;
			
		case"SetGroupLimitId":
			baseobj.sendkeys(locatorName, locatorValue,groupLimitId);
			break;
			
		case"GetCustomerLimitId":
			custLimitId=baseobj.getGroupLimitCode();
			System.out.println("Customer limit id: "+custLimitId);
			break;
			
		case"SetCustomerLimitId":
			baseobj.sendkeys(locatorName, locatorValue,custLimitId);
			break;
			
		case"miFinEnterValueInPan":
			panValue = baseobj.getRandomPANNo();
			baseobj.sendkeys(locatorName, locatorValue,panValue);
			System.out.println("PAN NO: "+panValue);
			break;
			
		case"sendkeysForAadhar":
			String aadharValue = baseobj.getRandomAadharNo();
			baseobj.sendkeys(locatorName, locatorValue,aadharValue);
			break;
			
		case"setRequestedLimitAmountTxtBx":
			try {
				baseobj.setRequestedLimitAmountTxtBx(locatorName, locatorValue, testData);
			} catch (InterruptedException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			break;
			
        case"enterCurrentBusinessDate":
			bsnsDate=baseobj.enterCurrenBsnsDate();
			baseobj.sendkeys(locatorName, locatorValue,bsnsDate);
			break;

		case"miFinUploadId":
			String uploadtext =baseobj.getAllertText();
			String[] splitText=uploadtext.split(":");
			UploadId=splitText[1].trim();
			System.out.println(splitText[1]);
			break;	
			
		case"GenerateGroupNameAuto":
			String groupName = baseobj.getRandomGroupName(testData);
			baseobj.sendkeys(locatorName, locatorValue,groupName);
			break;
			
		case"SetPanNoInGroup":
			baseobj.sendkeys(locatorName, locatorValue,panValue);
			break;
		case"sleep":
			try {
           
				Thread.sleep(Integer.parseInt(testData));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case"acceptAlert":
			baseobj.acceptAlert();
			extentLogger.log(Status.INFO, "Alert accepted and closed");
			break;

		case"saveCustomerId":
			String text =baseobj.getAllertText();
			String[] code=text.split("CODE");
			customerId=code[1];
			System.out.println(code[1]);
			break;	

		case"miFinSaveProspectId":
			String losText =baseobj.getAllertText();
			String[] Prospect=losText.split("#");
			prospectId=Prospect[1].trim();
			System.out.println(Prospect[1]);
			break;	

		case"searchCustomerId":
			baseobj.sendkeys(locatorName, locatorValue,customerId);
			break;	

		case"switchToDefaultWindow":
			baseobj.defaultWindow();
			break;	

		case"miFinFormNumber":
			String RandomFormNo=baseobj.getRandomFormNo();
			baseobj.sendkeys(locatorName, locatorValue,RandomFormNo);
			break;	

		case"getElementText":
			String elementText=baseobj.getElementText(locatorName, locatorValue);
			KeywordDataMap.put(testData, elementText);
			break;	
			
		case"getDateFormet":
			String bsnsDate=baseobj.enterCurrenBsnsDate();
			baseobj.sendkeys(locatorName, locatorValue,bsnsDate);
			
			break;	
		
		case"miFinClickProspectId":
			try {
				
				searchProspectNumber();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;	
			
		case"miFinEnterUploadId":
			try {
				
				baseobj.sendkeys(locatorName, locatorValue,UploadId);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;
			
		
		case"miFinClickOnPropertyId":
			baseobj.clickOnPropertyId(locatorName, locatorValue, prospectId);
			break;
		
		case"miFinEnterProspectId":
			try {
				
				baseobj.sendkeys(locatorName, locatorValue,prospectId);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			
			break;	
		
			
			
		case"verifyElementText":
			String actualElementText=baseobj.getElementText(locatorName, locatorValue);
			if(testData.startsWith("Var_")) {
				testData=KeywordDataMap.get(testData);
			}
			if(actualElementText.equalsIgnoreCase(testData)) {
				extentLogger.log(Status.PASS, MarkupHelper.createLabel(locatorValue+" text validation passed. expected-"+testData+" , actual-"+actualElementText, ExtentColor.GREEN));

			}else {
				Assert.assertEquals(actualElementText, testData);

			}
			break;

			
			////flexy pay
			
		case"MiFinCreateRepayMentForFlexiPay":
			baseobj.createRepaymentScheduleForFlexiPay(locatorName,locatorValue,prospectId);
			
			break;
			
		case"miFinUploadIdStatus":
			checkUploadStatus();
			break;	
		
			
		case"MiFiUploadRepayment":
			String path=System.getProperty("user.dir")+"\\TestData\\RepaymentSheet\\Flexipay\\Repayment.csv";
			baseobj.sendkeys(locatorName, locatorValue,path);
			
			break;


		case"verifyAlert":
			String actualPopupText= baseobj.getAlertText();
			if(!testData.equals("")||testData.equalsIgnoreCase("NA")) {
				if(testData.startsWith("Var_")) {
					testData=KeywordDataMap.get(testData);
				}
				if(actualPopupText.trim().equalsIgnoreCase(testData)) {
					extentLogger.log(Status.PASS ,MarkupHelper.createLabel(locatorValue+" text validation passed. expected-"+testData+" , actual-"+actualPopupText, ExtentColor.GREEN));

					baseobj.handleAlertDismiss();

				}else {
					SoftAssertObj.assertEquals(actualPopupText, testData);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String screenshotPath=baseobj.robotScreenshot(driverSheetTestCaseName+" alertIssue");

					extentLogger.log(Status.FAIL ,MarkupHelper.createLabel(locatorValue+" text validation passed. expected-"+testData+" , actual-"+actualPopupText, ExtentColor.RED));
					try {
						extentLogger.fail("details", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
					} catch (IOException e1) {

						e1.printStackTrace();
					}
					baseobj.handleAlertDismiss();
					DataPassedStatus=false;
				}
				break;

			}else {
				baseobj.handleAlertDismiss();
			}
		}



	}

	public void checkUploadStatus() {
		
		while (baseobj.driver.findElement(By.xpath("//div[@id='uploadResultsDiv']//tr[2]//td[4]")).getText().equalsIgnoreCase("BULK UPLOAD INITIATED"))  { 
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			baseobj.driver.findElement(By.name("Submit")).click();

			
	    } 
		 while(baseobj.driver.findElement(By.xpath("//div[@id='uploadResultsDiv']//tr[2]//td[4]")).getText().equalsIgnoreCase("VALIDATION IN PROGRESS")) {
			
				 try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 baseobj.driver.findElement(By.name("Submit")).click();
		    }
		
		}
	


	public void searchProspectNumber() throws Exception {
	String prospect=	prospectId.trim();
		baseobj.driver.findElement(By.xpath("//a[contains(text(),'"+prospect+"')]")).click();
	}


}