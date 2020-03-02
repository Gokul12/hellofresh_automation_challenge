package com.hellofresh.challenge.testscripts.web;

import com.hellofresh.challenge.commons.ActionKeywords;
import com.hellofresh.challenge.commons.TestHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;
import static com.hellofresh.challenge.utils.TestDataGenerator.generateRandomString;
import static com.hellofresh.challenge.utils.TestDataGenerator.generateRandomStringWithNumber;
import static com.hellofresh.challenge.utils.TestDataGenerator.generateRandomStringWithoutNumber;
import static com.hellofresh.challenge.utils.TestDataGenerator.randomNumber;
import static org.testng.Assert.assertFalse;

public class WebTest extends WebBaseTest {

  @Test
  public void signInTest() {
    TestHelper helper = getTestHelper();
    ActionKeywords actions = helper.getActions();
    RemoteWebDriver driver = helper.getDriver();
    String timestamp = String.valueOf(new Date().getTime());
    String email = generateRandomString(4) + timestamp + "@hf" + timestamp.substring(7) + ".com";
    String name = generateRandomStringWithoutNumber(7);
    String surname = generateRandomStringWithoutNumber(7);
    actions.click("Login");
    actions.type("EmailAddress", email);
    actions.click("CreateAccount");
    actions.click("Gender");
    actions.type("FirstName", name);
    actions.type("LastName", surname);
    actions.type("AccountPasswd", generateRandomString(5));
    actions.selectByValue("Days", "1");
    actions.selectByValue("Months", "1");
    actions.selectByValue("Years", "2000");
    actions.type("Company", generateRandomString(5));
    actions.type("Address1", generateRandomString(7));
    actions.type("Address2", generateRandomString(3));
    actions.type("City", generateRandomString(5));
    actions.selectByVisibleText("State", "Colorado");
    actions.type("PostCode", generateRandomStringWithNumber(5));
    actions.type("Other", generateRandomString(5));
    actions.type("Phone", generateRandomStringWithNumber(11));
    actions.type("PhoneMobile", generateRandomStringWithNumber(12));
    actions.type("Alias", generateRandomString(3));
    actions.click("SubmitAccount");
    actions.takeScreenshot("signInTest");
    boolean status = assertSignIn(driver, actions, name + " " + surname);
    tearDown(driver, actions);
    assertFalse(status);
  }

  @Test
  public void logInTest() {
    TestHelper helper = getTestHelper();
    ActionKeywords actions = helper.getActions();
    RemoteWebDriver driver = helper.getDriver();
    String fullName = "Joe Black";
    signIn(actions);
    actions.takeScreenshot("logInTest");
    boolean status = assertSignIn(driver, actions, fullName);
    tearDown(driver, actions);
    assertFalse(status);
  }

  @Test(dataProvider = "dp")
  public void checkoutTest(Map<String, String> testData) {
    TestHelper helper = getTestHelper();
    ActionKeywords actions = helper.getActions();
    RemoteWebDriver driver = helper.getDriver();
    signIn(actions);
    actions.click("Women");
    actions.click("DressName", testData.get("dressName"));
    actions.click("DressName", testData.get("dressName"));
    actions.clickIfPresent("ClosePopUp", 3);
    actions.type("Quantity", testData.get("quantity"));
    actions.selectByValue("Size", testData.get("size"));
    actions.clickIfPresent("Colour", testData.get("colour"));
    actions.clickIfPresent("Submit");
    actions.clickIfPresent("LayerCart");
    actions.click("CartNavigationCheckout");
    actions.click("ProcessAddress");
    actions.click("UniformCGV");
    actions.click("ProcessCarrier");
    actions.click("BankWire");
    actions.click("CartNavigation");
    actions.takeScreenshot("checkoutTest");

    boolean status = assertCheckOut(driver, actions);
    tearDown(driver, actions);
    assertFalse(status);
  }

  private void signIn(ActionKeywords actions) {
    String existingUserEmail = "hf_challenge_123456@hf123456.com";
    String existingUserPassword = "12345678";
    actions.click("Login");
    actions.type("Email", existingUserEmail);
    actions.type("AccountPasswd", existingUserPassword);
    actions.click("SubmitLogin");
  }

  private boolean assertSignIn(RemoteWebDriver driver, ActionKeywords actions, String name) {
    List<Boolean> status = new ArrayList<>();
    status.add(assertHeader(actions, "MY ACCOUNT"));
    status.add(actions.assertEquals("Account", name));
    status.add(actions.assertContains("AccountInfo", "Welcome to your account."));
    status.add((actions.isDisplayed("Logout")));
    status.add(assertURL(driver, actions, "controller=my-account"));
    return status.contains(false);
  }

  private boolean assertCheckOut(RemoteWebDriver driver, ActionKeywords actions) {
    List<Boolean> status = new ArrayList<>();
    status.add(assertHeader(actions, "ORDER CONFIRMATION"));
    status.add(actions.isDisplayed("Shipping"));
    status.add(actions.isDisplayed("Payment"));
    status.add(actions.assertContains("OrderComplete", "Your order on My Store is complete."));
    status.add(assertURL(driver, actions, "controller=order-confirmation"));
    return status.contains(false);
  }

  private boolean assertHeader(ActionKeywords actions, String header) {
    return actions.assertEquals("RegisterHeading", header);
  }

  private boolean assertURL(RemoteWebDriver driver, ActionKeywords actions, String url) {
    return actions.assertContains(driver, url);
  }

  private void tearDown(RemoteWebDriver driver, ActionKeywords actions) {
    actions.quitDriver(driver);
  }
}
