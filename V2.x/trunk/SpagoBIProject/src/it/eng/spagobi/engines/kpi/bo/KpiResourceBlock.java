package it.eng.spagobi.engines.kpi.bo;

import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.urls.IUrlBuilder;
import it.eng.spagobi.commons.utilities.urls.UrlBuilderFactory;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.BulletGraph;
import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.model.bo.Resource;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

public class KpiResourceBlock {
	
	Resource r = null;
	KpiLine root = null;
	
	public KpiResourceBlock(Resource r, KpiLine root) {
		super();
		this.r = r;
		this.root = root;
	}
	
	public KpiResourceBlock() {
		super();
	}

	public Resource getR() {
		return r;
	}
	public void setR(Resource r) {
		this.r = r;
	}
	public KpiLine getRoot() {
		return root;
	}
	public void setRoot(KpiLine root) {
		this.root = root;
	}
	
	public StringBuffer makeTree(HttpServletRequest httpReq, Boolean display_bullet_chart, Boolean display_alarm, Boolean display_semaphore, Boolean display_weight ){
		
		StringBuffer _htmlStream = new StringBuffer();		
		
		if (r!=null){
			_htmlStream.append("<table align=\"left\">\n");
			_htmlStream.append(" <tr class='kpi_resource_section'><td><div>Resource: "+r.getName()+"</div></td></tr>\n");
			_htmlStream.append("</table>\n");
		}
		
		addItemForTree(0,false,httpReq, root,_htmlStream,display_bullet_chart,display_alarm,display_semaphore,display_weight);
		
		 _htmlStream.append("<br>\n");
		return _htmlStream;
	}
	
	private StringBuffer addItemForTree(int recursionLev, Boolean evenLine,HttpServletRequest httpReq,KpiLine line, StringBuffer _htmlStream,Boolean display_bullet_chart, Boolean display_alarm, Boolean display_semaphore, Boolean display_weight) {
		
		HttpServletRequest httpRequest = httpReq;
		IUrlBuilder urlBuilder = UrlBuilderFactory.getUrlBuilder();
		String alarmImgSrc = urlBuilder.getResourceLink(httpRequest, "/img/kpi/alarm.jpg");
		String modelName = line.getModelNodeName();
		Boolean alarm = line.getAlarm();
		KpiValue kpiVal = line.getValue();
		String value = kpiVal.getValue();
		Double val = new Double(value);
		float lo =  val.floatValue();
		Double weight = kpiVal.getWeight();
		Color semaphorColor = line.getSemaphorColor();
		
		BulletGraph sbi = (BulletGraph)line.getChartBullet();		
		List children = line.getChildren();
		_htmlStream.append("<div width='"+recursionLev*2+"%'></div>\n");
		
		_htmlStream.append("<table align=\"left\">\n");
		if(evenLine){
			 _htmlStream.append("	<tr class='kpi_line_section_even'>\n");
		}else{
			_htmlStream.append("	<tr class='kpi_line_section_odd'>\n");
		}
		
		_htmlStream.append("		<td width='20%' style='height=30px;align:left;vertical-align:middle;'><div>"+modelName+"</div></td>\n");
		if (display_alarm){
			if(alarm) _htmlStream.append("		<td width='5%' style=\"align:left;vertical-align:middle;\"><div><img src=\""+alarmImgSrc+"\" alt=\"Kpi under Alarm Control\" /></div></td>\n");
			else _htmlStream.append("		<td width='5%' style=\"align:left;vertical-align:middle;\"><div>&nbsp; &nbsp;</div></td>\n");
		}else{
			_htmlStream.append("		<td width='5%' style=\"align:left;vertical-align:middle;\"><div>&nbsp; &nbsp;</div></td>\n");
		}
		_htmlStream.append("		<td  width='15%' style=\"align:left;vertical-align:middle;\"><div>"+lo+"</div></td>\n");
		if (display_weight && weight!=null){
			_htmlStream.append("		<td width='15%' style=\"align:left;vertical-align:middle;\"><div>Weight:"+weight.toString()+"</div></td>\n");
		}else{
			_htmlStream.append("		<td width='15%' style=\"align:left;vertical-align:middle;\"><div>&nbsp; &nbsp;</div></td>\n");
		}
		if (display_semaphore && semaphorColor!= null){
			String semaphorHex = Integer.toHexString( semaphorColor.getRGB() & 0x00ffffff ) ;		
			_htmlStream.append("		<td width='5%' style=\"align:left;vertical-align:middle;\"><div style=\"width:20px;height:20px;border: 1px solid #bbb;border-color: black;background-color:#"+semaphorHex+"\"></div></td>\n");
		}else{
			_htmlStream.append("		<td width='5%' style=\"align:left;vertical-align:middle;\"><div>&nbsp; &nbsp;</div></td>\n");
		}
		if (display_bullet_chart && sbi!=null){
			
			String requestIdentity = null;
			UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
			UUID uuid = uuidGen.generateTimeBasedUUID();
			requestIdentity = uuid.toString();
			requestIdentity = requestIdentity.replaceAll("-", "");
			JFreeChart chart = sbi.createChart();
			ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
			String dir=System.getProperty("java.io.tmpdir");
			String path=dir+"/"+requestIdentity+".png";
			java.io.File file1 = new java.io.File(path);
			try {
				ChartUtilities.saveChartAsPNG(file1, chart, 250, 20, info);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String urlPng=GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + 
			"?ACTION_NAME=GET_PNG2&NEW_SESSION=TRUE&userid=<%=userId%>&path="+path+"&LIGHT_NAVIGATOR_DISABLED=TRUE";
			_htmlStream.append("		<td width='30%' style=\"align:left;vertical-align:middle;\"><div><img style=\"align:left;vertical-align:middle;\" id=\"image\" src=\""+urlPng+"\" BORDER=\"1\" alt=\"Error in displaying the chart\" USEMAP=\"#chart\"/></div></td>\n");
			
		}else{
			_htmlStream.append("		<td width='30%' style=\"align:left;vertical-align:middle;\"><div>&nbsp; &nbsp;</div></td>\n");
		}
	   _htmlStream.append("	</tr>\n");
	   _htmlStream.append("</table>\n");
	   if (!children.isEmpty()){
		   Iterator childIt = children.iterator();
		   while (childIt.hasNext()){
			   KpiLine l = (KpiLine)childIt.next();
			   recursionLev ++;
			   if (evenLine){			   
				   addItemForTree(recursionLev,false,httpReq, l,_htmlStream,display_bullet_chart,display_alarm,display_semaphore,display_weight);
			   }else{
				   addItemForTree(recursionLev,true,httpReq, l,_htmlStream,display_bullet_chart,display_alarm,display_semaphore,display_weight);
			   }
			  
		   }
	   } 
	   
	   
		return _htmlStream;
	}
}
