package it.eng.spagobi.kpi.utils;




/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.engines.kpi.bo.KpiLine;
import it.eng.spagobi.engines.kpi.bo.KpiResourceBlock;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.BulletGraph;
import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.model.bo.Resource;
import it.eng.spagobi.kpi.threshold.bo.Threshold;
import it.eng.spagobi.kpi.threshold.bo.ThresholdValue;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;
import org.xml.sax.InputSource;


//TODO: Auto-generated Javadoc
/**
 * The Class BasicTemplateBuilder.
 * 
 * @author Giulio Gavardi
 */
public class BasicTemplateBuilder  {

	private static transient org.apache.log4j.Logger logger=Logger.getLogger(BasicTemplateBuilder.class);


	static String staticTextNameS="<staticText>" +
	"	<reportElement x=\"0\"" +
	"	y=\"0\"" +
	"   width=\"210\"" +
	"   height=\"12\"" +
	"	key=\"staticText-1\" />" +
	"	<box></box>" +
	"	<textElement>	" +
	"	<font size=\"8\" />" +
	"	</textElement>" +
	"	<text><![CDATA[KPI1]]></text>" +
	"	</staticText>";


	static String staticTextNumberS="<staticText>" +
	"	<reportElement" +
	"	x=\"235\"" +
	"	y=\"0\"" +
	"	width=\"35\"" +
	"	height=\"12\"" +
	"	key=\"staticText-2\"/>" +
	"	<box></box>" +
	"	<textElement textAlignment=\"Right\" >" +
	"	<font size=\"8\" isBold=\"true\"/>" +
	"	</textElement>" +
	"	<text></text>" +
	"	</staticText>";
	
	static String staticTextWeightS="<staticText>" +
	"	<reportElement" +
	"	x=\"284\"" +
	"	y=\"0\"" +
	"	width=\"17\"" +
	"	height=\"12\"" +
	"	key=\"staticText-2\"/>" +
	"	<box></box>" +
	"	<textElement textAlignment=\"Right\">" +
	"	<font size=\"6\" isBold=\"false\"/>" +
	"	</textElement>" +
	"	<text></text>" +
	"	</staticText>";

	static String imageS="<image  evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >" +
	"	<reportElement" +
	"	x=\"310\"" +
	"	y=\"35\"" +
	"	width=\"130\"" +
	"	height=\"11\"" +
	"	key=\"image-1\"/>" +
	"	<box></box>" +
	"	<graphicElement stretchType=\"NoStretch\"/>" +
	"	<imageExpression class=\"java.net.URL\"></imageExpression>" +
	"	</image>";
	

	static String resourceBandS="<rectangle>" +
	"	<reportElement" +
	"	x=\"0\"" +
	"	y=\"0\"" +
	"	width=\"535\"" +
	"	height=\"14\"" +
	"	forecolor=\"#FFFFFF\""+
	"	backcolor=\"#5B6C7C\"" +
	"	key=\"rectangle-2\"/>" +
	"	<graphicElement stretchType=\"NoStretch\"/>" +
	"	</rectangle>";


	static String resourceNameS="<staticText>" +
	"	<reportElement" +
	"	x=\"6\"" +
	"	y=\"0\"" +
	"	width=\"120\"" +
	"	height=\"14\"" +
	"	forecolor=\"#FFFFFF\""+
	"	key=\"staticText-3\"/>" +
	"	<box></box>" +
	"	<textElement verticalAlignment=\"Middle\" >" +
	"	<font size=\"10\" isBold=\"true\"/>" +
	" </textElement>" +
	"	<text><![CDATA[risorsa]]></text>" +
	"	</staticText>";
	
	static String columnHeaderBandS="<rectangle>" +
	"	<reportElement" +
	"	x=\"0\"" +
	"	y=\"0\"" +
	"	width=\"535\"" +
	"	height=\"12\"" +
	"	forecolor=\"#FFFFFF\""+
	"	backcolor=\"#DDDDDD\"" +
	"	key=\"rectangle-2\"/>" +
	"	<graphicElement stretchType=\"NoStretch\"/>" +
	"	</rectangle>";
	
	static String columnModelHeaderS="<staticText>"+
	"	<reportElement"+
	"	x=\"6\""+
	"	y=\"15\""+
	"	width=\"93\""+
	"	height=\"12\""+
	"	forecolor=\"#000000\""+
	"	key=\"staticText-4\"/>"+
	"	<box></box>"+
	"	<textElement verticalAlignment=\"Middle\">"+
	"	<font pdfFontName=\"Helvetica-Bold\" size=\"8\" isBold=\"true\"/>"+
	"	</textElement>"+
	"	<text><![CDATA[MODEL]]></text>"+
	"	</staticText>";
	  
