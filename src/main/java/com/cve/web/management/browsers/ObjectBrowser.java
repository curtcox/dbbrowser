package com.cve.web.management.browsers;

import com.cve.lang.ExecutableElement;
import com.cve.lang.ExecutableMethod;
import com.cve.html.HTMLTags;
import com.cve.lang.Executables;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.Check;
import com.cve.util.Strings;
import com.cve.web.management.ObjectLink;
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
    private final ObjectLink link;

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
    link = ObjectLink.of();
    tags = HTMLTags.of();
}

public static ObjectBrowser of(Object o) {
    return new ObjectBrowser(o);
}

/**
 * Return the HTML for our target object.
 * 
 */
public String toHTML() {
    if (target==null) {
        return "null";
    }

    return
        checkSpecialHandling(target) +
        h1(link.to(target)) +
        h1("toString")     + target +
        h1("Fields")       + showFields() +
        h1("Constructors") + showConstructors() +
        h1("Methods")      + showMethods()
    ;

} // show Object

String h1(String s) { return tags.h1(s); }
String h2(String s) { return tags.h2(s); }
String tr(String s) { return tags.tr(s); }
String td(String s) { return tags.td(s); }
String th(String s) { return tags.th(s); }
String borderTable(String s) { return tags.borderTable(s); }

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
static String showPublic(Object o) {
    return newPublic(o).toHTML();
}

/**
 * Show the given object in a new frame using private visibility.
 * @param o the object to show
 */
public static String showPrivate(Object o) {
    return newPrivate(o).toHTML();
}

/**
 * Return the output of any custom browsers that handle this object.
 */
private String checkSpecialHandling(Object o) {
    StringBuffer out = new StringBuffer();
    for (CustomBrowser browser : CustomBrowserRegistry.of().getBrowsersFor(o)) {
        String title = o.getClass().getName() + System.identityHashCode(o);
        out.append(title);
        String component = browser.getComponentFor(o);
        out.append(component);
    }
    return out.toString();
}

/**
 * Show all of the fields for this link.
 */
String showFields() {
    Object o = target;
    Class  c = o.getClass();
    Field[] fields = c.getDeclaredFields();
    StringBuffer out = new StringBuffer();
    String headerRow = tr(th("Modifiers") + th("Type") + th("Name") + th("Value"));
    out.append(headerRow);
    for (Field f : fields) {
        out.append(showField(f,o));
    }
    out.append(headerRow);
    return borderTable(out.toString());
}

/**
 * Show a single field.
 */
private String showField(Field f, Object o) {
    if (!mask.passes(f)) {
        return "";
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
    StringBuffer row = new StringBuffer();
    row.append(td(modifiers(f.getModifiers())));
    row.append(td(typeName(c)));
    String fname = f.getName();
    if (value==null) {
        row.append(td(fname));
    } else {
        row.append(td(link.to(fname,value)));
    }
    row.append(td(valueString) + "\n");
    return tr(row.toString());
}

/**
 * Return HTML describing all of the constructors for the class of our target.
 */
String showConstructors() {
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
String showMethods() {
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
String showExecutables(Collection<ExecutableElement> executables) {
    StringBuffer out = new StringBuffer();
    Multimap<Class,ExecutableElement> grouped = HashMultimap.create();
    for (ExecutableElement executable : executables) {
        Class clazz = executable.getDeclaringClass();
        grouped.put(clazz,executable);
    }
    for (Class clazz : grouped.keySet()) {
        out.append(h2(link.to(clazz.getName(),clazz)));
        out.append(showExecutablesFromOneClass(grouped.get(clazz)));
    }
    return out.toString();
}


String showExecutablesFromOneClass(Collection<ExecutableElement> executables) {
    StringBuffer out = new StringBuffer();
    String headerRow = tr(th("Modifiers") + th("Return Type") + th("Return Value") + th("Name")+ th("Arguments")+ th("Throws"));
    out.append(headerRow);
    for (ExecutableElement executable : executables) {
        out.append(showExecutable(executable));
    }
    out.append(headerRow);
    return borderTable(out.toString());
}

/**
 * Return HTML for the return type of the given executable
 */
String returnType(ExecutableElement executable){
    // only show return type for methods
    if (executable instanceof ExecutableMethod) {
        return typeName(executable.returnType);
    } else {
        return "";
    }
}  

/**
 * Return HTML for the value of the given executable.
 */
String returnValue(ExecutableElement executable){
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
    return "?";
}

/**
 * Print the modifiers, return type, name, parameter types, and exception
 *  type of an executable (method or constructor).
 */
String showExecutable(ExecutableElement method){

    final ImmutableList<Class> parameters = method.parameterTypes;
    final ImmutableList<Class> exceptions = method.exceptionTypes;

    return
         tr(
             td( modifiers(method.getModifiers())     ) +
             td( returnType(method)                   ) +
             td( returnValue(method)                  ) +
             td( method.name                          ) +
             td( "(" + csv(classes(parameters)) + ")" ) +
             td( csv(classes(exceptions))             )
         );
}  // print method

/**
 * Return comma separated values.
 */
static String csv(List<String> list) {
    return Strings.separated(list, ",");
}

/**
 * Return one string for each exception that could be thrown.
 */
ImmutableList<String> classes(final ImmutableList<Class> exceptions) {
    List<String> list = Lists.newArrayList();
    for (Class eClass : exceptions) {
        list.add(link.to(eClass.getName(),eClass));
    }
    return ImmutableList.copyOf(list);
}

/**
 * Return the name of an interface or primitive type, handling arrays.
 */
String typeName(Class t) {
    String brackets = "";
    while(t.isArray()) {
        brackets += "[]";
        t = t.getComponentType();
    }
    final String name = t.getName();
    if (t.isPrimitive()) {
        return name + brackets;
    } else {
        return link.to(name,t) + brackets;
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
