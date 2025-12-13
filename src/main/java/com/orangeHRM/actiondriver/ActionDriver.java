package com.orangeHRM.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger log = BaseClass.log;
	
	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		String explicitWait = BaseClass.getProp().getProperty("explicitwait");
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(explicitWait)));
		log.info("Webdriver instance is created");
	}

	// method to click on element
	public void click(By by) {
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			log.info("clicked on element - ");
			driver.findElement(by).click();
			ExtentManager.logStep("clicked on element - ");
		} catch (Exception e) {
			applyBorder(by, "red");
			log.error("Unable to click on element - " + e.getMessage());
			log.error("unable to click on element");
			ExtentManager.logStep("unable to click on element");
		}
	}

	// method to enter text on element
	public void enterText(By by, String value) {
		try {
//			String eleDesc = getElementDescription(by);
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			WebElement ele = driver.findElement(by);
			ele.clear();
			ele.sendKeys(value);
			ExtentManager.logStep("entered the value on element - " + value);
			log.info("entered the value on element - " + " - "+ value);
		} catch (Exception e) {
			applyBorder(by, "red");
			log.error("Unable to enter text on element - " + e.getMessage());
			log.error("Unable to enter text on element");
			ExtentManager.logFailure(driver, "Unable to enter text on element - " + e.getMessage(), "entered the value on element - " + " - "+ value);
		}
	}

	// method to get text of an element
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			log.error("Unable to get text on element - " + e.getMessage());
			return "";
		}
	}

	// method to compare two text
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			String actualText = driver.findElement(by).getText();
			if (actualText.equals(expectedText)) {
				log.info(actualText + " - is matching with - " + expectedText);
				ExtentManager.logStepWithScreenshot(driver, actualText + " - is matching with - " + expectedText, actualText + " - is matching with - " + expectedText);
				return true;
			} else {
				log.warn(actualText + " - is not matching with - " + expectedText);
				applyBorder(by, "red");
				ExtentManager.logFailure(driver, actualText + " - is matching with - " + expectedText, actualText + " - is not matching with - " + expectedText);
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			log.error("Unable to compare text - " + e.getMessage());
			ExtentManager.logFailure(driver, "Unable to compare text - " + e.getMessage(), "Unable to compare text");
		}
		return false;
	}
	
	//Check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			log.info("element is visible - " );
			ExtentManager.logStep("element is visible - ");
			ExtentManager.logStepWithScreenshot(driver, "Element is not displayed - ", "Element is displayed");
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			log.error("Element is not displayed - " + e.getMessage());
			ExtentManager.logFailure(driver, "Element is not displayed - " + e.getMessage(), "Element is not displayed");
			return false;
		}
	}
	
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(driver -> ((JavascriptExecutor)driver).
					executeScript("return document.readyState").equals("complete"));
			log.info("Page loaded successfully");
		} catch (Exception e) {
			log.error("Page didn't load within - " + timeOutInSec + "seconds - " + e.getMessage());
		}
		
	}
	
	//Scroll to element
	public void scrollToElement(By by) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement ele = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", ele);
			log.info("scrolled to element");
		} catch (Exception e) {
			log.error("Unable to scroll to element " + e.getMessage());
		}
	}

	public void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
			log.info("Element is clickable");
		} catch (Exception e) {
			log.error("Element is not clickable - " + e.getMessage());
		}
	}

	public void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			log.info("Element is visible");
		} catch (Exception e) {
			log.error("Element is not visible - " + e.getMessage());
		}
	}

	
	public String getElementDescription(By locator) {
		
		try {
			if(driver==null) {
				return "driver is null";
			}
			if(locator==null) {
				return "locator is null";
			}
			
			WebElement ele = driver.findElement(locator);
			
			String name = ele.getDomAttribute("name");
			String id = ele.getDomAttribute("id");
			String text = ele.getText();
			String className = ele.getDomAttribute("class");
			String placeHolder = ele.getDomAttribute("placeholder");
			
			//return the description based on element attributes
			
			if(isNotEmpty(name)) {
				return "--> Element with name - "+ name;
			}
			else if(isNotEmpty(id)) {
				return "--> Element with id - "+ id;
			}
			else if(isNotEmpty(text)) {
				return "--> Element with text - "+ truncate(text, 50);
			}
			else if(isNotEmpty(className)) {
				return "--> Element with className - "+ className;
			}
			else if(isNotEmpty(placeHolder)) {
				return "--> Element with placeHolder - "+ placeHolder;
			}
			
		} catch (Exception e) {
			log.error("--> Uanble to describe the element" + e.getMessage());
		}
		return "--> Uanble to describe the element";
	}
	
	
	//method to check if string in not null or empty
	private boolean isNotEmpty(String value) {
		return value!=null && !value.isEmpty();
	}
	
	//method to truncate to logn string
	private String truncate(String value, int maxLength) {
		if(value==null || value.length()<=maxLength) {
			return value;
		}
		return value.substring(0, maxLength)+ "...";
	}
	
	//utility  method to Border an element
	public void applyBorder(By by, String color) {
		try {
			WebElement ele = driver.findElement(by);
			String script = "arguments[0].style.border='3px solid "+color+"'";
			JavascriptExecutor js  = (JavascriptExecutor)driver;
			js.executeScript(script, ele);
			log.info("Applied the border with color "+ color +" to element:");
		} catch (Exception e) {
			log.warn("Failed to apply  the border to an element: ");
		}
		
	}
}
