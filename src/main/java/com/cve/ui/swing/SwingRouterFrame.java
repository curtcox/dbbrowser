package com.cve.ui.swing;

import com.cve.ui.PageViewer;
import com.cve.ui.UIConstructor;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 * Swing client for WebApps.
 * <p>
 * This is the top-level client that is equivalent to a web browser.
 * This is a single-tabbed browser that displays a single URL at a time.
 * Despite that fact, there are several visual components.
 * <ol>
 * <li> Navigation -- address, forward, back, reload
 * <li> Page -- the page itself
 * <li> Call tree -- How the page was produced on the "server".
 * Of course the "server" is in this JVM.
 * If the page contains a link to the call tree, that link will be used
 * to obtain call tree information.
 * <li> UI Tree -- the user interface elements being used to display the page.
 * <ol>
 * @author Curt
 */
public final class SwingRouterFrame
    extends JFrame
    implements PageViewer
{
    // Most of our mutable state is logically confined to the browse method,
    // but we keep instance variables around here, for the debugging buttons.
    PageRequest request;
    PageResponse response;
    Model model;
    ClientInfo info;
    UIPage pageUI;

    /**
     * The component corresponding to the page that we are currently displaying.
     */
    JComponent pageComponent;

    /**
     * For creating page components.
     */
    final UIConstructor constructor = SwingUIConstructor.of(this);

    // the swing UI components
    final JButton             forward = new JButton(">");
    final JButton                back = new JButton("<");
    final JButton              reload = new JButton("@");
    final JButton       requestButton = new JButton("1");
    final JButton      responseButton = new JButton("2");
    final JButton         modelButton = new JButton("3");
    final JButton        pageUIButton = new JButton("4");
    final JButton pageComponentButton = new JButton("5");
    final JFilteringComboBox  address = new JFilteringComboBox();
    final JPanel                 page = new JPanel();
    final JScrollPane      scrollPage = new JScrollPane(page);

    /**
     * Turns any page requests we get into pages.
     */
    final RequestHandler handler;

    /**
     * Turns pages into UI.
     */
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
            @Override public void run() {
                doBrowse(request);
            }
        });
    }

    void doBrowse(PageRequest request) {
        this.request = Check.notNull(request);
        response = handler.produce(request);
        if (response.redirect!=null) {
            browse(response.redirect);
            return;
        }
        model = response.model;
        info = ClientInfo.of();
        UIElement element = renderer.render(model, info);
        pageUI = (element instanceof UIPage)
            ? (UIPage) element
            : UIPage.of(element);
        setPage(pageUI);
        setAddress(request.fullURI);
        setVisible(true);
    }

    void setPage(UIPage uiPage) {
        page.removeAll();
        pageComponent = (JComponent) constructor.construct(uiPage);
        page.add(pageComponent);
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


    static RequestHandler newRequestHandler() {
        RequestHandler management = ManagementHandler.of();
        return ErrorReportHandler.of(
            DebugHandler.of(
                CompositeRequestHandler.of(
                    CoreServerHandler.of(),
                    management,
                    SingleObjectBrowserHandler.of(management)
                )
            )
        );
    }

    static ModelHtmlRenderer newModelHtmlRenderer() {
        return CompositeModelHtmlRenderer.of(
            ManagementModelHtmlRenderers.of(),
            GlobalHtmlRenderers.of()
        );
    }

    public static void main(String[] args) throws Exception {
        // Without this, VNC doesn't update Swing windows properly.
        System.setProperty("sun.java2d.noddraw","true");
        // Without this, we would currently default to Ocean
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        try {
            of(WebApp.of(newRequestHandler(),newModelHtmlRenderer())).browse(URIs.of("/"));
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-1);
        }
    }

    void layoutComponents() {
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
        //                       <  >        @  1  2  3  4  5
        double[] cols = {border, W, W, FILL, W, W, W, W, W, W, border};
        double[] rows = {border, H, FILL, border};

        setLayout(AwtLayoutAdapter.of(TableLayout.of(cols,rows)));
        int NAV = 1;
        int PAGE = 2;
        add(back,                TableLayoutConstraints.of(1, NAV));
        add(forward,             TableLayoutConstraints.of(2, NAV));
        add(address,             TableLayoutConstraints.of(3, NAV));
        add(reload,              TableLayoutConstraints.of(4, NAV));
        add(requestButton,       TableLayoutConstraints.of(5, NAV));
        add(responseButton,      TableLayoutConstraints.of(6, NAV));
        add(modelButton,         TableLayoutConstraints.of(7, NAV));
        add(pageUIButton,        TableLayoutConstraints.of(8, NAV));
        add(pageComponentButton, TableLayoutConstraints.of(9, NAV));
        add(scrollPage, TableLayoutConstraints.of(1, PAGE, 9, PAGE));
    }

    void configureComponents() {
        address.setEditable(true);
    }

    void addListeners() {
        // delegate each button to a method
                    forward.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { forward();  }});
                       back.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { back();     }});
                     reload.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { reload();   }});
              requestButton.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { showRequest();  }});
             responseButton.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { showResponse(); }});
                modelButton.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { showModel();         }});
               pageUIButton.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { showPageUI();        }});
        pageComponentButton.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { showPageComponent(); }});
    }

    void forward() {

    }

    void back() {

    }

    void reload() {

    }

    void       showRequest() { showNewFor(request);       }
    void      showResponse() { showNewFor(response);      }
    void         showModel() { showNewFor(model);         }
    void        showPageUI() { showNewFor(pageUI);        }
    void showPageComponent() { showNewFor(pageComponent); }

    void showNewFor(Object o) {
        
    }
}
