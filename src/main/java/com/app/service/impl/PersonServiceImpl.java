package com.app.service.impl;

import com.app.model.Person;
import com.app.service.PersonService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

public class PersonServiceImpl implements PersonService {

    @PersistenceContext(unitName = "backendPersistenceUnit")
    private EntityManager entityManager;

    @Override
    @Transactional
    public Person savePerson(Person person) {

        entityManager.persist(person);
        if (person.getId() == null)
            throw new RuntimeException("It was not possible to save this person");

        return person;
    }

    @Override
    public Person getPerson(Long id) {

        String query = "SELECT p FROM Person p WHERE p.id = :id";
        Person person = entityManager.createQuery(query, Person.class)
                            .setParameter("id", id)
                            .getSingleResult();
        return person;
    }

    @Override
    public List<Person> getAll() {

        String query = "SELECT p FROM Person p ORDER BY p.id";
        return entityManager.createQuery(query).setMaxResults(500).getResultList();
    }

    @Override
    public Person updatePerson(Person person) {
        return null;
    }

    @Override
    public boolean deletePerson(Long id) {
        return false;
    }
}
