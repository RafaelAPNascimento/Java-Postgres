package com.app.rest;

import com.app.model.Person;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/person")
public interface PersonApi {

    @GET
    @Path("/all")
    @Produces(APPLICATION_JSON)
    public Response getAll();

    @GET
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    public Response getPerson(@PathParam("id") Long id);

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response savePerson(@Valid Person person);

    @PUT
    @Consumes(APPLICATION_JSON)
    public Response updatePerson(Person person);

    @DELETE
    @Consumes(APPLICATION_JSON)
    public Response deletePerson(final @QueryParam("id") Long id);
}
