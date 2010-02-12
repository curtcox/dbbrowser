package com.cve.lang;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

/**
 *
 * @author curt
 * Some original code was taken from:
 * Memory Monitoring with Java SE 5
 * http://www.informit.com/guides/content.aspx?g=java&seqNum=249
 */
public class Memory {

    static void dumpInfo() {
      print( "DUMPING MEMORY INFO" );

      // Read MemoryMXBean
      MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
      print( "Heap Memory Usage: " + memorymbean.getHeapMemoryUsage() );
      print( "Non-Heap Memory Usage: " + memorymbean.getNonHeapMemoryUsage() );

      // Read Garbage Collection information
      List<GarbageCollectorMXBean> gcmbeans = ManagementFactory.getGarbageCollectorMXBeans();
      for( GarbageCollectorMXBean gcmbean : gcmbeans ) {
        print( "\nName: " + gcmbean.getName() );
        print( "Collection count: " + gcmbean.getCollectionCount() );
        print( "Collection time: " + gcmbean.getCollectionTime() );
        print( "Memory Pools: " );
        for(String memoryPoolName : gcmbean.getMemoryPoolNames()) {
             print( "\t" + memoryPoolName);
        }
      }

      // Read Memory Pool Information
      print( "Memory Pools Info" );
      List<MemoryPoolMXBean> mempoolsmbeans = ManagementFactory.getMemoryPoolMXBeans();
      for( MemoryPoolMXBean mempoolmbean : mempoolsmbeans ) {
        print( "\nName: " + mempoolmbean.getName() );
        print( "Usage: " + mempoolmbean.getUsage() );
        print( "Collection Usage: " + mempoolmbean.getCollectionUsage() );
        print( "Peak Usage: " + mempoolmbean.getPeakUsage() );
        print( "Type: " + mempoolmbean.getType() );
        print( "Memory Manager Names: " ) ;
        for(String memManagerName : mempoolmbean.getMemoryManagerNames()) {
            print( "\t" + memManagerName);
        }
        print( "\n" );
      }
  }

  static void print(String message) {
      System.out.println(message);
  }

  public static void main( String[] args ) {
    dumpInfo();
    // Tweak memory a little bit
    for( int i=0; i<1000000; i++ ) {
      String s = "My String " + i;
    }
    dumpInfo();
  }

    /**
     * Return true, if memory is currently low, and false otherwise.
     * @return
     */
    public static boolean isLow() {
        for(MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (pool.isUsageThresholdExceeded()) {
                return true;
            }
        }
        return false;
    }
}
