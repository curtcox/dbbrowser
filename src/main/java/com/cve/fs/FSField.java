package com.cve.fs;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.util.Canonicalizer;
import com.cve.web.fs.FSURICodec;
import com.google.common.collect.ImmutableList;
import java.net.URI;
import static com.cve.util.Check.notNull;
/**
 *
 * @author curt
 */
public final class FSField {

    /**
     * The path this field is on.
     */
    public final FSPath  path;

    /**
     * The name of this field.
     */
    public final String name;

    /**
     * For limiting the number of objects we produce.
     */
    private static final Canonicalizer<FSField> CANONICALIZER = Canonicalizer.of();

    /**
     * The field to use for "all fields".
     * This is used with count(*).
     */
    public static final FSField ALL = pathName(FSPath.NULL,"*");

    /**
     * Use the factories.
     */
    private FSField(FSPath path, String name) {
        this.path   = notNull(path);
        this.name    = notNull(name);
    }

    private static FSField canonical(FSField field) {
        return CANONICALIZER.canonical(field);
    }

    public static FSField pathName(FSPath path, String name) {
        return canonical(new FSField(path,name));
    }

    public FSLineFilter filterValue(FSValue value) {
        return FSLineFilter.of(this, value);
    }

    public static FSField parse(FSServer server, ImmutableList<FSPath> paths, String fullfieldName) {
        notNull(server,fullfieldName);
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static boolean startsWithDigit(String string) {
        char c = string.charAt(0);
        return c == '0' || c == '9' || (c > '0' && c < '9');
    }

    /**
     * Return database.pathName.fieldName.
     * So, full doesn't mean with server name.
     */
    public String fullName() {
        if (this==ALL) {
            return "*";
        }
        return path.server.uri + "." + path.name + "." + name;
    }

    @Override
    public   int hashCode() { return path.hashCode() ^ name.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        if (o==this) {
            return true;
        }
        FSField other = (FSField) o;
        return path.equals(other.path) && name.equals(other.name);
    }

    @Override
    public String toString() {
        return "name=" + name + " path=" + path;
    }

    public Link linkTo() {
        URI target = FSURICodec.encode(this);
        String markedName = name;
        Label text = Label.of(markedName);
        return Link.textTarget(text, target);
    }


}
