package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.DatamartProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.Hierarchies;
import it.eng.spagobi.studio.geo.editors.model.geo.Hierarchy;

import java.util.Vector;

public class HierarchyBO {
	
	public static void setNewHierarchy(GEODocument geoDocument, String name, String type){
		DatamartProvider dmProvider =geoDocument.getDatamartProvider();
		if(dmProvider != null){
			Hierarchies hierarchies = dmProvider.getHierarchies();
			Vector<Hierarchy> vectHier = null;
			if(hierarchies == null){
				hierarchies = new Hierarchies();
				vectHier = new Vector<Hierarchy>();
				hierarchies.setHierarchy(vectHier);
			}else{
				vectHier = hierarchies.getHierarchy();
				if(vectHier == null){
					vectHier = new Vector<Hierarchy>();
					hierarchies.setHierarchy(vectHier);
				}				
			}
			//add new hierarchy
			Hierarchy newHierarchy = new Hierarchy();
			newHierarchy.setName(name);
			newHierarchy.setType(type);
			vectHier.add(newHierarchy);
		}

	}

}