	static String columnKPIHeaderS="<staticText>"+
	"	<reportElement"+
	"	x=\"253\""+
	"	y=\"15\""+
	"	width=\"20\""+
	"	height=\"12\""+
	"	forecolor=\"#000000\""+
	"	key=\"staticText-5\"/>"+
	"	<box></box>"+
	"	<textElement verticalAlignment=\"Middle\">"+
	"	<font pdfFontName=\"Helvetica-Bold\" size=\"8\" isBold=\"true\"/>"+
	"	</textElement>"+
	"	<text><![CDATA[KPI]]></text>"+
	"	</staticText>";	
		
	static String columnWeightHeaderS="<staticText>"+
	"	<reportElement"+
	"	x=\"278\""+
	"	y=\"15\""+
	"	width=\"53\""+
	"	height=\"12\""+
	"	forecolor=\"#000000\""+
	"	key=\"staticText-6\"/>"+
	"	<box></box>"+
	"	<textElement verticalAlignment=\"Middle\">"+
	"	<font pdfFontName=\"Helvetica-Bold\" size=\"8\" isBold=\"true\"/>"+
	"	</textElement>"+
	"	<text><![CDATA[WEIGHT]]></text>"+
	"	</staticText>";	

	static String columnThresholdHeaderS="<staticText>"+
	"	<reportElement"+
	"	x=\"436\""+
	"	y=\"15\""+
	"	width=\"97\""+
	"	height=\"12\""+
	"	forecolor=\"#000000\""+
	"	key=\"staticText-7\"/>"+
	"	<box></box>"+
	"	<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">"+
	"	<font pdfFontName=\"Helvetica-Bold\" size=\"8\" isBold=\"true\"/>"+
	"	</textElement>"+
	"	<text><![CDATA[THRESHOLD RANGE]]></text>"+
	"	</staticText>";	

	static String thresholdCodeS="<staticText>"+
	"	<reportElement"+
	"	x=\"446\""+
	"	y=\"15\""+
	"	width=\"72\""+
	"	height=\"8\""+
	"	key=\"staticText-8\"/>"+
	"	<box></box>"+
	"	<textElement textAlignment=\"Right\" verticalAlignment=\"Middle\">"+
	"	<font size=\"6\" />"+
	"	</textElement>"+
	"	<text></text>"+
	"	</staticText>";	
	
	static String thresholdValueS="<staticText>"+
	"	<reportElement"+
	"	x=\"446\""+
	"	y=\"47\""+
	"	width=\"72\""+
	"	height=\"8\""+
	"	key=\"staticText-9\"/>"+
	"	<box></box>"+
	"	<textElement textAlignment=\"Right\" verticalAlignment=\"Middle\">"+
	"	<font size=\"6\" />"+
	"	</textElement>"+
	"	<text></text>"+
	"	</staticText>";	

	static String semaphorS="<rectangle>" +
	"	<reportElement" +
	"	mode=\"Opaque\"" +
	"	x=\"0\"" +
	"	y=\"0\"" +
	"	width=\"7\"" +
	"	height=\"7\"" +
	"	forecolor=\"#FFFFFF\"" +
	"	backcolor=\"#FFFFFF\"" +
	"	key=\"rectangle-1\"/>" +
	"	<graphicElement stretchType=\"NoStretch\"/>" +
	"	</rectangle>";
	
	
	static String oddLineSeparator="<line>"+
	"	<reportElement"+
	"	x=\"0\""+
	"	y=\"103\""+
	"	width=\"535\""+
	"	height=\"0\""+
	"	forecolor=\"#666666\""+
	"	key=\"line-1\"/>"+
	"	<graphicElement stretchType=\"NoStretch\"/>"+
	"	</line>";
	
	static String evenLineSeparator="<line>"+
	"	<reportElement"+
	"	x=\"0\""+
	"	y=\"103\""+
	"	width=\"535\""+
	"	height=\"0\""+
	"	forecolor=\"#990000\""+
	"	key=\"line-1\"/>"+
	"	<graphicElement stretchType=\"NoStretch\"/>"+
	"	</line>";
	
	static String thresholdBandS="<rectangle>" +
	"	<reportElement" +
	"	x=\"0\"" +
	"	y=\"0\"" +
	"	width=\"535\"" +
	"	height=\"14\"" +
	"	forecolor=\"#FFFFFF\""+
	"	backcolor=\"#009999\"" +
	"	key=\"rectangle-2\"/>" +
	"	<graphicElement stretchType=\"NoStretch\"/>" +
	"	</rectangle>";
	
