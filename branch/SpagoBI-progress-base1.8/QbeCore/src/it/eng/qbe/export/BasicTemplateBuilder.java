/**
 * 
 */
package it.eng.qbe.export;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Vector;

/**
 * @author Gioia
 *
 */
public class BasicTemplateBuilder extends AbstractTemplateBuilder {
	
	String query;
	String queryLanguage;
	Vector queryFields;
	Map params;
	
	public static final String SQL_LANGUAGE = "sql";
	public static final String HQL_LANGUAGE = "hql";
	
	
	public BasicTemplateBuilder(String query, String queryLanguage, Vector queryFields, Map params) {
		this.query = query;
		this.queryLanguage = queryLanguage;
		this.queryFields = queryFields;
		this.params = params;
	}
	
	public String buildTemplate() {
		String templateStr = getTemplateTemplate();
		
		templateStr = replaceParam(templateStr, "lang", queryLanguage);
		templateStr = replaceParam(templateStr, "query", query);
		templateStr = replaceParam(templateStr, "fields", getFieldsBlock());
		templateStr = replaceParam(templateStr, "body", getColumnHeaderBlock() + getDetailsBlock());
		return templateStr;
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
		
		buffer.append("<detail>\n");
		buffer.append("<band " + 
					  "height=\"" + DETAIL_HEIGHT + "\"  " + 
					  "isSplitAllowed=\"true\" >\n");
		
		
		for(int i = 0; i < queryFields.size(); i++) {
			Field field = (Field)queryFields.get(i);
			buffer.append("<textField isStretchWithOverflow=\"false\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >\n");
			buffer.append("<reportElement " + 
						  "mode=\"" + "Opaque" + "\" " + 
						  "x=\"" + i*(int)(DETAIL_WIDTH/queryFields.size()) + "\" " + 
						  "y=\"" + 0 + "\" " + 
						  "width=\"" + (int)(DETAIL_WIDTH/queryFields.size()) + "\" " + 
						  "height=\"" + DETAIL_HEIGHT + "\" " + 
						  "forecolor=\"" + params.get("evenRowsForegroundColor") /*"#000000"*/ + "\" " + 
						  "backcolor=\"" + params.get("evenRowsBackgroundColor")/*"#E1E1FC"*/ + "\" " + 
						  "key=\"textField\">\n");
			buffer.append("<printWhenExpression><![CDATA[new Boolean(\\$V\\{REPORT_COUNT\\}.intValue() % 2 == 0)]]></printWhenExpression>");
			buffer.append("</reportElement>");
			buffer.append("<textElement verticalAlignment=\"Middle\"> <font/> </textElement>\n");
			buffer.append("<textFieldExpression   " + 
						  "class=\"" + field.getClassType() + "\"> " + 
						  "<![CDATA[\\$F\\{" + field.getName() + "\\}]]>\n" +
						  "</textFieldExpression>\n");
			
			buffer.append("</textField>\n\n");	
			
			buffer.append("<textField isStretchWithOverflow=\"false\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >\n");
			buffer.append("<reportElement " + 
					      "mode=\"" + "Opaque" + "\" " + 
						  "x=\"" + i*(int)(DETAIL_WIDTH/queryFields.size()) + "\" " + 
						  "y=\"" + 0 + "\" " + 
						  "width=\"" + (int)(DETAIL_WIDTH/queryFields.size()) + "\" " + 
						  "height=\"" + DETAIL_HEIGHT + "\" " + 
						  "forecolor=\"" + params.get("oddRowsForegroundColor") /*"#000000"*/ + "\" " + 
						  "backcolor=\"" + params.get("oddRowsBackgroundColor") /*"#FFFFFF"*/ + "\" " + 
						  "key=\"textField\">\n");
			buffer.append("<printWhenExpression><![CDATA[new Boolean(\\$V\\{REPORT_COUNT\\}.intValue() % 2 != 0)]]></printWhenExpression>");
			buffer.append("</reportElement>");
			buffer.append("<textElement verticalAlignment=\"Middle\"> <font/> </textElement>\n");
			buffer.append("<textFieldExpression   " + 
						  "class=\"" + field.getClassType() + "\"> " + 
						  "<![CDATA[\\$F\\{" + field.getName() + "\\}]]>\n" +
						  "</textFieldExpression>\n");
			
			buffer.append("</textField>\n\n\n");		
		}
		
		buffer.append("</band>");
		buffer.append("</detail>");
		
		return buffer.toString();
	}
	
	public String getColumnHeaderBlock(){
		StringBuffer buffer = new StringBuffer();		
			
		buffer.append("<columnHeader>\n");
		buffer.append("<band " + 
					  "height=\"" + (DETAIL_HEIGHT + 20) + "\"  " + 
					  "isSplitAllowed=\"true\" >\n");
		
		for(int i = 0; i < queryFields.size(); i++) {
			Field field = (Field)queryFields.get(i);
			buffer.append("<staticText>\n");
			buffer.append("<reportElement " + 
						  "mode=\"" + "Opaque" + "\" " + 
						  "x=\"" + i*(int)(DETAIL_WIDTH/queryFields.size()) + "\" " + 
						  "y=\"" + 0 + "\" " + 
						  "width=\"" + (int)(DETAIL_WIDTH/queryFields.size()) + "\" " + 
						  "height=\"" + (DETAIL_HEIGHT + 20) + "\" " + 
						  "forecolor=\"" + params.get("columnHeaderForegroundColor") /*FFFFFF"*/ + "\" " + 
						  "backcolor=\"" + params.get("columnHeaderBackgroundColor") /*"#006666"*/ + "\" " + 
						  "key=\"staticText\"/>\n");	
			
			buffer.append("<box topBorder=\"None\" topBorderColor=\"#000000\" leftBorder=\"None\" leftBorderColor=\"#000000\" rightBorder=\"None\" rightBorderColor=\"#000000\" bottomBorder=\"None\" bottomBorderColor=\"#000000\"/>\n");
			
			buffer.append("<textElement verticalAlignment=\"Middle\"> <font pdfFontName=\"Helvetica-Bold\" size=\"12\" isBold=\"true\"/> </textElement>\n");
			buffer.append("<text><![CDATA[" + (field.getName().equalsIgnoreCase(""+i)?"Field" + field.getName(): field.getName()) + "]]></text>\n");
			
			buffer.append("</staticText>\n\n");		
		}
		
		buffer.append("</band>");
		buffer.append("</columnHeader>");
		
		return buffer.toString();
	}	
	
	private String getTemplateTemplate() {
		StringBuffer buffer = new StringBuffer();
		
		InputStream is = getClass().getClassLoader().getResourceAsStream("template.jrxml");
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
}
