package com.cve.web;

import com.cve.util.Check;
import com.cve.util.URIs;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.bouncycastle.util.encoders.Base64;
import com.cve.log.Log;
import static com.cve.util.Check.notNull;

/**
 * Wraps another request handler to provide a compressed URI scheme.
 * This is useful, because we tend to create big URLs with lots of info
 * in them.
 * @author curt
 */
public final class CompressedURIHandler implements RequestHandler {

    final RequestHandler handler;

    /**
     * Where we log to.
     */
    private final Log log;

    static final String PREFIX = "/z/";

    static final Charset charset = Charset.forName("UTF-8");

    private CompressedURIHandler(RequestHandler handler, Log log) {
        this.handler = notNull(handler);
        this.log = notNull(log);
    }
    
    public static RequestHandler of(RequestHandler handler, Log log) {
        return new CompressedURIHandler(handler,log);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        log.args(request);
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
     * Compress the bytes.
     * For now, this uses deflater, but that should change because deflater sucks.
     * <ol>
     * <li> Underdocumented.
     * <li> Buggy.  The methods to set level and strategy cause compression to
     * fail.  Then again, I could be using them wrong.  See underdocumented.
     * Setting the level in the constructor seems to work.
     * <li> Native.  This makes using a debugger to work around the problems
     * above much harder.
     * <li> Not good enough.  It makes small URLs bigger (OK), but it doesn't
     * shrink big URLs enough.  Sample results show the following results for
     * the entire process (deflate + base64).
     *   <ol>
     *       <li>  4:19
     *       <li>  5:20
     *       <li>  8:23
     *       <li>  9:24
     *       <li> 760:472
     *   </ol>
     * This means that deflate is doing a little better than cutting the bytes
     * in half for large URLs.  There are schemes available that could
     * probably do at least twice as well as that.
     *
     * http://cs.fit.edu/~mmahoney/compression/paq.html
     * http://mattmahoney.net/dc/
     */
    static byte[] deflate(byte[] input) {
         
         Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION,false);
         // Setting the level or strategy will cause deflation to fail for
         // undocumented reasons.  Yet, we would like to get better comprssion,
         // since currently we make short URLs longer and only cut the size of
         // long URLs by less than half.
         deflater.setInput(input);
         deflater.finish();
         byte[] output = new byte[10000];
         int length = deflater.deflate(output);
         //length += deflater.deflate(new byte[0]);
         if (!deflater.finished()) {
             throw new RuntimeException();
         }
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
