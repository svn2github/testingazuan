package it.eng.spagobi.engines.chart.bo.charttypes.utils;

import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;

public class MyCategoryUrlGenerator extends StandardCategoryURLGenerator{

	
	public String generateURL(CategoryDataset dataset, int series, int category) {
		// TODO Auto-generated method stub
		String URL=super.generateURL(dataset, series, category);
	URL=URL+"');";
	return URL;
	}

	public MyCategoryUrlGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyCategoryUrlGenerator(String prefix, String seriesParameterName,
			String categoryParameterName) {
		super(prefix, seriesParameterName, categoryParameterName);
		// TODO Auto-generated constructor stub
	}

	public MyCategoryUrlGenerator(String prefix) {
		super(prefix);
		// TODO Auto-generated constructor stub
	}

	
	
}
