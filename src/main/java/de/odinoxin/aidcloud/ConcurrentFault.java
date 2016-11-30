package de.odinoxin.aidcloud;

import javax.xml.ws.WebFault;

@WebFault(name = "ConcurrentFault")
public class ConcurrentFault extends Exception {

    public ConcurrentFault(String msg) {
        super();
    }

    public ConcurrentFault(String msg, Throwable cause) {
        super(msg, cause);
    }
}
