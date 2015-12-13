package com.cve.web.core;

import com.cve.lang.URIObject;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIElement;
import com.cve.web.core.models.ByteArrayModel;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

import static com.cve.util.Check.notNull;


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
    final RequestHandler router;

    /**
     * Where we log to.
     */
    final Log log = Logs.of();

    /**
     * Renders models into HTML, JPG, PNG, etc...
     */
    private final ModelHtmlRenderer renderer;

    /**
     * Dumps servlet requests for diagnostic purposes.
     */
    private final RequestDumpServlet dumper;

    public static RequestRouterServlet of(WebApp webapp) {
        RequestHandler      router = webapp.handler;
        ModelHtmlRenderer renderer = webapp.renderer;
        return new RequestRouterServlet(router, renderer);
    }

    /**
     * Use the factory.
     */
    private RequestRouterServlet(RequestHandler router, ModelHtmlRenderer renderer) {
        this.router = notNull(router);
        this.renderer = notNull(renderer);
        
        dumper = RequestDumpServlet.of();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        PageRequestProcessor.next();
        log.args(request,response);
        try {
            route(request,response);
        } catch (Throwable t) {
            write(PageResponse.of(PageRequest.request(request),t),response);
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        PageRequestProcessor.next();
        log.args(request,response);
        try {
            route(request,response);
        } catch (Throwable t) {
            write(PageResponse.of(PageRequest.request(request),t),response);
        }

    }

    /**
     * Either redirect, or render the model and send it to the client.
     */
    void write(PageResponse page, HttpServletResponse response) throws IOException {
        System.out.println("RequestRouterServlet.write start");
        try {
            writeResponse(page, response);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        System.out.println("RequestRouterServlet.write end");
    }

    void writeResponse(PageResponse page, HttpServletResponse response) throws IOException {
        log.args(page,response);
        URIObject redirect = page.redirect;
        // Redirect, if that is the response
        if (redirect!=null) {
            response.sendRedirect(redirect.toString());
            return;
        }
        // Otherwise, get the model render it, and send the response
        Model model = page.model;

        // Byte arrays get written to an output stream
        if (model instanceof ByteArrayModel) {
            OutputStream out = response.getOutputStream();
            ByteArrayModel byteModel = (ByteArrayModel) model;
            byte[]     bytes = byteModel.getBytes();
            ContentType type = byteModel.type;
            response.setContentType(type.toString());
            response.setContentLength(bytes.length);
            out.write(bytes);
            out.close();
            return;
        }

        // Everything else gets rendered, typed, and written to a writer
        ClientInfo client = ClientInfo.of();
        UIElement rendered = renderer.render(model,client);
        response.setContentType(ContentType.HTML.toString());
        PrintWriter pw = response.getWriter();
        pw.print(rendered);
        pw.close();
    }

    /**
     * Route the request into something that can handle it and send the
     * response.
     */
    void route(HttpServletRequest request, HttpServletResponse response)
        throws IOException, SQLException
    {
        log.args(request,response);
        String uri = request.getRequestURI();
        // You can dump any request by sticking a ! on the beginning or end.
        if (uri.startsWith("/!") || uri.endsWith("!")) {
            dumper.doGet(request,response);
            return;
        }
        // Transform the request.   Route it to something that knows how
        // to process it, and write the response.
        PageRequest   pageRequest = PageRequest.request(request);
        PageResponse  uriResponse = router.produce(pageRequest);
        if (uriResponse!=null) {
            write(uriResponse,response);
            log.debug(pageRequest,response);
            return;
        }
        // Any request that we couldn't process gets dumped, too.
        dumper.doGet(request,response);
    }

}
