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

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author cortella
 *
 */
public class GenerateJPAMappingWizardDirectorySelectionPage extends WizardPage {

	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	
	Text textDirectory;
	/**
	 * @param pageName
	 */
	protected GenerateJPAMappingWizardDirectorySelectionPage(String pageName) {
		super(pageName);
		setTitle(RL.getString("business.editor.wizard.generatemapping.title"));
		setDescription(RL.getString("business.editor.wizard.generatemapping.description"));
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.createBC") );
	    if (image!=null) setImageDescriptor(image);	
	    
	}


	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(6, false));
		
		Label lblDirectory = new Label(container, SWT.NONE);
		lblDirectory.setText(RL.getString("business.editor.wizard.generatemapping.label"));
		
	    // Input text box
	    textDirectory = new Text(container, SWT.BORDER);
	    GridData gd_textDirectory = new GridData(GridData.FILL_HORIZONTAL);
	    gd_textDirectory.horizontalSpan = 4;
	    textDirectory.setLayoutData(gd_textDirectory);
	    Location location = Platform.getInstanceLocation();
	    if (location != null){
	    	textDirectory.setText(location.getURL().getPath().substring(1));
	    }
	    else
	    	textDirectory.setText("D:\\Programmi\\eclipse\\helios-eclipse-3.6.0\\runtime-EclipseApplication\\TestOda\\mappings");
	    
	    // Browse button to select directory
	    Button buttonBrowse = new Button(container, SWT.PUSH);
	    buttonBrowse.setText(RL.getString("business.editor.wizard.generatemapping.browsebutton"));
	    buttonBrowse.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	        DirectoryDialog dlg = new DirectoryDialog(new Shell());

	        // Set the initial filter path according
	        // to anything they've selected or typed in
	        dlg.setFilterPath(textDirectory.getText());

	        dlg.setText(RL.getString("business.editor.wizard.generatemapping.directoryselection"));
	        dlg.setMessage(RL.getString("business.editor.wizard.generatemapping.directoryselection"));

	        // Calling open() will open and run the dialog.
	        // It will return the selected directory, or
	        // null if user cancels
	        String dir = dlg.open();
	        if (dir != null) {
	          // Set the text box to the new selection
	          textDirectory.setText(dir);
	        }
	        
	        checkPageComplete();
	      }
	    });

	}
	
	private void checkPageComplete(){
		if (textDirectory.getText().length() > 0){
			this.setPageComplete(true);
		} else {
			this.setPageComplete(false);
		}	
	}
	
	public String getSelectedDirectory(){
		return textDirectory.getText();
	}

}
