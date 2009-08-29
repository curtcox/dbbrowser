package com.cve.web.log.browsers;

import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITable;
import com.cve.web.log.AbstractBrowser;
import com.cve.web.log.ObjectLink;

/**
 * @author ccox
 */
public final class ArrayBrowser
    extends AbstractBrowser
{

    public ArrayBrowser() {
        super(Object.class); // harmless lie
    }

    /* (non-Javadoc)
     */
    @Override
    public String getComponentFor(Object o) {
        Object[] a = (Object[]) o;
        UITable table = UITable.of();
        int i = 0;
        for (Object value : a) {
            UIRow row = UIRow.of(UIDetail.of("" + i),UIDetail.of(ObjectLink.to(value)));
            table = table.with(row);
        }
        return table.toString();
    }

}
