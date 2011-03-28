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
package it.eng.spagobi.meta.querybuilder.ui.wizard;


import it.eng.qbe.model.structure.ViewModelStructure;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import org.eclipse.datatools.connectivity.oda.design.internal.ui.DataSetWizardBase;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;



/**
 * @author cortella
 *
 */
public class SpagoBIDataSetWizardResultPage extends DataSetWizardPage {
    private static String DEFAULT_MESSAGE = "Preview of the query results";
    
	public SpagoBIDataSetWizardResultPage( String pageName )
	{
        super( pageName );
        setTitle( pageName );
        setMessage( DEFAULT_MESSAGE );
     //   this.queryBuilderUI = new QueryBuilder();
	}
	
	public SpagoBIDataSetWizardResultPage( String pageName, String title,
			ImageDescriptor titleImage )
	{
        super( pageName, title, titleImage );
        setMessage( DEFAULT_MESSAGE );
	}

	@Override
	public void createPageCustomControl(Composite parent) {
		setControl( createPageControl( parent ) );

	}
	
	public QueryBuilder getQueryBuilder() {
		return getSpagoBIWizard().getQueryBuilder();
	}
	
	public SpagoBIDataSetWizard getSpagoBIWizard() {
		return (SpagoBIDataSetWizard)getOdaWizard();
	}
	
    /**
     * Creates custom control for query results preview.
     */
    private Control createPageControl( Composite parent )
    {
      
      Composite composite = getQueryBuilder().createResultsComponents(parent);
        setPageComplete( true );
        return composite;
    }
    

}
