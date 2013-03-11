/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.dnd;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class CalculatedFieldDropTargetListener extends DropTargetAdapter {
	
	Text target;
	
	public CalculatedFieldDropTargetListener(Text target){
		this.target = target;
	}
	
	public void dragOver(DropTargetEvent event) {
        if (event.item != null) {
          TreeItem item = (TreeItem) event.item;
       
        }
      }

      public void drop(DropTargetEvent event) {
        if (event.data == null) {
          event.detail = DND.DROP_NONE;
          return;
        } else {
        	String draggedText = (String)event.data;
        	//String targetText = target.getText();
        	//target.setText(targetText+" "+draggedText);
        	target.append(" "+draggedText);
        	event.detail = DND.DROP_MOVE;
        	
        }
        	
      }

}
