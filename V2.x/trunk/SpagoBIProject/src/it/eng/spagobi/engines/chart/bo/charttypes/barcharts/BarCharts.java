package it.eng.spagobi.engines.chart.bo.charttypes.barcharts;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.engines.chart.bo.ChartImpl;
import it.eng.spagobi.engines.chart.utils.LovAccessFunctions;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

public class BarCharts extends ChartImpl {

	Map confParameters;
	String categoryLabel="";
	String valueLabel="";
	Integer numberCatVisualization=null;
	HashMap colorMap=null;  // keeps user selected colors
	int categoriesNumber=0;
	HashMap categories;

	public Dataset calculateValue() throws SourceBeanException {
		String res=LovAccessFunctions.getLovResult(profile, getDataLov());
		categories=new HashMap();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		SourceBean sbRows=SourceBean.fromXMLString(res);
		List listAtts=sbRows.getAttributeAsList("ROW");


		// run all categories (one for each row)
		categoriesNumber=0;
		//categories.put(new Integer(0), "All Categories");
		for (Iterator iterator = listAtts.iterator(); iterator.hasNext();) {
			SourceBean category = (SourceBean) iterator.next();
			List atts=category.getContainedAttributes();

			HashMap series=new HashMap();
			String catValue="";

			String name="";
			String value="";

			//run all the attributes, to define series!
			for (Iterator iterator2 = atts.iterator(); iterator2.hasNext();) {
				SourceBeanAttribute object = (SourceBeanAttribute) iterator2.next();

				name=new String(object.getKey());
				value=new String((String)object.getValue());
				if(name.equalsIgnoreCase("x"))
				{
					catValue=value;
					categories.put(new Integer(categoriesNumber),value);
					categoriesNumber=categoriesNumber+1;
					
				}
				else {
					series.put(name, value);
				}
			}
			for (Iterator iterator3 = series.keySet().iterator(); iterator3.hasNext();) {
				String nameS = (String) iterator3.next();
				String valueS=(String)series.get(nameS);
				dataset.addValue(Double.valueOf(valueS).doubleValue(), nameS, catValue);
			}

		}

		return dataset;
	}

	public Dataset calculateValue(String cat) throws SourceBeanException {
		String res=LovAccessFunctions.getLovResult(profile, getDataLov());

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		SourceBean sbRows=SourceBean.fromXMLString(res);
		List listAtts=sbRows.getAttributeAsList("ROW");


		// run all categories (one for each row)
		categoriesNumber=0;
		for (Iterator iterator = listAtts.iterator(); iterator.hasNext();) {
			SourceBean category = (SourceBean) iterator.next();
			List atts=category.getContainedAttributes();

			HashMap series=new HashMap();
			String catValue="";

			String name="";
			String value="";

			//run all the attributes, to define series!
			for (Iterator iterator2 = atts.iterator(); iterator2.hasNext();) {
				SourceBeanAttribute object = (SourceBeanAttribute) iterator2.next();

				name=new String(object.getKey());
				value=new String((String)object.getValue());
				if(name.equalsIgnoreCase("x"))catValue=value;
				else series.put(name, value);
			}
			for (Iterator iterator3 = series.keySet().iterator(); iterator3.hasNext();) {
				String nameS = (String) iterator3.next();
				String valueS=(String)series.get(nameS);
				dataset.addValue(Double.valueOf(valueS).doubleValue(), nameS, catValue);
				categoriesNumber=categoriesNumber+1;
			}

		}

		return dataset;
	}


	public void configureChart(SourceBean content) {

		super.configureChart(content);
		confParameters = new HashMap();
		SourceBean confSB = (SourceBean)content.getAttribute("CONF");

		if(confSB==null) return;
		List confAttrsList = confSB.getAttributeAsList("PARAMETER");

		Iterator confAttrsIter = confAttrsList.iterator();
		while(confAttrsIter.hasNext()) {
			SourceBean param = (SourceBean)confAttrsIter.next();
			String nameParam = (String)param.getAttribute("name");
			String valueParam = (String)param.getAttribute("value");
			confParameters.put(nameParam, valueParam);
		}	

		if(confParameters.get("categorylabel")!=null){	
			categoryLabel=(String)confParameters.get("categorylabel");
		}
		else
		{
			categoryLabel="category";
		}

		if(confParameters.get("valuelabel")!=null){	
			valueLabel=(String)confParameters.get("valuelabel");
		}
		else
		{
			valueLabel="values";
		}
		
		if(confParameters.get("numbercatvisualization")!=null){	
			String nu=(String)confParameters.get("numbercatvisualization");
		numberCatVisualization=Integer.valueOf(nu);
		}
		else
		{
			numberCatVisualization=new Integer(1);
		}

		//reading series colors if present
		SourceBean colors = (SourceBean)content.getAttribute("CONF.SERIESCOLORS");
		if(colors!=null){
			colorMap=new HashMap();
			List atts=colors.getContainedAttributes();
			String colorNum="";
			String colorSerie="";
			String num="";
			for (Iterator iterator = atts.iterator(); iterator.hasNext();) {
				SourceBeanAttribute object = (SourceBeanAttribute) iterator.next();
				colorNum=new String(object.getKey());
				num=colorNum.substring(5, colorNum.length()); // gets the number from color1, color2 

				colorSerie=new String((String)object.getValue());
				Color col=new Color(Integer.decode(colorSerie).intValue());
				if(col!=null){
					colorMap.put(num,col); 
				}
			}		

		}

	}

	public Map getConfParameters() {
		return confParameters;
	}

	public void setConfParameters(Map confParameters) {
		this.confParameters = confParameters;
	}

	public JFreeChart createChart(String chartTitle, Dataset dataset) {
		// TODO Auto-generated method stub
		return super.createChart(chartTitle, dataset);
	}

	public String getCategoryLabel() {
		return categoryLabel;
	}

	public void setCategoryLabel(String categoryLabel) {
		this.categoryLabel = categoryLabel;
	}

	public String getValueLabel() {
		return valueLabel;
	}

	public void setValueLabel(String valueLabel) {
		this.valueLabel = valueLabel;
	}

	public int getCategoriesNumber() {
		return categoriesNumber;
	}

	public void setCategoriesNumber(int categoriesNumber) {
		this.categoriesNumber = categoriesNumber;
	}

	public Map getCategories() {
		return categories;
	}

	public Dataset filterDataset(Dataset dataset, String colKey) {
		DefaultCategoryDataset catDataset=(DefaultCategoryDataset)dataset;
		
		DefaultCategoryDataset newDataSet=new DefaultCategoryDataset();
		try {
			newDataSet=(DefaultCategoryDataset)catDataset.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List columns=new Vector(newDataSet.getColumnKeys());
			for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
			String col = (String) iterator.next();
				if(!(col.equals(colKey))){
					newDataSet.removeColumn(col);
				}			
			}

			return newDataSet;
	
	}

	public Integer getNumberCatVisualization() {
		return numberCatVisualization;
	}

	public void setNumberCatVisualization(Integer numberCatVisualization) {
		this.numberCatVisualization = numberCatVisualization;
	}

	
	
}
