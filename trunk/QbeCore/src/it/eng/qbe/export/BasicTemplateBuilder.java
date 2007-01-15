/**
 * 
 */
package it.eng.qbe.export;

import it.eng.qbe.utility.CalculatedField;
import it.eng.qbe.utility.LocalFileSystemQueryPersister;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.security.IEngUserProfile;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import sun.security.x509.Extension;

/**
 * @author Gioia
 *
 */
public class BasicTemplateBuilder extends AbstractTemplateBuilder {
	
	String query;
	String queryLanguage;
	Vector queryFields;
	Map params;
	
	String orderedFieldList;
	String extractedEntitiesList;
	String formulaFilePath;
	String manualCalculatedFields;
		
	private List calculatedFields = null;
	
	public static final String SQL_LANGUAGE = "sql";
	public static final String HQL_LANGUAGE = "hql";
	
	public static final String PN_BAND_WIDTH = "bandWidth";	
	public static final String PN_HEADER_HEIGHT = "columnHeaderHeight";	
	public static final String PN_PIXEL_PER_CHAR = "pixelPerChar";
	public static final String PN_PIXEL_PER_ROW = "pixelPerRow";
	public static final String PN_MAXLINE_PER_ROW = "maxLinesPerRow";	
	
	public static final String PN_HEADER_FONT = "columnHeaderFont";	
	public static final String PN_HEADER_FONT_SIZE = "columnHeaderFontSize";
	public static final String PN_HEADER_FORECOLOR = "columnHeaderForegroundColor";
	public static final String PN_HEADER_BACKCOLOR = "columnHeaderBackgroundColor";
	public static final String PN_ROW_FONT = "rowFont";	
	public static final String PN_ROW_FONT_SIZE = "rowFontSize";
	public static final String PN_DETAIL_EVEN_ROW_FORECOLOR = "evenRowsForegroundColor";
	public static final String PN_DETAIL_EVEN_ROW_BACKCOLOR = "evenRowsBackgroundColor";
	public static final String PN_DETAIL_ODD_ROW_FORECOLOR = "oddRowsForegroundColor";
	public static final String PN_DETAIL_ODD_ROW_BACKCOLOR = "oddRowsBackgroundColor";
	
	
	public static final String DEFAULT_BAND_WIDTH = "530";	
	public static final String DEFAULT_HEADER_HEIGHT = "40";
	public static final String DEFAULT_PIXEL_PER_CHAR = "9";
	public static final String DEFAULT_PIXEL_PER_ROW = "16";
	public static final String DEFAULT_MAXLINE_PER_ROW = "4";
	
	public static final String DEFAULT_HEADER_FONT = "Helvetica-Bold";
	public static final String DEFAULT_HEADER_FONT_SIZE = "12";
	public static final String DEFAULT_HEADER_FORECOLOR = "FFFFFF";
	public static final String DEFAULT_HEADER_BACKCOLOR = "#006666";
	public static final String DEFAULT_ROW_FONT = "Times-Roman";	
	public static final String DEFAULT_ROW_FONT_SIZE = "10";
	public static final String DEFAULT_DETAIL_EVEN_ROW_FORECOLOR = "#000000";
	public static final String DEFAULT_DETAIL_EVEN_ROW_BACKCOLOR = "#EEEEEE";
	public static final String DEFAULT_DETAIL_ODD_ROW_FORECOLOR = "#000000";
	public static final String DEFAULT_DETAIL_ODD_ROW_BACKCOLOR = "#FFFFFF";	
	
	private String getParamValue(String paramName, String paramDefaultValue) {
		String paramValue = null;
		
		paramValue = (String)params.get(paramName);
		paramValue = (paramValue != null)? paramValue: paramDefaultValue;
		
		return paramValue;
	}
	
	public BasicTemplateBuilder(String query, 
								String queryLanguage, 
								Vector queryFields, 
								Map params, 
								String orderedFieldList, 
								String extractedEntitiesList, 
								String formulaFilePath) {
		
		this.query = query;
		this.queryLanguage = queryLanguage;
		this.queryFields = queryFields;
		this.params = params;
		
		this.orderedFieldList = orderedFieldList;
		this.extractedEntitiesList = extractedEntitiesList;
		this.formulaFilePath = formulaFilePath;
	}
	
