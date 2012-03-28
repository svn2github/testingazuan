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
package it.eng.spagobi.meta.editor.multi.wizards;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.commons.utils.SpagoBIMetaConstants;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.editor.multi.BusinessTableSelectionPage;
import it.eng.spagobi.meta.editor.multi.SpagoBIModelEditor;
import it.eng.spagobi.meta.editor.multi.SpagoBIModelEditorInput;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.filter.PhysicalTableFilter;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.statushandlers.StatusManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is a simple wizard for creating a new model file.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SpagoBIModelEditorWizard  extends Wizard implements INewWizard {

	protected Model spagobiModel;
	protected String modelPath;
	protected URI fileURI;
	protected String catalogName = null;
	protected String schemaName = null;
	protected String connectionName = null; 
	protected String connectionDriver = null; 	
	protected String connectionUrl = null;
	protected String connectionUsername = null;
	protected String connectionPassword = null;
	protected String connectionDatabaseName = null;
	protected List<String> selectedPhysicalTable = null;
	protected String modelName = null;
	protected Connection connection = null;
	protected List<PhysicalTable> selectedBusinessTable = null;
	protected IPath containerFullPath = null;
	NewModelWizardFileCreationPage newModelWizardFileCreationPage;
	SelectionConnectionPage selectionConnectionPage;
	PhysicalTableSelectionPage physicalTableSelectionPage;
	BusinessTableSelectionPage businessTableSelectionPage;
	
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	private static Logger logger = LoggerFactory.getLogger(SpagoBIModelEditorWizard.class);

	/**
	 * The supported extensions for created files.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<String> FILE_EXTENSIONS =
		Collections.unmodifiableList(Arrays.asList(RL.getString("_UI_BusinessModelEditorFilenameExtensions").split("\\s*,\\s*")));

	/**
	 * A formatted list of supported file extensions, suitable for display.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String FORMATTED_FILE_EXTENSIONS =
		RL.getString("_UI_BusinessModelEditorFilenameExtensions").replaceAll("\\s*,\\s*", ", ");

	/**
	 * This caches an instance of the model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BusinessModelPackage businessModelPackage = BusinessModelPackage.eINSTANCE;

	/**
	 * This caches an instance of the model factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BusinessModelFactory businessModelFactory = businessModelPackage.getBusinessModelFactory();

	/**
	 * Remember the selection during initialization for populating the default container.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IStructuredSelection selection;

	/**
	 * Remember the workbench during initialization.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IWorkbench workbench;

	/**
	 * Caches the names of the types that can be created as the root object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected List<String> initialObjectNames;


	/**
	 * This just records the information.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle(RL.getString("_UI_Wizard_label"));
		setDefaultPageImageDescriptor(ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.NewBusinessModel") ));
	}

	/**
	 * Returns the names of the types that can be created as the root object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<String> getInitialObjectNames() {
		if (initialObjectNames == null) {
			initialObjectNames = new ArrayList<String>();
			for (EClassifier eClassifier : businessModelPackage.getEClassifiers()) {
				if (eClassifier instanceof EClass) {
					EClass eClass = (EClass)eClassifier;
					if (!eClass.isAbstract()) {
						initialObjectNames.add(eClass.getName());
					}
				}
			}
			Collections.sort(initialObjectNames, CommonPlugin.INSTANCE.getComparator());
		}
		return initialObjectNames;
	}



	/**
	 * Do the work after everything is specified.

	 */
	@Override
	public boolean performFinish() {
		try {
			// Remember the file.
			IFile modelIFile = newModelWizardFileCreationPage.createNewFile();
			modelIFile.setPersistentProperty(SpagoBIMetaConstants.DIRTY_MODEL, "true");
			modelPath = modelIFile.getRawLocation().toOSString();
			fileURI = URI.createFileURI(modelPath);			

			createModel();
			saveModel();
			selectModel();
			editModel();
			
			return true;
		} catch (Throwable t) {
			IStatus status = new Status(IStatus.ERROR, SpagoBIMetaEditorPlugin.PLUGIN_ID, IStatus.ERROR, "Impossible to generate metamodel", t);
		    StatusManager.getManager().handle(status, StatusManager.LOG|StatusManager.SHOW);
			return false;
		}
	}
	
	private void createModel() {
		logger.debug("IN");
		
		try {
			spagobiModel = ModelFactory.eINSTANCE.createModel();
			spagobiModel.setName(newModelWizardFileCreationPage.getModelName());
			
			createPhysicalModel();
			createBusinessModel();
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize the model", t);	
		} finally {
			logger.debug("OUT");
		}
	}
	
	private void createPhysicalModel() {
		try {
			catalogName = null;
			schemaName = null;

			connectionName = selectionConnectionPage.getConnectionName();
			logger.debug("Connection name is [{}]",connectionName);
			
			connectionDriver = selectionConnectionPage.getConnectionDriver();
			logger.debug("Connection Driver is [{}]",connectionDriver);
			
			connectionUrl = selectionConnectionPage.getConnectionUrl();
			logger.debug("Connection URL is [{}]",connectionUrl);
			
			connectionUsername = selectionConnectionPage.getConnectionUsername();
			logger.debug("Connection username is [{}]",connectionUsername);
			
			connectionPassword = selectionConnectionPage.getConnectionPassword();
			logger.debug("Connection password is [{}]",connectionPassword);
			
			connectionDatabaseName = selectionConnectionPage.getConnectionDatabaseName();
			logger.debug("Connection databaseName is [{}]",connectionDatabaseName);

			//Getting Catalog Name
			if (selectionConnectionPage.getCatalogName() != null){
				catalogName = selectionConnectionPage.getCatalogName();
				logger.debug("Connection catalog name is [{}]",catalogName);
			}
			//Getting Schema Name
			if (selectionConnectionPage.getSchemaName() != null){
				schemaName = selectionConnectionPage.getSchemaName();
				logger.debug("Connection schema name is [{}]",schemaName);
			}

			//Getting table to import inside physical model
			TableItem[] selectedPhysicalTableItem = physicalTableSelectionPage.getTablesToImport();
			selectedPhysicalTable = new ArrayList<String>();
			for (TableItem item: selectedPhysicalTableItem){
				selectedPhysicalTable.add(item.getText());
			}

			//Getting Model Name
			modelName = newModelWizardFileCreationPage.getModelName();
			//Getting JDBC Connection
			connection = selectionConnectionPage.getConnection();
			
			ProgressMonitorDialog dialogPhysicalModel = new ProgressMonitorDialog(new Shell());
			dialogPhysicalModel.setCancelable(false);
			dialogPhysicalModel.run(true, false, new IRunnableWithProgress(){
				public void run(IProgressMonitor monitor) {
					//Note: this is a non-UI Thread
					monitor.beginTask("Generating Physical Model of SpagoBI Model, please wait...", IProgressMonitor.UNKNOWN);

					//doing task...
					PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
					physicalModelInitializer.setRootModel(spagobiModel);

					//Physical Model initialization
					if (selectedPhysicalTable.isEmpty()){
						spagobiModel.getPhysicalModels().add( physicalModelInitializer.initialize(
								modelName, 
								connection,
								connectionName, 
								connectionDriver,
								connectionUrl,
								connectionUsername,
								connectionPassword,
								connectionDatabaseName,
								catalogName, 
								schemaName
						));		    		
					}
					else {
						//with table filtering
						spagobiModel.getPhysicalModels().add( physicalModelInitializer.initialize(
								modelName, 
								connection,
								connectionName,
								connectionDriver,
								connectionUrl,
								connectionUsername,
								connectionPassword,
								connectionDatabaseName,
								catalogName, 
								schemaName,
								selectedPhysicalTable
						));	
					}
					
					monitor.done();				        
				}
			});
		} catch (InvocationTargetException t) {
			throw new RuntimeException("Impossible to initialize the physical model", t);	
		} catch (InterruptedException e) {
			logger.error("Physical Model generation, InterruptedException [{}]",e);
			e.printStackTrace();
		}
	}
	
	private void createBusinessModel() {
		try {
			
			//Getting physical table to import inside business table
			TableItem[] selectedBusinessTableItem = businessTableSelectionPage.getTablesToImport();
			selectedBusinessTable = new ArrayList<PhysicalTable>();
			PhysicalModel physicalModel = spagobiModel.getPhysicalModels().get(0);
			//check if there are no selected table to import in the Business Model
			if (selectedBusinessTableItem != null){
				for (TableItem item: selectedBusinessTableItem){
					PhysicalTable physicalTable = physicalModel.getTable(item.getText());
					selectedBusinessTable.add(physicalTable);
				}
			}
	
			ProgressMonitorDialog dialogBusinessModel = new ProgressMonitorDialog(new Shell());
			dialogBusinessModel.setCancelable(false);

		
			dialogBusinessModel.run(true, false, new IRunnableWithProgress(){
				public void run(IProgressMonitor monitor) {
					//Note: this is a non-UI Thread
					monitor.beginTask("Generating Business Model of SpagoBI Model, please wait...", IProgressMonitor.UNKNOWN);
					logger.debug("start monitor");
					//doing task...
					//Business Model initialization
					if (selectedBusinessTable.isEmpty()){
						//create empty Business Model
						BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
						spagobiModel.getBusinessModels().add(businessModelInitializer.initializeEmptyBusinessModel(
								modelName, 
								spagobiModel.getPhysicalModels().get(0)
						));

					}
					else {
						//with table filtering
						BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
						spagobiModel.getBusinessModels().add(businessModelInitializer.initialize(
								modelName,
								new PhysicalTableFilter(selectedBusinessTable),
								spagobiModel.getPhysicalModels().get(0)
						));
					}
					//end of task		
					logger.debug("end monitor");
					monitor.done();				        
				}
			});
		} catch (InterruptedException e) {
			logger.error("Business Model generation, InterruptedException [{}]", e);
			e.printStackTrace();
		} catch (Throwable t) {
			throw new RuntimeException("Impossible to initialize the business model", t);	
		} 	
	}
	
	private void saveModel() {
		try {
			addFilePropertyToModel();
			
			WorkspaceModifyOperation operation =
				new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor progressMonitor) {
					try {
						// Create a resource set
						ResourceSet resourceSet = new ResourceSetImpl();
						
						// Create a resource for this file.
						Resource resource = resourceSet.createResource(fileURI);
	
						// Add the initial model object to the contents.
						EObject rootObject = spagobiModel;
						if (rootObject != null) {
							resource.getContents().add(rootObject);
						}
	
						// Save the contents of the resource to the file system.
						Map<Object, Object> options = new HashMap<Object, Object>();
						resource.save(options);
					
					} catch (Exception e) {
						throw new RuntimeException("Impossible perform save operation", e);
					} finally {
						progressMonitor.done();
					}
				}
			};

		
			getContainer().run(false, false, operation);
		} catch (InterruptedException e) {
			logger.error("Business Model generation, InterruptedException [{}]", e);
			e.printStackTrace();
		} catch (Throwable e) {
			throw new RuntimeException("Impossible to save model to file", e);	
		}	
	}

	public void selectModel() {
		try {
			IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = workbenchWindow.getActivePage();
			final IWorkbenchPart activePart = page.getActivePart();
			if (activePart instanceof ISetSelectionTarget) {
				final ISelection targetSelection = new StructuredSelection( new File(modelPath) );
				getShell().getDisplay().asyncExec
				(new Runnable() {
					public void run() {
						((ISetSelectionTarget)activePart).selectReveal(targetSelection);
					}
				});
			}
		} catch (Throwable e) {
			throw new RuntimeException("Impossible to select model", e);	
		}	
	}
	
	private void editModel() {
		try {
			IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = workbenchWindow.getActivePage();
			page.openEditor( 
				new SpagoBIModelEditorInput(new File(modelPath), spagobiModel) , SpagoBIModelEditor.EDITOR_ID					     
			);
			
		} catch (Throwable e) {
			throw new RuntimeException("Impossible to open mode in editor", e);
		}
	}
	
	private void addFilePropertyToModel() {
		//Add file path of this model as a property
		ModelProperty property = ModelFactory.eINSTANCE.createModelProperty();
		property.setPropertyType(spagobiModel.getPropertyType("structural.file"));
		property.setValue(modelPath);
		spagobiModel.getBusinessModels().get(0).getProperties().put(property.getPropertyType().getId(), property);
	}

	/**
	 * The framework calls this to create the contents of the wizard.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 */
	@Override
	public void addPages() {
		// Create a page, set the title, and the initial model file name.
		//
		newModelWizardFileCreationPage = new NewModelWizardFileCreationPage(selection);
		if(containerFullPath != null)newModelWizardFileCreationPage.setContainerFullPath(containerFullPath);
		selectionConnectionPage = new SelectionConnectionPage("Select Connection");
		physicalTableSelectionPage = new PhysicalTableSelectionPage("Select Physical Tables");
		businessTableSelectionPage = new BusinessTableSelectionPage("Select Business Tables");
		addPage(newModelWizardFileCreationPage);	
		addPage(selectionConnectionPage);
		addPage(physicalTableSelectionPage);
		addPage(businessTableSelectionPage);
		selectionConnectionPage.setPhysicalTableSelectionPageRef(physicalTableSelectionPage);
		physicalTableSelectionPage.setBusinessTableSelectionPageRef(businessTableSelectionPage);

	}

	public IPath getContainerFullPath() {
		return containerFullPath;
	}

	public void setContainerFullPath(IPath containerFullPath) {
		this.containerFullPath = containerFullPath;
	}



}