package com.cve.web;

/**
 *
 * @author curt
 */
final class PageContentTyper implements ContentTyper {

    public PageContentTyper() {}

    public ContentType type(Model model, Object rendered) {
        if (model instanceof ByteArrayModel) {
            return ContentType.TEXT;
        }
        if (rendered.toString().toLowerCase().contains("html")) {
            return ContentType.HTML;
        }
        return ContentType.TEXT;
    }

}
