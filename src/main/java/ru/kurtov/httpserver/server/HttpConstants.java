package ru.kurtov.httpserver.server;

public interface HttpConstants {
    /** 2XX: generally "OK" */
    public static final int HTTP_OK = 200;

    /** 3XX: relocation/redirect */  
    public static final int HTTP_NOT_MODIFIED = 304;
    
    /** 4ХХ: client error */
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_BAD_METHOD = 405;
    public static final int HTTP_NOT_ACCEPTABLE = 406;
    
    public static final byte[] EOL={(byte)'\r', (byte)'\n' }; 
}