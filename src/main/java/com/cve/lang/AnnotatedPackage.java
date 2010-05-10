package com.cve.lang;

import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.concurrent.Immutable;

/**
 * A little more information than Package alone provides.
 * Additionally, packages are presented as a hierarchy, despite the fact that
 * they are a hierarchy in name only.
 * @author curt
 */
@Immutable
public final class AnnotatedPackage {

    /**
     * The fully qualified name of this package.
     */
    public final Name name;

    /**
     * The packages this package contains.
     */
    public final ImmutableList<AnnotatedPackage> packages;

    /**
     * The classes this package contains.
     */
    public final ImmutableList<AnnotatedClass> classes;

    /**
     * Where the classes, resources, and source code for this package come from.
     * This is a list, rather than a single location, because the package might
     * have separate trees for for tests and resources for example.
     */
    final ClassPath sources;

    /**
     * Return the root package that all other packages are under.
     */
    public static final AnnotatedPackage ROOT = root();

    /**
     * 
     */
    private static Map<Name,AnnotatedPackage> packageNames = Maps.newHashMap();

    /**
     * Type safe package name
     */
    @Immutable
    public static final class Name {

        final String name;

        static final Name ROOT = new Name();

        Name() { this.name = ""; }

        Name(String name) {
            this.name = Check.notNull(name);
        }

        static Name of(String packageName) {
            return new Name(packageName);
        }

        static Name of(Package aPackage) {
            return new Name(aPackage.getName());
        }

        Name plus(ResourceLocation source) {
            throw new UnsupportedOperationException("Not yet implemented");
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
            Name other = (Name) o;
            return name.equals(other.name);
        }

    }

    /**
     * Use a factory.
     */
    private AnnotatedPackage(Name name, ClassPath sources) {
        this.name     = Check.notNull(name);
        this.sources  = Check.notNull(sources);
        this.packages = sources.packagesDirectlyUnder(name);
        this.classes  = sources.classesDirectlyUnder(name);
    }

    /**
     * Use a factory.  This constructor is just for the root package.
     */
    private AnnotatedPackage() {
        this.name     = Name.ROOT;
        this.sources  = ClassPath.of();
        this.packages = sources.packagesDirectlyUnder(name);
        this.classes  = sources.classesDirectlyUnder(name);
    }


    public static AnnotatedPackage of(Class clazz) {
        Name name = Name.of(clazz.getPackage());
        return of(name);
    }

    public static AnnotatedPackage of(String packageName) {
        return of(Name.of(packageName));
    }

    public static AnnotatedPackage of(Name name) {
        return packageNames.get(name);
    }

    public static AnnotatedPackage of(Name name, ClassPath sources) {
        AnnotatedPackage aPackage = new AnnotatedPackage(name,sources);
        packageNames.put(name,aPackage);
        return aPackage;
    }

    static AnnotatedPackage root() {
        return new AnnotatedPackage();
    }

    AnnotatedPackage getParent() {
        return ROOT;
    }

    static AnnotatedPackage parentOf(Name name) {
        return ROOT;
    }

    @Override
    public String toString() {
        return name.toString();
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
