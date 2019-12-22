/**
 * 
 */
package com.photon.vms.common.exception;

/**
 * @author karthigaiselvan_r
 *
 */
public class VmsApplicationException extends Exception{
	
	public VmsApplicationException(Throwable cause) {
        super(cause);
    }

    public VmsApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public VmsApplicationException(String message) {
        super(message);
    }

    public VmsApplicationException() {
    	super();
    }
    
}
