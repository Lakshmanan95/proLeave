package com.photon.vms.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {

	private static final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
	protected static String encryptPassword(String pw) {
        if ( !encryptor.isInitialized()) {
            encryptor.setPoolSize(4);
            encryptor.setPassword("Photon@123");
            encryptor.setAlgorithm("PBEWITHMD5ANDDES");
        }
        return encryptor.encrypt(pw);
    }
	
    protected static String decryptPassword(String val) {
        if ( !encryptor.isInitialized()) {
            encryptor.setPoolSize(4);
            encryptor.setPassword("Photon@123");
            encryptor.setAlgorithm("PBEWITHMD5ANDDES");
        }
        return encryptor.decrypt(val);
    }
}
