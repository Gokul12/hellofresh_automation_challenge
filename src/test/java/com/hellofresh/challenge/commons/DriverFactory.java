package com.hellofresh.challenge.commons;

import com.hellofresh.challenge.enums.Browser;
import com.hellofresh.challenge.enums.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import static com.hellofresh.challenge.constants.Constant.CHROME_DRIVER_SYS_PROP;
import static com.hellofresh.challenge.constants.Constant.CHROME_WEB_DRIVER_BASE_PATH;
import static com.hellofresh.challenge.constants.Constant.FIREFOX_DRIVER_SYS_PROP;
import static com.hellofresh.challenge.constants.Constant.FIREFOX_WEB_DRIVER_BASE_PATH;
import static com.hellofresh.challenge.constants.Constant.LINUX_DRIVER_PATH;
import static com.hellofresh.challenge.constants.Constant.MAC_DRIVER_PATH;
import static com.hellofresh.challenge.constants.Constant.OS_NAME;
import static com.hellofresh.challenge.constants.Constant.WINDOWS_DRIVER_PATH;

public class DriverFactory {

  public RemoteWebDriver getDriver(String browser) {
    String browserName = browser.trim();
    RemoteWebDriver webDriver = null;
    if (browserName.equalsIgnoreCase(Browser.CHROME.name())) {
      setSystemProperty(CHROME_DRIVER_SYS_PROP, CHROME_WEB_DRIVER_BASE_PATH);
      webDriver = new ChromeDriver();
    } else if (browserName.equalsIgnoreCase(Browser.FIREFOX.name())) {
      setSystemProperty(FIREFOX_DRIVER_SYS_PROP, FIREFOX_WEB_DRIVER_BASE_PATH);
      webDriver = new FirefoxDriver();
    }
    if (webDriver != null) {
      webDriver.manage().window().maximize();
    }
    return webDriver;
  }

  private void setSystemProperty(String key, String value) {
    String osName = System.getProperty(OS_NAME);
    if (osName.toUpperCase().trim().contains(Platform.WINDOWS.name())) {
      System.setProperty(key, value + WINDOWS_DRIVER_PATH);
    } else if (osName.toUpperCase().trim().contains(Platform.MAC.name())) {
      System.setProperty(key, value + MAC_DRIVER_PATH);
    } else if (osName.toUpperCase().trim().contains(Platform.LINUX.name())) {
      System.setProperty(key, value + LINUX_DRIVER_PATH);
    }
  }
}
