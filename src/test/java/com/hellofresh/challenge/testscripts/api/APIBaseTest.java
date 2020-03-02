package com.hellofresh.challenge.testscripts.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hellofresh.challenge.commons.LoggerClass;
import com.hellofresh.challenge.enums.Suite;
import com.hellofresh.challenge.testscripts.BaseTest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.List;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import static com.hellofresh.challenge.constants.Constant.API_TEST_DATA_BASE_PATH;


public class APIBaseTest extends BaseTest {

  @BeforeSuite
  public void init() {
    initReport(Suite.API.name());
  }

  @DataProvider(name = "dp")
  public Object[][] dataProvider(Method itr) {
    Object[][] returnValue = null;
    try {
      JsonElement jsonData =
          new JsonParser().parse(new FileReader(API_TEST_DATA_BASE_PATH + itr.getName() + ".json"));
      JsonElement dataSet = jsonData.getAsJsonObject().get("dataSet");
      List<APIRequest> testData = new Gson().fromJson(dataSet, new TypeToken<List<APIRequest>>() {
      }.getType());
      returnValue = new Object[testData.size()][1];
      int index = 0;
      for (Object[] each : returnValue) {
        each[0] = testData.get(index++);
      }
    } catch (FileNotFoundException e) {
      LoggerClass.logError("FileNotFoundException in data provider", e);
    }
    return returnValue;
  }
}
