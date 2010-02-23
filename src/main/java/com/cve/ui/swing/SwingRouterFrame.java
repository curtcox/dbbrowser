package com.cve.ui.swing;

import com.cve.ui.layout.TableLayout;
import com.cve.ui.layout.TableLayoutConstants;
import com.cve.ui.layout.TableLayoutConstraints;
import com.cve.util.Check;
import com.cve.util.URIs;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.Model;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.RequestHandler;
import com.cve.web.core.WebApp;
import com.cve.web.core.handlers.CompositeRequestHandler;
import com.cve.web.core.handlers.CoreServerHandler;
import com.cve.web.core.handlers.DebugHandler;
import com.cve.web.core.handlers.ErrorReportHandler;
import com.cve.web.core.renderers.CompositeModelHtmlRenderer;
import com.cve.web.core.renderers.GlobalHtmlRenderers;
import com.cve.web.management.ManagementHandler;
import com.cve.web.management.ManagementModelHtmlRenderers;
import com.cve.web.management.SingleObjectBrowserHandler;
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
 * Swing client for WebApps.
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

    public void browse(PageRequest request) {
        PageResponse response = handler.produce(request);
        if (response.redirect!=null) {
            browse(response.redirect);
            return;
        }
        Model model = response.model;
        ClientInfo info = ClientInfo.of();
        renderer.render(model, info);
        setVisible(true);
    }

    public void browse(URI uri) {
        browse(PageRequest.of(uri));
    }

    public static void main(String[] args) {
        RequestHandler management = ManagementHandler.of();
        RequestHandler handler = ErrorReportHandler.of(
            DebugHandler.of(
                CompositeRequestHandler.of(
                    CoreServerHandler.of(),
                    management,
                    SingleObjectBrowserHandler.of(management)
                )
            )
        );
        ModelHtmlRenderer renderer = CompositeModelHtmlRenderer.of(
            ManagementModelHtmlRenderers.of(),
            GlobalHtmlRenderers.of()
        );
        try {
            of(WebApp.of(handler,renderer)).browse(URIs.of("/"));
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-1);
        }
    }

    private void layoutComponents() {
        setBounds (100, 100, 300, 300);

        // Create a TableLayout for the frame
        double border = 10;
        double FILL = TableLayoutConstants.FILL;
        double[] cols = {border, 20, 20, FILL, 20, border};
        double[] rows = {border, 20, FILL, border};

        setLayout(new TableLayout(cols,rows));

        add(forward, TableLayoutConstraints.of(1, 1));
        add(back,    TableLayoutConstraints.of(2, 1));
        add(address, TableLayoutConstraints.of(3, 1));
        add(reload,  TableLayoutConstraints.of(4, 1));
        add(page,    TableLayoutConstraints.of(1, 2));
    }

    private void addListeners() {
    }
}
