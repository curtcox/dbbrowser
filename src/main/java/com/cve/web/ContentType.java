package com.cve.web;

/**
 * The MIME type of a page.
 */
public enum ContentType {
    
    TEXT("text/plain"),
    JPEG("image/jpg"),
    PNG("image/png"),
    PDF("application/pdf"),
    XLS("application/vnd.ms-excel"),
    XML("text/xml"),
    HTML("text/html");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public static ContentType of(String uri) {
        return TEXT;
    }

    /**
     * Given a path, return a guess at the content type.
     */
    public static ContentType guessByExtension(String path) {
        if (path.endsWith("html")) { return HTML; }
        if (path.endsWith("png"))  { return PNG; }
        if (path.endsWith("jpg"))  { return JPEG; }
        if (path.endsWith("txt"))  { return TEXT; }
        return TEXT;
    }

    @Override
    public String toString() {
        return value;
    }
}
