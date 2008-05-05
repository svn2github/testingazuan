/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.qbe.querysaver.service;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;


// TODO: Auto-generated Javadoc
/**
 * The Class PersistQueryTemporaryAction.
 * 
 * @author Andrea Zoppello
 * 
 * This action is responsible to Persist the current working query represented by
 * the object ISingleDataMartWizardObject in session
 */
public class PersistQueryTemporaryAction extends AbstractQbeEngineAction {
	
	/**
	 * Checks if is absolute path.
	 * 
	 * @param path the path
	 * 
	 * @return true, if is absolute path
	 */
	private static boolean isAbsolutePath(String path) {
		if(path == null) return false;
		return (path.startsWith("/") || path.startsWith("\\") || path.charAt(1) == ':');
	}
	
	/**
	 * Gets the qbe data mart directory.
	 * 
	 * @param baseDir the base dir
	 * 
	 * @return the qbe data mart directory
	 */
	private static String getQbeDataMartDir(File baseDir) {
		String qbeDataMartDir = null;
		qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
		if( !isAbsolutePath(qbeDataMartDir) )  {
			String baseDirStr = (baseDir != null)? baseDir.toString(): System.getProperty("user.home");
			qbeDataMartDir = baseDir + System.getProperty("file.separator") + qbeDataMartDir;
		}
		return qbeDataMartDir;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		try{
			DataMartModel dmModel = (DataMartModel)getRequestContainer().getSessionContainer().getAttribute("dataMartModel");
	
			String qbeDataMartDir = getQbeDataMartDir(new File(it.eng.spago.configuration.ConfigSingleton.getInstance().getRootPath()));
		
			String publicDmDir = qbeDataMartDir +System.getProperty("file.separator") +  dmModel.getName();
		
			UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
			UUID uuidObj = uuidGenerator.generateTimeBasedUUID();
			String uuid = uuidObj.toString();
		
			String fileName = publicDmDir + System.getProperty("file.separator") + uuid+ ".qbe";
		
			ISingleDataMartWizardObject wizardObject = getDatamartWizard();
			File f = new File(fileName);
			if (!f.exists()){
					f.createNewFile();
			}
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
			        new FileOutputStream(fileName)));
			encoder.writeObject(wizardObject);
			encoder.close();
			getResponseContainer().setAttribute(Constants.HTTP_RESPONSE_FREEZED, Boolean.TRUE);
			/*
			SourceBean sBean = new SourceBean("uuid");
			sBean.setCharacters(uuid);
			response.setAttribute(sBean);
			*/
			getHttpResponse().getWriter().write(uuid);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}
}
