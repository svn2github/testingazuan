package it.eng.spagobi.engines.kpi.bo;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.analiticalmodel.document.service.ExecuteBIObjectModule;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.commons.utilities.urls.IUrlBuilder;
import it.eng.spagobi.commons.utilities.urls.UrlBuilderFactory;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.BulletGraph;
import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.model.bo.Resource;

import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

public class KpiResourceBlock {
	
	private static transient Logger logger = Logger.getLogger(KpiResourceBlock.class);
	Resource r = null;
	KpiLine root = null;
	Date d = null;
	
	public KpiResourceBlock(Resource r, KpiLine root, Date d) {
		super();
		this.r = r;
		this.root = root;
		this.d = d;
	}
	
	public KpiResourceBlock() {
		super();
		this.d = new Date();
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

	public Date getD() {
		return d;
	}

	public void setD(Date d) {
		this.d = d;
	}
	
	public StringBuffer makeTree(ExecutionInstance instanceO,String userId,HttpServletRequest httpReq, Boolean display_bullet_chart, Boolean display_alarm, Boolean display_semaphore, Boolean display_weight ){
		logger.debug("IN");
		StringBuffer _htmlStream = new StringBuffer();				
		if (r!=null){
			IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();
			logger.debug("Start Kpi tree for Resource "+r.getName());
			_htmlStream.append("<div id ='"+r.getName()+"' >\n");				
			_htmlStream.append("<table class='kpi_table' style='CLEAR: left; WIDTH: 100%' >\n");
			_htmlStream.append("<TBODY>\n");
			String res = msgBuilder.getMessage("sbi.kpi.RESOURCE", httpReq);
			_htmlStream.append(" <tr class='kpi_resource_section' ><td colspan=\"9\" id=\"ext-gen58\" >"+res+r.getName()+"</td></tr>\n");
		}else{
			_htmlStream.append("<table class='kpi_table' style='CLEAR: left;  WIDTH: 100%' >\n");
			_htmlStream.append("<TBODY>\n");
		}
		
		addItemForTree(instanceO,userId,0,false,httpReq, root,_htmlStream,display_bullet_chart,display_alarm,display_semaphore,display_weight);
		logger.debug("Started Kpi tree with the root");

			_htmlStream.append("</TBODY>\n");
			_htmlStream.append("</TABLE>\n");
		if (r!=null){	
			_htmlStream.append("</div>\n");	
		}
		
		 _htmlStream.append("\n");
		 logger.debug("Ended Kpi tree"); 
		logger.debug("OUT");
		return _htmlStream;
	}
	
	private StringBuffer addItemForTree(ExecutionInstance instanceO,String userId,int recursionLev, Boolean evenLine,HttpServletRequest httpReq,KpiLine line, StringBuffer _htmlStream,Boolean display_bullet_chart, Boolean display_alarm, Boolean display_semaphore, Boolean display_weight) {
		
		logger.debug("IN");
		logger.debug("*********************");
		logger.debug("Recursion Level:"+recursionLev);
		IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();
		HttpServletRequest httpRequest = httpReq;
		IUrlBuilder urlBuilder = UrlBuilderFactory.getUrlBuilder();
		String requestIdentity = null;
		UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
		UUID uuid = uuidGen.generateTimeBasedUUID();
		requestIdentity = uuid.toString();
		requestIdentity = requestIdentity.replaceAll("-", "");
		String alarmImgSrc = urlBuilder.getResourceLink(httpRequest, "/img/kpi/alarm.gif");
		String infoImgSrc = urlBuilder.getResourceLink(httpRequest, "/img/kpi/info.gif");
		String docImgSrc = urlBuilder.getResourceLink(httpRequest, "/img/kpi/linkedDoc.gif");
		String trendImgSrc = urlBuilder.getResourceLink(httpRequest, "/img/kpi/trend.jpg");
		String modelName = line.getModelNodeName();
		logger.debug("Model node :"+modelName);
		Boolean alarm = line.getAlarm();
		logger.debug("Alarm Control:"+(alarm!=null ? alarm.toString(): "alarm null" ));
		KpiValue kpiVal = line.getValue();
		String thresholdJsArray = line.getThresholdsJsArray();
		if(thresholdJsArray==null){
			thresholdJsArray ="";
		}
		logger.debug("Threshold Js Array to make the legend:"+thresholdJsArray);
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
				logger.debug("Kpi value :"+lo);
				weight = kpiVal.getWeight();
				logger.debug("Kpi weight :"+weight);
			}	
		}
		Color semaphorColor = line.getSemaphorColor();
		if (semaphorColor!= null){
			String semaphorHex = Integer.toHexString( semaphorColor.getRGB() & 0x00ffffff ) ;		
			logger.debug("Kpi semaphore color:"+semaphorHex);
		}
		
