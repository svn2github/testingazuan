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
package it.eng.qbe.geo.action;

import it.eng.qbe.geo.configuration.Constants;
import it.eng.qbe.geo.configuration.DatamartProviderConfiguration;
import it.eng.qbe.geo.configuration.MapConfiguration;
import it.eng.qbe.geo.configuration.DatamartProviderConfiguration.Hierarchy;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.List;

/**
 * @author Andrea Gioia
 * 
 */
public class GeoAbstractAction extends AbstractQbeEngineAction {
	
	MapConfiguration mapConfiguration;
	DataMartModel datamartModel;
	ISingleDataMartWizardObject queryWizard;
	
	
	public void service(SourceBean request, SourceBean response) {
		super.service(request, response);
		
		RequestContainer requestContainer = getRequestContainer();
		SessionContainer sessionContainer = requestContainer.getSessionContainer();
		
		queryWizard = Utils.getWizardObject(sessionContainer);
		datamartModel = (DataMartModel) sessionContainer.getAttribute("dataMartModel");
		
		
		mapConfiguration = (MapConfiguration) sessionContainer.getAttribute("MAP_CONFIGURATION");
		if (mapConfiguration == null) {
			mapConfiguration = new MapConfiguration();
			DatamartProviderConfiguration datamartProviderConfiguration = mapConfiguration.getDatamartProviderConfiguration();
			
			MapCatalogueAccessUtils mapCatalogueClient = null;
			mapCatalogueClient = (MapCatalogueAccessUtils)getRequestContainer().getSessionContainer().getAttribute("MAP_CATALOGUE_CLIENT");
			
			DatamartProviderConfiguration.Hierarchy hierarchy = null;
			if(mapCatalogueClient == null) {
				hierarchy = new DatamartProviderConfiguration.Hierarchy("Standard", null);
			} else {
				try {
					hierarchy = getStandardHierarchy(mapCatalogueClient);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			datamartProviderConfiguration.addHieararchy(hierarchy);
			
			String connectionName = datamartModel.getDataSource().getName();
			datamartProviderConfiguration.setConnectionName(connectionName);
			
			
			sessionContainer.setAttribute("MAP_CONFIGURATION", mapConfiguration);
		}

		
	}
	
	private DatamartProviderConfiguration.Hierarchy getStandardHierarchy(MapCatalogueAccessUtils mapCatalogueClient) 
	throws Exception {
		
		DatamartProviderConfiguration.Hierarchy hierarchy;
		
		String sdtHierarchy = mapCatalogueClient.getStandardHierarchy(getHttpSession(), getUserId());
		SourceBean hierarchySB = SourceBean.fromXMLString(sdtHierarchy);	
		String name = (String)hierarchySB.getAttribute(Constants.HIERARCHY_NAME_ATRR);
		String table = (String)hierarchySB.getAttribute(Constants.HIERARCHY_TABLE_ATRR);
		hierarchy = new Hierarchy(name, table);
		List levels = hierarchySB.getAttributeAsList(Constants.HIERARCHY_LEVEL_TAG);
		for(int j = 0; j < levels.size(); j++) {
			SourceBean levelSB = (SourceBean)levels.get(j);
			String lname = (String)levelSB.getAttribute(Constants.HIERARCHY_LEVEL_NAME_ATRR);
			String lcolumnid = (String)levelSB.getAttribute(Constants.HIERARCHY_LEVEL_COLUMN_ID_ATRR);
			String lcolumndesc = (String)levelSB.getAttribute(Constants.HIERARCHY_LEVEL_COLUMN_DESC_ATRR);
			String lfeaturename = (String)levelSB.getAttribute(Constants.HIERARCHY_LEVEL_FEATURE_NAME_ATRR);
			Hierarchy.Level level = new Hierarchy.Level();
			level.setName(lname);
			level.setColumnId(lcolumnid);
			level.setColumnDesc(lcolumndesc);
			level.setFeatureName(lfeaturename);
			hierarchy.addLevel(level);
		}
		
		return hierarchy;
	}
}
