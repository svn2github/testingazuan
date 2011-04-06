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
package it.eng.spagobi.meta.generator.jpamapping.wrappers.impl;

import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaView;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaWrapperFactory {
	public static IJpaTable wrapTable(BusinessTable table) {
		return new JpaTable(table);
	}
	
	public static IJpaView wrapView(BusinessView view) {
		return new JpaView(view);
	}
	
	public static List<IJpaTable> wrapTables(List<BusinessTable> tables) {
		List<IJpaTable> jpaTables;
		
		jpaTables = new ArrayList<IJpaTable>();
		for(BusinessTable table : tables) {
			jpaTables.add( wrapTable(table) );
		}
		
		return jpaTables;
	}
	
	public static List<IJpaView> wrapViews(List<BusinessView> views) {
		List<IJpaView> jpaViews;
		
		jpaViews = new ArrayList<IJpaView>();
		for(BusinessView view : views) {
			jpaViews.add( wrapView(view) );
		}
		
		return jpaViews;
	}
}
