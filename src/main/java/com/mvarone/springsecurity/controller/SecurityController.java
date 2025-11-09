package com.mvarone.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {
    @GetMapping("/mvarone/api/v1/security")
    public String isAuthenticated(){
        return "Ciao, sei autenticato";
    }

    @GetMapping("/noauth/security")
    public String noAuthentication(){
        return "Ciao, non hai bisogno di autenticazione";
    }
}

