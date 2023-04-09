package com.marketcollection.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class CacheManagerCheck implements CommandLineRunner {

    private final CacheManager cacheManager;

    @Override
    public void run(String... args) throws Exception {
        log.info("Using cache manager: " + this.cacheManager.getClass().getName());
    }
}
