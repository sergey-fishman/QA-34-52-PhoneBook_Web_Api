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

//    User fails to log in if he clicks on LOGIN button without interacting with input fields at all.
//    Expected result: alert message appears containing text 'Wrong email or password'.
    @Test
    public void loginNegativeEmptyFieldsNoClickTest() {
        loginPage.clickBtnLogin();
        softAssert.assertTrue(loginPage.closeAlert().contains
                ("Wrong email or password"));
        softAssert.assertAll();
    }

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
        softAssert.assertTrue(loginPage.closeAlert().contains
                ("Wrong email or password"));
        softAssert.assertAll();
    }
}
