package com.cve.io.db.driver.oracle;

import com.cve.io.db.SelectRenderer;
import com.cve.io.db.SimpleSelectRenderer;

/**
 *
 * @author curt
 */
final class OracleSelectRenderer extends SimpleSelectRenderer {

    private OracleSelectRenderer() {}

    static SelectRenderer of() {
        return new OracleSelectRenderer();
    }

}
