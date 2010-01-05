package com.cve.web.db;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.DBServer;
import com.cve.html.CSS;
import com.cve.ui.UIDetail;
import java.util.Collection;

/**
 *
 * @author Curt
 */
public final class RenderingTools {

    public static UIDetail cell(DBServer server,int height) {
        return UIDetail.valueCssWidthHeight(server.linkTo().toString(),CSS.SERVER,1,height);
    }

    public static UIDetail cell(Database database, int height) {
        return UIDetail.valueCssWidthHeight(database.linkTo().toString(),CSS.DATABASE,1,height);
    }

    public static UIDetail cell(DBTable table) {
        return UIDetail.of(table.linkTo().toString(),CSS.TABLE);
    }

    public static UIDetail cell(Collection<DBColumn> columns) {
        StringBuilder out = new StringBuilder();
        for (DBColumn column : columns) {
            out.append(column.linkTo() + " ");
        }
        return UIDetail.of(out.toString(),CSS.COLUMN);
    }

}
