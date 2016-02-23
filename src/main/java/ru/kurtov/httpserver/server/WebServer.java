package ru.kurtov.httpserver.server;

import java.io.*;
import java.net.*;
import java.util.*;
import ru.kurtov.httpserver.statics.CacheStaticManager;
import ru.kurtov.httpserver.statics.NonCacheStaticManager;
import ru.kurtov.httpserver.statics.StaticManager;

public class WebServer {
    protected static Properties props = new Properties();
    static Vector threads = new Vector();
    static File root;
    static int port = 9090;
    static int workers = 10;
    static boolean cache = true;
    static StaticManager staticManager;
  
    public static ServerSocket ss;

    static void loadProps() throws IOException {
	File f = new File(System.getProperty("user.dir") +
            File.separator +
            "HttpServerProperties.properties");

        if(f.exists()) {
            InputStream is=new BufferedInputStream(new FileInputStream(f));
            props.load(is);
            is.close();
		
            String r = props.getProperty("root");
            if(r != null) {
                root = new File(r);
                if(!root.exists()) {
                    throw new Error(root + " doesn't exist as server root");
                }
            }
		
            r = props.getProperty("port");
            if(r != null) {
                port = Integer.parseInt(r);
            }
            
            r = props.getProperty("workers");
            if(r != null) {
                workers = Integer.parseInt(r);
            }

            r = props.getProperty("cache");
            if(r != null) {
                cache = Boolean.parseBoolean(r);
            }
                
                
            if(cache) {
                staticManager = new CacheStaticManager();
            } else {
                staticManager = new NonCacheStaticManager();
            }        
        }
    }

    private static void printProps() {
        System.out.println("root = "+root);
        System.out.println("port = "+port);
        System.out.println("workers = "+workers);
        System.out.println("cache = "+cache);
    }
  
    public static File getRoot() {
        return root;
    }
  
    public static StaticManager getStaticManager() {
        return staticManager;
    }

    public static void main(String[] a) throws Exception {
        loadProps();
        printProps();
	
        for(int i=0;i<workers;i++) {
            Worker w=new Worker(); 
            (new Thread(w, "worker #"+i)).start();
            threads.addElement(w);
        }
        
        ss = new ServerSocket(port); 
        while (true) { 
            Socket s = ss.accept(); 
            Worker w=null; 
            synchronized (threads) { 
                if(threads.isEmpty()) { 
                    Worker ws = new Worker(); 
                    ws.setSocket(s); 
                    (new Thread(ws, "additional worker")).start(); 
                } else { 
                    w = (Worker) threads.elementAt(0); 
                    threads.removeElementAt(0); 
                    w.setSocket(s); 
		} 
            } 
        } 
    }
} 