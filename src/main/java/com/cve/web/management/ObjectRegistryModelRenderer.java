package com.cve.web.management;

import com.cve.lang.AnnotatedStackTrace;
import com.cve.lang.Objects;
import com.cve.log.Logs;
import com.cve.ui.UITableDetail;
import com.cve.ui.UIElement;
import com.cve.ui.UILabel;
import com.cve.ui.UILink;
import com.cve.ui.UITableRow;
import com.cve.ui.UISeries;
import com.cve.ui.UITableBuilder;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.ModelHtmlRenderer;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author curt
 */
final class ObjectRegistryModelRenderer implements ModelHtmlRenderer {

    /**
     * Use the factory
     */
    private ObjectRegistryModelRenderer() {}

    public static ObjectRegistryModelRenderer of() {
        return new ObjectRegistryModelRenderer();
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        ObjectRegistryModel registry = (ObjectRegistryModel) model;
        return HtmlPage.guts(render(registry));
    }

    private String render(ObjectRegistryModel page) {
        UITableBuilder ui = UITableBuilder.of();
        ImmutableMultimap<Class,Object> objects = page.objects;
        for (Class c : objects.keySet()) {
            ui.add(row(objects,c));
        }
        return ui.build().toString();
    }

    UITableRow row(ImmutableMultimap<Class,Object> objects, Class c) {
        List<UIElement> list = Lists.newArrayList();
        int i = 1;
        long memory = 0;
        boolean computable = true;
        AnnotatedStackTrace trace = null;
        for (Object o : objects.get(c)) {
            list.add(link("" + i,c));
            list.add(UILabel.of(" "));
            if (computable) {
                try {
                    memory += Objects.sizeOf(o);
                } catch (IOException e) {
                    computable = false;
                    trace = Logs.of().annotatedStackTrace(e);
                }
            }
            i++;
        }
        UISeries links = UISeries.of(list);
        if (computable) {
            return UITableRow.of(
                detail(link(c.getName(),c)),
                detail(label(list.size())),
                detail(label(memory)),
                detail(links)
            );
        }
        return UITableRow.of(
            detail(link(c.getName(),c)),
            detail(label(list.size())),
            detail(link("Uncomputable",trace)),
            detail(links)
        );
    }

    UILink link(String label, Object target) {
        return UILink.to(label,target);
    }

    UITableDetail detail(UIElement e) {
        return UITableDetail.of(e);
    }

    UILabel label(long i) {
        return UILabel.of("" + i);
    }

}
