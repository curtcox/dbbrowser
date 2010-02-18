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
import com.cve.log.Logs;
import com.cve.stores.db.DBHintsStore;
import com.cve.ui.UIForm;
import com.cve.lang.AnnotatedStackTrace;
import com.cve.util.URIs;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.web.core.Search;
import com.cve.web.management.ObjectLink;
import com.google.common.collect.ImmutableList;
import java.net.URI;
import static com.cve.web.db.FreeFormQueryModel.*;
import static com.cve.ui.UIBuilder.*;
import java.sql.SQLException;

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

    final Log log = Logs.of();

    private static URI HELP = URIs.of("/resource/help/Select.html");

    private FreeFormQueryRenderer(DBMetaData.Factory db, DBHintsStore hintsStore) {
        this.db = db;
        this.hintsStore = hintsStore;
        
    }

    static FreeFormQueryRenderer of(DBMetaData.Factory db, DBHintsStore hintsStore) {
        return new FreeFormQueryRenderer(db,hintsStore);
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
            String guts = page.message + form.toString() + ObjectLink.of().to("details",trace);
            return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
        }
        Hints hints = hintsStore.get(results.columns);
        ImmutableList<Order> orders = ImmutableList.of();
        DBResultSetRenderer renderer = DBResultSetRenderer.resultsOrdersHintsClient(results, orders, hints, client);
        String guts = page.message + form.toString() + renderer.landscapeTable();
        URI base = base(page);
        return HtmlPage.gutsTitleNavHelpBase(guts,title,nav,HELP,base);
    }
}
