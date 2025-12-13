package com.orangeHRM.tests;

import org.testng.annotations.Test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;

public class DummyTest2 extends BaseClass{
	
	@Test
	public void dummyTest() {
//		ExtentManager.startTest("Dummy test 2 validation started");
		String title = getDriver().getTitle();
		assert title.equals("OrangeHRM"):"Test failed - Title is not matched";
		ExtentManager.logStep("tile is matched");
		
		ExtentManager.logStep("Test passed - title is matched");
	}

}
