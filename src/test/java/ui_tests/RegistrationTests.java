package ui_tests;

import data_providers.UserDataProvider;
import dto.UserLombok;
import manager.AppManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ContactsPage;
import pages.HomePage;
import pages.LoginPage;

import static utils.UserFactory.*;

import java.util.Random;

public class RegistrationTests extends AppManager {
    LoginPage loginPage;
    ContactsPage contactsPage;

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
        contactsPage.printMessageNoContacts();
    }

    @Test
    public void registrationNegativeEmptyAllFieldsTest() {
        loginPage.clickBtnRegistration();
        Assert.assertTrue(loginPage.closeAlert()
                .contains("Wrong email or password format"));
    }

    @Test
    public void registrationNegativeEmptyUsernameTest() {
        UserLombok user = positiveUser();
        user.setUsername("");
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        Assert.assertTrue(loginPage.closeAlert()
                .contains("Wrong email or password format"));
    }

    @Test
    public void registrationNegativeEmptyPasswordTest() {
        UserLombok user = positiveUser();
        user.setPassword("");
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        Assert.assertTrue(loginPage.closeAlert()
                .contains("Wrong email or password format"));
    }

    @Test(dataProvider = "dataProviderWrongPasswordOrEmail",
            dataProviderClass = UserDataProvider.class)
    public void registrationNegativeWrongPasswordTest(UserLombok user) {
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        Assert.assertTrue(loginPage.closeAlert()
                .contains("Wrong email or password format"));
    }

/*
почему мы передаем в наш дата провайдер и имейл, и пароль, если
меняется только пароль?
для негативного теста для имейла нам нужно будет создавать
отдельный метод в классе провайдер?
 */

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
