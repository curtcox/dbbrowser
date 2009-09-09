package com.cve.web;

import com.cve.util.Check;
import com.cve.util.URIs;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.bouncycastle.util.encoders.Base64;
import static com.cve.log.Log.args;

/**
 * Wraps another request handler to provide a compressed URI scheme.
 * This is useful, because we tend to create big URLs with lots of info
 * in them.
 * @author curt
 */
public final class CompressedURIHandler implements RequestHandler {

    final RequestHandler handler;

    static final String PREFIX = "/z/";

    static final Charset charset = Charset.forName("UTF-8");

    private CompressedURIHandler(RequestHandler handler) {
        this.handler = Check.notNull(handler);
    }
    
    static RequestHandler of(RequestHandler handler) {
        return new CompressedURIHandler(handler);
    }

    @Override
    public PageResponse produce(PageRequest request) throws IOException, SQLException {
        args(request);
        String uri = request.requestURI;
        if (uri.startsWith(PREFIX)) {
            String longUri = longURI(URIs.of(uri)).toString();
            return handler.produce(request.withURI(longUri));
        } else {
            return handler.produce(request);
        }
    }

    /**
     * See http://en.wikipedia.org/wiki/Base64
     */
    static byte[] encode(byte[] bytes) {
        return Base64.encode(bytes);
    }

    static byte[] decode(byte[] bytes) {
        return Base64.decode(bytes);
    }

    /**
     * Compress the bytes
     */
    static byte[] deflate(byte[] input) {
         byte[] output = new byte[10000];
         Deflater compresser = new Deflater();
         compresser.setInput(input);
         compresser.finish();
         int length = compresser.deflate(output);
         return copy(output,length);
    }

    /**
     * Decompress the bytes
     */
    static byte[] inflate(byte[] input) {
        try {
            Inflater decompresser = new Inflater();
            decompresser.setInput(input, 0, input.length);
            byte[] output = new byte[10000];
            int length = decompresser.inflate(output);
            decompresser.end();
            return copy(output,length);
        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return string >> bytes >> base64 >> >> inflate >> string
     */
    static String longOf(String s) {
        return string(inflate(decode(unstring(s))));
    }

    /**
     */
    static String shortOf(String s) {
        return string(encode(deflate(unstring(s))));
    }

    static byte[] unstring(String s) {
        return s.getBytes(charset);
    }

    static String string(byte[] bytes) {
        return new String(bytes,charset);
    }

    static byte[] copy(byte[] input, int length) {
        return Arrays.copyOf(input, length);
    }

    /**
     * Given a short URI starting with /z/ return the equivalent long one. 
     */
    public static URI longURI(URI uri) {
        Check.notNull(uri);
        String string = uri.toString();
        if ((!string.startsWith(PREFIX)))  {
            throw new IllegalArgumentException("" + uri);
        }
        String afterPrefix = string.substring(PREFIX.length());
        if (string.endsWith("/")) {
            String betweenSlashes = afterPrefix.substring(0,afterPrefix.length() - 1);
            return URIs.of(longOf(betweenSlashes) + "/");
        }
        return URIs.of(PREFIX + longOf(afterPrefix));
    }

    /**
     * Given a long URI, return the equivalent short one starting with /z/.
     */
    public static URI shortURI(URI uri) {
        Check.notNull(uri);
        String string = uri.toString();
        if ((!string.startsWith("/")) || string.startsWith("//"))  {
            throw new IllegalArgumentException("" + uri);
        }
        String afterPrefix = string.substring("/".length());
        if (string.endsWith("/")) {
            String betweenSlashes = afterPrefix.substring(0,afterPrefix.length() - 1);
            return URIs.of(shortOf(betweenSlashes) + "/");
        }
        return URIs.of(PREFIX + shortOf(afterPrefix));
    }
}
