package api_tests;

import data_providers.ContactDataProvider;
import dto.ContactDto;
import dto.ErrorMessageDto;
import dto.ResponseMessageDto;
import dto.TokenDto;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.BaseApi;
import utils.ILogin;

import java.io.IOException;

import static utils.ContactFactory.*;

public class AddNewContactApiTests implements BaseApi, ILogin {
    TokenDto tokenDto;
    SoftAssert softAssert = new SoftAssert();

    @BeforeClass
    public void login() {
        tokenDto = loginGetToken();
    }

    @Test
    public void addNewContactPositiveTest() {
        ContactDto contact = positiveContact();
        System.out.println(contact);
        System.out.println(tokenDto.getToken());
        RequestBody requestBody = RequestBody.create(GSON.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + CONTACTS_URL)
                .addHeader(AUTH, tokenDto.getToken())
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        ResponseMessageDto message;
        try {
            message = GSON.fromJson(response.body().string(), ResponseMessageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(message);
        softAssert.assertEquals(response.code(), 200,
                "Status code equals 200");
        softAssert.assertTrue(message.getMessage().contains("Contact was added!"),
                "Response message contains 'Contact was added'");
        softAssert.assertAll();
    }

    @Test
    public void addNewContactWrongTokenNegativeTest() {
        ContactDto contact = positiveContact();
        System.out.println(contact);
        System.out.println(tokenDto.getToken());
        RequestBody requestBody = RequestBody.create(GSON.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + CONTACTS_URL)
                .addHeader(AUTH, "bla-bla-bla")
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        ResponseMessageDto message;
        try {
            message = GSON.fromJson(response.body().string(), ResponseMessageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(message);
        softAssert.assertEquals(response.code(), 401,
                "Status code equals 401");
        softAssert.assertTrue(message.getMessage().contains("WT strings must contain"),
                "Response message contains 'WT strings must contain'");
        softAssert.assertAll();
    }

    @Test
    public void addNewContactNoTokenNegativeTest() {
        ContactDto contact = positiveContact();
        System.out.println(contact);
        System.out.println(tokenDto.getToken());
        RequestBody requestBody = RequestBody.create(GSON.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + CONTACTS_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        ResponseMessageDto message;
        try {
            message = GSON.fromJson(response.body().string(), ResponseMessageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(message);
        softAssert.assertEquals(response.code(), 403,
                "Status code equals 403");
        softAssert.assertAll();
    }

    @Test(dataProvider = "dataProviderWrongNumber", dataProviderClass = ContactDataProvider.class)
    public void addNewContactWrongNumberNegativeTest(ContactDto contact) {
        System.out.println(contact);
        System.out.println(tokenDto.getToken());
        RequestBody requestBody = RequestBody.create(GSON.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + CONTACTS_URL)
                .addHeader(AUTH, tokenDto.getToken())
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        ErrorMessageDto message;
        try {
            message = GSON.fromJson(response.body().string(), ErrorMessageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(message);
        softAssert.assertEquals(response.code(), 400,
                "Status code equals 400");
        softAssert.assertTrue(message.getMessage().toString().contains
                ("Phone number must contain only digits! And length min 10, max 15!"));
        softAssert.assertAll();
    }

    @Test
    public void addNewContactEmptyNameNegativeTest() {
        ContactDto contact = positiveContact();
        contact.setName("");
        System.out.println(contact);
        System.out.println(tokenDto.getToken());
        RequestBody requestBody = RequestBody.create(GSON.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + CONTACTS_URL)
                .addHeader(AUTH, tokenDto.getToken())
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        ErrorMessageDto message;
        try {
            message = GSON.fromJson(response.body().string(), ErrorMessageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(message);
        softAssert.assertEquals(response.code(), 400,
                "Status code equals 400");
        softAssert.assertTrue(message.getMessage().toString().contains
                ("name=must not be blank"));
        softAssert.assertAll();
    }
}