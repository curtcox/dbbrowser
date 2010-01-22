package com.cve.web;

import com.cve.log.Log;
import com.google.common.collect.Maps;
import java.util.Map;
import static com.cve.util.Check.notNull;

/**
 * For injecting debugging into an HTML page.
 * See PageDecorator, DebugHandler, Log, ObjectLink, etc...
 * @author curt
 */
final class DebugPageInjector {

     final Log log;

     private DebugPageInjector(Log log) {
         this.log = notNull(log);
     }
     
    /**
      Return JavaScript that will launch appropriate pages on keypress.
      Given a map from keys, to the page to launch on the keypress.
    */
    private static String buildKeypressHandler(Map<String,String> map, String requestURI) {
        String bindings = "";
        for (String key : map.keySet()) {
            String value = map.get(key);
            String[] parts = value.split(" : ");
            String page  = parts[0];
            String title = parts[1] + requestURI;
            bindings +=
            "if (keyChar == '" + key + "') { window.open('" + page +"','" +title +"'); }\r" ;
        }
/**
The template that follows is the Javascript key handler that will appear in
the page.  The only variable content is the sequence of bindings that we
just built.
*/
String script =
"<script language='JavaScript1.2'>" +
    "<!--" +
    "document.onkeypress = myKeyPressHandler;" +

    "function myKeyPressHandler(e) {" +
        "var keyChar = String.fromCharCode(e.which);"+
         bindings +
    "}" +
    "-->" +
"</script>";
        return script;
    } // buildKeypressHandler

   /**
    * Build a map from keypresses to the pages we want to load.
    */
    private static Map<String,String> buildKeypressMap(String query) {
        Map<String,String> map = Maps.newHashMap();
        map.put("D", "/debug/dump?"   + query + " : Application Dump");
        map.put("R", "/debug/report?" + query + " : Bug Reporter");
        map.put("L", "/debug/logs/?"  + query + " : Log Viewer");
        
        return map;
    }

   /**
    * Return an array describing this request.
    */
    private static Map pageDescription() {
        Map map = Maps.newHashMap();
        map.put("time", System.currentTimeMillis());
        map.put("pid", "UNIQUE_REQUEST_ID");
        map.put("server" , "SERVER_NAME");
        map.put("request", "REQUEST_URI");
        return map;
    }

    /**
    * Return a query string that gives some meta-data for the page being built.
    * This allows it to be looked up later in a variety of contexts.
    */
    private static String buildQueryStringForThisPage(Map data) {
        String query = null; // build_query(data);
        return query;
    }

    /**
      Given a query string that describes the page being displayed, embed
      an image that will alert a client-side debugger about the page.
    */
    private static String logThisPageToLocalHost(String query) {
        String clientDebugger = "http://localhost:6502?";
        String url            =  clientDebugger + query;
        return "<img src='" + url + "' width=\"0\" height=\"0\"/>";
    }

    /**
      Log this page request.
    */
    public void logThisPage(Map pageDescription) {
        log.notNullArgs(pageDescription);
    }

    static boolean isDebugEnabled() {
        return true;
    }

    /**
     Return a header that will allow for application debugging when enabled.
    */
    public static String getHeader(String requestURI) {
        if (!isDebugEnabled()) {
            return "";
        }

        Map    descr   = pageDescription();
        String query   = buildQueryStringForThisPage(descr);
        //String header  = logThisPageToLocalHost(query);
        String header  = buildKeypressHandler(buildKeypressMap(query),requestURI);

        return header;
    }

}


