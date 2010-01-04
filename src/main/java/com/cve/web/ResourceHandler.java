package com.cve.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.sql.SQLException;
import static com.cve.util.Check.notNull;
import static com.cve.log.Log.args;

/**
 * A {@link RequestHandler} for serving resource files.
 * It services requests that start with "/resource/".
 * @author curt
 */
public final class ResourceHandler extends AbstractRequestHandler {

    public static final String PREFIX = "/resource/";

    private static final ResourceHandler HANDLER = new ResourceHandler();

    private ResourceHandler() { super("^" + PREFIX); }

    static ResourceHandler of() {
        return HANDLER;
    }

    @Override
    public PageResponse produce(PageRequest request) throws IOException, SQLException {
        args(request);
        String uri = request.requestURI;
        if (handles(uri)) {
            return PageResponse.of(serveResource(request),ContentType.guessByExtension(uri));
        }
        return null;
    }

    @Override
    public Model get(PageRequest request) throws IOException {
        throw new UnsupportedOperationException("We overrode produce, so this should never happen.");
    }

    static byte[] serveResource(PageRequest request) throws IOException {
        String       uri = request.requestURI;
        String  resource = uri.substring("/resource".length());
        InputStream   in = notNull(ResourceHandler.class.getResourceAsStream(resource),resource);
        return copyStream(in);
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
