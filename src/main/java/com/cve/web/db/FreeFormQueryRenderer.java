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
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.Search;
import com.cve.web.log.ObjectLink;
import java.net.URI;
import static com.cve.web.db.FreeFormQueryModel.*;
import static com.cve.ui.UIBuilder.*;
import java.sql.SQLException;
import static com.cve.log.Log.args;


/**
 * For rendering the free-form query page.
 * @author curt
 */
final class FreeFormQueryRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resource/help/Select.html");

    FreeFormQueryRenderer() {}

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        args(model,client);
        try {
            return render((FreeFormQueryModel) model,client);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private URI base(FreeFormQueryModel page) {
        SQL sql = page.sql;
        Select select = SelectParser.parse(sql,page.meta);
        Search search = Search.EMPTY;
        URI uri = URIRenderer.render(select,search);
        return uri;
    }

    private HtmlPage render(FreeFormQueryModel page, ClientInfo client) throws SQLException {
        SQL sql = page.sql;
        DBResultSet results = page.results;
        String[] nav = new String[] {};
        String title = "Select...";
        UIForm form = UIForm.postAction(URIs.of("select"))
            .with(textArea(Q,sql.toString(),8,120))
            .with(label("<p>"))
            .with(submit("Execute"))
        ;
        if (sql.toString().isEmpty()) {
            String guts = page.message + form.toString();
            return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
        }
        AnnotatedStackTrace trace = page.trace;
        if (trace!=AnnotatedStackTrace.NULL) {
            String guts = page.message + form.toString() + ObjectLink.to("details",trace);
            return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
        }
        Hints hints = HintsStore.getHints(results.columns);
        DBResultSetRenderer renderer = DBResultSetRenderer.resultsHintsClient(results, hints, client);
        String guts = page.message + form.toString() + renderer.landscapeTable();
        URI base = base(page);
        return HtmlPage.gutsTitleNavHelpBase(guts,title,nav,HELP,base);
    }
}
