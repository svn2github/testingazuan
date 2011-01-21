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
package it.eng.spagobi.meta.model.presentation;



import it.eng.spagobi.meta.model.test.TestEditorPlugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiEditor;
import org.eclipse.ui.part.MultiEditorInput;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIModelEditor extends MultiEditor {
	
	private CLabel innerEditorTitle[];
	private SashForm sashForm;
	public static final String PLUGIN_ID = "it.eng.spagobi.meta.model.presentation.SpagoBIModelEditorID";
	private IEditorPart innerEditors[];
	private Control innerViewForm[] = new Control[2];
	private boolean firstEditor = true;
	private Label closeLbl;
	private Image iconCollapse, iconExpand;
	
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		System.err.println("model.doSave");
		super.doSave(progressMonitor);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent = new Composite(parent, SWT.BORDER);
		parent.setLayout(new FillLayout());
		sashForm = new SashForm(parent, SWT.HORIZONTAL);
		
		innerEditors = getInnerEditors();
		
		for (int i = 0; i < innerEditors.length; i++) {
			final IEditorPart e = innerEditors[i];
			ViewForm viewForm = new ViewForm(sashForm, SWT.NONE);
			viewForm.marginWidth = 0;
			viewForm.marginHeight = 0;

			//added store viewform references
			innerViewForm[i] = viewForm;
			
			createInnerEditorTitle(i, viewForm);
						
			Composite content = createInnerPartControl(viewForm,e);
					
			viewForm.setContent(content);
			updateInnerEditorTitle(e, innerEditorTitle[i]);
			
			System.err.println(innerEditors[i].getEditorInput().getClass().getName());
			
			final int index = i;
			e.addPropertyListener(new IPropertyListener() {
				public void propertyChanged(Object source, int property) {
					if (property == IEditorPart.PROP_DIRTY || property == IWorkbenchPart.PROP_TITLE)
						if (source instanceof IEditorPart)
							updateInnerEditorTitle((IEditorPart) source, innerEditorTitle[index]);
				}
			});
		}
		sashForm.setWeights(new int[]{80,20});

		//*****
		//Active selection testing
		//*****
		/*
		
		IWorkbenchPage page = getSite().getPage(); 
		
		//the current selection in the entire page
		ISelection selection = page.getSelection();
		
		//Set selection provider
		getSite().setSelectionProvider(((BusinessModelEditor)innerEditors[1]).getSelectionViewer());

		
		//the current selection in the navigator view
		//selection = page.getSelection(IPageLayout.ID_RES_NAV);


		//add a listener

		ISelectionListener sl = new ISelectionListener() {
		      public void selectionChanged(IWorkbenchPart part, ISelection sel) {
		         System.out.println("Selection is: " + sel);
		         getSite().setSelectionProvider(((BusinessModelEditor)innerEditors[1]).getSelectionViewer());

		         //getPropertySheetPage();
		         //propertySheetPage.refresh();
		      }
		   };
		   
		
		page.addSelectionListener(sl);
		 
		
		//the active part
		IWorkbenchPart active = page.getActivePart();
		System.out.println("Active part: "+ active);
		//adding a listener
		IPartListener2 pl = new IPartListener2() {
			@Override
			public void partActivated(IWorkbenchPartReference ref)
		    {
		         System.out.println("Active: "+ref.getTitle());
		         //getPropertySheetPage();
		         //propertySheetPage.refresh();
		    }

			@Override
			public void partBroughtToTop(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void partDeactivated(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void partOpened(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void partHidden(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void partVisible(IWorkbenchPartReference partRef) {
				 System.out.println("Visible: "+partRef.getTitle());
				
			}

			@Override
			public void partInputChanged(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub
				
			}
		};
		
		page.addPartListener(pl);
		*/
		

	}
	
	/*
	public Object getAdapter(Class key) {
		if (key.equals(IPropertySheetPage.class)) {
			return getPropertySheetPage();
		}
		else if (key == IPropertySource.class) {
			return null;
		}
		else {
			return super.getAdapter(key);
		}
	}
	
	*/


	/**
	 * Draw the gradient for the specified editor.
	 */
	protected void drawGradient(IEditorPart innerEditor, Gradient g) {
		CLabel label = innerEditorTitle[getIndex(innerEditor)];
		if((label == null) || label.isDisposed())
			return;
			
		label.setForeground(g.fgColor);
		label.setBackground(g.bgColors, g.bgPercents);
	}
	/*
	 * Create the label for each inner editor. 
	 */
	protected void createInnerEditorTitle(int index, ViewForm parent) {
		CLabel titleLabel = new CLabel(parent, SWT.SHADOW_NONE);
		//hookFocus(titleLabel);
		titleLabel.setAlignment(SWT.LEFT);
		titleLabel.setBackground(null, null);
		parent.setTopLeft(titleLabel);
		
		
		ImageDescriptor imageDescriptorExpand = ExtendedImageRegistry.INSTANCE.getImageDescriptor(TestEditorPlugin.INSTANCE.getImage("expand"));
		iconExpand = imageDescriptorExpand.createImage();
		
		ImageDescriptor imageDescriptorCollapse = ExtendedImageRegistry.INSTANCE.getImageDescriptor(TestEditorPlugin.INSTANCE.getImage("collapse"));
		iconCollapse = imageDescriptorCollapse.createImage();
		
		
		//create image and listener to hide physical model editor
		if (firstEditor == true) {
			closeLbl = new Label(parent, SWT.NONE);
			closeLbl.setImage(iconExpand);
			parent.setTopRight(closeLbl);
			
			closeLbl.addMouseListener(new MouseListener() {
		        public void mouseDoubleClick(MouseEvent e) {

		        }

		        public void mouseDown(MouseEvent e) {
		            if(sashForm.getMaximizedControl() == innerViewForm[0]){
				           sashForm.setMaximizedControl(null);	
				           closeLbl.setImage(iconExpand);
			            }
			            else {
				           sashForm.setMaximizedControl(innerViewForm[0]);
				           closeLbl.setImage(iconCollapse);
			            }		        	
		        }

		        public void mouseUp(MouseEvent e) {
		        }
		    });
			
			firstEditor = false;
		}

		if (innerEditorTitle == null)
			innerEditorTitle = new CLabel[getInnerEditors().length];
		innerEditorTitle[index] = titleLabel;
	}
	/*
	 * Update the tab for an editor.  This is typically called
	 * by a site when the tab title changes.
	 */
	public void updateInnerEditorTitle(IEditorPart editor, CLabel label) {
		if((label == null) || label.isDisposed())
			return;
		String title = editor.getTitle();
		if (editor.isDirty())
			title = "*" + title; //$NON-NLS-1$
		label.setText(title);
		Image image = editor.getTitleImage();
		if (image != null)
			if (!image.equals(label.getImage()))
				label.setImage(image);
		label.setToolTipText(editor.getTitleToolTip());
	}
	
	protected int getIndex(IEditorPart editor) {
		IEditorPart innerEditors[] = getInnerEditors();
		for (int i = 0; i < innerEditors.length; i++) {
			if (innerEditors[i] == editor)
				return i;
		}
		return -1;
	}

	/*
	private PropertySheetPage propertySheetPage;
	public IPropertySheetPage getPropertySheetPage() {
		
		AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) ((BusinessModelEditor)innerEditors[1]).getEditingDomain();
		ComposedAdapterFactory adapterFactory = (ComposedAdapterFactory) ((BusinessModelEditor)innerEditors[1]).getAdapterFactory();
		if (propertySheetPage == null) {
			propertySheetPage =
				new ExtendedPropertySheetPage(editingDomain) {
					@Override
					public void setSelectionToViewer(List<?> selection) {
						((BusinessModelEditor)innerEditors[1]).setSelectionToViewer(selection);
						((BusinessModelEditor)innerEditors[1]).setFocus();
					}

				
					
				};
			propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));
		}

		return propertySheetPage;
	}
	 */

}
