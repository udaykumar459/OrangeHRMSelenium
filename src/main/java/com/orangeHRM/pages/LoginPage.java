package com.orangeHRM.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.base.BaseClass;

public class LoginPage {

	
	private ActionDriver actionDriver;
	
	private By usernameField = By.name("username");
	private By passwordField = By.name("password");
	private By loginBtn = By.xpath("//button[text()=' Login ']");
	private By errorMsg = By.xpath("//p[text()='Invalid credentials']");
	
	
//	public LoginPage(WebDriver driver) {
//		this.actionDriver = new ActionDriver(driver);
//	}
//	
	
	public LoginPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	public void login(String username, String password) {
		actionDriver.enterText(usernameField, username);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginBtn);
	}
	
	
	public boolean verifyErrorMsg(String expectedErrorMsg) {
		actionDriver.isDisplayed(errorMsg);
		return actionDriver.compareText(errorMsg, expectedErrorMsg);
	}
}
