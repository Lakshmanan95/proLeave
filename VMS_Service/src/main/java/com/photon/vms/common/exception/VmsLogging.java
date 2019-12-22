/**
 * 
 */
package com.photon.vms.common.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author karthigaiselvan_r
 *
 */
public class VmsLogging {
	
	private static Logger log; 
	
	private VmsLogging() {
		
	}
	
	public static void logInfo(Class clazz, String message) {
		log = LogManager.getLogger(clazz);
		log.info(message);
	}
	
	public static void logDebug(Class clazz, String message) {
		log = LogManager.getLogger(clazz);
		log.debug(message);
	}
	
	public static void logDebug(Class clazz, String message,Throwable t) {
		log = LogManager.getLogger(clazz);
		log.debug(message,t);
	}
	
	public static void logError(Class clazz, String message, Throwable t) {
		log = LogManager.getLogger(clazz);
		if(t != null){
			log.error(message, t);
		}else{
			log.error(message);
		}
	}
	
}
