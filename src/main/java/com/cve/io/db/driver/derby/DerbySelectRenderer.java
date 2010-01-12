package com.cve.io.db.driver.derby;

import com.cve.io.db.SelectRenderer;
import com.cve.io.db.SimpleSelectRenderer;

/**
 *
 * @author curt
 */
final class DerbySelectRenderer extends SimpleSelectRenderer {

    private DerbySelectRenderer() {}

    static SelectRenderer of() {
        return new DerbySelectRenderer();
    }

}
