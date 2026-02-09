package com.example.urlshortener;

public class ShortUrlResponse {

    private String shortUrl;

    public ShortUrlResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}