	static String thresholdTitleS="<staticText>" +
	"	<reportElement" +
	"	x=\"6\"" +
	"	y=\"0\"" +
	"	width=\"120\"" +
	"	height=\"14\"" +
	"	forecolor=\"#FFFFFF\""+
	"	key=\"staticText-3\"/>" +
	"	<box></box>" +
	"	<textElement verticalAlignment=\"Middle\" >" +
	"	<font size=\"10\" isBold=\"true\"/>" +
	" </textElement>" +
	"	<text><![CDATA[THRESHOLD DETAILS]]></text>" +
	"	</staticText>";


	static String thresholdTextCodeS="<staticText>" +
	"	<reportElement" +
	"	x=\"0\"" +
	"	y=\"0\"" +
	"	width=\"95\"" +
	"	height=\"12\"" +
	"	forecolor=\"#000000\""+
	"	key=\"staticText-15\"/>" +
	"	<box></box>" +
	"	<textElement verticalAlignment=\"Middle\" textAlignment=\"Left\" >" +
	"	<font size=\"8\" />" +
	"   </textElement>" +
	"	<text></text>" +
	"	</staticText>";

	static String thresholdValuesCodeS="<staticText>" +
	"	<reportElement" +
	"	x=\"0\"" +
	"	y=\"0\"" +
	"	width=\"90\"" +
	"	height=\"12\"" +
	"	key=\"staticText-15\"/>" +
	"	<box></box>" +
	"	<textElement verticalAlignment=\"Middle\" textAlignment=\"Left\" >" +
	"	<font size=\"8\" />" +
	"   </textElement>" +
	"	<text></text>" +
	"	</staticText>";
	
	static String thresholdLineSeparatorS="<line>"+
	"	<reportElement"+
	"	x=\"0\""+
	"	y=\"103\""+
	"	width=\"535\""+
	"	height=\"0\""+
	"	forecolor=\"#000099\""+
	"	key=\"line-1\"/>"+
	"	<graphicElement stretchType=\"NoStretch\"/>"+
	"	</line>";

	SourceBean staticTextName=null;
	SourceBean staticTextNumber=null;
	SourceBean staticTextWeightNumber=null;
	SourceBean image=null;
	SourceBean resourceBand=null;
	SourceBean resourceName=null;
	SourceBean semaphor=null;
	SourceBean oddLineS=null;
	SourceBean evenLineS=null;
	SourceBean columnHeaderBand=null;
	SourceBean columnModelHeader=null;
	SourceBean columnKPIHeader=null;
	SourceBean columnWeightHeader=null;
	SourceBean columnThresholdHeader=null;
	SourceBean thresholdCode=null;
	SourceBean thresholdValue=null;
	
	SourceBean thresholdBand=null;
	SourceBean thresholdTitle=null;
	SourceBean thresholdLineSeparator=null;
	SourceBean thresholdTextCode=null;
	SourceBean thresholdTextValue=null;

	String documentName=null;
	
	List thresholdsList=new ArrayList();

	// margin left of text in summary band
	final Integer xStarter=new Integer(0);
	// indentation value
	final Integer xIncrease=new Integer(5);
	// margin up of text in summary bend
	final Integer yStarter=new Integer(5);
	// Height of the gray band with the resource name
	final Integer resourceBandHeight=new Integer(14); 
	// Height of a value row
	final Integer valueHeight=new Integer(20); 
//	height between lines
	final Integer separatorHeight=new Integer(1);
//	height between lines
	final Integer separatorModelsHeight=new Integer(10);
//	width between elements
	final Integer separatorWidth = new Integer(5);
//	Width of text label with code - name
	final Integer textWidth=new Integer(280);
//	width of text label with numbers
	final Integer numbersWidth=new Integer(50);	
//	width of the semaphor
	final Integer semaphorWidth=new Integer(10);
//	width of the title band
	final Integer titleHeight=new Integer(50);
//	height of the column header band
	final Integer columnHeaderHeight=new Integer(14);
//	height of the column header band
	final Integer thresholdFieldWidth=new Integer(92);
//	height of the column header band
	final Integer thresholdFieldSeparatorWidth=new Integer(10);

	// counting the actual weight of the report
	Integer actualHeight=new Integer(0);

	// Map for the name resolution of upper case tag names
	HashMap<String, String> nameResolution=new HashMap<String, String>();

	List resources;
	InputSource inputSource;

	public SourceBean templateBaseContent=null; 
	public SourceBean summary=null;	
	public SourceBean band=null;




	public BasicTemplateBuilder(String documentName) {
		super();
		this.documentName = documentName;
	}


