package com.cve.lang;

/**
 * Like a string, but requiring less memory.
 * This class exist, so that we use a lot of strings.
 * Any memory savings made here will be greatly magnified.
 * @author curt
 */
public final class Stringy
    implements Comparable<Stringy>
{

    private final String value;

    private Stringy(String value) {
        this.value = value;
    }

    public static Stringy of(String value) {
        return new Stringy(value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        Stringy other = (Stringy) o;
        return value.equals(other.value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(Stringy t) {
        Stringy other = (Stringy) t;
        return value.compareTo(other.value);
    }
}
