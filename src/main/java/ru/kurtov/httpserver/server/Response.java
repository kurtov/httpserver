//TODO: отличие print от write
package ru.kurtov.httpserver.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Response implements HttpConstants {
    
    private static final Map<Integer, String> MAP = new HashMap(); 
    private final PrintStream stream;
    
    static {
        MAP.put(HTTP_OK, "OK");
        MAP.put(HTTP_NOT_MODIFIED, "Not Modified");
	MAP.put(HTTP_NOT_FOUND, "Not Found");
	MAP.put(HTTP_BAD_METHOD, "Method Not Allowed");
	MAP.put(HTTP_BAD_REQUEST, "Bad Request");
        MAP.put(HTTP_NOT_ACCEPTABLE, "Not Acceptable");
    }
    
    public Response(PrintStream stream) {
        this.stream = stream;
    }
    
    public Response(Socket s) throws IOException {
        this.stream = new PrintStream(s.getOutputStream());;
    }
    
    public void setStatus(int status) throws IOException {
        this.stream.print("HTTP/1.1 " + status + " " + getStatusName(status)); 
        this.stream.write(EOL); 
    }
    
    public void setHeader(String name, String value) throws IOException {
        this.stream.print(name + ": " + value);
        this.stream.write(EOL);
    }
    
    public void setHeader(String name, long value) throws IOException {
        this.stream.print(name + ": " + value);
        this.stream.write(EOL);
    }
    
    private String getStatusName(int status) {
        return MAP.get(status);
    }
    
    public PrintStream getStream() {
        return this.stream;
    }
}