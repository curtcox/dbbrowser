package com.cve.lang;

import java.util.regex.Pattern;

/**
 * A regular expression.
 * @author curt
 */
public final class RegEx
    implements Comparable<RegEx>
{
    /**
     * The stuff we handle.
     */
    private final Pattern pattern;

    private final String value;

    private RegEx(String value) {
        this.value = value;
        pattern = Pattern.compile(value);
    }

    public static RegEx of(String value) {
        return new RegEx(value);
    }

    public boolean matches(String candidate) {
        return pattern.matcher(candidate).find();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        RegEx other = (RegEx) o;
        return value.equals(other.value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(RegEx t) {
        RegEx other = (RegEx) t;
        return value.compareTo(other.value);
    }
}
