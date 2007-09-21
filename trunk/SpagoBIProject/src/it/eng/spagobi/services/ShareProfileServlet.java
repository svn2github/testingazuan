/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
/*
 * Created on 4-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.services;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.ProfileExchanger;
import it.eng.spagobi.utilities.SpagoBITracer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



/**
 * A servlet used to manage export operation
 */
public class ShareProfileServlet extends HttpServlet{
	
	/**
	 * Init method definition
	 */
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
     } 
	
	public void service(HttpServletRequest request, HttpServletResponse response) {
		IEngUserProfile profile = null;
		try{
			// get the profile form the exchanger
			String username = request.getParameter("username");
			ProfileExchanger profExchanger = ProfileExchanger.getInstance();
			profile = profExchanger.getProfile(username);
			// get session
			HttpSession httpSession = request.getSession();
			// if the profile is not null load it into the session
			if(profile!=null)  {
				httpSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
			} else {
				// else if profile is null check if it is contained into session
				// if the session doesn't contain profile then the user must be informed that
				// some functionalities won't work
				if(httpSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE)==null){
					throw new Exception("Cannot find profile nor in ProfileExchanger neither in session");
				}
			}
			response.getOutputStream().write("done".getBytes());
			response.getOutputStream().flush();
		} catch(Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "service", "Profile not loaded into http session " + e);
			try {
				response.getOutputStream().write("fault".getBytes());
				response.getOutputStream().flush();
			} catch (Exception ex) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
			            			"service", "Unable to send fault message " + e);
			}
		}
	}
		
	
	
	

}	

