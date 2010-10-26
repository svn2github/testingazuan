package it.eng.spagobi.meta.editor.views.actions;

import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.editor.views.BusinessModelView;
import it.eng.spagobi.meta.editor.views.PhysicalModelView;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

public class CreateBusinessModelFromPhysicalModel implements
		IViewActionDelegate {

	private IViewPart businessModelView;
	
	@Override
	public void run(IAction action) {
		//retrieve root Model reference and the PhysicalModel
		CoreSingleton cs = CoreSingleton.getInstance();
        Model rootModel = cs.getRootModel();
        String bmName = cs.getBmName();
		//businessModelGroup.setText("Business Model: "+ bmName);
        PhysicalModel pm = cs.getPhysicalModel();
	    
        BusinessModel model;
        //check if BusinessModel is already created
        if (cs.getBusinessModel() != null)
        {
            //erase current Business Model
        	cs.deleteBusinessModel();    	
        	//initialize the EMF Business Model
            BusinessModelInitializer modelInitializer = new BusinessModelInitializer();
            model = modelInitializer.initialize( bmName, pm);
        } else {
        	model = cs.getBusinessModel();
        }
		
		
		//get View reference
		businessModelView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("it.eng.spagobi.meta.editor.BusinessModel");
		//invoke tree creation on BusinessModelView
		//((BusinessModelView)businessModelView).createTree(model);
		((BusinessModelView)businessModelView).setModel(model);

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IViewPart view) {
		// TODO Auto-generated method stub

	}

}
