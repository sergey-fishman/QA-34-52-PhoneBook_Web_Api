package ui_tests;

import data_providers.UserDataProvider;
import dto.UserLombok;
import manager.AppManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.ContactsPage;
import pages.HomePage;
import pages.LoginPage;
import utils.PropertiesReader;

import static utils.UserFactory.*;
import static utils.PropertiesReader.*;


import java.util.Random;

public class RegistrationTests extends AppManager {
    LoginPage loginPage;
    ContactsPage contactsPage;
    SoftAssert softAssert = new SoftAssert();

    @BeforeMethod
    public void goToRegistrationLoginPage() {
        new HomePage(getDriver()).clickLinkLogin();
        loginPage = new LoginPage(getDriver());
    }

    @Test
    public void registrationPositiveTest() {
        int i = new Random().nextInt(1000);
        UserLombok user = UserLombok.builder()
                .username("ser.gey" + i + "@ya.ru")
                .password("Qwerty1234!")
                .build();
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();

        Assert.assertTrue(new ContactsPage(getDriver())
                .validateTextInMessageNoContacts("No Contacts here!"));
    }

    @Test
    public void registrationPositiveTestWithFaker() {
        UserLombok user = positiveUser();
        System.out.println(user);
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        contactsPage = new ContactsPage(getDriver());
        Assert.assertTrue(contactsPage
                .validateTextInMessageNoContacts("No Contacts here!"));
    }

    /*
    TC 1 -> User fails to register if he bypasses input fields completely.
    Expected result -> Reg fail, Alert message appears containing text
    'Wrong email or password format'.
    */
    @Test
    public void registrationNegativeEmptyAllFieldsNoClickTest() {
        loginPage.clickBtnRegistration();
        Assert.assertTrue(loginPage.closeAlert()
                .contains("Wrong email or password format"));
    }

    /*
    TC 2 -> User fails to register with empty input fields.
    Expected result -> Reg fail, Alert message appears containing text
    'Wrong email or password format'.
     */
    @Test
    public void registrationNegativeEmptyAllFieldsTest() {
        UserLombok user = UserLombok.builder()
                .username("")
                .password("")
                .build();
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        softAssert.assertTrue(loginPage.getAlert()
                .getText().contains("Wrong email or password format"));
        softAssert.assertAll();
    }

    /*
    TC 3 -> User fails to register with empty username field.
    Expected result -> Reg fail, Alert message appears containing text
    'Wrong email or password format'.
    */
    @Test
    public void registrationNegativeEmptyUsernameTest() {
        UserLombok user = positiveUser();
        user.setUsername("");
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        softAssert.assertTrue(loginPage.getAlert()
                .getText().contains("Wrong email or password format"));
        softAssert.assertAll();
    }

    /*
    TC 3 -> User fails to register with empty password field.
    Expected result -> Reg fail, Alert message appears containing text
    'Wrong email or password format'.
    */
    @Test
    public void registrationNegativeEmptyPasswordTest() {
        UserLombok user = positiveUser();
        user.setPassword("");
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        softAssert.assertTrue(loginPage.getAlert()
                .getText().contains("Wrong email or password format"));
        softAssert.assertAll();
    }

    /*
    TC 4 -> User fails to register with already existing user data.
    Expected result -> Reg fail, Alert message appears containing text
    'User already exist'.
    */
    @Test
    public void registrationNegativeWithAlreadyExistingDataTest() {
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties","username"))
                .password(getProperty("base.properties","password"))
                .build();
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        softAssert.assertTrue(loginPage.getAlert()
                .getText().contains("User already exist"));
        softAssert.assertAll();
    }

    /*
    TC 5 -> User fails to register with incorrect password format.
    Expected result -> Reg fail, Alert message appears containing text
    'Wrong email or password format'.
    */
    @Test(dataProvider = "dataProviderWrongPassword",
            dataProviderClass = UserDataProvider.class)
    public void registrationNegativeWrongPasswordTest(UserLombok user) {
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        softAssert.assertTrue(loginPage.getAlert()
                .getText().contains("Wrong email or password format"),
                "If False -> not found message 'Wrong email or password format'");
        softAssert.assertAll();
    }

    /*
    TC 6 -> User fails to register with incorrect username format.
    Expected result -> Reg fail, Alert message appears containing text
    'Wrong email or password format'.
    Actual result -> 4 passed, 2 failed with the following data:
    username=camie.kertzmann@gmail.c
    username=camie.kertzmann@gmail
    НАЙДЕНО ДВА БАГА
    */
    @Test(dataProvider = "dataProviderWrongUsername",
            dataProviderClass = UserDataProvider.class)
    public void registrationNegativeWrongUsernameTest(UserLombok user) {
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        softAssert.assertTrue(loginPage.getAlert()
                        .getText().contains("Wrong email or password format"),
                "If False -> not found message 'Wrong email or password format'");
        softAssert.assertAll();
    }


/*
    @Test
    public void testMethod() {
        new HomePage(getDriver()).method();
    }
    @Test
    public void textAjaxMethod() {
        new HomePage(getDriver()).ajaxMethod();
    }
 */
}
