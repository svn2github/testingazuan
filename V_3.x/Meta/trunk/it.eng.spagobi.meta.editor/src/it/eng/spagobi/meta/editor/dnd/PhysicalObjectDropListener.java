/*
 * DropSourceListener for the TreeViewer inside the GraphicEditorView
 */
package it.eng.spagobi.meta.editor.dnd;

import it.eng.spagobi.meta.editor.business.wizards.inline.AddBusinessTableWizard;
import it.eng.spagobi.meta.editor.business.wizards.inline.AddPhysicalTableWizard;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.AddBusinessTableCommand;
import it.eng.spagobi.meta.model.business.commands.AddPhysicalTableToBusinessTableCommand;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @deprecated  replaced by BusinessModelDropTargetListener
 * 
 */

public class PhysicalObjectDropListener  extends ViewerDropAdapter {

	private final Viewer viewer;
	private BusinessModel businessModel;
	private AdapterFactoryEditingDomain editingDomain;
	
	private static Logger logger = LoggerFactory.getLogger(PhysicalObjectDropListener.class);
	
	
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
		logger.debug("Business model is equal to [{}]", businessModel);
		BusinessModelInitializer initializer = new BusinessModelInitializer();
		Model model = businessModel.getParentModel();
		
		//checking if the target of the drop is correct
		Object target = getCurrentTarget();
		int loc = getCurrentLocation();
		
