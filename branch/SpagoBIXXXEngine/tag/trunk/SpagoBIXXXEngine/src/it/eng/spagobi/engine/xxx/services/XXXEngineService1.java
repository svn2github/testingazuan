/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.engine.xxx.services;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import it.eng.spagobi.utilities.engines.AbstractEngineStartServlet;
import it.eng.spagobi.utilities.engines.EngineStartServletIOManager;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;



/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class XXXEngineService1 extends AbstractEngineStartServlet {
	
	public void doService( EngineStartServletIOManager servletIOManager ) throws SpagoBIEngineException {
		 
         String destination = "/WEB-INF/jsp/result.jsp";
         
         RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
         try {
			rd.forward(servletIOManager.getRequest(), servletIOManager.getResponse());
		} catch (ServletException e) {
			throw new SpagoBIServiceException("XXXEngineService1", e);
		} catch (IOException e) {
			throw new SpagoBIServiceException("XXXEngineService1", e);
		}

	}

}
