package com.hellofresh.challenge.constants;


import com.hellofresh.challenge.enums.Platform;
import java.io.File;

public final class Constant {

  private Constant() {
  }

  /**
   * Driver Constants
   */

  public static final String RESOURCES_PATH =
      "src" + File.separator + "test" + File.separator + "resources" + File.separator;
  public static final String CHROME_WEB_DRIVER_BASE_PATH =
      RESOURCES_PATH + "drivers" + File.separator + "chromedriver";
  public static final String FIREFOX_WEB_DRIVER_BASE_PATH =
      RESOURCES_PATH + "drivers" + File.separator + "geckodriver";

  //Chrome driver properties
  public static final String CHROME_DRIVER_SYS_PROP = "webdriver.chrome.driver";
  public static final String OS_NAME = "os.name";

  //Firefox driver properties
  public static final String FIREFOX_DRIVER_SYS_PROP = "webdriver.gecko.driver";

  public static final String MAC_DRIVER_PATH = "_" + Platform.MAC.name();
  public static final String WINDOWS_DRIVER_PATH = "_" + Platform.WINDOWS.name() + ".exe";
  public static final String LINUX_DRIVER_PATH = "_" + Platform.LINUX.name();


  /**
   * Logger constants
   */
  public static final String LOG_ERROR = "LogError: ";
  public static final String LOG_SUCCESS = "LogSuccess: ";
  public static final String LOG_WARNING = "LogWarning: ";
  public static final String REASON = "Reason: ";


  /**
   * Common constants
   */
  public static final String NEW_LINE = "\n";
  public static final String EMPTY_STRING = "";

  /**
   * Report constants
   */
  public static final String REPORT_FILE_NAME = "testReports.html";
  public static final String REPORT_TEMPLATE_FILE_PATH =
      RESOURCES_PATH + "reports/ReportTemplate.html";

  /**
   * Configuration constants
   */
  public static final String BROWSER = "browser";
  public static final String PLATFORM = "platform";
  public static final String ENVIRONMENT = "environment";
  public static final String IS_PARALLEL = "is_parallel";

  /**
   * Environment constants
   */
  public static final String ENV_PROP_FILE_PATH = RESOURCES_PATH + "environment.json";
  public static final String URL = "url";

  /**
   * Locator constants
   */
  public static final String LOCATORS_FILE_PATH = RESOURCES_PATH + "locators.properties";

  /**
   * Screenshot constants
   */
  public static final String SCREENSHOT_BASE_PATH = RESOURCES_PATH + "screenshots" + File.separator;
  public static final String PNG_EXTENSION = ".png";

  /**
   * TestData constants
   */
  public static final String API_TEST_DATA_BASE_PATH =
      RESOURCES_PATH + "testdata" + File.separator + "api" + File.separator;
  public static final String WEB_TEST_DATA_BASE_PATH =
      RESOURCES_PATH + "testdata" + File.separator + "web" + File.separator;
  public static final String STATUS_CODE = "statusCode";
  public static final String DATA_VALIDATION = "dataValidation";
}
