package com.hellofresh.challenge.commons;

import org.openqa.selenium.remote.RemoteWebDriver;

public class TestHelper {
  private RemoteWebDriver driver;
  private ActionKeywords actions;

  public RemoteWebDriver getDriver() {
    return driver;
  }

  public void setDriver(RemoteWebDriver driver) {
    this.driver = driver;
  }

  public ActionKeywords getActions() {
    return actions;
  }

  public void setActions(ActionKeywords actions) {
    this.actions = actions;
  }
}
