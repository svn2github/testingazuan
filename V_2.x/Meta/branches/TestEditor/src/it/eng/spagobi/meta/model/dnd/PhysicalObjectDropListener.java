/*
 * DropSourceListener for the TreeViewer inside the GraphicEditorView
 */
package it.eng.spagobi.meta.model.dnd;

import java.util.ArrayList;
import java.util.StringTokenizer;


import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.AddBusinessTableCommand;
import it.eng.spagobi.meta.model.business.wizards.AddBusinessTableWizard;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;



public class PhysicalObjectDropListener  extends ViewerDropAdapter {

	private final Viewer viewer;
	private BusinessModel businessModel;
	private AdapterFactoryEditingDomain editingDomain;
	//private CoreSingleton coreSingleton = CoreSingleton.getInstance();
	
	public PhysicalObjectDropListener(Viewer viewer, EObject viewerInput, AdapterFactoryEditingDomain editingDomain){
		super(viewer);
		this.viewer = viewer;
		this.setScrollExpandEnabled(true);
		businessModel = (BusinessModel)viewerInput; 
		this.editingDomain = editingDomain;
	}
	
	// This method performs the actual drop
	@Override
	public boolean performDrop(Object data) {
		System.err.println(">> Input: "+businessModel);
		BusinessModelInitializer initializer = new BusinessModelInitializer();
		Model model = businessModel.getParentModel();
		
		//checking if the target of the drop is correct
		Object target = getCurrentTarget();
		int loc = getCurrentLocation();
       	if ( (target instanceof BusinessRootItemProvider) || 
       	   ( (target instanceof BusinessTable) && (loc == LOCATION_BEFORE || loc == LOCATION_AFTER)) ) {
       		StringTokenizer stringTokenizer = new StringTokenizer(data.toString(), "$$");
       		//obtaining table(s) name(s) from the passed string
       		while (stringTokenizer.hasMoreTokens()){
           		String tableName = stringTokenizer.nextToken();
       			
           		//getting physical table
           		URI uri = URI.createURI(tableName);
           		EObject eObject = model.eResource().getResourceSet().getEObject(uri, false);
           		
           		if (eObject instanceof PhysicalTable){
           			PhysicalTable physicalTable = (PhysicalTable)eObject;
            		if (physicalTable != null){
        				//Get Active Window
        				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        				//Launch AddBCWizard
        				
        				//*** to CHECK ***
        				Command addBusinessTableCommand = editingDomain.createCommand
        		        (AddBusinessTableCommand.class, 
        		        		new CommandParameter(businessModel, null, null, new ArrayList<Object>()));
        				AddBusinessTableWizard wizard = new AddBusinessTableWizard(businessModel, physicalTable, editingDomain,(AbstractSpagoBIModelCommand)addBusinessTableCommand);
        		    	WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
        				dialog.create();
        		    	dialog.open();
        			}		
           		}	
       		}
       		return true;
		}
       	else if ( (target instanceof BusinessTable) || 
            	( (target instanceof BusinessColumn) && (loc == LOCATION_BEFORE || loc == LOCATION_AFTER)) ){
       		
       		BusinessTable businessTableTarget = null;
       		//getting target table object
       		if (target instanceof BusinessTable){
       			businessTableTarget = (BusinessTable)target;
       		}
       		else if (target instanceof BusinessColumn){
       			businessTableTarget = (BusinessTable)((BusinessColumn)target).getTable();
       		}
       			
       		StringTokenizer stringTokenizer = new StringTokenizer(data.toString(), "$$");
       		//obtaining column(s) name(s) from the passed string
       		while (stringTokenizer.hasMoreTokens()){
           		String columnName = stringTokenizer.nextToken();
       			
  
           		URI uri = URI.createURI(columnName);
           		EObject eObject = model.eResource().getResourceSet().getEObject(uri, false);
           		
           		if (eObject instanceof PhysicalColumn){
           			PhysicalColumn physicalColumn = (PhysicalColumn)eObject;
            		if (physicalColumn != null){
            			BusinessTable businessTable = businessModel.getBusinessTable(physicalColumn.getTable());
            			//if target table is the same of the added columns perform the add
            			if (businessTable.equals(businessTableTarget)){
                			initializer.addColumn(physicalColumn, businessTable);
            			}
        			}		
           		}	
       		}
       		return true;
       		
       	}
		else {
			//drop not possible
			this.getCurrentEvent().detail = DND.DROP_NONE;
			return false;
		}
		
	}

	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		int loc = getCurrentLocation();
		if ( (target instanceof BusinessRootItemProvider) || 
		       	   ( (target instanceof BusinessTable) && (loc == LOCATION_BEFORE || loc == LOCATION_AFTER)) ) {
			return TextTransfer.getInstance().isSupportedType(transferType);
		}
		else if ( (target instanceof BusinessTable) || 
            	( (target instanceof BusinessColumn) && (loc == LOCATION_BEFORE || loc == LOCATION_AFTER)) ){
			return TextTransfer.getInstance().isSupportedType(transferType);
		}
		else {
			this.getCurrentEvent().detail = DND.DROP_NONE;
			return false;
		}

	}

}
