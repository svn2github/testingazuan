package it.eng.spagobi.engines.kpi.bo;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.KpiInterval;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.Meter;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.SimpleDial;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.Speedometer;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.Thermometer;
import it.eng.spagobi.engines.kpi.utils.DataSetAccessFunctions;
import it.eng.spagobi.engines.kpi.utils.DatasetMap;
import it.eng.spagobi.engines.kpi.utils.StyleLabel;
import it.eng.spagobi.kpi.threshold.bo.Threshold;

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
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;

public class ChartImpl {
	
	protected String name=null;
	protected String subName=null;
	protected Integer width;
	protected Integer height;
	protected IEngUserProfile profile;
	protected Color color;
	protected Boolean legend=true;
	private static transient Logger logger=Logger.getLogger(ChartImpl.class);
	protected StyleLabel styleTitle;
	protected StyleLabel styleSubTitle;
	
	protected DefaultValueDataset dataset ;
	
	protected Vector intervals ;	
	protected double lower=0.0;
	protected double upper=0.0;

	/**
	 * This function creates the chart object.
	 * 
	 * @param chartTitle the chart title
	 * @param dataset the dataset
	 * 
	 * @return the j free chart
	 */
	public JFreeChart createChart() {
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
	public static ChartImpl createChart(String subtype){
		ChartImpl sbi=null;
			if(subtype.equalsIgnoreCase("Speedometer")){
				sbi=new Speedometer();
			}
			else if(subtype.equalsIgnoreCase("SimpleDial")){
				sbi= new SimpleDial();
			}
			else if(subtype.equalsIgnoreCase("Thermometer")){
				sbi= new Thermometer();
			}
			else if(subtype.equalsIgnoreCase("Meter")){
				sbi= new Meter();
			}
		return sbi;
	}

	public void configureChart(HashMap config) {
		
		name = (String) config.get("name");
		subName =(String) config.get("subName");
		styleTitle = (StyleLabel)config.get("styleTitle");
		styleSubTitle =(StyleLabel) config.get("styleSubTitle");
		color = (Color) config.get("color");
		width = (Integer) config.get("width");
		height = (Integer) config.get("height");
		legend = (Boolean) config.get("legend");	
	}
	
	public void setValueDataSet(Double valueToRepresent){
		this.dataset = new DefaultValueDataset(valueToRepresent);
	}
	
	public DefaultValueDataset getValueDataSet(){
		return this.dataset ;
	}

	
	public void setThresholds(List thresholds) {
		if(thresholds!=null && !thresholds.isEmpty()){
			Iterator it = thresholds.iterator();
			//TODO testare con min da solo o max da solo
			while(it.hasNext()){
				Threshold t = (Threshold)it.next();
				Double min = t.getMinValue();
				Double max = t.getMaxValue();
				String label = t.getLabel();
				Color c = t.getColor();
				if (min.doubleValue()<lower){
					lower = min.doubleValue();
				}
				if (max.doubleValue()>upper){
					upper = max.doubleValue();
				}
				KpiInterval interval = new KpiInterval();
				if(color!=null)	{
					interval.setColor(color);
				}else{
					interval.setColor(Color.WHITE);
				}
				if(label!=null)	{
					interval.setLabel(label);
				}else{
					interval.setLabel("");
				}
				
				if(max!=null)	{
					interval.setMax(max);
				}else{
					interval.setMax(upper);
				}
				
				if(min!=null)	{
					interval.setMin(min);
				}else{
					interval.setMin(lower);
				}
				
				intervals.add(interval);
			}
		}
		
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

}
