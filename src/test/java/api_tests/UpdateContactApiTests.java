package api_tests;

import dto.ContactDto;
import dto.TokenDto;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.BaseApi;
import utils.ICreateContact;
import utils.ILogin;

import java.io.IOException;

import static utils.ContactFactory.*;

public class UpdateContactApiTests implements BaseApi, ILogin, ICreateContact {
    String contactID;
    TokenDto tokenDto;

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
}
