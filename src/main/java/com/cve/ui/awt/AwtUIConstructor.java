package com.cve.ui.awt;

import com.cve.ui.PageViewer;
import com.cve.ui.UIComposite;
import com.cve.ui.UIConstructor;
import com.cve.ui.UIElement;
import com.cve.ui.UILabel;
import com.cve.ui.UILink;
import com.cve.ui.UIPage;
import com.cve.ui.UITable;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableHeader;
import com.cve.util.Check;
import com.cve.web.core.PageRequest;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.net.URI;

/**
 * Constructs an AWT UI from toolkit-independent UIElements.
 * @author curt
 */
final class AwtUIConstructor implements UIConstructor {

    final PageViewer pageViewer;

    private AwtUIConstructor(PageViewer pageViewer) {
        this.pageViewer = Check.notNull(pageViewer);
    }

    static AwtUIConstructor of(PageViewer pageViewer) {
        return new AwtUIConstructor(pageViewer);
    }

    @Override
    public Component construct(UIElement e) {
        if (e instanceof UILink)        { return link((UILink) e);  }
        if (e instanceof UILabel)       { return label((UILabel) e);  }
        if (e instanceof UIPage)        { return page((UIPage) e);   }
        if (e instanceof UITable)       { return table((UITable) e);   }
        if (e instanceof UITableDetail) { return tableDetail((UITableDetail) e);   }
        if (e instanceof UITableHeader) { return tableHeader((UITableHeader) e);   }
        if (e instanceof UIComposite)   { return composite((UIComposite) e);   }
        String message = "Unsupported element " + e.getClass();
        throw new IllegalArgumentException(message);
    }

    @Override
    public Label label(UILabel label) {
        return new Label(label.value);
    }

    private Component link(UILink link) {
        return new Label(link.text);
    }

    @Override
    public Panel page(UIPage page) {
        ScrollableFlowPanel panel = new ScrollableFlowPanel();
        for (UIElement element : page.items) {
            panel.add(construct(element));
        }
        return panel;
    }

    @Override
    public Panel composite(UIComposite page) {
        ScrollableFlowPanel panel = new ScrollableFlowPanel();
        for (UIElement element : page.items) {
            panel.add(construct(element));
        }
        return panel;
    }

    private Component tableDetail(UITableDetail tableDetail) {
        return construct(tableDetail.element);
    }

    private Component tableHeader(UITableHeader tableHeader) {
        return construct(tableHeader.element);
    }

    @Override
    public AwtUITable table(UITable table) {
        return AwtUITable.of(table,this);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                showAllElements();
            }
        });
    }

    static void showAllElements() {
        Frame frame = new Frame();
        Panel panel = new Panel();
        frame.add(panel);
        // Allow user to close the window to terminate the program
        AwtCloser.exitOnClose(frame);
        PageViewer pageViewer = new PageViewer() {
            @Override
            public void browse(PageRequest request) {
                System.out.println("Requested " + request);
            }

            @Override
            public void browse(URI uri) {
                System.out.println("Requested " + uri);
            }
        };
        AwtUIConstructor constructor = AwtUIConstructor.of(pageViewer);
        UIElement ui = UIPage.of(
            UILabel.of("Label")
        );
        panel.add(constructor.construct(ui));
        frame.setVisible(true);
        frame.setSize(800,800);
    }

}
