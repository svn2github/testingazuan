package it.eng.qbe.action;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Utils;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;

import org.hibernate.SessionFactory;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to instantiate the datamart where
 * 
 * working with qbe
 */
public class SelectDataMartAction extends AbstractAction {

	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		
		String jndiDataSourceName = (String)request.getAttribute("JNDI_DS");
		
		String dialect = (String)request.getAttribute("DIALECT");
		
		String dmPath = (String)request.getAttribute("PATH");
		
		DataMartModel dmModel = new DataMartModel(dmPath, jndiDataSourceName, dialect);
		
		ApplicationContainer application = ApplicationContainer.getInstance();
		
		ApplicationContainer.getInstance().setAttribute("CURRENT_THREAD_CONTEXT_LOADER", Thread.currentThread().getContextClassLoader());
		
		dmModel.setName(dmModel.getPath());
		dmModel.setDescription(dmModel.getPath());
		
		SessionFactory sf = Utils.getSessionFactory(dmModel, application);
		
        getRequestContainer().getSessionContainer().setAttribute("dataMartModel", dmModel);
        
	}
}
