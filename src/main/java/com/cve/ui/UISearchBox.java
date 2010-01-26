package com.cve.ui;

import com.cve.log.Log;
import com.cve.util.URIs;
import com.cve.web.Icons;
import com.cve.web.Search;
import java.net.URI;
import static com.cve.ui.UIBuilder.*;

/**
 * Small simple form for searching.
 * @author curt
 */
public final class UISearchBox {

    public static UIForm contents(Search search, Log log) {
        return uriSearch(URIs.of("search"),search,log);
    }

    public static UIForm uriSearch(URI uri, Search search, Log log) {
        UIForm form = UIForm.getAction(uri,log)
            .with(text(Search.FIND,search.target))
            .with(submit("find",Icons.SEARCH));
        return form;
    }

}
