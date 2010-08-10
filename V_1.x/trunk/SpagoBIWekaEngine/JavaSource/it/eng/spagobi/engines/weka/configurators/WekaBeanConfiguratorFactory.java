/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.weka.configurators;

import weka.gui.beans.Filter;

/**
 * @author Gioia
 *
 */
public class WekaBeanConfiguratorFactory {
	
	private static WekaBeanConfigurator filterConfigurator = new FilterConfigurator();
	
	public static void setup(Object bean) {
		try{
			if(bean.getClass().getName().equalsIgnoreCase(Filter.class.getName()))
				filterConfigurator.setup(bean);	
		} catch(Exception e) {
			e.printStackTrace();
		}
				
	}
}
