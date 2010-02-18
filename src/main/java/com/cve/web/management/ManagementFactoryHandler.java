package com.cve.web.management;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.core.handlers.AbstractRequestHandler;
import com.cve.web.core.Model;
import com.cve.web.core.PageRequest;

/**
 *
 * @author curt
 */
final class ManagementFactoryHandler extends AbstractRequestHandler {

    private final Log log = Logs.of();

    private ManagementFactoryHandler() {
        super("^/jmx/");
    }

    static ManagementFactoryHandler of() {
        return new ManagementFactoryHandler();
    }

    @Override
    public Model get(PageRequest request) {
        return ManagementFactoryModel.of();
    }
}
