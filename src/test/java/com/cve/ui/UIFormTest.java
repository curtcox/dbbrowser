package com.cve.ui;

import com.cve.util.URIs;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class UIFormTest {

    ;

    @Test public void toStringContainsPost() {
        String html = UIForm.postAction(URIs.of("/smaction")).toString();
        assertTrue(html,html.contains("POST"));
        assertTrue(html,html.contains("/smaction"));
    }

    @Test public void toStringContainsValue() {
        String html = UIForm.postAction(URIs.of("/"))
            .with(UISubmit.value("svalue")).toString();
        assertTrue(html,html.contains("svalue"));
    }
}
