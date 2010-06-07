package com.cve.ui;

import com.cve.lang.URIObject;
import com.cve.log.Log;
import com.cve.util.URIs;
import com.cve.web.core.Icons;
import com.cve.web.core.Search;

import static com.cve.ui.UIBuilder.*;

/**
 * Small simple form for searching.
 * @author curt
 */
public final class UISearchBox {

    public static UIForm contents(Search search) {
        return uriSearch(URIs.of("search"),search);
    }

    public static UIForm uriSearch(URIObject uri, Search search) {
        UIForm form = UIForm.getAction(uri)
            .with(text(Search.FIND,search.target))
            .with(submit("find",Icons.SEARCH));
        return form;
    }

}
