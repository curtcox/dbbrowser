package com.cve.web.log;

/**
 *
 * @author Curt
 */
final class Link {

    /**
     * Return the label from the URL, or an empty string if there is none.
     */
    static String getLabel(Object object) {
        if (object==null) {
            return "null";
        }
        if (object instanceof Class) {
            return object.toString();
        }
        return object.getClass().toString();
    }


    /**
     * Return a labeled link to the given object.
     */
    static String to(Object target) {
        String hash = ObjectRegistry.put(target).toHexString();
        String label = getLabel(target) + "/" + hash;
        return "<a href=\"" + hash + "\">" + label + "</a>";
    }

    /**
     * Return a labeled link to the given object.
     */
    static String to(String label, Object target) {
        String hash = ObjectRegistry.put(target).toHexString();
        return "<a href=\"" + hash + "\">" + label + "</a>";
    }

}
