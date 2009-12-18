/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.datasource.service;


import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * 
 * @author Chiarelli (chiara.chiarelli@eng.it)
 *
 */
public class TestConnectionAction extends AbstractHttpAction {

	static private Logger logger = Logger.getLogger(TestConnectionAction.class);
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.services.BaseProfileAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
			throws Exception {
		
		logger.debug("IN");
		String message = null;
		freezeHttpResponse();
		HttpServletResponse httResponse = getHttpResponse();
		
		String url = (String) serviceRequest.getAttribute("urlc");
		String jndi = (String) serviceRequest.getAttribute("jndi");
		String isjndi = (String) serviceRequest.getAttribute("isjndi");
		String user = (String) serviceRequest.getAttribute("user");
		String pwd = (String) serviceRequest.getAttribute("pwd");
		String driver = (String) serviceRequest.getAttribute("driver");
		
		Connection connection = null;
		Context ctx;
		try {
			if (isjndi.equals("true")){
				    ctx = new InitialContext();
				    DataSource ds = (DataSource) ctx.lookup(jndi);
				    connection = ds.getConnection();
			}else if (isjndi.equals("false")){			
				    Class.forName(driver);
				    connection = DriverManager.getConnection(url, user, pwd);
			}
		 if (connection != null){
			    	message = "sbi.connTestOk";
			   }
		} catch (NamingException ne) {
		    logger.error("JNDI error", ne);
		    message = "sbi.connTestError";
		} catch (SQLException sqle) {
		    logger.error("Cannot retrive connection", sqle);
		    message = "sbi.connTestError";
		} catch (ClassNotFoundException e) {
			    logger.error("Driver not found", e);
			    message = "sbi.connTestError";	
		}finally {
			httResponse.getOutputStream().write(message.getBytes());
			httResponse.getOutputStream().flush();
			if ((connection != null) && (!connection.isClosed())) connection.close();
			logger.debug("OUT");
		}
	}	
}
