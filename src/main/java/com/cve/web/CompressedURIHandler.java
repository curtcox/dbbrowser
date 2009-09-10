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
     * Normal base 64 encoding uses + and /.
     * We use - and _ instead AKA base64url.
     * If we need source to include or modify, look here:
     * http://www.source-code.biz/snippets/java/Base64Coder.java.txt
     */
    static byte[] encode(byte[] bytes) {
        return url(Base64.encode(bytes));
    }

    static byte[] decode(byte[] bytes) {
        return Base64.decode(deurl(bytes));
    }

    static byte[] url(byte[] in) {
        byte[] out = new byte[in.length];
        for (int i=0; i<in.length; i++) {
            out[i] = in[i];
            if (out[i]=='/') {
                out[i] = '_';
            }
            if (out[i]=='+') {
                out[i] = '-';
            }
        }
        return out;
    }

    static byte[] deurl(byte[] in) {
        byte[] out = new byte[in.length];
        for (int i=0; i<in.length; i++) {
            out[i] = in[i];
            if (out[i]=='_') {
                out[i] = '/';
            }
            if (out[i]=='-') {
                out[i] = '+';
            }
        }
        return out;
    }

    /**
     * Compress the bytes
     */
    static byte[] deflate(byte[] input) {
         Deflater deflater = new Deflater();
         // Setting the level or strategy will cause deflation to fail for
         // undocumented reasons.  Yet, we would like to get better comprssion,
         // since currently we make short URLs longer and only cut the size of
         // long URLs by less than half.
         //deflater.setLevel(Deflater.BEST_COMPRESSION);
         //deflater.setStrategy(Deflater.HUFFMAN_ONLY);
         deflater.setInput(input);
         deflater.finish();
         byte[] output = new byte[10000];
         int length = deflater.deflate(output);
         return copy(output,length);
    }

    /**
     * Decompress the bytes
     */
    static byte[] inflate(byte[] input) {
        try {
            Inflater inflater = new Inflater();
            inflater.setInput(input, 0, input.length);
            byte[] output = new byte[10000];
            int length = inflater.inflate(output);
            inflater.end();
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
     * Given a short URI starting with /z/, return the equivalent long one.
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
            return URIs.of("/" + longOf(betweenSlashes) + "/");
        }
        return URIs.of("/" + longOf(afterPrefix));
    }

    /**
     * Given a long URI, return the equivalent short one starting with /z/.
     */
    public static URI shortURI(URI uri) {
        Check.notNull(uri);
        String string = uri.toString();
        if (string.startsWith(PREFIX) || (!string.startsWith("/")) || string.startsWith("//"))  {
            throw new IllegalArgumentException("" + uri);
        }
        String afterPrefix = string.substring("/".length());
        if (string.endsWith("/")) {
            String betweenSlashes = afterPrefix.substring(0,afterPrefix.length() - 1);
            return URIs.of(PREFIX + shortOf(betweenSlashes) + "/");
        }
        return URIs.of(PREFIX + shortOf(afterPrefix));
    }
}
