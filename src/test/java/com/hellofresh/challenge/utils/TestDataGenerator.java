package com.hellofresh.challenge.utils;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public final class TestDataGenerator {
  private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
  private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
  private static final String NUMBER = "0123456789";

  private TestDataGenerator() {
  }

  private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
  private static final String DATA_FOR_RANDOM_STRING_WITHOUT_NUMBER = CHAR_LOWER + CHAR_UPPER;
  private static SecureRandom random = new SecureRandom();

  public static String generateRandomString(int length) {
    if (length < 1) throw new IllegalArgumentException();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      // 0-62 (exclusive), random returns 0-61
      int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
      char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
      sb.append(rndChar);
    }
    return sb.toString();
  }

  public static String generateRandomStringWithNumber(int length) {
    if (length < 1) throw new IllegalArgumentException();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int rndCharAt = random.nextInt(NUMBER.length());
      char rndChar = NUMBER.charAt(rndCharAt);
      sb.append(rndChar);
    }
    return sb.toString();
  }

  public static String generateRandomStringWithoutNumber(int length) {
    if (length < 1) throw new IllegalArgumentException();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING_WITHOUT_NUMBER.length());
      char rndChar = DATA_FOR_RANDOM_STRING_WITHOUT_NUMBER.charAt(rndCharAt);
      sb.append(rndChar);
    }
    return sb.toString();
  }

  public static int randomNumber(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }
}