		BulletGraph sbi = (BulletGraph)line.getChartBullet();		
		List children = line.getChildren();
		
		if(recursionLev==0){
			_htmlStream.append("	<tr style='background-color:#DDDDDD;' class='kpi_line_section_odd'>\n");
			
				
		}
		else if(evenLine){
			 _htmlStream.append("	<tr class='kpi_line_section_even' style='border-bottom: 1px solid #660000 !important;'>\n");
		}else{
			_htmlStream.append("	<tr class='kpi_line_section_odd' style='border-bottom: 1px solid #DDDDDD !important;'>\n");
		}
		if (display_semaphore && semaphorColor!= null){
			String semaphorHex = Integer.toHexString( semaphorColor.getRGB() & 0x00ffffff ) ;		
			_htmlStream.append("		<td width='25%' class='kpi_td' ><div style=\"margin-left: "+20*recursionLev+"px;margin-top:5px;margin-right:5px;float:left;width:9px;height:9px;border: 1px solid #5B6B7C;background-color:#"+semaphorHex+"\"></div><div  class='kpi_div'>"+modelName+"</div></td>\n");
		}else{
			_htmlStream.append("		<td width='25%'  class='kpi_td' ><div class='kpi_div'><div style='MARGIN-LEFT: "+20*recursionLev+"px;text-align:left;' class='kpi_div'>"+modelName+"</div></div></td>\n");
		}
		logger.debug("Written HTML for Semaphore");
		logger.debug("Written HTML for ModelName:"+modelName);
		
		_htmlStream.append("		<td  width='28%' class='kpi_td' ><div id=\""+requestIdentity+"\" style='display:none;float:right;'></div></td>\n");
		
		String valueDescr = "";
		if(kpiVal !=null && kpiVal.getValueDescr()!=null){
			valueDescr = kpiVal.getValueDescr();
			_htmlStream.append("		<td width='4%' class='kpi_td' title='"+valueDescr+"' style='text-align:center;'  ><div class='kpi_div'  ><img src=\""+infoImgSrc+"\" /></div></td>\n");				
		}else{
			_htmlStream.append("		<td width='4%' class='kpi_td' title='"+valueDescr+"'   ><div ></div></td>\n");				
		}
		
