package com.cve.web.management.browsers;

import com.cve.lang.ExecutableElement;
import com.cve.lang.ExecutableMethod;
import com.cve.ui.HTMLTags;
import com.cve.lang.Executables;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.Check;
import com.cve.ui.UIComposite;
import com.cve.ui.UIElement;
import com.cve.ui.UILabel;
import com.cve.ui.UITable;
import com.cve.ui.UITableBuilder;
import com.cve.ui.UITableCell;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableHeader;
import com.cve.ui.UITableRow;
import com.cve.ui.UITaskPane;
import com.cve.web.management.ObjectLinks;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import com.google.common.collect.Multimap;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * A graphical browser for objects.
 * @author ccox
 */
public final class ObjectBrowser {

    /**
     * The object we browse
     */
    private final Object target;

    /**
     * For creating links to other objects.
     */
    private final ObjectLinks link;

    /**
     * What visibility to show.  Public, private, etc...
     */
    private final ModifierMask mask;

    private final Log log = Logs.of();

    /**
     * For creating HTML tags
     */
    private final HTMLTags tags;

private ObjectBrowser(Object o) {
    this(o,ModifierMask.PRIVATE);
}

private ObjectBrowser(Object target, ModifierMask mask) {
    this.mask   = Check.notNull(mask);
    this.target = target;
    link = ObjectLinks.of();
    tags = HTMLTags.of();
}

public static ObjectBrowser of(Object o) {
    return new ObjectBrowser(o);
}

/**
 * Return the HTML for our target object.
 * 
 */
public UIElement toHTML() {
    UILabel toString = (target==null)
        ? UILabel.of("null")
        : UILabel.of(target.toString());

    return UITaskPane.of(
        checkSpecialHandling(target),
        h1(link.to(target)),
        h1("toString")     , toString,
        h1("Fields")       , showFields() ,
        h1("Constructors") , showConstructors() ,
        h1("Methods")      , showMethods()
    );

} // show Object

UIElement h1(UIElement e) { return e; }
UILabel h1(String s) { return UILabel.of(s); }
UITableDetail td(String s) { return UITableDetail.of(s); }
UITableHeader th(String s) { return UITableHeader.of(s); }
UITableDetail td(UIElement s) { return UITableDetail.of(s); }
UITableDetail td(UIElement... e) { return UITableDetail.of(UIComposite.of(e)); }
UITableHeader th(UIElement s) { return UITableHeader.of(s); }

/**
 * Return a new browser for this object using public visibility.
 */
static ObjectBrowser newPublic(Object o) {
    return new ObjectBrowser(o);
}

/**
 * Return a new browser for this object using private visibility.
 */
static ObjectBrowser newPrivate(Object o) {
    return new ObjectBrowser(o,ModifierMask.PRIVATE);
}

/**
 * Show the given object in a new frame using public visibility.
 * @param o the object to show
 */
static UIElement showPublic(Object o) {
    return newPublic(o).toHTML();
}

/**
 * Show the given object in a new frame using private visibility.
 * @param o the object to show
 */
public static UIElement showPrivate(Object o) {
    return newPrivate(o).toHTML();
}

/**
 * Return the output of any custom browsers that handle this object.
 */
private UIElement checkSpecialHandling(Object o) {
    List<UIElement> elements = Lists.newArrayList();
    for (CustomBrowser browser : CustomBrowserRegistry.of().getBrowsersFor(o)) {
        UIElement title = UILabel.of(o.getClass().getName() + System.identityHashCode(o));
        elements.add(title);
        UIElement component = browser.getComponentFor(o);
        elements.add(component);
    }
    return UIComposite.of(elements);
}

/**
 * Show all of the fields for this link.
 */
UIElement showFields() {
    Object o = target;
    Class  c = o.getClass();
    Field[] fields = c.getDeclaredFields();
    UITableBuilder out = UITableBuilder.of();
    UITableRow header = UITableRow.of(th("Modifiers"),th("Type"),th("Name"),th("Value"));
    out.add(header);
    for (Field f : fields) {
        out.add(showField(f,o));
    }
    out.add(header);
    return out.build();
}

/**
 * Show a single field.
 */
private UITableRow showField(Field f, Object o) {
    if (!mask.passes(f)) {
        return null;
    }
    Object value = null;
    String valueString;
    f.setAccessible(true);
    Class c = f.getType();
    try {
        value = f.get(o);
        valueString = (value==null) ? "null" : value.toString();
    } catch (IllegalAccessException e) {
        valueString = " inaccessible";
    } catch (NullPointerException e) {
        valueString = " null pointer ";
    }
    List<UITableCell> row = Lists.newArrayList();
    row.add(td(modifiers(f.getModifiers())));
    row.add(td(typeName(c)));
    String fname = f.getName();
    if (value==null) {
        row.add(td(fname));
    } else {
        row.add(td(link.to(fname,value)));
    }
    row.add(td(valueString));
    return UITableRow.of(row);
}

/**
 * Return HTML describing all of the constructors for the class of our target.
 */
UIElement showConstructors() {
    Class c = target.getClass();
    List<ExecutableElement> list = Lists.newArrayList();
    for (Constructor constructor : c.getConstructors()) {
        if (mask.passes(constructor)) {
            list.add(Executables.of(constructor));
        }
    }
    return showExecutables(list);
}

/**
 * Return HTML describing all of the methods of our target.
 */
UIElement showMethods() {
    Class c = target.getClass();
    List<ExecutableElement> list = Lists.newArrayList();
    for (Method method : c.getMethods()) {
        if (mask.passes(method)) {
            list.add(Executables.of(method));
        }
    }
    return showExecutables(list);
}

/**
 * Return HTML describing all of the executables given.
 */
UIElement showExecutables(Collection<ExecutableElement> executables) {
    List<UIElement> out = Lists.newArrayList();
    Multimap<Class,ExecutableElement> grouped = HashMultimap.create();
    for (ExecutableElement executable : executables) {
        Class clazz = executable.getDeclaringClass();
        grouped.put(clazz,executable);
    }
    for (Class clazz : grouped.keySet()) {
        out.add(h1(link.to(clazz.getName(),clazz)));
        out.add(showExecutablesFromOneClass(grouped.get(clazz)));
    }
    return UITaskPane.of(out);
}


UITable showExecutablesFromOneClass(Collection<ExecutableElement> executables) {
    UITableBuilder out = UITableBuilder.of();
    UITableRow headerRow = UITableRow.of(
        th("Modifiers"),th("Return Type"),th("Return Value"),th("Name"),th("Arguments"),th("Throws")
    );
    out.add(headerRow);
    for (ExecutableElement executable : executables) {
        out.add(showExecutable(executable));
    }
    out.add(headerRow);
    return out.build();
}

/**
 * Return HTML for the return type of the given executable
 */
UIElement returnType(ExecutableElement executable){
    // only show return type for methods
    if (executable instanceof ExecutableMethod) {
        return typeName(executable.returnType);
    } else {
        return UILabel.of("");
    }
}  

/**
 * Return HTML for the value of the given executable.
 */
UIElement returnValue(ExecutableElement executable){
    if (executable.name.startsWith("get") && executable.parameterTypes.isEmpty()) {
        if (executable instanceof ExecutableMethod) {
            Method method = ((ExecutableMethod) executable).inner;
            method.setAccessible(true);
            try {
                Object result = method.invoke(target, null);
                return link.to("" + result,result);
            } catch (IllegalAccessException e) {
                return link.to("IllegalAccessException" + e,e);
            } catch (InvocationTargetException e) {
                return link.to("InvocationTargetException" + e,e);
            }
        }
    }
    return UILabel.of("?");
}

/**
 * Print the modifiers, return type, name, parameter types, and exception
 *  type of an executable (method or constructor).
 */
UITableRow showExecutable(ExecutableElement method){

    final ImmutableList<Class> parameters = method.parameterTypes;
    final ImmutableList<Class> exceptions = method.exceptionTypes;

    return
         UITableRow.of(
             td( modifiers(method.getModifiers())     ),
             td( returnType(method)                   ),
             td( returnValue(method)                  ),
             td( method.name                          ),
             td( UILabel.of("("),csv(classes(parameters)), UILabel.of(")") ),
             td( csv(classes(exceptions))             )
         );
}  // print method

/**
 * Return comma separated values.
 */
static UIComposite csv(List<UIElement> list) {
    List<UIElement> out = Lists.newArrayList();
    for (int i=0; i<list.size(); i++) {
        out.add(list.get(i));
        if (i<list.size() - 1) {
            out.add(UILabel.of(","));
        }
    }
    return UIComposite.of(out);
}

/**
 * Return one string for each exception that could be thrown.
 */
ImmutableList<UIElement> classes(final ImmutableList<Class> exceptions) {
    List<UIElement> list = Lists.newArrayList();
    for (Class eClass : exceptions) {
        list.add(link.to(eClass.getName(),eClass));
    }
    return ImmutableList.copyOf(list);
}

/**
 * Return the name of an interface or primitive type, handling arrays.
 */
UIElement typeName(Class t) {
    String brackets = "";
    while(t.isArray()) {
        brackets += "[]";
        t = t.getComponentType();
    }
    final String name = t.getName();
    if (t.isPrimitive()) {
        return UILabel.of(name + brackets);
    } else {
        return UIComposite.of(link.to(name,t) , UILabel.of(brackets));
    }
} 

/**
 * Return a string version of modifiers, handling spaces nicely.
 */
public static String modifiers(int m) {
    if (m == 0) return "";
    return Modifier.toString(m) + " ";
} 

public static void main(String[] args) {
    showPublic("Public");
}

} // Object Browser
