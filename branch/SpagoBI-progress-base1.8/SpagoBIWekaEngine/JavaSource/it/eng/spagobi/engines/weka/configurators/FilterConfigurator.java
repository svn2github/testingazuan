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
package it.eng.spagobi.engines.weka.configurators;


import org.apache.log4j.Logger;

import weka.clusterers.Clusterer;
import weka.filters.unsupervised.attribute.AddCluster;
import weka.gui.beans.Filter;

/**
 * @author Gioia
 *
 */
public class FilterConfigurator extends AbstractWekaBeanConfigurator{
	
	private static transient Logger logger = Logger.getLogger(FilterConfigurator.class);
	
	static private void log(String msg) {
		//logger.debug("FilterConfigurator:" + msg);
		System.out.println("FilterConfigurator: " + msg);
	}
	
	public void setup(Filter bean)  throws Exception {
		weka.filters.Filter filter =  bean.getFilter();
		String className = filter.getClass().getName();
		log("Class: " + className);
		if(className.equalsIgnoreCase(AddCluster.class.getName())) {
			AddCluster addCluster = (AddCluster)filter;
			StringBuffer buffer = new StringBuffer();
			buffer.append("Options: ");
			String[] options = addCluster.getOptions();		
			for(int i = 0; i < options.length; i++) {
				buffer.append(options[i] + "; ");
			}
			log(buffer.toString());
			Clusterer clusterer = addCluster.getClusterer();
			log(addCluster.getClusterer().getClass().getName());
		}
	}
	
	public void setup(Object bean)  throws Exception {
		if(!bean.getClass().getName().equalsIgnoreCase(Filter.class.getName()))
			throw new Exception();
		setup((Filter)bean);
	}
}
