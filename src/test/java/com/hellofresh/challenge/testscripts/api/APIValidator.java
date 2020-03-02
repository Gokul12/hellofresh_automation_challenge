package com.hellofresh.challenge.testscripts.api;

import com.google.gson.internal.LinkedTreeMap;
import com.hellofresh.challenge.commons.LoggerClass;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.hellofresh.challenge.constants.Constant.DATA_VALIDATION;
import static com.hellofresh.challenge.constants.Constant.STATUS_CODE;


class APIValidator {
  private Map<String, String> dataValidations;
  private final String regex = "[^.]+$";
  private final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
  private String statusCode;

  APIValidator(APIRequest request) {
    this.statusCode = String.valueOf(request.getValidators().get(STATUS_CODE));
    //noinspection unchecked
    this.dataValidations =
        (LinkedTreeMap<String, String>) request.getValidators().get(DATA_VALIDATION);
  }

  private boolean verifyElements(HttpResponse<String> response,
      Map<String, String> verifyElements) {
    boolean status = true;
    for (Map.Entry<String, String> entry : verifyElements.entrySet()) {
      String expectedValue = String.valueOf(verifyElements.get(entry.getKey()));
      String actualValue = JsonPath.read(response.getBody(), entry.getKey()).toString();
      String fieldName = null;
      final Matcher matcher = pattern.matcher(entry.getKey());
      while (matcher.find()) {
        fieldName = matcher.group(0).toUpperCase();
      }
      if (expectedValue.contains(actualValue)) {
        LoggerClass.log(
            "Actual data " + actualValue + " in " + fieldName + " matches with the expected value "
                + actualValue);
      } else {
        LoggerClass.logError("Actual data " + actualValue + " in " + fieldName
            + " does not match with the expected value " + expectedValue);
        status = false;
      }

    }
    if (status && verifyElements.size() > 0) {
      LoggerClass
          .logSuccess("All data validations passed.Please click on Show Logs for detailed logs");
    }
    return status;
  }

  boolean validate(HttpResponse<String> response) {
    boolean isStatusCode = (this.statusCode.contains(String.valueOf(response.getStatus())));
    if (isStatusCode) {
      LoggerClass.logSuccess(
          "Actual Status Code: " + response.getStatus() + " matches with Expected Status Code: "
              + response.getStatus());
    } else {
      LoggerClass.logError(
          "Actual Status Code: " + response.getStatus() + "; Expected Status Code: "
              + this.statusCode);
    }
    return isStatusCode && verifyElements(response, dataValidations);
  }
}
