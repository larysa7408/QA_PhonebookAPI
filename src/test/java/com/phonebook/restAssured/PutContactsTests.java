package com.phonebook.restAssured;

import com.phonebook.dto.ContactDto;
import com.phonebook.dto.ErrorDto;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class PutContactsTests extends TestBase {
    String id;

    @BeforeMethod
    public void precondition() {
        ContactDto contactDto = ContactDto.builder()
                .name("Grey")
                .lastName("Dicaprio")
                .email("dicaprio@gmail.com")
                .phone("123456789012")
                .address("New Jersey")
                .description("doctor")
                .build();

        String message = given()
                .header(AUTH, TOKEN)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .extract().path("message");

        String[] split = message.split(": ");
        id = split[1];
    }

    @Test
    public void editContactSuccessTest() {
        ContactDto contactDto = ContactDto.builder()
                .id(id)
                .name("Grey")
                .lastName("Dicaprio")
                .email("dicaprio@gmail.com")
                .phone("1234567890")
                .address("New Jersey")
                .description("doctor")
                .build();

        given()
                .header(AUTH, TOKEN)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body(containsString("Contact was updated"));
    }

    @Test
    public void editContactWithoutIDTest() {
        ContactDto contactDto = ContactDto.builder()
                .name("Grey")
                .lastName("Dicaprio")
                .email("dicaprio@gmail.com")
                .phone("1234567890")
                .address("New Jersey")
                .description("doctor")
                .build();

        given()
                .header(AUTH, TOKEN)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.id", containsString("must not be blank"));
    }

    @Test
    public void editContactUnauthorizedTest() {
        ContactDto contactDto = ContactDto.builder()
                .id(id)
                .name("Grey")
                .lastName("Dicaprio")
                .email("dicaprio@gmail.com")
                .phone("1234567890")
                .address("New Jersey")
                .description("doctor")
                .build();

        given()
                .header(AUTH, "qwagdfshtfyfgkhjb")
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("error", containsString("Unauthorized"));
    }

    @Test
    public void editDeletedContactTest() {

        given()
                .header(AUTH, TOKEN)
                .when()
                .delete("contacts/" + id)
                .then()
                .assertThat().statusCode(200);

        ContactDto contactDto = ContactDto.builder()
                .id(id)
                .name("Grey")
                .lastName("Dicaprio")
                .email("dicaprio@gmail.com")
                .phone("123456789012")
                .address("New Jersey")
                .description("doctor")
                .build();

        ErrorDto errorDto = given()
                .header(AUTH, TOKEN)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(404)
                .extract().response().as(ErrorDto.class);
        //.assertThat().body("error", containsString("Unauthorized"));

        System.out.println(errorDto.getError());
        System.out.println(errorDto.getMessage());
    }

}
