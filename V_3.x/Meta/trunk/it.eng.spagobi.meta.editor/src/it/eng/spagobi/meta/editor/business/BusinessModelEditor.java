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

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.editor.dnd.BusinessModelDragSourceListener;
import it.eng.spagobi.meta.editor.dnd.BusinessModelDropTargetListener;
import it.eng.spagobi.meta.editor.properties.CustomizedAdapterFactoryContentProvider;
import it.eng.spagobi.meta.editor.properties.CustomizedBusinessPropertySheetPage;
import it.eng.spagobi.meta.editor.properties.CustomizedPhysicalPropertySheetPage;
import it.eng.spagobi.meta.editor.properties.CustomizedPropertySheetSorter;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.analytical.provider.AnalyticalModelItemProviderAdapterFactory;
import it.eng.spagobi.meta.model.behavioural.provider.BehaviouralModelItemProviderAdapterFactory;
import it.eng.spagobi.meta.model.business.provider.BusinessModelItemProviderAdapterFactory;
import it.eng.spagobi.meta.model.olap.provider.OlapModelItemProviderAdapterFactory;
import it.eng.spagobi.meta.model.physical.provider.PhysicalModelItemProviderAdapterFactory;
import it.eng.spagobi.meta.model.provider.ModelItemProviderAdapterFactory;
import it.eng.spagobi.meta.model.validator.ModelValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.UnwrappingSelectionProvider;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BusinessModelEditor
	extends MultiPageEditorPart
	implements IEditingDomainProvider, IMenuListener, IViewerProvider {
	
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	
	public static final String EDITOR_ID = BusinessModelEditor.class.getName();
	
	
	// ================================================================================
	// IEditingDomainProvider
	// ================================================================================
	/**
	 * This keeps track of the editing domain that is used to track all changes to the model.
	 */
	protected AdapterFactoryEditingDomain editingDomain;

	
	/**
	 * This returns the editing domain as required by the {@link IEditingDomainProvider} interface.
	 * This is important for implementing the static methods of {@link AdapterFactoryEditingDomain}
	 * and for supporting {@link org.eclipse.emf.edit.ui.action.CommandAction}.
	 */
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	
	// ================================================================================
	// Model views: only one for the moment (i.e. tree)
	// ================================================================================
	
	/**
	 * This is the one adapter factory used for providing views of the model.
	 */
	protected ComposedAdapterFactory adapterFactory;

	/**
	 * This is the viewer that shadows the selection in the content outline.
	 * The parent relation must be correctly defined for this to work.
	 */
	protected TreeViewer modelTreeViewer;

	/**
	 * This keeps track of the active viewer pane, in the book.
	 */
	protected ViewerPane currentViewerPane;

	/**
	 * This keeps track of the active content viewer, which may be either one of the viewers 
	 * in the pages or the content outline viewer.
	 */
	protected Viewer currentViewer;
	
	// ================================================================================
	
	
	
	
	
	// ================================================================================
	
	
	/**
	 * This is the property sheet page.
	 */
	protected PropertySheetPage propertySheetPage;

	/**
	 * This listens to which ever viewer is active.
	 */
	protected ISelectionChangedListener selectionChangedListener;

	protected BusinessModelEditorSelectionProvider selectionProvider;

	

	
	/**
	 * This listens for when the outline becomes active
	 */
	protected IPartListener partListener; 
	
	

	



	

		
		
	private static Logger logger = LoggerFactory.getLogger(BusinessModelEditor.class);
	
	
	// =================================================================================================
	
	/**
	 * This creates a model editor.
	 */
	public BusinessModelEditor() {
		super();
		initializeAdapterFactory();
		initializeEditingDomain();
	}

	/**
	 * This sets up the editing domain for the model editor.
	 */
	protected void initializeEditingDomain() {
		BasicCommandStack commandStack;
		Map<Resource, Boolean> resourceToReadOnlyMap;
		
		logger.trace("IN");
		
		commandStack = new BasicCommandStack();
		commandStack.addCommandStackListener(new BusinessModelEditorCommandStackListener(this));
		resourceToReadOnlyMap = new HashMap<Resource, Boolean>();

		editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, resourceToReadOnlyMap);
		
		logger.trace("OUT");
	}
	
	/**
	 * Create an adapter factory that yields item providers.
	 */
	protected void initializeAdapterFactory() {
		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ModelItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new PhysicalModelItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new BusinessModelItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new OlapModelItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new BehaviouralModelItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new AnalyticalModelItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
	}

	

	
	@Override
	public void init(IEditorSite site, IEditorInput editorInput) {
		setSite(site);
		setInputWithNotify(editorInput);
		setPartName(editorInput.getName());
		
		selectionProvider = new BusinessModelEditorSelectionProvider(this);
		partListener = new BusinessModelPartListener(this, logger);
		
		site.setSelectionProvider(selectionProvider);
		site.getPage().addPartListener(partListener);
		
		loadModel();
	}
	
	/**
	 * This is the method used by the framework to install your own controls.
	 */
	@Override
	public void createPages() {
		
		//loadModel();

		// Only creates the pages if there is something that can be edited
		if (!getEditingDomain().getResourceSet().getResources().isEmpty()) {
			// Create a page for the selection tree view.
			//
			createSelectionTreeView();

			getSite().getShell().getDisplay().asyncExec
				(new Runnable() {
					 public void run() {
						 setActivePage(0);
					 }
				 });
		}

		// Ensures that this editor will only display the page's tab
		// area if there are more than one page
		//
		getContainer().addControlListener
			(new ControlAdapter() {
				boolean guard = false;
				@Override
				public void controlResized(ControlEvent event) {
					if (!guard) {
						guard = true;
						hideTabs();
						guard = false;
					}
				}
			 });

		
	}	
	
	public void createSelectionTreeView() {
		ViewerPane viewerPane =
			new ViewerPane(getSite().getPage(), BusinessModelEditor.this) {
				@Override
				public Viewer createViewer(Composite composite) {
					Tree tree = new Tree(composite, SWT.MULTI);
					TreeViewer newTreeViewer = new TreeViewer(tree);
					return newTreeViewer;
				}
				@Override
				public void requestActivation() {
					super.requestActivation();
					setCurrentViewerPane(this);
				}
			};
		viewerPane.createControl(getContainer());

		modelTreeViewer = (TreeViewer)viewerPane.getViewer();
		modelTreeViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
		modelTreeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
		
		modelTreeViewer.getTree().addKeyListener(new KeyAdapter() {
		      public void keyPressed(KeyEvent event) {
		    	  if (event.keyCode == SWT.DEL) {
		    		 // stops propagation
		    		 event.doit = false;
		    	  } else if(event.keyCode == SWT.F5) {
		    		  refreshViewer();
		    	  }
		    }
		});
		
		URI rootObjectURI = ((BusinessModelEditorInput)getEditorInput()).getRootObjectURI();
		EObject rootObject = editingDomain.getResourceSet().getEObject(rootObjectURI, false);
		
		//selectionViewer.setInput(editingDomain.getResourceSet());
		modelTreeViewer.setInput(rootObject);
		//selectionViewer.setSelection(new StructuredSelection(editingDomain.getResourceSet().getResources().get(0)), true);
		//selectionViewer.setSelection(new StructuredSelection(ro), true);
		viewerPane.setTitle(editingDomain.getResourceSet());

		new AdapterFactoryTreeEditor(modelTreeViewer.getTree(), adapterFactory);

		createContextMenuFor(modelTreeViewer);
		int pageIndex = addPage(viewerPane.getControl());
		setPageText(pageIndex, RL.getString("business.editor.selectionpage.label"));
		
		modelTreeViewer.expandToLevel(2);
	}

	

	// =================================================================================================
	

	/**
	 * This sets the selection into whichever viewer is active.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelectionToViewer(Collection<?> collection) {
		final Collection<?> theSelection = collection;
		// Make sure it's okay.
		//
		if (theSelection != null && !theSelection.isEmpty()) {
			Runnable runnable =
				new Runnable() {
					public void run() {
						// Try to select the items in the current content viewer of the editor.
						//
						if (currentViewer != null) {
							currentViewer.setSelection(new StructuredSelection(theSelection.toArray()), true);
						}
					}
				};
			getSite().getShell().getDisplay().asyncExec(runnable);
		}
	}
	
	public void refreshViewer() {
		if (currentViewer != null) {
			currentViewer.refresh();
			if(currentViewer instanceof TreeViewer) {
				TreeViewer treeViewer = (TreeViewer)currentViewer;
				treeViewer.expandToLevel(2);
			}
		}
	}

	
	
	

	public void setCurrentViewerPane(ViewerPane viewerPane) {
		if (currentViewerPane != viewerPane) {
			if (currentViewerPane != null) {
				currentViewerPane.showFocus(false);
			}
			currentViewerPane = viewerPane;
		}
		setCurrentViewer(currentViewerPane.getViewer());
	}

	/**
	 * This makes sure that one content viewer, either for the current page or the outline view,
	 * if it has focus, is the current one.
	 */
	public void setCurrentViewer(Viewer viewer) {
		// If it is changing...
		if (currentViewer != viewer) {
			if (selectionChangedListener == null) {
				// Create the listener on demand.
				selectionChangedListener =
					new ISelectionChangedListener() {
						// This just notifies those things that are affected by the section.
						public void selectionChanged(SelectionChangedEvent selectionChangedEvent) {
							selectionProvider.setSelection(selectionChangedEvent.getSelection());
						}
					};
			}

			// Stop listening to the old one.
			if (currentViewer != null) {
				currentViewer.removeSelectionChangedListener(selectionChangedListener);
			}

			// Start listening to the new one.
			if (viewer != null) {
				viewer.addSelectionChangedListener(selectionChangedListener);
			}

			// Remember it.
			//
			currentViewer = viewer;

			// Set the editors selection based on the current viewer's selection.
			selectionProvider.setSelection(currentViewer == null ? StructuredSelection.EMPTY : currentViewer.getSelection());
		}
	}

	/**
	 * This returns the viewer as required by the {@link IViewerProvider} interface.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Viewer getViewer() {
		return currentViewer;
	}

	/**
	 * This creates a context menu for the viewer and adds a listener as well registering the menu for extension.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 */
	protected void createContextMenuFor(StructuredViewer viewer) {
		MenuManager contextMenu = new MenuManager("#PopUp");
		contextMenu.add(new Separator("additions"));
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(this);
		Menu menu= contextMenu.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(contextMenu, new UnwrappingSelectionProvider(viewer));

		URI rootObjectURI = ((BusinessModelEditorInput)getEditorInput()).getRootObjectURI();
		EObject rootObject = editingDomain.getResourceSet().getEObject(rootObjectURI, false);

		
		//set drop target
		Transfer[] transferTypes = new Transfer[]{ TextTransfer.getInstance(),LocalSelectionTransfer.getTransfer()  };
		DropTargetListener dropTargetListener = new BusinessModelDropTargetListener(viewer, rootObject,editingDomain);
		viewer.addDropSupport(DND.DROP_MOVE, transferTypes, dropTargetListener);
		
		//set dragSource (drag in the same tree)
		DragSourceListener dragSourceListener = new BusinessModelDragSourceListener(viewer, rootObject);
		viewer.addDragSupport(DND.DROP_MOVE,  new Transfer[] {LocalSelectionTransfer.getTransfer()}, dragSourceListener);
		
	}

	

	

	

	/**
	 * If there is just one page in the multi-page editor part,
	 * this hides the single tab at the bottom.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void hideTabs() {
		if (getPageCount() <= 1) {
			setPageText(0, "");
			if (getContainer() instanceof CTabFolder) {
				((CTabFolder)getContainer()).setTabHeight(1);
				Point point = getContainer().getSize();
				getContainer().setSize(point.x, point.y + 6);
			}
		}
	}

	/**
	 * If there is more than one page in the multi-page editor part,
	 * this shows the tabs at the bottom.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void showTabs() {
		if (getPageCount() > 1) {
			setPageText(0, RL.getString("business.editor.selectionpage.label"));
			if (getContainer() instanceof CTabFolder) {
				((CTabFolder)getContainer()).setTabHeight(SWT.DEFAULT);
				Point point = getContainer().getSize();
				getContainer().setSize(point.x, point.y - 6);
			}
		}
	}



	/**
	 * This is how the framework determines which interfaces we implement.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IPropertySheetPage.class)) {
			return getPropertySheetPage();
		}
		else {
			return super.getAdapter(key);
		}
	}

	

	/**
	 * This accesses a cached version of the property sheet.
	 */
	public PropertySheetPage getPropertySheetPage() {
		if (propertySheetPage == null) {
			CustomizedPropertySheetSorter propertySheetSorter = new CustomizedPropertySheetSorter();
			propertySheetPage = new CustomizedBusinessPropertySheetPage(this, propertySheetSorter);
			CustomizedAdapterFactoryContentProvider adapterFactoryContentProvider = new CustomizedAdapterFactoryContentProvider(adapterFactory);
			adapterFactoryContentProvider.setEditor(this);
			propertySheetPage.setPropertySourceProvider(adapterFactoryContentProvider);
			isDirty();
		}
	
		return propertySheetPage;
	}

	
	
	
	
	
	
	
	boolean isDirty;
	
	
	/**
	 * This is for implementing {@link IEditorPart} and simply tests the command stack.
	 * @generated
	 */
	@Override
	public boolean isDirty() {
		boolean b = isDirty || ((BasicCommandStack)editingDomain.getCommandStack()).isSaveNeeded();
		return isDirty || ((BasicCommandStack)editingDomain.getCommandStack()).isSaveNeeded();
	}
	
	public void setDirty(boolean isDirty){
		this.isDirty = isDirty;
	}
	
	/**
	 * This is the method called to load a resource into the editing domain's 
	 * resource set based on the editor's input.
	 */
	public void loadModel() {
		URI resourceURI = ((BusinessModelEditorInput)getEditorInput()).getResourceFileURI();
		try {
			Resource resource = editingDomain.getResourceSet().getResource(resourceURI, true);
			List<EObject> eObjects = resource.getContents();
			Model model = (Model)eObjects.get(0);
			ModelValidator validator = new ModelValidator();
			if(validator.validate(model) == false) {
				showError("Impossible to load model", validator.getDiagnosticMessage());
			}
		} catch (Throwable t) {
			showError("Impossible to load model", "An eror occurred while loading model: \n" + t.getMessage());
			t.printStackTrace();
		}
	}

	/**
	 * This is for implementing {@link IEditorPart} and simply saves the model file.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 */
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		
		logger.trace("IN");
		/*
		// Do the work within an operation because this is a long running activity that modifies the workbench.
		WorkspaceModifyOperation operation =
			new WorkspaceModifyOperation() {
				// This is the method that gets invoked when the operation runs.
				//
				@Override
				public void execute(IProgressMonitor monitor) {
					BusinessModelEditorInput businessModelEditorInput = (BusinessModelEditorInput)getEditorInput();
					Resource resource = editingDomain.getResourceSet().getResources().get(0);
					URI resourceURI = businessModelEditorInput.getResourceFileURI();
					
					TreeIterator<EObject> it = resource.getAllContents();
					Model spagobiModel = null;
					if(it.hasNext()) {
						spagobiModel = (Model)it.next();
					}
					
					if(spagobiModel != null) {
						logger.debug("Model: " + spagobiModel.getName());
		
						EmfXmiSerializer serializer = new EmfXmiSerializer();
						serializer.serialize(spagobiModel, new File(resourceURI.toFileString()));
						
					    //Force Workspace refresh
						IWorkspace workspace= ResourcesPlugin.getWorkspace();    
						IPath location= Path.fromOSString(resourceURI.toFileString()); 
						IFile ifile= workspace.getRoot().getFileForLocation(location);
				        try {
				        	ifile.refreshLocal(IResource.DEPTH_ZERO, null);
							logger.debug("Refresh Local workspace on [{}]",ifile.getRawLocation().toOSString());
						} catch (CoreException e) {
							logger.error("Refresh Local workspace error [{}]",e);
							e.printStackTrace();
						}
						//*******
						
						logger.debug("doSave");
					} else {
						logger.debug("not saved");
					}
				}
			};
		 */
	
		try {
			BusinessModelEditorInput businessModelEditorInput = (BusinessModelEditorInput)getEditorInput();
			Resource resource = editingDomain.getResourceSet().getResources().get(0);
			URI resourceURI = businessModelEditorInput.getResourceFileURI();
			new ProgressMonitorDialog(getSite().getShell()).run(true, false, new SaveOperation(resource, resourceURI));

			// Refresh the necessary state.
			((BasicCommandStack)editingDomain.getCommandStack()).saveIsDone();
			setDirty(false);
			firePropertyChange(IEditorPart.PROP_DIRTY);
		} catch (Throwable t) {
			Throwable rootCause = t;
			while ( rootCause.getCause() != null ) rootCause = rootCause.getCause();
			showError("Impossible to save model", rootCause.getMessage());
			t.printStackTrace();
		}
		
		logger.trace("OUT");
	}
	
	/**
	 * Show an error dialog box.
	 */
	public void showError(final String title, final String message) {
	  Display.getDefault().asyncExec(new Runnable() {
	    @Override
	    public void run() {
	      MessageDialog.openError(null, title, message);
	    }
	  });
	}	

	

	/**
	 * This always returns true because it is not currently supported.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * This also changes the editor's input.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void doSaveAs() {
		SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
		saveAsDialog.open();
		IPath path = saveAsDialog.getResult();
		if (path != null) {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file != null) {
				doSaveAs(URI.createPlatformResourceURI(file.getFullPath().toString(), true), new FileEditorInput(file));
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void doSaveAs(URI uri, IEditorInput editorInput) {
		(editingDomain.getResourceSet().getResources().get(0)).setURI(uri);
		setInputWithNotify(editorInput);
		setPartName(editorInput.getName());
		IProgressMonitor progressMonitor =
			getActionBars().getStatusLineManager() != null ?
				getActionBars().getStatusLineManager().getProgressMonitor() :
				new NullProgressMonitor();
		doSave(progressMonitor);
	}

	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFocus() {
		if (currentViewerPane != null) {
			currentViewerPane.setFocus();
		}
		else {
			getControl(getActivePage()).setFocus();
		}
	}



	
	public void setStatusLineManager(ISelection selection) {
		IStatusLineManager statusLineManager = getActionBars().getStatusLineManager();

		if (statusLineManager != null) {
			if (selection instanceof IStructuredSelection) {
				Collection<?> collection = ((IStructuredSelection)selection).toList();
				switch (collection.size()) {
					case 0: {
						
						statusLineManager.setMessage(RL.getString("business.editor.status.noobjectselected"));
						break;
					}
					case 1: {
						String text = new AdapterFactoryItemDelegator(adapterFactory).getText(collection.iterator().next());
						statusLineManager.setMessage(RL.getString("business.editor.status.singleobjectselected", new Object[]{text}));
						break;
					}
					default: {
						statusLineManager.setMessage(RL.getString("business.editor.status.multiobjectselected", new Object[]{Integer.toString(collection.size())} ));
						break;
					}
				}
			}
			else {
				statusLineManager.setMessage("");
			}
		}
	}


	

	/**
	 * This implements {@link org.eclipse.jface.action.IMenuListener} to help fill the context menus with contributions from the Edit menu.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void menuAboutToShow(IMenuManager menuManager) {
		((IMenuListener)getEditorSite().getActionBarContributor()).menuAboutToShow(menuManager);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EditingDomainActionBarContributor getActionBarContributor() {
		return (EditingDomainActionBarContributor)getEditorSite().getActionBarContributor();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IActionBars getActionBars() {
		return getActionBarContributor().getActionBars();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}


	@Override
	public void dispose() {
	
		getSite().getPage().removePartListener(partListener);

		adapterFactory.dispose();

		if (getActionBarContributor().getActiveEditor() == this) {
			getActionBarContributor().setActiveEditor(null);
		}

		if (propertySheetPage != null) {
			propertySheetPage.dispose();
		}

		
		super.dispose();
	}

	public void firePropertyChange(int propertyId) {
		super.firePropertyChange(propertyId);
	}
	
	/**
	 * Returns whether the outline view should be presented to the user.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected boolean showOutlineView() {
		return true;
	}
	
	/**
	 * @param selectionViewer the selectionViewer to set
	 */
	public void setSelectionViewer(TreeViewer selectionViewer) {
		this.modelTreeViewer = selectionViewer;
	}

	/**
	 * @return the selectionViewer
	 */
	public TreeViewer getSelectionViewer() {
		return modelTreeViewer;
	}
}
