package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;
import java.util.Vector;

public class Measures  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3393925648919400182L;
	private Vector<KPI> kpi;

	public Vector<KPI> getKpi() {
		return kpi;
	}

	public void setKpi(Vector<KPI> kpi) {
		this.kpi = kpi;
	}

}
