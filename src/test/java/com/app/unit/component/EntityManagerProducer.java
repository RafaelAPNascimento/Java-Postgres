package com.app.unit.component;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerProducer {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("mock");

    @Produces
    public EntityManager create()
    {
        return emf.createEntityManager();
    }

    public void terminate(@Disposes EntityManager entityManager)
    {
        entityManager.close();
    }
}
