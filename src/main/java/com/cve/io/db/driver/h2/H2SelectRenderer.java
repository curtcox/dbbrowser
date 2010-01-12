package com.cve.io.db.driver.h2;

import com.cve.io.db.SelectRenderer;
import com.cve.io.db.SimpleSelectRenderer;

/**
 *
 * @author curt
 */
final class H2SelectRenderer extends SimpleSelectRenderer {

    private H2SelectRenderer() {}

    static SelectRenderer of() {
        return new H2SelectRenderer();
    }

}
