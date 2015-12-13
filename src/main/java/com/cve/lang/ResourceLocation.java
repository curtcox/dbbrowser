package com.cve.lang;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;

/**
 * Where a resource is at.
 * @author curt
 */
public final class ResourceLocation {

    final File file;

    static ResourceLocation of(String className) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    static ResourceLocation of(File file) {
        return new ResourceLocation(file);
    }

    private ResourceLocation(File file) {
        this.file = file;
    }

    boolean isContainer() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    ImmutableList<String> readLines() {
        return readLines(getInput());
    }

    /**
     * Read the resource as lines.
     */
    private ImmutableList<String> readLines(InputStream in) {
          try {
              try {
                  List<String> lines = Lists.newArrayList();
                  BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                  for (String line = reader.readLine(); line!=null; line = reader.readLine()) {
                      lines.add(line);
                  }
                  return ImmutableList.copyOf(lines);
              } finally {
                  in.close();
              }
          } catch (IOException e) {
              throw new RuntimeException(e);
          }
    }

    private InputStream getInput() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return noSourceAvailable();
        }
    }

    private InputStream noSourceAvailable() {
        byte[] bytes = SourceCode.UNAVAILABLE.source.getBytes();
        return new ByteArrayInputStream(bytes);
    }
    /*
        String     resource = sourceFileResource(className);
           InputStream   in = AnnotatedClass.class.getResourceAsStream(resource);
        List<SourceCode> source = (in==null)
            ? SourceCode.NO_SOURCE_AVAILABLE
            : readLines(in)
        ;
            String     resource = sourceFileResource(className);
               InputStream   in = AnnotatedClass.class.getResourceAsStream(resource);
            List<SourceCode> source = (in==null)
                ? SourceCode.NO_SOURCE_AVAILABLE
                : readLines(in)
            ;

    */

}
