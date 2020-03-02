package com.hellofresh.challenge.testscripts.api;


import java.util.Map;

public class APIRequest {
  private Map<String, Object> validations;
  public String baseURL;
  public String endPoint;
  public Map<String, String> headers;
  public Map<String, Object> uriParams;
  Map<String, Object> bodyParams;

  Map<String, Object> getValidators() {
    return validations;
  }

  public void setValidators(Map<String, Object> validators) {
    this.validations = validators;
  }

  public Map<String, Object> getBodyParams() {
    return bodyParams;
  }

  public void setBodyParams(Map<String, Object> bodyParams) {
    this.bodyParams = bodyParams;
  }

  public String getBaseURL() {
    return baseURL;
  }

  void setBaseURL(String baseURL) {
    this.baseURL = baseURL;
  }

  String getEndPoint() {
    return endPoint;
  }

  void setEndPoint(String endPoint) {
    this.endPoint = endPoint;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public Map<String, Object> getUriParams() {
    return uriParams;
  }

  void setUriParams(Map<String, Object> uriParams) {
    this.uriParams = uriParams;
  }

  @Override
  public String toString() {
    return "APIRequest{" + "baseURL='" + baseURL + '\'' + ", endPoint='" + endPoint + '\''
        + ", headers=" + headers + ", uriParams=" + uriParams + ", validations=" + validations
        + ", bodyParams=" + bodyParams + '}';
  }
}