	public String buildTemplate() {
		String templateStr = getTemplateTemplate();
		if(getParamValue("pagination", "false").equalsIgnoreCase("true")) {
			templateStr = replaceParam(templateStr, "pagination", 
					"isIgnorePagination=\"true\"");
		} else {
			templateStr = replaceParam(templateStr, "pagination", "");
		}
		templateStr = replaceParam(templateStr, "lang", queryLanguage);
		templateStr = replaceParam(templateStr, "params", getParamBlock());
		templateStr = replaceParam(templateStr, "query", query);
		templateStr = replaceParam(templateStr, "fields", getFieldsBlock());
		templateStr = replaceParam(templateStr, "body", getColumnHeaderBlock() + getDetailsBlock());
		
		return templateStr;
	}
		
	public String getParamBlock() {
		StringBuffer buffer = new StringBuffer();
		String qbeJRMappingString = calculateQbeJRMappingString();
		buffer.append("<parameter name=\"QBE_JR_MAPPING\" isForPrompting=\"false\" class=\"java.lang.String\"><defaultValueExpression ><![CDATA[\""+qbeJRMappingString+"\"]]></defaultValueExpression></parameter>");
		return buffer.toString();
	}
	
	public String calculateQbeJRMappingString(){
		StringBuffer sb = new StringBuffer();
		String[] orderedField = orderedFieldList.split(";");
		
		Field field = null;
		
		for (int i=0; i < orderedField.length; i++){
			field = (Field)queryFields.get(i);
			sb.append(orderedField[i]+"->"+field.getName());
			if (i != (orderedField.length - 1))
				sb.append(";");
		}
		
		return sb.toString();
	}
	public String getFieldsBlock() {
		StringBuffer buffer = new StringBuffer();
				
		for(int i = 0; i < queryFields.size(); i++) {
			Field field = (Field)queryFields.get(i);
			buffer.append("<field name=\"" + field.getName() + "\" class=\"" + field.getClassType() + "\"/>\n");
			
		}
		
		
		return buffer.toString();
	}
	
	public static final int DETAIL_HEIGHT = 20;
	public static final int DETAIL_WIDTH = 530;
	
