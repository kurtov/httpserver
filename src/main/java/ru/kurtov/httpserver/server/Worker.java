package ru.kurtov.httpserver.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import ru.kurtov.httpserver.Page;

public class Worker extends WebServer implements HttpConstants, Runnable { 

    /* Socket to client we're handling */ 
    private Socket s; 

    Worker() { 
        s=null; 
    } 

    synchronized void setSocket(Socket s) { 
        this.s=s; 
        notify(); 
    } 

	
    public synchronized void run() { 
        while(true) { 
            if (s== null) { 
                /* nothing to do */ 
                try{ 
                    wait(); 
                } catch (InterruptedException e) { 
                    /* should not happen */ 
                    continue; 
                } 
            } try { 
                handleClient(); 
            } catch (Exception e) { 
                e.printStackTrace(); 
            } 
            /* go back in wait queue if there's fewer 
            * than numHandler connections. */ 
            s=null; 
            Vector pool = WebServer.threads; 
            synchronized (pool) { 
                if (pool.size()>= WebServer.workers) {
                    /* too many threads, exit this one */
                    return;
                } else {
                    pool.addElement(this);
                }
	    }
        }
   }


    void handleClient() throws IOException {
        try {
            Request request = new Request(s);
            Response response = new Response(s);
            new Page().service(request, response);
        } 
        finally {
            s.close();
	}
    }
}