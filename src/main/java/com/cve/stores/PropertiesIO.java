package com.cve.stores;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * For loading and saving properties files
 * @author curt
 */
public final class PropertiesIO {

    static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }

    public static File getFile(String name) {
        File home = getUserHome();
        File dbbrowser = new File(home + File.separator + ".dbbrowser");
        if (!dbbrowser.exists()) {
            dbbrowser.mkdirs();
        }
        File file = new File(dbbrowser + File.separator + name + ".props");
        return file;
    }

    public static Properties load(String name) {
        try {
            return load0(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(Properties props, String name) {
        try {
            save0(props,name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties load0(String name) throws IOException {
        File file = getFile(name);
        Properties props = new Properties();
        if (!file.exists()) {
            return props;
        }
        InputStream in = new FileInputStream(file);
        props.load(in);
        in.close();
        return props;
    }

    private static void save0(Properties props, String name) throws IOException {
        File file = getFile(name);
        String comments = name;
        OutputStream out = new FileOutputStream(file);
        props.store(out, comments);
    }

}
