/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.engines.geo.map.renderer.configurator;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.map.renderer.InteractiveMapRenderer;
import it.eng.spagobi.engines.geo.map.renderer.LabelProducer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class InteractiveMapRendererConfigurator.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class InteractiveMapRendererConfigurator {
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(InteractiveMapRendererConfigurator.class);
	
	
	/**
	 * Configure.
	 * 
	 * @param interactiveMapRenderer the interactive map renderer
	 * @param conf the conf
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	public static void configure(InteractiveMapRenderer interactiveMapRenderer, Object conf) throws GeoEngineException {
		SourceBean confSB = null;
		
		if(conf instanceof String) {
			try {
				confSB = SourceBean.fromXMLString( (String)conf );
			} catch (SourceBeanException e) {
				logger.error("Impossible to parse configuration block for MapRenderer", e);
				throw new GeoEngineException("Impossible to parse configuration block for MapRenderer", e);
			}
		} else {
			confSB = (SourceBean)conf;
		}
		
		if(confSB != null) {
			SourceBean labelsConfigurationSB = (SourceBean)confSB.getAttribute("LABELS");
			if(labelsConfigurationSB != null) {
				Map labelProducers = getLabelProducers(labelsConfigurationSB);
				interactiveMapRenderer.setLabelProducers(labelProducers);				
			}	
		}
	}
	
	/**
	 * Gets the label producers.
	 * 
	 * @param labelsConfigurationSB the labels configuration sb
	 * 
	 * @return the label producers
	 */
	public static Map getLabelProducers(SourceBean labelsConfigurationSB) {
		Map labelProducers = new HashMap();
		List labelList = labelsConfigurationSB.getAttributeAsList("LABEL");
		Iterator labelIterator = labelList.iterator();
		while( labelIterator.hasNext() ) {
			SourceBean label = (SourceBean)labelIterator.next();
			String position = (String)label.getAttribute("position");
			String clazz = (String)label.getAttribute("class_name");
			
			LabelProducer labelProducer = null;
			try {
				labelProducer = (LabelProducer) Class.forName(clazz).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(labelProducer != null) {
				labelProducer.init(label); 
				labelProducers.put(position, labelProducer);
			}
		}
		
		return labelProducers;
	}
}
