package com.cve.io.db.driver.mysql;

import com.cve.io.db.SelectRenderer;
import com.cve.io.db.SimpleSelectRenderer;

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
