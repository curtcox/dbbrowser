package com.cve.ui.swing;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


/**
 *
 * @author curt
 */
public final class Borders {

    static final Border GREEN_LINE     = BorderFactory.createLineBorder(Color.GREEN);
    static final Border WHITE_LINE     = BorderFactory.createLineBorder(Color.WHITE);
    static final Border RED_LINE       = BorderFactory.createLineBorder(Color.RED);
    static final Border BLACK_LINE     = BorderFactory.createLineBorder(Color.BLACK);
    static final Border RAISED_Etched  = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    static final Border LOWERED_ETCHED = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    static final Border RAISED_BEVEL   = BorderFactory.createRaisedBevelBorder();
    static final Border LOWERED_BEVEL  = BorderFactory.createLoweredBevelBorder();
    static final Border EMPTY          = BorderFactory.createEmptyBorder();

}
