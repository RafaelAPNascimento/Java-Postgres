package com.app.rest.impl;

import com.app.model.Person;
import com.app.rest.PersonApi;
import com.app.service.PersonService;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PersonApiImpl implements PersonApi {

    private static final Logger LOGGER = Logger.getLogger(PersonApi.class);

    @Inject
    private PersonService personService;

    @Override
    public Response getAll() {

        LOGGER.info("get all");
        return Response.ok(personService.getAll(), MediaType.APPLICATION_JSON).build();
    }

    @Override
    public Response getPerson(Long id) {

        LOGGER.info("get Person "+ id);
        Person person = personService.getPerson(id);
        return Response.ok(person, MediaType.APPLICATION_JSON).build();
    }

    @Override
    public Response savePerson(Person person) {

        LOGGER.info("save Person\n"+person);
        try {
            person = personService.savePerson(person);
            return Response.status(Response.Status.CREATED).entity(person).build();
        }
        catch (RuntimeException e) {
            LOGGER.error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @Override
    public Response updatePerson(Person person) {

        LOGGER.info("update Person\n" + person);
        personService.updatePerson(person);
        return Response.ok().build();
    }

    @Override
    public Response deletePerson(Long id) {

        LOGGER.info("delete Person id " + id);
        try {
            boolean deleted = personService.deletePerson(id);
            if (deleted)
                return Response.ok().build();

            throw new RuntimeException("Nao foi possivel remover person");
        }
        catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }
}
