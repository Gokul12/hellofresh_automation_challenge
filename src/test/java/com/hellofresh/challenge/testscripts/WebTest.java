package com.hellofresh.challenge.testscripts;

import com.hellofresh.challenge.commons.TestHelper;
import java.util.Date;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;


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

    assertSignIn(th, name + " " + surname);
    tearDown(th);
  }

  @Test
  public void logInTest() {
    TestHelper th = getTestObject();
    String fullName = "Joe Black";
    signIn(th);
    assertSignIn(th, fullName);
    tearDown(th);
  }

  @Test
  public void checkoutTest() {
    TestHelper th = getTestObject();
    signIn(th);
    th.getActions().click("Women");
    th.getActions().click("DressName", "Faded Short Sleeve T-shirts");
    th.getActions().click("DressName", "Faded Short Sleeve T-shirts");
    th.getActions().clickIfPresent("Submit");
    th.getActions().clickIfPresent("LayerCart");
    th.getActions().click("CartNavigationCheckout");
    th.getActions().click("ProcessAddress");
    th.getActions().click("UniformCGV");
    th.getActions().click("ProcessCarrier");
    th.getActions().click("BankWire");
    th.getActions().click("CartNavigation");

    assertHeader(th, "ORDER CONFIRMATION");
    assertTrue(th.getActions().isDisplayed("Shipping"));
    assertTrue(th.getActions().isDisplayed("Payment"));
    assertTrue(
        th.getActions().assertContains("OrderComplete", "Your order on My Store is complete."));
    assertTrue(assertURL(th, "controller=order-confirmation"));
    tearDown(th);
  }

  private void signIn(TestHelper th) {
    th.getActions().click("Login");
    th.getActions().type("Email", existingUserEmail);
    th.getActions().type("AccountPasswd", existingUserPassword);
    th.getActions().click("SubmitLogin");
  }

  private void assertSignIn(TestHelper th, String name) {
    assertHeader(th, "MY ACCOUNT");
    th.getActions().assertEquals("Account", name);
    assertTrue(th.getActions().assertContains("AccountInfo", "Welcome to your account."));
    assertTrue(th.getActions().isDisplayed("Logout"));
    assertTrue(assertURL(th, "controller=my-account"));
  }

  private void assertHeader(TestHelper th, String header) {
    th.getActions().assertEquals("RegisterHeading", header);
  }

  private boolean assertURL(TestHelper th, String url) {
    return th.getActions().assertContains(th.getDriver(), url);
  }

  private void tearDown(TestHelper th) {
    th.getActions().quitDriver(th.getDriver());
  }
}
