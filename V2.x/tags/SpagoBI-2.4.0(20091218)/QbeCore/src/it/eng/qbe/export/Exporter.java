package it.eng.qbe.export;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import it.eng.qbe.model.QbeDataSet;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStoreMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Exporter {
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(Exporter.class);
	IDataStore dataStore = null;

	public Exporter(IDataStore dataStore) {
		super();
		this.dataStore = dataStore;
	}

	public IDataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(IDataStore dataStore) {
		this.dataStore = dataStore;
	}

	public Exporter() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Workbook exportInExcel(String language, String country){
		Workbook wb = new HSSFWorkbook();
	    CreationHelper createHelper = wb.getCreationHelper();
	    Sheet sheet = wb.createSheet("new sheet");

		SourceBean formatSB=null; 
		// if a particular language is specified take the corresponding number-format
		if(language!=null ){
			if(country==null){
				formatSB = ((SourceBean)ConfigSingleton.getInstance().getAttribute("QBE.QBE-EXCEL-EXPORT-"+language.toUpperCase()));
			}
			else{
				formatSB = ((SourceBean)ConfigSingleton.getInstance().getAttribute("QBE.QBE-EXCEL-EXPORT-"+language.toUpperCase()+"_"+country.toUpperCase()));				
			}		
		}
		if(formatSB==null){
			formatSB = ((SourceBean)ConfigSingleton.getInstance().getAttribute("QBE.QBE-EXCEL-EXPORT-DEFAULT"));
		}
		String format = (String) formatSB.getAttribute("numberFormat");
		logger.debug("Number Format: "+format);		
	    
	    if(dataStore!=null  && !dataStore.isEmpty()){
	    	IDataStoreMetaData d = dataStore.getMetaData();	
	    	int colnum = d.getFieldCount();
	    	Row row = sheet.createRow((short)0);
	    	for(int j =0;j<colnum;j++){
	    		Cell cell = row.createCell(j);
	    	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    	    String fieldName = d.getFieldName(j);	
	    	    cell.setCellValue(createHelper.createRichTextString(fieldName));
	    	}
	    	
	    	Iterator it = dataStore.iterator();
	    	int rownum = 1;
	    	short formatIndexInt = HSSFDataFormat.getBuiltinFormat("#,##0");
		    CellStyle cellStyleInt = wb.createCellStyle();   
		    cellStyleInt.setDataFormat(formatIndexInt);
		    
		    short formatIndexDoub = HSSFDataFormat.getBuiltinFormat("#,##0.00");
		    CellStyle cellStyleDoub = wb.createCellStyle();   
		    cellStyleDoub.setDataFormat(formatIndexDoub);
		    
			while(it.hasNext()){
				Row rowVal = sheet.createRow(rownum);
				IRecord record =(IRecord)it.next();
				List fields = record.getFields();
				int length = fields.size();
				for(int fieldIndex =0; fieldIndex<length; fieldIndex++){
					IField f = (IField)fields.get(fieldIndex);
					if (f != null && f.getValue()!= null) {
						
						Class c = d.getFieldType(fieldIndex);
						logger.debug("Column [" + (fieldIndex+1) + "] class is equal to [" + c.getName() + "]");
						if( Integer.class.isAssignableFrom(c) || Short.class.isAssignableFrom(c)) {
							logger.debug("Column [" + (fieldIndex+1) + "] type is equal to [" + "INTEGER" + "]");
							Cell cell = rowVal.createCell(fieldIndex);
						    Number val = (Number)f.getValue();
						    cell.setCellValue(val.intValue());
						    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						    cell.setCellStyle(cellStyleInt);
						}else if( Number.class.isAssignableFrom(c) ) {
							logger.debug("Column [" + (fieldIndex+1) + "] type is equal to [" + "NUMBER" + "]");
							Cell cell = rowVal.createCell(fieldIndex);
						    Number val = (Number)f.getValue();
						    cell.setCellValue(val.doubleValue());
						    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						   // List formats = HSSFDataFormat.getBuiltinFormats();
						    cell.setCellStyle(cellStyleDoub);
						}else if( String.class.isAssignableFrom(c)){
							logger.debug("Column [" + (fieldIndex+1) + "] type is equal to [" + "STRING" + "]");
							Cell cell = rowVal.createCell(fieldIndex);		    
						    String val = (String)f.getValue();
						    cell.setCellValue(createHelper.createRichTextString(val));
						    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						}else if( Boolean.class.isAssignableFrom(c) ) {
							logger.debug("Column [" + (fieldIndex+1) + "] type is equal to [" + "BOOLEAN" + "]");
							Cell cell = rowVal.createCell(fieldIndex);
						    Boolean val = (Boolean)f.getValue();
						    cell.setCellValue(val.booleanValue());
						    cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
						}else if(Date.class.isAssignableFrom(c)){
							logger.debug("Column [" + (fieldIndex+1) + "] type is equal to [" + "DATE" + "]");
							CellStyle cellStyle = wb.createCellStyle();
						    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy"));
						    Cell cell = rowVal.createCell(fieldIndex);		    
						    Date val = (Date)f.getValue();
						    cell.setCellValue(val);	
						    cell.setCellStyle(cellStyle);
						}else{
							logger.warn("Column [" + (fieldIndex+1) + "] type is equal to [" + "???" + "]");
							Cell cell = rowVal.createCell(fieldIndex);
						    String val = f.getValue().toString();
						    cell.setCellValue(createHelper.createRichTextString(val));
						    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						}
						
					}
				}
				rownum ++;
			}
	    }

	    return wb;
	}

}
