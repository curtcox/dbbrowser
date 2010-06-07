package com.cve.ui;

import com.cve.lang.URIObject;
import com.cve.web.core.PageRequest;


/**
 *
 * @author curt
 */
public interface PageViewer {

    void browse(PageRequest request);
    void browse(URIObject uri);

}
