package com.github.phillbarber.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")
public class HelloWorldResource {

    public HelloWorldResource() {
    }

    @GET
    public String hello(){
        return "Hooray!";
    }

}