		String periodValid = msgBuilder.getMessage("sbi.kpi.validPeriod", httpReq);
		if (kpiVal !=null){
			periodValid = periodValid.replaceAll("%0", kpiVal.getBeginDate().toString());
			periodValid = periodValid.replaceAll("%1", kpiVal.getEndDate().toString());		    
		}

		
		if (lo!= null && kpiVal !=null && kpiVal.getScaleCode()!=null){
			_htmlStream.append("		<td  width='9%' title='"+periodValid+"' class='kpi_td_left'  ><div  class='kpi_div'>"+lo.toString()+"("+kpiVal.getScaleCode()+")</div></td>\n");
		}else if(lo!= null){
			_htmlStream.append("		<td  width='9%' title='"+periodValid+"' class='kpi_td_left'  ><div  class='kpi_div'>"+lo.toString()+"</div></td>\n");
		}else{
			_htmlStream.append("		<td  width='9%' title='"+periodValid+"' class='kpi_td_left' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		logger.debug("Written HTML for value");
		
		String weight2 = msgBuilder.getMessage("sbi.kpi.weight", httpReq);
		if (display_weight && weight!=null){
			_htmlStream.append("		<td width='5%' title='"+weight2+"' class='kpi_td_left'  ><div  class='kpi_div'>["+weight.toString()+"]</div></td>\n");
		}else{
			_htmlStream.append("		<td width='5%' class='kpi_td_left' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		logger.debug("Written HTML for weight");
		
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
			_htmlStream.append("		<td width='22%' class='kpi_td_left'  ><div style='margin-top:4px;'><img style=\"align:left;\" onmouseover=\"showLegendTooltip("+thresholdJsArray+",'"+requestIdentity+"');\" onmouseout=\"hideLegendTooltip('"+requestIdentity+"');\" id=\"image\" src=\""+urlPng+"\" BORDER=\"1\" alt=\"Error in displaying the chart\" USEMAP=\"#chart\"/></div></td>\n");
			
		}else{
			_htmlStream.append("		<td width='22%' class='kpi_td_left' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		logger.debug("Written HTML for Bullet Chart.");
				
		
		if(lo!= null){		

				HashMap execUrlParMap = new HashMap();
				execUrlParMap.put(ObjectsTreeConstants.ACTION, "GET_TREND");
				if (r!=null){
					execUrlParMap.put("RESOURCE_ID", r.getId());
					execUrlParMap.put("RESOURCE_NAME", r.getName());
				}
				if (d!=null){
					SourceBean formatSB = ((SourceBean) ConfigSingleton.getInstance().getAttribute(
					"SPAGOBI.DATE-FORMAT"));
					String format = (String) formatSB.getAttribute("format");
					SimpleDateFormat f = new SimpleDateFormat();
					f.applyPattern(format);	
				    String dat = f.format(d);
				    execUrlParMap.put("END_DATE", dat);						
				}
				if (kpiVal!=null) execUrlParMap.put("KPI_INST_ID", kpiVal.getKpiInstanceId());
				execUrlParMap.put("LIGHT_NAVIGATOR_DISABLED", "true");
				
				String trend = msgBuilder.getMessage("sbi.kpi.trend", httpReq);
				String trendPopupUrl = urlBuilder.getUrl(httpRequest, execUrlParMap);
				_htmlStream.append("		<td  width='3%'  class='kpi_td' title=\""+trend+"\" style='text-align:center;' ><div class='kpi_div'  ><a id='linkDetail_"+requestIdentity+"_"+recursionLev+"' ><img  src=\""+trendImgSrc+"\" /></div></a></td>\n");
				// insert javascript for open popup window for the trend
			    _htmlStream.append(" <script>\n");
			    _htmlStream.append("   var win"+requestIdentity+"_"+recursionLev+"; \n");
			    _htmlStream.append("Ext.get('linkDetail_"+requestIdentity+"_"+recursionLev+"').on('click', function(){ \n");
			    _htmlStream.append("   if ( win"+requestIdentity+"_"+recursionLev+" == null ) {win"+requestIdentity+"_"+recursionLev+"=new Ext.Window({id:'win"+requestIdentity+"_"+recursionLev+"',\n");
			    _htmlStream.append("            bodyCfg:{ \n" );
			    _htmlStream.append("                tag:'div' \n");
			    _htmlStream.append("                ,cls:'x-panel-body' \n");
			    _htmlStream.append("               ,children:[{ \n");
			    _htmlStream.append("                    tag:'iframe', \n");
			    _htmlStream.append("                    name: 'dynamicIframe"+requestIdentity+"_"+recursionLev+"', \n");
			    _htmlStream.append("                    id  : 'dynamicIframe"+requestIdentity+"_"+recursionLev+"', \n");
			    _htmlStream.append("                    src: '"+trendPopupUrl+"', \n");
			    _htmlStream.append("                    frameBorder:0, \n");
			    _htmlStream.append("                    width:'100%', \n");
			    _htmlStream.append("                    height:'100%', \n");
			    _htmlStream.append("                    style: {overflow:'auto'}  \n ");        
			    _htmlStream.append("               }] \n");
			    _htmlStream.append("            }, \n");
			    _htmlStream.append("            modal: true,\n");
			    _htmlStream.append("            layout:'fit',\n");
				_htmlStream.append("            height:360,\n");
				_htmlStream.append("            width:500,\n");
				_htmlStream.append("            closeAction:'hide',\n");
		        _htmlStream.append("            scripts: true, \n");
		        _htmlStream.append("            plain: true \n");       
		        _htmlStream.append("        }); }; \n");
			    _htmlStream.append("   win"+requestIdentity+"_"+recursionLev+".show(); \n");
			    _htmlStream.append("	} \n");
			    _htmlStream.append(");\n");
			    _htmlStream.append(" </script>\n");
			}else{
				_htmlStream.append("		<td class='kpi_td' width='3%'  ><div  ></div></td>\n");
			}
		logger.debug("Written HTML for Popup window with trend.");
		
		List documents = line.getDocuments();
		
		if (documents!=null && !documents.isEmpty()){
			String executionFlowId = instanceO.getFlowId();
			String uuidPrincip = instanceO.getExecutionId();
			_htmlStream.append("		<td width='2%'  class='kpi_td' ><div class='kpi_div'  >\n");
			Iterator it = documents.iterator();
			while(it.hasNext()){
				String docLabel =(String)it.next();
				HashMap execUrlParMap = new HashMap();
				execUrlParMap.put(ObjectsTreeConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
				execUrlParMap.put(ObjectsTreeConstants.OBJECT_LABEL, docLabel);
				execUrlParMap.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EXEC_CROSS_NAVIGATION);
				execUrlParMap.put("EXECUTION_FLOW_ID", executionFlowId);
				execUrlParMap.put("SOURCE_EXECUTION_ID", uuidPrincip);
				
				if (r!=null){
					execUrlParMap.put(r.getColumn_name(), r.getName());
				}
				if (d!=null){
					SourceBean formatSB = ((SourceBean) ConfigSingleton.getInstance().getAttribute(
					"SPAGOBI.DATE-FORMAT"));
					String format = (String) formatSB.getAttribute("format");
					SimpleDateFormat f = new SimpleDateFormat();
					f.applyPattern(format);	
				    String dat = f.format(d);
				    execUrlParMap.put("ParKpiDate", dat);						
				}
				String docLinked = msgBuilder.getMessage("sbi.kpi.docLinked", httpReq);
				String docHref = urlBuilder.getUrl(httpRequest, execUrlParMap);
				_htmlStream.append("<a  title='"+docLinked+"' href=\""+docHref+"\"> <img  src=\""+docImgSrc+"\" alt=\"Attached Document\" /></a>\n");				
			}
			_htmlStream.append("		</div></td>\n");
		}else{
			_htmlStream.append("		<td width='2%' class='kpi_td_right' ><div style='align:left;' class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		logger.debug("Written HTML for Documents linked to the kpi");
		
		if (display_alarm){
			String alarmControl = msgBuilder.getMessage("sbi.kpi.alarmControl", httpReq);
			if(alarm) _htmlStream.append("		<td width='2%' title='"+alarmControl+"' style='text-align:center;' class='kpi_td_right' ><div class='kpi_div'  ><img  src=\""+alarmImgSrc+"\" alt=\"Kpi under Alarm Control\" /></div></td>\n");
			else _htmlStream.append("		<td width='2%' class='kpi_td_right' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}else{
			_htmlStream.append("		<td width='2%' class='kpi_td_right' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		logger.debug("Written HTML for alarm control:");
		
		
	   _htmlStream.append("	</tr>\n");

	   if (children!=null && !children.isEmpty()){
		   recursionLev ++;
		   Iterator childIt = children.iterator();
		   while (childIt.hasNext()){
			   logger.debug("Starting children levels");
			   KpiLine l = (KpiLine)childIt.next();
			   
			   if (evenLine){			   
				   addItemForTree(instanceO,userId,recursionLev,false,httpReq, l,_htmlStream,display_bullet_chart,display_alarm,display_semaphore,display_weight);
			   }else{
				   addItemForTree(instanceO,userId,recursionLev,true,httpReq, l,_htmlStream,display_bullet_chart,display_alarm,display_semaphore,display_weight);
			   }  
		   }
	   } 
	   
	   logger.debug("OUT");
		return _htmlStream;
	}
	
}
