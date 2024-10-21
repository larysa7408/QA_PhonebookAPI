package com.phonebook.restAssured;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteAllContactsTests extends TestBase {

    @Test
    public void deleteAllContactsSuccessTest() {
//        String message =
        given()
                .header(AUTH, TOKEN)
                .when()
                .delete("contacts/clear")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", equalTo("All contacts was deleted!"));
//                .extract().path("message");

    }
}
