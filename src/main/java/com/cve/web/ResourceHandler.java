package com.cve.web;

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

    public static final String PREFIX = "/resource/";

    private static final ResourceHandler HANDLER = new ResourceHandler();

    private ResourceHandler() { super("^" + PREFIX); }

    static ResourceHandler newInstance() {
        return HANDLER;
    }

    public PageResponse get(PageRequest request) throws IOException {
        return serveResource(request);
    }

    static PageResponse serveResource(PageRequest request) throws IOException {
        String       uri = request.getRequestURI();
        String  resource = uri.substring("/resource".length());
        InputStream   in = notNull(ResourceHandler.class.getResourceAsStream(resource),resource);
        return PageResponse.of(copyStream(in));
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
