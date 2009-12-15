package com.cve.stores;

/**
 * For reading from and writing to bytes.
 * Every object that needs to be persisted will have a corresponding IO
 * for the class.
 * This is similar to java.io.Serializable, but with several key differences:
 * <ol>
 *   <li> The IO instance handles persistence instead of the object itself.
 *   <li> Only object trees are persisted -- not object graphs.
 *   <li> There is no support for object versioning.
 * </ol>
 * You might be more comfortable with the term data access object.
 * @author Curt
 */
public interface IO<T> {
    /**
     * Turn the given bytes into an object.
     */
    T read(byte[] line);

    /**
     * Turn the given object into bytes.
     */
    byte[] write(T value);
}
