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

import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet used to manage export operation
 */
public class BIObjectNotesServlet extends HttpServlet{
	
	private static int LOCK_DURATION_MS = 60000;
	private Map execIdMap = new HashMap(); 
	
	/**
	 * Init method definition
	 */
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
     } 
	
    /**
     * Service method definition which, based on a particular parameter, 
     * redirects the execution to a specific handler
     * 
     * @param request The http servlet request
     * @param response The http servlet response
     * @throws IOException If any exception occurred
     */
	public void service(HttpServletRequest request, HttpServletResponse response) {
		try{
			String task = (String)request.getParameter("task");
			if((task!=null) && (task.equalsIgnoreCase("requireLock"))){
				requireLockHandler(request, response);
				return;
			} else if((task!=null) && (task.equalsIgnoreCase("saveNotes"))) {
				saveNotesHandler(request, response);
				return;
			} else if((task!=null) && (task.equalsIgnoreCase("getNotes"))) {
				getNotesHandler(request, response);
				return;
			} else if((task!=null) && (task.equalsIgnoreCase("holdLock"))) {
				holdLockHandler(request, response);
				return;
			} 
		} finally {}
	}
		
	
	private void holdLockHandler(HttpServletRequest request, HttpServletResponse response) {
		try{	
			String userName = request.getParameter("user");
			String execIdentifier = request.getParameter("execidentifier");
			List execdata = null;
			boolean locked = false;
			synchronized(this) {
				execdata = (List)execIdMap.get(execIdentifier);
				if(execdata!=null) {
					String existingLockUser = (String)execdata.get(0);
					if(existingLockUser.equalsIgnoreCase(userName)) {
						long currentTime = new Date().getTime();
						long lockTime = ((Long)execdata.get(1)).longValue();
						if((currentTime-lockTime)<LOCK_DURATION_MS){
							execdata = new ArrayList();
							execdata.add(0, userName);
							execdata.add(1, new Long(new Date().getTime()));
							execIdMap.put(execIdentifier, execdata);
						}
					}
				}
			}
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
								   "holdLockHandler", "Error while getting lock" + e);
		} finally {	}
	}

	
	
	
	
	private void getNotesHandler(HttpServletRequest request, HttpServletResponse response) {
		String execIdent = null;
		String respStr = "";
		try{	
			String biobjIdStr = request.getParameter("biobjid");
			int biobjId = new Integer(biobjIdStr).intValue();
			execIdent = request.getParameter("execidentifier");
			IBIObjectDAO objectDAO = DAOFactory.getBIObjectDAO();
			BIObject biobject = objectDAO.loadBIObjectById(new Integer(biobjId));
			IBIObjectCMSDAO objectCMSDAO = DAOFactory.getBIObjectCMSDAO();
			String notes = objectCMSDAO.getExecutionNotes(biobject.getPath(), execIdent);
			respStr = notes;
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
								   "saveNotesHandler", "Error while saving notes" + e);
			respStr = "SpagoBIError:Error while saving notes";
		} 
		try{
			response.getOutputStream().write(respStr.getBytes());
			response.getOutputStream().flush();	
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					               "saveNotesHandler", "Error while flushing respose" + e);
		}
		
	}
	
	
	private void saveNotesHandler(HttpServletRequest request, HttpServletResponse response) {
		String execIdent = null;
		String respStr = "";
		try{	
			String biobjIdStr = request.getParameter("biobjid");
			int biobjId = new Integer(biobjIdStr).intValue();
			execIdent = request.getParameter("execidentifier");
			String notes = request.getParameter("notes");
			String userName = request.getParameter("user");
			// check if the user have the lock
			boolean hasLock = true;
			synchronized(this) {
				List execdata = (List)execIdMap.get(execIdent);
				if(execdata!=null){
					String existingLockUser = (String)execdata.get(0);
					if(!existingLockUser.equals(userName)) {
						hasLock = false;
					}
				} else {
					hasLock = false;
				}
			}
			// if the user doesn't have lock send error otherwise send empty message
			if(hasLock){
				IBIObjectDAO objectDAO = DAOFactory.getBIObjectDAO();
				BIObject biobject = objectDAO.loadBIObjectById(new Integer(biobjId));
				IBIObjectCMSDAO objectCMSDAO = DAOFactory.getBIObjectCMSDAO();
				objectCMSDAO.saveExecutionNotes(biobject.getPath(), execIdent, notes);
			} else {
				respStr = "SpagoBIError:Editor locked by another user";
			}
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
								   "saveNotesHandler", "Error while saving notes" + e);
			respStr = "SpagoBIError:Error while saving notes";
		} finally {	
			synchronized(this) {
				execIdMap.remove(execIdent);
			}
		}
		try{
			response.getOutputStream().write(respStr.getBytes());
			response.getOutputStream().flush();	
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					               "saveNotesHandler", "Error while flushing respose" + e);
		}
		
	}
	
	
	private void requireLockHandler(HttpServletRequest request, HttpServletResponse response) {
		try{	
			String biobjIdStr = request.getParameter("biobjid");
			String userName = request.getParameter("user");
			String execIdentifier = request.getParameter("execidentifier");
			
			List execdata = null;
			boolean locked = false;
			synchronized(this) {
				execdata = (List)execIdMap.get(execIdentifier);
				if(execdata!=null) {
					String existingLockUser = (String)execdata.get(0);
					if(!existingLockUser.equalsIgnoreCase(userName)) {
						long currentTime = new Date().getTime();
						long lockTime = ((Long)execdata.get(1)).longValue();
						if((currentTime-lockTime)<LOCK_DURATION_MS){
							locked = true;
						}
					}
				}
				if(!locked) {
					execdata = new ArrayList();
					execdata.add(0, userName);
					execdata.add(1, new Long(new Date().getTime()));
					execIdMap.put(execIdentifier, execdata);
				}
			}
			
			String respStr = "";
			if(locked){
				respStr = "SpagoBIError:Editor locked by another user";
			} else {
				int biobjId = new Integer(biobjIdStr).intValue();
				IBIObjectDAO objectDAO = DAOFactory.getBIObjectDAO();
				BIObject biobject = objectDAO.loadBIObjectById(new Integer(biobjId));
				IBIObjectCMSDAO objectCMSDAO = DAOFactory.getBIObjectCMSDAO();
				String notes = objectCMSDAO.getExecutionNotes(biobject.getPath(), execIdentifier);
				respStr = notes;
			}
			response.getOutputStream().write(respStr.getBytes());
			response.getOutputStream().flush();	
						
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
								   "requireLockManager", "Error while getting lock" + e);
		} finally {	}
	}

	

}	

