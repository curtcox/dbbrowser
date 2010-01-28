package com.cve.log;

import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 *
 * @author curt
 */
@Immutable
public final class LogName {

    public final String name;

    private LogName(String name) {
        this.name = notNull(name);
    }
}
