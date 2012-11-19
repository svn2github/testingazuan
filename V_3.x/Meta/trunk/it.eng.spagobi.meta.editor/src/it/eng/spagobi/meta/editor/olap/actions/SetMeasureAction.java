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
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.generate.CreateQueryCommand;
import it.eng.spagobi.meta.model.olap.Cube;
import it.eng.spagobi.meta.model.olap.OlapModel;
import it.eng.spagobi.meta.model.olap.commands.edit.cube.CreateCubeCommand;
import it.eng.spagobi.meta.model.olap.commands.edit.cube.CreateMeasureCommand;
import it.eng.spagobi.meta.model.olap.commands.edit.dimension.CreateDimensionCommand;
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
public class SetMeasureAction extends AbstractSpagoBIModelAction {

	private SimpleBusinessColumn businessColumn;
	public OlapModelInitializer olapModelInitializer = new OlapModelInitializer();

	/**
	 * @param commandClass
	 * @param workbenchPart
	 * @param selection
	 */
	public SetMeasureAction(IWorkbenchPart workbenchPart,
			ISelection selection) {
		super(CreateMeasureCommand.class, workbenchPart, selection);
	
	}
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		try {
			businessColumn = (SimpleBusinessColumn)owner;

			Model rootModel = businessColumn.getTable().getModel().getParentModel();
	    	Cube cube = checkIfInsideCube(businessColumn);
	    	if (cube != null){
				CommandParameter commandParameter =  new CommandParameter(cube, null, businessColumn, new ArrayList<Object>());
			    if (editingDomain != null) {	    	
			    	editingDomain.getCommandStack().execute(new CreateMeasureCommand(editingDomain,commandParameter));
			    }	     
				//Settare la property columntype = measure
				businessColumn.getProperties().get("structural.columntype").setValue("measure");
	    	}

		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	private Cube checkIfInsideCube(SimpleBusinessColumn simpleBusinessColumn){
		return olapModelInitializer.getCube(simpleBusinessColumn.getTable());
	}

}
