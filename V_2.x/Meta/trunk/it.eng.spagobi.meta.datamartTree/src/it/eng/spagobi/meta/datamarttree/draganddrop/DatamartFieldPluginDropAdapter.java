package it.eng.spagobi.meta.datamarttree.draganddrop;

import it.eng.spagobi.meta.datamarttree.bo.DatamartItem;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;

public class DatamartFieldPluginDropAdapter extends ViewerDropAdapter {
	   public DatamartFieldPluginDropAdapter(TreeViewer viewer) {
		      super(viewer);
		   }
		   /**
		    * Method declared on ViewerDropAdapter
		    */
		   public boolean performDrop(Object data) {
			   DatamartItem target = (DatamartItem)getCurrentTarget();
		      if (target == null)
		         target = (DatamartItem)getViewer().getInput();
		      DatamartItem[] toDrop = (DatamartItem[])data;
		      TreeViewer viewer = (TreeViewer)getViewer();
		      //cannot drop a gadget onto itself or a child
		      for (int i = 0; i < toDrop.length; i++)
		         if (toDrop[i].equals(target) || target.hasParent(toDrop[i]))
		            return false;
		      for (int i = 0; i < toDrop.length; i++) {
		         toDrop[i].setParent(target);
		         viewer.add(target, toDrop[i]);
		         viewer.reveal(toDrop[i]);
		      }
		      return true;
		   }
		   /**
		    * Method declared on ViewerDropAdapter
		    */
		   public boolean validateDrop(Object target, int op, TransferData type) {
		      return DatamartFieldTransfer.getInstance().isSupportedType(type);
		   }
		}