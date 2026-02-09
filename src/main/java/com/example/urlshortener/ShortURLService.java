package com.example.urlshortener;

import org.springframework.stereotype.Service;
import java.util.Random;
import java.time.LocalDateTime;

@Service
public class ShortURLService {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;

    private final UrlMappingRepository repository;
    private final Random random = new Random();

    public ShortURLService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    public String shortenURL(String longURL, Integer expiryMinutes, String customCode) {

        if (longURL == null || longURL.isBlank()) {
            throw new IllegalArgumentException("Long URL cannot be null or empty");
        }

        // Determine expiry time
        int minutes = (expiryMinutes != null && expiryMinutes > 0) ? expiryMinutes : 10;
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(minutes);

        // 🔥 Custom short code logic
        if (customCode != null && !customCode.isBlank()) {

            if (repository.findByShortCode(customCode).isPresent()) {
                throw new IllegalArgumentException("Custom short code already exists");
            }

            repository.save(new UrlMapping(customCode, longURL, expiryTime));
            return customCode;
        }

        // Auto-generate
        return repository.findByLongUrl(longURL)
                .map(UrlMapping::getShortCode)
                .orElseGet(() -> {

                    String shortCode;
                    do {
                        shortCode = generateCode();
                    } while (repository.findByShortCode(shortCode).isPresent());

                    repository.save(new UrlMapping(shortCode, longURL, expiryTime));

                    return shortCode;
                });
    }

    public String getOriginalURL(String shortCode) {

        return repository.findByShortCode(shortCode)
                .map(mapping -> {

                    if (mapping.getExpiresAt() != null &&
                            mapping.getExpiresAt().isBefore(LocalDateTime.now())) {
                        return "EXPIRED";
                    }

                    mapping.setClickCount(mapping.getClickCount() + 1);
                    repository.save(mapping);

                    return mapping.getLongUrl();
                })
                .orElse("URL not found");
    }

    public UrlMapping getStats(String shortCode) {
        return repository.findByShortCode(shortCode)
                .orElse(null);
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }
        return sb.toString();
    }
}