	public String getDetailsBlock() {
		StringBuffer buffer = new StringBuffer();
		
		int totalWidth = Integer.parseInt(getParamValue(PN_BAND_WIDTH, DEFAULT_BAND_WIDTH ));
		int detailHeight = getRowHeight(Integer.parseInt(DEFAULT_BAND_WIDTH));
		
		buffer.append("<detail>\n");
		buffer.append("<band " + 
					  "height=\"" + detailHeight + "\"  " + 
					  "isSplitAllowed=\"true\" >\n");
		
		int[] columnWidth = getColumnWidth(totalWidth);	
		int x = 0;
		
		int i=0;
		for(i = 0; i < queryFields.size(); i++) {
			Field field = (Field)queryFields.get(i);
			
			buffer.append("<textField isStretchWithOverflow=\"true\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >\n");
			
			buffer.append("<reportElement " + 
						  		"mode=\"" + "Opaque" + "\" " + 
						  		"x=\"" + x + "\" " + 
						  		"y=\"" + 0 + "\" " + 
						  		"width=\"" + columnWidth[i] + "\" " + 
						  		"height=\"" + detailHeight + "\" " + 
						  		"forecolor=\"" + getParamValue(PN_DETAIL_EVEN_ROW_FORECOLOR, DEFAULT_DETAIL_EVEN_ROW_FORECOLOR ) + "\" " + 
						  		"backcolor=\"" + getParamValue(PN_DETAIL_EVEN_ROW_BACKCOLOR, DEFAULT_DETAIL_EVEN_ROW_BACKCOLOR) + "\" " + 
						  		"key=\"textField\">\n");
			
			buffer.append("<printWhenExpression><![CDATA[new Boolean(\\$V\\{REPORT_COUNT\\}.intValue() % 2 == 0)]]></printWhenExpression>");
			buffer.append("</reportElement>");
			buffer.append("<textElement " +
								"textAlignment=\"" + (field.getClassType().equalsIgnoreCase("java.lang.String")? "Left": "Right") + "\" " +
								"verticalAlignment=\"Middle\"> " +
								"<font pdfFontName=\"" + getParamValue(PN_ROW_FONT, DEFAULT_ROW_FONT)+ "\" " +
									  "size=\"" + getParamValue(PN_ROW_FONT_SIZE, DEFAULT_ROW_FONT_SIZE)+ "\"/>" +
						  "</textElement>\n");
			
			if(field.getClassType().equalsIgnoreCase("java.sql.Date")) {
				buffer.append("<textFieldExpression   " + 
						  "class=\"java.lang.String\"> " + 
						  "<![CDATA[\\$F\\{" + field.getName() + "\\}.toString()]]>\n" +
						  "</textFieldExpression>\n");
			} else {
				buffer.append("<textFieldExpression   " + 
						  "class=\"" + field.getClassType() + "\"> " + 
						  "<![CDATA[\\$F\\{" + field.getName() + "\\}]]>\n" +
						  "</textFieldExpression>\n");
			}
		
			
			buffer.append("</textField>\n\n");	
			
			
			
			
			buffer.append("<textField isStretchWithOverflow=\"true\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >\n");
			buffer.append("<reportElement " + 
					      		"mode=\"" + "Opaque" + "\" " + 
					      		"x=\"" +  x  + "\" " + 
					      		"y=\"" + 0 + "\" " + 
					      		"width=\"" + columnWidth[i] + "\" " + 
					      		"height=\"" + detailHeight + "\" " + 
					      		"forecolor=\"" + getParamValue(PN_DETAIL_ODD_ROW_FORECOLOR, DEFAULT_DETAIL_ODD_ROW_FORECOLOR ) + "\" " + 
						  		"backcolor=\"" + getParamValue(PN_DETAIL_ODD_ROW_BACKCOLOR, DEFAULT_DETAIL_ODD_ROW_BACKCOLOR) + "\" " + 
						  		"key=\"textField\">\n");
			buffer.append("<printWhenExpression><![CDATA[new Boolean(\\$V\\{REPORT_COUNT\\}.intValue() % 2 != 0)]]></printWhenExpression>");
			buffer.append("</reportElement>");
			buffer.append("<textElement " +
								"textAlignment=\"" + (field.getClassType().equalsIgnoreCase("java.lang.String")? "Left": "Right") + "\" " +
								"verticalAlignment=\"Middle\"> " +
								"<font pdfFontName=\"" + getParamValue(PN_ROW_FONT, DEFAULT_ROW_FONT)+ "\" " +
								  "size=\"" + getParamValue(PN_ROW_FONT_SIZE, DEFAULT_ROW_FONT_SIZE)+ "\"/>" +
			  			  "</textElement>\n");
			
			if(field.getClassType().equalsIgnoreCase("java.sql.Date")) {
				buffer.append("<textFieldExpression   " + 
						  "class=\"java.lang.String\"> " + 
						  "<![CDATA[\\$F\\{" + field.getName() + "\\}.toString()]]>\n" +
						  "</textFieldExpression>\n");
			} else {
				buffer.append("<textFieldExpression   " + 
						  "class=\"" + field.getClassType() + "\"> " + 
						  "<![CDATA[\\$F\\{" + field.getName() + "\\}]]>\n" +
						  "</textFieldExpression>\n");
			}
			
			buffer.append("</textField>\n\n\n");	
			
			x += columnWidth[i];		
		}
		
		Iterator it = calculatedFields.iterator();
		CalculatedField cField = null;
		while(it.hasNext()){
			cField = (CalculatedField)it.next();
			buffer.append("<textField isStretchWithOverflow=\"true\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >\n");
			buffer.append("<reportElement " + 
						  "mode=\"" + "Opaque" + "\" " + 
						  "x=\"" + x + "\" " + 
						  "y=\"" + 0 + "\" " + 
						  "width=\"" + (int)(totalWidth/(queryFields.size() + calculatedFields.size())) + "\" " + 
						  "height=\"" + detailHeight + "\" " + 
						  "forecolor=\"" + getParamValue(PN_DETAIL_EVEN_ROW_FORECOLOR, DEFAULT_DETAIL_EVEN_ROW_FORECOLOR ) + "\" " + 
					  		"backcolor=\"" + getParamValue(PN_DETAIL_EVEN_ROW_BACKCOLOR, DEFAULT_DETAIL_EVEN_ROW_BACKCOLOR) + "\" " + 
					  		"key=\"textField\">\n");
			buffer.append("<printWhenExpression><![CDATA[new Boolean(\\$V\\{REPORT_COUNT\\}.intValue() % 2 == 0)]]></printWhenExpression>");
			buffer.append("</reportElement>");
			buffer.append("<textElement " +
					"textAlignment=\"Left\" " +
					"verticalAlignment=\"Middle\"> " +
					"<font pdfFontName=\"" + getParamValue(PN_ROW_FONT, DEFAULT_ROW_FONT)+ "\" " +
					  "size=\"" + getParamValue(PN_ROW_FONT_SIZE, DEFAULT_ROW_FONT_SIZE)+ "\"/>" +
  			  "</textElement>\n");
			buffer.append("<textFieldExpression   " + 
						 "class=\"java.lang.String\"> " + 
						 "<![CDATA[((it.eng.qbe.utility.Scriptlet)\\$P\\{REPORT_SCRIPTLET\\}).executeGroovyScript(\""+cField.getScript()+"\",\""+ cField.getEntityName()+"\",\""+ cField.getClassNameInQuery()+"\",\""+ cField.getFldCompleteNameInQuery()+"\",\""+ cField.getMappings()+"\")]]>\n" +
						  "</textFieldExpression>\n");
			
			buffer.append("</textField>\n\n");	
			
			buffer.append("<textField isStretchWithOverflow=\"true\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >\n");
			buffer.append("<reportElement " + 
					      "mode=\"" + "Opaque" + "\" " + 
						  "x=\"" + x + "\" " + 
						  "y=\"" + 0 + "\" " + 
						  "width=\"" + (int)(totalWidth/(queryFields.size() + calculatedFields.size())) + "\" " + 
						  "height=\"" + detailHeight + "\" " + 
						  "forecolor=\"" + getParamValue(PN_DETAIL_ODD_ROW_FORECOLOR, DEFAULT_DETAIL_ODD_ROW_FORECOLOR ) + "\" " + 
					  		"backcolor=\"" + getParamValue(PN_DETAIL_ODD_ROW_BACKCOLOR, DEFAULT_DETAIL_ODD_ROW_BACKCOLOR) + "\" " + 
					  		"key=\"textField\">\n");
			buffer.append("<printWhenExpression><![CDATA[new Boolean(\\$V\\{REPORT_COUNT\\}.intValue() % 2 != 0)]]></printWhenExpression>");
			buffer.append("</reportElement>");
			buffer.append("<textElement " +
					"textAlignment=\"Left\" " +
					"verticalAlignment=\"Middle\"> " +
					"<font pdfFontName=\"" + getParamValue(PN_ROW_FONT, DEFAULT_ROW_FONT)+ "\" " +
					  "size=\"" + getParamValue(PN_ROW_FONT_SIZE, DEFAULT_ROW_FONT_SIZE)+ "\"/>" +
  			  "</textElement>\n");
			buffer.append("<textFieldExpression   " + 
							"class=\"java.lang.String\"> " + 
							"<![CDATA[((it.eng.qbe.utility.Scriptlet)\\$P\\{REPORT_SCRIPTLET\\}).executeGroovyScript(\""+cField.getScript()+"\",\""+ cField.getEntityName()+"\",\""+ cField.getClassNameInQuery()+"\",\""+ cField.getFldCompleteNameInQuery()+"\",\""+ cField.getMappings()+"\")]]>\n" +
						  "</textFieldExpression>\n");
			buffer.append("</textField>\n\n\n");
			x += (int)(totalWidth/(queryFields.size()+ calculatedFields.size()));
		}
		
		
		buffer.append("</band>");
		buffer.append("</detail>");
		
		
		return buffer.toString();
	}
	
