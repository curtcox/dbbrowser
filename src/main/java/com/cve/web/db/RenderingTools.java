package com.cve.web.db;

import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.html.CSS;
import com.cve.log.Log;
import com.cve.ui.UIDetail;
import java.util.Collection;
import static com.cve.util.Check.notNull;

/**
 *
 * @author Curt
 */
public final class RenderingTools {

    final Log log;

    private RenderingTools(Log log) {
        this.log = notNull(log);
    }

    public static RenderingTools of(Log log) {
        return new RenderingTools(log);
    }
    
    public UIDetail cell(DBServer server,int height) {
        return UIDetail.valueCssWidthHeight(server.linkTo().toString(),CSS.SERVER,1,height,log);
    }

    public UIDetail cell(Database database, int height) {
        return UIDetail.valueCssWidthHeight(database.linkTo().toString(),CSS.DATABASE,1,height,log);
    }

    public UIDetail cell(DBTable table) {
        return UIDetail.of(table.linkTo().toString(),CSS.TABLE,log);
    }

    public UIDetail cell(Collection<DBColumn> columns) {
        StringBuilder out = new StringBuilder();
        for (DBColumn column : columns) {
            out.append(column.linkTo() + " ");
        }
        return UIDetail.of(out.toString(),CSS.COLUMN,log);
    }

}
