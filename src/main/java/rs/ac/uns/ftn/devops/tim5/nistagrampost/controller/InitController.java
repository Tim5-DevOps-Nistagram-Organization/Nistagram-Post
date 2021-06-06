package rs.ac.uns.ftn.devops.tim5.nistagrampost.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitController {
    @GetMapping
    public String init() {
        return "Hello, Nistagram Post";
    }
}
