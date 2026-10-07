package api_tests;

import dto.ContactDto;
import dto.ContactsDto;
import dto.TokenDto;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.BaseApi;
import utils.ILogin;

import java.io.IOException;

public class GetAllContacts implements BaseApi, ILogin {
    TokenDto tokenDto;

    @BeforeClass
    public void login() {
        tokenDto = loginGetToken();
    }

    @Test
    public void getAllUserContactsPositiveTest() {
        Request request = new Request.Builder()
                .url(BASE_URL + CONTACTS_URL)
                .get()
                .addHeader(AUTH, tokenDto.getToken())
                .build();
        Response response;
        ContactsDto contacts;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            contacts = GSON.fromJson(response.body().string(), ContactsDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(contacts);
        Assert.assertEquals(response.code(), 200);
    }
}
