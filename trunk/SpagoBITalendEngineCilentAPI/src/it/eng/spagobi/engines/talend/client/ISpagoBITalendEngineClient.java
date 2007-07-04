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
package it.eng.spagobi.engines.talend.client;

import it.eng.spagobi.engines.talend.client.exception.AuthenticationFailedException;
import it.eng.spagobi.engines.talend.client.exception.EngineUnavailableException;
import it.eng.spagobi.engines.talend.client.exception.ServiceInvocationFailedException;

import java.io.File;

public interface ISpagoBITalendEngineClient {
	
	/**
	 * Calls properly the EngineInfo-Service in order to get the version number of the remote 
	 * engine instance
	 * 
	 * @return The engine version number
	 
	 * @throws EngineUnavailableException when is not possible to connect to the service
	 * @throws ServiceInvocationFailedException when the service execution generate an error
	 */
	public abstract String getEngineVersion() throws EngineUnavailableException, ServiceInvocationFailedException;
	
	/**
	 * Calls properly the EngineInfo-Service in order to get the full name of the remote 
	 * engine instance
	 * 
	 * @return The name of the engine
	 * @throws EngineUnavailableException 
	 * @throws ServiceInvocationFailedException when the service execution generate an error
	 */
	public abstract String getEngineName() throws EngineUnavailableException, ServiceInvocationFailedException;

	/**
	 * Calls properly the EngineInfo-Service in order to check if the remote 
	 * engine is available 
	 *  
	 * @return true if the engine is avilable; false otherwise
	 
	 * @throws EngineUnavailableException when is not possible to connect to the service
	 */
	public abstract boolean isEngineAvailible() throws EngineUnavailableException;

	/**
	 * 
	 * @param jobDeploymentDescriptor an object containing some deployment informations 
	 * (i.e. project name, project language)
	 * @param executableJobFiles the zip file produced by TOS exportation functionality
	 * 
	 * @return true if the job is succesfully deployed into the runtime repository
	 * of the engine
	 * 
	 * @throws EngineUnavailableException when is not possible to connect to the service
	 * @throws AuthenticationFailedException when the user is not authenticated succefully
	 * @throws ServiceInvocationFailedException  when the service execution generate an error
	 */
	public abstract boolean deployJob(
			JobDeploymentDescriptor jobDeploymentDescriptor,
			File executableJobFiles) throws EngineUnavailableException, AuthenticationFailedException, ServiceInvocationFailedException;
}