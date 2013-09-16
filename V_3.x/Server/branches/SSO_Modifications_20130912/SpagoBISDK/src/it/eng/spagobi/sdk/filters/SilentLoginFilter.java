/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.sdk.filters;

import it.eng.spago.security.DefaultCipher;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.sdk.proxy.TestConnectionServiceProxy;
import it.eng.spagobi.services.common.SsoServiceInterface;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.log4j.Logger;

/**
 * @author Zerbetto (davide.zerbetto@eng.it)
 * 
 *         This filter tries to authenticate user, using credentials provided within the HTTP request
 */

public class SilentLoginFilter implements Filter {

	private static transient Logger logger = Logger.getLogger(SilentLoginFilter.class);

	public void destroy() {
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//logger.debug("IN");
		try {
			if (request instanceof HttpServletRequest) {
				HttpServletRequest httpRequest = (HttpServletRequest) request;
				UsernamePasswordCredentials credentials = this.findUserCredentials(httpRequest);
				if (credentials != null) {
					try {
						logger.debug("User credentials found.");
						if (!httpRequest.getMethod().equalsIgnoreCase("POST")) {
							logger.error("Request method is not POST!!!");
							throw new InvalidMethodException();
						}
						logger.debug("Authenticating user ...");
						try {
							this.doAuthenticate(credentials);
							logger.debug("User authenticated");
							HttpSession session = httpRequest.getSession();
							session.setAttribute("spagobi_user", credentials.getUserName());
							session.setAttribute("spagobi_pwd", credentials.getPassword());
						} catch (Throwable t) {
							logger.error("Authentication failed", t);
							throw new SilentAuthenticationFailedException();
						}
					} catch (Exception e) {
						logger.error("Error authenticating user", e);
						httpRequest.getRequestDispatcher("silentLoginFailed.jsp").forward(request, response);
					}
				} else {
					logger.debug("User credentials not found.");
				}
				chain.doFilter(request, response);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	private void doAuthenticate(UsernamePasswordCredentials credentials) throws Throwable {
		logger.debug("IN: userId = " + credentials.getUserName());
		try {
			TestConnectionServiceProxy proxy = new TestConnectionServiceProxy(credentials.getUserName(), credentials.getPassword());
		    proxy.setEndpoint("http://localhost:8080/SpagoBI/sdk/TestConnectionService");
			boolean result = proxy.connect();
			if (!result) {
				logger.error("Authentication failed for user " + credentials.getUserName());
				throw new SecurityException("Authentication failed");
			}
		} catch (Throwable t) {
			logger.error("Error while authenticating userId = " + credentials.getUserName(), t);
			throw t;
		} finally {
			logger.debug("OUT");
		}
	}

	private UsernamePasswordCredentials findUserCredentials(HttpServletRequest httpRequest) {
		UsernamePasswordCredentials toReturn = null;
		String userId = httpRequest.getParameter(SsoServiceInterface.USER_NAME_REQUEST_PARAMETER);
		String password = httpRequest.getParameter(SsoServiceInterface.PASSWORD_REQUEST_PARAMETER);
		if (!StringUtilities.isEmpty(userId) && !StringUtilities.isNull(password)) {
			logger.debug("Read credentials from request: user id is [" + userId + "]");
			String passwordMode = httpRequest.getParameter(SsoServiceInterface.PASSWORD_MODE_REQUEST_PARAMETER);
			if (!StringUtilities.isEmpty(passwordMode) && passwordMode.equalsIgnoreCase(SsoServiceInterface.PASSWORD_MODE_ENCRYPTED)) {
				logger.debug("Password mode is encrypted. Decripting password...");
				DefaultCipher chiper = new DefaultCipher();
				password = chiper.decrypt(password);
				logger.debug("Password decrypted.");
			}
			toReturn = new UsernamePasswordCredentials(userId, password);
		}
		return toReturn;
	}

	public void init(FilterConfig config) throws ServletException {
		// do nothing
	}
	
	public class SilentAuthenticationFailedException extends RuntimeException {
		
	}
	
	public class InvalidMethodException extends RuntimeException {
		
	}

}
