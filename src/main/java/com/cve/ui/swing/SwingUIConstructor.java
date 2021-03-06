package com.cve.ui.swing;

import com.cve.ui.PageViewer;
import com.cve.ui.PrintPageViewer;
import com.cve.ui.UIComposite;
import com.cve.ui.UIConstructor;
import com.cve.ui.UIElement;
import com.cve.ui.UILabel;
import com.cve.ui.UILink;
import com.cve.ui.UIPage;
import com.cve.ui.UITable;
import com.cve.ui.UITableBuilder;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableHeader;
import com.cve.ui.UITaskPane;
import com.cve.util.Check;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;

/**
 * Constructs a Swing UI from toolkit-independent UIElements.
 * @author curt
 */
final class SwingUIConstructor implements UIConstructor {

    /**
     * This is used to display a new page, when the user follows a link or
     * submits a form.
     */
    final PageViewer pageViewer;


    private SwingUIConstructor(PageViewer pageViewer) {
        this.pageViewer = Check.notNull(pageViewer);
    }

    static SwingUIConstructor of(PageViewer pageViewer) {
        return new SwingUIConstructor(pageViewer);
    }

    @Override
    public JPanel construct(UIPage page) {
        return page(page);
    }

    JComponent construct(UIElement e) {
        if (e instanceof UILink)        { return link((UILink) e);  }
        if (e instanceof UILabel)       { return label((UILabel) e);  }
        if (e instanceof UIPage)        { return page((UIPage) e);   }
        if (e instanceof UITable)       { return table((UITable) e);   }
        if (e instanceof UITableDetail) { return tableDetail((UITableDetail) e);   }
        if (e instanceof UITableHeader) { return tableHeader((UITableHeader) e);   }
        if (e instanceof UITaskPane)    { return taskPane((UITaskPane) e);   }
        if (e instanceof UIComposite)   { return composite((UIComposite) e);   }
        String message = "Unsupported element " + e.getClass();
        throw new IllegalArgumentException(message);
    }

    JXHyperlink link(final UILink link) {
        AbstractHyperlinkAction linkAction = new AbstractHyperlinkAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageViewer.browse(link.target);
                setVisited(true);
            }
        };
        JXHyperlink hyperlink = new JXHyperlink(linkAction);
        hyperlink.setText(link.text);
        return hyperlink;
    }

    JLabel label(UILabel label) {
        JLabel jLabel = new JLabel(label.value);
        return jLabel;
    }

    JPanel page(UIPage page) {
        JPanel panel = new JPanel();
        for (UIElement element : page.items) {
            panel.add(construct(element));
        }
        return panel;
    }

    JPanel composite(UIComposite composite) {
        JPanel panel = new JPanel();
        for (UIElement element : composite.items) {
            panel.add(construct(element));
        }
        return panel;
    }

    JPanel taskPane(UITaskPane pane) {
        JXTaskPaneContainer tasks = new JXTaskPaneContainer();
        for (int i=0; i<pane.items.size(); i++) {
            JXTaskPane task = new JXTaskPane();
            task.setAnimated(false);
            UIElement title = pane.heading.get(i);
            String titleString = null;
            if (title instanceof UILabel) { titleString = ((UILabel) title).value; }
            if (title instanceof UILink)  { titleString = ((UILink)  title).text;  }
            task.setTitle(titleString);
            task.add(construct(pane.items.get(i)));
            tasks.add(task);
        }
        return tasks;
    }

    private JComponent tableDetail(UITableDetail tableDetail) {
        return construct(tableDetail.element);
    }

    private JComponent tableHeader(UITableHeader tableHeader) {
        return construct(tableHeader.element);
    }

    SwingUITable table(UITable table) {
        return SwingUITable.of(table,this);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                showAllElements();
            }
        });
    }

    /**
     * Show a widget gallery of all we produce.
     */
    static void showAllElements() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UITableBuilder table = UITableBuilder.of();
        table.addRow(UITableHeader.of("h1"),UITableHeader.of("h2"));
        table.addRow(UITableDetail.of("d1"),UITableDetail.of("d2"));
        UIElement ui = UIPage.of(
            UILabel.of("Label"),
            table.build()
        );
        PageViewer pageViewer = PrintPageViewer.of();
        panel.add(SwingUIConstructor.of(pageViewer).construct(ui));
        frame.setVisible(true);
        frame.setSize(800,800);
    }

}
