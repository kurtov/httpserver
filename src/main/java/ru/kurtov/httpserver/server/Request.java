package ru.kurtov.httpserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private final InputStream stream;
    
    private final Map<String, String> headers = new HashMap();
    private String verb;
    private String path;
    
    public Request(Socket s) throws IOException {
        stream = s.getInputStream();
        this.readRequest();
    }
    
    private void readRequest() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String ln;

        processFirstLine(reader.readLine());
        while (true) {
            ln = reader.readLine();
            if (ln == null || ln.isEmpty()) {
                break;
            }
            processHeader(ln);
        }
    }
    
    private void processFirstLine(String firstLine) {
        detectVerb(firstLine);
        detectPath(firstLine);
    }
    
    private void detectVerb(String firstLine) {
        int idx = firstLine.indexOf(" ");
        this.verb = firstLine.substring(0, idx);
    }
    
    private void detectPath(String firstLine) {
        int from = firstLine.indexOf(" ") + 1; 
        int to = firstLine.indexOf(" ", from); 
        String pathWithParams = firstLine.substring(from, to); 
        
        int paramIndex = pathWithParams.indexOf("?"); 
        if (paramIndex != -1) {
            pathWithParams = pathWithParams.substring(0, paramIndex);
        }
        this.path = pathWithParams;
    }
    
    private void processHeader(String header) {
        int idx = header.indexOf(":");

        headers.put(
            header.substring(0, idx),
            header.substring(idx+2, header.length())
        );
    }
    
    public boolean isGet() {
        return "GET".equals(this.verb);
    }
    
    public String getPath() {
        return this.path;
    }
    
    public String getHeader(String header) {
        return headers.get(header);
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Verb = ").append(this.verb).append("\n").
                append("Path = ").append(this.path).append("\n").
                append("Headers:").append("\n");
        
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            s.append(entry.getKey()).
                    append(" = ").
                    append(entry.getValue()).
                    append("\n");
        }

        return s.toString();
    }
}