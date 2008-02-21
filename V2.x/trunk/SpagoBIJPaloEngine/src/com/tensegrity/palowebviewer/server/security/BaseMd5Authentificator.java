package com.tensegrity.palowebviewer.server.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.tensegrity.palowebviewer.server.exeptions.PaloWebViewerException;

/**
 * 
 * BaseMd5Authentificator for authenificate user from cookies
 *
 */
public abstract class BaseMd5Authentificator implements IAuthenificator {
	
	private static String ALGORITHM = "MD5";

	private MessageDigest digest;
	
	/**
	 * Create BaseMd5Authentificator, initialize md5 algorithm 
	 * @throws PaloWebViewerException if can't create md5 algorithm
	 */
	public BaseMd5Authentificator() throws PaloWebViewerException{
    	try {
    		digest = MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new PaloWebViewerException("Can't create md5 algorithm",e);
		}
	}
	
	/**
	 * Calculate hash for a given key
	 * @param key password
	 */
	public String calculateHash(String key) {
		synchronized (digest) {
			digest.reset();
	    	digest.update(key.getBytes());
	    	BigInteger number = new BigInteger(digest.digest());
	    	return number.toString(16);
		}
	}

	
}
