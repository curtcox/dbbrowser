package com.cve.stores;

import com.cve.util.Check;
import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
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
public final class ActiveFunction<F,T> implements SQLFunction<F,T> {

    /**
     * When was the last time any of our data changed?
     */
    private volatile long lastUpdate = System.currentTimeMillis();

    private final IO<F,T> io;
    private final File file;
    private final SQLFunction<F,T> mapper;
    private final Map<F,T> values = Maps.newHashMap();
    private final Executor executor = Executors.newSingleThreadExecutor();

    private ActiveFunction(File file, IO io, SQLFunction mapper) {
        this.file = Check.notNull(file);
        this.io = Check.notNull(io);
        this.mapper = Check.notNull(mapper);
        new HashMap();
    }

    public static SQLFunction fileIOFunc(File file, IO io, SQLFunction mapper) throws IOException {
        ActiveFunction func = new ActiveFunction(file,io,mapper);
        if (file.exists()) {
            func.load();
        }
        return func;
    }

    @Override
    public T apply(F from) throws SQLException {
        F f = (F) from;
        synchronized (values) {
            if (values.containsKey(f)) {
                return values.get(f);
            }
            T value = mapper.apply(f);
            values.put(f, value);
            lastUpdateNow();
            return value;
        }
    }

    private void lastUpdateNow() {
        lastUpdate = System.currentTimeMillis();
    }

    private void load() throws IOException {
        synchronized (values) {
            values.clear();
            BufferedReader lines = new BufferedReader(new FileReader(file));
            boolean isKey = true;
            F key = null;
            T value = null;
            for (String line = lines.readLine(); line!=null; line = lines.readLine()) {
                if (isKey) {
                    key = io.readKey(line);
                } else {
                    value = io.readValue(line);
                    values.put(key, value);
                }
                isKey = !isKey;
            }
        }
    }
}
