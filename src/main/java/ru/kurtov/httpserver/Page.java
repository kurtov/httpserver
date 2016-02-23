package ru.kurtov.httpserver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import ru.kurtov.httpserver.server.AcceptCharset;
import ru.kurtov.httpserver.server.ContentType;
import ru.kurtov.httpserver.server.Request;
import ru.kurtov.httpserver.server.Response;
import ru.kurtov.httpserver.server.WebServer;
import ru.kurtov.httpserver.statics.StaticItem;

public class Page {
    public void service(Request request, Response response) throws IOException {
        File file;
        StaticItem item;
        String ifNoneMatch;
        String charset;
        boolean canEncode = true;
                
        if(!request.isGet()) {
            response.setStatus(Response.HTTP_BAD_METHOD);
            return;
        }
        
        file= new File(WebServer.getRoot(), request.getPath()); 
        if(!file.isFile()) {
            response.setStatus(Response.HTTP_NOT_FOUND);
            return;
        }
        
        charset = AcceptCharset.getCharset(request.getHeader("Accept-Charset"));
        if(charset == null) {
            response.setStatus(Response.HTTP_NOT_ACCEPTABLE);
            return;            
        }

        item = WebServer.getStaticManager().getItem(file);
        
        //Проверить, можно ли вернуть текст в запрашиваемой кодировке.
        //Поскольку
        // - реализовано "UTF-8" и "US-ASCII",
        // - все исходники хранятся в "UTF-8"
        //То если требуется вернуть "US-ASCII", а исходник в него не конвертится,
        //то вернуть ошибку
        if(ContentType.isText(file)) {
            CharsetEncoder encoder = Charset.forName(charset).newEncoder();
            if (!encoder.canEncode(new String(item.getData(), "UTF-8"))) {
                response.setStatus(Response.HTTP_BAD_REQUEST);
                return;                
            }
        }
        
        ifNoneMatch = request.getHeader("If-None-Match");
        if(ifNoneMatch == null || !ifNoneMatch.equals(String.valueOf(item.getEtag()))) {
            response.setStatus(Response.HTTP_OK);
            response.setHeader("Content-Length", file.length());
            response.setHeader("Etag", item.getEtag());
            
            if(ContentType.isText(file)) {
                response.setHeader("Content-Type", ContentType.getContentType(file)
                        + "; charset=" + charset);
            } else {
                response.setHeader("Content-Type", ContentType.getContentType(file));
            }
            
            response.getStream().write(Response.EOL);
            response.getStream().write(item.getData());
        } else {
            response.setStatus(Response.HTTP_NOT_MODIFIED);
            response.setHeader("Etag", item.getEtag());
        }
    }
}