/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.sdk.utilities;

import it.eng.spagobi.sdk.documents.bo.SDKSchema;
import it.eng.spagobi.sdk.exceptions.SDKException;
import it.trend.inmind.dmp.dm.Column;
import it.trend.inmind.dmp.dm.Table;
import it.trend.inmind.dmp.parser.Parser;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.axis.attachments.ManagedMemoryDataSource;
import org.apache.log4j.Logger;

public class SchemaConverter {

	static private Logger logger = Logger.getLogger(SchemaConverter.class);

	public DataHandler fromDMPFileToSchemaMondrian(SDKSchema schema) throws Exception{
		logger.debug("IN");
		DataHandler toReturn = null;
		try {
            String content = "";
            InputStream schemaDefinition = schema.getSchemaFile().getContent().getInputStream();
            Parser p = new Parser(schemaDefinition);         
            
            content = writeRow(null,"<Schema name='"+schema.getSchemaName()+"'>\n");
            //loading shared dimensions
            List<Table> tables = p.getTables();     
            if (tables == null || tables.size() == 0){
            	logger.error("Tables aren't defined in DMP file xml. Check the source file." );
				throw new SDKException("2010", "Tables aren't defined. Check the source file. " );
            }
            boolean foundFtTable = false;
            for(Table t: tables) {
            	if (t.isFactTable()){
            		foundFtTable = true;
        		  String cubeName = (t.getDescription()!= null)? t.getDescription() : t.getName();            
                  content = writeRow(content,"\t<Cube name='" + cubeName + "'>\n");
                  content = writeRow(content,"\t\t<Table name='" + t.getName() + "'/>\n");
                  Set<Column> columns = t.getKeyColumns();   
                  if (columns == null || columns.size() == 0){
                	logger.error("The fact table has not key columns. Is not possible to define links with dimensions." );
      				throw new SDKException("2020", "The fact table has not key columns. Is not possible to define links with dimensions. Check the source file. " );
                  }
            		//definition of all dimensions from key columns of the fact table                  
            		for(Column ftKeyCol: columns) {
            			Table dimension = ftKeyCol.getLink();
            			if (dimension == null ){
            				logger.error("The dimension linked to column " + ftKeyCol.getName() + " is undefined.");
            				throw new SDKException("2030", "The dimension linked to column " + ftKeyCol.getName() + " is undefined.");
            			}
            			if (dimension.getSurrogateKeyName()==null){
            				logger.error("The surrogate key name isn't defined for dimension " + dimension.getName());
            				throw new SDKException("2031", "The linked dimension is undefined or he surrogate key name isn't defined for dimension " + dimension.getName());
            			}
            			String dimName = (dimension.getDescription()!= null)? dimension.getDescription() : dimension.getName();            			
            			String surrogateKey = dimension.getSurrogateKeyName();
            			//---------------------------------
            			// ONLY FOR LOCAL TEST
            			//surrogateKey = "STATUS_RAPPORTO";
            			//---------------------------------
            			content = writeRow(content,"\t\t<Dimension name='"+ dimName +"' foreignKey='" + ftKeyCol.getName() +"' >\n");
            			content = writeRow(content,"\t\t\t<Hierarchy hasAll='true' allMemberName='All' primaryKey='"+ surrogateKey +"'>\n");
            			content = writeRow(content,"\t\t\t\t<Table name='"+ dimension.getName() +"'/>\n");
            			content = writeRow(content,"\t\t\t\t<Level name='"+ dimName +"' column='" + surrogateKey + "'  uniqueMembers='false'/>\n");
            			content = writeRow(content,"\t\t\t</Hierarchy>\n");
            			/*
            		    boolean hierarchyExists = false;
            			for(Column dimCol: dimension.getKeyColumns()) {  
            				if (hierarchyExists){
            					logger.error("Too many columns are defined as dimension keys for dimension " + dimension.getName());
                				throw new SDKException("2020", "Too many columns are defined as dimension keys for dimension " + dimension.getName() +
                											   ". Check the source xml file");
            				}
                			String levName = (dimCol.getDescription()!= null)? dimCol.getDescription() : dimCol.getName(); 
                			content = writeRow(content,"\t\t\t<Hierarchy hasAll='true' allMemberName='All' primaryKey='"+ dimension.getSurrogateKeyName() +"'>\n");
                			content = writeRow(content,"\t\t\t\t<Table name='"+ dimension.getName() +"'/>\n");
                			content = writeRow(content,"\t\t\t\t<Level name='"+ levName +"' column='" + dimension.getSurrogateKeyName() + "'  uniqueMembers='false'/>\n");
                			content = writeRow(content,"\t\t\t</Hierarchy>\n");
                			hierarchyExists = true;                			
            			} 
            			*/          			            			
                		content = writeRow(content,"\t\t</Dimension>\n");           
            		} 
            		//definition of measures
            		boolean foundMeasures = false;
            		for(Column ftCol: t.getColumns()) {                   			            			
            			if (ftCol.getType() == (ftCol.getType().NM)){        
            				foundMeasures = true;
            				String measureName = (ftCol.getDescription()!= null)? ftCol.getDescription() : ftCol.getName();  
                			//String measureAggr = ftCol.getAggrFunction();
                			String measureAggr = "sum"; //forced as default
                			String measureFormat = "";
                			String decimalFormat = "";
                			if (ftCol.getDecimals() != 0){
                				decimalFormat = ".";
                				for (int i=0, l=ftCol.getDecimals(); i<l; i++){
                					decimalFormat += "0";
                				}
                			}
                			if (ftCol.getDecimals() < ftCol.getLen()){
                				for (int i=ftCol.getLen()-ftCol.getDecimals(), l=0; i>l; i--){
                					if (i<ftCol.getLen()-ftCol.getDecimals() && i%3 == 0){
                						measureFormat += "," ;
                					}
                					measureFormat += "#" ;                					
                				}
                			}
                			measureFormat += decimalFormat;
            				content = writeRow(content,"\t\t<Measure name='"+measureName+"' column='"+ftCol.getName()+"' aggregator='"+measureAggr+"' formatString='"+measureFormat+"'/>\n");
            			}
            		} 
            		if (!foundMeasures){
                    	logger.error("Measures aren't defined in DMP File. Isn't possible to create the Mondrian cube! Check the source file." );
        				throw new SDKException("2040", "Fact table isn't defined in DMP File. Isn't possible to create the Mondrian cube! Check the source file." );
                    }
            		content = writeRow(content,"\t</Cube>\n");  
            	}            	
	        } 
            if (!foundFtTable){
            	logger.error("Fact table isn't defined in DMP File. Isn't possible to create the Mondrian cube! Check the source file." );
				throw new SDKException("2050", "Measures aren't defined in DMP File. Isn't possible to create the Mondrian cube! Check the source file." );
            }          
            content = writeRow(content,"</Schema>\n");  
            byte[] bytes = content.getBytes();
            ManagedMemoryDataSource mods =  new ManagedMemoryDataSource(new java.io.ByteArrayInputStream(bytes), Integer.MAX_VALUE - 2,
					null, true);
            toReturn = new DataHandler(mods);	
        } catch (SDKException se) {
			throw new SDKException(se.getCode(), se.getDescription());
		} catch(Exception e) {
        	logger.error("Error on decoding DMP File: " + e);
            throw new SDKException("2100", e.getMessage());
        }
		logger.debug("OUT");
		return toReturn;
	}
	
	private String writeRow(String content, String row){
		String toReturn = "";
		if (content != null) toReturn = content;
		
		toReturn += row;
		return toReturn;
	}

		
}
