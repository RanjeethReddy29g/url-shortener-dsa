package com.example.urlshortener;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class URLController {

    private final ShortURLService service;

    public URLController(ShortURLService service) {
        this.service = service;
    }

    @PostMapping("/urls")
    public ResponseEntity<ShortUrlResponse> createShortUrl(
            @jakarta.validation.Valid @RequestBody UrlRequest request) {

        String shortCode = service.shortenURL(
                request.getLongUrl(),
                request.getExpiryMinutes(),
                request.getCustomCode()
        );

        ShortUrlResponse response =
                new ShortUrlResponse("http://localhost:8080/api/" + shortCode);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        String originalUrl = service.getOriginalURL(shortCode);

        if (originalUrl.equals("URL not found")) {
            return ResponseEntity.notFound().build();
        }

        if (originalUrl.equals("EXPIRED")) {
            return ResponseEntity.status(410).build();
        }

        return ResponseEntity
                .status(302)
                .header("Location", originalUrl)
                .build();
    }

    @GetMapping("/stats/{shortCode}")
    public ResponseEntity<UrlResponse> getStats(@PathVariable String shortCode) {

        UrlMapping mapping = service.getStats(shortCode);

        if (mapping == null) {
            return ResponseEntity.notFound().build();
        }

        UrlResponse response = new UrlResponse(
                mapping.getShortCode(),
                mapping.getLongUrl(),
                mapping.getClickCount(),
                mapping.getExpiresAt()
        );

        return ResponseEntity.ok(response);
    }
}
