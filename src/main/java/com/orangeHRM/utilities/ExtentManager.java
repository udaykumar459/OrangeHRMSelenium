package com.orangeHRM.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	
	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();
	
	//initialize the extent report
	public synchronized static ExtentReports getReporter() {
		if(extent==null) {
			String reportPath = System.getProperty("user.dir")+ "/src/test/resources/ExtentReport/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation test report");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.DARK);
			
			extent = new ExtentReports();
			extent.attachReporter(spark);
			
			//adding system information
			extent.setSystemInfo("OS", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version ", System.getProperty("java.version"));
			extent.setSystemInfo("User Name ", System.getProperty("user.name"));
			
			
		}
		return extent;
	}
	
	//start the Test
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest	extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}
	
	//End test
	public synchronized static void endTest() {
		getReporter().flush();
	}
	
	//Get current Thread test
	public synchronized static ExtentTest getTest() {
		return test.get();
	}
	
	//get the name of the test
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if(currentTest!=null) {
			return currentTest.getModel().getName();
		}
		else{
			return "No test is currently active for this thread";
		}
	}
	
	//log a step
	public static void logStep(String logMsg) {
		getTest().info(logMsg);
	}
	
	//log a step validation with screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMsg, String screenshotName) {
		getTest().pass(logMsg);
		//screenshot code
		attachScreenshot(driver, screenshotName);
	}
	
	
	//log a failure
	public static void logFailure(WebDriver driver, String logMsg, String screenshotName) {
			String colorMsg = String.format("<span style='color:red;'>%s</span>", logMsg);
			getTest().fail(colorMsg);
			//screenshot code
			attachScreenshot(driver, screenshotName);
	}
	
	//log a skip
	public static void logSkip(String logMsg) {
			String colorMsg = String.format("<span style='color:orange;'>%s</span>", logMsg);
			getTest().skip(colorMsg);
	}
	
	//Take a screenshot with date and time
	public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) {
		TakesScreenshot ts = (TakesScreenshot)driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		
		//format date and time for file name
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		String destFile = System.getProperty("user.dir")+ "/src/test/resources/Screenshots/"+screenshotName+"_"+timeStamp+".png";
		try {
			FileUtils.copyFile(src, new File(destFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//convert screenshot to Base64  for embedding in the report
		String base64Format = convertToBase64(src);
		return base64Format;
	}

	
	
	//convert screenshot to Base64Format
	public static String convertToBase64(File screenshotFile) {
		//Read the file content into a byte array
		byte[] fileContent = null;
		try {
			fileContent = FileUtils.readFileToByteArray(screenshotFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Base64.getEncoder().encodeToString(fileContent);
	}
	
	
	//attach screenshot to report using Base64
	public static synchronized void attachScreenshot(WebDriver driver, String message) {
		try {
			String screenshotBase64 = takeScreenshot(driver, getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
		} catch (Exception e) {
			getTest().fail("Failed to attach screenshot"+ message);
			e.printStackTrace();
		}
	}
	
	//convert screenshot to Base64  for embedding in the report
	
	//Register webdriver for current thread
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}
	
	
}