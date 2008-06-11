package it.eng.spagobi.engines.chart.bo.charttypes.utils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.HashMap;

import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.category.CategoryDataset;


public class MyStandardCategoryItemLabelGenerator extends StandardCategoryItemLabelGenerator {

	HashMap catSerLabel=null;



	public MyStandardCategoryItemLabelGenerator() {
		super();

		catSerLabel=new HashMap();
	}

	public MyStandardCategoryItemLabelGenerator(HashMap catSerMap) {
		super();

		catSerLabel=catSerMap;
	}

	
	

	public MyStandardCategoryItemLabelGenerator(HashMap catSerMap,String labelFormat,
			DateFormat formatter) {
		super(labelFormat, formatter);
		catSerLabel=catSerMap;
	}

	public MyStandardCategoryItemLabelGenerator(HashMap catSerMap,String labelFormat,
			NumberFormat formatter, NumberFormat percentFormatter) {
		super(labelFormat, formatter, percentFormatter);
		catSerLabel=catSerMap;	}

	public MyStandardCategoryItemLabelGenerator(HashMap catSerMap,String labelFormat,
			NumberFormat formatter) {
		super(labelFormat, formatter);
		catSerLabel=catSerMap;	}

	public String generateLabel(CategoryDataset dataset, int row, int column) {

		String category=(String)dataset.getColumnKey(column);
		String serie=(String)dataset.getRowKey(row);

		String index=category+"-"+serie;

		String value="";
		if(catSerLabel.get(index)!=null && !catSerLabel.get(index).equals("")) 
		{
			value=(String)catSerLabel.get(index);
		}


		return value;
	}

}
