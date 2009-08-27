package com.cve.web;

/**
 * For rendering strings to HTML.
 * @author curt
 */
public final class StringModelRenderer implements ModelHtmlRenderer {

    public String render(Model model, ClientInfo client) {
        StringModel objectModel = (StringModel) model;
        String s = objectModel.string;
        return s;
    }

}