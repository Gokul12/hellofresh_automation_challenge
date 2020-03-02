package com.hellofresh.challenge.commons;

import com.google.gson.Gson;
import com.hellofresh.challenge.testscripts.api.APIRequest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

public class APIActionKeywords {

  public HttpResponse<String> get(APIRequest request) {
    HttpResponse<String> jsonResponse = null;
    try {
      jsonResponse = Unirest.get(request.baseURL + request.endPoint).queryString(request.uriParams)
          .headers(request.headers).asString();
    } catch (UnirestException e) {
      LoggerClass.logError(
          "UnirestException in get request for endpoint: " + request.baseURL + request.endPoint, e);
    }
    return jsonResponse;
  }

  public HttpResponse<String> post(APIRequest request) {
    HttpResponse<String> jsonResponse = null;
    try {
      String rawBody = new Gson().toJsonTree(request.getBodyParams().get("params")).toString();
      jsonResponse = Unirest.post(request.baseURL + request.endPoint).queryString(request.uriParams)
          .headers(request.headers).body(new JSONObject(rawBody)).asString();
    } catch (UnirestException e) {
      LoggerClass.logError(
          "UnirestException in post request for endpoint: " + request.baseURL + request.endPoint,
          e);
    }
    return jsonResponse;
  }
}
