package it.eng.spagobi.engines.chart.bo.charttypes.utils;

import org.apache.log4j.Logger;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;

public class MyPieUrlGenerator extends StandardPieURLGenerator{

	private String categoryUrlLabel="catergory";
	private boolean document_composition=false;
	private static transient Logger logger=Logger.getLogger(MyPieUrlGenerator.class);


	public String generateURL(PieDataset dataset, Comparable key, int pieIndex) {
		logger.debug("IN");

		String URL=super.generateURL(dataset, key, 0);

		if(categoryUrlLabel==null){categoryUrlLabel="category";}
		else{
			String replace=categoryUrlLabel+"=";
			URL=replaceParameter(URL, replace);

		}
		String categoryRep=categoryUrlLabel+"=";
		if(URL.contains("category=")){
			URL=URL.replace("category=", (categoryRep));
		}

		if(document_composition){
			URL=URL+"');";
		}

		logger.debug("OUT");
		return URL;
	}

	public MyPieUrlGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
	public MyPieUrlGenerator(String prefix, String categoryParameterName,
			String indexParameterName) {
		super(prefix, categoryParameterName, indexParameterName);
		// TODO Auto-generated constructor stub
	}

	public MyPieUrlGenerator(String prefix, String categoryParameterName) {
		super(prefix, categoryParameterName);
		// TODO Auto-generated constructor stub
	}

	public MyPieUrlGenerator(String prefix) {
		super(prefix);
		// TODO Auto-generated constructor stub
	}


	public String getCategoryUrlLabel() {
		return categoryUrlLabel;
	}

	public void setCategoryUrlLabel(String categoryUrlLabel) {
		this.categoryUrlLabel = categoryUrlLabel;
	}

	public boolean isDocument_composition() {
		return document_composition;
	}

	public void setDocument_composition(boolean document_composition) {
		this.document_composition = document_composition;
	}



	private String replaceParameter(String URL, String replace){
		logger.debug("IN");
		// if there is already a parameter named like serieUrlLabel delete it

		if(URL.contains(replace)){
			int startIndex=URL.indexOf(replace);
			int otherStart=URL.lastIndexOf(replace);
			if(startIndex!=otherStart){ //menas that there are more occurrence of the same parameter... ERROR
				logger.error("Too many occurrence of the same parameter defined in template");
				return null;
			}
			else{

				int endIndex=URL.indexOf('&', startIndex);

				String delete="";
				if(!(endIndex==-1)){
					delete=URL.substring(startIndex, endIndex);
				}
				else{
					delete=URL.substring(startIndex, URL.length());
				}

				char before=URL.charAt(startIndex-1);


				if(URL.contains("?"+delete+"&")){ // in this case delete the & after
					URL=URL.replaceAll((delete+"&"), "");
				}
				else if(URL.contains("&"+delete)) 
				{
					URL=URL.replaceAll(("&"+delete), "");
				}
				else{
					URL=URL.replaceAll(delete, "");
				}
			}
		}
		logger.debug("OUT");
		return URL;
	}






}
