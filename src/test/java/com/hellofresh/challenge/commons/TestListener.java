package com.hellofresh.challenge.commons;

import com.hellofresh.challenge.config.SuiteProperties;
import com.hellofresh.challenge.reports.Report;
import com.hellofresh.challenge.reports.TestResult;
import com.hellofresh.challenge.reports.TestStatus;
import com.hellofresh.challenge.testscripts.BaseTest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.testng.IAlterSuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;
import static com.hellofresh.challenge.constants.Constant.BROWSER;
import static com.hellofresh.challenge.constants.Constant.EMPTY_STRING;
import static com.hellofresh.challenge.constants.Constant.ENVIRONMENT;
import static com.hellofresh.challenge.constants.Constant.IS_PARALLEL;
import static com.hellofresh.challenge.constants.Constant.PLATFORM;
import static com.hellofresh.challenge.constants.Constant.PNG_EXTENSION;
import static com.hellofresh.challenge.constants.Constant.SCREENSHOT_BASE_PATH;

public class TestListener extends TestListenerAdapter
    implements ITestListener, IAlterSuiteListener {

  public static final SuiteProperties SUITE_PROPERTIES = new SuiteProperties();

  @Override
  public void onTestSuccess(ITestResult tr) {
    generateTestResult(tr);
  }

  @Override
  public void onTestFailure(ITestResult tr) {
    generateTestResult(tr);
  }

  @Override
  public void onTestSkipped(ITestResult tr) {
    generateTestResult(tr);
  }

  @Override
  public void alter(List<XmlSuite> suites) {
    for (XmlSuite suite : suites) {
      boolean isParallel;
      if (EMPTY_STRING.equals(System.getProperty(IS_PARALLEL))) {
        isParallel = Boolean.valueOf(suite.getParameter(IS_PARALLEL));
      } else {
        isParallel = Boolean.valueOf(System.getProperty(IS_PARALLEL));
      }
      String browser = System.getProperty(BROWSER);
      String platform = System.getProperty(PLATFORM);
      String environment = System.getProperty(ENVIRONMENT);
      if (EMPTY_STRING.equals(browser)) {
        SUITE_PROPERTIES.setBrowser(suite.getParameter(BROWSER));
      } else {
        SUITE_PROPERTIES.setBrowser(browser);
      }
      if (EMPTY_STRING.equals(platform)) {
        SUITE_PROPERTIES.setPlatform(suite.getParameter(PLATFORM));
      } else {
        SUITE_PROPERTIES.setPlatform(platform);
      }
      if (EMPTY_STRING.equals(environment)) {
        SUITE_PROPERTIES.setEnvironment(suite.getParameter(ENVIRONMENT));
      } else {
        SUITE_PROPERTIES.setEnvironment(environment);
      }
      SUITE_PROPERTIES.setIsParallel(isParallel);
      if (isParallel) {
        suite.setParallel(XmlSuite.ParallelMode.METHODS);
        suite.setThreadCount(5);
      }
    }
  }

  private void generateTestResult(ITestResult tr) {
    TestResult tR = new TestResult(tr.getName().toUpperCase());
    long totalDuration = (tr.getEndMillis() - tr.getStartMillis());
    List<String> logs = new ArrayList<>();
    logs.addAll(LoggerClass.getOutput(tr));
    long seconds = TimeUnit.MILLISECONDS.toSeconds(totalDuration);
    String totalTime = String.valueOf(seconds / 60) + ":" + seconds % 60;
    tR.setDuration(totalTime);
    tR.setDurationInSeconds(seconds);
    if (fetchLogs(tR, logs)) {
      tR.setStatus(TestStatus.valueOf(4).name());
    } else {
      tR.setStatus(TestStatus.valueOf(tr.getStatus()).name());
    }
    if (TestStatus.FAIL.name().equalsIgnoreCase(tR.getStatus())) {
      tR.setScreenshotLocation(SCREENSHOT_BASE_PATH + tr.getName() + PNG_EXTENSION);
    }
    BaseTest bT = (BaseTest) tr.getInstance();
    Report report = bT.getReport();
    report.addResults(tR);
  }

  private boolean fetchLogs(TestResult testResult, List<String> logs) {
    boolean warningStatus = false;
    String successRegEx = "LogSuccess:(.+)";
    Pattern successPattern = Pattern.compile(successRegEx);

    String warningRegEx = "LogWarning:(.+)";
    Pattern warningPattern = Pattern.compile(warningRegEx);

    String failRegEx = "LogError:(.+)";
    Pattern failPattern = Pattern.compile(failRegEx);

    for (String log : logs) {
      log = log.replace("<", "&lt;");
      log = log.replace(">", "&gt;");
      log = log.replace("\n", "<br/>");
      if (log.contains("&")) {
        log = log.replaceAll("&amp;|&", "&amp;");
      }
      if (log.contains("%s")) {
        log = log.replaceAll("%s", " ");
      }


      Matcher successMatcher = successPattern.matcher(log);
      Matcher warningMatcher = warningPattern.matcher(log);
      Matcher errorMatcher = failPattern.matcher(log);

      if (successMatcher.find()) {
        testResult.setPassedCheckPoint(successMatcher.group(1).trim());
        testResult.setDetailedLog(successMatcher.group(1).trim());
      } else if (errorMatcher.find()) {
        testResult.setFailedCheckPoint(errorMatcher.group(1).trim());
        testResult.setDetailedLog(errorMatcher.group(1).trim());
      } else if (warningMatcher.find()) {
        warningStatus = true;
        testResult.setFailedCheckPoint(warningMatcher.group(1).trim());
        testResult.setDetailedLog(warningMatcher.group(1).trim());
      } else {
        testResult.setDetailedLog(log);
      }
    }
    return warningStatus;
  }
}
