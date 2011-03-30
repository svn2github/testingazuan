/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.presentation;


import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.editor.SpagoBIMetaModelEditorPlugin;
import it.eng.spagobi.meta.model.filter.PhysicalTableFilter;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalModelFactory;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is a simple wizard for creating a new model file.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SpagoBIModelWizard  extends Wizard implements INewWizard {
		
	private static Logger logger = LoggerFactory.getLogger(SpagoBIModelWizard.class);
	
	/**
	 * The supported extensions for created files.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<String> FILE_EXTENSIONS =
		Collections.unmodifiableList(Arrays.asList(SpagoBIMetaModelEditorPlugin.INSTANCE.getString("_UI_BusinessModelEditorFilenameExtensions").split("\\s*,\\s*")));

	/**
	 * A formatted list of supported file extensions, suitable for display.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String FORMATTED_FILE_EXTENSIONS =
		SpagoBIMetaModelEditorPlugin.INSTANCE.getString("_UI_BusinessModelEditorFilenameExtensions").replaceAll("\\s*,\\s*", ", ");

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
		setWindowTitle(SpagoBIMetaModelEditorPlugin.INSTANCE.getString("_UI_Wizard_label"));
		setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(SpagoBIMetaModelEditorPlugin.INSTANCE.getImage("full/wizban/NewBusinessModel")));
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
	 * Create a new model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 */
	protected EObject createInitialModel() {
		
		Model spagobiModel = ModelFactory.eINSTANCE.createModel();
		spagobiModel.setName("Fodmart DWH");
		
		PhysicalModel physicalModel = PhysicalModelFactory.eINSTANCE.createPhysicalModel();
		physicalModel.setName("Physical model (Fodmart DWH)");
		PhysicalTable physicalTable = PhysicalModelFactory.eINSTANCE.createPhysicalTable();
		physicalTable.setName("table1");
		physicalModel.getTables().add(physicalTable);
		
		BusinessModel businessModel = BusinessModelFactory.eINSTANCE.createBusinessModel();
		businessModel.setName("Business model (Fodmart DWH)");
		BusinessTable businessTable = BusinessModelFactory.eINSTANCE.createBusinessTable();
		businessTable.setName("Tabella 1");
		businessModel.setPhysicalModel(physicalModel);
		businessModel.getTables().add(businessTable);
		
		
		spagobiModel.getPhysicalModels().add(physicalModel);
		spagobiModel.getBusinessModels().add(businessModel);
		return spagobiModel;
	}

	/**
	 * Do the work after everything is specified.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 */
	@Override
	public boolean performFinish() {
		try {
			// Remember the file.
			//
			//final File modelFile = newModelWizardFileCreationPage.getModelFile();
			//... for retrieving the textfield's input-string:
			final File modelFile = newModelWizardFileCreationPage.getModelFile() ;		
			
			logger.debug(modelFile.getAbsolutePath().toString());
			
			final URI fileURI = URI.createFileURI(modelFile.getAbsolutePath().toString());
			
			
			final Model spagobiModel;
			
			if(selectionConnectionPage.getConnection() != null) {
				String catalogName = null;
				String schemaName = null;
				
				spagobiModel = ModelFactory.eINSTANCE.createModel();
				spagobiModel.setName(newModelWizardFileCreationPage.getModelName());
				
				String connectionName = selectionConnectionPage.getConnectionName();
				logger.debug("Connection name is [{}]",connectionName);
				String connectionUrl = selectionConnectionPage.getConnectionUrl();
				logger.debug("Connection URL is [{}]",connectionUrl);
				String connectionUsername = selectionConnectionPage.getConnectionUsername();
				logger.debug("Connection username is [{}]",connectionUsername);
				String connectionPassword = selectionConnectionPage.getConnectionPassword();
				logger.debug("Connection password is [{}]",connectionPassword);
				String connectionDatabaseName = selectionConnectionPage.getConnectionDatabaseName();
				logger.debug("Connection databaseName is [{}]",connectionDatabaseName);

				
				PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
		    	physicalModelInitializer.setRootModel(spagobiModel);
		    	
		    	if (selectionConnectionPage.getCatalogName() != null){
		    		catalogName = selectionConnectionPage.getCatalogName();
					logger.debug("Connection catalog name is [{}]",catalogName);
		    	}
		    	if (selectionConnectionPage.getSchemaName() != null){
		    		schemaName = selectionConnectionPage.getSchemaName();
					logger.debug("Connection schema name is [{}]",schemaName);
		    	}
		    	
		    	//Getting table to import inside physical table
		    	TableItem[] selectedPhysicalTableItem = physicalTableSelectionPage.getTablesToImport();
		    	List<String> selectedPhysicalTable = new ArrayList<String>();
		    	for (TableItem item: selectedPhysicalTableItem){
		    		selectedPhysicalTable.add(item.getText());
		    	}
		    	
		    	//Physical Model initialization
		    	if (selectedPhysicalTable.isEmpty()){
			    	spagobiModel.getPhysicalModels().add( physicalModelInitializer.initialize(
							newModelWizardFileCreationPage.getModelName(), 
							selectionConnectionPage.getConnection(),
							connectionName, 
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
							newModelWizardFileCreationPage.getModelName(), 
							selectionConnectionPage.getConnection(),
							connectionName,
							connectionUrl,
							connectionUsername,
							connectionPassword,
							connectionDatabaseName,
							catalogName, 
							schemaName,
							selectedPhysicalTable
					));	
		    	}
		    	//Getting physical table to import inside business table
		    	TableItem[] selectedBusinessTableItem = businessTableSelectionPage.getTablesToImport();
		    	List<PhysicalTable> selectedBusinessTable = new ArrayList<PhysicalTable>();
		    	PhysicalModel physicalModel = spagobiModel.getPhysicalModels().get(0);
		    	//check if there are no selected table to import in the Business Model
		    	if (selectedBusinessTableItem != null){
			    	for (TableItem item: selectedBusinessTableItem){
			    		PhysicalTable physicalTable = physicalModel.getTable(item.getText());
			    		selectedBusinessTable.add(physicalTable);
			    	}
		    	}
	
		    	//Business Model initialization
		    	if (selectedBusinessTable.isEmpty()){
		    		//create empty Business Model
					BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
					spagobiModel.getBusinessModels().add(businessModelInitializer.initializeEmptyBusinessModel(
							newModelWizardFileCreationPage.getModelName(), 
							spagobiModel.getPhysicalModels().get(0)
					));
					/*
					spagobiModel.getBusinessModels().add(businessModelInitializer.initialize(
							newModelWizardFileCreationPage.getModelName(), 
							spagobiModel.getPhysicalModels().get(0)
					));
					*/
		    	}
		    	else {
		    		//with table filtering
					BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
					spagobiModel.getBusinessModels().add(businessModelInitializer.initialize(
							newModelWizardFileCreationPage.getModelName(),
							new PhysicalTableFilter(selectedBusinessTable),
							spagobiModel.getPhysicalModels().get(0)
					));
		    	}

						
			} else {
				spagobiModel = (Model)createInitialModel();
			}
			
			
			
			// Do the work within an operation.
			//
			WorkspaceModifyOperation operation =
				new WorkspaceModifyOperation() {
					@Override
					protected void execute(IProgressMonitor progressMonitor) {
						try {
							// Create a resource set
							//
							ResourceSet resourceSet = new ResourceSetImpl();

							
							// Create a resource for this file.
							//
							Resource resource = resourceSet.createResource(fileURI);

							// Add the initial model object to the contents.
							//
							EObject rootObject = spagobiModel;
							if (rootObject != null) {
								resource.getContents().add(rootObject);
							}

							// Save the contents of the resource to the file system.
							//
							Map<Object, Object> options = new HashMap<Object, Object>();
							//options.put(XMLResource.OPTION_ENCODING, initialObjectCreationPage.getEncoding());
							resource.save(options);
						}
						catch (Exception exception) {
							SpagoBIMetaModelEditorPlugin.INSTANCE.log(exception);
						}
						finally {
							progressMonitor.done();
						}
					}
				};

			getContainer().run(false, false, operation);

			// Select the new file resource in the current view.
			//
			IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = workbenchWindow.getActivePage();
			final IWorkbenchPart activePart = page.getActivePart();
			if (activePart instanceof ISetSelectionTarget) {
				final ISelection targetSelection = new StructuredSelection(modelFile);
				getShell().getDisplay().asyncExec
					(new Runnable() {
						 public void run() {
							 ((ISetSelectionTarget)activePart).selectReveal(targetSelection);
						 }
					 });
			}

			// Open an editor on the new file.
			//			
			try {
				
				
				page.openEditor( 
						new SpagoBIModelInput(modelFile, spagobiModel) , SpagoBIModelEditor.PLUGIN_ID					     
				);
			}
			catch (PartInitException exception) {
				MessageDialog.openError(workbenchWindow.getShell(), SpagoBIMetaModelEditorPlugin.INSTANCE.getString("_UI_OpenEditorError_label"), exception.getMessage());
				return false;
			}

			return true;
		}
		catch (Exception exception) {
			SpagoBIMetaModelEditorPlugin.INSTANCE.log(exception);
			return false;
		}
	}


	NewModelWizardFileCreationPage newModelWizardFileCreationPage;
	SelectionConnectionPage selectionConnectionPage;
	PhysicalTableSelectionPage physicalTableSelectionPage;
	BusinessTableSelectionPage businessTableSelectionPage;
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
		newModelWizardFileCreationPage = new NewModelWizardFileCreationPage("New SpagoBI BM Project Page");
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
}