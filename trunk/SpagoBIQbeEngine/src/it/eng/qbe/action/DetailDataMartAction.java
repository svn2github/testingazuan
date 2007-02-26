package it.eng.qbe.action;

import it.eng.qbe.datasource.HibernateDataSource;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.DataMartModel;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to instantiate the datamart where
 * 
 * working with qbe
 */
public class DetailDataMartAction extends AbstractAction {

	/**
	 * Builds an empty datamart just to retrive all the datamarts defined and saved
	 * TODO buid a proper class to do this job and build the datamart only once when
	 * all the data are in place
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		String path = (String)request.getAttribute("PATH");
		IDataSource dataSource = new HibernateDataSource(path, null, null);
		DataMartModel dataMartModel = new DataMartModel(dataSource);
				
		//DataMartModel dataMartModel = new DataMartModel(, null, null);
		getRequestContainer().getSessionContainer().setAttribute("dataMartModel", dataMartModel);
       
	}
}
