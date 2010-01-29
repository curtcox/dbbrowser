package com.cve.web.alt;

import com.cve.model.db.DBColumn;
import com.cve.model.db.DBResultSet;
import com.cve.model.db.DBRow;
import com.cve.model.db.SelectResults;
import com.cve.model.db.DBValue;
import com.cve.io.db.DBMetaData;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.util.Strings;
import com.cve.web.AbstractBinaryRequestHandler;
import com.cve.web.ContentType;
import com.cve.web.PageRequest;
import com.google.common.collect.Lists;
import java.util.List;
import static com.cve.util.Check.notNull;

/**
 * For handling requests to view a result set as CSV.
 * @author curt
 */
final class CSVHandler extends AbstractBinaryRequestHandler {

     /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    final DBServersStore serversStore;

    final DBHintsStore hintsStore;

    final ManagedFunction.Factory managedFunction;

    final Log log = Logs.of();

    private CSVHandler(DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        super("^/view/CSV/",ContentType.TEXT);
        this.db = notNull(db);
        this.serversStore = serversStore;
        this.hintsStore = hintsStore;
        this.managedFunction = managedFunction;
        
    }

    static CSVHandler of(DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return new CSVHandler(db,serversStore,hintsStore,managedFunction);
    }

    @Override
    public byte[] get(PageRequest request) {
        log.args(request);
        AlternateViewHandler alt = AlternateViewHandler.of(db,serversStore,hintsStore,managedFunction);
        return csv(alt.getResultsFromDB(request.requestURI));
    }

    public byte[] csv(SelectResults results) {
        log.args(results);
        DBResultSet  rows = results.resultSet;
        StringBuilder out = new StringBuilder();
        int maxColumn = rows.columns.size();
        for (DBRow row : rows.rows) {
            List<String> values = Lists.newArrayList();
            for (int i=0; i<maxColumn; i++) {
                DBColumn column = rows.columns.get(i);
                DBValue value = rows.getValue(row, column);
                values.add(value.toString());
            }
            out.append(Strings.separated(values, ",") + "\r\n");
        }
        return out.toString().getBytes();
    }
}
