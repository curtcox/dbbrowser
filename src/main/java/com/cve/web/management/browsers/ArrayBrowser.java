package com.cve.web.management.browsers;

import com.cve.ui.UIElement;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableRow;
import com.cve.ui.UITable;
import com.cve.web.management.ObjectLink;

/**
 * @author ccox
 */
public final class ArrayBrowser
    extends AbstractBrowser
{

    private ArrayBrowser() {
        super(Object.class); // harmless lie
        
    }

    public static ArrayBrowser of() {
        return new ArrayBrowser();
    }

    /* (non-Javadoc)
     */
    @Override
    public UIElement getComponentFor(Object o) {
        Object[] a = (Object[]) o;
        UITable table = UITable.of();
        int i = 0;
        for (Object value : a) {
            UITableRow row = UITableRow.of(UITableDetail.of("" + i),link(value));
            table = table.with(row);
            i++;
        }
        return table;
    }

    private UITableDetail link(Object o) {
        return UITableDetail.of(ObjectLink.of().to("" + o,o));
    }
}
