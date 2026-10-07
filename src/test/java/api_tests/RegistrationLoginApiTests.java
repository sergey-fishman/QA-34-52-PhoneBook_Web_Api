package api_tests;

import data_providers.UserDataProvider;
import dto.UserLombok;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseApi;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static utils.UserFactory.*;
import static utils.PropertiesReader.*;

public class RegistrationLoginApiTests implements BaseApi {

    @Test
    public void registrationApiPositiveTest() {
        UserLombok user = positiveUser();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(GSON.toJson(user));
        System.out.println(response);
        Assert.assertEquals(response.code(), 200);
    }

    @Test
    public void registrationApiWrongPasswordNegativeTest() {
        UserLombok user = positiveUser();
        user.setPassword("qwerty123!");
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        Assert.assertEquals(response.code(), 400);
    }

    @Test
    public void registrationApiDuplicateUserNegativeTest() {
        UserLombok user = positiveUser();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            OK_HTTP_CLIENT.newCall(request).execute();
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        Assert.assertEquals(response.code(), 409);
    }

    @Test
    public void registrationApiWrongFormatNegativeTest() {
        UserLombok user = positiveUser();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), TEXT);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        Assert.assertEquals(response.code(), 500);
    }

    @Test
    public void loginApiPositiveTest() {
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties", "username"))
                .password(getProperty("base.properties", "password"))
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+LOGIN_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(GSON.toJson(user));
        System.out.println(response);
        Assert.assertEquals(response.code(), 200);
    }
    @Test
    public void loginApiWrongFormatPositiveTest() {
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties", "username"))
                .password(getProperty("base.properties", "password"))
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), TEXT);
        Request request = new Request.Builder()
                .url(BASE_URL+LOGIN_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(GSON.toJson(user));
        System.out.println(response);
        Assert.assertEquals(response.code(), 500);
    }

    @Test
    public void loginApiWrongPasswordNegativeTest() {
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties", "username"))
                .password("Qwerty12345!")
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+LOGIN_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        Assert.assertEquals(response.code(), 401);
    }

    @Test
    public void loginApiWrongUsernameNegativeTest() {
        UserLombok user = UserLombok.builder()
                .username("faker.fake@yahoo.com")
                .password(getProperty("base.properties", "password"))
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+LOGIN_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(GSON.toJson(user));
        System.out.println(request);
        System.out.println(response);
        Assert.assertEquals(response.code(), 401);
    }

    @Test
    public void loginApiWrongKeyUsernameNegativeTest() {
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties", "username"))
                .password(getProperty("base.properties", "password"))
                .build();
        Map<String,String> invalidJson = new HashMap<>();
        invalidJson.put("email", user.getUsername());
        invalidJson.put("password", user.getPassword());
        RequestBody requestBody = RequestBody.create(GSON.toJson(invalidJson), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+LOGIN_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(GSON.toJson(invalidJson));
        System.out.println(request);
        try {
            System.out.println(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(), 400);
    }
    // (username=camie.katzmann@gmail.c, password=Qwerty123$)) FAILED expected [400] but found [200]
    // (username=camie.katzmann@gmail, password=Qwerty123$)) FAILED expected [400] but found [200]
    @Test(dataProvider = "dataProviderWrongUsername",
            dataProviderClass = UserDataProvider.class)
    public void registrationApiWrongUsernameNegativeTest(UserLombok user) {
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        Assert.assertEquals(response.code(), 400);
    }
    // (username=jerrold.thiel@hotmail.com, password=Qwarty 12345$)) FAILED expected [400] but found [200]
    // (username=ray.mayer@yahoo.com, password=Qwarty123456789$)) FAILED expected [400] but found [200]
    @Test(dataProvider = "dataProviderWrongPassword",
            dataProviderClass = UserDataProvider.class)
    public void registrationApiWrongPasswordNegativeTest(UserLombok user) {
        user.setUsername(faker.internet().emailAddress());
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        Assert.assertEquals(response.code(), 400);
    }

    @Test
    public void registrationApiNullValuesNegativeTest() {
        UserLombok user = UserLombok.builder()
                .username(null).password(null).build();
        RequestBody requestBody = RequestBody.create(GSON_WITH_NULLS.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(GSON_WITH_NULLS.toJson(user));
        System.out.println(request);
        try {
            System.out.println(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(), 400);
    }
    // method GET
    @Test
    public void registrationApiWrongMethodNegativeTest() {
        UserLombok user = positiveUser();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION_URL)
                .get()
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("requestBody = "+requestBody);
        System.out.println("request = "+request);
        System.out.println("response = "+response);
        Assert.assertEquals(response.code(), 403);
    }
    /*
    Пустой JSON ({}) — это валидный JSON-объект, в котором просто нет полей.
    Обычно серверы на такой запрос отвечают кодом 400 Bad Request или 422 Unprocessable Entity,
    так как обязательные поля отсутствуют.
     */
    @Test
    public void registrationApiEmptyJsonBodyNegativeTest() {
        RequestBody requestBody = RequestBody.create("{}", JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("requestBody = "+requestBody);
        System.out.println("request = "+request);
        System.out.println("response = "+response);
        Assert.assertEquals(response.code(), 400);
    }
}
