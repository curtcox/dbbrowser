package com.cve.web.log.browsers;

import com.cve.util.AnnotatedStackTrace;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.log.AbstractBrowser;
import com.cve.web.log.AnnotatedStackTraceModel;
import com.cve.web.log.AnnotatedStackTraceRenderer;


/**
 * @author ccox
 */
public final class AnnotatedStackTraceBrowser
    extends AbstractBrowser
{

    public AnnotatedStackTraceBrowser() {
        super(AnnotatedStackTrace.class);
    }

    /* (non-Javadoc)
     */
    @Override
    public String getComponentFor(Object o) {
        AnnotatedStackTrace trace = (AnnotatedStackTrace) o;
        ClientInfo client = ClientInfo.of();
        Model model = AnnotatedStackTraceModel.trace(trace);
        HtmlPage page = new AnnotatedStackTraceRenderer().render(model,client);
        return page.body;
    }

}
