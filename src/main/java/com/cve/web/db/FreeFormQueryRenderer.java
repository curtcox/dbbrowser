package com.cve.web.db;

import com.cve.model.db.DBResultSet;
import com.cve.model.db.Hints;
import com.cve.model.db.Order;
import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.io.db.DBMetaData;
import com.cve.web.db.render.DBResultSetRenderer;
import com.cve.io.db.select.SelectParser;
import com.cve.io.db.select.DBURIRenderer;
import com.cve.log.Log;
import com.cve.stores.db.DBHintsStore;
import com.cve.ui.UIForm;
import com.cve.util.AnnotatedStackTrace;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.Search;
import com.cve.web.log.ObjectLink;
import com.google.common.collect.ImmutableList;
import java.net.URI;
import static com.cve.web.db.FreeFormQueryModel.*;
import static com.cve.ui.UIBuilder.*;
import java.sql.SQLException;
import static com.cve.util.Check.notNull;

/**
 * For rendering the free-form query page.
 * @author curt
 */
final class FreeFormQueryRenderer implements ModelHtmlRenderer {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    final DBHintsStore hintsStore;

    final Log log;

    private static URI HELP = URIs.of("/resource/help/Select.html");

    private FreeFormQueryRenderer(DBMetaData.Factory db, DBHintsStore hintsStore, Log log) {
        this.db = db;
        this.hintsStore = hintsStore;
        this.log = notNull(log);
    }

    static FreeFormQueryRenderer of(DBMetaData.Factory db, DBHintsStore hintsStore, Log log) {
        return new FreeFormQueryRenderer(db,hintsStore,log);
    }
    
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.args(model,client);
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
        URI uri = DBURIRenderer.render(select,search);
        return uri;
    }

    private HtmlPage render(FreeFormQueryModel page, ClientInfo client) throws SQLException {
        SQL sql = page.sql;
        DBResultSet results = page.results;
        String[] nav = new String[] {};
        String title = "Select...";
        UIForm form = UIForm.postAction(URIs.of("select"),log)
            .with(textArea(Q,sql.toString(),8,120))
            .with(label("<p>"))
            .with(submit("Execute"))
        ;
        if (sql.toString().isEmpty()) {
            String guts = page.message + form.toString();
            return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP,log);
        }
        AnnotatedStackTrace trace = page.trace;
        if (trace!=AnnotatedStackTrace.NULL) {
            String guts = page.message + form.toString() + ObjectLink.of(log).to("details",trace);
            return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP,log);
        }
        Hints hints = hintsStore.get(results.columns);
        ImmutableList<Order> orders = ImmutableList.of();
        DBResultSetRenderer renderer = DBResultSetRenderer.resultsOrdersHintsClient(results, orders, hints, client,log);
        String guts = page.message + form.toString() + renderer.landscapeTable();
        URI base = base(page);
        return HtmlPage.gutsTitleNavHelpBase(guts,title,nav,HELP,base,log);
    }
}
