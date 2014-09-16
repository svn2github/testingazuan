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
public class BitlyLinkCategoryPojo {

	private long link_id;
	private String type;
	private String category;
	private int clicks_count;
	private String link;

	public BitlyLinkCategoryPojo(long li, String t, String c, int cc) {
		this.link_id = li;
		this.type = t;
		this.category = c;
		this.clicks_count = cc;
	}

	public BitlyLinkCategoryPojo(String t, String c, int cc, String link) {
		this.type = t;
		this.category = c;
		this.clicks_count = cc;
		this.link = link;
	}

	public long getLink_id() {
		return link_id;
	}

	public void setLink_id(long link_id) {
		this.link_id = link_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getClicks_count() {
		return clicks_count;
	}

	public void setClicks_count(int clicks_count) {
		this.clicks_count = clicks_count;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "BitlyLinkCategoryPojo [link_id=" + link_id + ", type=" + type + ", category=" + category + ", clicks_count=" + clicks_count + ", link=" + link + "]";
	}

}
