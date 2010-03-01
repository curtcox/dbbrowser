package com.cve.ui.swing;

import com.cve.ui.PageViewer;
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
import com.cve.ui.layout.AwtLayoutAdapter;
import com.cve.ui.layout.FlowLayout;
import com.cve.util.Check;
import com.cve.web.core.PageRequest;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;

/**
 * Constructs a Swing UI from toolkit-independent UIElements.
 * @author curt
 */
final class SwingUIConstructor implements UIConstructor {

    final PageViewer pageViewer;

    static final Border GREEN_LINE     = BorderFactory.createLineBorder(Color.GREEN);
    static final Border WHITE_LINE     = BorderFactory.createLineBorder(Color.WHITE);
    static final Border RED_LINE       = BorderFactory.createLineBorder(Color.RED);
    static final Border BLACK_LINE     = BorderFactory.createLineBorder(Color.BLACK);
    static final Border RAISED_Etched  = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    static final Border LOWERED_ETCHED = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    static final Border RAISED_BEVEL   = BorderFactory.createRaisedBevelBorder();
    static final Border LOWERED_BEVEL  = BorderFactory.createLoweredBevelBorder();
    static final Border EMPTY          = BorderFactory.createEmptyBorder();

    private SwingUIConstructor(PageViewer pageViewer) {
        this.pageViewer = Check.notNull(pageViewer);
    }

    static SwingUIConstructor of(PageViewer pageViewer) {
        return new SwingUIConstructor(pageViewer);
    }

    @Override
    public JComponent construct(UIElement e) {
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

    public JXHyperlink link(final UILink link) {
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

    @Override
    public JLabel label(UILabel label) {
        JLabel jLabel = new JLabel(label.value);
        jLabel.setBorder(GREEN_LINE);
        return jLabel;
    }

    @Override
    public JPanel page(UIPage page) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(800,800));
        panel.setLayout(AwtLayoutAdapter.of(FlowLayout.of()));
        panel.setBorder(RED_LINE);
        for (UIElement element : page.items) {
            panel.add(construct(element));
        }
        return panel;
    }

    @Override
    public JPanel composite(UIComposite composite) {
        JPanel panel = new JPanel();
        panel.setLayout(AwtLayoutAdapter.of(FlowLayout.of()));
        panel.setBorder(WHITE_LINE);
        for (UIElement element : composite.items) {
            panel.add(construct(element));
        }
        return panel;
    }

    private JComponent tableDetail(UITableDetail tableDetail) {
        return construct(tableDetail.element);
    }

    private JComponent tableHeader(UITableHeader tableHeader) {
        return construct(tableHeader.element);
    }

    @Override
    public SwingUITable table(UITable table) {
        return SwingUITable.of(table,this);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                showAllElements();
            }
        });
    }

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
        panel.add(SwingUIConstructor.of(pageViewer).construct(ui));
        frame.setVisible(true);
        frame.setSize(800,800);
    }


}
