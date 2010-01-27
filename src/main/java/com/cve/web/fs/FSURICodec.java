
package com.cve.web.fs;

import com.cve.model.fs.FSField;
import com.cve.util.*;
import com.cve.model.fs.FSPath;
import com.cve.model.fs.FSPipeline;
import com.cve.model.fs.FSServer;
import com.cve.log.Log;
import com.cve.web.Search;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.net.URI;
import java.net.URLDecoder;
import java.util.List;
import static com.cve.util.Check.notNull;

/**
 * Tools for converting between objects and the specially formatted database
 * {@link URI}S we work with.
 * <p>
 * What is the special format?
 * The URL space hierarcy is mapped onto URLs like:
 * http://webserver/search/dbserver/databases/tables/columns/joins/filters/orders/groups/limit
 * Empty parts are preserved as an empty path element like filters, orders, and
 * groups in the following:
 * http://webserver/+/dbserver/databases/tables/columns/joins////limit
 *
 */
public final class FSURICodec {

    /**
     * The position within the URI where the given information is specified.
     * Most of these map very directly to clauses in SQL select statements.
     */
    enum Position {

        /**
         * The optional search text.
         */
        SEARCH(1),

        /**
         * The database server.
         */
        SERVER(2),
        
        /**
         * The metadata method name.
         * This isn't used in normal operations.
         * It is for low-level JDBC debugging and experiments.
         */
        METADATA(3),

        /**
         * The file system files.
         */
        FILES(4),

        /**
         * The table columns.
         */
        COLUMNS(5),

        /**
         * Joins between tables.
         */
        JOINS(6),

        /**
         * Filters on column values
         */
        FILTERS(7),

        /**
         * Column sort orders
         */
        ORDERS(8),

        /**
         * Group by clauses
         */
        GROUPS(9),

        /**
         * Limits on what rows should be returned.
         */
        LIMIT(10),
    ;

        /**
         * Number of slashes before this position in the URL.
         * For example:
         *                    1        2        3        4
         * http://webserver/search/dbserver/databases/tables
         */
        private final int index;

        Position(int index) {
            this.index = index;
        }

    }

    /**
     * Where we log to.
     */
    final Log log;

    private FSURICodec(Log log) {
        this.log = notNull(log);
    }

    public static FSURICodec of(Log log) {
        return new FSURICodec(log);
    }
    
    String at(String uri, Position pos) {
        log.args(uri);
        return uri.split("/")[pos.index];
    }

    static boolean exists(String uri, Position pos) {
        String[] parts = uri.split("/");
        return parts.length > pos.index;
    }

    public Search getSearch(String uri) {
        log.args(uri);
        notNull(uri);
        if (uri.equals("/")) {
            return Search.EMPTY;
        }
        String search = URLDecoder.decode(at(uri,Position.SEARCH));
        return Search.parse(search);
    }

    public FSServer getServer(String uri) {
        log.notNullArgs(uri);
        String name = at(uri,Position.SERVER);
        return FSServer.uri(URIs.of(name),log);
    }

    public String getMetaDataMethod(String uri) {
        log.notNullArgs(uri);
        if (!exists(uri,Position.METADATA)) {
            return "";
        }
        return at(uri,Position.METADATA);
    }

    public ImmutableList<FSPath> getPaths(String uri) {
        log.notNullArgs(uri);
        if (!exists(uri,Position.FILES)) {
            return ImmutableList.of();
        }
        FSServer server = FSServer.uri(URIs.of(at(uri,Position.SERVER)),log);
        List<FSPath> list = Lists.newArrayList();
        for (String fullTableName : at(uri,Position.FILES).split("\\+")) {
            FSPath        path = FSPath.parse(server,fullTableName,log);
            list.add(path);
        }
        return ImmutableList.copyOf(list);
    }

    /**
     * Get columns from
     * /server/dbs/tables/columns
     */
    public ImmutableList<FSField> getColumns(ImmutableList<FSPath> tables, String uri) {
        if (!exists(uri,Position.COLUMNS)) {
            return ImmutableList.of();
        }
        FSServer server = FSServer.uri(URIs.of(at(uri,Position.SERVER)),log);
        List<FSField> list = Lists.newArrayList();
        for (String fullColumnName : at(uri,Position.COLUMNS).split("\\+")) {
            fullColumnName = splitFullColumnName(fullColumnName)[1];
            FSField column = FSField.parse(server,tables,fullColumnName);
            list.add(column);
        }
        return ImmutableList.copyOf(list);
    }

    /**
     * Tokenize the column name and optional aggregate function into two
     * parts.
     */
    private static String[] splitFullColumnName(String full) {
        String[] parts = full.split("\\(|\\)");
        if (parts.length==1) {
            return new String[] { "", parts[0] };
        }
        if (parts.length==2) {
            return parts;
        }
        throw new IllegalArgumentException(full);
    }

    public static URI encode(FSServer aThis) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static URI encode(FSPath aThis) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static URI encode(FSField aThis) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public FSPipeline getPipeline(String uri) {
        log.notNullArgs(uri);
        // get everything out of the URL
        ImmutableList<FSPath>                paths = getPaths(uri);

        // Setup the select
        FSPipeline pipeline = null;
        return pipeline;
    }
}
