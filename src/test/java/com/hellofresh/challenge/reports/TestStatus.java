package com.hellofresh.challenge.reports;

import java.util.HashMap;
import java.util.Map;

public enum TestStatus {
  PASS(1), FAIL(2), SKIP(3), PASS_WITH_WARNINGS(4);

  private int value;

  private static Map<Integer, TestStatus> map = new HashMap<>();

  static {
    for (TestStatus legEnum : TestStatus.values()) {
      map.put(legEnum.value, legEnum);
    }
  }

  TestStatus(final int value) {
    this.value = value;
  }

  public static TestStatus valueOf(int legNo) {
    return map.get(legNo);
  }


}
