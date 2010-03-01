package com.cve.ui.awt;

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
import com.cve.ui.layout.AwtLayoutAdapter;
import com.cve.ui.layout.TableLayout;
import com.cve.ui.layout.TableLayoutConstraints;
import com.cve.web.core.PageRequest;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.net.URI;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author curt
 */
final class AwtUITable extends Panel {

     private AwtUITable(UITable table, UIConstructor constructor) {
        setLayout(tableLayout(table));
        int r = 0;
        for (UITableRow row : table.rows) {
            r++;
            int c = 0;
            for (UITableCell cell : row.details) {
                c++;
                Component component = (Component) constructor.construct(cell);
                TableLayoutConstraints constraint = TableLayoutConstraints.of(c, r);
                System.out.println("" + constraint);
                add(component, constraint);
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

     static AwtUITable of(UITable table, UIConstructor constructor) {
         return new AwtUITable(table,constructor);
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
        panel.add(AwtUIConstructor.of(pageViewer).construct(ui));
        frame.setVisible(true);
        frame.setSize(300,300);
     }
}
