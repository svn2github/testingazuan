/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.mapper.cwm;



/**
 * @author agioia
 *
 */
public interface ICWM {
	public CWMImplType getImplementationType();
	public String getName();
	public void setName(String name);
	
	public void exportToXMI(String filename);	
	public void importFromXMI(String filename);
}