	/* Build the template
	 * @see it.eng.qbe.export.ITemplateBuilder#buildTemplate()
	 */
	public String buildTemplate(List resources) {
		logger.debug("IN");
		// name resolution for upper cases tag
		nameResolution();

		// Create SOurce Bean of template of template
		String templateStr = getTemplateTemplate();
		templateBaseContent =null;
		String finalTemplate="";
		logger.debug("Recovered template of templates "+templateStr);
		if(templateStr!=null){
			try {
				templateBaseContent = SourceBean.fromXMLString(templateStr);
			} catch (Exception e) {
				logger.error("Error in converting template of template into a SOurce Bean, check the XML code");
			}


		try {
			staticTextName = SourceBean.fromXMLString(staticTextNameS); // this is for text
			staticTextNumber = SourceBean.fromXMLString(staticTextNumberS); 
			staticTextWeightNumber = SourceBean.fromXMLString(staticTextWeightS);
			image = SourceBean.fromXMLString(imageS);
			resourceBand=SourceBean.fromXMLString(resourceBandS);
			resourceName=SourceBean.fromXMLString(resourceNameS);
			semaphor=SourceBean.fromXMLString(semaphorS);
			evenLineS=SourceBean.fromXMLString(evenLineSeparator);
			oddLineS=SourceBean.fromXMLString(oddLineSeparator);
			columnHeaderBand=SourceBean.fromXMLString(columnHeaderBandS);
			columnModelHeader=SourceBean.fromXMLString(columnModelHeaderS);
			columnKPIHeader=SourceBean.fromXMLString(columnKPIHeaderS);
			columnWeightHeader=SourceBean.fromXMLString(columnWeightHeaderS);
			columnThresholdHeader=SourceBean.fromXMLString(columnThresholdHeaderS);
			thresholdCode=SourceBean.fromXMLString(thresholdCodeS);
			thresholdValue=SourceBean.fromXMLString(thresholdValueS);
			
			thresholdBand=SourceBean.fromXMLString(thresholdBandS);
			thresholdTitle=SourceBean.fromXMLString(thresholdTitleS);
			thresholdTextValue=SourceBean.fromXMLString(thresholdValuesCodeS);
			thresholdTextCode=SourceBean.fromXMLString(thresholdTextCodeS);
			thresholdLineSeparator=SourceBean.fromXMLString(thresholdLineSeparatorS);
		} catch (Exception e) {
			logger.error("Error in converting static elemnts into Source Beans, check the XML code");
		}

		//change title
		SourceBean titleSB=(SourceBean)templateBaseContent.getAttribute("title");
		SourceBean titleText=(SourceBean)titleSB.getAttribute("band.staticText.text");
		titleText.setCharacters(documentName);

		// make SUMMARY
		summary=(SourceBean)templateBaseContent.getAttribute("SUMMARY");
		band=(SourceBean)summary.getAttribute("BAND");

		// cycle on resources
		for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
			KpiResourceBlock thisBlock = (KpiResourceBlock) iterator.next();
			newResource(thisBlock);			
		}
		newThresholdBlock();

		increaseHeight();

		finalTemplate=templateBaseContent.toXML();


		for (Iterator iterator = nameResolution.keySet().iterator(); iterator.hasNext();) {
			String toReplace = (String) iterator.next();
			String replaceWith=nameResolution.get(toReplace);
			finalTemplate=finalTemplate.replaceAll("<"+toReplace, "<"+replaceWith);
			finalTemplate=finalTemplate.replaceAll("</"+toReplace, "</"+replaceWith);
			
		}
		logger.debug("Built template: "+finalTemplate);
		//System.out.println(finalTemplate);
		}
		logger.debug("OUT");
		return finalTemplate;
	}



	// set the total height 
	public void increaseHeight(){
		logger.debug("IN");

		try {
			templateBaseContent.setAttribute("pageHeight",actualHeight+titleHeight+70);
			band.setAttribute("height", (actualHeight+5));
		} catch (SourceBeanException e) {
			logger.error("error in setting the height");
			return;
		}
		logger.debug("OUT");


	}


