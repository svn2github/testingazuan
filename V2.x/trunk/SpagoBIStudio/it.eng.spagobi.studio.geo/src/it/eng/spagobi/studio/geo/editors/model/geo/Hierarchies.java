package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;
import java.util.Vector;

public class Hierarchies  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7414377872385811955L;
	private Vector<Hierarchy> hierarchy;

	public Vector<Hierarchy> getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Vector<Hierarchy> hierarchy) {
		this.hierarchy = hierarchy;
	}
	

}
