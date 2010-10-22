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
package it.eng.spagobi.meta.editor.views;

import java.util.Date;

import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class CustomizedPropertyDescriptor extends PropertyDescriptor {
	

	public CustomizedPropertyDescriptor(Object object, IItemPropertyDescriptor itemPropertyDescriptor) {
		super(object, itemPropertyDescriptor);
	}

	public CellEditor createPropertyEditor(Composite composite) {
		   CellEditor result = super.createPropertyEditor(composite);
		   if(result == null) return result;
		   
		   EStructuralFeature feature = (EStructuralFeature)itemPropertyDescriptor.getFeature(object);
		   
		   EClassifier eType = feature.getEType();
		   System.err.println(feature.getName());
		   
		  
		   if (feature.getName().equalsIgnoreCase("id")) {
		      //EDataType eDataType = (EDataType) eType;
		      if(true /*eDataType.getInstanceClass() == Date.class*/){
		         result = new ExtendedDialogCellEditor(composite, getEditLabelProvider()){
		             protected Object openDialogBox(Control cellEditorWindow) {
		                final DateTime dateTime[] = new DateTime[1];
		                final Date[] date = new Date[1];
		                Dialog d = new Dialog(cellEditorWindow.getShell()){
		                   protected Control createDialogArea(Composite parent) {
		                      Composite dialogArea = (Composite) super.createDialogArea(parent);
		                      dateTime[0] = new DateTime(dialogArea, SWT.CALENDAR);
		                      dateTime[0].addSelectionListener(new SelectionAdapter(){
		                          public void widgetSelected(SelectionEvent e) {
		                              Date dateValue = new Date();
		                              dateValue.setYear(dateTime[0].getYear());
		                              dateValue.setMonth(dateTime[0].getMonth());
		                              dateValue.setDate(dateTime[0].getDay());
		                              date[0] = dateValue;
		                              super.widgetSelected(e);
		                           }
		                      });
		                      return dialogArea;
		                   }
		                };
		               d.setBlockOnOpen(true);
		               d.open();
		               if(d.getReturnCode() == Dialog.OK){
		                  return date[0];
		               }
		               return null;
		            }
		         };
		      }
		   }
		
		   return result;
		}

}
