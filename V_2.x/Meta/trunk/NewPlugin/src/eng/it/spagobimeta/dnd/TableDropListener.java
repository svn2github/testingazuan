/*
 * DropSourceListener for the TreeViewer inside the GraphicEditorView
 */
package eng.it.spagobimeta.dnd;

import java.util.ArrayList;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;
import eng.it.spagobimeta.model.BMWrapper;
import eng.it.spagobimeta.model.BusinessClass;
import eng.it.spagobimeta.model.BusinessModel;


public class TableDropListener  extends ViewerDropAdapter {

	private final Viewer viewer;
	private BMWrapper bmw;

	public TableDropListener(Viewer v){
		super(v);
		this.viewer = v;
		//get unique instance of BMWrapper
		this.bmw = BMWrapper.getInstance();
	}
	
	// This method performs the actual drop
	@Override
	public boolean performDrop(Object data) {
		//retrieve Business Model
		ArrayList<BusinessModel> el = (ArrayList<BusinessModel>)bmw.getBm();
		BusinessModel bm =  el.get(0);
		
		//Add dropped element to the business model
		bm.addBc(new BusinessClass(data.toString(),bm));
		el = new ArrayList<BusinessModel>();
		el.add(bm);
		bmw.setBm(el);
		viewer.setInput(bmw);
		((TreeViewer)viewer).expandAll();
		
		return false;
	}

	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		return TextTransfer.getInstance().isSupportedType(transferType);

	}

}
