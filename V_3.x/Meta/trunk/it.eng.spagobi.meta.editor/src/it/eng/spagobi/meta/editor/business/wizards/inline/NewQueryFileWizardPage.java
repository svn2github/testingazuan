/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * @author cortella
 *
 */
public class NewQueryFileWizardPage extends WizardNewFileCreationPage {
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 

	public NewQueryFileWizardPage(IStructuredSelection selection) {
        super("NewQueryFileWizardPage", selection);
        setTitle(RL.getString("business.editor.wizard.newqueryfile.title"));
        setDescription(RL.getString("business.editor.wizard.newqueryfile.description"));
        setFileExtension("metaquery");
    }


}
