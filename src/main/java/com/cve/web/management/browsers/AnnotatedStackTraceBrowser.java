package com.cve.web.management.browsers;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.lang.AnnotatedStackTrace;
import com.cve.ui.UIElement;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.Model;
import com.cve.web.management.AnnotatedStackTraceModel;
import com.cve.web.management.AnnotatedStackTraceRenderer;

/**
 * @author ccox
 */
public final class AnnotatedStackTraceBrowser
    extends AbstractBrowser
{

    final Log log = Logs.of();

    private AnnotatedStackTraceBrowser() {
        super(AnnotatedStackTrace.class);
        
    }

    public static AnnotatedStackTraceBrowser of() {
        return new AnnotatedStackTraceBrowser();
    }
    
    /* (non-Javadoc)
     */
    @Override
    public UIElement getComponentFor(Object o) {
        AnnotatedStackTrace trace = (AnnotatedStackTrace) o;
        ClientInfo client = ClientInfo.of();
        Model model = AnnotatedStackTraceModel.trace(trace);
        UIElement page = AnnotatedStackTraceRenderer.of().render(model,client);
        return page;
    }

}
