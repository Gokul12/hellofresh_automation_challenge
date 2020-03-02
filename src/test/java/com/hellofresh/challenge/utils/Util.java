package com.hellofresh.challenge.utils;

import com.hellofresh.challenge.commons.LoggerClass;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public final class Util {

  private Util() {
  }

  public static Properties getProperties(String propertyPath) {
    Properties properties = null;
    try (InputStream input = new FileInputStream(propertyPath)) {
      properties = new Properties();
      properties.load(input);
    } catch (IOException ex) {
      LoggerClass.logError("IOException while reading from path: " + propertyPath + ex);
    }
    return properties;
  }
}
