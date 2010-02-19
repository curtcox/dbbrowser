package com.cve.web.core.renderers;

import com.cve.web.core.ModelHtmlRenderer;
import com.cve.ui.HTMLTags;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.models.TextFileModel;

/**
 * For rendering TextFileModelS to HTML.
 * @author curt
 */
final class TextFileModelHtmlRenderer implements ModelHtmlRenderer {

    final HTMLTags tags = HTMLTags.of();

    private TextFileModelHtmlRenderer() {}

    public static TextFileModelHtmlRenderer of() {
        return new TextFileModelHtmlRenderer();
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return render((TextFileModel) model);
    }

    HtmlPage render(TextFileModel model) {
        StringBuilder out = new StringBuilder();
        int num = 1;
        for (String line : model.lines) {
            out.append(line(num) + " " + line + "\r");
            num++;
        }
        return HtmlPage.guts(tags.pre(out.toString()));
    }

    String line(int i) {
        if (i<10) {
            return "000" + i;
        }
        if (i<100) {
            return "00" + i;
        }
        if (i<100) {
            return "0" + i;
        }
        return "" + i;
    }
}
