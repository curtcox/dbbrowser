package com.cve.stores;

import com.cve.util.Check;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Persistence, cacheing, cache eviction and value computation are all
 * resonably separable concerns.  In practice, however, if you want to do them
 * all, it is easier to do them all in the same class, because they all
 * impact each other.  We can, however, define them separately:
 * <ol>
 *   <li> Persistence -- storing stuff we already know to disk.  We do this because
 * the local disk responds more quickly than the database server.
 *   <li> Caching -- Keeping some (right, now all) of our data in memory
 *   <li> Cache evistion -- Getting rid of cached values that aren't as valuable as
 * other values, or potentially wrong based on age.
 *   <li> Value Computation -- determining values for keys.
 * </ol>
 *
 * @author Curt
 */
public final class LocalActiveFunction<F,T> implements ActiveFunction<F,T> {

    @Override
    public CurrentResult<T> apply(F from) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
