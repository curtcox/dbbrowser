package com.cve.web.db;

import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.html.CSS;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UITableDetail;
import java.util.Collection;

/**
 *
 * @author Curt
 */
public final class RenderingTools {

    final Log log = Logs.of();

    private RenderingTools() {
        
    }

    public static RenderingTools of() {
        return new RenderingTools();
    }
    
    public UITableDetail cell(DBServer server,int height) {
        return UITableDetail.valueCssWidthHeight(server.linkTo().toString(),CSS.SERVER,1,height);
    }

    public UITableDetail cell(Database database, int height) {
        return UITableDetail.valueCssWidthHeight(database.linkTo().toString(),CSS.DATABASE,1,height);
    }

    public UITableDetail cell(DBTable table) {
        return UITableDetail.of(table.linkTo().toString(),CSS.TABLE);
    }

    public UITableDetail cell(Collection<DBColumn> columns) {
        StringBuilder out = new StringBuilder();
        for (DBColumn column : columns) {
            out.append(column.linkTo() + " ");
        }
        return UITableDetail.of(out.toString(),CSS.COLUMN);
    }

}
