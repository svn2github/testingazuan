/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.olap.actions;

import java.util.ArrayList;

import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.generate.CreateQueryCommand;
import it.eng.spagobi.meta.model.olap.OlapModel;
import it.eng.spagobi.meta.model.olap.commands.edit.cube.CreateCubeCommand;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * @author cortella
 *
 */
public class SetCubeAction extends AbstractSpagoBIModelAction {

	private BusinessColumnSet businessColumnSet;
	public OlapModelInitializer olapModelInitializer = new OlapModelInitializer();


	/**
	 * @param commandClass
	 * @param workbenchPart
	 * @param selection
	 */
	public SetCubeAction(IWorkbenchPart workbenchPart,
			ISelection selection) {
		super(CreateCubeCommand.class, workbenchPart, selection);
	
	}
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		try {
			businessColumnSet = (BusinessColumnSet)owner;


			Model rootModel = businessColumnSet.getModel().getParentModel();
			OlapModel olapModel = rootModel.getOlapModels().get(0);
			
			CommandParameter commandParameter =  new CommandParameter(olapModel, null, businessColumnSet, new ArrayList<Object>());
		    if (editingDomain != null) {	    	
		    	editingDomain.getCommandStack().execute(new CreateCubeCommand(editingDomain,commandParameter));
		    }




		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

}
