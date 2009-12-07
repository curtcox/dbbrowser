package com.cve.stores;

/**
 * For reading from and writing to bytes.
 * Every object that needs to be persisted will have a corresponding IO
 * for the class.
 * This is similar to java.io.Serializable, but with several key differences:
 * <ol>
 *   <li> The IO instance handles persistence instead of the object itself.
 *   <li>
 * </ol>
 * You might be more comfortable with the term data access object.
 * @author Curt
 */
public interface IO<T> {
    T parse(byte[] line);
    byte[] format(T value);
}
