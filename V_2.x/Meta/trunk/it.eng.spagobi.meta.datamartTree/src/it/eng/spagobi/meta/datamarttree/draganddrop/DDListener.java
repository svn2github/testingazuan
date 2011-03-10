package it.eng.spagobi.meta.datamarttree.draganddrop;


import it.eng.spagobi.meta.datamarttree.bo.DatamartField;

import java.util.Iterator;

import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.part.PluginTransferData;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.dnd.*;
/**
 * Supports dragging fields from a structured viewer.
 */
public class DDListener extends DragSourceAdapter {
   private StructuredViewer viewer;
   public DDListener(StructuredViewer viewer) {
      this.viewer = viewer;
   }
   /**
    * Method declared on DragSourceListener
    */
//   public void dragFinished(DragSourceEvent event) {
//      if (!event.doit)
//         return;
//      //if the field was moved, remove it from the source viewer
////      if (event.detail == DND.DROP_MOVE) {
////         IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
////         for (Iterator it = selection.iterator(); it.hasNext();) {
////            ((DatamartField)it.next()).setParent(null);
////         }
////         viewer.refresh();
////      }
//   }
   /**
    * Method declared on DragSourceListener
    */
   public void dragSetData(DragSourceEvent event) {
      IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
      DatamartField[] gadgets = (DatamartField[])selection.toList().toArray(new DatamartField[selection.size()]);
      if (DatamartFieldTransfer.getInstance().isSupportedType(event.dataType)) {
         event.data = gadgets;
      } else if (PluginTransfer.getInstance().isSupportedType(event.dataType)) {
         byte[] data = DatamartFieldTransfer.getInstance().toByteArray(gadgets);
         event.data = new PluginTransferData("it.eng.spagobi.meta.datamarttree.views.datamartFieldDrop", data);
      }
   }
   /**
    * Method declared on DragSourceListener
    */
   public void dragStart(DragSourceEvent event) {
      event.doit = !viewer.getSelection().isEmpty();
   }
}