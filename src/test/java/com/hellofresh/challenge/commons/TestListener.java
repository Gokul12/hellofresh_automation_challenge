package com.hellofresh.challenge.commons;

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

public class TestListener extends TestListenerAdapter
    implements ITestListener, IAlterSuiteListener {

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
