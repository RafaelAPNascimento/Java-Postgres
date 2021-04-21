package com.app.integration;

import com.app.model.Person;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonApiTest {

    private final static String BASE_URI = "http://localhost:8080/java-postgres-be/api";
    private final static String BASE_PATH = "/person";

    private Person[] persons;

    @Test
    @Order(1)
    public void shouldSavePerson() {

        Person person = Person.builder().age(30).name("Rafael").build();
        save(person);
        person = Person.builder().age(10).name("Maria").build();
        save(person);
        person = Person.builder().age(45).name("Joao").build();
        save(person);
        person = Person.builder().age(5).name("Helena").build();
        save(person);
    }

    private void save(Person person) {

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
    @Order(2)
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
    @Order(3)
    public void shouldGetById() {

        persons = getAll();
        long id = persons[0].getId();

        String path = "/person/{id}";

        given().baseUri(BASE_URI)
                .basePath(path)
                .pathParam("id", id)
                .request()
                .log().all()
                .when()
                .get()
                .then().log().all()
                .assertThat().statusCode(SC_OK);
    }

    @Test
    @Order(4)
    public void shouldDelete() {

        long id = persons[1].getId();

        given().baseUri(BASE_URI)
                .basePath(BASE_PATH)
                .queryParam("id", id)
                .request()
                .log().all()
                .when()
                .delete()
                .then().log().all()
                .assertThat().statusCode(SC_OK);
    }

    @Test
    @Order(5)
    public void shouldUpdate() {

        persons[2].setName("New Name");

        given().baseUri(BASE_URI)
                .basePath(BASE_PATH)
                .contentType(JSON)
                .request()
                .body(persons[2])
                .log().all()
                .when().put()
                .then()
                .log().all();
    }

    @Test
    @Order(6)
    public void shouldDeleteAll() {

        for (Person p : persons)
            given().baseUri(BASE_URI)
                    .basePath(BASE_PATH)
                    .queryParam("id", p.getId())
                    .request()
                    .when()
                    .delete();

        persons = getAll();

        Assertions.assertTrue(persons.length == 0);
    }

    private Person[] getAll() {
        String path = "/person/all";

        return given().baseUri(BASE_URI)
                .basePath(path)
                .request()
                .when()
                .get()
                .then()
                .extract().as(Person[].class);
    }
}
