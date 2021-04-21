package com.app.unit.service;

import com.app.model.Person;
import com.app.service.PersonService;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonServiceTest {

    private Weld weld;
    private WeldContainer container;
    private PersonService personService;

    @BeforeAll
    public void init() {

        System.setProperty(Weld.ARCHIVE_ISOLATION_SYSTEM_PROPERTY, "false");

        weld = new Weld();
        container = weld.initialize();
        personService = container.instance().select(PersonService.class).get();
    }

    @AfterAll
    public void end() throws Exception {

        weld.shutdown();
    }

    @Test
    public void shouldInsertNewLog() {

        Person person = Person.builder().age(30).name("Rafael").build();
        person = personService.savePerson(person);

        Assertions.assertNotNull(person.getId());
    }

    @Test
    public void shouldGetPersonList()
    {
        List<Person> logs = personService.getAll();
        Assertions.assertFalse(logs.isEmpty());
    }

    public void shouldDelete() {

        boolean deleted = personService.deletePerson(1L);
        Assertions.assertTrue(deleted);
    }

}
