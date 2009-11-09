package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.DatamartProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.Hierarchies;
import it.eng.spagobi.studio.geo.editors.model.geo.Hierarchy;
import it.eng.spagobi.studio.geo.editors.model.geo.Level;
import it.eng.spagobi.studio.geo.editors.model.geo.Levels;

import java.util.Vector;

public class LevelBO {

	public static void setNewLevel(GEODocument geoDocument,
			String hierarchyName, Level newLevel) {
		DatamartProvider dmProvider = geoDocument.getDatamartProvider();
		if (dmProvider != null) {
			Hierarchies hierarchies = dmProvider.getHierarchies();
			Vector<Hierarchy> vectHier = null;
			if (hierarchies != null) {
				vectHier = hierarchies.getHierarchy();
				if (vectHier != null) {

					for (int i = 0; i < vectHier.size(); i++) {
						if (vectHier.elementAt(i).getName().equals(
								hierarchyName)) {
							Levels levels = null;
							Vector<Level> vectLevels = null;
							if (vectHier.elementAt(i).getLevels() == null) {
								levels = new Levels();
								vectLevels = new Vector<Level>();
								levels.setLevel(vectLevels);
							} else {
								levels = vectHier.elementAt(i).getLevels();
								vectLevels = levels.getLevel();
								if (vectLevels == null) {
									vectLevels = new Vector<Level>();
									levels.setLevel(vectLevels);
								}
							}
							levels.getLevel().add(newLevel);

						}
					}
				}
			}
		}
	}
	public static void deleteLevel(GEODocument geoDocument,
			String hierarchyName, String toDeleteLevel) {
		DatamartProvider dmProvider = geoDocument.getDatamartProvider();
		if (dmProvider != null) {
			Hierarchies hierarchies = dmProvider.getHierarchies();
			Vector<Hierarchy> vectHier = null;
			if (hierarchies != null) {
				vectHier = hierarchies.getHierarchy();
				if (vectHier != null) {
					for (int i = 0; i < vectHier.size(); i++) {
						if (vectHier.elementAt(i).getName().equals(
								hierarchyName)) {
							Levels levels = null;
							Vector<Level> vectLevels = null;
							if (vectHier.elementAt(i).getLevels() == null && vectHier.elementAt(i).getLevels().getLevel() != null) {
								for(int j=0; j<vectHier.elementAt(i).getLevels().getLevel().size(); j++){
									Level l = vectHier.elementAt(i).getLevels().getLevel().elementAt(j);
									if(l.getName().equals(toDeleteLevel)){
										vectHier.elementAt(i).getLevels().getLevel().remove(l);
									}
									
								}
							}
						}
					}
				}
			}
		}
	}
}
