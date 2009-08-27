package com.cve.web.db;

import com.cve.ui.UIForm;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import static com.cve.web.db.FreeFormQueryModel.*;
import static com.cve.ui.UIBuilder.*;

/**
 * For rendering the free-form query page.
 * @author curt
 */
final class FreeFormQueryRenderer implements ModelHtmlRenderer {

    FreeFormQueryRenderer() {}

    @Override
    public String render(Model model, ClientInfo client) {
        return render((FreeFormQueryModel) model);
    }

    private String render(FreeFormQueryModel page) {
        UIForm form = UIForm.postAction(URIs.of("select"))
            .with(text(Q,page.sql.toString()));
        return form.toString();
    }
}
