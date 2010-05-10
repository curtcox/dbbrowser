package com.cve.lang;

import com.cve.lang.AnnotatedPackage.Name;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * Not a general class path, but one like we are using.
 * @author curt
 */
final class ClassPath {

    final ImmutableList<ResourceLocation> roots;

    /**
     * Use a factory.
     */
    private ClassPath(List<ResourceLocation> roots) {
        this.roots = ImmutableList.copyOf(roots);
    }

    static ClassPath of() {
        return new ClassPath(rootSources());
    }

    static ImmutableList<ResourceLocation> rootSources() {
        List<ResourceLocation> list = Lists.newArrayList();
        return ImmutableList.copyOf(list);
    }

    ImmutableList<AnnotatedPackage> packagesDirectlyUnder(Name name) {
        List<AnnotatedPackage> list = Lists.newArrayList();
        for (ResourceLocation source : sourcesDirectlyUnder(name)) {
            if (source.isContainer()) {
                Name child = name.plus(source);
                list.add(AnnotatedPackage.of(child,this));
            }
        }
        return ImmutableList.copyOf(list);
    }

    ImmutableList<AnnotatedClass> classesDirectlyUnder(Name name) {
        List<AnnotatedClass> list = Lists.newArrayList();
        for (ResourceLocation source : sourcesDirectlyUnder(name)) {
            if (!source.isContainer()) {
                list.add(AnnotatedClass.of(source));
            }
        }
        return ImmutableList.copyOf(list);
    }

    ImmutableList<ResourceLocation> sourcesDirectlyUnder(Name name) {
        List<ResourceLocation> list = Lists.newArrayList();
        return ImmutableList.copyOf(list);
    }

}