	public String getColumnHeaderBlock(){
		StringBuffer buffer = new StringBuffer();		
		
		int totalWidth = Integer.parseInt(getParamValue(PN_BAND_WIDTH, DEFAULT_BAND_WIDTH ));
		
		buffer.append("<columnHeader>\n");
		buffer.append("<band " + 
					  "height=\"" + getParamValue(PN_HEADER_HEIGHT, DEFAULT_HEADER_HEIGHT) + "\"  " + 
					  "isSplitAllowed=\"true\" >\n");
		
		int[] columnWidth = getColumnWidth(totalWidth);		
		int x = 0;
		
		int i=0;
		for(i = 0; i < queryFields.size(); i++) {
			Field field = (Field)queryFields.get(i);
			buffer.append("<staticText>\n");
			buffer.append("<reportElement " + 
			  		"mode=\"" + "Opaque" + "\" " + 
			  		"x=\"" + x + "\" " + 
			  		"y=\"" + 0 + "\" " + 
			  		"width=\"" + columnWidth[i] + "\" " + 
			  		"height=\"" + getParamValue(PN_HEADER_HEIGHT, DEFAULT_HEADER_HEIGHT ) + "\" " + 
			  		"forecolor=\"" + getParamValue(PN_HEADER_FORECOLOR, DEFAULT_HEADER_FORECOLOR ) + "\" " + 
			  		"backcolor=\"" + getParamValue(PN_HEADER_BACKCOLOR, DEFAULT_HEADER_BACKCOLOR ) + "\" " + 
			  		"key=\"staticText\"/>\n");	

			buffer.append("<box topBorder=\"None\" topBorderColor=\"#000000\" leftBorder=\"None\" leftBorderColor=\"#000000\" rightBorder=\"None\" rightBorderColor=\"#000000\" bottomBorder=\"None\" bottomBorderColor=\"#000000\"/>\n");

			buffer.append("<textElement " +
					"textAlignment=\"" + (field.getClassType().equalsIgnoreCase("java.lang.String")? "Left": "Right") + "\" " +
					"verticalAlignment=\"Middle\"> " +
						"<font pdfFontName=\"" + getParamValue(PN_HEADER_FONT, DEFAULT_HEADER_FONT) + "\" " +
							  "size=\"" + getParamValue(PN_HEADER_FONT_SIZE, DEFAULT_HEADER_FONT_SIZE) + "\" isBold=\"true\"/> " +
			  "</textElement>\n");

			buffer.append("<text><![CDATA[" + 
					(field.getName().equalsIgnoreCase(""+i)?"Field" + field.getName(): field.getName()) + 
			   "]]></text>\n");

			buffer.append("</staticText>\n\n");		

			x += columnWidth[i];	
		}
		
		Iterator it = calculatedFields.iterator();
		CalculatedField cField = null;
		while(it.hasNext()){
			cField = (CalculatedField)it.next();
			buffer.append("<staticText>\n");
			buffer.append("<reportElement " + 
					  "mode=\"" + "Opaque" + "\" " + 
					  "x=\"" + x + "\" " + 
					  "y=\"" + 0 + "\" " + 
					  "width=\"" + (int)(totalWidth/(queryFields.size()+ calculatedFields.size())) + "\" " + 
					  "height=\"" + getParamValue(PN_HEADER_HEIGHT, DEFAULT_HEADER_HEIGHT ) + "\" " + 
				  		"forecolor=\"" + getParamValue(PN_HEADER_FORECOLOR, DEFAULT_HEADER_FORECOLOR ) + "\" " + 
				  		"backcolor=\"" + getParamValue(PN_HEADER_BACKCOLOR, DEFAULT_HEADER_BACKCOLOR ) + "\" " + 
				  		"key=\"staticText\"/>\n");
		
			buffer.append("<box topBorder=\"None\" topBorderColor=\"#000000\" leftBorder=\"None\" leftBorderColor=\"#000000\" rightBorder=\"None\" rightBorderColor=\"#000000\" bottomBorder=\"None\" bottomBorderColor=\"#000000\"/>\n");
		
			buffer.append("<textElement " +
					"textAlignment=\"Left\" " +
					"verticalAlignment=\"Middle\"> " +
						"<font pdfFontName=\"" + getParamValue(PN_HEADER_FONT, DEFAULT_HEADER_FONT) + "\" " +
							  "size=\"" + getParamValue(PN_HEADER_FONT_SIZE, DEFAULT_HEADER_FONT_SIZE) + "\" isBold=\"true\"/> " +
			  "</textElement>\n");
			buffer.append("<text><![CDATA["+cField.getFldLabel()+"]]></text>\n");
			buffer.append("</staticText>\n\n");
			x += (int)(totalWidth/(queryFields.size()+ calculatedFields.size()));
		}
		
		
		buffer.append("</band>");
		buffer.append("</columnHeader>");
		
		return buffer.toString();
	}	
	
