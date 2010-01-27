package com.cve.web.log.browsers;

import com.cve.log.Log;
import com.cve.util.AnnotatedStackTrace;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.log.AbstractBrowser;
import com.cve.web.log.AnnotatedStackTraceModel;
import com.cve.web.log.AnnotatedStackTraceRenderer;
import static com.cve.util.Check.notNull;

/**
 * @author ccox
 */
public final class AnnotatedStackTraceBrowser
    extends AbstractBrowser
{

    final Log log;

    private AnnotatedStackTraceBrowser(Log log) {
        super(AnnotatedStackTrace.class);
        this.log = notNull(log);
    }

    public static AnnotatedStackTraceBrowser of(Log log) {
        return new AnnotatedStackTraceBrowser(log);
    }
    
    /* (non-Javadoc)
     */
    @Override
    public String getComponentFor(Object o) {
        AnnotatedStackTrace trace = (AnnotatedStackTrace) o;
        ClientInfo client = ClientInfo.of();
        Model model = AnnotatedStackTraceModel.trace(trace,log);
        HtmlPage page = AnnotatedStackTraceRenderer.of(log).render(model,client);
        return page.body;
    }

}
