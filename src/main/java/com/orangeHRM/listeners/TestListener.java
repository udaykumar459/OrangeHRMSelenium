package com.orangeHRM.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Base64;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer {
	
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

	// This will trigger when suite starts
	@Override
	public void onStart(ITestContext context) {
		// initialize extent report
		ExtentManager.getReporter();
	}

	// this will trigger when suite ends
	@Override
	public void onFinish(ITestContext context) {
		// flush the report
		ExtentManager.endTest();
	}

	// this will trigger when test starts
	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test case started - " + testName);
	}

	//this will trigger when test passed
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test passed successfully", "Test End: " + testName + " - ✔️ Test passed");
	}


	//this will trigger when test failed
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		ExtentManager.logFailure(BaseClass.getDriver(), "Test failed", "Test End: " + testName + " - ❌ Test failed");
	}

	//this will trigger when test skipped
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test skipped "+ testName);
	}

	

}
