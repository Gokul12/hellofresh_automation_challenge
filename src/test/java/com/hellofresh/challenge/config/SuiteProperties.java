package com.hellofresh.challenge.config;

public class SuiteProperties {
  private String browser;
  private String environment;
  private String platform;
  private boolean isParallel;

  public String getBrowser() {
    return browser;
  }

  public void setBrowser(String browser) {
    this.browser = browser;
  }

  public String getEnvironment() {
    return environment;
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public boolean getIsParallel() {
    return isParallel;
  }

  public void setIsParallel(boolean isParallel) {
    this.isParallel = isParallel;
  }
}
