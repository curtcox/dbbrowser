package com.cve.web.test;

import com.cve.lang.AnnotatedPackage;
import com.cve.lang.RegEx;
import com.cve.util.Check;
import com.cve.web.core.pages.AbstractPage;
import javax.annotation.concurrent.Immutable;

/**
 *
 * @author curt
 */
@Immutable
public final class PackageTestPage extends AbstractPage {

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
}
