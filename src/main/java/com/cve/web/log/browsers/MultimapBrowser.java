package com.cve.web.log.browsers;

import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITable;
import com.cve.web.log.AbstractBrowser;
import com.cve.web.log.ObjectLink;
import com.google.common.collect.Multimap;

/**
 * Map-aware object browser.
 * @author ccox
 */
public final class MultimapBrowser
    extends AbstractBrowser
{

    public MultimapBrowser() {
        super(Multimap.class);
    }

    @Override
    public String getComponentFor(Object o) {
        Multimap map = (Multimap) o;
        UITable table = UITable.of();
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            UIRow row = UIRow.of(link(key),link(value));
            table = table.with(row);
        }
        return table.toString();
    }

    private static UIDetail link(Object o) {
        return UIDetail.of(ObjectLink.to("" + o,o));
    }

}