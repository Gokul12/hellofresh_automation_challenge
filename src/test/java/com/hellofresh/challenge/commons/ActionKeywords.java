package com.hellofresh.challenge.commons;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Properties;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import static com.hellofresh.challenge.constants.Constant.EMPTY_STRING;
import static com.hellofresh.challenge.constants.Constant.NEW_LINE;
import static com.hellofresh.challenge.constants.Constant.PNG_EXTENSION;
import static com.hellofresh.challenge.constants.Constant.SCREENSHOT_BASE_PATH;

public class ActionKeywords {
  private RemoteWebDriver driver;
  private Properties locators;

  public ActionKeywords(RemoteWebDriver driver, Properties locators) {
    this.driver = driver;
    this.locators = locators;
  }

  public boolean type(String locatorKey, String data) {
    boolean status = false;
    WebElement element;
    try {
      element = fluentWait(locatorKey, data, false);
      if (null != element) {
        element.clear();
        element.sendKeys(data);
        LoggerClass.log("Typed " + data + " on " + locatorKey);
        status = true;
      }
    } catch (Exception e) {
      LoggerClass.logError("Type action failed on " + locatorKey, e);
    }
    return status;
  }

  public boolean click(String locatorKey) {
    boolean status = false;
    WebElement element;
    try {
      element = fluentWait(locatorKey, null, false);
      if (element != null) {
        element.click();
        LoggerClass.log("Clicked" + " on " + locatorKey);
        status = true;
      }
    } catch (Exception e) {
      LoggerClass.logError("Click action failed on " + locatorKey, e);
    }
    return status;
  }

  public boolean click(String locatorKey, String data) {
    boolean status = false;
    WebElement element;
    try {
      element = fluentWait(locatorKey, data, false);
      if (element != null) {
        element.click();
        LoggerClass.log("Clicked" + " on " + locatorKey);
        status = true;
      }
    } catch (Exception e) {
      LoggerClass.logError("Click action failed on " + locatorKey, e);
    }
    return status;
  }

  public boolean clickIfPresent(String locatorKey) {
    boolean status = false;
    WebElement element = null;
    try {
      element = fluentWait(locatorKey, null, true);
      if (element != null) {
        element.click();
        LoggerClass.log("Clicked" + " on " + locatorKey);
        status = true;
      }
    } catch (Exception e) {
      if (e.getMessage().contains("is obscured") || LoggerClass.logShortMessage(e)
          .contains("is not clickable")) {
        element.sendKeys(Keys.RETURN);
      } else {
        LoggerClass.logError("Click action failed on " + locatorKey, e);
      }
    }
    return status;
  }

  public boolean clickIfPresent(String locatorKey, String data) {
    boolean status = false;
    WebElement element = null;
    try {
      element = fluentWait(locatorKey, data, true);
      if (element != null) {
        element.click();
        LoggerClass.log("Clicked" + " on " + locatorKey);
        status = true;
      }
    } catch (Exception e) {
      if (e.getMessage().contains("is obscured") || LoggerClass.logShortMessage(e)
          .contains("is not clickable")) {
        element.sendKeys(Keys.RETURN);
      } else {
        LoggerClass.logError("Click action failed on " + locatorKey, e);
      }
    }
    return status;
  }

  public boolean clickIfPresent(String locatorKey, int waitTime) {
    boolean status = false;
    WebElement element = null;
    try {
      element = fluentWaitWithCustomTime(locatorKey, null, waitTime, true);
      if (element != null) {
        element.click();
        LoggerClass.log("Clicked" + " on " + locatorKey);
        status = true;
      }
    } catch (Exception e) {
      LoggerClass.logError("Click action failed on " + locatorKey, e);
    }
    return status;
  }


  public boolean selectByValue(String locatorKey, String data) {
    boolean status = false;
    WebElement element = null;
    try {
      element = fluentWait(locatorKey, EMPTY_STRING, false);
      if (element != null) {
        Select select = new Select(element);
        select.selectByValue(data);
        LoggerClass.log("Selected the pageElement" + element + " with value: " + data);
        status = true;
      }
    } catch (Exception e) {
      LoggerClass.log("Failed to select the element," + locatorKey + " by value");
      LoggerClass.log("Reason: " + e.getMessage().split(NEW_LINE)[0]);
    }
    return status;
  }


  public boolean selectByVisibleText(String locatorKey, String data) {
    boolean status = false;
    WebElement element;
    try {
      element = fluentWait(locatorKey, EMPTY_STRING, false);
      if (element != null) {
        Select select = new Select(element);
        select.selectByVisibleText(data);
        LoggerClass.log("Selected the element with visible text: " + data);
        status = true;
      }
    } catch (Exception e) {
      LoggerClass.logError("Failed to select the element," + data, e);
    }
    return status;
  }

  public boolean assertEquals(String locatorKey, String expectedResult) {
    boolean status = false;
    WebElement element;
    String actualResult = null;
    try {
      element = fluentWait(locatorKey, null, false);
      if (element != null) {
        actualResult = element.getText();
        if (expectedResult.equals(actualResult)) {
          LoggerClass.logSuccess(
              "AssertSuccess! Expected data: " + expectedResult + " match with Actual data: "
                  + actualResult);
          status = true;
        }
      }
    } catch (Exception e) {
      LoggerClass.logError(
          "AssertFailed! Expected data: " + expectedResult + " did not match with Actual data: "
              + actualResult, e);
    }
    return status;
  }

