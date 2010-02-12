package com.cve.web.management;

import com.cve.web.Model;

/**
 *
 * @author curt
 */
final class ObjectModel implements Model {

    public final Object object;
    
    private ObjectModel(Object object) {
        this.object = object;
    }

    static ObjectModel of(Object object) {
        return new ObjectModel(object);
    }
}
