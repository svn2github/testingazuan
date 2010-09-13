/*
 * ContentProvider for the TreeViewer inside the GraphicEditorView
 */
package eng.it.spagobimeta.util;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import eng.it.spagobimeta.listeners.DeltaEvent;
import eng.it.spagobimeta.listeners.IDeltaListener;
import eng.it.spagobimeta.model.BCField;
import eng.it.spagobimeta.model.BMWrapper;
import eng.it.spagobimeta.model.BusinessClass;
import eng.it.spagobimeta.model.BusinessModel;

public class BMTreeContentProvider implements ITreeContentProvider, IDeltaListener {
	private static Object[] EMPTY_ARRAY = new Object[0];
	protected TreeViewer viewer;
	
	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof BMWrapper){
			BMWrapper bmw = (BMWrapper)parentElement;
			return bmw.getBm().toArray();
		}
		if(parentElement instanceof BusinessModel){
			BusinessModel bm = (BusinessModel)parentElement;
			return bm.getBcCollection().toArray();
		}
		if(parentElement instanceof BusinessClass){
			BusinessClass bc = (BusinessClass)parentElement;
			return bc.getFieldsCollection().toArray();
		}
		if(parentElement instanceof BCField){
			BCField field = (BCField)parentElement;
			return new Object[]{ field.getType() };
		}
			
		return EMPTY_ARRAY;
	}

	@Override
	public Object getParent(Object element) {
	    if(element instanceof BusinessClass) {
	        return ((BusinessClass)element).getBmParent();
	    }
	    if(element instanceof BCField) {
	        return ((BCField)element).getBcParent();
	    }	

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	
	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof BMWrapper) {
			return getChildren(inputElement);
		}
		return null;
	}
	

	@Override
	public void dispose() {

	}

	@Override
	//Called when the setInput method is used
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer)viewer;
		if(oldInput != null) {
			removeListenerFrom((BMWrapper)oldInput);
			System.out.println("Removed listener");
		}
		if(newInput != null) {
			addListenerTo((BMWrapper)newInput);
			System.out.println("Added listener");
		}
	}

	//*** Methods for listener to model ****
	
	protected void removeListenerFrom(BMWrapper bmw) {
		bmw.removeListener(this);
	}
	

	protected void addListenerTo(BMWrapper bmw) {
		bmw.addListener(this);
	}	
	
	@Override
	public void add(DeltaEvent event) {
		Object bmw = ((BMWrapper)event.receiver());
		viewer.refresh(bmw, false);
	}

	@Override
	public void remove(DeltaEvent event) {
		add(event);	
	}
}
