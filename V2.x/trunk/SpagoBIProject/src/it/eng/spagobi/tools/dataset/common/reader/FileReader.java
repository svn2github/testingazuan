/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.reader;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.bo.FileDataSet;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class FileReader implements IDataReader {
	
	public static final String messageBundle = "component_spagobidashboardIE_messages";
	private static transient Logger logger = Logger.getLogger(FileReader.class);
	FileDataSet ds = null;
	IEngUserProfile profile=null;
	String fileType = "xml";
	final static String SEPARATOR=";";

    public IEngUserProfile getProfile() {
		return profile;
	}




	public void setProfile(IEngUserProfile profile) {
		this.profile = profile;
	}




	public FileReader() {
		super();
		// TODO Auto-generated constructor stub
	}




	public FileDataSet getDs() {
		return ds;
	}


	public void setDataSetConfig(DataSetConfig ds) {
		this.ds = (FileDataSet)ds;
	}


	public IDataStore read(HashMap parameters) {

		logger.debug("IN");
		
		IDataStore ids = (IDataStore)new DataStoreImpl();
		FileInputStream fis = null;
		String fileName = ds.getFileName();
		if(fileName!=null){
			int lenght = fileName.length();
			fileType = fileName.substring(lenght-3, lenght);			
		}
		try{
			fis = new FileInputStream(fileName);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.debug("File not found",e);
		}
		
		if(fileType.equalsIgnoreCase("txt") || fileType.equalsIgnoreCase("xml")){
			
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
		}else if(fileType.equalsIgnoreCase("csv")){
			InputStreamReader fileReader=null;
			LineNumberReader lineReader=null;
			InputSource inputSource=new InputSource(fis);
			InputStream is = inputSource.getByteStream();
			
			try {				
				fileReader=new InputStreamReader(is);
				lineReader=new LineNumberReader(fileReader);
				boolean fineFile=false;
				int numRow = 0;
				List fields = new ArrayList();
				while (!fineFile){
					
					String riga=lineReader.readLine();
					if (riga==null) fineFile=true;
					else {
						IRecord r = (IRecord)new Record();
						
						StringTokenizer tokenizer=new StringTokenizer(riga,SEPARATOR);
						int count = 0;
						while(tokenizer.hasMoreElements()){
							String token=tokenizer.nextToken();
							if (numRow==0){
								fields.add(token);
							}else{
								if (token!=null) {
								int numberOfFIelds = fields.size();
								IFieldMeta fMeta = (IFieldMeta)new FieldMetadata();
								String fName = (String) fields.get(count);
								fMeta.setName(fName);
								//Each Record is made out of different IFields with a value and metadata
								IField f = (IField)new Field(fMeta,token);
								r.appendField(f);
								count ++;
								}
							}
						}
						if(numRow!=0)ids.appendRow(r);					
					}
					numRow++;
				}	
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		// pivoting of results if its configurated
		if (ds.getPivotColumnName() != null && !ds.getPivotRowName().equals("") && !ds.getPivotColumnName().equals("")){
			ids.applyTranformer((IDataTransformer)new PivotingTransformer(), ds.getPivotColumnName(), 
									ds.getPivotRowName(), ds.getPivotColumnValue());
			logger.info("Pivot is applicated on the result dataset with next configuration: " +
				    " PivotColumnName: " + ds.getPivotColumnName() + " PivotRowName: " + ds.getPivotRowName() + 
				    " PivotCoumnValue: " + ds.getPivotColumnValue());
		}
		else
			logger.info("Pivot is not applicated on the result dataset because therisn't a complete configuration.\n" +
					    " PivotColumnName: " + ds.getPivotColumnName() + " PivotRowName: " + ds.getPivotRowName() + 
					    " PivotCoumnValue: " + ds.getPivotColumnValue());
		logger.debug("OUT");
	return ids;
    }

}
