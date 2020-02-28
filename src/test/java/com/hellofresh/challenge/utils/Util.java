package com.hellofresh.challenge.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Util {

  private Util() {
  }

  public static Properties getProperties(String propertyPath) {
    Properties properties = null;
    try (InputStream input = new FileInputStream(propertyPath)) {
      properties = new Properties();
      properties.load(input);
    } catch (IOException ex) {
      //TODO add logger
      ex.printStackTrace();
    }
    return properties;
  }
}
