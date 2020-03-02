package com.hellofresh.challenge.reports;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestResult {
  private TestStatus teststatus;
  private String testScriptName;
  private String status;
  private String duration;
  private long durationInSeconds;
  private final List<String> passedCheckPoints;
  private final List<String> failedCheckPoints;
  private String testData;
  private final List<String> detailedLogs;
  private String resultID;
  private String screenshotLocation;


  public TestResult() {
    passedCheckPoints = new ArrayList<>();
    failedCheckPoints = new ArrayList<>();
    detailedLogs = new ArrayList<>();
  }

  public TestResult(String scriptName) {
    this();
    this.testScriptName = scriptName;
  }

  public List<String> getDetailedLogs() {
    return detailedLogs;
  }

  public String getDuration() {
    return duration;
  }

  public long getDurationInSeconds() {
    return durationInSeconds;
  }

  public List<String> getFailedCheckPoints() {
    return failedCheckPoints;
  }

  public List<String> getPassedCheckPoints() {
    return passedCheckPoints;
  }

  public String getResultID() {
    return resultID;
  }

  public TestStatus getresultStatus() {
    return this.teststatus;
  }

  public String getScreenshotLocation() {
    return screenshotLocation;
  }

  public String getStatus() {
    return this.status;
  }

  public String gettestData() {
    return testData;
  }

  public String getTestScriptName() {
    return testScriptName;
  }

  public void setDetailedLog(String message) {
    this.detailedLogs.add(message);
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public void setDurationInSeconds(long durationInSeconds) {
    this.durationInSeconds = durationInSeconds;
  }

  public void setFailedCheckPoint(String failedCheckPoint) {
    this.failedCheckPoints.add(failedCheckPoint);
  }

  public void setPassedCheckPoint(String passedCheckPoint) {
    this.passedCheckPoints.add(passedCheckPoint);
  }


  public void setResultID(String resultID) {
    this.resultID = resultID;
  }

  public void setresultStatus(TestStatus resultstatus) {
    this.teststatus = resultstatus;
  }

  public void setScreenshotLocation(String screenshotLocation) {
    this.screenshotLocation = screenshotLocation;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setTestData(String testData) {
    this.testData = testData;
  }

  public void setTestScriptName(String testScriptName) {
    this.testScriptName = testScriptName;
  }

  @Override
  public String toString() {
    return "scriptName:" + this.testScriptName + "; status:" + this.teststatus + "; duration:"
        + this.duration + "; passedLogs:" + this.passedCheckPoints + "; failedLogs:"
        + this.failedCheckPoints + "detailedLogs:" + this.detailedLogs;
  }
}
