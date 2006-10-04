
package it.eng.qbe.action;

import org.hibernate.SessionFactory;

import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This action is responsible to put the contents of the query composed automatically
 * in the expert query contents
 */
public class AlignExpertAction extends AbstractAction {
	
		
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		try{
			
			RequestContainer aRequestContainer = getRequestContainer();
			SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
			DataMartModel dm = (it.eng.qbe.model.DataMartModel)aSessionContainer.getAttribute("dataMartModel"); 
		    ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(aSessionContainer);
			
		    aWizardObject.composeQuery();
			SessionFactory sf = Utils.getSessionFactory(dm, ApplicationContainer.getInstance());
			HqlToSqlQueryRewriter queryRewriter = new HqlToSqlQueryRewriter(sf.openSession());
			String sqlQuery = queryRewriter.rewrite( aWizardObject.getFinalQuery() );
			aWizardObject.setExpertQueryDisplayed(sqlQuery);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
