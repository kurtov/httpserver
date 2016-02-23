package ru.kurtov.httpserver.server;

import org.junit.Test;
import static org.junit.Assert.*;

public class RequestTest {
    
    @Test
    public void testParseAcceptCharset() {
        
        testAcceptCharset(null, "UTF-8");
        testAcceptCharset("utf-8", "UTF-8");
        testAcceptCharset("utf-8, US-ASCII", "UTF-8");
        testAcceptCharset("utf-8;q=0.5, US-ASCII;q=1.0", "UTF-8");
        testAcceptCharset("*", "UTF-8");
        testAcceptCharset("qwer", null);
        testAcceptCharset("qwer, US-ASCII", "US-ASCII");
    }
    
    private void testAcceptCharset(String header, String expResult) {
        String result = AcceptCharset.getCharset(header);
        assertEquals(expResult, result);                
    }
}
