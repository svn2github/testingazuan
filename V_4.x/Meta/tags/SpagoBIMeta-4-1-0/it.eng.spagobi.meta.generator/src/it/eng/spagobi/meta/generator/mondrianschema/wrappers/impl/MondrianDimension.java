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
package it.eng.spagobi.meta.generator.mondrianschema.wrappers.impl;

import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.meta.generator.mondrianschema.wrappers.IMondrianDimension;
import it.eng.spagobi.meta.generator.mondrianschema.wrappers.IMondrianHierarchy;
import it.eng.spagobi.meta.model.olap.Dimension;
import it.eng.spagobi.meta.model.olap.Hierarchy;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class MondrianDimension implements IMondrianDimension {

	Dimension dimension;
	/**
	 * @param dimension
	 */
	public MondrianDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.mondrianschema.wrappers.IMondrianDimension#getName()
	 */
	@Override
	public String getName() {
		return dimension.getName();
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.mondrianschema.wrappers.IMondrianDimension#getHierarchies()
	 */
	@Override
	public List<IMondrianHierarchy> getHierarchies() {
		List<IMondrianHierarchy> hierarchies =  new ArrayList<IMondrianHierarchy>();
		for (Hierarchy hierarchy : dimension.getHierarchies()){
			MondrianHierarchy mondrianHierarchy= new MondrianHierarchy(hierarchy);
			hierarchies.add(mondrianHierarchy);
		}
		return hierarchies;
	}

}
