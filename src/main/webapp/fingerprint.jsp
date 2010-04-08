<%@ page import="java.io.*,
                 java.util.jar.*,
                 java.util.*"
    session="false" %>
<html>
<head>
<title>System Fingerprint</title>
</head>
<body>
<%!

    /*
     * Fingerprint the users system. This is mainly for use in
     * diagnosing classpath problems. It is intended to dump out
     * a copy of the environment this webapp is running in,
     * and additionally attempt to identify versions of each jar
     * in the classpath.
     */

    final java.util.Properties versionProps = new Properties();

    /**
     * Identify the version of a jar file. This uses a properties file
     * containing known names and sizes in the format
     * 'name(size)=version'.
     */
    public String getFileVersion(File file) throws IOException {
        Date timestamp=new Date(file.lastModified());
        return "<td>" + file.getName() + "</td>" +
               "<td>" + file.length()  + "</td>" +
               "<td>" + timestamp      + "</td>";
    }

    /**
     * Split up a classpath-like variable. Returns a list of files.
     */
    File[] splitClasspath(String path) throws IOException {
        StringTokenizer st = new StringTokenizer(path,System.getProperty("path.separator"));
        int toks=st.countTokens();
        File[] files=new File[toks];
        for(int i=0;i<toks;i++) {
            files[i]=new File(st.nextToken());
        }
        return files;
    }

    /** given a list of files, return a list of jars which actually exist */
    File[] scanFiles(File[] files) throws IOException {
        File[] jars=new File[files.length];
        int found=0;
        for (int i=0; i<files.length; i++) {
            if (files[i].getName().toLowerCase().endsWith(".jar")
                    && files[i].exists()) {
                jars[found]=files[i];
                found++;
            }
        }
        if (found<files.length) {
            File[] temp=new File[found];
            System.arraycopy(jars,0,temp,0,found);
            jars=temp;
        }
        return jars;    
    }

    private static final File[] NO_FILES=new File[0];

    /** scan a directory for jars */    
    public File[] scanDir(String dir) throws IOException {
        if(dir==null) {
            return NO_FILES;
        }
        return scanDir(new File(dir));
   }
        
    public File[] scanDir(File dir) throws IOException {
        if (!dir.exists() || !dir.isDirectory()) {
            return NO_FILES;
        }
        return scanFiles(dir.listFiles());
    }

    /** scan a classpath for jars */    
    public File[] scanClasspath(String path) throws IOException {
        if (path==null) {
            return NO_FILES;
        }
        return scanFiles(splitClasspath(path));
    }

    /** 
     * scan a 'dirpath' (like the java.ext.dirs system property) for jars 
     */   
    public File[] scanDirpath(String path) throws IOException {
        if (path==null) {
            return NO_FILES;
        }
        File[] current=new File[0];
        File[] dirs=splitClasspath(path);
        for(int i=0; i<dirs.length; i++) {
            File[] jars=scanDir(dirs[i]);
            File[] temp=new File[current.length+jars.length];
            System.arraycopy(current,0,temp,0,current.length);
            System.arraycopy(jars,0,temp,current.length,jars.length);
            current=temp;
        }
        return scanFiles(current);
    }

    /** print out the jar versions for a directory */
    public void listDirectory(String title, JspWriter out,String dir, String comment) throws IOException {
        listVersions(title, out,scanDir(dir), comment);
    }

    /** print out the jar versions for a directory-like system property */
    public void listDirProperty(String title, JspWriter out,String key, String comment) throws IOException {
        String path = System.getProperty(key);
        if (comment==null) {
            comment = path;
        }
        listVersions(title, out,scanDir(path), comment);
    }

    /** print out the jar versions for a classpath-like system property */
    public void listClasspathProperty(String title, JspWriter out,String key, String comment) throws IOException {
        String path = System.getProperty(key);
        if (comment==null) {
            comment = path;
        }
        listVersions(title, out,scanClasspath(path), comment);
    }

    /** print out the jar versions for a 'java.ext.dirs'-like system property */
    public void listDirpathProperty(String title, JspWriter out,String key, String comment) throws IOException {
        String path = System.getProperty(key);
        if (comment==null) {
            comment = path;
        }
        listVersions(title, out,scanDirpath(path), comment);
    }

    /** print out the jar versions for a context-relative directory */
    public void listContextPath(String title, JspWriter out, String path, String comment)  throws IOException {
        String realPath = getServletConfig().getServletContext().getRealPath(path);
        if (comment==null) {
            comment = realPath;
        }
        listVersions(title, out,scanDir(realPath), comment);
    }

    /** print out the jar versions for a given list of files */
    public void listVersions(String title, JspWriter out,File[] jars, String comment) throws IOException {
        out.print("<h2>");
        out.print(title);
        out.println("</h2>");
        if(comment!=null && comment.length()>0) {
            out.println("<p>");
            out.println(comment);
            out.println("<p>");
        }
        out.println("<table>");
        for (int i=0; i<jars.length; i++) {
            out.println("<tr>"+getFileVersion(jars[i])+"</tr>");
        }
        out.println("</table>");
    }

    private String showManifest()
        throws IOException , ClassNotFoundException
    {
        String metaInf = getServletConfig().getServletContext().getRealPath("/META-INF");
        File file = new File(metaInf + File.separator + "MANIFEST.MF");
        InputStream stream = new FileInputStream(file);
        //clazz.getResourceAsStream("META-INF/MANIFEST.MF");
        if (stream == null) {
            return "<h2>No manifest found for " + file + "</h2>";
        }
        StringBuffer out = new StringBuffer();
        out.append("<h2>Manifest from " + file + "</h2>");
        final Manifest manifest = new Manifest(stream);
        stream.close();
        Map map = manifest.getEntries();
        out.append(mapAsTable("Entries"         ,manifest.getEntries()));
        out.append(mapAsTable("Main Attributes" ,manifest.getMainAttributes()));
        return out.toString();
    }

    public static String mapAsTable(String title,Map map) {
        StringBuffer out = new StringBuffer();
        out.append("<h2>" + title + "</h2>\r");
        out.append("<table>\r");
        for (Object key : map.keySet()) {
             Object value = map.get(key);
             out.append("<tr>\r");
             out.append("    <td>" + key + " </td><td>" + value + "</td>\r");
             out.append("</tr>\r");
        }
        out.append("</table>\r");
        return out.toString();
    }
