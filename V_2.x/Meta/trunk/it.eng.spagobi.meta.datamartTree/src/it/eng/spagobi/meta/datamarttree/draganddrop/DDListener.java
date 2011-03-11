package it.eng.spagobi.meta.datamarttree.draganddrop;


import it.eng.qbe.model.structure.AbstractDataMartItem;


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
   public void dragSetData(DragSourceEvent event) {
      IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
      AbstractDataMartItem[] datamartItems = (AbstractDataMartItem[])selection.toList().toArray(new AbstractDataMartItem[selection.size()]);
      if (DatamartFieldTransfer.getInstance().isSupportedType(event.dataType)) {
         event.data = datamartItems;
      } else if (PluginTransfer.getInstance().isSupportedType(event.dataType)) {
         byte[] data = DatamartFieldTransfer.getInstance().toByteArray(datamartItems);
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