	public int[] getColumnWidth(int totalWidth) {
		int[] columnWidthInPixel = new int[queryFields.size()];
		
		int pixelPerChar = Integer.parseInt(getParamValue(PN_PIXEL_PER_CHAR, DEFAULT_PIXEL_PER_CHAR ));
		
		int freePixels = 0;
		int overflowFieldNum = queryFields.size();
		int pixelPerColumn = totalWidth/(queryFields.size()+ calculatedFields.size());
		int remainderPixels = totalWidth%(queryFields.size()+ calculatedFields.size());
		
		for(int i = 0; i < queryFields.size(); i++) {
			int fieldRequiredWidthInPixel = ((Field)queryFields.get(i)).getDisplySize() * pixelPerChar;
			if(fieldRequiredWidthInPixel < pixelPerColumn) {
				columnWidthInPixel[i] = fieldRequiredWidthInPixel;
				freePixels += (pixelPerColumn-fieldRequiredWidthInPixel);
				overflowFieldNum--;
			} else {
				columnWidthInPixel[i] = pixelPerColumn;
			}
			
		}
		
		if(overflowFieldNum > 0 && freePixels > 0) {
			freePixels += remainderPixels;
			pixelPerColumn = freePixels/overflowFieldNum;
			remainderPixels = freePixels%overflowFieldNum;
			for(int i = 0; i < queryFields.size(); i++) {
				int fieldRequiredWidthInPixel = ((Field)queryFields.get(i)).getDisplySize() * pixelPerChar;
				if(fieldRequiredWidthInPixel > columnWidthInPixel[i]) {
					columnWidthInPixel[i] += pixelPerColumn;
					if(fieldRequiredWidthInPixel > columnWidthInPixel[i]) overflowFieldNum--;
					freePixels -= pixelPerColumn;
				}
			}
			freePixels -= remainderPixels;			
		} 
		columnWidthInPixel[queryFields.size()-1] += remainderPixels;
				
		return columnWidthInPixel;
	}
	
