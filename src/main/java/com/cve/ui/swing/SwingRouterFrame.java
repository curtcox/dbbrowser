package com.cve.ui.swing;

import com.cve.ui.UIElement;
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
    final SwingUIConstructor constructor = SwingUIConstructor.of();

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
        setPage(renderer.render(model, info));
        setAddress(request.fullURI);
        setVisible(true);
    }

    void setPage(UIElement e) {
        page.removeAll();
        page.add(constructor.construct(e));
    }
    
    void setAddress(URI uri) {

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
        int x = 100;
        int y = 100;
        int w = 800;
        int h = 800;
        setBounds (x, y, w, h);

        // Create a TableLayout for the frame
        double border = 10;
        double FILL = TableLayoutConstants.FILL;
        int W = 50;
        int H = 50;
        double[] cols = {border, W, W, FILL, W, border};
        double[] rows = {border, H, FILL, border};

        setLayout(new TableLayout(cols,rows));
        int NAV = 1;
        int PAGE = 2;
        add(forward, TableLayoutConstraints.of(1, NAV));
        add(back,    TableLayoutConstraints.of(2, NAV));
        add(address, TableLayoutConstraints.of(3, NAV));
        add(reload,  TableLayoutConstraints.of(4, NAV));
        add(page,    TableLayoutConstraints.of(1, PAGE, 4, PAGE));
    }

    private void addListeners() {
    }
}
