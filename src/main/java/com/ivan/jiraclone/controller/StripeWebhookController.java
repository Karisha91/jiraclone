package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.service.UserService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/api/webhooks")
public class StripeWebhookController {


    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    private final UserService userService;

    public StripeWebhookController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/stripe")
    public ResponseEntity<String> stripeWebhook(@RequestBody String payload,
                                                @RequestHeader("Stripe-Signature") String sigHeader) {


        Event event;

        try {

            event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);



        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        if (event.getType().equals("checkout.session.completed")) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();
            String userId = session.getMetadata().get("userId");
            userService.upgradeToPremium(Long.valueOf(userId));

        }

        return ResponseEntity.ok("Recieved");


    }
}