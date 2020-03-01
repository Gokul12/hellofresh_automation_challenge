package com.hellofresh.challenge.testscripts;

import com.hellofresh.challenge.commons.TestHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.testng.annotations.Test;
import static org.testng.Assert.assertFalse;


public class WebTest extends BaseTest {
  String existingUserEmail = "hf_challenge_123456@hf123456.com";
  String existingUserPassword = "12345678";

  @Test
  public void signInTest() {
    TestHelper th = getTestObject();
    String timestamp = String.valueOf(new Date().getTime());
    String email = "hf_challenge_" + timestamp + "@hf" + timestamp.substring(7) + ".com";
    String name = "Firstname";
    String surname = "Lastname";
    th.getActions().click("Login");
    th.getActions().type("EmailAddress", email);
    th.getActions().click("CreateAccount");
    th.getActions().click("Gender");
    th.getActions().type("FirstName", name);
    th.getActions().type("LastName", surname);
    th.getActions().type("AccountPasswd", "Qwerty");
    th.getActions().selectByValue("Days", "1");
    th.getActions().selectByValue("Months", "1");
    th.getActions().selectByValue("Years", "2000");
    th.getActions().type("Company", "Company");
    th.getActions().type("Address1", "Qwerty, 123");
    th.getActions().type("Address2", "zxcvb");
    th.getActions().type("City", "Qwerty");
    th.getActions().selectByVisibleText("State", "Colorado");
    th.getActions().type("PostCode", "12345");
    th.getActions().type("Other", "Qwerty");
    th.getActions().type("Phone", "12345123123");
    th.getActions().type("PhoneMobile", "12345123123");
    th.getActions().type("Alias", "hf");
    th.getActions().click("SubmitAccount");
    th.getActions().takeScreenshot("signInTest");
    boolean status = assertSignIn(th, name + " " + surname);
    tearDown(th);
    assertFalse(status);
  }

  @Test
  public void logInTest() {
    TestHelper th = getTestObject();
    String fullName = "Joe Black";
    signIn(th);
    th.getActions().takeScreenshot("logInTest");
    boolean status = assertSignIn(th, fullName);
    tearDown(th);
    assertFalse(status);
  }

  @Test
  public void checkoutTest() {
    TestHelper th = getTestObject();
    signIn(th);
    th.getActions().click("Women");
    th.getActions().click("DressName", "Faded Short Sleeve T-shirts");
    th.getActions().click("DressName", "Faded Short Sleeve T-shirts");
    th.getActions().clickIfPresent("ClosePopUp");
    th.getActions().clickIfPresent("Submit");
    th.getActions().clickIfPresent("LayerCart");
    th.getActions().click("CartNavigationCheckout");
    th.getActions().click("ProcessAddress");
    th.getActions().click("UniformCGV");
    th.getActions().click("ProcessCarrier");
    th.getActions().click("BankWire");
    th.getActions().click("CartNavigation");
    th.getActions().takeScreenshot("checkoutTest");

    List<Boolean> status = new ArrayList<>();
    status.add(assertHeader(th, "ORDER CONFIRMATION"));
    status.add(th.getActions().isDisplayed("Shipping"));
    status.add(th.getActions().isDisplayed("Payment"));
    status.add(
        th.getActions().assertContains("OrderComplete", "Your order on My Store is complete."));
    status.add(assertURL(th, "controller=order-confirmation"));
    tearDown(th);
    assertFalse(status.contains(false));
  }

  private void signIn(TestHelper th) {
    th.getActions().click("Login");
    th.getActions().type("Email", existingUserEmail);
    th.getActions().type("AccountPasswd", existingUserPassword);
    th.getActions().click("SubmitLogin");
  }

  private boolean assertSignIn(TestHelper th, String name) {
    List<Boolean> status = new ArrayList<>();
    status.add(assertHeader(th, "MY ACCOUNT"));
    status.add(th.getActions().assertEquals("Account", name));
    status.add(th.getActions().assertContains("AccountInfo", "Welcome to your account."));
    status.add((th.getActions().isDisplayed("Logout")));
    status.add(assertURL(th, "controller=my-account"));
    return status.contains(false);
  }

  private boolean assertHeader(TestHelper th, String header) {
    return th.getActions().assertEquals("RegisterHeading", header);
  }

  private boolean assertURL(TestHelper th, String url) {
    return th.getActions().assertContains(th.getDriver(), url);
  }

  private void tearDown(TestHelper th) {
    th.getActions().quitDriver(th.getDriver());
  }
}
