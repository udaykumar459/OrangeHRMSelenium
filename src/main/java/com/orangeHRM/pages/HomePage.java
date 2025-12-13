package com.orangeHRM.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.base.BaseClass;

public class HomePage {
	
	
private ActionDriver actionDriver;
	
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By orangeLogo = By.cssSelector("img[alt='client brand banner']");
	private By profileIcon = By.cssSelector(".oxd-userdropdown-img");
	private By logoutBtn = By.xpath("//a[text()='Logout']");
	
	
//	public HomePage(WebDriver driver) {
//		this.actionDriver = new ActionDriver(driver);
//	}
//	
	
	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	public boolean isAdminTabDisplayed() {
		return actionDriver.isDisplayed(adminTab);
	}
	
	public boolean isLogoDisplayed() {
		return actionDriver.isDisplayed(orangeLogo);
	}
	
	public void logout() {
		actionDriver.click(profileIcon);
		actionDriver.click(logoutBtn);
	}

}