	public int getRowHeight(int totalWidth) {
		
		int pixelPerChar = Integer.parseInt(getParamValue(PN_PIXEL_PER_CHAR, DEFAULT_PIXEL_PER_CHAR ));
		int pixelPerRow = Integer.parseInt(getParamValue(PN_PIXEL_PER_ROW, DEFAULT_PIXEL_PER_ROW ));
		
		int rowHeight = pixelPerRow;
		int[] columnWidthInPixel = new int[queryFields.size()];
		
		int freePixels = 0;
		int overflowFieldNum = queryFields.size();
		int pixelPerColumn = totalWidth/(queryFields.size()+ calculatedFields.size());
		int remainderPixels = totalWidth%(queryFields.size()+ calculatedFields.size());
		
		for(int i = 0; i < queryFields.size(); i++) {
			int fieldRequiredWidthInPixel = ((Field)queryFields.get(i)).getDisplySize() * pixelPerChar;
			if(fieldRequiredWidthInPixel < pixelPerColumn) {
				columnWidthInPixel[i] = fieldRequiredWidthInPixel;
				freePixels += (pixelPerColumn-fieldRequiredWidthInPixel);
				overflowFieldNum--;
			} else {
				columnWidthInPixel[i] = pixelPerColumn;
			}
			
		}
		
		int lines = 1;
		if(overflowFieldNum > 0 && freePixels > 0) {
			freePixels += remainderPixels;
			pixelPerColumn = freePixels/overflowFieldNum;
			remainderPixels = freePixels%overflowFieldNum;
			for(int i = 0; i < queryFields.size(); i++) {
				int fieldRequiredWidthInPixel = ((Field)queryFields.get(i)).getDisplySize() * pixelPerChar;
				if(fieldRequiredWidthInPixel > columnWidthInPixel[i]) {
					columnWidthInPixel[i] += pixelPerColumn;
					if(fieldRequiredWidthInPixel < columnWidthInPixel[i]) overflowFieldNum--;
					else {
						int l = fieldRequiredWidthInPixel/columnWidthInPixel[i];
						if(fieldRequiredWidthInPixel%columnWidthInPixel[i] > 0) l += 1;
						if(l > lines) lines = l;
					}
					freePixels -= pixelPerColumn;
				}
			}
			freePixels -= remainderPixels;			
		} 
		columnWidthInPixel[queryFields.size()-1] += remainderPixels;
		
		int maxLinesPerRow = Integer.parseInt(getParamValue(PN_MAXLINE_PER_ROW, DEFAULT_MAXLINE_PER_ROW));
		lines = (lines>maxLinesPerRow)? maxLinesPerRow: lines;
		rowHeight = lines * pixelPerRow;
		
		return (rowHeight);
	}
	
	
	
	
	
