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
package it.eng.spagobi.meta.querybuilder.ui.editor;

import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIDataSetEditPage extends Composite {

	public SpagoBIDataSetEditPage(Composite container, QueryBuilder builder) {
		super(container, SWT.NONE);
		
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
	
		setLayout(new GridLayout(2, false));
		
		//Create Business Model Tree 
		builder.createEditBusinessModelTree(this);
		
		//Create Query Filters
		builder.createEditGroup(this);
	}


}
