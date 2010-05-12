package com.cve.web.test;

import com.cve.lang.AnnotatedPackage;
import com.cve.lang.RegEx;
import com.cve.util.Check;
import com.cve.web.core.pages.AbstractPage;
import javax.annotation.concurrent.Immutable;

/**
 * Test results for a package tree.
 * @author curt
 */
@Immutable
public final class PackageTestPage extends AbstractPage {

    /**
     * The package this page is for.
     */
    final AnnotatedPackage annotatedPackage;

    /**
     * Use a factory.
     */
    private PackageTestPage(RegEx root, AnnotatedPackage annotatedPackage) {
        super(root);
        this.annotatedPackage = Check.notNull(annotatedPackage);
    }

    public static PackageTestPage of() {
        RegEx root = RegEx.of("^test/");
        AnnotatedPackage annotatedPackage = AnnotatedPackage.ROOT;
        return new PackageTestPage(root,annotatedPackage);
    }

    public static PackageTestPage of(RegEx root, AnnotatedPackage annotatedPackage) {
        return new PackageTestPage(root,annotatedPackage);
    }

    /**
     * Produce a test page for the root package, write it to a file, and open
     * the file in a browser.
     */
    public static void main(String[] args) {

    }
}
