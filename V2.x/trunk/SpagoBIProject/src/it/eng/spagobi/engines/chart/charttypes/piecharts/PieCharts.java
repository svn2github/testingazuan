package it.eng.spagobi.engines.chart.charttypes.piecharts;

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
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;

public class PieCharts extends ChartImpl {

	Map confParameters;
	boolean percentage=false;





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

		if(confParameters.get("percentage")!=null){	
			String percent=(String)confParameters.get("percentage");
			if(percent.equalsIgnoreCase("true"))percentage=true;
			else percentage=false;
		}
		else
		{
			percentage=false;
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

		public JFreeChart createChart(String chartTitle, Dataset dataset) {
			// TODO Auto-generated method stub
			return super.createChart(chartTitle, dataset);



		}

		public Dataset calculateValue() throws SourceBeanException {
			String res=LovAccessFunctions.getLovResult(profile, getDataLov());

			SourceBean sbRows=SourceBean.fromXMLString(res);
			SourceBean sbRow=(SourceBean)sbRows.getAttribute("ROW");
			List listAtts=sbRow.getContainedAttributes();
			DefaultPieDataset dataset = new DefaultPieDataset();
			for (Iterator iterator = listAtts.iterator(); iterator.hasNext();) {
				SourceBeanAttribute att = (SourceBeanAttribute) iterator.next();
				String name=att.getKey();
				String valueS=(String)att.getValue();

				//try Double and Integer Conversion

				Double valueD=null;
				try{
					valueD=Double.valueOf(valueS);
				}
				catch (Exception e) {}

				Integer valueI=null;
				if(valueD==null){
					valueI=Integer.valueOf(valueS);
				}

				if(name!=null && valueD!=null){
					dataset.setValue(name, valueD);
				}
				else if(name!=null && valueI!=null){
					dataset.setValue(name, valueI);
				}

			}

			return dataset;
		}



		public Map getConfParameters() {
			return confParameters;
		}

		public void setConfParameters(Map confParameters) {
			this.confParameters = confParameters;
		}
		public boolean isPercentage() {
			return percentage;
		}

		public void setPercentage(boolean percentage) {
			this.percentage = percentage;
		}



	}