%>
<h1>System Fingerprint</h1>
<%= showManifest() %>
<h2>JVM and Server Version</h2>
<table>
<tr>
    <td>Servlet Engine</td>
    <td><%= getServletConfig().getServletContext().getServerInfo() %></td>
    <td><%= getServletConfig().getServletContext().getMajorVersion() %></td>
    <td><%= getServletConfig().getServletContext().getMinorVersion() %></td>
</tr>
<tr>
    <td>Java VM</td>
    <td><%= System.getProperty("java.vm.vendor") %></td>
    <td><%= System.getProperty("java.vm.name") %></td>
    <td><%= System.getProperty("java.vm.version") %></td>
</tr>
<tr>
    <td>Java RE</td>
    <td><%= System.getProperty("java.vendor") %></td>
    <td><%= System.getProperty("java.version") %></td>
    <td> </td>
</tr>
<tr>
    <td>Platform</td>
    <td><%= System.getProperty("os.name") %></td>
    <td><%= System.getProperty("os.arch") %></td>
    <td><%= System.getProperty("os.version") %></td>
</tr>
</table>
<%= mapAsTable("System Properties",System.getProperties()) %>
<%
listClasspathProperty("Boot jars", out,"sun.boot.class.path", "Only valid on a sun jvm");
listClasspathProperty("System jars", out,"java.class.path", null);
listDirpathProperty("Extra system jars", out,"java.ext.dirs", null);
listContextPath("Webapp jars", out, "/WEB-INF/lib", null);
%>
</body>
</html>