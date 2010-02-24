package com.cve.ui.swing;

import com.cve.ui.UIConstructor;
import com.cve.ui.UITable;
import com.cve.ui.UITableCell;
import com.cve.ui.UITableRow;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
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

     private SwingUITable(UITable table, UIConstructor constructor) {
        for (UITableRow row : table.rows) {
            for (UITableCell cell : row.details) {
                add((JComponent) constructor.construct(cell));
            }
        }
        this.setBorder(BLACK_LINE);
     }

     static SwingUITable of(UITable table, UIConstructor constructor) {
         return new SwingUITable(table,constructor);
     }
}
