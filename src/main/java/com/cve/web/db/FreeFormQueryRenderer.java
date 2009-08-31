package com.cve.web.db;

import com.cve.db.DBResultSet;
import com.cve.db.Hints;
import com.cve.db.SQL;
import com.cve.db.Select;
import com.cve.db.render.DBResultSetRenderer;
import com.cve.db.select.SelectParser;
import com.cve.db.select.URIRenderer;
import com.cve.stores.HintsStore;
import com.cve.ui.UIForm;
import com.cve.util.AnnotatedStackTrace;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.log.ObjectLink;
import java.net.URI;
import static com.cve.web.db.FreeFormQueryModel.*;
import static com.cve.ui.UIBuilder.*;
import java.sql.SQLException;
import static com.cve.log.Log.args;
import static com.cve.html.HTML.*;

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
            .with(textArea(Q,sql.toString(),8,120))
            .with(label("<p>"))
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
        Select select = SelectParser.parse(sql,page.meta);
        URI uri = URIRenderer.render(select);
        String head = head(base(uri));
        return head + page.message + form.toString() + renderer.landscapeTable();
    }
}
