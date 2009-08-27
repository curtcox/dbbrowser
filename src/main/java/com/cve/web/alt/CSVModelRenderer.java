package com.cve.web.alt;

import com.cve.util.Strings;
import com.cve.web.ClientInfo;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.google.common.collect.ImmutableList;

/**
 * For rendering CSV Models as HTML.
 * @author Curt
 */
final class CSVModelRenderer implements ModelHtmlRenderer {

    CSVModelRenderer() {}

    @Override
    public String render(Model model, ClientInfo client) {
        return render((CSVModel) model);
    }

    String render(CSVModel model) {
        StringBuilder out = new StringBuilder();
        ImmutableList<ImmutableList<String>> values = model.values;
        for (ImmutableList<String> row : values) {
            out.append(Strings.separated(row, ",") + "\r\n");
        }
        return out.toString();
    }

}
