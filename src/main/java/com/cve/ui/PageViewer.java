package com.cve.ui;

import com.cve.web.core.PageRequest;
import java.net.URI;

/**
 *
 * @author curt
 */
public interface PageViewer {

    void browse(PageRequest request);
    void browse(URI uri);

}
