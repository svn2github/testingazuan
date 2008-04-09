/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;

/** 
 *  * @author Giulio Gavardi
 *     giulio.gavardi@eng.it
 */

/** Extends StandardCategoryURLGenerator: the purpose of this class is to add a postfix in the case of document-composition and to substitute in the URL
 * serie anc category parameter, addedd by JFreeCharts function, with label chosen by the user
 */


public class MyCategoryUrlGenerator extends StandardCategoryURLGenerator{

	
	private String serieUrlLabel="series";
	private String categoryUrlLabel="catergory";
	private boolean document_composition=false;
	private static transient Logger logger=Logger.getLogger(MyCategoryUrlGenerator.class);


	public String generateURL(CategoryDataset dataset, int series, int category) {
logger.debug("IN");
		String URL=super.generateURL(dataset, series, category);


		if(serieUrlLabel==null){serieUrlLabel="series";}
		else{
			String replace=serieUrlLabel+"=";
			URL=replaceParameter(URL, replace);

		}
		if(categoryUrlLabel==null){categoryUrlLabel="category";}
		else{
			String replace=categoryUrlLabel+"=";
			URL=replaceParameter(URL, replace);

		}

		String seriesRep=serieUrlLabel+"=";
		String categoryRep=categoryUrlLabel+"=";

		if(URL.contains("series=")){
			URL=URL.replace("series=", (seriesRep));
		}
		if(URL.contains("category=")){
			URL=URL.replace("category=", (categoryRep));
		}
		
		if(document_composition){
			URL=URL+"');";
		}

		logger.debug("OUT");
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

	public boolean isDocument_composition() {
		return document_composition;
	}

	public void setDocument_composition(boolean document_composition) {
		this.document_composition = document_composition;
	}



	private String replaceParameter(String URL, String replace ){
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
