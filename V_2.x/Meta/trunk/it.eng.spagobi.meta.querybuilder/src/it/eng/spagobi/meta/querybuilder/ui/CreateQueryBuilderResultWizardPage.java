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
package it.eng.spagobi.meta.querybuilder.ui;

import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;



/**
 * @author cortella
 *
 */
public class CreateQueryBuilderResultWizardPage extends DataSetWizardPage {
    private static String DEFAULT_MESSAGE = "Preview of the query results";
    
    private CreateQueryBuilderUI queryBuilderUI;
 

    
	public CreateQueryBuilderResultWizardPage( String pageName )
	{
        super( pageName );
        setTitle( pageName );
        setMessage( DEFAULT_MESSAGE );
        this.queryBuilderUI = new CreateQueryBuilderUI();
	}
	
	public CreateQueryBuilderResultWizardPage( String pageName, CreateQueryBuilderUI queryBuilderUI )
	{
        super( pageName );
        setTitle( pageName );
        setMessage( DEFAULT_MESSAGE );
        this.queryBuilderUI = queryBuilderUI;
	}
	
	public CreateQueryBuilderResultWizardPage( String pageName, String title,
			ImageDescriptor titleImage )
	{
        super( pageName, title, titleImage );
        setMessage( DEFAULT_MESSAGE );
        this.queryBuilderUI = new CreateQueryBuilderUI();

	}

	@Override
	public void createPageCustomControl(Composite parent) {
		setControl( createPageControl( parent ) );

	}
	
    /**
     * Creates custom control for query results preview.
     */
    private Control createPageControl( Composite parent )
    {
    	Composite composite = queryBuilderUI.createResultsComponent(parent);
        setPageComplete( true );
        return composite;
    }
    

}
