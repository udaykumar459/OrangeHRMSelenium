package com.orangeHRM.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{

	
	private int retryCount = 0;
	private static final int maxCount = 2;
	@Override
	public boolean retry(ITestResult result) {
		if(retryCount<maxCount) {
			retryCount++;
			return true;
		}
		return false;
	}
}
