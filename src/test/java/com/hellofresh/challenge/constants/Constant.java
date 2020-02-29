package com.hellofresh.challenge.constants;


import java.io.File;

public final class Constant {

  private Constant() {
  }

  /**
   * Driver Constants
   */

  public static final String RESOURCES_PATH =
      "src" + File.separator + "test" + File.separator + "resources" + File.separator;
  public static final String WEB_DRIVER_PATH = RESOURCES_PATH + "chromedriver";

  //Chrome driver properties
  public static final String CHROME_DRIVER_SYS_PROP = "webdriver.chrome.driver";
  public static final String OS_NAME = "os.name";

  public static final String EXE = "exe";

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
  public static final String DOT = ".";
  public static final String NEW_LINE = "\n";

  /**
   * Report constants
   */
  public static final String REPORT_FILE_NAME = "testReports.html";
  public static final String REPORT_TEMPLATE_FILE_NAME = "ReportTemplate.html";


}
