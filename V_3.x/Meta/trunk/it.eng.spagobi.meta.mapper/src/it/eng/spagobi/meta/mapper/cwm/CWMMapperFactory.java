/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.mapper.cwm;

import it.eng.spagobi.meta.mapper.cwm.emf.CWMMapperEMFImpl;
import it.eng.spagobi.meta.mapper.cwm.jmi.SpagoBICWMMapperJMIImpl;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class CWMMapperFactory {
	public static ICWMMapper getMapper(CWMImplType implementationType) {
		ICWMMapper mapper = null;
		
		if(implementationType == CWMImplType.JMI) {
			mapper = new SpagoBICWMMapperJMIImpl();
		} else {
			mapper = new CWMMapperEMFImpl();
		}
		
		return mapper;
	}
}
