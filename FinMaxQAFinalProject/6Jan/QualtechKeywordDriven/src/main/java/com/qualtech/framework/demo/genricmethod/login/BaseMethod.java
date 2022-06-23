package com.qualtech.framework.demo.genricmethod.login;


import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qualtech.mifin.framework.utilities.testdata.DataConvertionUtil;
import org.testng.Reporter;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.google.common.io.Files;
import com.qualtech.keywordframework.core.DataManager;
import com.qualtech.keywordframework.core.Repayment;
import com.qualtech.keywordframework.core.SystemUtil;
import com.relevantcodes.extentreports.ExtentTest;

public class BaseMethod {
	public static WebDriver driver;
	public Properties prop;
	public static ExtentTest extentLogger;
	public static final String USER_DIR="user.dir";
	public static final String DATE_FORMAT_PATTERN = "mm_dd_yyyy hh_mm_ss";
	public final String SCENARIO_SHEET_PATH=System.getProperty("user.dir")+"\\TestData\\keywordTestData.xlsx";
	
	RandomUtil rUtil= new RandomUtil();

	public  int getColumnNumberByColumnName(Sheet sheet, String columnName) {
		Row firstRowColumns=sheet.getRow(0);
		int columnNumber=-1;
		int columnCount=firstRowColumns.getLastCellNum();
		for(int i=0; i<=columnCount-1; i++){
			if(firstRowColumns.getCell(i).getStringCellValue().toLowerCase().contains(columnName.toLowerCase())){
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
			if(cell !=null && cell.getStringCellValue().equalsIgnoreCase(rowID)){
				rowNumber=i;
				break;
			}
		}
		return rowNumber;
	}
	public String enterCurrenBsnsDate()
	{
		return driver.findElement(By.xpath("//span[@class='date-calender-top']")).getText().trim();
	}
	public WebDriver LaunchBrowse(String browsername) {


		if(browsername.equalsIgnoreCase("chrome")){
			System.setProperty("webdriver.chrome.driver", "Driver\\chromedriver.exe");

//			if(prop.getProperty("headless").equals("chrome")) {
//				ChromeOptions option=new ChromeOptions();
//				option.addArguments("----headless");
//				driver=new ChromeDriver(option);
//			}else {
				driver=new ChromeDriver();
			//}
			
		}
		else if(browsername.equalsIgnoreCase("firefox"))
		{
			System.setProperty("webdriver.gecko.driver", "Driver\\geckodriver.exe");
			driver=new FirefoxDriver();
			Reporter.log("Firefox browser is launched!!!",true);
			
		}
		return driver;
	}
	
	
	public void openUrl(String url) {


		driver.get(url);
		driver.manage().timeouts().implicitlyWait(2000, TimeUnit.SECONDS);
	}
	public void quitDriver() {


		driver.quit();
	}

	
	
	
	
	public void acceptAlert() {

		int i=0;
		while(i++<10)
		{
			try
			{
				Alert alert = driver.switchTo().alert();
				String aleartObj= alert.getText();
				
				alert.accept();
				break;
			}
			catch(NoAlertPresentException e)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				continue;
			}
		}
	}
	
	
	public String getAlertText() {
		String aleartObj="";
		int i=0;
		while(i++<10)
		{
			try
			{
				Alert alert = driver.switchTo().alert();
				aleartObj= alert.getText();
				return aleartObj;
			}
			catch(NoAlertPresentException e)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				continue;
			}
		}
		return aleartObj;
	}

	
	
	
	public void  handleAlertDismiss()  {
		int i=0;
		while(i++<5)
		{
			try
			{
				Alert alert = driver.switchTo().alert();
				alert.dismiss();
				break;
			}
			catch(NoAlertPresentException e)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				continue;
			}
		}
	}
	
	
	
	public String getAllertText() {

		return driver.switchTo().alert().getText();
	}
	public By locatorValue(String locatorTpye, String value) {
		By by;
		switch (locatorTpye) {
		case "id":
			by = By.id(value);
			break;
		case "name":
			by = By.name(value);
			break;
		case "xpath":
			by = By.xpath(value);
			break;
		case "css":
			by = By.cssSelector(value);
			break;
		case "linkText":
			by = By.linkText(value);
			break;
		case "partialLinkText":
			by = By.partialLinkText(value);
			break;
		default:
			by = null;
			break;
		}
		return by;
	}

	public void click(String locatorTpye, String value) {
		By by = locatorValue(locatorTpye, value);
		WebElement elementObj=driver.findElement(by);
		waitUntilElementEnabled(elementObj, 2000);
		elementObj.click();

	}


	public void sendkeys(String locatorTpye, String locatorvalue,String value ) {
		By by = locatorValue(locatorTpye, locatorvalue);
		
		WebElement elementObj=driver.findElement(by);
		waitUntilElementVisible(elementObj, 2000);
		elementObj.clear();
		elementObj.sendKeys(value);;

	}

	
	public  String  dateFormet() {
		  String format = "08-DEC-2034" ;
		  return format;
	}
	
	
	
	
	public  String  addMonths(String dateAsString, int nbMonths) throws ParseException {
        String format = "dd-MMM-yyyy" ;
        SimpleDateFormat sdf = new SimpleDateFormat(format) ;
        Date dateAsObj = sdf.parse(dateAsString) ;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAsObj);
        cal.add(Calendar.MONTH, nbMonths);
        Date dateAsObjAfterAMonth = cal.getTime() ;
    System.out.println(sdf.format(dateAsObjAfterAMonth));
    return sdf.format(dateAsObjAfterAMonth) ;
}
	public Properties readproPerties() throws IOException {
		prop= new Properties();
		try{
			FileInputStream fisObj=new FileInputStream("src\\main\\java\\com\\qualtech\\framework\\demo\\properties\\config\\config.properties");
			prop.load(fisObj);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	
	public String takeScreenShot(String testName) {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		File destFile=null;	
		try {		
			String timeStamp=SystemUtil.getObject().getTimeStamp("dd-MM-yyyy hh_mm_ss");			
			destFile=new File(System.getProperty("user.dir")+File.separator+"ExtentResult"+ File.separator+testName+"_"+timeStamp+".png");		
			Files.copy(scrFile, destFile);		
		} catch (Exception e) {		

		}		
		return destFile.getAbsolutePath();	
	}
	
	public String robotScreenshot(String testName) {
		
		File destFile=null;
		try {

			String timeStamp=SystemUtil.getObject().getTimeStamp("dd-MM-yyyy hh_mm_ss");			
			destFile=new File(System.getProperty("user.dir")+File.separator+"ExtentResult"+ File.separator+testName+"_"+timeStamp+".png");		
			BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

			ImageIO.write(image, "png", destFile);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return destFile.getAbsolutePath();
	}
	public void defaultWindow() {
		String lastWindow = null;
		 Set<String> handles = driver.getWindowHandles();
		    for (String aux : handles) {
		        lastWindow = aux;
		    }
		    driver.switchTo().window(lastWindow);
	}

	public void switchToNewopenWindow()
	{
		Set<String> handleValues=driver.getWindowHandles();
		for(String st:handleValues)
		{
			driver.switchTo().window(st);
		}
	}

	public void switchWindowFocusByTitle(String title) {
		try{
			Set<String> handleValues=driver.getWindowHandles();
			for(String handleValue:handleValues){
				driver.switchTo().window(handleValue);	
				if(driver.getTitle().trim().equalsIgnoreCase(title)){
					break;
				}
			}
		}catch(Exception e){
			//logger.debug( Constants.ELEMENT_SEARCH_ERROR_MESSAGE, e);
		}
	}
///////////////////////////////////////////////////////////////////////////////
	public  String getElementText(String locatorTpye, String locatorvalue) {
		By by = locatorValue(locatorTpye,locatorvalue);
		String getText = driver.findElement(by).getText();
		return getText;
	}
	
	public  String getAttributeValue(String locatorTpye, String locatorvalue,String attributeName) {
		By by = locatorValue(locatorTpye,locatorvalue);
		String attributeValue = driver.findElement(by).getAttribute(attributeName);
		return attributeValue;
	}

	public  int getWebElementCount(String locatorTpye, String locatorvalue) {
		By by = locatorValue(locatorTpye,locatorvalue);
	int elementCount=driver.findElements(by).size();
	return elementCount;
	}
	///////////////////////////////////////////////////////
	public String selectByVisibleText(String locatorTpye, String locatorvalue,String textToSelect)  {
		By by = locatorValue(locatorTpye, locatorvalue);
		WebElement elementObj=driver.findElement(by);
		waitUntilElementVisible(elementObj, 2000);
		Select select=select(elementObj);
		if(select!=null){
			select.selectByVisibleText(textToSelect);
			//BaseTestMethods.test.log(LogStatus.PASS, textToSelect+" is selected from Drop-down.");
		}
		return textToSelect;
	}

	
	public String getText(String locatorTpye, String locatorvalue)  {
		By by = locatorValue(locatorTpye, locatorvalue);
		String text = driver.findElement(by).getText();
		return text;
		
		
	}
	public String getGroupLimitCode()
	{
		String[] words = getAlertText().split(" ");
		return words[words.length-1];
	}
	
	
	
	
	public String getRandomGroupName(String groupName)
	{
		
		String last = rUtil.generateRandomString(4);
		return groupName+" "+last;

	}

	public void clickOnPropertyId(String locatorType, String locatorvalue,String prospectId )
	{
		By by = locatorValue(locatorType, locatorvalue);
		
		 List<WebElement> elementObj = driver.findElements(by);
		 List<WebElement> propertyIdLinks = driver.findElements(By.xpath("(//tr[@class='tr_list_alternate'])//td[1]/a"));
		 for(int i=0;i<elementObj.size();i++)
		 {
			 String text = elementObj.get(i).getText();
			 String text2 = propertyIdLinks.get(i).getText();
			 if(text.contains(prospectId))
			 {
				 propertyIdLinks.get(i).click();
				 break;
			 }
		 }
		 
		
	}
	
	public void setRequestedLimitAmountTxtBx(String locatorTpye, String locatorvalue,String value) throws InterruptedException
	{
		By by = locatorValue(locatorTpye, locatorvalue);
		WebElement elementObj=driver.findElement(by);
	
		if(elementObj.getAttribute("title").equalsIgnoreCase("0"))
		{
			
			elementObj.sendKeys(Keys.CONTROL + "a");
			elementObj.sendKeys(value);
			
		}else {
			elementObj.click();
			elementObj.clear();
			elementObj.sendKeys(value);
		}

	}
	
	
	
	public void createRepaymentScheduleForFlexiPay(String Tenure, String totalLoanAmount,String ProspectNum) {
		 String excelfileName=System.getProperty("user.dir")+"\\TestData\\RepaymentSheet\\Flexipay\\Repayment.xls";
	     String csvFileName1=System.getProperty("user.dir")+"\\TestData\\RepaymentSheet\\Flexipay\\Repayment.csv";
		 Repayment repaymentObj=new Repayment();
			//repaymentSchedule
		 
			try {
				repaymentObj.repaymentScheduleForFlexiPay(Tenure,totalLoanAmount,ProspectNum);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DataConvertionUtil ConvertFile=new DataConvertionUtil();
			try {
				ConvertFile.excelToCSV(excelfileName, csvFileName1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	

	
	
	
	public void selectByIndex(String locatorTpye, String locatorvalue,String value)  {
		By by = locatorValue(locatorTpye, locatorvalue);
		Select select=select(driver.findElement(by));
		if(select!=null){
			int index = Integer.parseInt(value);	
			select.selectByIndex(index);
			//BaseTestMethods.test.log(LogStatus.PASS, textToSelect+" is selected from Drop-down.");
		}
		
	}

	protected Select select(WebElement element) {
		Select selectElement = null;
		try {
			selectElement = new Select(element);
		} catch (UnexpectedTagNameException e) {
			//logger.error("Element " + element + " was not with select tag name   Error Message UnexpectedTagNameException  -->",e);
		}
		return selectElement;
	}

	public String getRandomPANNo()
	{
		String firstChars = rUtil.generateRandomString(5);
		String numeric = rUtil.generateRandomNumber(4);
		String last = rUtil.generateRandomString(1);

		String pan=firstChars+numeric+last;
		return pan;

	}


	public String getRandomAadharNo()
	{
		String aadharNo = rUtil.generateRandomNumber(11);
		return "5"+aadharNo;

	}

	public String getRandomFormNo(){
		String FormNo = rUtil.generateRandomNumber(8);
		return FormNo;

	}
	public void waitUntilElementEnabled(WebElement element, int timeOut){
		WebDriverWait wt = new WebDriverWait(driver, timeOut);
		wt.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	
	public void waitUntilElementVisible(WebElement element, int timeOut){
		WebDriverWait wt = new WebDriverWait(driver, timeOut);
		wt.until(ExpectedConditions.visibilityOf(element));
	}

	public  List<Map<String, String>> getTestCaseData(String dataSheetPath, String sheetName) {
		Workbook wbook=null;
		try {
			wbook = new XSSFWorkbook(dataSheetPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Sheet sheet=wbook.getSheet(sheetName);
		FormulaEvaluator formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) wbook);
		MissingCellPolicy MCP=Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;
		List<Map<String, String>> dataMapsList=new ArrayList<>();
		for(int j=1; j<=sheet.getLastRowNum();j++) {
			Map<String, String> dataMap=new HashMap<String, String>();
			Row columnRow=sheet.getRow(0);
			Row dataRow=sheet.getRow(j);
			for(int i=0; i<columnRow.getLastCellNum(); i++){
				String dataKeyName=columnRow.getCell(i, MCP).getStringCellValue();
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


}





