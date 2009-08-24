package com.cve.web.log;

import com.cve.web.Model;

/**
 *
 * @author curt
 */
final class ObjectModel implements Model {

    public final Object object;
    
    public ObjectModel(Object object) {
        this.object = object;
    }

}
