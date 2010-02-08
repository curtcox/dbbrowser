package com.cve.web.log;

import com.cve.lang.ExecutableElement;
import com.cve.lang.ExecutableConstructor;
import com.cve.lang.ExecutableMethod;
import com.cve.html.HTMLTags;
import com.cve.lang.Executables;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.Check;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import com.google.common.collect.Multimap;
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

    private final ObjectLink link;

    /**
     * What visibility to show.  Public, private, etc...
     */
    private final Mask mask;

    private final Log log = Logs.of();

    private final HTMLTags tags;

/**
 * Something that limits the kind of methods and variables we are interested in.
 *
 * @author ccox
 */
public static enum Mask {

    /**
     * Public or protected, but not private.
     */
    PUBLIC(Modifier.PUBLIC | Modifier.PROTECTED, Modifier.PRIVATE),

    /**
     * Everything.
     */
    PRIVATE(0,0);

    private final int required;
    private final int prohibited;

    Mask(int required, int prohibited) {
        this.required   = required;
        this.prohibited = prohibited;
    }

    /**
     * Return true, if the member passes this mask.
     */
    boolean passes(Member member) {
        int modifiers = member.getModifiers();
        // if there are some requirements and they aren't met, fail
        if (required!=0 && ((modifiers & required) == 0)) {
            return false;
        }
        if (prohibited!=0 && ((modifiers & prohibited) != 0)) {
            return false;
        }
        return true;
    }
} // Mask


/**
 * A DeferredMethod is a wrapper around a method so that it can be executed
 * later.  It is necessary, because we want to be able to browse any objects 
 * that would be returned by a method invocation, but we don't want to actually
 * invoke the method, unless the link for that method is followed. 
 */
private static class DeferredMethod {

    private Object target;
    private Method method;

    DeferredMethod(Object target, Method method) {
        Check.notNull(target);
        Check.notNull(method);
        this.target = target;
        this.method = method;
    }

    Object invoke() {
        try {
            method.setAccessible(true);
            return method.invoke(target,null);
        } catch (Throwable t) {
            return t;
        }
    }
} // Deferred Method

public ObjectBrowser(Object o) {
    this(o,Mask.PRIVATE);
}

public ObjectBrowser(Object target, Mask mask) {
    this.mask   = mask;
    this.target = target;
    link = ObjectLink.of();
    tags = HTMLTags.of();
}

/**
 * Return the HTML for our target object.
 * 
 */
String toHTML() {
    StringBuffer out = new StringBuffer();
    out.append(checkSpecialHandling(target));
    out.append(h1(link.to(target)));

    out.append(h1("toString"));
    out.append("" + target);

    if (target==null) {
        return out.toString();
    }
    
    out.append(h1("Fields"));
    out.append(showFields());

    out.append(h1("Constructors"));
    out.append(showConstructors());

    out.append(h1("Methods"));
    out.append(showMethods());

    return out.toString();
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
private static ObjectBrowser newPublic(Object o) {
    return new ObjectBrowser(o);
}

/**
 * Return a new browser for this object using private visibility.
 */
private static ObjectBrowser newPrivate(Object o) {
    return new ObjectBrowser(o,Mask.PRIVATE);
}

/**
 * Show the given object in a new frame using public visibility.
 * @param o the object to show
 */
private static String showPublic(Object o) {
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
private String showFields() {
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

private String showConstructors() {
    Class c = target.getClass();
    List<ExecutableElement> list = Lists.newArrayList();
    for (Constructor constructor : c.getConstructors()) {
        if (mask.passes(constructor)) {
            list.add(Executables.of(constructor));
        }
    }
    return showExecutables(list);
}

private String showMethods() {
    Class c = target.getClass();
    List<ExecutableElement> list = Lists.newArrayList();
    for (Method method : c.getMethods()) {
        if (mask.passes(method)) {
            list.add(Executables.of(method));
        }
    }
    return showExecutables(list);
}

private String showExecutables(Collection<ExecutableElement> executables) {
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

private String showExecutablesFromOneClass(Collection<ExecutableElement> executables) {
    StringBuffer out = new StringBuffer();
    String headerRow = tr(th("Modifiers") + th("Return Type") + th("Name")+ th("Arguments")+ th("Throws"));
    out.append(headerRow);
    for (ExecutableElement executable : executables) {
        out.append(showExecutable(executable));
    }
    out.append(headerRow);
    return borderTable(out.toString());
}

/**
 * Print the modifiers, return type, name, parameter types, and exception
 *  type of an executable (method or constructor).
 */
private String showExecutable(ExecutableElement method){

    final Class   returnType = method.getReturnType();
    final Class[] parameters = method.getParameterTypes();
    final Class[] exceptions = method.getExceptionTypes();

    StringBuffer r = new StringBuffer();
    r.append(td(modifiers(method.getModifiers())));

    // only show return type for methods
    if (method instanceof ExecutableMethod) {
        r.append(td(typeName(returnType)));
    }

    // method name
    if (method instanceof ExecutableConstructor || parameters.length>0) {
        r.append(td(method.getName()));
    } else {
        Method m = ((ExecutableMethod) method).getMethod();
        r.append(td(link.to(m.getName(),new DeferredMethod(target,m))));
    }

    // arguments
    StringBuffer args = new StringBuffer("(");
    for (int i=0; i<parameters.length; i++) {
        Class pClass = parameters[i];
        args.append(link.to(pClass.getName(),pClass));
        if (i + 1 < parameters.length) {
            args.append(",");
        }
    }
    args.append(")");
    r.append(td(args.toString()));

    // throws
    if (exceptions.length>0) {
        StringBuffer all = new StringBuffer();
        for (int i=0; i<exceptions.length; i++) {
            Class eClass = exceptions[i];
            all.append(link.to(eClass.getName(),eClass));
            if (i + 1 < exceptions.length) {
                all.append(",");
            }
        }
        r.append(td(all.toString()));
    }

    return tr(r.toString());
}  // print method 


/**
 * Return the name of an interface or primitive type, handling arrays.
 */
private String typeName(Class t) {
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
