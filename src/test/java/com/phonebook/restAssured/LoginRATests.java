package com.phonebook.restAssured;

import com.phonebook.dto.AuthRequestDto;
import com.phonebook.dto.AuthResponseDto;
import com.phonebook.dto.ErrorDto;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class LoginRATests extends TestBase {

    AuthRequestDto requestDto = AuthRequestDto.builder()
            .username("larisa@gmail.com")
            .password("Qwerty123!")
            .build();
    SoftAssert softAssert=new SoftAssert();

    @Test
    public void loginSuccessTest() {
        AuthResponseDto dto = given()
                .contentType("application/json")
                .body(requestDto)
                .when()
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
                .when()
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(200)
                .body(containsString("token"))
                .extract().path("token");

        System.out.println(responseToken);
    }

    @Test
    public void loginWrongPasswordTest() {
        ErrorDto errorDto = given()
                .body(AuthRequestDto.builder()
                        .username("larisa@gmail.com")
                        .password("Qwerty153!")
                        .build())
                .contentType(ContentType.JSON)
                .when()
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(401)
                .extract().response().as(ErrorDto.class);
        softAssert.assertEquals(errorDto.getError(),"Unauthorized");
        softAssert.assertEquals(errorDto.getMessage(),"Login or Password incorrect");
        softAssert.assertAll();
    }

    @Test
    public void loginWrongPasswordPerfectTest() {
        given()
                .body(AuthRequestDto.builder()
                        .username("larisa@gmail.com")
                        .password("Qwerty153!")
                        .build())
                .contentType(ContentType.JSON)
                .when()
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message",containsString("Login or Password incorrect"))
                .assertThat().body("error", equalTo("Unauthorized"));
    }
}