//	Add a resource band
	public void newResource(KpiResourceBlock block){
		logger.debug("IN");
		Resource res=block.getR();
		
			
			try{
				actualHeight+=separatorModelsHeight;

				SourceBean bandRes=new SourceBean(resourceBand);
				SourceBean bandName=new SourceBean(resourceName);
				SourceBean columnHeadBand=new SourceBean(columnHeaderBand);
				SourceBean modelColHeader=new SourceBean(columnModelHeader);
				SourceBean weightColHeader=new SourceBean(columnWeightHeader);
				SourceBean kpiColHeader=new SourceBean(columnKPIHeader);
				SourceBean kthreshColHeader=new SourceBean(columnThresholdHeader);
				
				if(res!=null){
					bandRes.setAttribute("reportElement.y", actualHeight.toString());
					bandName.setAttribute("reportElement.y", actualHeight.toString());
					logger.debug("add resource band for resource "+res.getName());
					SourceBean textValue1=(SourceBean)bandName.getAttribute("text");
					textValue1.setCharacters("RESOURCE: "+res.getName());
					band.setAttribute(bandRes);
					band.setAttribute(bandName);
					actualHeight+=resourceBandHeight;
				}
				
				columnHeadBand.setAttribute("reportElement.y",actualHeight.toString());
				modelColHeader.setAttribute("reportElement.y",actualHeight.toString());
				kpiColHeader.setAttribute("reportElement.y",actualHeight.toString());
				weightColHeader.setAttribute("reportElement.y",actualHeight.toString());
				kthreshColHeader.setAttribute("reportElement.y",actualHeight.toString());
				
				band.setAttribute(columnHeadBand);
				band.setAttribute(modelColHeader);
				band.setAttribute(kpiColHeader);
				band.setAttribute(weightColHeader);
				band.setAttribute(kthreshColHeader);
				
				
				actualHeight+=columnHeaderHeight;
				//The line
				KpiLine lineRoot=block.getRoot();
				newLine(lineRoot, 0,true);
			}
			catch (Exception e) {
				logger.error("Error in setting the resource band",e);
				return;
			}
		
		logger.debug("OUT");
	}


	public void newLine(KpiLine kpiLine, int level,Boolean evenLevel){
		logger.debug("IN");
		try {
			actualHeight+=separatorHeight;
			SourceBean textCodeName=new SourceBean(staticTextName);   // code - name
			SourceBean textValue=new SourceBean(staticTextNumber);  //value number
			SourceBean textWeight=new SourceBean(staticTextWeightNumber);  // weight number
			SourceBean image1=new SourceBean(image);// Bullet Chart
			SourceBean semaphor1=new SourceBean(semaphor);// Semaphore
			SourceBean threshCode=new SourceBean(thresholdCode);// Threshold Code
			SourceBean threshValue=new SourceBean(thresholdValue);// Threshold Value
			SourceBean evenLine=new SourceBean(evenLineS);// Separator for even lines
			SourceBean oddLine=new SourceBean(oddLineS);// Separator for odd lines
			if(evenLevel){
				setLineAttributes(kpiLine,semaphor1,textCodeName,textValue,textWeight,image1,level,evenLine,threshCode,threshValue);
			}else{
				setLineAttributes(kpiLine,semaphor1,textCodeName,textValue,textWeight,image1,level,oddLine,threshCode,threshValue);
			}
			actualHeight+=valueHeight;

			band.setAttribute(semaphor1);
			band.setAttribute(textCodeName);
			band.setAttribute(textValue);
			band.setAttribute(textWeight);
			band.setAttribute(image1);
			band.setAttribute(threshCode);
			band.setAttribute(threshValue);
			if(evenLevel){
				band.setAttribute(evenLine);
			}else{
				band.setAttribute(oddLine);
			}	
			
		} catch (SourceBeanException e) {
			logger.error("error while adding a line");
			return;
		}

		List<KpiLine> children=kpiLine.getChildren();
		if(children!=null){
			for (Iterator iterator = children.iterator(); iterator.hasNext();) {
				KpiLine kpiLineChild = (KpiLine) iterator.next();
				newLine(kpiLineChild, level+1,!evenLevel);
			}}

		logger.debug("OUT");
	}




	private void setLineAttributes(KpiLine line,SourceBean semaphor, SourceBean textCodeName, SourceBean textValue, 
			SourceBean textWeight, SourceBean image1, int level, SourceBean separatorline,SourceBean threshCode,SourceBean threshValue){
		logger.debug("IN");
		Color colorSemaphor=line.getSemaphorColor();
		KpiValue kpiValue=line.getValue();

		Integer xValue=xStarter+(xIncrease*Integer.valueOf(level));

		Integer yValue=actualHeight;

		try {

			//set Semaphor
			semaphor.setAttribute("reportElement.x", xValue.toString());
			semaphor.setAttribute("reportElement.y", new Integer(yValue.intValue()+2).toString());
			if(colorSemaphor!=null){

				String color=Integer.toHexString(colorSemaphor.getRGB());
				color="#"+color.substring(2);

				semaphor.setAttribute("reportElement.forecolor",  "#000000");
				semaphor.setAttribute("reportElement.backcolor", color);
			}else{
				semaphor.setAttribute("reportElement.forecolor", "#FFFFFF");
				semaphor.setAttribute("reportElement.backcolor", "#FFFFFF");
			}
			xValue=xValue+semaphorWidth+separatorWidth;

			// set text 1: Model CODE - Model NAME
			textCodeName.setAttribute("reportElement.x", (xValue));
			textCodeName.setAttribute("reportElement.y", yValue.toString());
			SourceBean textValue1=(SourceBean)textCodeName.getAttribute("text");
			textValue1.setCharacters(line.getModelInstanceCode()+"-"+line.getModelNodeName());

			xValue=xValue+textWidth+separatorWidth;

			//Set Value, weight and threshold code and value
			if(kpiValue!=null){
				String value1=kpiValue.getValue() != null ? kpiValue.getValue() : "";
				//set text2
				textValue.setAttribute("reportElement.y", yValue.toString());
				SourceBean textValue2=(SourceBean)textValue.getAttribute("text");
				textValue2.setCharacters(value1);

				String weight=(kpiValue.getWeight()!=null) ? kpiValue.getWeight().toString() : "";
				//set text2
				xValue=xValue+numbersWidth+separatorWidth;
				textWeight.setAttribute("reportElement.y", new Integer(yValue.intValue()+2).toString());
				SourceBean textValue3=(SourceBean)textWeight.getAttribute("text");
				textValue3.setCharacters(weight);
				
				ThresholdValue t = line.getThresholdOfValue();
				if(t!=null){
					try {
						Threshold tr = DAOFactory.getThresholdDAO().loadThresholdById(t.getThresholdId());
						if (!thresholdsList.contains(tr)){
							thresholdsList.add(tr);
						}
						
					} catch (EMFUserError e) {
						logger.error("error in loading the Threshold by Id",e);
						e.printStackTrace();
					}
					String code=t.getThresholdCode() != null ? t.getThresholdCode() : "";
					String codeTh = "Code: "+code;
					
					threshCode.setAttribute("reportElement.y",  new Integer(yValue.intValue()-2).toString());
					SourceBean threshCode2=(SourceBean)threshCode.getAttribute("text");
					threshCode2.setCharacters(codeTh);
				
				
					String labelTh=t.getLabel() != null ? t.getLabel() : "";
					String min = t.getMinValue()!= null ? t.getMinValue().toString() : "";
					String max = t.getMaxValue()!= null ?  t.getMaxValue().toString() : "";
					String valueTh = "Value: "+min+"-"+max+" "+labelTh;
					
					threshValue.setAttribute("reportElement.y", new Integer(yValue.intValue()+7).toString());
					SourceBean threshValue2=(SourceBean)threshValue.getAttribute("text");
					threshValue2.setCharacters(valueTh);
				}

			}
			//Sets the bullet chart
			if(line.getChartBullet()!=null){
				BulletGraph sbi = (BulletGraph)line.getChartBullet();	
				JFreeChart chart = sbi.createChart();
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				String requestIdentity = null;
				UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
				UUID uuid = uuidGen.generateTimeBasedUUID();
				requestIdentity = uuid.toString();
				requestIdentity = requestIdentity.replaceAll("-", "");
				String path_param = requestIdentity;
				String dir=System.getProperty("java.io.tmpdir");
				String path=dir+"/"+requestIdentity+".png";
				java.io.File file1 = new java.io.File(path);
				try {
					ChartUtilities.saveChartAsPNG(file1, chart, 130, 11, info);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String urlPng=GeneralUtilities.getSpagoBiHost()+GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + 
				"?ACTION_NAME=GET_PNG2&NEW_SESSION=TRUE&path="+path_param+"&LIGHT_NAVIGATOR_DISABLED=TRUE";
				urlPng = "new java.net.URL(\""+urlPng+"\")";
				
				image1.setAttribute("reportElement.y", yValue.toString());
				SourceBean imageValue=(SourceBean)image1.getAttribute("imageExpression");
				imageValue.setCharacters(urlPng);
				
			}
			
			separatorline.setAttribute("reportElement.y", new Integer(yValue.intValue()+16).toString());


		} catch (SourceBeanException e) {
			logger.error("error in drawing the line",e);
			e.printStackTrace();
		}
		logger.debug("OUT");
	}

	public void newThresholdBlock(){
		logger.debug("IN");

			try{//Draws the Threshold Band and Title
				actualHeight+=separatorModelsHeight;
				SourceBean thresholdBand1=new SourceBean(thresholdBand);  
				SourceBean thresholdTitle1=new SourceBean(thresholdTitle); 
				
				thresholdBand1.setAttribute("reportElement.y",actualHeight.toString());
				thresholdTitle1.setAttribute("reportElement.y",actualHeight.toString());	
				
				band.setAttribute(thresholdBand1);
				band.setAttribute(thresholdTitle1);
				
				actualHeight+=columnHeaderHeight;
				
				if(thresholdsList!=null && !thresholdsList.isEmpty()){
					Iterator th = thresholdsList.iterator();
					while(th.hasNext()){
						Threshold t =(Threshold)th.next();
						if (t!=null){
							actualHeight+=separatorHeight;	
							Integer yValue=actualHeight;
							Integer xValue = thresholdFieldWidth;
							//Draws the Threshold Code
							SourceBean thresholdTextCode1=new SourceBean(thresholdTextCode);  
							String code=t.getCode() != null ? t.getCode() : "";
							String codeTh = "Code: "+code;
	
							thresholdTextCode1.setAttribute("reportElement.y", yValue.toString());
							SourceBean threshCode2=(SourceBean)thresholdTextCode1.getAttribute("text");
							threshCode2.setCharacters(codeTh);
							
							band.setAttribute(thresholdTextCode1);
							
							newThresholdLine(t);
							//Adds a separator line
							SourceBean thresholdLineSeparator1=new SourceBean(thresholdLineSeparator); 
							thresholdLineSeparator1.setAttribute("reportElement.y",new Integer(yValue.intValue()+16).toString());
							band.setAttribute(thresholdLineSeparator1);	
						}
					}
				
				}
			}	
			catch (Exception e) {
				logger.error("Error in setting the resource band");
				return;
			}
		
		logger.debug("OUT");
	}
	
	public void newThresholdLine(Threshold t){
		logger.debug("IN");
		try {
			actualHeight+=separatorHeight;	
			Integer yValue=actualHeight;
			Integer xValue = new Integer(5);
			List thValues = t.getThresholdValues();
			if(thValues!=null && !thValues.isEmpty()){
				Iterator thIt = thValues.iterator();
				while(thIt.hasNext()){
					ThresholdValue val = (ThresholdValue)thIt.next();
					if (val!=null){
						SourceBean semaphor1=new SourceBean(semaphor);
						SourceBean thresholdTextValue1=new SourceBean(thresholdTextValue);
						
						//Semaphore Threshold creation
						xValue = xValue + thresholdFieldWidth;
						
						String colorSemaphor = val.getColourString();
						semaphor1.setAttribute("reportElement.x", xValue.toString());
						semaphor1.setAttribute("reportElement.y", new Integer(yValue.intValue()+2).toString());
						if(colorSemaphor!=null){
							semaphor1.setAttribute("reportElement.forecolor",  "#000000");
							semaphor1.setAttribute("reportElement.backcolor", colorSemaphor);
						}else{
							semaphor1.setAttribute("reportElement.forecolor", "#FFFFFF");
							semaphor1.setAttribute("reportElement.backcolor", "#FFFFFF");
						}
						
						//Threshold Value Creation
						xValue = xValue + thresholdFieldSeparatorWidth;
						
						String labelTh=val.getLabel() != null ? val.getLabel() : "";
						String min = val.getMinValue()!= null ? val.getMinValue().toString() : "";
						String max = val.getMaxValue()!= null ?  val.getMaxValue().toString() : "";
						String valueTh = "Value: "+min+"-"+max+" "+labelTh;

						thresholdTextValue1.setAttribute("reportElement.x", xValue.toString());
						thresholdTextValue1.setAttribute("reportElement.y", yValue.toString());
						SourceBean threshValue2=(SourceBean)thresholdTextValue1.getAttribute("text");
						threshValue2.setCharacters(valueTh);
						
						band.setAttribute(semaphor1);	
						band.setAttribute(thresholdTextValue1);	
					}
				}
			}

			actualHeight+=valueHeight;
			
		} catch (SourceBeanException e) {
			logger.error("error while adding a threshold line");
			return;
		}

		logger.debug("OUT");
	}


	private void nameResolution(){

//		property
//		import
//		queryString
//		field
//		variable
//		background
//		band
//		title
//		line
//		reportElement
//		graphicElement
//		textField
//		box
//		textElement
//		font
//		textFieldExpression
//		pageHeader
//		columnHeader
//		detail
//		columnFooter
//		pageFooter
//		summary
//		staticText
//		text
//		image
//		imageExpression
		nameResolution.put("IMAGEEXPRESSION", "imageExpression");
		nameResolution.put("imageEXPRESSION", "imageExpression");
		nameResolution.put("JASPERREPORT", "jasperReport");
		nameResolution.put("IMPORT", "import");
		nameResolution.put("PROPERTY", "property");
		nameResolution.put("QUERYSTRING", "queryString");
		nameResolution.put("FIELD", "field");
		nameResolution.put("VARIABLE", "variable");
		nameResolution.put("BACKGROUND", "background");
		nameResolution.put("BAND", "band");
		nameResolution.put("TITLE", "title");
		nameResolution.put("LINE", "line");
		nameResolution.put("REPORTELEMENT", "reportElement");
		nameResolution.put("GRAPHICELEMENT", "graphicElement");
		nameResolution.put("TEXTFIELD", "textField");
		nameResolution.put("BOX", "box");
		nameResolution.put("TEXTELEMENT", "textElement");
		nameResolution.put("textELEMENT", "textElement");
		nameResolution.put("FONT", "font");
		nameResolution.put("TEXTFIELDEXPRESSION", "textFieldExpression");
		nameResolution.put("PAGEHEADER", "pageHeader");
		nameResolution.put("COULMNHEADER", "columnHeader");
		nameResolution.put("DETAIL", "detail");
		nameResolution.put("COLUMNFOOTER", "columnFooter");
		nameResolution.put("PAGEFOOTER", "pageFooter");
		nameResolution.put("SUMMARY", "summary");
		nameResolution.put("STATICTEXT", "staticText");
		nameResolution.put("TEXT", "text");
		nameResolution.put("IMAGE", "image");	
		nameResolution.put("RECTANGLE", "rectangle");
		nameResolution.put("INITIALVALUEEXPRESSION", "initialValueExpression");
		nameResolution.put("COLUMNHEADER", "columnHeader");
	}




	/**
	 * Gets the template template.
	 * 
	 * @return the template template
	 */
	public String getTemplateTemplate() {
		StringBuffer buffer = new StringBuffer();
		logger.debug("IN");
		try{
			// Used to test
			//File file = new File("D:/progetti/spagobi/eclipse2/SpagoBIProject/src/it/eng/spagobi/kpi/utils/templateKpi.jrxml");

			String rootPath=ConfigSingleton.getRootPath();
			logger.debug("rootPath: "+rootPath!=null ? rootPath : "");
			String templateDirPath=rootPath+"/WEB-INF/classes/it/eng/spagobi/kpi/utils/";
			logger.debug("templateDirPath: "+templateDirPath!=null ? templateDirPath : "");
			templateDirPath+="templateKpi.jrxml";
			logger.debug("templatePath: "+templateDirPath!=null ? templateDirPath : "");
			if (templateDirPath!=null){
				File file=new File(templateDirPath);
				if(file!=null){
					logger.debug("File found ");
				}
				FileInputStream fis=new FileInputStream(file);
				if(fis!=null){
					logger.debug("File Input Stream created");
				}
				inputSource=new InputSource(fis);
				if(inputSource!=null){
					logger.debug("Input Source created");
				}
				BufferedReader reader = new BufferedReader( new InputStreamReader(fis) );
				if(reader!=null){
					logger.debug("Buffer Reader created");
				}
				String line = null;
				try {
					while( (line = reader.readLine()) != null) {
						buffer.append(line + "\n");
					}
				} catch (IOException e) {
					logger.error("error in appending lines to the buffer",e);
					e.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			logger.error("error in retrieving the template",e);
			e.printStackTrace();
			return null;
		}
		logger.debug("OUT");
		return buffer.toString();
	}


	/**
	 * Replace param.
	 * 
	 * @param template the template
	 * @param pname the pname
	 * @param pvalue the pvalue
	 * 
	 * @return the string
	 */
	private String replaceParam(String template, String pname, String pvalue) {
		logger.debug("IN");
		int index = -1;
		while( (index = template.indexOf("${" + pname + "}")) != -1) {
			template = template.replaceAll("\\$\\{" + pname + "\\}", pvalue);
		}
		logger.debug("OUT");
		return template;
	}


//	/**
//	* Fill calculated fields.
//	* 
//	* @param savedQueryObjectID the saved query object id
//	*/
//	public void fillCalculatedFields(String savedQueryObjectID) {

//	}

//	/**
//	* Deserialize query object.
//	* 
//	* @param savedQueryObjectID the saved query object id
//	* 
//	* @return the i single data mart wizard object
//	*/
//	private Object deserializeQueryObject(String savedQueryObjectID){        
//	String fileName = null; //formulaFile.getParent() + System.getProperty("file.separator") + savedQueryObjectID+ ".qbe";
//	File f = null;
//	FileInputStream fis = null; 

//	try {
//	f = new File(fileName);
//	fis = new FileInputStream(f);
//	XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(fis));


//	Object o = (Object)decoder.readObject();
//	decoder.close();

//	return o;
//	} catch (FileNotFoundException e) {
//	//Logger.error(LocalFileSystemQueryPersister.class, e);
//	return null;
//	}finally{
//	try{
//	fis.close();
//	}catch (Exception e) {

//	}
//	f.delete();
//	}

//	}




}
