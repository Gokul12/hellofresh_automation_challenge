package com.hellofresh.challenge.testscripts;

import com.hellofresh.challenge.reports.Report;
import com.hellofresh.challenge.reports.WebReport;
import org.testng.annotations.BeforeSuite;
import static com.hellofresh.challenge.constants.Constant.REPORT_TEMPLATE_FILE_NAME;
import static com.hellofresh.challenge.constants.Constant.RESOURCES_PATH;

public abstract class BaseTest {
  private Report report;

  public Report getReport() {
    return report;
  }

  private void setReport(Report report) {
    this.report = report;
  }

  @BeforeSuite
  public void init() {
    setReport(new WebReport(RESOURCES_PATH + REPORT_TEMPLATE_FILE_NAME));
  }
}

