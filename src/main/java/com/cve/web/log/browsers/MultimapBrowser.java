package com.cve.web.log.browsers;

import com.cve.log.Log;
import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITable;
import com.cve.web.log.AbstractBrowser;
import com.cve.web.log.ObjectLink;
import com.google.common.collect.Multimap;
import static com.cve.util.Check.notNull;

/**
 * Map-aware object browser.
 * @author ccox
 */
public final class MultimapBrowser
    extends AbstractBrowser
{

    Log log;

    private MultimapBrowser(Log log) {
        super(Multimap.class);
        this.log = notNull(log);
    }

    public static MultimapBrowser of(Log log) {
        return new MultimapBrowser(log);
    }

    @Override
    public String getComponentFor(Object o) {
        Multimap map = (Multimap) o;
        UITable table = UITable.of(log);
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            UIRow row = UIRow.of(log,link(key),link(value));
            table = table.with(row);
        }
        return table.toString();
    }

    private UIDetail link(Object o) {
        return UIDetail.of(ObjectLink.of(log).to("" + o,o),log);
    }

}
