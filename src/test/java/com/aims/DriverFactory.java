package com.aims;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {

	private DriverFactory() {
		// Do not allow to initialize this class from outside
	}

	private static DriverFactory instance = new DriverFactory();

	public static DriverFactory getInstance() {
		return instance;
	}

	ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

	public WebDriver getDriver(URL url, Capabilities capabilities) {
		WebDriver remoteWebDriver = driver.get();
		if (remoteWebDriver == null) {
			remoteWebDriver = new RemoteWebDriver(url, capabilities);
			driver.set(remoteWebDriver);
		}
		return remoteWebDriver;
	}

	public void quit() {
		if (driver.get() != null) {
			driver.get().quit();
			driver.remove();
		}
	}
}