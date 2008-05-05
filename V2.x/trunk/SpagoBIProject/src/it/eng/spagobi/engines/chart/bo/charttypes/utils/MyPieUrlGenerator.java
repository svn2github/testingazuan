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

import it.eng.spagobi.commons.constants.ObjectsTreeConstants;

import org.apache.log4j.Logger;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;

public class MyPieUrlGenerator extends StandardPieURLGenerator{

	private String categoryUrlLabel="catergory";
	private boolean document_composition=false;
	private static transient Logger logger=Logger.getLogger(MyPieUrlGenerator.class);
	private String URL=null;

	/* (non-Javadoc)
	 * @see org.jfree.chart.urls.StandardPieURLGenerator#generateURL(org.jfree.data.general.PieDataset, java.lang.Comparable, int)
	 */
	public String generateURL(PieDataset dataset, Comparable key, int pieIndex) {
		logger.debug("IN");
		URL=new String();
		URL=super.generateURL(dataset, key, 0);

		// take the categoryUrlLabel, default is "category"
		if(categoryUrlLabel==null){categoryUrlLabel="category";}
		String categoryToMove=replaceAndGetParameter("category=", categoryUrlLabel);

		// this is the string to move inside PARAMETERS=
		String toMove=categoryToMove;

		// insert into PARAMETERS=
		String parameters=ObjectsTreeConstants.PARAMETERS;
		URL=URL.replaceAll(parameters+"=", parameters+"="+toMove);

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

	
	

	private String replaceAndGetParameter(String toReplace, String replacer){
		// toReplace, series= or category=

		// Start index to substitute, check there is only one
		int startIndex=URL.indexOf(toReplace);
		int otherStart=URL.lastIndexOf(toReplace);
		if(startIndex!=otherStart){ //menas that there are more occurrence of the same parameter... ERROR
			logger.error("Too many occurrence of the same parameter defined in template");
			return null;
		}
		//end index of thing to substitute
		int endIndex=URL.indexOf('&', startIndex);

		// if there is no end index that is the end of the string
		if(endIndex==-1)endIndex=URL.length();
		String toMove=URL.substring(startIndex, endIndex);

		URL=URL.replaceAll("&amp;"+toMove, "");

			toMove=toMove.replaceAll("category", replacer);	


		toMove=toMove.replaceAll("=", "%253");
		toMove="%2526"+toMove;


		return toMove;


	}
	
}
