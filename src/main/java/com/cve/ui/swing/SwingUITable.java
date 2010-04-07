package com.cve.ui.swing;

import com.cve.ui.PageViewer;
import com.cve.ui.PrintPageViewer;
import com.cve.ui.UIElement;
import com.cve.ui.UIPage;
import com.cve.ui.UITable;
import com.cve.ui.UITableBuilder;
import com.cve.ui.UITableCell;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableHeader;
import com.cve.ui.UITableRow;
import com.cve.ui.layout.AwtLayoutAdapter;
import com.cve.ui.layout.TableLayout;
import com.cve.ui.layout.TableLayoutConstraints;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This isn't a JTable, or a wrapper of one.
 * It is more a Swing approximation of an HTML table.
 * @author curt
 */
final class SwingUITable extends JPanel {

     private SwingUITable(UITable table, SwingUIConstructor constructor) {
        setLayout(tableLayout(table));
        int r = 0;
        for (UITableRow row : table.rows) {
            r++;
            int c = 0;
            for (UITableCell cell : row.details) {
                c++;
                JComponent component = (JComponent) constructor.construct(cell);
                JPanel panel = new JPanel();
                if (cell instanceof UITableHeader) {
                    panel.setBorder(Borders.RAISED_BEVEL);
                } else {
                    panel.setBorder(Borders.WHITE_LINE);
                }
                panel.add(component);
                TableLayoutConstraints constraint = TableLayoutConstraints.of(c, r);
                add(panel, constraint);
            }
        }
     }

     static LayoutManager tableLayout(UITable table) {
        int maxCols = 0;
        for (UITableRow row : table.rows) {
            int cols = row.details.size();
            if (cols > maxCols) {
                maxCols = cols;
            }
        }
        return AwtLayoutAdapter.of(TableLayout.of(maxCols,table.rows.size()));
     }

     static SwingUITable of(UITable table, SwingUIConstructor constructor) {
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
        PageViewer pageViewer = PrintPageViewer.of();

        panel.add(SwingUIConstructor.of(pageViewer).construct(ui));
        frame.setVisible(true);
        frame.setSize(300,300);
     }
}
