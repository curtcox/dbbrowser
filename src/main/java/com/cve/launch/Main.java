package com.cve.launch;

/**
 * This class is just an index for other entry points.
 * Launch options:
 * @author curt
 */
public final class Main {

    static final String[] NO_ARGS = new String[0];

    public static void localServerOnly() {
        LocalServerLauncher.main(NO_ARGS);
    }

    public static void localMirrorOnly() {
        LaunchLocalMirror.main(NO_ARGS);
    }
    
    public static void appEngineOnly() {

    }

    public static void localServerWithAppEngine() {

    }

    /**
     * Local server with App engine handling authentication.
     */
    public static void localServerWithAppEngineAuth() {

    }

    public static void localServerWithAppEngineMirror() {

    }

    public static void main(String[] args) {
        localServerOnly();
    }
}
