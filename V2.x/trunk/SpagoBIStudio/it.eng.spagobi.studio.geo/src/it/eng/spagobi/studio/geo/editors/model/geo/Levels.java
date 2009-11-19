package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;
import java.util.Vector;


public class Levels  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6722328642875612731L;
	private Vector<Level> level ;

	public Vector<Level> getLevel() {
		return level;
	}

	public void setLevel(Vector<Level> level) {
		this.level = level;
	}

}
