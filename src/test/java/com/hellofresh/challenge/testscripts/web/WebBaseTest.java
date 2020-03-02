package com.hellofresh.challenge.testscripts.web;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hellofresh.challenge.commons.ActionKeywords;
import com.hellofresh.challenge.commons.DriverFactory;
import com.hellofresh.challenge.commons.LoggerClass;
import com.hellofresh.challenge.commons.TestHelper;
import com.hellofresh.challenge.commons.TestListener;
import com.hellofresh.challenge.config.EnvironmentProperties;
import com.hellofresh.challenge.config.SuiteProperties;
import com.hellofresh.challenge.enums.Suite;
import com.hellofresh.challenge.testscripts.BaseTest;
import com.hellofresh.challenge.utils.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import static com.hellofresh.challenge.constants.Constant.ENV_PROP_FILE_PATH;
import static com.hellofresh.challenge.constants.Constant.LOCATORS_FILE_PATH;
import static com.hellofresh.challenge.constants.Constant.SCREENSHOT_BASE_PATH;
import static com.hellofresh.challenge.constants.Constant.URL;
import static com.hellofresh.challenge.constants.Constant.WEB_TEST_DATA_BASE_PATH;

public abstract class WebBaseTest extends BaseTest {
  private final DriverFactory driverFactory = new DriverFactory();
  private SuiteProperties suiteProperties = TestListener.SUITE_PROPERTIES;
  private Properties locators;
  private final EnvironmentProperties envProps = new EnvironmentProperties();

  @BeforeSuite
  public void init() {
    initReport(Suite.WEB.name());
    initEnvProps();
    initLocators();
    deleteScreenshots();
    makeScreenshotdir();
  }

  @DataProvider(name = "dp")
  public Object[][] dataProvider(Method itr) {
    Object[][] returnValue = null;
    try {
      JsonElement jsonData =
          new JsonParser().parse(new FileReader(WEB_TEST_DATA_BASE_PATH + itr.getName() + ".json"));
      JsonElement dataSet = jsonData.getAsJsonObject().get("dataSet");
      List<Map<String, String>> testData =
          new Gson().fromJson(dataSet, new TypeToken<List<Map<String, String>>>() {
          }.getType());
      returnValue = new Object[testData.size()][1];
      int index = 0;
      for (Object[] each : returnValue) {
        each[0] = testData.get(index++);
      }
    } catch (FileNotFoundException e) {
      LoggerClass.logError("FileNotFoundException in data provider", e);
    }
    return returnValue;
  }

  private RemoteWebDriver getBrowser() {
    return driverFactory.getDriver(suiteProperties.getBrowser());
  }

  TestHelper getTestHelper() {
    TestHelper th = new TestHelper();
    RemoteWebDriver driver = getBrowser();
    th.setDriver(driver);
    ActionKeywords actions = new ActionKeywords(driver, locators);
    th.setActions(actions);
    actions.navigateToURL(driver, envProps.getUrl());
    return th;
  }

  private void initEnvProps() {
    JsonParser parser = new JsonParser();
    try {
      JsonElement element =
          parser.parse(new InputStreamReader(new FileInputStream(new File(ENV_PROP_FILE_PATH))));
      JsonObject jsonObject = element.getAsJsonObject();
      JsonObject j = jsonObject.getAsJsonObject(suiteProperties.getEnvironment());
      envProps.setUrl(j.get(URL).getAsString());
    } catch (FileNotFoundException e) {
      LoggerClass.logError("Environment property file not found in " + ENV_PROP_FILE_PATH, e);
    }
  }

  private void initLocators() {
    locators = Util.getProperties(LOCATORS_FILE_PATH);
  }

  private void deleteScreenshots() {
    Path rootPath = Paths.get(SCREENSHOT_BASE_PATH);
    try (Stream<Path> walk = Files.walk(rootPath)) {
      walk.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    } catch (Exception e) {
      LoggerClass.logError("Exception in delete screenshots", e);
    }
  }

  private void makeScreenshotdir() {
    Path rootPath = Paths.get(SCREENSHOT_BASE_PATH);
    try {
      Files.createDirectories(rootPath);
    } catch (IOException e) {
      LoggerClass.logError("IOException in create screenshot directory", e);
    }
  }
}

