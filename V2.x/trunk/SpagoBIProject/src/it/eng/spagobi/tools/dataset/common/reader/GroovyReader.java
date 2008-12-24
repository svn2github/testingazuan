/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.reader;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.behaviouralmodel.lov.handlers.ScriptManager;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.bo.QueryDataSet;
import it.eng.spagobi.tools.dataset.bo.ScriptDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreImpl;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.FieldMetadata;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IFieldMeta;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.datastore.Record;
import it.eng.spagobi.tools.dataset.common.transformer.IDataTransformer;
import it.eng.spagobi.tools.dataset.common.transformer.PivotingTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class GroovyReader implements IDataReader {

	private static transient Logger logger = Logger.getLogger(GroovyReader.class);
	ScriptDataSet ds=null;
	IEngUserProfile profile=null;

    public GroovyReader() {
		super();
		// TODO Auto-generated constructor stub
	}


	public IDataStore read(HashMap parameters) {
		
		logger.debug("IN");
		IDataStore ids = (IDataStore)new DataStoreImpl();
    	
		String script = ds.getScript();
		String result = null;

		try {
		result = ScriptManager.runScript(script);
		
		// check if the result must be converted into the right xml sintax
		boolean toconvert = checkSintax(result);
		if(toconvert) { 
			result = convertResult(result);
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
		} catch (SourceBeanException e) {
			logger.error("SourceBeanException",e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception",e);
			e.printStackTrace();
		}
		// pivoting of results if its configurated
		if (ds.getPivotColumnName() != null && !ds.getPivotRowName().equals("") && !ds.getPivotColumnName().equals("")){
			ids.applyTranformer((IDataTransformer)new PivotingTransformer(), ds.getPivotColumnName(), 
									ds.getPivotRowName(), ds.getPivotColumnValue());
		}
		else
			logger.info("Pivot is not applicated on the result dataset because therisn't a complete configuration.\n" +
					    " PivotColumnName: " + ds.getPivotColumnName() + " PivotRowName: " + ds.getPivotRowName() + 
					    " PivotCoumnValue: " + ds.getPivotColumnValue());
		logger.debug("OUT");
	return ids;
    }
    
    private boolean checkSintax(String result) {
		
    	logger.debug("IN");
		List visibleColumnNames = null;
		String valueColumnName = "";
		String descriptionColumnName = "";
		
		boolean toconvert = false;
		try{
			SourceBean source = SourceBean.fromXMLString(result);
			if(!source.getName().equalsIgnoreCase("ROWS")) {
				toconvert = true;
			} else {
				List rowsList = source.getAttributeAsList(DataRow.ROW_TAG);
				if( (rowsList==null) || (rowsList.size()==0) ) {
					toconvert = true;
				} else {
					// TODO this part can be moved to the import transformer
					// RESOLVES RETROCOMPATIBILITY PROBLEMS
					// finds the name of the first attribute of the rows if exists 
					String defaultName = "";
					SourceBean rowSB = (SourceBean) rowsList.get(0);
					List attributes = rowSB.getContainedAttributes();
					if (attributes != null && attributes.size() > 0) {
						SourceBeanAttribute attribute = (SourceBeanAttribute) attributes.get(0);
						defaultName = attribute.getKey();
					}
					// if a value column is specified, it is considered
					SourceBean valueColumnSB = (SourceBean) source.getAttribute("VALUE-COLUMN");
					if (valueColumnSB != null) {
						String valueColumn = valueColumnSB.getCharacters();
						if (valueColumn != null) {
							valueColumnName = valueColumn;
						}
					} else {
						valueColumnName = defaultName;
					}
					SourceBean visibleColumnsSB = (SourceBean) source.getAttribute("VISIBLE-COLUMNS");
					if (visibleColumnsSB != null) {
						String allcolumns = visibleColumnsSB.getCharacters();
						if (allcolumns != null) {
							String[] columns = allcolumns.split(",");
							visibleColumnNames = Arrays.asList(columns);
						}
					} else {
						String[] columns = new String[] {defaultName};
						visibleColumnNames = Arrays.asList(columns);
					}
					SourceBean descriptionColumnSB = (SourceBean) source.getAttribute("DESCRIPTION-COLUMN");
					if (descriptionColumnSB != null) {
						String descriptionColumn = descriptionColumnSB.getCharacters();
						if (descriptionColumn != null) {
							descriptionColumnName = descriptionColumn;
						}
					} else {
						descriptionColumnName = defaultName;
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("the result of the dataset is not formatted with the right structure so it will be wrapped inside an xml envelope",e);
			toconvert = true;
		}
		logger.debug("OUT");
		return toconvert;
	}
	
	private String convertResult(String result) {
		
		logger.debug("IN");
		
		List visibleColumnNames = null;
		String valueColumnName = "";
		String descriptionColumnName = "";
		StringBuffer sb = new StringBuffer();
		sb.append("<ROWS>");
		sb.append("<ROW VALUE=\"" + result +"\"/>");
		sb.append("</ROWS>");
		descriptionColumnName = "VALUE";
		valueColumnName = "VALUE";
		String [] visibleColumnNamesArray = new String [] {"VALUE"};
		visibleColumnNames = Arrays.asList(visibleColumnNamesArray);
		
		logger.debug("OUT");
		return sb.toString();
	}

	public ScriptDataSet getDs() {
		return ds;
	}

	public void setDs(ScriptDataSet ds) {
		this.ds = ds;
	}

	public void setDataSetConfig(DataSetConfig ds) {
		this.ds =(ScriptDataSet)ds;
		
	}

	public void setProfile(IEngUserProfile profile) {
		this.profile = profile;
		
	}

	public IEngUserProfile getProfile() {
		return profile;
	}
	

}
