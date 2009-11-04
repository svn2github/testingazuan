package it.eng.spagobi.engines.kpi.bo;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.utilities.ChannelUtilities;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.commons.utilities.urls.IUrlBuilder;
import it.eng.spagobi.commons.utilities.urls.UrlBuilderFactory;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.BulletGraph;
import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.model.bo.Resource;
import it.eng.spagobi.kpi.threshold.bo.ThresholdValue;
import it.eng.spagobi.utilities.themes.ThemesManager;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class KpiResourceBlock implements Serializable{
	
	private static transient Logger logger = Logger.getLogger(KpiResourceBlock.class);
	Resource r = null;
	KpiLine root = null;
	Date d = null;
	String title = "";
	String subtitle = "";
	protected HashMap parMap;
	protected String currTheme="";
	protected RequestContainer requestContainer = null;
	KpiLineVisibilityOptions options = new KpiLineVisibilityOptions();
	
	public KpiResourceBlock(Resource r, KpiLine root, Date d, HashMap parMap,KpiLineVisibilityOptions options ) {
		super();
		this.r = r;
		this.root = root;
		this.d = d;
		this.parMap = parMap;
		this.options = options;
	}
	
	public KpiResourceBlock() {
		super();
		this.d = new Date();
		this.parMap = new HashMap();
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
	
	public KpiLineVisibilityOptions getOptions() {
		return options;
	}

	public void setOptions(KpiLineVisibilityOptions options) {
		this.options = options;
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
	
	public HashMap getParMap() {
		return parMap;
	}

	public void setParMap(HashMap parMap) {
		this.parMap = parMap;
	}	
	
	
	public StringBuffer makeTree(ExecutionInstance instanceO,String userId,HttpServletRequest httpReq, KpiLineVisibilityOptions options ){
		logger.debug("IN");
		StringBuffer _htmlStream = new StringBuffer();	
		String id = "";
		requestContainer = ChannelUtilities.getRequestContainer(httpReq);
    	currTheme=ThemesManager.getCurrentTheme(requestContainer);
    	if(currTheme==null)currTheme=ThemesManager.getDefaultTheme();
		
		if (r!=null){
			IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();
			logger.debug("Start Kpi tree for Resource "+r.getName());
			_htmlStream.append("<div id ='"+r.getName()+"' >\n");				
			_htmlStream.append("<table class='kpi_table' id='KPI_TABLE"+r.getId()+"' >\n");
			_htmlStream.append("<TBODY>\n");
			String res = msgBuilder.getMessage("sbi.kpi.RESOURCE", httpReq);
			if (options.getDisplay_bullet_chart() && options.getDisplay_threshold_image() ){
				_htmlStream.append(" <tr class='kpi_resource_section' ><td colspan=\"10\" id=\"ext-gen58\" >"+res+r.getName()+"</td></tr>\n");
			}else{
				_htmlStream.append(" <tr class='kpi_resource_section' ><td colspan=\"9\" id=\"ext-gen58\" >"+res+r.getName()+"</td></tr>\n");
			}
			id = "node"+r.getId();
		}else{
			_htmlStream.append("<table class='kpi_table' id='KPI_TABLE' >\n");
			_htmlStream.append("<TBODY>\n");
			id = "node1";
		}
		_htmlStream.append(" <tr class='kpi_first_line_section_odd' >");
		_htmlStream.append("		<td width='53%'  class='kpi_first_line_td' style='text-align:left;' >"+options.getModel_title()+"</td>\n");
		_htmlStream.append("		<td width='4%' ><div></div></td>\n");		
		if(options.getKpi_title()!=null){
			_htmlStream.append("		<td  width='9%' class='kpi_first_line_td' >"+options.getKpi_title()+"</td>\n");
		}else{
			_htmlStream.append("		<td  width='9%' class='kpi_first_line_td' ><div></div></td>\n");
		}
		if (options.getDisplay_weight() && options.getWeight_title()!=null){ 
			_htmlStream.append("		<td width='5%' class='kpi_first_line_td' >"+options.getWeight_title()+"</td>\n");
		}else{
			_htmlStream.append("		<td width='5%' class='kpi_first_line_td' ><div></div></td>\n");
		}
		if (options.getDisplay_bullet_chart() && options.getDisplay_threshold_image() && options.getBullet_chart_title()!=null && options.getBullet_chart_title()!=null){
			_htmlStream.append("		<td width='15%' class='kpi_first_line_td' style='text-align:center;' >"+options.getBullet_chart_title()+"</td>\n");
			_htmlStream.append("		<td width='7%' class='kpi_first_line_td' >"+(options.getThreshold_image_title()!=null?options.getThreshold_image_title():"")+"</td>\n");
		}else if(options.getDisplay_bullet_chart()  && options.getBullet_chart_title()!=null){
			_htmlStream.append("		<td width='22%' class='kpi_first_line_td' style='text-align:center;' >"+options.getBullet_chart_title()+"</td>\n");
		}else if(options.getDisplay_threshold_image()  && options.getThreshold_image_title()!=null){
			_htmlStream.append("		<td width='22%' class='kpi_first_line_td' style='text-align:center;' >"+options.getThreshold_image_title()+"</td>\n");
		}else{
			_htmlStream.append("		<td width='22%' class='kpi_first_line_td' style='text-align:center;' ><div></div></td>\n");
		}
		_htmlStream.append("		<td width='3%' ><div></div></td>\n");
		_htmlStream.append("		<td width='2%' ><div></div></td>\n");
		_htmlStream.append("		<td width='2%' ><div></div></td>\n");
		_htmlStream.append(" </tr>");
		
		addItemForTree(id,instanceO,userId,0,false,httpReq, root,_htmlStream,options);
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
	
	private StringBuffer addItemForTree(String id,ExecutionInstance instanceO,String userId,int recursionLev, Boolean evenLine,HttpServletRequest httpReq,KpiLine line, StringBuffer _htmlStream,KpiLineVisibilityOptions options) {
		logger.debug("IN");
		logger.debug("*********************");
		
		logger.debug("Recursion Level:"+recursionLev);
		IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();
		String execMod = instanceO.getExecutionModality();
		HttpServletRequest httpRequest = httpReq;
		IUrlBuilder urlBuilder = UrlBuilderFactory.getUrlBuilder();
		String requestIdentity = null;
		UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
		UUID uuid = uuidGen.generateTimeBasedUUID();
		requestIdentity = uuid.toString();
		requestIdentity = requestIdentity.replaceAll("-", "");
		String alarmImgSrc = urlBuilder.getResourceLinkByTheme(httpRequest, "/img/kpi/alarm.gif",currTheme);
		String infoImgSrc = urlBuilder.getResourceLinkByTheme(httpRequest, "/img/kpi/info.gif",currTheme);
		String docImgSrc = urlBuilder.getResourceLinkByTheme(httpRequest, "/img/kpi/linkedDoc.gif",currTheme);
		String trendImgSrc = urlBuilder.getResourceLinkByTheme(httpRequest, "/img/kpi/trend.jpg",currTheme);
		String modelName = line.getModelNodeName();
		String modelCode = line.getModelInstanceCode();
		String timeRangeFrom = null;
		String timeRangeTo = null;
		if (parMap!=null && !parMap.isEmpty() && parMap.containsKey("TimeRangeFrom")){
		  timeRangeFrom = (String) parMap.get("TimeRangeFrom");
		}
		if (parMap!=null && !parMap.isEmpty() && parMap.containsKey("TimeRangeTo")){
		  timeRangeTo = (String) parMap.get("TimeRangeTo");
		}
		if(modelCode!=null && !modelCode.equals("")){
			modelName = modelCode+" - "+ modelName;
		}
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
			thresholds = kpiVal.getThresholdValues();
			value = kpiVal.getValue();
			if(value!=null){
				Double val = new Double(value);
				lo = new Float( val.floatValue());
				logger.debug("Kpi value :"+lo);
				weight = kpiVal.getWeight();
				logger.debug("Kpi weight :"+weight);
				if(options.getWeighted_values()){
					lo =new Float(val*weight);
				}
			}	
		}
		Color semaphorColor = line.getSemaphorColor();		
		BulletGraph sbi = (BulletGraph)line.getChartBullet();		
		List children = line.getChildren();
		String tab_name = "KPI_TABLE";
		if(r!=null){
			tab_name = tab_name+r.getId();
		}
		
		if(recursionLev==0){
			_htmlStream.append("	<tr class='kpi_line_section_odd' id='"+id+"'>\n");
			
				
		}else if(evenLine){
			 _htmlStream.append("	<tr class='kpi_line_section_even' id='"+id+"' >\n");
		}else{
			_htmlStream.append("	<tr class='kpi_line_section_odd' id='"+id+"' >\n");
		}
		if (options.getDisplay_semaphore() && semaphorColor!= null){
			String semaphorHex ="rgb("+semaphorColor.getRed()+", "+semaphorColor.getGreen()+", "+semaphorColor.getBlue()+")" ;	
			if (children!=null && !children.isEmpty()){
					_htmlStream.append("		<td width='53%' class='kpi_td' ><div class='kpi_semaphore' style=\"margin-left: "+20*recursionLev+"px;background-color:"+semaphorHex+"\"></div><div  class='kpi_div'><span class='toggleKPI' onclick=\"toggleHideChild('"+id+"','"+tab_name+"');\">&nbsp;</span>"+modelName+"</div></td>\n");
			}else{
				_htmlStream.append("		<td width='53%' class='kpi_td' ><div class='kpi_semaphore' style=\"margin-left: "+20*recursionLev+"px;background-color:"+semaphorHex+"\"></div><div  class='kpi_div'>"+modelName+"</div></td>\n");
			}
		}else{
			if (children!=null && !children.isEmpty()){
					_htmlStream.append("		<td width='53%'  class='kpi_td' ><div class='kpi_div'><div style='MARGIN-LEFT: "+20*recursionLev+"px;text-align:left;' class='kpi_div'><span class='toggleKPI' onclick=\"toggleHideChild('"+id+"','"+tab_name+"');\">&nbsp;</span>"+modelName+"</div></div></td>\n");
			}else{
				_htmlStream.append("		<td width='53%'  class='kpi_td' ><div class='kpi_div'><div style='MARGIN-LEFT: "+20*recursionLev+"px;text-align:left;' class='kpi_div'>"+modelName+"</div></div></td>\n");
			}
		}
		logger.debug("Written HTML for Semaphore");
		logger.debug("Written HTML for ModelName:"+modelName);
		
		//_htmlStream.append("		<td  width='0%' class='kpi_td' ><div id=\""+requestIdentity+"\" style='display:none;float:right;'></div></td>\n");
		
		String valueDescr = "";
		if(kpiVal !=null && kpiVal.getKpiInstanceId()!=null){
			if(kpiVal.getValueDescr()!=null)valueDescr = kpiVal.getValueDescr();		
			
			HashMap execUrlParMap = new HashMap();
			execUrlParMap.put(ObjectsTreeConstants.ACTION, "GET_KPI_METADATA");
			execUrlParMap.put("KPI_VALUE_DESCR", valueDescr);

			if (kpiVal!=null){
				execUrlParMap.put("KPI_BEGIN_DATE",kpiVal.getBeginDate() !=null ? kpiVal.getBeginDate().toString():"");
				execUrlParMap.put("KPI_END_DATE",kpiVal.getEndDate() !=null ? kpiVal.getEndDate().toString():"");
				execUrlParMap.put("KPI_INST_ID", kpiVal.getKpiInstanceId()!=null ? kpiVal.getKpiInstanceId().toString():"");
				execUrlParMap.put("KPI_VALUE",kpiVal.getValue()!=null ? kpiVal.getValue():"");
				execUrlParMap.put("KPI_WEIGHT",kpiVal.getWeight()!=null ? kpiVal.getWeight().toString():"");
				execUrlParMap.put("WEIGHTED_VALUE",options.getWeighted_values()!=null ? options.getWeighted_values():false);
				execUrlParMap.put("KPI_TARGET",kpiVal.getTarget()!=null ? kpiVal.getTarget().toString():"");
			}		
			execUrlParMap.put("KPI_MODEL_INST_ID",line.getModelInstanceNodeId()!=null ? line.getModelInstanceNodeId().toString():"");
			execUrlParMap.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");

			//String metadataPopupUrl = urlBuilder.getUrl(httpRequest, execUrlParMap);
			String metadataPopupUrl = createUrl_popup(execUrlParMap,userId);
			_htmlStream.append("		<td  width='4%'  class='kpi_td' title='"+valueDescr+"' style='text-align:center;' ><div class='kpi_div'  ><a id='linkMetadata_"+requestIdentity+"_"+recursionLev+"' ><img src=\""+infoImgSrc+"\" /></div></a></td>\n");
			// insert javascript for open popup window for the trend
		    _htmlStream.append(" <script>\n");
		    _htmlStream.append("   var winMetadata"+requestIdentity+"_"+recursionLev+"; \n");
		    _htmlStream.append("Ext.get('linkMetadata_"+requestIdentity+"_"+recursionLev+"').on('click', function(){ \n");
		    _htmlStream.append("   if ( winMetadata"+requestIdentity+"_"+recursionLev+" == null ) {winMetadata"+requestIdentity+"_"+recursionLev+"=new Ext.Window({id:'winMetadata"+requestIdentity+"_"+recursionLev+"',\n");
		    _htmlStream.append("            bodyCfg:{ \n" );
		    _htmlStream.append("                tag:'div' \n");
		    _htmlStream.append("                ,cls:'x-panel-body' \n");
		    _htmlStream.append("               ,children:[{ \n");
		    _htmlStream.append("                    tag:'iframe', \n");
		    _htmlStream.append("                    name: 'dynamicIframe"+requestIdentity+"_"+recursionLev+"', \n");
		    _htmlStream.append("                    id  : 'dynamicIframe"+requestIdentity+"_"+recursionLev+"', \n");
		    _htmlStream.append("                    src: '"+metadataPopupUrl+"', \n");
		    _htmlStream.append("                    frameBorder:0, \n");
		    _htmlStream.append("                    width:'100%', \n");
		    _htmlStream.append("                    height:'100%', \n");
		    _htmlStream.append("                    style: {overflow:'auto'}  \n ");        
		    _htmlStream.append("               }] \n");
		    _htmlStream.append("            }, \n");
		    _htmlStream.append("            modal: true,\n");
		    _htmlStream.append("            layout:'fit',\n");
			_htmlStream.append("            height:380,\n");
			_htmlStream.append("            width:500,\n");
			_htmlStream.append("            closeAction:'hide',\n");
	        _htmlStream.append("            scripts: true, \n");
	        _htmlStream.append("            plain: true \n");       
	        _htmlStream.append("        }); }; \n");
		    _htmlStream.append("   winMetadata"+requestIdentity+"_"+recursionLev+".show(); \n");
		    _htmlStream.append("	} \n");
		    _htmlStream.append(");\n");
		    _htmlStream.append(" </script>\n");
			
			//_htmlStream.append("		<td width='4%' class='kpi_td' title='"+valueDescr+"' style='text-align:center;'  ><div class='kpi_div'  ><img src=\""+infoImgSrc+"\" /></div></td>\n");				
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
		if (options.getDisplay_weight() && weight!=null){
			_htmlStream.append("		<td width='5%' title='"+weight2+"' class='kpi_td_left'  ><div  class='kpi_div'>["+weight.toString()+"]</div></td>\n");
		}else{
			_htmlStream.append("		<td width='5%' class='kpi_td_left' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		logger.debug("Written HTML for weight");
		
		if (kpiVal!=null && kpiVal.getThresholdValues()!=null && !kpiVal.getThresholdValues().isEmpty() && sbi!=null){
			if (options.getDisplay_bullet_chart() && options.getDisplay_threshold_image() ){	
				
				JFreeChart chart = sbi.createChart();
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				String path_param = requestIdentity;
				String dir=System.getProperty("java.io.tmpdir");
				String path=dir+"/"+requestIdentity+".png";
				java.io.File file1 = new java.io.File(path);
				try {
					if(!options.getShow_axis()){
						ChartUtilities.saveChartAsPNG(file1, chart, 200, 16, info);
					}else{
						ChartUtilities.saveChartAsPNG(file1, chart, 200, 30, info);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				String urlPng1=GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + 
				"?ACTION_NAME=GET_PNG2&NEW_SESSION=TRUE&path="+path_param+"&LIGHT_NAVIGATOR_DISABLED=TRUE";
				_htmlStream.append("		<td width='15%' class='kpi_td_left'  ><div class='kpi_bulletchart'><img style=\"align:left;\" id=\"image\" src=\""+urlPng1+"\" BORDER=\"1\" alt=\"Error in displaying the chart\" USEMAP=\"#chart\"/></div></td>\n");
			
				ThresholdValue tOfVal = line.getThresholdOfValue();
				if (tOfVal!=null && tOfVal.getPosition()!=null && tOfVal.getThresholdCode()!=null){
					String fileName ="position_"+tOfVal.getPosition().intValue();
					String dirName = tOfVal.getThresholdCode();
					String urlPng=GeneralUtilities.getSpagoBiHost()+GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + 
					"?ACTION_NAME=GET_THR_IMAGE&NEW_SESSION=TRUE&fileName="+fileName+"&dirName="+dirName+"&LIGHT_NAVIGATOR_DISABLED=TRUE";	
					_htmlStream.append("		<td width='7%' class='kpi_td_left'  ><div class='kpi_image'><img style=\"align:left;\" id=\"image\" src=\""+urlPng+"\" alt=\"Error in displaying the chart\" USEMAP=\"#chart\" BORDER=\"1\" /></div></td>\n");
					
				}
				
			}else if(options.getDisplay_bullet_chart() && !options.getDisplay_threshold_image()){
				
				JFreeChart chart = sbi.createChart();
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				String path_param = requestIdentity;
				String dir=System.getProperty("java.io.tmpdir");
				String path=dir+"/"+requestIdentity+".png";
				java.io.File file1 = new java.io.File(path);
				try {
					if(!options.getShow_axis()){
						ChartUtilities.saveChartAsPNG(file1, chart, 250, 16, info);
					}else{
						ChartUtilities.saveChartAsPNG(file1, chart, 250, 30, info);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				String urlPng=GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + 
				"?ACTION_NAME=GET_PNG2&NEW_SESSION=TRUE&path="+path_param+"&LIGHT_NAVIGATOR_DISABLED=TRUE";
				_htmlStream.append("		<td width='22%' class='kpi_td_left'  ><div class='kpi_bulletchart'><img style=\"align:left;\" id=\"image\" src=\""+urlPng+"\" BORDER=\"1\" alt=\"Error in displaying the chart\" USEMAP=\"#chart\"/></div></td>\n");
				
			}else if(!options.getDisplay_bullet_chart() && options.getDisplay_threshold_image()){
				ThresholdValue tOfVal = line.getThresholdOfValue();
				if (tOfVal!=null && tOfVal.getPosition()!=null && tOfVal.getThresholdCode()!=null){
					String fileName ="position_"+tOfVal.getPosition().intValue();
					String dirName = tOfVal.getThresholdCode();
					String urlPng=GeneralUtilities.getSpagoBiHost()+GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + 
					"?ACTION_NAME=GET_THR_IMAGE&NEW_SESSION=TRUE&fileName="+fileName+"&dirName="+dirName+"&LIGHT_NAVIGATOR_DISABLED=TRUE";	
					_htmlStream.append("		<td width='22%' class='kpi_td_left'  ><div class='kpi_image'><img style=\"align:left;\" id=\"image\" src=\""+urlPng+"\" alt=\"Error in displaying the chart\" USEMAP=\"#chart\" BORDER=\"1\" /></div></td>\n");
					
				}
			}
		}else{
			if (options.getDisplay_bullet_chart() && options.getDisplay_threshold_image() ){
				_htmlStream.append("		<td width='15%' class='kpi_td_left' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
				_htmlStream.append("		<td width='7%' class='kpi_td_left' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
			}else{
				_htmlStream.append("		<td width='22%' class='kpi_td_left' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
			}
		}
		logger.debug("Written HTML for Bullet Chart.");
				
		
		if(lo!= null){		

				HashMap execUrlParMap = new HashMap();
				execUrlParMap.put(ObjectsTreeConstants.ACTION, "GET_TREND");
				if (r!=null){
					execUrlParMap.put("RESOURCE_ID",r.getId()!=null ? r.getId().toString(): "");
					execUrlParMap.put("RESOURCE_NAME", r.getName());
				}
				SourceBean formatSB = ((SourceBean) ConfigSingleton.getInstance().getAttribute("SPAGOBI.DATE-FORMAT-SERVER"));
				String format = (String) formatSB.getAttribute("format");
				SimpleDateFormat f = new SimpleDateFormat();
				f.applyPattern(format);	
				if (d!=null){	
				    String dat = f.format(d);
				    execUrlParMap.put("END_DATE", dat);						
				}
				if (timeRangeFrom!=null && timeRangeTo!=null){
					try{
						Date timeR_From = f.parse(timeRangeFrom);
						Date timeR_To = f.parse(timeRangeTo);
						if (timeR_From.before(timeR_To)){
							execUrlParMap.put("TimeRangeFrom", timeRangeFrom);	
							execUrlParMap.put("TimeRangeTo", timeRangeTo);	
						}
					} catch (ParseException e) {
					    logger.error("ParseException.value=" + value, e);
					}
				}
				if (kpiVal!=null) execUrlParMap.put("KPI_INST_ID",kpiVal.getKpiInstanceId()!=null ? kpiVal.getKpiInstanceId().toString():"");
				
				execUrlParMap.put("LIGHT_NAVIGATOR_DISABLED", "true");
				
				String trend = msgBuilder.getMessage("sbi.kpi.trend", httpReq);
				//String trendPopupUrl = urlBuilder.getUrl(httpRequest, execUrlParMap);
				String trendPopupUrl = createUrl_popup(execUrlParMap,userId);
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
				String parameters = "";
				parameters +="EXECUTION_FLOW_ID="+executionFlowId;
				parameters +="&SOURCE_EXECUTION_ID="+uuidPrincip;
				parameters +="&KPI_VALUE_ID="+(kpiVal.getKpiValueId()!=null? kpiVal.getKpiValueId().toString():"-1");				
				
				if (r!=null){
					parameters +="&ParKpiResource="+r.getName();
				}
				SourceBean formatSB = ((SourceBean) ConfigSingleton.getInstance().getAttribute("SPAGOBI.DATE-FORMAT-SERVER"));
				String format = (String) formatSB.getAttribute("format");
				SimpleDateFormat f = new SimpleDateFormat();
				f.applyPattern(format);
				
				if (d!=null){						
				    String dat = f.format(d);
				    parameters +="&ParKpiDate="+dat;
				}
				if (timeRangeFrom!=null && timeRangeTo!=null){
					try {
						Date timeR_From = f.parse(timeRangeFrom);
						Date timeR_To = f.parse(timeRangeTo);
						if (timeR_From.before(timeR_To)){
							 parameters +="&TimeRangeFrom="+timeRangeFrom;
							 parameters +="&TimeRangeTo="+timeRangeTo;
						}
					} catch (ParseException e) {
					    logger.error("ParseException.value=" + value, e);
					}
				}
				String docLinked = msgBuilder.getMessage("sbi.kpi.docLinked", httpReq);
				String docHref="javascript:parent.execCrossNavigation(this.name,'"+docLabel+"','"+parameters+"');";
				_htmlStream.append("<a  title='"+docLinked+"' href=\""+docHref+"\"> <img  src=\""+docImgSrc+"\" alt=\"Attached Document\" /></a>\n");				
			}
			_htmlStream.append("		</div></td>\n");
		}else{
			_htmlStream.append("		<td width='2%' class='kpi_td_right' ><div style='align:left;' class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		logger.debug("Written HTML for Documents linked to the kpi");
		
		if (options.getDisplay_alarm()){
			String alarmControl = msgBuilder.getMessage("sbi.kpi.alarmControl", httpReq);
			if(alarm) _htmlStream.append("		<td width='2%' title='"+alarmControl+"' style='text-align:center;' class='kpi_td_right' ><div class='kpi_div'  ><img  src=\""+alarmImgSrc+"\" alt=\"Kpi under Alarm Control\" /></div></td>\n");
			else _htmlStream.append("		<td width='2%' class='kpi_td_right' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}else{
			_htmlStream.append("		<td width='2%' class='kpi_td_right' ><div class='kpi_div'>&nbsp; &nbsp;</div></td>\n");
		}
		logger.debug("Written HTML for alarm control:");
		
		
	   _htmlStream.append("	</tr>\n");

	   if (children!=null && !children.isEmpty()){
		   children = orderChildren(new ArrayList(),children);
		   recursionLev ++;
		   Iterator childIt = children.iterator();
		   while (childIt.hasNext()){
			   logger.debug("Starting children levels");
			   KpiLine l = (KpiLine)childIt.next();
			   String idTemp = id+"_child"+children.indexOf(l);
			   if (evenLine){			   
				   addItemForTree(idTemp,instanceO,userId,recursionLev,false,httpReq, l,_htmlStream,options);
			   }else{
				   addItemForTree(idTemp,instanceO,userId,recursionLev,true,httpReq, l,_htmlStream,options);
			   }  
		   }
	   } 
	   
	   logger.debug("OUT");
		return _htmlStream;
	}
	
	protected String createUrl_popup(HashMap paramsMap, String userid) {

		String url = GeneralUtilities.getSpagoBIProfileBaseUrl(userid);
		if (paramsMap != null){
			Iterator keysIt = paramsMap.keySet().iterator();
			String paramName = null;
			Object paramValue = null;
			while (keysIt.hasNext()){
				paramName = (String)keysIt.next();
				paramValue = paramsMap.get(paramName); 
				url += "&"+paramName+"="+paramValue.toString();
			}
		}
		return url;
	}
	
	protected List orderChildren(List ordered, List notordered) {
		//Arrays.fill s = children ;
		List toReturn = ordered;
		List temp = new ArrayList();
		KpiLine l = null;
		if(notordered!=null && !notordered.isEmpty()){
			Iterator it = notordered.iterator();
			while(it.hasNext()){
				KpiLine k = (KpiLine)it.next();
				if(l==null){
					l = k ;
				}else{
					if (k!=null && k.compareTo(l)<=0){
						temp.add(l);
						l = k;
					}else{
						temp.add(k);
					}
				}
			}
			toReturn.add(l);
			toReturn = orderChildren(toReturn,temp);
		}
		return toReturn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	
}
