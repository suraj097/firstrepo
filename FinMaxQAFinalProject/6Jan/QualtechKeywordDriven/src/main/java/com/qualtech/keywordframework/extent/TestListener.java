package com.qualtech.keywordframework.extent;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.qualtech.framework.demo.pages.baseclass.KeywordDrive;
import com.relevantcodes.extentreports.LogStatus;

public class TestListener  implements ITestListener{
	 private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }
 
    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("I am in on Start method " + iTestContext.getName());
        iTestContext.setAttribute("WebDriver", KeywordDrive.driver);
    }
 
    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("I am in on Finish method " + iTestContext.getName());
        //Do tier down operations for extentreports reporting!
        ExtentTestManager.endTest();
        ExtentManager.getReporter().flush();
    }
 
    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("I am in on TestStart method " + getTestMethodName(iTestResult) + " start");
    }
 
    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("I am in on TestSuccess method " + getTestMethodName(iTestResult) + " succeed");
        //ExtentReports log operation for passed tests.
        ExtentTestManager.getTest().log(LogStatus.PASS, "Test passed");
    }
 
    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("I am in on TestFailure method " + getTestMethodName(iTestResult) + " failed");
 
        //Get driver from BaseTest and assign to local webDriver variable.
        Object testClass = iTestResult.getInstance();
       WebDriver driver= KeywordDrive.driver;
 
        //Take base64Screenshot screenshot.
        String base64Screenshot = "data:image/png;base64," + ((TakesScreenshot) driver).
            getScreenshotAs(OutputType.BASE64);
 
        //ExtentReports log and screenshot operations for failed tests.
        ExtentTestManager.getTest().log(LogStatus.FAIL, "Test Failed",
        		ExtentTestManager.getTest().addScreenCapture(base64Screenshot));
    }
 
    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("I am in on TestSkipped method " + getTestMethodName(iTestResult) + " skipped");
        //ExtentReports log operation for skipped tests.
        ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
    }
 
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
    }
	
	
}
