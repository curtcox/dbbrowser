package com.cve.web.test;

import com.cve.lang.AnnotatedPackage;
import com.cve.lang.RegEx;
import com.cve.model.test.AnnotatedTest;
import com.cve.util.Check;
import com.cve.web.core.pages.AbstractPage;
import javax.annotation.concurrent.Immutable;

/**
 * Page for test results.
 * @author curt
 */
@Immutable
public final class AnnotatedTestPage extends AbstractPage {

    /**
     * The test this page is for.
     */
    final AnnotatedTest test;

    /**
     * Use a factory.
     */
    private AnnotatedTestPage(RegEx root, AnnotatedTest test) {
        super(root);
        this.test = Check.notNull(test);
    }

    public static AnnotatedTestPage of() {
        RegEx root = RegEx.of("^test/");
        AnnotatedTest test = AnnotatedTest.of(AnnotatedPackage.getRoot());
        return new AnnotatedTestPage(root,test);
    }

    public static AnnotatedTestPage of(RegEx root, AnnotatedTest test) {
        return new AnnotatedTestPage(root,test);
    }

}
