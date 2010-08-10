/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.test;

import it.eng.geo.datamart.DatamartObject;
import it.eng.geo.datamart.DefaultDatamartProvider;
import it.eng.geo.document.XMLDocumentException;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import java.io.IOException;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TestDefaultDatamartProvider{


    public static void main(String[] args) throws IOException, XMLDocumentException {
        
        String conf = "<DATAMART_PROVIDER class_name=\"it.eng.geo.datamart.DefaultDatamartProvider\"" 
							            + " registred_pool_name=\"geopostgres\"" 
							            + " query_name=\"SELECT_ABITANTI\""
							            + " element_id=\"path_id\""
							            + " element_value=\"num_abitanti\"/>";
        SourceBean confSB = null;
        try {
            confSB = SourceBean.fromXMLString(conf);
        } catch (SourceBeanException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        DefaultDatamartProvider datamartProvider = new DefaultDatamartProvider();
        DatamartObject datamartObject = datamartProvider.getDatamartObject(confSB);
    }
}