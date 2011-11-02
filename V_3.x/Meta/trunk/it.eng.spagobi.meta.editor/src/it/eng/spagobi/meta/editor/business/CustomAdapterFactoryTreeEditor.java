/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.editor.business;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IUpdateableItemText;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class CustomAdapterFactoryTreeEditor extends AdapterFactoryTreeEditor {

	BusinessModelEditor businessModelEditor;
	/**
	 * @param tree
	 * @param adapterFactory
	 */
	public CustomAdapterFactoryTreeEditor(Tree tree,
			AdapterFactory adapterFactory, BusinessModelEditor businessModelEditor) {
		super(tree, adapterFactory);
		this.businessModelEditor = businessModelEditor;
	}
	
	  @Override
	  /*
	   * This code was found in AdapterFactoryTreeEditor and modified for setting Business Model Editor to dirty
	   */
	  protected void editItem(final TreeItem treeItem)
	  {
	    final Object object = treeItem.getData();
	    final IUpdateableItemText updateableItemText = (IUpdateableItemText)adapterFactory.adapt(object, IUpdateableItemText.class);
	    if (updateableItemText != null)
	    {
	      String string = updateableItemText.getUpdateableText(object);

	      if (string != null)
	      {
	        horizontalAlignment = SWT.LEFT;
	        // grabHorizontal = true;
	        minimumWidth = Math.max(50, treeItem.getBounds().width);
	    
	        if (System.getProperty("EMF_COMBO_TEST") == null)
	        {
	          final Text text = new Text(tree, SWT.BORDER);
	          setEditor(text, treeItem);
	          text.setFocus();
	          text.setText(string);


	          text.setSelection(0, string.length());
	          
	          
	          
	          //This is the added part
	          text.addModifyListener(new ModifyListener()
	             {
	               public void modifyText(ModifyEvent event)
	               {
		  	          	businessModelEditor.setDirty(true);
		  	          	businessModelEditor.firePropertyChange(IEditorPart.PROP_DIRTY);
	               }
	             });
	          //---------------------
	      
	          text.addFocusListener
	           (new FocusAdapter()
	            {
	              @Override
	              public void focusLost(FocusEvent event)
	              {
	                updateableItemText.setText(object, text.getText());
	                text.setVisible(false);
	              }
	            });
	          text.addKeyListener
	           (new KeyAdapter()
	            {
	              @Override
	              public void keyPressed(KeyEvent event)
	              {
	                if (event.character == '\r' || event.character == '\n')
	                {
	                  updateableItemText.setText(object, text.getText());
	                  setEditor(null);
	                  text.dispose();
	                }
	                else if (event.character == '\033')
	                {
	                  setEditor(null);
	                  text.dispose();
	                }
	              }
	            });
	        }
	        else
	        {
	          final Combo combo = new Combo(tree, SWT.BORDER);
	          setEditor(combo, treeItem);
	          combo.setFocus();
	          combo.setText(string);
	          combo.setSelection(new Point(0,string.length()));
	          combo.add("Item 1");
	          combo.add("Item 2");
	          combo.add("Item 3");
	          combo.add("Item 4");
	      
	          combo.addFocusListener
	           (new FocusAdapter()
	            {
	              @Override
	              public void focusLost(FocusEvent event)
	              {
	                System.out.println("Combo lost focus");
	                updateableItemText.setText(object, combo.getText());
	                combo.setVisible(false);
	              }
	            });

	          combo.addKeyListener
	           (new KeyAdapter()
	            {
	              @Override
	              public void keyPressed(KeyEvent event)
	              {
	                System.out.println("Combo key event");
	                if (event.character == '\r' || event.character == '\n')
	                {
	                  updateableItemText.setText(object, combo.getText());
	                  setEditor(null);
	                  combo.dispose();
	                }
	                else if (event.character == '\033')
	                {
	                  setEditor(null);
	                  combo.dispose();
	                }
	              }
	            });

	          combo.addModifyListener
	            (new ModifyListener()
	             {
	               public void modifyText(ModifyEvent event)
	               {
	                 System.out.println("Combo modified");
	               }
	             });
	        }
	      }
	    }
	  }

}
