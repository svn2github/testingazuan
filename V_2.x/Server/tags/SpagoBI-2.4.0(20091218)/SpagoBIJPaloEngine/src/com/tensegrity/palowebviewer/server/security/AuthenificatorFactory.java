package com.tensegrity.palowebviewer.server.security;

import java.lang.reflect.Constructor;

import com.tensegrity.palowebviewer.server.PaloConfiguration;
import com.tensegrity.palowebviewer.server.exeptions.PaloWebViewerException;

/**
 * 
 * Facotory for create authenificators
 *
 */
public class AuthenificatorFactory {
	
	/**
	 * Create authenificator by class name
	 * 
	 * @param authenificatorClassName authenificator class name
	 * @return IAuthenificator implementetion
	 * @throws PaloWebViewerException if authenificator class was not founded
	 */
	public static IAuthenificator getAuthenificator(String authenificatorClassName, PaloConfiguration cfg) throws PaloWebViewerException {
		try {
			Class authClass  = Class.forName(authenificatorClassName);
			Constructor constructor = authClass.getConstructor(new Class[]{PaloConfiguration.class});
			IAuthenificator authenificator = (IAuthenificator)constructor.newInstance(new Object[]{cfg});
			return authenificator;
		} catch (Exception e) {
			throw new PaloWebViewerException("Can't create authenificator", e);
		}
	}
	
}
