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



import java.net.URL;

import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.model.business.editor.BusinessModelEditor;
import it.eng.spagobi.meta.model.physical.presentation.PhysicalModelEditor;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.MultiEditor;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIModelEditor extends MultiEditor {
	
	private SashForm splitContainer;
	private IEditorPart[] innerEditors;
	private Control[] innerEditorWrapperContainers;
	private CLabel[] innerEditorTitle;
	
	
	private boolean firstEditor = true;
	private Label expandCollapseButton;
	private Image iconCollapse, iconExpand;
	
	public static final String EDITOR_ID = "it.eng.spagobi.meta.model.presentation.SpagoBIModelEditorID";
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	private static final Logger logger = LoggerFactory.getLogger(SpagoBIModelEditor.class);
	
	
	@Override
	public void createPartControl(Composite parentContainer) {
		
		PhysicalModelEditor physicalModelEditor;
		BusinessModelEditor businessModelEditor;
		
		logger.trace("IN");
		
		innerEditors = getInnerEditors();
		Assert.assertTrue("Multi editor [" + SpagoBIModelEditor.class.getName() + "] cannot be composed by [" + innerEditors.length + " inner editors]", innerEditors.length == 2);
		
		physicalModelEditor = null;
		businessModelEditor = null;
		for (int i = 0; i < innerEditors.length; i++) {
			if(innerEditors[i] instanceof PhysicalModelEditor) {
				physicalModelEditor = (PhysicalModelEditor)innerEditors[i];
			} else if(innerEditors[i] instanceof BusinessModelEditor) {
				businessModelEditor = (BusinessModelEditor)innerEditors[i];
			} else {
				throw new SpagoBIPluginException("Multi editor [" + SpagoBIModelEditor.class.getName() + "] cannot contain an inner editor of type [" + innerEditors[i].getClass().getName() + "]");
			}
			logger.debug("Inner editor at index [" + i + "] is equal to [" + innerEditors[i].getClass().getName() + "]");
		}
		Assert.assertNotNull("Multi editor [" + SpagoBIModelEditor.class.getName() + "] must contain an inner editor of type [" + PhysicalModelEditor.class.getName() + " inner editors]", physicalModelEditor);
		Assert.assertNotNull("Multi editor [" + SpagoBIModelEditor.class.getName() + "] must contain an inner editor of type [" + BusinessModelEditor.class.getName() + " inner editors]", businessModelEditor);
		
		if(RL.getPropertyAsString("model.presentation.lefteditor.width", "business").equalsIgnoreCase("business")) {
			innerEditors[LEFT] = businessModelEditor;
			innerEditors[RIGHT] = physicalModelEditor;
		} else {
			innerEditors[LEFT] = physicalModelEditor;
			innerEditors[RIGHT] = businessModelEditor;
		}
		
		innerEditorWrapperContainers =  new Control[ innerEditors.length ];
		innerEditorTitle = new CLabel[ innerEditors.length ];
		
		Composite mainContainer = new Composite(parentContainer, SWT.BORDER);
		mainContainer.setLayout(new FillLayout());
		splitContainer = new SashForm(mainContainer, SWT.HORIZONTAL);
                   
		for (int i = 0; i < innerEditors.length; i++) {
			createInnerEditorContainer(i, splitContainer);
		}
		
		splitContainer.setWeights(new int[]{
			RL.getPropertyAsInteger("model.presentation.lefteditor.width", 80),
			RL.getPropertyAsInteger("model.presentation.righteditor.width", 20)
		});
		
		/*
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener(){
			public void resourceChanged(IResourceChangeEvent event) {
				logger.debug("resourceChanged");
				logger.debug("Change type is equal to REMOVE [{}]", event.getDelta().getKind() == IResourceDelta.REMOVED);
				
			}
		}, IResourceChangeEvent.POST_CHANGE);
		*/
	}
	
	protected void createInnerEditorContainer(int innerEditorIndex, Composite parentContainer) {
		ViewForm innerEditorWrapperContainer;
		Composite innerEditorContainer;
		IEditorPart innerEditor;
		
		innerEditor = innerEditors[innerEditorIndex];		
		innerEditorWrapperContainer = new ViewForm(parentContainer, SWT.NONE);
		innerEditorWrapperContainers[innerEditorIndex] = innerEditorWrapperContainer;
		
		innerEditorWrapperContainer.marginWidth = 0;
		innerEditorWrapperContainer.marginHeight = 0;
		createInnerEditorTitle(innerEditorIndex, innerEditorWrapperContainer);
		
		innerEditorContainer = createInnerPartControl(innerEditorWrapperContainer, innerEditor);
		innerEditorWrapperContainer.setContent(innerEditorContainer);
		
		refreshInnerEditorTitle(innerEditor, innerEditorTitle[innerEditorIndex]);
		
		final int index = innerEditorIndex;
		innerEditor.addPropertyListener(new IPropertyListener() {
			public void propertyChanged(Object source, int property) {
				if (property == IEditorPart.PROP_DIRTY || property == IWorkbenchPart.PROP_TITLE)
					if (source instanceof IEditorPart)
						refreshInnerEditorTitle((IEditorPart) source, innerEditorTitle[index]);
			}
		});
	}
	
	protected void createInnerEditorTitle(int index, ViewForm innerEditorWrapperContainer) {
		CLabel titleLabel;
		
		titleLabel = new CLabel(innerEditorWrapperContainer, SWT.SHADOW_NONE);
		//hookFocus(titleLabel);
		titleLabel.setAlignment(SWT.LEFT);
		titleLabel.setBackground(null, null);
		innerEditorWrapperContainer.setTopLeft(titleLabel);
		
		iconExpand = ImageDescriptor.createFromURL( (URL)RL.getImage("model.presentation.button.expand") ).createImage(); 	
		iconCollapse = ImageDescriptor.createFromURL( (URL)RL.getImage("model.presentation.button.collapse") ).createImage(); 

		//create image and listener to hide physical model editor
		if (firstEditor == true) {
			expandCollapseButton = new Label(innerEditorWrapperContainer, SWT.NONE);
			expandCollapseButton.setImage(iconExpand);
			innerEditorWrapperContainer.setTopRight(expandCollapseButton);
			
			expandCollapseButton.addMouseListener(new MouseListener() {
		        public void mouseDoubleClick(MouseEvent e) { }

		        public void mouseDown(MouseEvent e) {
		        	if(splitContainer.getMaximizedControl() == innerEditorWrapperContainers[0]){
		        		splitContainer.setMaximizedControl(null);	
				        expandCollapseButton.setImage(iconExpand);
			        } else {
				        splitContainer.setMaximizedControl(innerEditorWrapperContainers[0]);
				        expandCollapseButton.setImage(iconCollapse);
			        }		        	
		        }

		        public void mouseUp(MouseEvent e) { }
		    });
			
			firstEditor = false;
		}

		innerEditorTitle[index] = titleLabel;
	}
	
	public void refreshInnerEditorTitle(IEditorPart editor, CLabel label) {
		
		if((label == null) || label.isDisposed()) return;
		
		String title = editor.getTitle();
		if (editor.isDirty()) {
			title = "*" + title; //$NON-NLS-1$
		}
		label.setText(title);
		
		Image image = editor.getTitleImage();
		if (image != null && !image.equals(label.getImage())) {
			label.setImage(image);
		}
		
		label.setToolTipText(editor.getTitleToolTip());
	}

	
	
	
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		logger.trace("IN");
		super.doSave(progressMonitor);
		logger.trace("OUT");
	}

	
	
	
	
	protected void drawGradient(IEditorPart innerEditor, Gradient g) {
		CLabel label = innerEditorTitle[getIndex(innerEditor)];
		if((label == null) || label.isDisposed()) return;
			
		label.setForeground(g.fgColor);
		label.setBackground(g.bgColors, g.bgPercents);
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