		//----------------------------------------------------------------
		// Creation of a new BusinessTable from the dragged PhysicalTable
		//----------------------------------------------------------------
       	if ( (target instanceof BusinessRootItemProvider) || 
       	   ( (target instanceof BusinessColumnSet) && (loc == LOCATION_BEFORE || loc == LOCATION_AFTER)) ) {
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
        				
        				//Create addBusinessTable command
        				Command addBusinessTableCommand = editingDomain.createCommand
        		        (AddBusinessTableCommand.class, 
        		        		new CommandParameter(businessModel, null, null, new ArrayList<Object>()));
        				//Launch AddBusinessTableWizard
        				AddBusinessTableWizard wizard = new AddBusinessTableWizard(businessModel, physicalTable, editingDomain,(AbstractSpagoBIModelCommand)addBusinessTableCommand);
        		    	WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
        				dialog.create();
        		    	dialog.open();
        			}		
           		}	
       		}
       		return true;
		}
		//----------------------------------------------------------------
		// Add BusinessColumns or PhysicalTable to BusinessColumnSet from the dragged PhysicalColumn or PhysicalTable
		//----------------------------------------------------------------
       	else if ( (target instanceof BusinessColumnSet) || 
            	( (target instanceof BusinessColumn) && (loc == LOCATION_BEFORE || loc == LOCATION_AFTER)) ){
       		
       		BusinessColumnSet businessColumnSet = null;
       		//getting target table object
       		if (target instanceof BusinessTable){
       			businessColumnSet = (BusinessTable)target;
       		}
       		else if (target instanceof BusinessColumn){
       			businessColumnSet = (BusinessColumnSet)((BusinessColumn)target).getTable();
       		}
       		else if (target instanceof BusinessView){
       			businessColumnSet = (BusinessView)target;
       		}
       			
       		StringTokenizer stringTokenizer = new StringTokenizer(data.toString(), "$$");
       		
       		while (stringTokenizer.hasMoreTokens()){
           		String objectName = stringTokenizer.nextToken();
  
           		URI uri = URI.createURI(objectName);
           		EObject eObject = model.eResource().getResourceSet().getEObject(uri, false);
           		
           		//------------------
           		// Add Columns
           		//------------------
           		//obtaining column(s) name(s) from the passed string
           		if (eObject instanceof PhysicalColumn){
           			PhysicalColumn physicalColumn = (PhysicalColumn)eObject;
            		if (physicalColumn != null){
            			//check if target is a BusinessView or BusinessTable
            			if ( businessColumnSet instanceof BusinessView ){
            				// check if column's source PhysicalTable is inside target Physical TableS 
            				List<PhysicalTable> targetPhysicalTables = ((BusinessView)businessColumnSet).getPhysicalTables();
            				PhysicalTable sourcePhysicalTable = physicalColumn.getTable();
            				
            				if (targetPhysicalTables.contains(sourcePhysicalTable)){
            					//add column
                    			initializer.addColumn(physicalColumn, businessColumnSet);
            				} else {
            					//add PhysicalTable is necessary
                				Command addPhysicalTableCommand = editingDomain.createCommand
                		        (AddPhysicalTableToBusinessTableCommand.class, 
                		        		new CommandParameter(businessColumnSet, null, null, new ArrayList<Object>()) );
                				AddPhysicalTableWizard wizard = new AddPhysicalTableWizard(businessColumnSet,editingDomain, (AbstractSpagoBIModelCommand)addPhysicalTableCommand, true, sourcePhysicalTable.getName() );
                		    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
                				dialog.create();
                		    	dialog.open();
            					
                		    	//add column
                		    	initializer.addColumn(physicalColumn, businessColumnSet);
            				}
            			}
            			else if ( businessColumnSet instanceof BusinessTable ){
            				PhysicalTable sourcePhysicalTable = physicalColumn.getTable();
            				PhysicalTable targetPhysicalTable = ((BusinessTable)businessColumnSet).getPhysicalTable();
                			//if target table has the same PhysicalTable of the added columns, then perform the addColumn
                			if (sourcePhysicalTable.equals(targetPhysicalTable)){
                    			initializer.addColumn(physicalColumn, businessColumnSet);
                			}
                			//if target table has a different PhysicalTable, then upgrade to BusinessView is necessary
                			else {
                				//upgrade BusinessTable to BusinessView
                				Command addPhysicalTableCommand = editingDomain.createCommand
                		        (AddPhysicalTableToBusinessTableCommand.class, 
                		        		new CommandParameter(businessColumnSet, null, null, new ArrayList<Object>()));
                				AddPhysicalTableWizard wizard = new AddPhysicalTableWizard(businessColumnSet,editingDomain, (AbstractSpagoBIModelCommand)addPhysicalTableCommand, false, sourcePhysicalTable.getName());
                		    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
                				dialog.create();
                		    	dialog.open();
                				
                		    	//re-set businessColumnSet reference to point to BusinessView
                		    	businessColumnSet = (BusinessView)businessModel.getTable(businessColumnSet.getName());
                				
                   		    	//add the column
                    			initializer.addColumn(physicalColumn, businessColumnSet);
                			}
            			}

        			}		
           		}
           		//------------------
           		// Add Table
           		//------------------
           		//obtaining table(s) name(s) from the passed string
           		else if (eObject instanceof PhysicalTable){
           			PhysicalTable sourcePhysicalTable = (PhysicalTable)eObject;
           			boolean isBusinessView = false;
           			if (businessColumnSet instanceof BusinessView){
           				isBusinessView = true;
           			}
           			//add PhysicalTable to BusinessTable or BusinessView
        			Command addPhysicalTableCommand = editingDomain.createCommand
        		    (AddPhysicalTableToBusinessTableCommand.class, 
        		      		new CommandParameter(businessColumnSet, null, null, new ArrayList<Object>()));
        			AddPhysicalTableWizard wizard = new AddPhysicalTableWizard(businessColumnSet,editingDomain, (AbstractSpagoBIModelCommand)addPhysicalTableCommand, isBusinessView, sourcePhysicalTable.getName());
        		    WizardDialog dialog = new WizardDialog(new Shell(), wizard);
        			dialog.create();
        		    dialog.open();
        		    
        		    //update businessColumnSet reference in case that BusinessTable upgraded to BusinessView
        		    businessColumnSet = businessModel.getTable(businessColumnSet.getName());
           		}
           		
       		}
       		return true;
       		
       	}
		//----------------------------------------------------------------
       	// Drop not possible
		//----------------------------------------------------------------
		else {
			this.getCurrentEvent().detail = DND.DROP_NONE;
			return false;
		}
		
	}

	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		int loc = getCurrentLocation();
		if ( (target instanceof BusinessRootItemProvider) || 
		       	   ( (target instanceof BusinessColumnSet) && (loc == LOCATION_BEFORE || loc == LOCATION_AFTER)) ) {
			return TextTransfer.getInstance().isSupportedType(transferType);
		}
		else if ( (target instanceof BusinessColumnSet) || 
            	( (target instanceof BusinessColumn) && (loc == LOCATION_BEFORE || loc == LOCATION_AFTER)) ){
			return TextTransfer.getInstance().isSupportedType(transferType);
		}
		else {
			this.getCurrentEvent().detail = DND.DROP_NONE;
			return false;
		}

	}

}
