package com.cve.web;

import com.cve.web.db.DBBrowserHandler;
import com.cve.web.alt.AlternateViewHandler;
import java.sql.SQLException;
import javax.servlet.http.*;
import java.io.*;
import java.net.URI;

/**
 * This single servlet provides the servlet version of our framework.
 * The framework,
 * <ol>
 *   <li> Finds the right handler for a given request
 *   <li> Uses it to produce a PageResponse
 *   <li> Depending on the response, either
 *      <ol>
 *          <li> Redirect to a different URL
 *          <li> Render the page and write it to the client
 *      </ol>
 * </ol>
 * Bonus diagnostic features include:
 * <ol>
 *  <li> Request Dumping
 *  <li> Object Browsing (coming soon)
 *  <li> Log Browsing (coming soon)
 * </ol>
 * <p>
 * Ultimately, there will be a JavaFX implementation that mirrors this.
 * @author Curt
 */
public final class RequestRouterServlet extends HttpServlet {

    /**
     * This is how we find something to respond to a given request.
     * The router is just a composite handler.
     */
    private static final RequestHandler ROUTER = ErrorReportHandler.of(
        CompositeRequestHandler.of(
            ExitHandler.newInstance(),
            ResourceHandler.newInstance(),
            AlternateViewHandler.newInstance(),
            DBBrowserHandler.newInstance()
        )
    );

    /**
     * Renders models into HTML, JPG, PNG, etc...
     */
    private static final ModelRenderer RENDERER = new PageModelRenderer();

    /**
     * Determines the appropriate MIME type for the objects produced by
     * renderers.
     */
    private static final ContentTyper  TYPER    = new PageContentTyper();

    /**
     * Dumps servlet requests for diagnostic purposes.
     */
    private static final RequestDumpServlet DUMPER = RequestDumpServlet.newInstance();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        try {
            route(request,response);
        } catch (Throwable t) {
            write(PageResponse.of(t),response);
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        try {
            route(request,response);
        } catch (Throwable t) {
            write(PageResponse.of(t),response);
        }

    }

    /**
     * Either redirect, or render the model and send it to the client.
     */
    static void write(PageResponse page, HttpServletResponse response) throws IOException {
        URI redirect = page.getRedirect();
        // Redirect, if that is the response
        if (redirect!=null) {
            response.sendRedirect(redirect.toString());
            return;
        }
        // Otherwise, get the model render it, and send the response
        Model model = page.getModel();

        // Byte arrays get written to an output stream
        if (model instanceof ByteArrayModel) {
            OutputStream out = response.getOutputStream();
            byte[] bytes = ((ByteArrayModel) model).getBytes();
            out.write(bytes);
            out.close();
            return;
        }

        // Everything else gets rendered, typed, and written to a writer
        ClientInfo client = ClientInfo.of();
        Object  rendered = RENDERER.render(model,client);
        ContentType type = TYPER.type(model,rendered);
        response.setContentType(type.toString());
        PrintWriter pw = response.getWriter();
        pw.print(rendered);
        pw.close();
    }

    /**
     * Route the request into something that can handle it and send the
     * response.
     */
    static void route(HttpServletRequest request, HttpServletResponse response)
        throws IOException, SQLException
    {
        String uri = request.getRequestURI();
        // You can dump any request by sticking a ! on the beginning or end.
        if (uri.startsWith("/!") || uri.endsWith("!")) {
            DUMPER.doGet(request,response);
            return;
        }
        // Transform the request, produce route it to something that knows how
        // to process it, and write the response.
        PageRequest   pageRequest = PageRequest.request(request);
        PageResponse  uriResponse = ROUTER.produce(pageRequest);
        if (uriResponse!=null) {
            write(uriResponse,response);
            return;
        }
        // Any request that we couldn't process gets dumped, too.
        DUMPER.doGet(request,response);
    }

}
