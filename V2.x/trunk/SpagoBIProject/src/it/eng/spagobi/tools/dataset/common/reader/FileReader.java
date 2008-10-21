/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.reader;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.tools.dataset.bo.FileDataSet;
import it.eng.spagobi.tools.dataset.common.DataSetImpl;
import it.eng.spagobi.tools.dataset.common.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreImpl;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.service.DetailDataSetModule;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.InputSource;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class FileReader implements IDataReader {


    public IDataStore read(HashMap parameters) {

    	IDataStore dataStore = (IDataStore)new DataStoreImpl();
    	
		String pathFile=(String)parameters.get("FILENAME");

		FileInputStream fis=null;
		try{
		fis=new FileInputStream(pathFile);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		try{
		InputSource inputSource=new InputSource(fis);

		SourceBean rowsSourceBean = null;
		List colNames = new ArrayList();

			rowsSourceBean = SourceBean.fromXMLStream(inputSource);
			//I must get columnNames. assumo che tutte le righe abbiano le stesse colonne
			if(rowsSourceBean!=null){
				List row =rowsSourceBean.getAttributeAsList("ROW");
				if(row.size()>=1){
					Iterator iterator = row.iterator(); 
					SourceBean sb = (SourceBean) iterator.next();
					List sbas=sb.getContainedAttributes();
					for (Iterator iterator2 = sbas.iterator(); iterator2.hasNext();) {
						SourceBeanAttribute object = (SourceBeanAttribute) iterator2.next();
						String name=object.getKey();
						colNames.add(name);
					}
					
				}
			}
		}

		catch (Exception e) {
			//return null;
		}
		finally{
			if(fis!=null)
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	return null;
    }

}
