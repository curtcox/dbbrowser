package com.cve.web.management;

import com.cve.web.core.Model;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import javax.annotation.concurrent.Immutable;

/**
 *
 * @author curt
 */
@Immutable
final class ObjectRegistryModel implements Model {

    public final ImmutableMultimap<Class,Object> objects;

    private ObjectRegistryModel(Multimap<Class,Object> objects) {
        this.objects = ImmutableMultimap.copyOf(objects);
    }

    static ObjectRegistryModel of() {
        Multimap<Class,Object> map = HashMultimap.create();
        for (Object o : ObjectRegistry.values()) {
            map.put(o.getClass() , o);
        }
        return new ObjectRegistryModel(map);
    }

}
