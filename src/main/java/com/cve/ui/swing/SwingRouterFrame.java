package com.cve.ui.swing;

import com.cve.ui.PageViewer;
import com.cve.ui.UIElement;
import com.cve.ui.UIPage;
import com.cve.ui.layout.AwtLayoutAdapter;
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
import java.awt.Dimension;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 * Swing client for WebApps.
 * This is the top-level client that is equivalent to a web browser.
 * @author Curt
 */
public final class SwingRouterFrame extends JFrame implements PageViewer {

    final JButton            forward = new JButton(">");
    final JButton               back = new JButton("<");
    final JButton             reload = new JButton("@");
    final JFilteringComboBox address = new JFilteringComboBox();
    final JPanel                page = new JPanel();
    final JScrollPane     scrollPage = new JScrollPane(page);

    final RequestHandler handler;
    final ModelHtmlRenderer renderer;

    private SwingRouterFrame(WebApp webApp) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.handler = Check.notNull(webApp.handler);
        this.renderer = Check.notNull(webApp.renderer);
        layoutComponents();
        configureComponents();
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

    @Override
    public void browse(final PageRequest request) {
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run() {
                doBrowse(request);
            }
        });
    }

    void doBrowse(PageRequest request) {
        PageResponse response = handler.produce(request);
        if (response.redirect!=null) {
            browse(response.redirect);
            return;
        }
        Model model = response.model;
        ClientInfo info = ClientInfo.of();
        UIElement element = renderer.render(model, info);
        UIPage pageUI = (element instanceof UIPage)
            ? (UIPage) element
            : UIPage.of(element);
        setPage(pageUI);
        setAddress(request.fullURI);
        setVisible(true);
    }

    void setPage(UIPage p) {
        page.removeAll();
        SwingUIConstructor constructor = SwingUIConstructor.of(this);
        page.add(constructor.construct(p));
    }
    
    void setAddress(URI uri) {
        if (!address.items.contains(uri)) {
            address.addItem(uri);
        }
        address.model.setSelectedItem(uri);
    }

    @Override
    public void browse(URI uri) {
        browse(PageRequest.of(uri));
    }

    public static void main(String[] args) throws Exception {
        // Without this, VNC doesn't update Swing windows properly.
        System.setProperty("sun.java2d.noddraw","true");
        // Without this, we would currently default to Ocean
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
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
        Dimension d = new Dimension(800,800);
        //page.setPreferredSize(d);
        setBounds (x, y, w, h);

        // Create a TableLayout for the frame
        double border = 10;
        double FILL = TableLayoutConstants.FILL;
        int W = 50;
        int H = 50;
        double[] cols = {border, W, W, FILL, W, border};
        double[] rows = {border, H, FILL, border};

        setLayout(AwtLayoutAdapter.of(TableLayout.of(cols,rows)));
        int NAV = 1;
        int PAGE = 2;
        add(back,       TableLayoutConstraints.of(1, NAV));
        add(forward,    TableLayoutConstraints.of(2, NAV));
        add(address,    TableLayoutConstraints.of(3, NAV));
        add(reload,     TableLayoutConstraints.of(4, NAV));
        add(scrollPage, TableLayoutConstraints.of(1, PAGE, 4, PAGE));
    }

    private void configureComponents() {
        address.setEditable(true);
    }

    private void addListeners() {

    }

}
