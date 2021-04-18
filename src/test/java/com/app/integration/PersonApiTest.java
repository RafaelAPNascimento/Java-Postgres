package com.app.integration;

import com.app.model.Person;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
public class PersonApiTest {

    private final static String BASE_URI = "http://localhost:8080/java-postgres-be/api";
    private final static String BASE_PATH = "/person";

    @Test
    public void shouldGetAll() {

        String path = "/person/all";

        given().baseUri(BASE_URI)
                .basePath(path)
                .request()
                .log().all()
                .when()
                .get()
                .then().log().all()
                .assertThat().statusCode(SC_OK);
    }

    @Test
    public void shouldSavePerson() {

        Person person = Person.builder().age(30).name("Rafael").build();

        given().baseUri(BASE_URI)
                .basePath(BASE_PATH)
                .contentType(JSON)
                .request()
                .body(person)
                .log().all()
                .when().post()
                .then()
                .log().all()
                .assertThat().statusCode(SC_CREATED);
    }

    @Test
    public void shouldGetById() {

        String path = "/person/{id}";

        given().baseUri(BASE_URI)
                .basePath(path)
                .pathParam("id", 3L)
                .request()
                .log().all()
                .when()
                .get()
                .then().log().all()
                .assertThat().statusCode(SC_OK);
    }

    @Test
    public void shouldDelete() {

        given().baseUri(BASE_URI)
                .basePath(BASE_PATH)
                .queryParam("id", 5L)
                .request()
                .log().all()
                .when()
                .delete()
                .then().log().all()
                .assertThat().statusCode(SC_OK);
    }

    @Test
    public void shouldUpdate() {

        Person person = new Person();

        given().baseUri(BASE_URI)
                .basePath(BASE_PATH)
                .contentType(JSON)
                .request()
                .body(person)
                .log().all()
                .when().put()
                .then()
                .log().all();
    }
}
