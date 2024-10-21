package com.phonebook.restAssured;

import com.phonebook.dto.ContactDto;
import com.phonebook.dto.ErrorDto;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class AddContactTests extends TestBase{

    ContactDto contactDto = ContactDto.builder()
            .name("Grey")
            .lastName("Dicaprio")
            .email("dicaprio@gmail.com")
            .phone("123456789012")
            .address("New Jersey")
            .description("doctor")
            .build();

    @Test
    public void addContactSuccessTest() {

        given()
                .header(AUTH, TOKEN)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body(containsString("Contact was added!"));
//                .extract().path("message");

//        System.out.println(message);
//        Contact was added! ID: d214993a-835d-4723-8a88-9ed407deea8f
    }

    @Test
    public void addContactWithoutNameTest() {

        ContactDto contactDto1 = ContactDto.builder()
                .lastName("Dicaprio")
                .email("dicaprio@gmail.com")
                .phone("123456789012")
                .address("New Jersey")
                .description("doctor")
                .build();

        ErrorDto errorDto = given()
                .header(AUTH, TOKEN)
                .body(contactDto1)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .extract().response().as(ErrorDto.class);

        Assert.assertTrue(errorDto.getMessage().toString().contains("name=must not be blank"));
    }

    @Test
    public void addContactWithInvalidPhoneTest() {

        ContactDto contactDto2 = ContactDto.builder()
                .name("Grey")
                .lastName("Dicaprio")
                .email("dicaprio@gmail.com")
                .phone("12345678")
                .address("New Jersey")
                .description("doctor")
                .build();

//        ErrorDto errorDto =
        given()
                .header(AUTH, TOKEN)
                .body(contactDto2)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.phone",
                        containsString("Phone number must contain only digits! And length min 10, max 15!"));
//                .extract().response().as(ErrorDto.class);

        //Assert.assertTrue(errorDto.getMessage().toString().contains("phone=Phone number must contain only digits! And length min 10, max 15!"));
    }
}
