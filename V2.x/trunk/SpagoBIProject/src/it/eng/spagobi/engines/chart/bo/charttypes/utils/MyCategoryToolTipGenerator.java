package it.eng.spagobi.engines.chart.bo.charttypes.utils;

import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.CombinedCategoryBar;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.data.category.CategoryDataset;

public class MyCategoryToolTipGenerator extends StandardCategoryToolTipGenerator {

	boolean enableFreeTip=false;
	HashMap<String, String> categoriesToolTips=null;
	HashMap<String, String> serieToolTips=null;
	private static transient Logger logger=Logger.getLogger(MyCategoryToolTipGenerator.class);


	public MyCategoryToolTipGenerator(boolean _enableFreeTip, HashMap<String, String> _serieToolTips, HashMap<String, String> _categoriesToolTips) {
		logger.debug("IN");
		enableFreeTip=_enableFreeTip;
		serieToolTips=_serieToolTips;
		categoriesToolTips=_categoriesToolTips;
		logger.debug("OUT");
	}


	public String generateToolTip(CategoryDataset dataset, int row, int column) {
		logger.debug("IN");
		//String tooltip=super.generateToolTip(dataset, row, column);
		String rowName="";
		String columnName="";
		try{
			Comparable rowNameC=(String)dataset.getRowKey(row);
			Comparable columnNameC=(String)dataset.getColumnKey(column);
			if(rowNameC!=null)rowName=rowNameC.toString();
			if(columnNameC!=null)columnName=columnNameC.toString();

		}
		catch (Exception e) {
			logger.error("error in recovering name of row and column");
			return "undef";
		}

		// check if there is a predefined FREETIP message
		if(enableFreeTip==true){
			if(categoriesToolTips.get("FREETIP_x_"+columnName)!=null){
				String freeName=categoriesToolTips.get("FREETIP_x_"+columnName);
				return freeName;
			}
		}

		String columnTipName=columnName;
		String rowTipName=rowName;
		// check if tip name are defined, else use standard
		if(categoriesToolTips.get("TIP_x_"+columnName)!=null){
			columnTipName=categoriesToolTips.get("TIP_x_"+columnName);
		}

		if(serieToolTips.get("TIP_"+rowName)!=null){
			rowTipName=serieToolTips.get("TIP_"+rowName);
		}

		Number num=dataset.getValue(row, column);
		String numS=(num!=null)? " = "+num.toString() : "";
		String toReturn="("+columnTipName+", "+rowTipName+")"+numS;

		logger.debug("OUT");
		return toReturn;
		

	}



}
