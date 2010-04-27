package com.cve.lang;

import com.cve.test.Assert;
import org.junit.Test;

/**
 *
 * @author curt
 */
public class AnnotatedClassTest {

    @Test
    public void twoClassesInTheSamePackageHaveEqualPackages() {
        AnnotatedPackage p1 = AnnotatedPackage.of(AnnotatedClassTest.class);
        AnnotatedPackage p2 = AnnotatedPackage.of(AnnotatedPackageTest.class);
        Assert.equals(p1,p2);
    }
}
