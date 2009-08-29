package com.cve.web.db;

import com.cve.db.DBResultSet;
import com.cve.db.Hints;
import com.cve.db.SQL;
import com.cve.db.render.DBResultSetRenderer;
import com.cve.stores.HintsStore;
import com.cve.ui.UIForm;
import com.cve.util.AnnotatedStackTrace;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.log.ObjectLink;
import static com.cve.web.db.FreeFormQueryModel.*;
import static com.cve.ui.UIBuilder.*;
import java.sql.SQLException;
import static com.cve.log.Log.args;

/**
 * For rendering the free-form query page.
 * @author curt
 */
final class FreeFormQueryRenderer implements ModelHtmlRenderer {

    FreeFormQueryRenderer() {}

    @Override
    public String render(Model model, ClientInfo client) {
        args(model,client);
        try {
            return render((FreeFormQueryModel) model, client);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String render(FreeFormQueryModel page, ClientInfo client) throws SQLException {
        SQL sql = page.sql;
        DBResultSet results = page.results;
        UIForm form = UIForm.postAction(URIs.of("select"))
            .with(text(Q,sql.toString()))
            .with(submit("Execute"))
        ;
        if (sql.toString().isEmpty()) {
            return page.message + form.toString();
        }
        AnnotatedStackTrace trace = page.trace;
        if (trace!=AnnotatedStackTrace.NULL) {
            return page.message + form.toString() + ObjectLink.to("details",trace);
        }
        Hints hints = HintsStore.getHints(results.columns);
        DBResultSetRenderer renderer = DBResultSetRenderer.resultsHintsClient(results, hints, client);
        return page.message + form.toString() + renderer.landscapeTable();
    }
}
