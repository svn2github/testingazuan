/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.reader;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.tools.dataset.bo.FileDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreImpl;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.FieldMetadata;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IFieldMeta;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.datastore.Record;
import it.eng.spagobi.tools.dataset.service.ListTestDataSetModule;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class FileReader implements IDataReader {
	
	private static transient Logger logger = Logger.getLogger(FileReader.class);
	FileDataSet ds = null;

    public FileReader() {
		super();
		// TODO Auto-generated constructor stub
	}


	public FileReader(FileDataSet dataSet) {
		super();
		this.ds = dataSet;
	}


	public FileDataSet getDs() {
		return ds;
	}


	public void setDs(FileDataSet ds) {
		this.ds = ds;
	}


	public IDataStore read(HashMap parameters) {

		logger.debug("IN");
		
		IDataStore ids = (IDataStore)new DataStoreImpl();
		FileInputStream fis = null;
		String fileName = ds.getFileName();
		try{
			fis = new FileInputStream(fileName);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception during File Opening");
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
					
					while(iterator.hasNext()){
											
					SourceBean sb = (SourceBean) iterator.next();
					//For each row I instanciate an IRecord
					IRecord r = (IRecord)new Record();
					
					List sbas=sb.getContainedAttributes();
					for (Iterator iterator2 = sbas.iterator(); iterator2.hasNext();) {
						
						SourceBeanAttribute object = (SourceBeanAttribute) iterator2.next();
						String fieldName=object.getKey();
						IFieldMeta fMeta = (IFieldMeta)new FieldMetadata();
						fMeta.setName(fieldName);
						//Each Record is made out of different IFields with a value and metadata
						IField f = (IField)new Field(fMeta,object);
						r.appendField(f);
					}
					ids.appendRow(r);
					}
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception reading File data");
		}
		finally{
			if(fis!=null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("IOException during File Closure");
				}
		}
		logger.debug("OUT");
	return ids;
    }

}
