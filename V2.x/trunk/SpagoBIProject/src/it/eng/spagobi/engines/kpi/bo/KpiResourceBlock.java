package it.eng.spagobi.engines.kpi.bo;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.service.ExecuteBIObjectModule;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.urls.IUrlBuilder;
import it.eng.spagobi.commons.utilities.urls.UrlBuilderFactory;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.BulletGraph;
import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.model.bo.Resource;
import it.eng.spagobi.kpi.threshold.bo.Threshold;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
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
	
	public StringBuffer makeTree(String userId,HttpServletRequest httpReq, Boolean display_bullet_chart, Boolean display_alarm, Boolean display_semaphore, Boolean display_weight ){
		
		StringBuffer _htmlStream = new StringBuffer();				
		if (r!=null){
			_htmlStream.append("<div id ='"+r.getName()+"' >\n");				
			_htmlStream.append("<table style='width:100%;margin-left:0px;align:left;vertical-align:middle;' float=\"left\">\n");
			_htmlStream.append(" <tr class='kpi_resource_section'><td>RESOURCE: "+r.getName()+"</td><td></td><td></td><td></td><td></td></tr>\n");
			_htmlStream.append("</table>\n");
		}
		
		addItemForTree(userId,0,false,httpReq, root,_htmlStream,display_bullet_chart,display_alarm,display_semaphore,display_weight);
		if (r!=null){
			_htmlStream.append("</div><br>\n");
		}
		 _htmlStream.append("\n");
		return _htmlStream;
	}
	
	private StringBuffer addItemForTree(String userId,int recursionLev, Boolean evenLine,HttpServletRequest httpReq,KpiLine line, StringBuffer _htmlStream,Boolean display_bullet_chart, Boolean display_alarm, Boolean display_semaphore, Boolean display_weight) {
		
		HttpServletRequest httpRequest = httpReq;
		IUrlBuilder urlBuilder = UrlBuilderFactory.getUrlBuilder();
		String requestIdentity = null;
		UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
		UUID uuid = uuidGen.generateTimeBasedUUID();
		requestIdentity = uuid.toString();
		requestIdentity = requestIdentity.replaceAll("-", "");
		String alarmImgSrc = urlBuilder.getResourceLink(httpRequest, "/img/kpi/alarm.jpg");
		String docImgSrc = urlBuilder.getResourceLink(httpRequest, "/img/linkedDoc.gif");
		String modelName = line.getModelNodeName();
		Boolean alarm = line.getAlarm();
		KpiValue kpiVal = line.getValue();
		String thresholdJsArray = line.getThresholdsJsArray();
		if(thresholdJsArray==null){
			thresholdJsArray ="";
		}
		List thresholds = null;		
		String value = null;
		Float lo =null;
		Double weight = null;
		if (kpiVal!=null){
			thresholds = kpiVal.getThresholds();
			value = kpiVal.getValue();
			if(value!=null){
				Double val = new Double(value);
				lo = new Float( val.floatValue());
				weight = kpiVal.getWeight();
			}	
		}
		Color semaphorColor = line.getSemaphorColor();
		
		BulletGraph sbi = (BulletGraph)line.getChartBullet();		
		List children = line.getChildren();
		if(evenLine){
			_htmlStream.append("<div><table   style='width:"+(100-(2*recursionLev))+"%;margin-left:"+20*recursionLev+"px;align:left;vertical-align:middle;border-bottom: 1px solid #660000 !important;'  >\n");
		}else{
			_htmlStream.append("<div><table   style='width:"+(100-(2*recursionLev))+"%;margin-left:"+20*recursionLev+"px;align:left;vertical-align:middle;border-bottom: 1px solid #DDDDDD !important;'  >\n");
		}
		
		if(recursionLev==0){
			_htmlStream.append("	<tr style='background-color:#DDDDDD;' class='kpi_line_section_odd'>\n");
		}
		else if(evenLine){
			 _htmlStream.append("	<tr class='kpi_line_section_even' style='border-bottom: 1px solid #660000 !important;height:9px;'>\n");
		}else{
			_htmlStream.append("	<tr class='kpi_line_section_odd'>\n");
		}
		if (display_semaphore && semaphorColor!= null){
			String semaphorHex = Integer.toHexString( semaphorColor.getRGB() & 0x00ffffff ) ;		
			_htmlStream.append("		<td width='2%' style='padding-top:8px;padding-bottom:8px;' ><div style=\"width:9px;height:9px;border: 1px solid #5B6B7C;background-color:#"+semaphorHex+"\"></div></td>\n");
		}else{
			_htmlStream.append("		<td width='2%' class='kpi_td_left' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		
		_htmlStream.append("		<td class='kpi_td_left' style='vertical-align:middle;' width='53%' title='Model Instance Node' ><div style='vertical-align:middle;' class='kpi_div'>"+modelName+"</div></td>\n");
		
		_htmlStream.append("		<td  width='5%' ><div id=\""+requestIdentity+"\" style='display:none'></div></td>\n");
		if (lo!= null){
			_htmlStream.append("		<td  width='9%' title='Value' class='kpi_td_left' style='vertical-align:middle;' ><div style='vertical-align:middle;' class='kpi_div'>"+lo.toString()+"</div></td>\n");
		}else{
			_htmlStream.append("		<td  width='9%' title='Value' class='kpi_td_left' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		if (display_weight && weight!=null){
			_htmlStream.append("		<td width='5%' title='Weight' class='kpi_td_left' style='vertical-align:middle;' ><div style='vertical-align:middle;' class='kpi_div'>["+weight.toString()+"]</div></td>\n");
		}else{
			_htmlStream.append("		<td width='5%' class='kpi_td_left' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		
		if (display_bullet_chart && sbi!=null){
			
			JFreeChart chart = sbi.createChart();
			ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
			String dir=System.getProperty("java.io.tmpdir");
			String path=dir+"/"+requestIdentity+".png";
			java.io.File file1 = new java.io.File(path);
			try {
				ChartUtilities.saveChartAsPNG(file1, chart, 250, 16, info);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String urlPng=GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + 
			"?ACTION_NAME=GET_PNG2&NEW_SESSION=TRUE&userid=<%=userId%>&path="+path+"&LIGHT_NAVIGATOR_DISABLED=TRUE";
			_htmlStream.append("		<td width='25%' class='kpi_td_left' style='vertical-align:middle;' ><div class='kpi_div' style='vertical-align:middle;' ><img style=\"align:left;vertical-align:middle;\" onmouseover=\"showLegendTooltip("+thresholdJsArray+",'"+requestIdentity+"');\" onmouseout=\"hideLegendTooltip('"+requestIdentity+"');\" id=\"image\" src=\""+urlPng+"\" BORDER=\"1\" alt=\"Error in displaying the chart\" USEMAP=\"#chart\"/></div></td>\n");
			
		}else{
			_htmlStream.append("		<td width='25%' class='kpi_td_left' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		
		List documents = line.getDocuments();
		
		if (documents!=null && !documents.isEmpty()){
			_htmlStream.append("		<td width='3%' style='vertical-align:middle;' class='kpi_td_left' ><div class='kpi_div' style='vertical-align:middle;' >\n");
			Iterator it = documents.iterator();
			while(it.hasNext()){
				String docLabel =(String)it.next();
				HashMap execUrlParMap = new HashMap();
				execUrlParMap.put(ObjectsTreeConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
				execUrlParMap.put(ObjectsTreeConstants.OBJECT_LABEL, docLabel);
				execUrlParMap.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE);
				String docHref = urlBuilder.getUrl(httpRequest, execUrlParMap);
				_htmlStream.append("<a style='vertical-align:middle;' title='Document linked to the kpi' href=\""+docHref+"\"> <img style='vertical-align:middle;' src=\""+docImgSrc+"\" alt=\"Attached Document\" /></a>\n");				
			}
			_htmlStream.append("		</div></td>\n");
		}else{
			_htmlStream.append("		<td width='4%' class='kpi_td_left' ><div style='align:left;' class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		
		if (display_alarm){
			if(alarm) _htmlStream.append("		<td width='2%' title='Kpi under Alarm control' style='vertical-align:middle;' class='kpi_td_right' ><div class='kpi_div' style='vertical-align:middle;' ><img style='vertical-align:middle;' src=\""+alarmImgSrc+"\" alt=\"Kpi under Alarm Control\" /></div></td>\n");
			else _htmlStream.append("		<td width='2%' class='kpi_td_right' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}else{
			_htmlStream.append("		<td width='2%' class='kpi_td_right' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		
	   _htmlStream.append("	</tr>\n");
	   _htmlStream.append("</table></div>\n");
	   if (children!=null && !children.isEmpty()){
		   recursionLev ++;
		   Iterator childIt = children.iterator();
		   while (childIt.hasNext()){
			   KpiLine l = (KpiLine)childIt.next();
			   
			   if (evenLine){			   
				   addItemForTree(userId,recursionLev,false,httpReq, l,_htmlStream,display_bullet_chart,display_alarm,display_semaphore,display_weight);
			   }else{
				   addItemForTree(userId,recursionLev,true,httpReq, l,_htmlStream,display_bullet_chart,display_alarm,display_semaphore,display_weight);
			   }  
		   }
	   } 
	   
	   
		return _htmlStream;
	}
	
}
