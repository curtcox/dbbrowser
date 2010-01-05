package com.cve.fs.fsio;

import com.cve.fs.FSServer;

/**
 *
 * @author curt
 */
public interface FSMetaData {

    /**
     * For making FSMetaDataS.
     */
    public interface Factory {
        FSMetaData of(FSServer server);
    }


}
