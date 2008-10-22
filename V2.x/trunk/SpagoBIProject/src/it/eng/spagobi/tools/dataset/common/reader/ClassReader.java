/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.reader;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.behaviouralmodel.lov.bo.JavaClassDetail;
import it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet;
import it.eng.spagobi.tools.dataset.bo.JClassDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreImpl;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.FieldMetadata;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IFieldMeta;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.datastore.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class ClassReader implements IDataReader {
	
	IEngUserProfile profile=null;
	JClassDataSet ds = null;

    public ClassReader() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClassReader(IEngUserProfile profile, JClassDataSet ds) {
		super();
		this.profile = profile;
		this.ds = ds;
	}

	public IDataStore read(HashMap parameters) {
    	
    	IDataStore ids = (IDataStore)new DataStoreImpl();
    	
		String javaClassName = ds.getJavaClassName();
		JavaClassDetail javaClassDetail =new JavaClassDetail();
		try{					
			IJavaClassDataSet javaClass = (IJavaClassDataSet) Class.forName(javaClassName).newInstance();
    		String result = javaClass.getValues(profile);
    		result = result.trim();
    		boolean toconvert =javaClassDetail.checkSintax(result);
    		// check if the result must be converted into the right xml sintax
			if(toconvert) { 
				result = javaClassDetail.convertResult(result);
			}
    		
    		SourceBean rowsSourceBean = null;
    		List colNames = new ArrayList();
    		
    		rowsSourceBean = SourceBean.fromXMLString(result);
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
    		
		} catch (Exception e) {
			String stacktrace = e.toString();
		}

	return ids;
    }

	public IEngUserProfile getProfile() {
		return profile;
	}

	public void setProfile(IEngUserProfile profile) {
		this.profile = profile;
	}

	public JClassDataSet getDs() {
		return ds;
	}

	public void setDs(JClassDataSet ds) {
		this.ds = ds;
	}

}
