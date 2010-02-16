package com.cve.web.management.browsers;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.lang.AnnotatedStackTrace;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
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
    public String getComponentFor(Object o) {
        AnnotatedStackTrace trace = (AnnotatedStackTrace) o;
        ClientInfo client = ClientInfo.of();
        Model model = AnnotatedStackTraceModel.trace(trace);
        HtmlPage page = AnnotatedStackTraceRenderer.of().render(model,client);
        return page.body;
    }

}
