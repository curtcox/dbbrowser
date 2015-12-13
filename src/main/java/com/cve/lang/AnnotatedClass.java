package com.cve.lang;

import com.cve.util.SimpleCache;
import com.google.common.collect.ImmutableList;

import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

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
     * Where we found the source code.
     */
    public final ResourceLocation file;

    /**
     * The source code.
     */
    public final ImmutableList<SourceCode> source;

    /**
     * Use the factory.
     */
    private AnnotatedClass(Class clazz, ResourceLocation file, List<SourceCode> source) {
        this.clazz  = notNull(clazz);
        this.file   = notNull(file);
        this.source = ImmutableList.copyOf(source);
    }

    static AnnotatedClass of(ResourceLocation source) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static SimpleCache<Class,AnnotatedClass> CLASS_CACHE = SimpleCache.of();
    public static AnnotatedClass of(Class c) {
        if (CLASS_CACHE.containsKey(c)) {
            return CLASS_CACHE.get(c);
        }
        AnnotatedClass annotated = of0(c);
        CLASS_CACHE.put(c, annotated);
        return annotated;
    }

    private static AnnotatedClass of0(Class c) {
        String      className = c.getName();
        ResourceLocation file = ResourceLocation.of(className);
        ImmutableList<SourceCode> source = SourceCode.readFrom(file);
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
            ResourceLocation file = ResourceLocation.of(new File(element.getFileName()));
            ImmutableList<SourceCode> source = SourceCode.readFrom(file);
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
        return Executables.of(e);
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
