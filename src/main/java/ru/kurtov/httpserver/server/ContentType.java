package ru.kurtov.httpserver.server;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ContentType {
    
    private static final Map<String, String> MAP = new HashMap(); 
    private static final String UNKNOWN = "unknown/unknown";
    
    static {
	MAP.put("", UNKNOWN);
	MAP.put(".jpg", "image/jpeg");
	MAP.put(".jpeg", "image/jpeg");
	MAP.put(".html", "text/html");
	MAP.put(".txt", "text/plain");
        MAP.put(".js", "application/javascript");
    }

    public static String getContentType(String fileName) {
        int ind = fileName.lastIndexOf('.'); 
        String contentType = null; 
                
        if (ind > 0) {
            contentType = MAP.get(fileName.substring(ind));
        }
        
        if (contentType == null) {
            contentType = UNKNOWN;
        }
        
        return contentType;
    }
    
    public static String getContentType(File file) {
        return getContentType(file.getName());
    }
    
    public static boolean isText(File file) {
        String contentType = getContentType(file);
        
        return contentType.equals("text/html") || contentType.equals("application/javascript");
    }
}
