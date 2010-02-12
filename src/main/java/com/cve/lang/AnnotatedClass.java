package com.cve.lang;

import com.cve.util.SimpleCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * A little more information than a class alone provides.
 * @author curt
 */
@Immutable
public final class AnnotatedClass {

    /**
     * The class we annotate.
     */
    public final Class clazz;

    /**
     * The file where we found the source code.
     */
    public final File file;

    /**
     * The source code.
     */
    public final ImmutableList<String> source;

    /**
     * Use the factory.
     */
    private AnnotatedClass(Class clazz, File file, List<String> source) {
        this.clazz  = notNull(clazz);
        this.file   = notNull(file);
        this.source = ImmutableList.copyOf(source);
    }

    private static final List<String> NO_SOURCE_AVAILABLE = ImmutableList.of();

    private static AnnotatedClass of0(Class c) {
        String    className = c.getName();
        File           file = new File("");
        String     resource = sourceFileResource(className);
           InputStream   in = AnnotatedClass.class.getResourceAsStream(resource);
        List<String> source = (in==null)
            ? NO_SOURCE_AVAILABLE
            : readLines(in)
        ;

        return new AnnotatedClass(c,file,source);
    }

    private static SimpleCache<StackTraceElement,AnnotatedClass> ELEMENT_CACHE = SimpleCache.of();
    static AnnotatedClass of(StackTraceElement element) {
        if (ELEMENT_CACHE.containsKey(element)) {
            return ELEMENT_CACHE.get(element);
        }
        AnnotatedClass c = of0(element);
        ELEMENT_CACHE.put(element, c);
        return c;
    }

    /**
     * Return a new AnnotatedClass for the class indicated in this element.
     */
    private static AnnotatedClass of0(StackTraceElement element) {
        try {
            String    className = element.getClassName();
            Class         clazz = Class.forName(className);
            File           file = new File(element.getFileName());
            String     resource = sourceFileResource(className);
               InputStream   in = AnnotatedClass.class.getResourceAsStream(resource);
            List<String> source = (in==null)
                ? NO_SOURCE_AVAILABLE
                : readLines(in)
            ;

            return new AnnotatedClass(clazz,file,source);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Return the  name of the resource that holds the source code for this
     * class name.
     */
    static String sourceFileResource(String className) {
        return "/" + className.replace(".", "/") + ".java";
    }

    /**
     * Read the resource as lines.
     */
    static ImmutableList<String> readLines(InputStream in) {
          try {
              try {
                  List<String> lines = Lists.newArrayList();
                  BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                  for (String line = reader.readLine(); line!=null; line = reader.readLine()) {
                      lines.add(line);
                  }
                  return ImmutableList.copyOf(lines);
              } finally {
                  in.close();
              }
          } catch (IOException e) {
              throw new RuntimeException(e);
          }

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

    @Override
    public int hashCode() {
        return clazz.hashCode() ^ file.hashCode() ^ source.hashCode();    
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        AnnotatedClass other = (AnnotatedClass) o;
        return clazz.equals(other.clazz) &&
                 file.equals(other.file) &&
               source.equals(other.source);
    }
}
