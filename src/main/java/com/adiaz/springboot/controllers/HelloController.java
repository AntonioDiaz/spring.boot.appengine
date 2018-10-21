package com.adiaz.springboot.controllers;

import com.adiaz.springboot.entities.Item;
import com.googlecode.objectify.ObjectifyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HelloController {

    @GetMapping("")
    public String hello() {
                return "toma toma!";
        }

    @RequestMapping("/put")
    public String put() {
        ObjectifyService.ofy().save().entity(new Item("001", "name", "desc"));
        return "put";
    }

    @RequestMapping("/get")
    public String get() {
        Item e = ObjectifyService.ofy().cache(true).load().type(Item.class).id("001").now();

        return String.format("%s: %s - %s", e.getId(), e.getName(), e.getDescription());
    }
}
