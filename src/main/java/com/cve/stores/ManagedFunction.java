package com.cve.stores;

/**
 * Persistence, caching, cache eviction and value computation are all
 * reasonably separable concerns.  In practice, however, if you want to do them
 * all, it is easier to do them all in the same class, because they all
 * impact each other.  We can, however, define them separately:
 * <ol>
 *   <li> Persistence -- storing stuff we already know to disk.  We do this because
 * the local disk responds more quickly than the database server.
 *   <li> Caching -- Keeping some (right, now all) of our data in memory
 *   <li> Cache eviction -- Getting rid of cached values that aren't as valuable as
 * other values, or potentially wrong based on age.
 *   <li> Value Computation -- determining values for keys.
 * </ol>
 *
 * @author Curt
 */
public interface ManagedFunction<F,T> {

    interface Factory<F,T> {
        ManagedFunction<F,T> of(UnpredictableFunction<F,T> f, Class domain, Class range, T nullValue);
    }
    
  /**
   */
  CurrentValue<T> apply(F from);

}
