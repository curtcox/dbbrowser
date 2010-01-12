package com.cve.io.fs;

import com.cve.model.fs.FSServer;

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
