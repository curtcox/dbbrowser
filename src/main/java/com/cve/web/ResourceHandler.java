package com.cve.web;

import com.cve.log.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.cve.util.Check.notNull;

/**
 * A {@link RequestHandler} for serving resource files.
 * It services requests that start with "/resource/".
 * @author curt
 */
public final class ResourceHandler extends AbstractRequestHandler {

    private final Log log;

    public static final String PREFIX = "/resource/";

    private ResourceHandler(Log log) {
        super("^" + PREFIX,log);
        this.log = notNull(log);
    }

    static ResourceHandler of(Log log) {
        return new ResourceHandler(log);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        log.notNullArgs(request);
        String uri = request.requestURI;
        if (handles(uri)) {
            return PageResponse.of(serveResource(request),ContentType.guessByExtension(uri),log);
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
}
