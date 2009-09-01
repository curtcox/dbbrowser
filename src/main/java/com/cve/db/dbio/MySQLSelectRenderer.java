package com.cve.db.dbio;

/**
 *
 * @author curt
 */
final class MySQLSelectRenderer extends SimpleSelectRenderer {

    private MySQLSelectRenderer() {}

    static SelectRenderer of() {
        return new MySQLSelectRenderer();
    }

}
