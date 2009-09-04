package com.cve.ui;

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

    public static UIForm contents(String contents) {
        return uriContents(URIs.of("search"),contents);
    }

    public static UIForm uriContents(URI uri, String contents) {
        UIForm form = UIForm.getAction(uri)
            .with(text(Search.FIND,contents))
            .with(submit("find",Icons.SEARCH));
        return form;
    }

}
