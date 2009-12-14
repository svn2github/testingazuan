package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;
import java.util.Vector;

public class CrossNavigation  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5915328890533988875L;
	private Vector<Link> links = new Vector<Link>();
	public static int idLink=0;

	public Vector<Link> getLinks() {
		return links;
	}

	public void setLinks(Vector<Link> links) {
		this.links = links;
	}

}
