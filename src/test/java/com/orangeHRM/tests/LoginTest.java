package com.orangeHRM.tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.RetryAnalyzer;

public class LoginTest extends BaseClass{

	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void testSetup() {
		
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
		
	}
	
	@Test
	public void loginTest() {
//		ExtentManager.startTest("Valid Login Test");
		ExtentManager.logStep("Entered username and password and clicked on login button");
		loginPage.login("admin", "admin123");
		ExtentManager.logStep("login successful");
		softAssert.get().assertTrue(homePage.isAdminTabDisplayed(), "admin tab is not displayed");
		ExtentManager.logStep("admin tab is not displayed");
//		assert homePage.isAdminTabDisplayed()==true:"admin tab is not displayed";
		homePage.logout();
		ExtentManager.logStep("logout successful");
		staticWait(2);
	}
	
//	@Test(retryAnalyzer = RetryAnalyzer.class)
	@Test
	public void invalidLoginTest() {
//		ExtentManager.startTest("in-Valid Login Test");
		ExtentManager.logStep("Entered username and password and clicked on login button");
		loginPage.login("admin", "admin1234");
		ExtentManager.logStep("login failed");
		assertTrue(loginPage.verifyErrorMsg("Invalid credentials"), "invalid credentilas - login failed");
//		softAssert.get().assertTrue(loginPage.verifyErrorMsg("Invalid credentials1"), "invalid credentilas - login failed");
		ExtentManager.logStep("validate error message successful");
		softAssert.get().assertAll();
	}
	
}
