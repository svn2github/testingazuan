/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.reader;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.tools.dataset.common.DataSetImpl;
import it.eng.spagobi.tools.dataset.common.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreImpl;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.FieldMetadata;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IFieldMeta;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.datastore.Record;
import it.eng.spagobi.tools.dataset.service.DetailDataSetModule;

import java.io.File;
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
	
	String fileName = "";


    public FileReader() {
		super();
		// TODO Auto-generated constructor stub
	}


	public FileReader(String fileName) {
		super();
		this.fileName = fileName;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public IDataStore read(HashMap parameters) {

		IDataStore ids = (IDataStore)new DataStoreImpl();
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(fileName);
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
					
					while(iterator.hasNext()){
											
					SourceBean sb = (SourceBean) iterator.next();
					IRecord r = (IRecord)new Record();
					
					List sbas=sb.getContainedAttributes();
					for (Iterator iterator2 = sbas.iterator(); iterator2.hasNext();) {
						
						SourceBeanAttribute object = (SourceBeanAttribute) iterator2.next();
						String fieldName=object.getKey();
						IFieldMeta fMeta = (IFieldMeta)new FieldMetadata();
						fMeta.setName(fieldName);
						IField f = (IField)new Field(fMeta,object);
						r.appendField(f);
					}
					ids.appendRow(r);
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
	return ids;
    }

}
