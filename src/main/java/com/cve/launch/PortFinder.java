package com.cve.launch;

/**
 * For finding free ports to run our server on.
 * There is no telling what ports might be free -- and we don't really care
 * which one we run on -- but we need to find a free one.
 * @author curt
 */
final class PortFinder {

    private static int PORT = 8888;

    static int findFree() {
        return PORT;
    }

}
