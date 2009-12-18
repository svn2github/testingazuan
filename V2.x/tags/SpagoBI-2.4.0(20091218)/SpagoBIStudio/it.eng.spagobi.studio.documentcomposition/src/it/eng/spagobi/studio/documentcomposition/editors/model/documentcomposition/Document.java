/**
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition;

import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocument;

import java.util.Vector;



public class Document {

	private String sbiObjLabel;
	private String localFileName;	
	private Parameters parameters;//parameters
	private Style style; 
	private String id;

//	private static long idCounter = 0;
//
//	public static synchronized String createID()
//	{
//		return String.valueOf(idCounter++);
//	}

	public Document() {
		//this.id = createID();
	}

	public Style getStyle() {
		return style;
	}
	public void setStyle(Style style) {
		this.style = style;
	}
	public String getLocalFileName() {
		return localFileName;
	}
	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}
	public Parameters getParameters() {
		return parameters;
	}
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	public String getSbiObjLabel() {
		return sbiObjLabel;
	}
	public void setSbiObjLabel(String sbiObjLabel) {
		this.sbiObjLabel = sbiObjLabel;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Document(MetadataDocument metadataDocument, Style _style) {
		super();
		sbiObjLabel=metadataDocument.getLabel();
		localFileName=metadataDocument.getLocalFileName();
		style=_style;
		this.id = metadataDocument.getIdMetadataDocument();
	}	



}
