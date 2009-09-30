package com.cve.stores;

import com.cve.util.Check;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Persistence, cacheing, cache eviction and value computation are all
 * resonably separable concerns.  In practice, however, if you want to do them
 * all, it is easier to do them all in the same class, because they all
 * impact each other.  We can, however, define them separately:
 * <ol>
 *   <li> Persistence
 *   <li> Caching
 *   <li> Cache evistion
 *   <li> Value Computation
 * </ol>
 *
 * @author Curt
 */
public final class ActiveFunction<F,T> implements Function<F,T> {

    private final IO io;
    private final File file;
    private final Function<F,T> mapper;
    private final Map<F,T> values = Maps.newHashMap();
    private final Executor executor = Executors.newSingleThreadExecutor();

    private ActiveFunction(File file, IO io, Function mapper) {
        this.file = Check.notNull(file);
        this.io = Check.notNull(io);
        this.mapper = Check.notNull(mapper);
        new HashMap();
    }

    public static Function fileIOFunc(String file, IO io, Function mapper) {
        return new ActiveFunction(new File(file),io,mapper);
    }

    public static Function fileIOFunc(File file, IO io, Function mapper) {
        return new ActiveFunction(file,io,mapper);
    }

    @Override
    public T apply(F from) {
        F f = (F) from;
        synchronized (values) {
            if (values.containsKey(f)) {
                return values.get(f);
            }
            T value = mapper.apply(f);
            values.put(f, value);
            return value;
        }
    }

    static UnsupportedOperationException no() {
        return new UnsupportedOperationException("Not supported yet.");
    }

}
