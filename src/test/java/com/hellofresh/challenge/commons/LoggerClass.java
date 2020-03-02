package com.hellofresh.challenge.commons;

import org.testng.Reporter;
import static com.hellofresh.challenge.constants.Constant.LOG_ERROR;
import static com.hellofresh.challenge.constants.Constant.LOG_SUCCESS;
import static com.hellofresh.challenge.constants.Constant.LOG_WARNING;
import static com.hellofresh.challenge.constants.Constant.NEW_LINE;
import static com.hellofresh.challenge.constants.Constant.REASON;

public class LoggerClass extends Reporter {

  public static void logError(String message, Exception e) {
    log(LOG_ERROR + message, 1, true);
    logShortMessage(e);
  }

  public static void logError(String message) {
    log(LOG_ERROR + message, 1, true);
  }

  public static void logSuccess(String message) {
    log(LOG_SUCCESS + message, 2, true);
  }

  public static void log(Exception e) {
    log(LOG_WARNING + e.getMessage(), 3, true);
  }

  public static void log(String message) {
    log(message, 4, true);
  }

  public static void log(String message, int level) {
    log(message, level, true);
  }

  public static void log(String message, int level, Exception e) {
    log(message, level, true);
  }

  static String logShortMessage(Exception e) {
    String msg = ((e.getMessage() != null) && e.getMessage().contains(NEW_LINE)) ?
        e.getMessage().split(NEW_LINE)[0] :
        e.getMessage();
    LoggerClass.log(REASON + msg);
    return msg;
  }

}
