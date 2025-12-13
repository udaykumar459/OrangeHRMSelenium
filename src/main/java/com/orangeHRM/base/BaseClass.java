package com.orangeHRM.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	//public static WebDriver driver;
	//private static ActionDriver actionDriver;
	
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<ActionDriver>();
	
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
	
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}
	
	public static final Logger log = LoggerManager.getLogger(BaseClass.class);

	@BeforeSuite
	public void loadConfig() throws IOException {
		//load configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/config.properties");
		prop.load(fis);
		log.info("config properties file loaded");
//		ExtentManager.getReporter();
	}

	/*
	 * initialize the webdriver based on browser defined in config.properties
	 */
	private synchronized void launchBrowser() {
		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			
			//chrome options
			
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless");//run chrome in headless mode
			options.addArguments("--disable-gpu");// Disables GPU hardware acceleration.
			options.addArguments("--disable-notifications");// Disables browser notifications (like location, popups, alerts).
			options.addArguments("--no-sandbox");// Bypasses OS security sandbox.
			options.addArguments("--disable-dev-shm-usage");// Prevents Chrome from using /dev/shm (shared memory).// Fixes crashes in low-memory environments like Docker containers.
		
			
			
			driver.set(new ChromeDriver(options));
			log.info("chrome browser launched");
		} else if (browser.equalsIgnoreCase("firefox")) {
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless");//run chrome in headless mode
			options.addArguments("--disable-gpu");// Disables GPU hardware acceleration.
			options.addArguments("--disable-notifications");// Disables browser notifications (like location, popups, alerts).
			options.addArguments("--no-sandbox");// Bypasses OS security sandbox.
			options.addArguments("--disable-dev-shm-usage");// Prevents Chrome from using /dev/shm (shared memory).// Fixes crashes in low-memory environments like Docker containers.
		
			driver.set(new FirefoxDriver(options));
//			driver = new FirefoxDriver();
			log.info("firefox browser launched");
		} else if (browser.equalsIgnoreCase("edge")) {
			
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless");//run chrome in headless mode
			options.addArguments("--disable-gpu");// Disables GPU hardware acceleration.
			options.addArguments("--disable-notifications");// Disables browser notifications (like location, popups, alerts).
			options.addArguments("--no-sandbox");// Bypasses OS security sandbox.
			options.addArguments("--disable-dev-shm-usage");// Prevent
			driver.set(new EdgeDriver(options));
//			driver = new EdgeDriver();
			log.info("edge browser launched");
		} else {
			throw new IllegalArgumentException("Browser not supported:" + browser);
		}
		ExtentManager.registerDriver(getDriver());
	}

	/**
	 * maximize browser window and navigate to application URL
	 */
	private void navigateToURL() {
		// implicit wait - global wait - applicable for entire driver life
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		// maximize the browser
		getDriver().manage().window().maximize();

		// launch url
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			log.error("Failed to launch URL "+ e.getMessage());
		}

	}

	@BeforeMethod
	public synchronized void setup() throws IOException {
		System.out.println("Setting up WebDriver for:"+ this.getClass().getSimpleName());
		launchBrowser();
		navigateToURL();
		staticWait(1);
		log.info("Webdriver initialized and browser maximized");
		log.trace("this is a trace message");
		log.error("this is a error message");
		log.debug("this is a debug message");
		log.fatal("this is a fatal message");
		log.warn("this is a warn message");
		
		
		//initialize action driver
		/*
		 * if(actionDriver == null) { actionDriver = new ActionDriver(driver);
		 * log.info("Actiondriver object is created "); }
		 */
		
		//initialize action driver for the current thread
		actionDriver.set(new ActionDriver(getDriver()));
		log.info("Action driver initialized for thread - " + Thread.currentThread().getId());
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				log.error("Unable to quit browser " + e.getMessage());
			}
		}
		log.info("webdriver instance is closed");
		//driver = null;
		//actionDriver = null;
		driver.remove();
		actionDriver.remove();
//		ExtentManager.endTest();
	}
	
	//static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	/*
	 * get the driver
	 */
	public static WebDriver getDriver() {
		
		if(driver.get()==null) {
			log.error("webdriver is not initialized");
			throw new IllegalStateException("webdriver is not initialized");
		}
		return driver.get();
	}

	/*
	 * get the action driver
	 */
	public static ActionDriver getActionDriver() {
		
		if(actionDriver==null) {
			log.error("actionDriver is not initialized");
			throw new IllegalStateException("actionDriver is not initialized");
		}
		return actionDriver.get();
	}
	/**
	 * setting up driver 
	 * @param driver
	 */
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	public static Properties getProp() {
		return prop;
	}
}
