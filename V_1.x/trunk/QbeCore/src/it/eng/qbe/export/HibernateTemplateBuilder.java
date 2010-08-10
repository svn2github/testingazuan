/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.qbe.export;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.hibernate.SessionFactory;

/**
 * @author Gioia
 *
 */
public class HibernateTemplateBuilder extends AbstractTemplateBuilder {
	
	private String query;
	private SessionFactory sessionFactory;

	public HibernateTemplateBuilder(String query, SessionFactory sessionFactory) {
		this.query = query;
		this.sessionFactory = sessionFactory;
	}
	
	public String buildTemplate() {
		String templateStr = getTemplateTemplate();
		templateStr = replaceParam(templateStr, "query", query);
		templateStr = replaceParam(templateStr, "fields", getFieldsBlock());
		templateStr = replaceParam(templateStr, "body", getColumnHeaderBlock() + getDetailsBlock());
		return templateStr;
	}
	
	public String getFieldsBlock() {
		StringBuffer buffer = new StringBuffer();
		
		IFieldsReader fieldsReader = new HQLFieldsReader(query, sessionFactory);
		Vector fields = null;
		try {
			fields = fieldsReader.readFields();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		for(int i = 0; i < fields.size(); i++) {
			Field field = (Field)fields.get(i);
			buffer.append("<field name=\"" + field.getName() + "\" class=\"" + field.getClassType() + "\"/>\n");
		}
		return buffer.toString();
	}
	
	public static final int DETAIL_HEIGHT = 20;
	public static final int DETAIL_WIDTH = 530;
	
	public String getDetailsBlock() {
		StringBuffer buffer = new StringBuffer();
		
		IFieldsReader fieldsReader = new HQLFieldsReader(query, sessionFactory);
		Vector fields = null;
		try {
			fields = fieldsReader.readFields();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		buffer.append("<detail>\n");
		buffer.append("<band " + 
					  "height=\"" + DETAIL_HEIGHT + "\"  " + 
					  "isSplitAllowed=\"true\" >\n");
		
		
		for(int i = 0; i < fields.size(); i++) {
			Field field = (Field)fields.get(i);
			buffer.append("<textField isStretchWithOverflow=\"false\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >\n");
			buffer.append("<reportElement " + 
						  "mode=\"" + "Opaque" + "\" " + 
						  "x=\"" + i*(int)(DETAIL_WIDTH/fields.size()) + "\" " + 
						  "y=\"" + 0 + "\" " + 
						  "width=\"" + (int)(DETAIL_WIDTH/fields.size()) + "\" " + 
						  "height=\"" + DETAIL_HEIGHT + "\" " + 
						  "forecolor=\"" + "#000000" + "\" " + 
						  "backcolor=\"" + "#CCFFCC" + "\" " + 
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
						  "x=\"" + i*(int)(DETAIL_WIDTH/fields.size()) + "\" " + 
						  "y=\"" + 0 + "\" " + 
						  "width=\"" + (int)(DETAIL_WIDTH/fields.size()) + "\" " + 
						  "height=\"" + DETAIL_HEIGHT + "\" " + 
						  "forecolor=\"" + "#000000" + "\" " + 
						  "backcolor=\"" + "#FFFFCC" + "\" " + 
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
		
		IFieldsReader fieldsReader = new HQLFieldsReader(query, sessionFactory);
		Vector fields = null;
		try {
			fields = fieldsReader.readFields();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		buffer.append("<columnHeader>\n");
		buffer.append("<band " + 
					  "height=\"" + (DETAIL_HEIGHT + 20) + "\"  " + 
					  "isSplitAllowed=\"true\" >\n");
		
		for(int i = 0; i < fields.size(); i++) {
			Field field = (Field)fields.get(i);
			buffer.append("<staticText>\n");
			buffer.append("<reportElement " + 
						  "mode=\"" + "Opaque" + "\" " + 
						  "x=\"" + i*(int)(DETAIL_WIDTH/fields.size()) + "\" " + 
						  "y=\"" + 0 + "\" " + 
						  "width=\"" + (int)(DETAIL_WIDTH/fields.size()) + "\" " + 
						  "height=\"" + (DETAIL_HEIGHT + 20) + "\" " + 
						  "forecolor=\"" + "#FFFFFF" + "\" " + 
						  "backcolor=\"" + "#006666" + "\" " + 
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
