/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.ui.wizard;

import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

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
	}
	
	public SpagoBIDataSetWizardResultPage( String pageName, String title,
			ImageDescriptor titleImage )
	{
        super( pageName, title, titleImage );
        setMessage( DEFAULT_MESSAGE );
	}

	@Override
	public void createPageCustomControl(Composite parent) {
		getSpagoBIWizard().initQueryBuilder(getInitializationDesign());
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
      getQueryBuilder().refreshQueryResultPage();
      setPageComplete( true );
      return composite;
    }
    

}
