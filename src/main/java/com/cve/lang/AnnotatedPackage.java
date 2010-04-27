package com.cve.lang;

import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.concurrent.Immutable;

/**
 * A little more information than Package alone provides.
 * Additionally, packages are presented as a hierarcy, despite the fact that
 * they are a hierarchy in name only.
 * @author curt
 */
@Immutable
public final class AnnotatedPackage {

    /**
     * The fully qualified name of this package.
     */
    final String name;

    /**
     * The package that contains this one.
     */
    final AnnotatedPackage parent;

    /**
     * The packages this package contains.
     */
    final ImmutableList<AnnotatedPackage> packages;

    /**
     * The classes this package contains.
     */
    final ImmutableList<AnnotatedClass> classes;

    public static final AnnotatedPackage ROOT = root();

    /**
     * Use a factory.
     */
    private AnnotatedPackage(String name, AnnotatedPackage parent, List<AnnotatedPackage> packages, List<AnnotatedClass> classes) {
        this.name     = Check.notNull(name);
        this.parent   = Check.notNull(parent);
        this.packages = ImmutableList.copyOf(Check.notNull(packages));
        this.classes  = ImmutableList.copyOf(Check.notNull(classes));
    }

    /**
     * Use a factory.  This constructor is just for the root package.
     */
    private AnnotatedPackage(List<AnnotatedPackage> packages, List<AnnotatedClass> classes) {
        this.name     = "";
        this.parent   = null;
        this.packages = ImmutableList.copyOf(Check.notNull(packages));
        this.classes  = ImmutableList.copyOf(Check.notNull(classes));
    }

    public static AnnotatedPackage of(Class clazz) {
        String             name = clazz.getPackage().getName();
        AnnotatedPackage parent = parentOf(name);
        List<AnnotatedPackage> packages = ImmutableList.of();
        List<AnnotatedClass>    classes = ImmutableList.of();
        return new AnnotatedPackage(name,parent,packages,classes);
    }

    static AnnotatedPackage root() {
        List<AnnotatedPackage> packages = ImmutableList.of();
        List<AnnotatedClass>    classes = ImmutableList.of();
        return new AnnotatedPackage(packages,classes);
    }

    static AnnotatedPackage parentOf(String name) {
        return ROOT;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        AnnotatedPackage other = (AnnotatedPackage) o;
        return name.equals(other.name);
    }
}
