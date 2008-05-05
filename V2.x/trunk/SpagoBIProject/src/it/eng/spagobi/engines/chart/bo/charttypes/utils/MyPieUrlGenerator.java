/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.engines.chart.bo.charttypes.utils;

import org.apache.log4j.Logger;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;

public class MyPieUrlGenerator extends StandardPieURLGenerator{

	private String categoryUrlLabel="catergory";
	private boolean document_composition=false;
	private static transient Logger logger=Logger.getLogger(MyPieUrlGenerator.class);


	/* (non-Javadoc)
	 * @see org.jfree.chart.urls.StandardPieURLGenerator#generateURL(org.jfree.data.general.PieDataset, java.lang.Comparable, int)
	 */
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

	/**
	 * Instantiates a new my pie url generator.
	 */
	public MyPieUrlGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
	/**
	 * Instantiates a new my pie url generator.
	 * 
	 * @param prefix the prefix
	 * @param categoryParameterName the category parameter name
	 * @param indexParameterName the index parameter name
	 */
	public MyPieUrlGenerator(String prefix, String categoryParameterName,
			String indexParameterName) {
		super(prefix, categoryParameterName, indexParameterName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new my pie url generator.
	 * 
	 * @param prefix the prefix
	 * @param categoryParameterName the category parameter name
	 */
	public MyPieUrlGenerator(String prefix, String categoryParameterName) {
		super(prefix, categoryParameterName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new my pie url generator.
	 * 
	 * @param prefix the prefix
	 */
	public MyPieUrlGenerator(String prefix) {
		super(prefix);
		// TODO Auto-generated constructor stub
	}


	/**
	 * Gets the category url label.
	 * 
	 * @return the category url label
	 */
	public String getCategoryUrlLabel() {
		return categoryUrlLabel;
	}

	/**
	 * Sets the category url label.
	 * 
	 * @param categoryUrlLabel the new category url label
	 */
	public void setCategoryUrlLabel(String categoryUrlLabel) {
		this.categoryUrlLabel = categoryUrlLabel;
	}

	/**
	 * Checks if is document_composition.
	 * 
	 * @return true, if is document_composition
	 */
	public boolean isDocument_composition() {
		return document_composition;
	}

	/**
	 * Sets the document_composition.
	 * 
	 * @param document_composition the new document_composition
	 */
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
