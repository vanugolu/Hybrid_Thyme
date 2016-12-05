package com.aims;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aims.model.TestStepData;

public class Keywords {
	protected static Map<String, String> suiteParameters;
	protected static Map<String, String> moduleParameters;
	protected Capabilities capabilities;
	protected WebDriver driver;
	protected static Logger log;
	protected String hubURL;
	
	protected By getBy(Map<String, String> properties, String locator) {

		By by = null;
		String value= null;

		try {
			value = properties.get(locator);

			if(locator.endsWith("xpath"))
				by = By.xpath(value);
			else if(locator.endsWith("id"))
				by = By.id(value);
			else if(locator.endsWith("cssSelector"))
				by = By.cssSelector(value);
			else if(locator.endsWith("linkText"))
				by = By.linkText(value);
			else if(locator.endsWith("partialLinkText"))
				by = By.partialLinkText(value);
			else if(locator.endsWith("tagName"))
				by = By.tagName(value);
			else if(locator.endsWith("name"))
				by = By.name(value);
			else if(locator.endsWith("className"))
				by = By.className(value);
			else
				by = By.xpath(value);      //statement added to cater to the rest locator properties
		}catch(Throwable t) {
			log.debug("Exception caught while accessing the locator :" +locator);
		}
		return by;
	}
	protected WebElement getWebElement(Map<String, String> properties, String locator)
	{
		WebElement element = null;
	
		try {

			element = driver.findElement(getBy(properties, locator));
			//Functions.highlighter(driver, element);
			

		}catch(Throwable t) {
			log.debug("Exception caught at object :" +locator);
		}
		return element;
	}

	protected List<WebElement> getWebElements(Map<String, String> objectFile,String locator)
	{
		List<WebElement> element = null;
		try {
			element = driver.findElements(getBy(objectFile, locator));
			

		}catch(Throwable t) {
			log.debug("Exception caught at object :" +locator);
			
		
		}
		return element;
	}
	
	public boolean input(TestStepData testStepData) {
		log.debug("=============================");
		log.debug("Executing input Keyword");
		try {
			String data = testStepData.getTestStepInputData().get(0).getDataCoulmnValue();
			String objectElement = testStepData.getObject().get(0);
			log.debug("input data@@@@@ -" + data);
			WebElement element = getWebElement(suiteParameters, objectElement);
			element.clear();
			element.sendKeys(data);
			Thread.sleep(2000);
			return true;
		} catch (Throwable t) {
			log.error(t);
		}
		return false;
	}
	
	public boolean clickLink(TestStepData testStepData) {
		try {		
			log.debug("=============================");
			log.debug("Executing clickLink");
			Capabilities capabilities = ((RemoteWebDriver) getDriver()).getCapabilities();
			String browserName = capabilities.getBrowserName();
			WebElement webElement = getWebElement(suiteParameters, testStepData.getObject().get(0));
			log.debug("Content of the item clicked :"+ webElement.getText());
			if(!BrowserType.IE.equals(browserName)) {
				webElement.click();
			}
			else {
				webElement.sendKeys(Keys.CONTROL);
				webElement.click();
			}
			return true;
		} catch (Throwable t) {
			log.error("Error while clicking on link -" + t);
		}
		return false;
	}
	
	public boolean quitBrowser(TestStepData testStepData) {
		quit();
		return true;
	}

	protected WebDriver getDriver() {
		if (driver == null) {
			try {
				driver = DriverFactory.getInstance().getDriver(new URL(hubURL), capabilities);
			} catch (MalformedURLException e) {
				log.error(e);
			}
		}
		return driver;
	}
	
	protected void quit() {
		if (driver != null) {
			DriverFactory.getInstance().quit();
			driver = null;
		}
	}
	
	public boolean launchWebpage(TestStepData testStepData)
	{
		log.debug("=============================");
		log.debug("executing keyword launchWebpage");
		
		String url = testStepData.getObject().get(0);
		log.debug("Url: "+ url);

		try {
			getDriver().navigate().to(suiteParameters.get(url));
			getDriver().manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
			
			String browserName = capabilities.getBrowserName();
			if(BrowserType.CHROME.equals(browserName) && System.getProperty("os.name").equals("Mac OS X")) {
				driver.manage().window().setSize(new Dimension(1920, 978));
			}else {
				driver.manage().window().maximize();
			}
			return true;
			
		} catch(Throwable e) {
			log.error(e);
		}
		return false;
	}

}