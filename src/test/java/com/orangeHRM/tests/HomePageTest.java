package com.orangeHRM.tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.ExtentManager;

public class HomePageTest extends BaseClass{

	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void testSetup() {
		
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
		
	}
	
	
	@Test
	public void verifyHomePage() {
//		ExtentManager.startTest("Verifying home page logo");
		loginPage.login("admin", "admin123");
		ExtentManager.logStep("login successul");
		assertTrue(homePage.isAdminTabDisplayed(), "admin tab is not displayed in home page");
		ExtentManager.logStep("Logo is displayed in home page");
	}
	
}
