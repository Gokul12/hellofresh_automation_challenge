package com.hellofresh.challenge.testscripts;

import com.hellofresh.challenge.reports.Report;
import com.hellofresh.challenge.reports.WebReport;
import static com.hellofresh.challenge.constants.Constant.REPORT_TEMPLATE_FILE_PATH;

public abstract class BaseTest {
  private Report report;

  public Report getReport() {
    return report;
  }

  private void setReport(Report report) {
    this.report = report;
  }

  protected void initReport(String suite) {
    setReport(new WebReport(REPORT_TEMPLATE_FILE_PATH, suite));
  }
}
