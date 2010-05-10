package com.cve.lang;

import com.cve.test.Assert;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

/**
 *
 * @author curt
 */
public class AnnotatedPackageTest {

    @Test
    public void twoClassesFromTheSamePackageHaveTheSamePackage() {
        AnnotatedPackage p1 = packageOf(AnnotatedClass.class);
        AnnotatedPackage p2 = packageOf(AnnotatedPackage.class);
        Assert.equals(p1,p2);
    }

    @Test
    public void thisClassIsInThisPackage() {
        AnnotatedPackage p = packageOf(AnnotatedClassTest.class);
        Assert.that(p.classes.contains(AnnotatedClass.of(AnnotatedClassTest.class)));
    }

    @Test
    public void theClassThisTestsIsInThisPackage() {
        AnnotatedPackage p = packageOf(AnnotatedClassTest.class);
        Assert.that(p.classes.contains(AnnotatedClass.of(AnnotatedClass.class)));
    }

    @Test
    public void thisPackageIsInInItsParentPackage() {
        AnnotatedPackage     p1 = packageOf(AnnotatedClass.class);
        AnnotatedPackage parent = p1.getParent();
        Assert.that(parent.packages.contains(p1));
    }

    @Test
    public void thisPackagesParentContainsSeveralExpectedPackages() {
        AnnotatedPackage parent = packageOf(AnnotatedClass.class);
        ImmutableList<AnnotatedPackage> children = parent.packages;
        Assert.that(children.contains(packageOf(AnnotatedClass.class)));
        Assert.that(children.contains(packageOf(com.cve.log.Log.class)));
        Assert.that(children.contains(packageOf(com.cve.util.Check.class)));
    }

    @Test
    public void rootPackageContainsComPackage() {
        AnnotatedPackage root = AnnotatedPackage.ROOT;
        Assert.that(root.packages.contains(AnnotatedPackage.of("com")));
    }

    static AnnotatedPackage packageOf(Class clazz) {
        return AnnotatedPackage.of(clazz);
    }

}
