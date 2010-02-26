package com.cve.ui.swing;

import com.cve.ui.PageViewer;
import com.cve.ui.UIConstructor;
import com.cve.ui.UIElement;
import com.cve.ui.UIPage;
import com.cve.ui.UITable;
import com.cve.ui.UITableBuilder;
import com.cve.ui.UITableCell;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableHeader;
import com.cve.ui.UITableRow;
import com.cve.ui.layout.TableLayout;
import com.cve.ui.layout.TableLayoutConstraints;
import com.cve.web.core.PageRequest;
import java.awt.Color;
import java.awt.EventQueue;
import java.net.URI;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author curt
 */
final class SwingUITable extends JPanel {

     static final Border BLACK_LINE     = BorderFactory.createLineBorder(Color.black);
     static final Border RAISED_Etched  = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
     static final Border LOWERED_ETCHED = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
     static final Border RAISED_BEVEL   = BorderFactory.createRaisedBevelBorder();
     static final Border LOWERED_BEVEL  = BorderFactory.createLoweredBevelBorder();
     static final Border EMPTY          = BorderFactory.createEmptyBorder();
     static final int MATTE_WIDTH = 2;
     static final Border MATTE          = BorderFactory.createMatteBorder(MATTE_WIDTH, MATTE_WIDTH, MATTE_WIDTH, MATTE_WIDTH, (Color) null);

     private SwingUITable(UITable table, UIConstructor constructor) {
        setLayout(tableLayout(table));
        int r = 0;
        for (UITableRow row : table.rows) {
            r++;
            int c = 0;
            for (UITableCell cell : row.details) {
                c++;
                JComponent component = (JComponent) constructor.construct(cell);
                JPanel panel = new JPanel();
                panel.setBorder(MATTE);
                panel.add(component);
                TableLayoutConstraints constraint = TableLayoutConstraints.of(c, r);
                add(panel, constraint);
            }
        }
        setBorder(BLACK_LINE);
     }

     static TableLayout tableLayout(UITable table) {
        int maxCols = 0;
        for (UITableRow row : table.rows) {
            int cols = row.details.size();
            if (cols > maxCols) {
                maxCols = cols;
            }
        }
        return TableLayout.of(maxCols,table.rows.size());
     }

     static SwingUITable of(UITable table, UIConstructor constructor) {
         return new SwingUITable(table,constructor);
     }

     public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                showExampleTable();
            }
        });
     }

     static void showExampleTable() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UITableBuilder table = UITableBuilder.of();
        table.addRow(UITableHeader.of("h1"),UITableHeader.of("h2"));
        table.addRow(UITableDetail.of("d1"),UITableDetail.of("d2"));
        UIElement ui = UIPage.of(
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
        frame.setSize(300,300);
     }
}
