package com.github.phillbarber.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResource {

    private final Client remoteServiceClient;

    public MessageResource(Client remoteServiceClient) {
        this.remoteServiceClient = remoteServiceClient;
    }


    @GET
    public String hello(){

        Response response = remoteServiceClient.target("http://localhost:8888/remote-service/message").request().get();

        return response.readEntity(String.class);
    }

}
