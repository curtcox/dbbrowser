package com.cve.lang;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 *
 * @author curt
 */
@Immutable
public final class AnnotatedClass {

    public final Class clazz;

    public final File file;

    public final ImmutableList<String> source;

    private AnnotatedClass(Class clazz, File file, List<String> source) {
        this.clazz = notNull(clazz);
        this.file = notNull(file);
        this.source = ImmutableList.copyOf(source);
    }

    static AnnotatedClass of(StackTraceElement element) {
        try {
            Class clazz = Class.forName(element.getClassName());
            File file = new File(element.getFileName());
            List<String> source = readSource(file);
            return new AnnotatedClass(clazz,file,source);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static ImmutableList<String> readSource(File file) {
        if (!file.exists()) {
            return ImmutableList.of();
        }
        List<String> lines = Lists.newArrayList();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            for (String line = reader.readLine(); line!=null; line = reader.readLine() ) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ImmutableList.copyOf(lines);
    }
    
    /**
     * Return the method of this class being invoked in the given element.
     */
    ExecutableElement getExecutable(StackTraceElement e) {
        String name = e.getMethodName();
        // We could probably do better here by trying to match the method
        // by args and not just by name
        for (Method method : clazz.getDeclaredMethods()) {
            if (name.equals(method.getName())) {
                return Executables.of(method);
            }
        }
        // Alternatively, we could parse the source somehow, which we would
        // need for better constructor resolution
        if (name.equals("<init>")) {
            for (Constructor constructor : clazz.getDeclaredConstructors()) {
                return Executables.of(constructor);
            }
        }
        String message = e.toString();
        throw new IllegalArgumentException(message);
    }

}
