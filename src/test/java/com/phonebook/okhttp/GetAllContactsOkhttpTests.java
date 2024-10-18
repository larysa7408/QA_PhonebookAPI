package com.phonebook.okhttp;

import com.google.gson.Gson;
import com.phonebook.dto.AllContactsDto;
import com.phonebook.dto.ContactDto;
import com.phonebook.dto.ErrorDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetAllContactsOkhttpTests {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibGFyaXNhQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNzI5ODY2NDc1LCJpYXQiOjE3MjkyNjY0NzV9.DTuZ6d8WdJqt8kINT4gmXl5fg0LliGFerT_EKTMMCEE";

    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    @Test
    public void getAllContactsSuccessTest() throws IOException {

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

        AllContactsDto contactsDto = gson.fromJson(response.body().string(), AllContactsDto.class);
        List<ContactDto> contacts = contactsDto.getContacts();

        for (ContactDto c : contacts) {
            System.out.println(c.getId());
            System.out.println(c.getName());
            System.out.println(c.getPhone());
        }
    }

    @Test
    public void getAllContactsWithWrongToken() throws IOException {

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization", "adsdjhjhytye")
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 401);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getError(), "Unauthorized");
    }
}
