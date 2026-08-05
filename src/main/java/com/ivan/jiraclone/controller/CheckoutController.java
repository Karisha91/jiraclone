package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.model.User;
import com.ivan.jiraclone.service.CheckoutService;
import com.ivan.jiraclone.service.UserService;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {


    private final CheckoutService checkoutService;
    private final UserService userService;


    public CheckoutController(CheckoutService checkoutService, UserService userService) {
        this.checkoutService = checkoutService;
        this.userService = userService;
    }


    @PostMapping
    public String createCheckoutSession(Principal principal) throws StripeException {
        User user = userService.findByUsername(principal.getName());
        return checkoutService.createCheckoutSession(user.getId());
    }

}
