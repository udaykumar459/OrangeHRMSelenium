package com.orangeHRM.tests;

import static org.testng.Assert.assertTrue;

import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.ExtentManager;

public class DummyTest extends BaseClass{
	
	@BeforeMethod
	public void testbefore() {
		System.out.println("before method dummytest");
	}
	
	@Test
	public void dummyTest() {
//		ExtentManager.startTest("Dummy test  validation started");
		String title = getDriver().getTitle();
		assert title.equals("OrangeHRM"):"Test failed - Title is not matched";
//		ExtentManager.logSkip("this test is skipped");
//		throw new SkipException("skipping this test case as part of testing");
	}

//	private LoginPage loginPage;
//	private HomePage homePage;
//	
//	@BeforeMethod
//	public void testSetup() {
//		
//		loginPage = new LoginPage(getDriver());
//		homePage = new HomePage(getDriver());
//		
//	}
//	
//	@Test
//	public void loginTest() {
//		loginPage.login("admin", "admin123");
//		assertTrue(homePage.isAdminTabDisplayed(), "admin tab is not displayed");
////		assert homePage.isAdminTabDisplayed()==true:"admin tab is not displayed";
//		homePage.logout();
//		staticWait(2);
//	}
}
