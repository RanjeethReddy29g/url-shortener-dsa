package com.example.urlshortener;

import java.time.LocalDateTime;

public class UrlResponse {

    private String shortCode;
    private String longUrl;
    private int clickCount;
    private LocalDateTime expiresAt;

    public UrlResponse(String shortCode, String longUrl, int clickCount, LocalDateTime expiresAt) {
        this.shortCode = shortCode;
        this.longUrl = longUrl;
        this.clickCount = clickCount;
        this.expiresAt = expiresAt;
    }

    public String getShortCode() { return shortCode; }
    public String getLongUrl() { return longUrl; }
    public int getClickCount() { return clickCount; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
}
