package it.eng.spagobi.engines.chart.charttypes.piecharts;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.engines.chart.ChartImpl;
import it.eng.spagobi.engines.chart.charttypes.utils.LovAccessFunctions;

import java.util.Iterator;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;

public class PieCharts extends ChartImpl {

	public void configureChart(SourceBean content) {
		super.configureChart(content);
		
	}

	public JFreeChart createChart(String chartTitle, Dataset dataset) {
		// TODO Auto-generated method stub
		return super.createChart(chartTitle, dataset);
		
		
		
		
	}

	public Dataset calculateValue() throws SourceBeanException {
		String res=LovAccessFunctions.getLovResult(profile, getDataName());

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
	
	
}
