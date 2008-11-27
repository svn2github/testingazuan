package it.eng.spagobi.kpi.model.service;

import java.util.List;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.kpi.model.bo.ModelInstance;
import it.eng.spagobi.kpi.utils.AbstractConfigurableListModule;

public class ListModelInstanceModule extends AbstractConfigurableListModule {

	private static transient Logger logger = Logger.getLogger(ListModelInstanceModule.class);

	@Override
	protected List getObjectList(SourceBean request) {
		List result = null;
		try {
			result = DAOFactory.getModelInstanceDAO().loadModelsInstanceRoot();
		} catch (EMFUserError e) {
			logger.error(e);
		}
		return result;
	}

	@Override
	protected void setRowAttribute(SourceBean rowSB, Object obj)
			throws SourceBeanException {
		ModelInstance aModelInstance = (ModelInstance) obj;
		rowSB.setAttribute("name", aModelInstance.getName());
		rowSB.setAttribute("id", aModelInstance.getId());

	}

}
