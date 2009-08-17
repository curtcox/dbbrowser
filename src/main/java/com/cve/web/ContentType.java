package com.cve.web;

/**
 * The MIME type of a page.
 */
public enum ContentType {
    
    TEXT("text/plain"),
    JPEG("image/jpg"),
    PNG("image/png"),
    HTML("text/html");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public static ContentType of(String uri) {
        return TEXT;
    }

    @Override
    public String toString() {
        return value;
    }
}
