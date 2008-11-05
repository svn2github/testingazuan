package it.eng.spagobi.engines.kpi.bo;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.Meter;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.SimpleDial;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.Speedometer;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.Thermometer;
import it.eng.spagobi.engines.kpi.utils.DataSetAccessFunctions;
import it.eng.spagobi.engines.kpi.utils.DatasetMap;
import it.eng.spagobi.engines.kpi.utils.StyleLabel;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;

public class ChartImpl {
	
	protected int titleDimension;
	protected String name=null;
	protected String subName=null;
	protected int width;
	protected int height;
	protected String data;
	protected String confDataset;
	protected boolean isLovConfDefined;
	protected IEngUserProfile profile;
	protected String type="";
	protected String subtype="";
	protected Color color;
	protected boolean legend=true;
	private static transient Logger logger=Logger.getLogger(ChartImpl.class);
	protected Map parametersObject;
	protected boolean filter=true;
	protected boolean slider=true;
	protected StyleLabel styleTitle;
	protected StyleLabel styleSubTitle;
	protected HashMap seriesLabelsMap = null;
	
	protected double lower=0.0;
	protected double upper=0.0;
	protected Map confParameters;
	protected SourceBean sbRow;

	/**
	 * configureChart reads the content of the template and sets the chart parameters.
	 * 
	 * @param content the content
	 */
	public void configureChart(SourceBean content) {
		logger.debug("IN");
		// common part for all charts
		//setting the title with parameter values if is necessary
		if(content.getAttribute("name")!=null) {
			String titleChart = (String)content.getAttribute("name");
			String tmpTitle = titleChart;
			while (!tmpTitle.equals("")){
				if (tmpTitle.indexOf("$P{") >= 0){
					String parName = tmpTitle.substring(tmpTitle.indexOf("$P{")+3, tmpTitle.indexOf("}"));
					
					String parValue = (parametersObject.get(parName)==null)?"":(String)parametersObject.get(parName);
					parValue = parValue.replaceAll("\'", "");
					
					if(parValue.equals("%")) parValue = "";
					int pos = tmpTitle.indexOf("$P{"+parName+"}") + (parName.length()+4);
					titleChart = titleChart.replace("$P{" + parName + "}", parValue);
					tmpTitle = tmpTitle.substring(pos);
				}
				else
					tmpTitle = "";
			}
			setName(titleChart);
		}
		else setName("");

		SourceBean styleTitleSB = (SourceBean)content.getAttribute("STYLE_TITLE");
		if(styleTitleSB!=null){

			String fontS = (String)content.getAttribute("STYLE_TITLE.font");
			String sizeS = (String)content.getAttribute("STYLE_TITLE.size");
			String colorS = (String)content.getAttribute("STYLE_TITLE.color");


			try{
				Color color=Color.decode(colorS);
				int size=Integer.valueOf(sizeS).intValue();
				styleTitle=new StyleLabel(fontS,size,color);
				
			}
			catch (Exception e) {
				logger.error("Wrong style Title settings, use default");
			}

		}
		
		SourceBean styleSubTitleSB = (SourceBean)content.getAttribute("STYLE_SUBTITLE");
		if(styleSubTitleSB!=null){

			String subTitle = (String)content.getAttribute("STYLE_SUBTITLE.name");
			if(subTitle!=null) {
				String tmpSubTitle = subTitle;
				while (!tmpSubTitle.equals("")){
					if (tmpSubTitle.indexOf("$P{") >= 0){
						String parName = tmpSubTitle.substring(tmpSubTitle.indexOf("$P{")+3, tmpSubTitle.indexOf("}"));
						String parValue = (parametersObject.get(parName)==null)?"":(String)parametersObject.get(parName);
						parValue = parValue.replaceAll("\'", "");
						if(parValue.equals("%")) parValue = "";
						int pos = tmpSubTitle.indexOf("$P{"+parName+"}") + (parName.length()+4);
						subTitle = subTitle.replace("$P{" + parName + "}", parValue);
						tmpSubTitle = tmpSubTitle.substring(pos);
					}
					else
						tmpSubTitle = "";
				}
				setSubName(subTitle);
			}
			else setSubName("");
			
			String fontS = (String)content.getAttribute("STYLE_SUBTITLE.font");
			String sizeS = (String)content.getAttribute("STYLE_SUBTITLE.size");
			String colorS = (String)content.getAttribute("STYLE_SUBTITLE.color");


			try{
				Color color=Color.decode(colorS);
				int size=Integer.valueOf(sizeS).intValue();
				styleSubTitle=new StyleLabel(fontS,size,color);				
			}
			catch (Exception e) {
				logger.error("Wrong style SubTitle settings, use default");
			}

		}


		if(content.getAttribute("title_dimension")!=null) 
		{
			String titleD=((String)content.getAttribute("title_dimension"));
			titleDimension=Integer.valueOf(titleD).intValue();
		}
		else setTitleDimension(18);


		String colS = (String)content.getAttribute("COLORS.background");
		if(colS!=null) 
		{
			Color col=new Color(Integer.decode(colS).intValue());
			if(col!=null){
				setColor(col);}
			else{
				setColor(Color.white);
			}
		}
		else { 	
			setColor(Color.white);
		}

		String widthS = (String)content.getAttribute("DIMENSION.width");
		String heightS = (String)content.getAttribute("DIMENSION.height");
		if(widthS==null || heightS==null){
			logger.warn("Width or height non defined, use default ones");
			widthS="400";
			heightS="300";
		}

		width=Integer.valueOf(widthS).intValue();
		height=Integer.valueOf(heightS).intValue();

		// get all the data parameters 


		try{					
			Map dataParameters = new HashMap();
			SourceBean dataSB = (SourceBean)content.getAttribute("CONF");
			List dataAttrsList = dataSB.getContainedSourceBeanAttributes();
			Iterator dataAttrsIter = dataAttrsList.iterator();
			while(dataAttrsIter.hasNext()) {
				SourceBeanAttribute paramSBA = (SourceBeanAttribute)dataAttrsIter.next();
				SourceBean param = (SourceBean)paramSBA.getValue();
				String nameParam = (String)param.getAttribute("name");
				String valueParam = (String)param.getAttribute("value");
				dataParameters.put(nameParam, valueParam);
			}


			if(dataParameters.get("confdataset")!=null && !(((String)dataParameters.get("confdataset")).equalsIgnoreCase("") )){	
				confDataset=(String)dataParameters.get("confdataset");
				isLovConfDefined=true;
			}
			else {
				isLovConfDefined=false;
			}

			legend=true;
			if(dataParameters.get("legend")!=null && !(((String)dataParameters.get("legend")).equalsIgnoreCase("") )){	
				String leg=(String)dataParameters.get("legend");
				if(leg.equalsIgnoreCase("false"))
					legend=false;
			}

			filter=true;
			if(dataParameters.get("view_filter")!=null && !(((String)dataParameters.get("view_filter")).equalsIgnoreCase("") )){	
				String fil=(String)dataParameters.get("view_filter");
				if(fil.equalsIgnoreCase("false"))
					filter=false;
			}

			slider=true;
			if(dataParameters.get("view_slider")!=null && !(((String)dataParameters.get("view_slider")).equalsIgnoreCase("") )){	
				String sli=(String)dataParameters.get("view_slider");
				if(sli.equalsIgnoreCase("false"))
					slider=false;
			}

			//reading series orders if present
			SourceBean sbSerieLabels = (SourceBean)content.getAttribute("CONF.SERIES_LABELS");
			if(sbSerieLabels!=null){
				seriesLabelsMap=new LinkedHashMap();
				List atts=sbSerieLabels.getContainedAttributes();
				String serieLabel="";
				for (Iterator iterator = atts.iterator(); iterator.hasNext();) {
					SourceBeanAttribute object = (SourceBeanAttribute) iterator.next();
					String serieName=(String)object.getKey();
					serieLabel=new String((String)object.getValue());
					if(serieLabel!=null){
						seriesLabelsMap.put(serieName, serieLabel); 
					}
				}		
			}
			
			logger.debug("Configuration in template");
			confParameters = new HashMap();
			SourceBean confSB = (SourceBean)content.getAttribute("CONF");

			List confAttrsList = confSB.getAttributeAsList("PARAMETER");

			Iterator confAttrsIter = confAttrsList.iterator();
			while(confAttrsIter.hasNext()) {
				SourceBean param = (SourceBean)confAttrsIter.next();
				String nameParam = (String)param.getAttribute("name");
				String valueParam = (String)param.getAttribute("value");
				confParameters.put(nameParam, valueParam);
			}	
			if(confParameters.get("lower")!=null){	
				String lower=(String)confParameters.get("lower");
				setLower(Double.valueOf(lower).doubleValue());
			}
			else {
				logger.error("lower bound not defined");
				throw new Exception("lower bound not defined");
			}
			if(confParameters.get("upper")!=null){	
				String upper=(String)confParameters.get("upper");
				setUpper(Double.valueOf(upper).doubleValue());
			}
			else {
				logger.error("upper bound not defined");
				throw new Exception("upper bound not defined");
			}
			
		}
		catch (Exception e) {
			logger.error("error in reading data source parameters");
		}


	}

