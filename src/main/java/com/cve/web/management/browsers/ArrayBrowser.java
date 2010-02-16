package com.cve.web.management.browsers;

import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
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
    public String getComponentFor(Object o) {
        Object[] a = (Object[]) o;
        UITable table = UITable.of();
        int i = 0;
        for (Object value : a) {
            UIRow row = UIRow.of(UIDetail.of("" + i),link(value));
            table = table.with(row);
            i++;
        }
        return table.toString();
    }

    private UIDetail link(Object o) {
        return UIDetail.of(ObjectLink.of().to("" + o,o));
    }
}
