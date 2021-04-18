package com.app.service;

import com.app.model.Person;

import java.util.List;

public interface PersonService {

    Person savePerson(final Person person);

    Person getPerson(final Long id);

    List<Person> getAll();

    Person updatePerson(final Person person);

    boolean deletePerson(final Long id);
}