	private String getTemplateTemplate() {
		StringBuffer buffer = new StringBuffer();
		
		SourceBean config = (SourceBean)ConfigSingleton.getInstance();		
		SourceBean baseTemplateFileSB = (SourceBean)config.getAttribute("QBE.TEMPLATE-BUILDER.BASE-TEMPLATE");
		String baseTemplateFileStr = null;
		if(baseTemplateFileSB != null) baseTemplateFileStr = baseTemplateFileSB.getCharacters();
		File baseTemplateFile = null;
		if(baseTemplateFileStr != null) baseTemplateFile = new File(baseTemplateFileStr);
		InputStream is = null;
		if(baseTemplateFile!=null && baseTemplateFile.exists()) {
			try {
				is = new FileInputStream(baseTemplateFile);
			} catch (FileNotFoundException e1) {
				is = null;
			}
		}
		
		if(is == null)	is = getClass().getClassLoader().getResourceAsStream("template.jrxml");
		
		BufferedReader reader = new BufferedReader( new InputStreamReader(is) );
		String line = null;
		try {
			while( (line = reader.readLine()) != null) {
				buffer.append(line + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	private String replaceParam(String template, String pname, String pvalue) {
		int index = -1;
		while( (index = template.indexOf("${" + pname + "}")) != -1) {
			template = template.replaceAll("\\$\\{" + pname + "\\}", pvalue);
		}
		
		return template;
	}
	
	public void fillCalculatedFields(String savedQueryObjectID) {
		try{
			
			ISingleDataMartWizardObject aWizardObject = deserializeQueryObject(savedQueryObjectID);
			this.calculatedFields = new ArrayList();
			
			List queryManuallyCalculatedFields = aWizardObject.getSelectClause().getCalcuatedFields();
			CalculatedField cField = null;
			for (Iterator it = queryManuallyCalculatedFields.iterator(); it.hasNext();){
				cField = (CalculatedField)it.next();
				if (cField.getInExport().equalsIgnoreCase("true"))
					this.calculatedFields.add(cField);
			}
			List queryAutoFields = Utils.getCalculatedFields(this.extractedEntitiesList,this.formulaFilePath);
			cField = null;
			for (Iterator it = queryAutoFields.iterator(); it.hasNext();){
				cField = (CalculatedField)it.next();
				if (cField.getInExport().equalsIgnoreCase("true"))
					this.calculatedFields.add(cField);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private ISingleDataMartWizardObject deserializeQueryObject(String savedQueryObjectID){
		
        
        String fileName = formulaFilePath + System.getProperty("file.separator") + savedQueryObjectID+ ".qbe";
        File f = null;
        FileInputStream fis = null; 
        
        try {
            f = new File(fileName);
            fis = new FileInputStream(f);
        	XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(fis));
        
            
            ISingleDataMartWizardObject o = (ISingleDataMartWizardObject)decoder.readObject();
            decoder.close();
            
            return o;
        } catch (FileNotFoundException e) {
    		Logger.error(LocalFileSystemQueryPersister.class, e);
    		return null;
        }finally{
        	try{
        		fis.close();
        	}catch (Exception e) {
				
			}
        	f.delete();
        }
       
	}
}
