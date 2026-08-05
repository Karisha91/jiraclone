package com.ivan.jiraclone.service;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

    private static final Logger log = LoggerFactory.getLogger(CheckoutService.class);

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public String createCheckoutSession(Long userId) throws StripeException {

        log.info("Creating checkout session for userId: {}", userId);
        log.info("Stripe key starts with: {}", stripeSecretKey != null ? stripeSecretKey.substring(0, 7) + "..." : "NULL");

        if (stripeSecretKey == null || stripeSecretKey.isEmpty()) {
            log.error("STRIPE SECRET KEY IS MISSING!");
            throw new RuntimeException("Stripe secret key is not configured");
        }

        StripeClient client = new StripeClient(stripeSecretKey);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl("https://jiraclone-frontend-git-main-karisha91s-projects.vercel.app/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("https://jiraclone-frontend-git-main-karisha91s-projects.vercel.app/cancel")
                .putMetadata("userId", String.valueOf(userId))
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice("price_1U0eBBI0eP3E1HYjaTaljUFk")
                                .setQuantity(1L)
                                .build()
                )
                .build();

        try {
            Session session = client.v1().checkout().sessions().create(params);
            log.info("Checkout session created successfully: {}", session.getId());
            return session.getUrl();
        } catch (StripeException e) {
            log.error("Stripe error creating checkout session: {}", e.getMessage());
            throw e;
        }
    }
}