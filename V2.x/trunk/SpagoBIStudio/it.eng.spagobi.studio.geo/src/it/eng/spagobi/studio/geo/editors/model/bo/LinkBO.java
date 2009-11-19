package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.CrossNavigation;
import it.eng.spagobi.studio.geo.editors.model.geo.DatamartProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.Link;

import java.util.Vector;

public class LinkBO {
	public static Link getLinkByHierarchyAndLevel(GEODocument geoDocument, 
			String hierarchyName, String levelName){
		Link link = null;
		DatamartProvider dmProvider = geoDocument.getDatamartProvider();
		CrossNavigation crossNavigation =dmProvider.getCrossNavigation();
		Vector<Link> links = crossNavigation.getLinks();
		for(int i=0; i<links.size(); i++){
			Link linkI = links.elementAt(i);
			if(hierarchyName != null && levelName != null && linkI.getHierarchy().equals(hierarchyName) && linkI.getLevel().equals(levelName)){
				link=linkI;
			}
			
		}
		return link;
	}

}
