package com.hafizbahtiar.murmur.features.status.controllers;

import org.springframework.web.bind.annotation.*;

import com.hafizbahtiar.murmur.features.status.entities.Status;

@RestController
public class StatusController {
    @GetMapping("/status")
    public Status status() {
        return new Status("UP", "Murmur");
    }
}
