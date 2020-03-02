package com.hellofresh.challenge.testscripts.api;

import com.google.gson.internal.LinkedTreeMap;
import com.hellofresh.challenge.commons.APIActionKeywords;
import com.hellofresh.challenge.commons.LoggerClass;
import com.hellofresh.challenge.utils.TestDataGenerator;
import com.mashape.unirest.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Responsible for:
 * CreateBooking
 * GetBooking
 * GetBookingById(At least 2)
 * CreateBooking negative test cases
 * 1. CheckOut date Less than CheckIn date
 * 2. CheckOut date Less than CheckIn date
 * Update Booking
 * Delete Booking
 */
public class APITest extends APIBaseTest {
  private static final String BOOKINGS = "bookings";
  private static final String BOOKING = "booking";
  private static final String BOOKING_ID = "bookingid";
  private static final String ROOM_ID = "roomid";
  private final APIActionKeywords actions = new APIActionKeywords();

  @Test(dataProvider = "dp")
  public HttpResponse<String> createBooking_Valid(APIRequest apiRequest) {
    updateRandomRoomNo(apiRequest);
    HttpResponse<String> response = actions.post(apiRequest);
    validate(response, apiRequest);
    return response;
  }

  @Test(dataProvider = "dp")
  public void getBookingById_Valid(APIRequest apiRequest) {
    // Create booking before getBookingById
    HttpResponse<String> res = createRandomBooking(apiRequest);

    //Get bookingId from above and query
    Integer bookingId = (Integer) (new JSONObject(res.getBody()).get(BOOKING_ID));
    Integer roomId =
        (Integer) ((JSONObject) (new JSONObject(res.getBody()).get(BOOKING))).get(ROOM_ID);

    //Add bookingId and roomId to validations
    apiRequest.getValidators().put("$.bookings[0].roomid", roomId);
    apiRequest.getValidators().put("$.bookings[0].bookingid", bookingId);
    apiRequest.setEndPoint(apiRequest.getEndPoint() + bookingId);
    HttpResponse<String> response = actions.get(apiRequest);
    validate(response, apiRequest);
  }

  @Test(dataProvider = "dp", dependsOnMethods = "createAtLeastTwoBooking_Valid")
  public void getAtLeastTwoBooking_Valid(APIRequest apiRequest) {
    HttpResponse<String> response = actions.get(apiRequest);
    int size = ((JSONArray) (new JSONObject(response.getBody()).get(BOOKINGS))).length();
    //Validate if the response has at least 2 bookings
    if (size >= 2) {
      LoggerClass.logSuccess("At least 2 bookings are returned");
    } else {
      LoggerClass.logError("At least 2 bookings are not returned");
    }
    validate(response, apiRequest);
  }

  @Test(dataProvider = "dp")
  public void createAtLeastTwoBooking_Valid(APIRequest apiRequest) {
    HttpResponse<String> response = actions.post(apiRequest);
    validate(response, apiRequest);
  }

  @Test(dataProvider = "dp")
  public void createBooking_InValid_CheckOutLessThanCheckIn(APIRequest apiRequest) {
    HttpResponse<String> response = actions.post(apiRequest);
    validate(response, apiRequest);
  }

  @Test(dataProvider = "dp")
  public void createBooking_InValid_MoreThanOneBookingOnGivenDate(APIRequest apiRequest) {
    HttpResponse<String> response = actions.post(apiRequest);
    validate(response, apiRequest);
  }

  private HttpResponse<String> createRandomBooking(APIRequest apiRequest) {
    //Create booking using random roomId to handle 409
    updateRandomRoomNo(apiRequest);
    return actions.post(apiRequest);
  }

  private void validate(HttpResponse<String> response, APIRequest apiRequest) {
    APIValidator validator = new APIValidator(apiRequest);
    Assert.assertTrue(validator.validate(response));
  }

  @SuppressWarnings("unchecked")
  private void updateRandomRoomNo(APIRequest apiRequest) {
    LinkedTreeMap<String, Object> bodyParams =
        (LinkedTreeMap<String, Object>) apiRequest.getBodyParams().get("params");
    bodyParams.put(ROOM_ID, TestDataGenerator.randomNumber(1, 10000));
  }
}
