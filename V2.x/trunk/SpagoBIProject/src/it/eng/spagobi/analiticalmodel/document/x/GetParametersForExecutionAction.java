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
package it.eng.spagobi.analiticalmodel.document.x;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ObjParuse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ParameterUse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IObjParuseDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IParameterUseDAO;
import it.eng.spagobi.behaviouralmodel.check.bo.Check;
import it.eng.spagobi.chiron.serializer.SerializationException;
import it.eng.spagobi.chiron.serializer.SerializerFactory;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class GetParametersForExecutionAction  extends AbstractSpagoBIAction {
	
	public static final String SERVICE_NAME = "GET_PARAMETERS_FOR_EXECUTION_SERVICE";
	
	// request parameters
	public static String DOCUMENT_ID = ObjectsTreeConstants.OBJECT_ID;
	public static String DOCUMENT_LABEL = ObjectsTreeConstants.OBJECT_LABEL;
	
	// logger component
	private static Logger logger = Logger.getLogger(GetParameterValuesForExecutionAction.class);
	
	
	public void doService() {
		
		ExecutionInstance executionInstance;
		
		Assert.assertNotNull(getContext(), "Execution context cannot be null" );
		Assert.assertNotNull(getContext().getExecutionInstance( ExecutionInstance.class.getName() ), "Execution instance cannot be null");
	
		executionInstance = getContext().getExecutionInstance( ExecutionInstance.class.getName() );
		
		BIObject obj = executionInstance.getBIObject();
		String roleName = executionInstance.getExecutionRole();

		List parametersForExecution = new ArrayList();
		List parameters = obj.getBiObjectParameters();
		
		if (parameters != null && parameters.size() > 0) {
			Iterator iter = parameters.iterator();
			while (iter.hasNext()) {
				BIObjectParameter biparam = (BIObjectParameter) iter.next();
				
				parametersForExecution.add( new ParameterForExecution(biparam) );
			}
		}
		
		JSONArray parametersJSON = null;
		try {
			parametersJSON = (JSONArray)SerializerFactory.getSerializer("application/json").serialize( parametersForExecution );
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		
		try {
			writeBackToClient( new JSONSuccess( parametersJSON ) );
		} catch (IOException e) {
			throw new SpagoBIServiceException("Impossible to write back the responce to the client", e);
		}
		

	}

	
	
	public class ParameterForExecution {
		
		Parameter par; 
		String id;
		String label;
		String parType; // DATE, STRING, ...
		String selectionType; // COMBOBOX, LIST, ...
		String typeCode; // SpagoBIConstants.INPUT_TYPE_X
		boolean mandatory;
		
		ObjParuse fatherObject;
		
		List dependencies;
		
		public ParameterForExecution(BIObjectParameter biparam) {
			id = biparam.getParameterUrlName();
			label = localize( biparam.getLabel() );
			par = biparam.getParameter();
			parType = par.getType(); 
			selectionType = par.getModalityValue().getSelectionType();
			typeCode = par.getModalityValue().getITypeCd();			
			
			
			
			mandatory = false;
			Iterator it = par.getChecks().iterator();	
			while (it.hasNext()){
				Check check = (Check)it.next();
				if (check.getValueTypeCd().equalsIgnoreCase("MANDATORY")){
					mandatory = true;
					break;
				}
			} 
			
			try {
				dependencies = DAOFactory.getObjParuseDAO().getDependencies(biparam.getId());
			} catch (EMFUserError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Object[] results = getObjectFather(biparam);
			BIObjectParameter objParFather = (BIObjectParameter) results[1];
			ObjParuse objParuse = (ObjParuse) results[0];
			if (objParFather != null && objParuse != null) {
				fatherObject = objParuse;
				fatherObject.getParuseId(); 
			}
		}
		
		
		private Object[] getObjectFather(BIObjectParameter biparam) {
			BIObjectParameter objParFather = null;
			ObjParuse objParuse = null;
			try {
				IObjParuseDAO objParuseDAO = DAOFactory.getObjParuseDAO();
				IParameterUseDAO paruseDAO = DAOFactory.getParameterUseDAO();
				List objParuses = objParuseDAO.loadObjParuses(biparam.getId());
				if (objParuses != null && objParuses.size() > 0) {
					Iterator it = objParuses.iterator();
					while (it.hasNext()) {
						ObjParuse aObjParuse = (ObjParuse) it.next();
						Integer paruseId = aObjParuse.getParuseId();
						ParameterUse aParameterUse = paruseDAO.loadByUseID(paruseId);
						Integer idLov = aParameterUse.getIdLov();
						if (idLov.equals(biparam.getParameter().getModalityValue().getId())) {
							// the ModalitiesValue of the BIObjectParameter
							// corresponds to a ParameterUse correlated
							objParuse = aObjParuse;
							logger.debug("Found correlation:" + " dependent BIObjectParameter id = " + biparam.getId()
									+ "," + " ParameterUse with id = " + paruseId + ";"
									+ " BIObjectParameter father has id = " + objParuse.getObjParFatherId());
							// now we have to find the BIObjectParameter father of
							// the correlation
							Integer objParFatherId = objParuse.getObjParFatherId();
							ExecutionInstance instance =  getContext().getExecutionInstance( ExecutionInstance.class.getName() );
							BIObject obj = instance.getBIObject();
							List parameters = obj.getBiObjectParameters();
							Iterator i = parameters.iterator();
							while (i.hasNext()) {
								BIObjectParameter aBIObjectParameter = (BIObjectParameter) i.next();
								if (aBIObjectParameter.getId().equals(objParFatherId)) {
									objParFather = aBIObjectParameter;
									break;
								}
							}
							if (objParFather == null) {
								// the BIObjectParameter father of the correlation
								// was not found
								logger.error("Cannot find the BIObjectParameter father of the correlation");
								throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
							}
							break;
						}
					}
				}
			} catch (EMFUserError e) {
				logger.error("Error while retrieving information from db", e);
				e.printStackTrace();
			}
		
			return new Object[] { objParuse, objParFather };
		}
		
		public String getId() {
			return id;
		}


		public void setId(String id) {
			this.id = id;
		}
		
		public String getTypeCode() {
			return typeCode;
		}

		public void setTypeCode(String typeCode) {
			this.typeCode = typeCode;
		}

		public Parameter getPar() {
			return par;
		}

		public void setPar(Parameter par) {
			this.par = par;
		}

		public String getParType() {
			return parType;
		}

		public void setParType(String parType) {
			this.parType = parType;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public boolean isMandatory() {
			return mandatory;
		}

		public void setMandatory(boolean mandatory) {
			this.mandatory = mandatory;
		}

		public String getSelectionType() {
			return selectionType;
		}

		public void setSelectionType(String selectionType) {
			this.selectionType = selectionType;
		}

		public ObjParuse getFatherObject() {
			return fatherObject;
		}

		public void setFatherObject(ObjParuse fatherObject) {
			this.fatherObject = fatherObject;
		}

		public List getDependencies() {
			return dependencies;
		}

		public void setDependencies(List dependencies) {
			this.dependencies = dependencies;
		}

		
	}

}
