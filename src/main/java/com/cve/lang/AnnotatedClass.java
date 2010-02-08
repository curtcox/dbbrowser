package com.cve.lang;

import java.io.File;
import java.lang.reflect.Method;
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

    private AnnotatedClass(Class clazz, File file) {
        this.clazz = notNull(clazz);
        this.file = notNull(file);
    }

    static AnnotatedClass of(StackTraceElement element) {
        try {
            Class clazz = Class.forName(element.getClassName());
            File file = new File(element.getFileName());
            return new AnnotatedClass(clazz,file);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Return the method of this class being invoked in the given element.
     */
    ExecutableElement getExecutable(StackTraceElement e) {
        String name = e.getMethodName();
        for (Method method : clazz.getDeclaredMethods()) {
            if (name.equals(method.getName())) {
                return Executables.of(method);
            }
        }
        String message = e.toString();
        throw new IllegalArgumentException(message);
    }

}
