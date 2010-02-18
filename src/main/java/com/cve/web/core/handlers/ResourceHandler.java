package com.cve.web.core.handlers;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.core.ContentType;
import com.cve.web.core.Model;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.models.TextFileModel;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static com.cve.util.Check.notNull;

/**
 * A {@link RequestHandler} for serving resource files.
 * It services requests that start with "/resource/".
 * @author curt
 */
public final class ResourceHandler extends AbstractRequestHandler {

    private final Log log = Logs.of();

    public static final String PREFIX = "/resource/";

    private ResourceHandler() {
        super("^" + PREFIX);
        
    }

    static ResourceHandler of() {
        return new ResourceHandler();
    }

    @Override
    public PageResponse produce(PageRequest request) {
        log.args(request);
        String uri = request.requestURI;
        if (handles(uri)) {
            if (uri.endsWith(".java")) {
                return PageResponse.of(request,serveTextFile(request));
            }
            return PageResponse.of(request,serveResource(request),ContentType.guessByExtension(uri));
        }
        return null;
    }

    @Override
    public Model get(PageRequest request) {
        throw new UnsupportedOperationException("We overrode produce, so this should never happen.");
    }

    static byte[] serveResource(PageRequest request) {
        String       uri = request.requestURI;
        String  resource = uri.substring("/resource".length());
        InputStream   in = notNull(ResourceHandler.class.getResourceAsStream(resource),resource);
        try {
            return copyStream(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return a TextFileModel fro the given request.
     */
    static TextFileModel serveTextFile(PageRequest request) {
        String       uri = request.requestURI;
        String  resource = uri.substring("/resource".length());
        InputStream   in = notNull(ResourceHandler.class.getResourceAsStream(resource),resource);
        try {
            return TextFileModel.of(readLines(in));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Copy the input stream to the byte array
     */
    static byte[] copyStream(InputStream in) throws IOException {

          ByteArrayOutputStream out = new ByteArrayOutputStream();
          int buffer_size = 4096;
          try {
              byte[] buf = new byte[buffer_size];
              for(int read = in.read(buf,0,buffer_size); read > 0;
                      read = in.read(buf,0,buffer_size))
              {
                  out.write(buf,0,read);
              }
          } finally {
              in.close();
              out.close();
          }

          return out.toByteArray();
      }

    /**
     * Read the resource as lines.
     */
    static ImmutableList<String> readLines(InputStream in) throws IOException {
          List<String> lines = Lists.newArrayList();
          try {
              BufferedReader reader = new BufferedReader(new InputStreamReader(in));
              for (String line = reader.readLine(); line!=null; line = reader.readLine()) {
                  lines.add(line);
              }
          } finally {
              in.close();
          }

          return ImmutableList.copyOf(lines);
    }

}
