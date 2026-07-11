package com.ivan.jiraclone.service;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    ConcurrentHashMap<String, Bucket> bucket = new ConcurrentHashMap<>();


    public Bucket resolveBucket(String ipAdress) {
        Bandwidth limit = Bandwidth.simple(5, java.time.Duration.ofMinutes(1));
        return bucket.computeIfAbsent(ipAdress, k -> Bucket.builder().addLimit(limit).build());

    }


}
