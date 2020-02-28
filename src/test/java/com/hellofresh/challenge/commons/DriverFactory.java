package com.hellofresh.challenge.commons;

import com.hellofresh.challenge.enums.Browser;
import com.hellofresh.challenge.enums.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import static com.hellofresh.challenge.constants.Constant.CHROME_DRIVER_SYS_PROP;
import static com.hellofresh.challenge.constants.Constant.DOT;
import static com.hellofresh.challenge.constants.Constant.EXE;
import static com.hellofresh.challenge.constants.Constant.OS_NAME;
import static com.hellofresh.challenge.constants.Constant.WEB_DRIVER_PATH;

public class DriverFactory {

  public RemoteWebDriver getDriver(String browser) {
    String browserName = browser.trim();
    RemoteWebDriver webDriver = null;
    if (browserName.equalsIgnoreCase(Browser.CHROME.name())) {
      webDriver = new ChromeDriver();
      String osName = System.getProperty(OS_NAME);
      if (osName.toUpperCase().trim().contains(Platform.WINDOWS.name())) {
        System.setProperty(CHROME_DRIVER_SYS_PROP,
            WEB_DRIVER_PATH + Platform.WINDOWS.name() + DOT + EXE);
      } else if (osName.toUpperCase().trim().contains(Platform.MAC.name())) {
        System.setProperty(CHROME_DRIVER_SYS_PROP, WEB_DRIVER_PATH + Platform.MAC.name());
      } else if (osName.toUpperCase().trim().contains(Platform.LINUX.name())) {
        System.setProperty(CHROME_DRIVER_SYS_PROP, WEB_DRIVER_PATH + Platform.LINUX.name());
      } else {
        //TODO log
      }
    } else if (browserName.equalsIgnoreCase(Browser.FIREFOX.name())) {
       webDriver = new FirefoxDriver();
    }
    return webDriver;
  }
}