  public boolean isDisplayed(String locatorKey) {
    boolean status = false;
    WebElement element;
    try {
      element = fluentWaitWithCustomTime(locatorKey, null, 5, false);
      if (element != null) {
        status = element.isDisplayed();
        LoggerClass.logSuccess("AssertSuccess! Locator: " + locatorKey + " is displayed");
      } else {
        LoggerClass.logError("AssertFailed! Locator: " + locatorKey + " is not displayed");
      }
    } catch (Exception e) {
      LoggerClass.logError("AssertFailed! Locator: " + locatorKey + " is not displayed", e);
    }
    return status;
  }

  public boolean assertContains(String locatorKey, String expectedResult) {
    boolean status = false;
    WebElement element;
    String actualResult = null;
    try {
      element = fluentWait(locatorKey, null, false);
      if (element != null) {
        actualResult = element.getText();
        if (actualResult.contains(expectedResult)) {
          LoggerClass.logSuccess(
              "AssertSuccess! Actual result: " + actualResult + " contains Expected result: "
                  + expectedResult);
          status = true;
        } else {
          LoggerClass.logError(
              "AssertFailed! Actual result: " + actualResult + " did not contain Expected result: "
                  + expectedResult);
        }
      }
    } catch (Exception e) {
      LoggerClass.logError(
          "AssertFailed! Actual result: " + actualResult + " did not contain Expected result: "
              + expectedResult, e);
    }
    return status;
  }

  public boolean navigateToURL(RemoteWebDriver driver, String url) {
    driver.get(url);
    LoggerClass.log("Navigated to URL: " + url);
    return true;
  }

  public boolean assertContains(RemoteWebDriver driver, String expectedResult) {
    String actualResult = driver.getCurrentUrl();
    boolean status = actualResult.contains(expectedResult);
    if (status) {
      LoggerClass.logSuccess(
          "AssertSuccess! Actual URL: " + actualResult + " contains Expected result: "
              + expectedResult);
    } else {
      LoggerClass.logError(
          "AssertFailed! Actual URL: " + actualResult + " contains Expected result: "
              + expectedResult);
    }
    return status;
  }

  public boolean quitDriver(RemoteWebDriver driver) {
    driver.quit();
    return true;
  }

  public boolean takeScreenshot(String fileName) {
    boolean status = false;
    try {
      TakesScreenshot takesScreenshot;
      String filePath = SCREENSHOT_BASE_PATH + fileName + PNG_EXTENSION;
      takesScreenshot = driver;
      File file = takesScreenshot.getScreenshotAs(OutputType.FILE);
      Files.copy(file.toPath(), new File(filePath).toPath());
      status = true;
    } catch (Exception e) {
      LoggerClass.log("Failed to capture screenshot");
    }
    return status;
  }

  private WebElement fluentWait(final String locatorKey, final String data,
      boolean isVisibilityRequired) {
    return fluentWaitWithCustomTime(locatorKey, data, 10, isVisibilityRequired);
  }

  private WebElement fluentWaitWithCustomTime(final String locatorKey, final String data,
      final int seconds, final boolean isVisibilityRequired) {
    LoggerClass.log("In fluentWait method to get value for pageElement " + locatorKey, 5);

    final String id = getProcessedIdentifier(locatorKey, data);

    if (id == null) {
      LoggerClass.logError("Element, " + locatorKey + ", is not present in the Object Repository");
      return null;
    }

    final Wait<RemoteWebDriver> wait =
        new FluentWait<>(driver).withTimeout(Duration.ofSeconds(seconds))
            .pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
    WebElement e = null;
    try {
      e = wait.until(new Function<RemoteWebDriver, WebElement>() {
        @SuppressWarnings("unchecked")
        @Override
        public WebElement apply(RemoteWebDriver driver) {
          WebElement element = null;
          element = driver.findElement(By.xpath(id));
          if (element != null) {
            if (isVisibilityRequired && !element.isDisplayed()) {
              element = null;
            }
            LoggerClass.log("PageElement " + locatorKey + " is found in the UI ", 5);
          } else {
            LoggerClass.logError("Element " + locatorKey + " is found in the UI ");
          }
          return element;
        }
      });
    } catch (TimeoutException te) {
      LoggerClass.log("The element, " + locatorKey + "[" + id + "], is not found in the page, "
          + ". Timed out after " + seconds + " seconds", 5, te);
    } catch (Exception ex) {
      LoggerClass.logError("Unexpected Exception", ex);
    }
    return e;
  }

  private String getProcessedIdentifier(final String locatorKey, final String data) {
    String value = locators.getProperty(locatorKey);
    if (value == null) {
      LoggerClass.logError(locatorKey + " is not found");
    } else if (value.contains("$variable")) {
      value = value.replace("$variable", data);
      LoggerClass.log(locatorKey + " is replaced with " + value, 5);
    } else {
      LoggerClass.log(value + " is not changed", 5);
    }
    return value;
  }

}
