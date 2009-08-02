package com.cve.db.select;

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
