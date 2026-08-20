package ui_tests;

import dto.UserLombok;
import manager.AppManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.ContactsPage;
import pages.HomePage;
import pages.LoginPage;

import static utils.PropertiesReader.*;

public class LoginTests extends AppManager {
    LoginPage loginPage;
    ContactsPage contactsPage;
    SoftAssert softAssert = new SoftAssert();

    @BeforeMethod
    public void goToRegistrationLoginPage() {
        new HomePage(getDriver()).clickLinkLogin();
        loginPage = new LoginPage(getDriver());
    }

    @Test
    public void loginPositiveTest() {
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties", "username"))
                .password(getProperty("base.properties", "password"))
                .build();
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnLogin();
        contactsPage = new ContactsPage(getDriver());
        softAssert.assertTrue(contactsPage.isLinkContactsDisplayed(),
                "If False -> link Contacts is not displayed");
        softAssert.assertTrue(contactsPage.validateTextInNavbar
                ("CONTACTS"), "If False -> No 'CONTACTS' found in navbar");
        softAssert.assertTrue(contactsPage.isUrlContainsText("contacts"),
                "If False -> URL does not contain text string");
        softAssert.assertAll();
    }
//    TC 1
//    User fails to log in if he clicks on LOGIN button without interacting with input fields at all.
//    Expected result: alert message appears containing text 'Wrong email or password'.
    @Test
    public void loginNegativeEmptyFieldsNoClickTest() {
        loginPage.clickBtnLogin();
        softAssert.assertTrue(loginPage.getAlert().getText()
                .contains("Wrong email or password"));
        softAssert.assertAll();
    }
//    TC 2
//    User fails to log in if he leaves the input form empty and clicks LOGIN btn.
//    Expected result: alert message appears containing text 'Wrong email or password'.
    @Test
    public void loginNegativeEmptyFieldsTest() {
        UserLombok user = UserLombok.builder()
                .username("")
                .password("")
                .build();
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnLogin();
        softAssert.assertTrue(loginPage.getAlert().getText()
                .contains("Wrong email or password"));
        softAssert.assertAll();
    }
    /*
    TC 3 -> User fails to log in with empty username and valid password.
    Expected result: alert message appears containing text 'Wrong email or password'.
     */
    @Test
    public void loginNegativeEmptyUsernameTest() {
        UserLombok user = UserLombok.builder()
                .username("")
                .password(getProperty("base.properties","password"))
                .build();
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnLogin();
        softAssert.assertTrue(loginPage.getAlert().getText()
                .contains("Wrong email or password"));
        softAssert.assertAll();
    }
    /*
    TC 4 -> User fails to log in with empty password and valid username.
    Expected result: alert message appears containing text 'Wrong email or password'.
     */
    @Test
    public void loginNegativeEmptyPasswordTest() {
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties", "username"))
                .password("")
                .build();
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnLogin();
        softAssert.assertTrue(loginPage.getAlert().getText()
                .contains("Wrong email or password"));
        softAssert.assertAll();
    }
    /*
    TC 5 -> User fails to log in with invalid password and valid username.
    Expected result: alert message appears containing text 'Wrong email or password'.
    */
    @Test
    public void loginNegativeInvalidPasswordTest() {
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties", "username"))
                .password("Test012345&")
                .build();
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnLogin();
        softAssert.assertTrue(loginPage.getAlert().getText()
                .contains("Wrong email or password"));
        softAssert.assertAll();
    }
    /*
    TC 6 -> User fails to log in with valid password and invalid username.
    Expected result: alert message appears containing text 'Wrong email or password'.
    */
    @Test
    public void loginNegativeInvalidUsernameTest() {
        UserLombok user = UserLombok.builder()
                .username("faker.fake@yahoo.com")
                .password(getProperty("base.properties","password"))
                .build();
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnLogin();
        softAssert.assertTrue(loginPage.getAlert().getText()
                .contains("Wrong email or password"));
        softAssert.assertAll();
    }
}