	/**
	 * This function creates the chart object.
	 * 
	 * @param chartTitle the chart title
	 * @param dataset the dataset
	 * 
	 * @return the j free chart
	 */

	public JFreeChart createChart(DatasetMap dataset) {
		return null;
	}


	/**
	 * This function creates the object of the right subtype as specified by type and subtype parameters found in template.
	 * 
	 * @param type the type
	 * @param subtype the subtype
	 * 
	 * @return the chart impl
	 */

	public static ChartImpl createChart(String type,String subtype){
		ChartImpl sbi=null;
		if(type.equals("DIALCHART")){
			if(subtype.equalsIgnoreCase("speedometer")){
				sbi=new Speedometer();
			}
			else if(subtype.equalsIgnoreCase("simpledial")){
				sbi= new SimpleDial();
			}
			else if(subtype.equalsIgnoreCase("thermomether")){
				sbi= new Thermometer();
			}
			else if(subtype.equalsIgnoreCase("meter")){
				sbi= new Meter();
			}
		}

		return sbi;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getData()
	 */
	public String getData() {
		return data;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getHeight()
	 */
	public int getHeight() {
		return height;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getWidth()
	 */
	public int getWidth() {
		return width;

	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setData(java.lang.String)
	 */
	public void setData(String _data) {
		data=_data;		
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setHeight(int)
	 */
	public void setHeight(int _height) {
		height=_height;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setName(java.lang.String)
	 */
	public void setName(String _name) {
		name=_name;		
	}
	
	public void setSubName(String _name) {
		subName=_name;		
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setWidth(int)
	 */
	public void setWidth(int _width) {
		width=_width;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#calculateValue()
	 */
	public DatasetMap calculateValue() throws Exception {
		logger.debug("IN");
		String res=DataSetAccessFunctions.getDataSetResultFromId(profile, getData(),parametersObject);
		if (res!=null){
			
			if (name.indexOf("$F{") >= 0){
				List atts=new Vector();
				atts.add(res);
				setTitleParameter(atts);
				}
			
			logger.debug("Dataset result:"+res);
			SourceBean sbRows=SourceBean.fromXMLString(res);
			SourceBean sbRow=(SourceBean)sbRows.getAttribute("ROW");
			String result="";
			if(sbRow==null){
				result=(new Double(lower)).toString();
			}
			else{
				result=(String)sbRow.getAttribute("value");
			}
			DefaultValueDataset dataset = new DefaultValueDataset(Double.valueOf(result));
			logger.debug("OUT");

			DatasetMap datasets=new DatasetMap();
			datasets.addDataset("1",dataset);
			return datasets;		}
		logger.error("dataset is null!!!!!!!!!");
		return null;
	}

	/**
	 * Gets the conf dataset.
	 * 
	 * @return the conf dataset
	 */
	public String getConfDataset() {
		return confDataset;
	}

	/**
	 * Sets the conf dataset.
	 * 
	 * @param confDataset the new conf dataset
	 */
	public void setConfDataset(String confDataset) {
		this.confDataset = confDataset; 
	}

	/**
	 * Gets the profile.
	 * 
	 * @return the profile
	 */
	public IEngUserProfile getProfile() {
		return profile;
	}

	/**
	 * Sets the profile.
	 * 
	 * @param profile the new profile
	 */
	public void setProfile(IEngUserProfile profile) {
		this.profile = profile;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#isLinkable()
	 */
	public boolean isLinkable() {
		return false;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the subtype.
	 * 
	 * @return the subtype
	 */
	public String getSubtype() {
		return subtype;
	}

	/**
	 * Sets the subtype.
	 * 
	 * @param subtype the new subtype
	 */
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#isChangeableView()
	 */
	public boolean isChangeableView() {
		return false;
	}

	/**
	 * Gets the change view label.
	 * 
	 * @param theme the theme
	 * @param i the i
	 * 
	 * @return the change view label
	 */
	public String getChangeViewLabel(String theme, int i) {
		return "";
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setChangeViewChecked(boolean)
	 */
	public void setChangeViewChecked(boolean b) {
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getPossibleChangePars()
	 */
	public List getPossibleChangePars() {
		return new Vector();
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setChangeViewsParameter(java.lang.String, boolean)
	 */
	public void setChangeViewsParameter(String changePar, boolean how) {


	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getChangeViewParameter(java.lang.String)
	 */
	public boolean getChangeViewParameter(String changePar) {
		return false;		
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getChangeViewParameterLabel(java.lang.String, int)
	 */
	public String getChangeViewParameterLabel(String changePar, int i) {
		return null;
	}

	/**
	 * Gets the color.
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 * 
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#filterDataset(org.jfree.data.general.Dataset, java.util.HashMap, int, int)
	 */
	public Dataset filterDataset(Dataset dataset, HashMap categories, int catSelected, int numberCatsVisualization) {

		return null;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#isLegend()
	 */
	public boolean isLegend() {
		return legend;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setLegend(boolean)
	 */
	public void setLegend(boolean legend) {
		this.legend = legend;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getParametersObject()
	 */	
	public Map getParametersObject() {
		return parametersObject;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setParametersObject(java.util.Map)
	 */
	public void setParametersObject(Map parametersObject) {
		this.parametersObject = parametersObject;
	}



	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public boolean isSlider() {
		return slider;
	}

	public void setSlider(boolean slider) {
		this.slider = slider;
	}


	public void setTitleParameter(List atts) {
		try{
			String tmpTitle=new String(name);
			if (tmpTitle.indexOf("$F{") >= 0){
				String parName = tmpTitle.substring(tmpTitle.indexOf("$F{")+3, tmpTitle.indexOf("}"));

				for (Iterator iterator2 = atts.iterator(); iterator2.hasNext();) {
					SourceBeanAttribute object = (SourceBeanAttribute) iterator2.next();

					String nameP=new String(object.getKey());
					String value=new String((String)object.getValue());
					if(nameP.equalsIgnoreCase(parName))
					{
						int pos = tmpTitle.indexOf("$F{"+parName+"}") + (parName.length()+4);
						name = name.replace("$F{" + parName + "}", value);
						tmpTitle = tmpTitle.substring(pos);
					}
				}

			}
		}
		catch (Exception e) {
			logger.error("Error in parameters Title");
		}

	}

	public TextTitle setStyleTitle(String title,StyleLabel titleLabel){
		Font font=null;
		Color color=null;


		boolean definedFont=true;
		boolean definedColor=true;

		if(titleLabel!=null ){
			if(titleLabel.getFont()!=null){
				font=titleLabel.getFont();
			}
			else{
				definedFont=false;
			}
			if(titleLabel.getColor()!=null){
				color=titleLabel.getColor();
			}
			else{
				definedColor=false;
			}
		}
		else{
			definedColor=false;
			definedFont=false;
		}

		if(!definedFont)
			font=new Font("Tahoma", Font.BOLD, 18);
		if(!definedColor)
			color=Color.BLACK;

		TextTitle titleText=new TextTitle(title,font,color, RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.TOP, RectangleInsets.ZERO_INSETS);

		return titleText;
	}
	

	public int getTitleDimension() {
		return titleDimension;
	}

	public void setTitleDimension(int titleDimension) {
		this.titleDimension = titleDimension;
	}

	public HashMap getSeriesLabels() {
		return seriesLabelsMap;
	}

	public void setSeriesLabels(HashMap seriesLabels) {
		this.seriesLabelsMap = seriesLabels;
	}
	
	/**
	 * Gets the lower.
	 * 
	 * @return the lower
	 */
	public double getLower() {
		return lower;
	}

	/**
	 * Sets the lower.
	 * 
	 * @param lower the new lower
	 */
	public void setLower(double lower) {
		this.lower = lower;
	}

	/**
	 * Gets the upper.
	 * 
	 * @return the upper
	 */
	public double getUpper() {
		return upper;
	}

	/**
	 * Sets the upper.
	 * 
	 * @param upper the new upper
	 */
	public void setUpper(double upper) {
		this.upper = upper;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.ChartImpl#isLovConfDefined()
	 */
	public boolean isLovConfDefined() {
		return isLovConfDefined;
	}




	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.ChartImpl#setLovConfDefined(boolean)
	 */
	public void setLovConfDefined(boolean isLovConfDefined) {
		this.isLovConfDefined = isLovConfDefined;
	}




	/**
	 * Gets the conf parameters.
	 * 
	 * @return the conf parameters
	 */
	public Map getConfParameters() {
		return confParameters;
	}




	/**
	 * Sets the conf parameters.
	 * 
	 * @param confParameters the new conf parameters
	 */
	public void setConfParameters(Map confParameters) {
		this.confParameters = confParameters;
	}




	/**
	 * Gets the sb row.
	 * 
	 * @return the sb row
	 */
	public SourceBean getSbRow() {
		return sbRow;
	}




	/**
	 * Sets the sb row.
	 * 
	 * @param sbRow the new sb row
	 */
	public void setSbRow(SourceBean sbRow) {
		this.sbRow = sbRow;
	}



}
