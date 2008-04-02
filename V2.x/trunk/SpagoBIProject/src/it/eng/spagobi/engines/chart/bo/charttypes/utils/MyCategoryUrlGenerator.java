package it.eng.spagobi.engines.chart.bo.charttypes.utils;

import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;

public class MyCategoryUrlGenerator extends StandardCategoryURLGenerator{

	private String serieUrlLabel="series";
	private String categoryUrlLabel="catergory";


	public String generateURL(CategoryDataset dataset, int series, int category) {
		// TODO Auto-generated method stub
		String URL=super.generateURL(dataset, series, category);
		URL=URL+"');";

		if(serieUrlLabel==null){serieUrlLabel="series";}
		if(categoryUrlLabel==null){categoryUrlLabel="category";}
		
		String seriesRep=serieUrlLabel+"=";
		String categoryRep=categoryUrlLabel+"=";

		if(URL.contains("series=")){
			URL=URL.replace("series=", (seriesRep));
		}
		if(URL.contains("category=")){
			URL=URL.replace("category=", (categoryRep));
		}

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

	public String getSerieUrlLabel() {
		return serieUrlLabel;
	}

	public void setSerieUrlLabel(String serieUrlLabel) {
		this.serieUrlLabel = serieUrlLabel;
	}

	public String getCategoryUrlLabel() {
		return categoryUrlLabel;
	}

	public void setCategoryUrlLabel(String categoryUrlLabel) {
		this.categoryUrlLabel = categoryUrlLabel;
	}





}
