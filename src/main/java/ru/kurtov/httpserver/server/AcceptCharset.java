package ru.kurtov.httpserver.server;

import java.util.Arrays;

public class AcceptCharset {
    
    //по параметру Accept-Charset вернуть кодировку
    //возвращается null, если ни одна из кодировок не поддерживается
    public static String getCharset(String acceptCharset) {
        String[] charsets = parseAcceptCharset(acceptCharset);
        String[] allowCharsets = new String[] {"UTF-8", "US-ASCII"};
               
        if(charsets.length == 0) {
            return "UTF-8";
        }
        
        if(charsets.length == 1 && "*".equals(charsets[0])) {
            return "UTF-8";
        }
        
        Arrays.sort(charsets);
        
        for(int i=0, len=allowCharsets.length; i < len; i++) {
            String charset = allowCharsets[i];
            
            if(Arrays.binarySearch(charsets, charset) >=0 ){
                return charset;
            }
        }
        
        return null;
    }

    private static String[] parseAcceptCharset(String header) {
        if(header == null) {
            return new String[]{};
        }
        
        header = header.toUpperCase();
        
        String[] paths = header.split(", ");
        for(int i=0, len = paths.length; i < len; i++) {
            int idx = paths[i].indexOf(";");
            if(idx>-1) {
                paths[i] = paths[i].substring(0, idx);
            }
        }
        return paths;
    }
}
