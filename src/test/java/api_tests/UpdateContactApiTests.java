package api_tests;

import data_providers.ContactDataProvider;
import dto.ContactDto;
import dto.ErrorMessageDto;
import dto.ResponseMessageDto;
import dto.TokenDto;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.BaseApi;
import utils.ILoginCreateContact;

import java.io.IOException;

import static utils.ContactFactory.*;

public class UpdateContactApiTests implements BaseApi, ILoginCreateContact {
    String contactID;
    TokenDto tokenDto;
    SoftAssert softAssert = new SoftAssert();

    @BeforeClass
    public void login() {
        tokenDto = loginGetToken();
        System.out.println(tokenDto);
        contactID = createContact();
        System.out.println(contactID);
    }

    @Test
    public void updateContactPositiveTest() {
        ContactDto newContact = positiveContact();
        newContact.setId(contactID);
        RequestBody requestBody = RequestBody.create(GSON.toJson(newContact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + CONTACTS_URL)
                .put(requestBody)
                .addHeader(AUTH, tokenDto.getToken())
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(), 200);
    }

    @Test(dataProvider = "dataProviderWrongNumber", dataProviderClass = ContactDataProvider.class)
    public void updateContactWrongNumberNegativeTest(ContactDto contact) {
        System.out.println(contact);
        contact.setId(contactID);
        RequestBody requestBody = RequestBody.create(GSON.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + CONTACTS_URL)
                .addHeader(AUTH, tokenDto.getToken())
                .put(requestBody)
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
    public void updateContactWrongTokenNegativeTest() {
        ContactDto newContact = positiveContact();
        newContact.setId(contactID);
        RequestBody requestBody = RequestBody.create(GSON.toJson(newContact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + CONTACTS_URL)
                .put(requestBody)
                .addHeader(AUTH, "tokenDto.getToken()")
                .build();
        Response response;
        ResponseMessageDto message;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            message = GSON.fromJson(response.body().string(), ResponseMessageDto.class);
            System.out.println("Message = " + message);
            System.out.println("Response code = " + response.code());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(), 401);
    }

    @Test
    public void updateContactNoTokenNegativeTest() {
        ContactDto newContact = positiveContact();
        newContact.setId(contactID);
        RequestBody requestBody = RequestBody.create(GSON.toJson(newContact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + CONTACTS_URL)
                .put(requestBody)
                .build();
        Response response;
        ResponseMessageDto message;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            message = GSON.fromJson(response.body().string(), ResponseMessageDto.class);
            System.out.println("Message = " + message);
            System.out.println("Response code = " + response.code());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(), 403);
    }

    @Test
    public void updateContactWrongIdNegativeTest() {
        ContactDto newContact = positiveContact();
        newContact.setId("bla-bla-bla");
        RequestBody requestBody = RequestBody.create(GSON.toJson(newContact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + CONTACTS_URL)
                .addHeader(AUTH, tokenDto.getToken())
                .put(requestBody)
                .build();
        Response response;
        ResponseMessageDto message;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            message = GSON.fromJson(response.body().string(), ResponseMessageDto.class);
            System.out.println("Message = " + message);
            System.out.println("Response code = " + response.code());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(), 400);
    }
}
