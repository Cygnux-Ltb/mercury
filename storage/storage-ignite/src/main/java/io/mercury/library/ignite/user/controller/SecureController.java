package io.mercury.library.ignite.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test the jwt, if the token is valid then return "Login Successful"
 * If is not valid, the request will be intercepted by JwtFilter
 **/
@RestController
@RequestMapping("/secure")
public class SecureController {

    @RequestMapping("/users/user")
    public String loginSuccess() {
        return "Login Successful!";
    }

}