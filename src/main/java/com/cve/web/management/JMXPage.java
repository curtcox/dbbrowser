package com.cve.web.management;

import com.cve.web.core.PageRequest;
import com.cve.web.core.pages.AbstractPage;
import com.google.common.collect.ImmutableList;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import javax.annotation.concurrent.Immutable;
import javax.management.MBeanServer;
import static java.lang.management.ManagementFactory.*;

/**
 *
 * @author curt
 */
@Immutable
public final class JMXPage extends AbstractPage {

        /**
    * The managed bean for the class loading system of the Java virtual machine.
    */
    public final ClassLoadingMXBean	classLoading;

    /**
     * The managed bean for the compilation system of the Java virtual machine.
     */
    public final CompilationMXBean compilation;

    /**
     * A list of GarbageCollectorMXBean objects in the Java virtual machine.
     */
    public final ImmutableList<GarbageCollectorMXBean>	garbageCollector;

    /**
     * A list of MemoryManagerMXBean objects in the Java virtual machine.
     */
    public final ImmutableList<MemoryManagerMXBean>	memoryManager;

    /**
     * The managed bean for the memory system of the Java virtual machine.
     */
    public final MemoryMXBean memory;

    /**
     * A list of MemoryPoolMXBean objects in the Java virtual machine.
     */
    public final ImmutableList<MemoryPoolMXBean> memoryPool;

    /**
    * The managed bean for the operating system on which the Java virtual machine is running.
    */
    public final OperatingSystemMXBean operatingSystem;

    /**
    Returns the platform MBeanServer.
    */
    public final MBeanServer platformMBeanServer;

    /**
    Returns the managed bean for the runtime system of the Java virtual machine.
    */
    public final RuntimeMXBean runtime;

    /**
    * Returns the managed bean for the thread system of the Java virtual machine.
    */
    public final ThreadMXBean thread;

    private JMXPage() {
        super("^/jmx/");
        classLoading = getClassLoadingMXBean();
        compilation  = getCompilationMXBean();
        garbageCollector = ImmutableList.copyOf(getGarbageCollectorMXBeans());
        memoryManager = ImmutableList.copyOf(getMemoryManagerMXBeans());
        memory = getMemoryMXBean();
        memoryPool = ImmutableList.copyOf(getMemoryPoolMXBeans());
        operatingSystem = getOperatingSystemMXBean();
        platformMBeanServer = getPlatformMBeanServer();
        runtime = getRuntimeMXBean();
        thread = getThreadMXBean();
    }

    static JMXPage of() { return new JMXPage(); }

    @Override
    public AbstractPage get(PageRequest request) {
        return this;
    }
}
