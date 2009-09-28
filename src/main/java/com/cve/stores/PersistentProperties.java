package com.cve.stores;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Properties, with extra bits for persisting to disk.
 * @author curt
 */
public final class PersistentProperties extends Properties {

    @Override
    public synchronized Enumeration keys() {
	return super.keys();
    }

}
