/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.datamart.provider;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.DatamartProviderConfiguration;

/**
 * Builds and returns class instances of the  DatamartProviderIFace interface 
 */
public class DatamartProviderFactory {

	/**
	 * Builds and returns class instances of the  DatamartProviderIFace interface 
	 * @param className The class name 
	 * @return an instance of the class which must implement the DatamartProviderIFace interface
	 * @throws Exception if the class doesn't exist or it doens't implement the interface
	 */
    public static IDatamartProvider getDatamartProvider(DatamartProviderConfiguration datamartProviderConfiguration) throws Exception {
    	IDatamartProvider datamartProvider = null;
    	String dataProvClassName = datamartProviderConfiguration.getClassName();
    	datamartProvider = (IDatamartProvider) Class.forName(dataProvClassName).newInstance();
    	datamartProvider.setDatamartProviderConfiguration(datamartProviderConfiguration);
    	return datamartProvider;
    }
    
}