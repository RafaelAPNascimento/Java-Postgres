package com.app.unit.service;

import com.app.model.Person;
import com.app.service.PersonService;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@Alternative
public class PersonServiceMock implements PersonService {

    @Inject
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @PostConstruct
    public void init() {
        transaction = entityManager.getTransaction();
    }

    @Override
    public Person savePerson(Person person) {

        initTransaction();
        entityManager.persist(person);
        commitTransaction();
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

        initTransaction();
        person = entityManager.merge(person);
        commitTransaction();
        return person;
    }

    @Override
    public boolean deletePerson(Long id) {

        initTransaction();
        String query = "DELETE FROM Person person WHERE person.id = :id";
        int countDeleted = entityManager.createQuery(query).setParameter("id", id).executeUpdate();
        commitTransaction();

        return countDeleted == 1;
    }

    private void initTransaction() {
        transaction.begin();
    }
    private void commitTransaction() {
        transaction.commit();
    }
}
