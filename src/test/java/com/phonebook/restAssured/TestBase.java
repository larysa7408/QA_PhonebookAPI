package com.phonebook.restAssured;

import com.phonebook.dto.AuthRequestDto;
import com.phonebook.dto.AuthResponseDto;
import com.phonebook.dto.ErrorDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestBase {
    public static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibGFyaXNhQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNzI5ODcwNzEyLCJpYXQiOjE3MjkyNzA3MTJ9.LyFq4Z9kUIBXgoqTZjYkt3j9DztLoUbVi8WA-NL8sAk";
    public static final String AUTH = "Authorization";

    @BeforeMethod
    public void init() {
        System.err.close();

        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    AuthRequestDto requestDto = AuthRequestDto.builder()
            .username("larisa@gmail.com")
            .password("Qwerty123!")
            .build();

    @Test
    public void loginSuccessTest() {

        AuthResponseDto dto = given()
                .contentType("application/json")
                .body(requestDto)
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(AuthResponseDto.class);

        System.out.println(dto.getToken());
    }

    @Test
    public void loginSuccessTest2() {

        String responseToken = given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(200)
                .body(containsString("token"))
                .extract().path("token");

        System.out.println(responseToken);
    }
}


