/*
 * DropSourceListener for the TreeViewer inside the GraphicEditorView
 */
package it.eng.spagobi.meta.editor.dnd;

import java.util.StringTokenizer;

import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.editor.wizards.AddBusinessTableWizard;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;



public class PhysicalObjectDropListener  extends ViewerDropAdapter {

	private final TreeViewer treeViewer;
	private CoreSingleton coreSingleton = CoreSingleton.getInstance();
	
	public PhysicalObjectDropListener(TreeViewer tree){
		super(tree);
		treeViewer = tree;
		this.setScrollExpandEnabled(true);
	}
	
	// This method performs the actual drop
	@Override
	public boolean performDrop(Object data) {
		CoreSingleton coreSingleton = CoreSingleton.getInstance();
		BusinessModelInitializer initializer = new BusinessModelInitializer();
		
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
           		Model model = coreSingleton.getRootModel();
           		URI uri = URI.createURI(tableName);
           		EObject eObject = model.eResource().getResourceSet().getEObject(uri, false);
           		
           		if (eObject instanceof PhysicalTable){
           			PhysicalTable physicalTable = (PhysicalTable)eObject;
            		if (physicalTable != null){
        				//Get Active Window
        				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        				//Launch AddBCWizard
        				AddBusinessTableWizard wizard = new AddBusinessTableWizard(physicalTable);
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
       		
       		BusinessColumnSet businessTableTarget = null;
       		//getting target table object
       		if (target instanceof BusinessTable){
       			businessTableTarget = (BusinessTable)target;
       		}
       		else if (target instanceof BusinessColumn){
       			businessTableTarget = ((BusinessColumn)target).getTable();
       		}
       			
       		StringTokenizer stringTokenizer = new StringTokenizer(data.toString(), "$$");
       		//obtaining column(s) name(s) from the passed string
       		while (stringTokenizer.hasMoreTokens()){
           		String columnName = stringTokenizer.nextToken();
       			
           		//getting physical column
           		Model model = coreSingleton.getRootModel();
           		URI uri = URI.createURI(columnName);
           		EObject eObject = model.eResource().getResourceSet().getEObject(uri, false);
           		
           		if (eObject instanceof PhysicalColumn){
           			PhysicalColumn physicalColumn = (PhysicalColumn)eObject;
            		if (physicalColumn != null){
            			BusinessTable businessTable = coreSingleton.getBusinessModel().getTable(physicalColumn.getTable());
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
