package com.cve.stores;

import com.cve.util.Check;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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
public final class ActiveFunction<F,T> implements SQLFunction<F,T> {

    /**
     * When was the last time any of our data changed?
     */
    volatile long lastUpdate = now();

    /**
     * When was the last time our data was saved?
     */
    volatile long lastSave = lastUpdate;

    /**
     * For reading and writing keys and values.
     */
    final MapIO io;

    /**
     * Where we read from and write to.
     */
    final File file;

    /**
     * How we produce new values.
     */
    final SQLFunction<F,T> mapper;

    /**
     * The keys and values we know about.
     */
    final Map<F,T> values = Maps.newHashMap();

    static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    private ActiveFunction(File file, MapIO io, SQLFunction mapper) {
        this.file = Check.notNull(file);
        this.io = Check.notNull(io);
        this.mapper = Check.notNull(mapper);
    }

    /**
     * Create a new Active function given the required arguments.
     */
    public static SQLFunction fileIOFunc(File file, MapIO io, SQLFunction mapper) throws IOException {
        ActiveFunction func = new ActiveFunction(file,io,mapper);
        if (file.exists()) {
            try {
               func.load();
            } catch (Throwable t) {
                boolean successfullyDeleted = file.delete();
                if (!successfullyDeleted) {
                    throw new IOException();
                }
            }
        }
        new PeriodicSave(func).schedule();
        return func;
    }

    /**
     * Runnable to periodically save our data.
     */
    static final class PeriodicSave implements Runnable {
        final ActiveFunction func;
        PeriodicSave(ActiveFunction func) {
            this.func = func;
        }

        @Override
        public void run() {
            try {
                if ((func.lastUpdate != func.lastSave) && (now() - func.lastSave > 5 * 1000)) {
                    func.save();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                schedule();
            }
        }

        void schedule() {
            EXECUTOR.execute(this);
        }
    }

    static long now() {
        return System.currentTimeMillis();
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
            lastUpdate = now();
            return value;
        }
    }

    /**
     * Load all existing values from disk.
     */
    void load() throws IOException {
    }

    /**
     * Save all of our lines.
     * @throws IOException
     */
    void save() throws IOException {
        lastSave = now();
    }
}
