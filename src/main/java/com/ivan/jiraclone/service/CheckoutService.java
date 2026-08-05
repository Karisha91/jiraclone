package com.ivan.jiraclone.service;


import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public String createCheckoutSession(Long userId) throws StripeException {

        StripeClient client = new StripeClient(stripeSecretKey);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl("https://jiraclone-frontend-git-main-karisha91s-projects.vercel.app/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:5173/cancel")
                .putMetadata("userId", String.valueOf(userId))
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice("price_1U0eBBI0eP3E1HYjaTaljUFk")
                                .setQuantity(1L)
                                .build()
                )
                .build();

        Session session = client.v1().checkout().sessions().create(params);
        return session.getUrl();
    }
}
