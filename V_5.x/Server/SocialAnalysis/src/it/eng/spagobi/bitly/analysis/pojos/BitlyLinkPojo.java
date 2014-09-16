/**

SpagoBI, the Open Source Business Intelligence suite
 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
package it.eng.spagobi.bitly.analysis.pojos;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici
 *         (giorgio.federici@eng.it)
 *
 */
public class BitlyLinkPojo {

	private String link;
	private int counter_clicks;

	public BitlyLinkPojo(String l, int cc) {
		this.link = l;
		this.counter_clicks = cc;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getCounter_clicks() {
		return counter_clicks;
	}

	public void setCounter_clicks(int counter_clicks) {
		this.counter_clicks = counter_clicks;
	}

	@Override
	public String toString() {
		return "BitlyLinkPojo [link=" + link + ", counter_clicks=" + counter_clicks + "]";
	}

}
