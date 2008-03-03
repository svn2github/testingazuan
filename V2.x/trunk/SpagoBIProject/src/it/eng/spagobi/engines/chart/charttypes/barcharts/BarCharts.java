package it.eng.spagobi.engines.chart.charttypes.barcharts;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.engines.chart.ChartImpl;
import it.eng.spagobi.engines.chart.charttypes.utils.LovAccessFunctions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

public class BarCharts extends ChartImpl {

	Map confParameters;



	public Dataset calculateValue() throws SourceBeanException {
		String res=LovAccessFunctions.getLovResult(profile, getDataLov());


		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		SourceBean sbRows=SourceBean.fromXMLString(res);
		List listAtts=sbRows.getAttributeAsList("ROW");


		// run all categories (one for each row)
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
		
		if(confParameters.get("linkable")!=null){	
			String link=(String)confParameters.get("linkable");
			if(link.equalsIgnoreCase("true"))setLinkable(true);
			else setLinkable(false);
		}
		else
		{
			setLinkable(false);
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

}
