package com.cve.web;

/**
 * Our only implementation of ContentTyper.
 * @author curt
 */
final class PageContentTyper implements ContentTyper {

    private PageContentTyper() {}

    /**
     * Wrapper for debugging.
     */
    static class LoggedTyper implements ContentTyper {
        final ContentTyper typer;

        LoggedTyper(ContentTyper typer) {
            this.typer = typer;
        }

        @Override
        public ContentType type(Model model, Object rendered) {
            ContentType type = typer.type(model,rendered);
            return type;
        }
    }

    static ContentTyper of() {
        return new LoggedTyper(new PageContentTyper());
    }

    @Override
    public ContentType type(Model model, Object rendered) {
        if (isPng(rendered)) {
            return ContentType.PNG;
        }
        if (model instanceof ByteArrayModel) {
            return ContentType.TEXT;
        }
        if (rendered.toString().toLowerCase().contains("html")) {
            return ContentType.HTML;
        }
        return ContentType.TEXT;
    }

    /**
     * See http://en.wikipedia.org/wiki/Portable_Network_Graphics#File_header
     */
    static boolean isPng(Object rendered) {
        if (!(rendered instanceof byte[])) {
            return false;
        }
        byte[] bytes = (byte[]) rendered;
        return bytes[0] == -119 &&
               bytes[1] ==   80 &&
               bytes[2] ==   78 &&
               bytes[3] ==   71 &&
               bytes[4] ==   13 &&
               bytes[5] ==   10 &&
               bytes[6] ==   26 &&
               bytes[7] ==   10
               ;
    }

}
