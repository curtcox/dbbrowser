package com.cve.ui.swing;

import com.cve.util.Check;
import com.cve.util.URIs;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.Model;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.RequestHandler;
import com.cve.web.core.WebApp;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Curt
 */
public final class SwingRouterFrame extends JFrame {

    final JButton    forward = new JButton(">");
    final JButton    back    = new JButton("<");
    final JButton    reload  = new JButton("@");
    final JComboBox  address = new JComboBox();
    final JPanel        page = new JPanel();
    final RequestHandler handler;
    final ModelHtmlRenderer renderer;

    private SwingRouterFrame(WebApp webApp) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.handler = Check.notNull(webApp.handler);
        this.renderer = Check.notNull(webApp.renderer);
        layoutComponents();
        addListeners();
    }

    public static SwingRouterFrame of(final WebApp webApp) {
        try {
            FutureTask task = new FutureTask(new Callable(){
                @Override
                public Object call() throws Exception {
                    return new SwingRouterFrame(webApp);
                }
            });
            EventQueue.invokeAndWait(task);
            return (SwingRouterFrame) task.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void browse(URI uri) {
        PageRequest   request = PageRequest.NULL;
        PageResponse response = handler.produce(request);
        if (response.redirect!=null) {
            browse(response.redirect);
            return;
        }
        Model model = response.model;
        ClientInfo info = ClientInfo.of();
        renderer.render(model, info);
    }

    public static void main(String[] args) {
        RequestHandler     handler = null;
        ModelHtmlRenderer renderer = null;
        of(WebApp.of(handler,renderer)).browse(URIs.of("/"));
    }

    private void layoutComponents() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void addListeners() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
