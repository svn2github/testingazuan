/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.presentation;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.StringTokenizer;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.emf.common.CommonPlugin;

import org.eclipse.emf.common.util.URI;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.xmi.XMLResource;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import org.eclipse.swt.SWT;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import org.eclipse.ui.actions.WorkspaceModifyOperation;

import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.part.MultiEditorInput;

import it.eng.spagobi.meta.commons.IModelObjectFilter;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.ModelPackage;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.presentation.BusinessModelEditor;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalModelFactory;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.physical.presentation.PhysicalModelEditor;
import it.eng.spagobi.meta.model.provider.SpagoBIMetalModelEditPlugin;


import it.eng.spagobi.meta.model.test.TestEditorPlugin;

import org.eclipse.core.runtime.Path;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;


/**
 * This is a simple wizard for creating a new model file.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SpagoBIModelWizard  extends Wizard implements INewWizard {
		
	/**
	 * The supported extensions for created files.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<String> FILE_EXTENSIONS =
		Collections.unmodifiableList(Arrays.asList(TestEditorPlugin.INSTANCE.getString("_UI_BusinessModelEditorFilenameExtensions").split("\\s*,\\s*")));

	/**
	 * A formatted list of supported file extensions, suitable for display.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String FORMATTED_FILE_EXTENSIONS =
		TestEditorPlugin.INSTANCE.getString("_UI_BusinessModelEditorFilenameExtensions").replaceAll("\\s*,\\s*", ", ");

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
		setWindowTitle(TestEditorPlugin.INSTANCE.getString("_UI_Wizard_label"));
		setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(TestEditorPlugin.INSTANCE.getImage("full/wizban/NewBusinessModel")));
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
	 * @generated
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
	 * @generated
	 */
	@Override
	public boolean performFinish() {
		try {
			// Remember the file.
			//
			//final File modelFile = newModelWizardFileCreationPage.getModelFile();
			//... for retrieving the textfield's input-string:
			final File modelFile = newModelWizardFileCreationPage.getModelFile() ;		
			
			System.err.println(modelFile.getAbsolutePath().toString());
			
			final URI fileURI = URI.createFileURI(modelFile.getAbsolutePath().toString());
			
			
			final Model spagobiModel;
			
			if(selectionConnectionPage.getConnection() != null) {
				String catalogName = null;
				String schemaName = null;
				
				spagobiModel = ModelFactory.eINSTANCE.createModel();
				spagobiModel.setName(newModelWizardFileCreationPage.getModelName());
				
				
				PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
		    	physicalModelInitializer.setRootModel(spagobiModel);
		    	
		    	if (selectionConnectionPage.getCatalogName() != null){
		    		catalogName = selectionConnectionPage.getCatalogName();
		    	}
		    	if (selectionConnectionPage.getSchemaName() != null){
		    		schemaName = selectionConnectionPage.getSchemaName();
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
							catalogName, 
							schemaName
					));		    		
		    	}
		    	else {
		    		//with table filtering
			    	spagobiModel.getPhysicalModels().add( physicalModelInitializer.initialize(
							newModelWizardFileCreationPage.getModelName(), 
							selectionConnectionPage.getConnection(), 
							catalogName, 
							schemaName,
							selectedPhysicalTable
					));	
		    	}
		    	//Getting physical table to import inside business table
		    	TableItem[] selectedBusinessTableItem = businessTableSelectionPage.getTablesToImport();
		    	List<PhysicalTable> selectedBusinessTable = new ArrayList<PhysicalTable>();
		    	PhysicalModel physicalModel = spagobiModel.getPhysicalModels().get(0);
		    	for (TableItem item: selectedBusinessTableItem){
		    		PhysicalTable physicalTable = physicalModel.getTable(item.getText());
		    		selectedBusinessTable.add(physicalTable);
		    	}
		    	
		    	//Business Model initialization
		    	if (selectedBusinessTable.isEmpty()){
					BusinessModelInitializer businessModelInitializer = new BusinessModelInitializer();
					spagobiModel.getBusinessModels().add(businessModelInitializer.initialize(
							newModelWizardFileCreationPage.getModelName(), 
							spagobiModel.getPhysicalModels().get(0)
					));
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
							TestEditorPlugin.INSTANCE.log(exception);
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
				
				System.out.println("Open editor");
				/*
				PhysicalModel targetPhysicalModel =  spagobiModel.getPhysicalModels().get(0);
				PhysicalModelInput physicalModelInput = new PhysicalModelInput(modelFile, targetPhysicalModel);
				
				BusinessModel targetBusinessModel =  spagobiModel.getBusinessModels().get(0);
				BusinessModelInput businessModelInput = new BusinessModelInput(modelFile, targetBusinessModel);
				
				page.openEditor( 
					new MultiEditorInput(
						new String[]{PhysicalModelEditor.PLUGIN_ID, BusinessModelEditor.PLUGIN_ID},
						new IEditorInput[]{physicalModelInput, businessModelInput}
					) , SpagoBIModelEditor.PLUGIN_ID					     
				);
				*/
			}
			catch (PartInitException exception) {
				MessageDialog.openError(workbenchWindow.getShell(), TestEditorPlugin.INSTANCE.getString("_UI_OpenEditorError_label"), exception.getMessage());
				return false;
			}

			return true;
		}
		catch (Exception exception) {
			TestEditorPlugin.INSTANCE.log(exception);
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
	 * @generated
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

/*		
	public class NewModelWizardFileCreationPage extends WizardPage {

		Text modelNameFieldInput;
		StringButtonFieldEditor modelFileNameFieldInput;
		org.eclipse.swt.widgets.List tableList;
		DSEBridge dseBridge;
		
		protected NewModelWizardFileCreationPage(String pageName) {
			super(pageName);
			setDescription("This wizard drives you to create a new SpagoBI Meta Business Model," +
			" please insert a name for your BM.");
			ImageDescriptor image = ExtendedImageRegistry.INSTANCE.getImageDescriptor(TestEditorPlugin.INSTANCE.getImage("full/wizban/NewBusinessModel"));;
		    if (image!=null) {
		    	setImageDescriptor(image);
		    }
		    dseBridge = new DSEBridge();
		}

		@Override
		public void createControl(Composite parent) {
			Composite composite = new Composite(parent, SWT.NULL);
			composite.setLayout(new GridLayout(3, false));
			
			
			new Label(composite, SWT.NONE).setText("Model name:");
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.horizontalSpan = 2;
			modelNameFieldInput = new Text(composite, SWT.BORDER);
			modelNameFieldInput.setLayoutData(gridData);
			modelNameFieldInput.setText("Test Model");
			
			modelFileNameFieldInput = new FileFieldEditor("modelfile", "Model file:", composite);
			modelFileNameFieldInput.setStringValue("D:/Documenti/Progetti/metadati/libri/TestModel.sbimodel");
			
			tableList = new org.eclipse.swt.widgets.List(composite, SWT.BORDER|SWT.SINGLE|SWT.V_SCROLL|SWT.H_SCROLL);
	 		GridData gdList = new GridData(GridData.FILL_BOTH);
	 		gdList.heightHint = 60;
	 		gdList.horizontalAlignment = GridData.FILL;
	 		gdList.horizontalSpan = 2;
	 		gdList.verticalSpan = 2;
	 		tableList.setLayoutData(gdList);
	 		
	 		IConnectionProfile[] cp = dseBridge.getConnectionProfiles();
	 		for (int i = 0; i < cp.length; i++){
				tableList.add( cp[i].getName() );		
			}
	 		
	 		new Button(composite, SWT.PUSH).setText("Add");
	 		new Button(composite, SWT.PUSH).setText("Edit");

	 		
	 		
			//Important: Setting page control
	 		setControl(composite);
		}

		public String getConnectionName() {
			return tableList.getSelection()[0];
		}
		
		public Connection getConnection() {
			return dseBridge.connect( getConnectionName() );
		}
		
		public String getModelFileName() {
			String fileName = modelFileNameFieldInput.getStringValue();
			if(!fileName.endsWith(".sbimodel")) {
				fileName += ".sbimodel";
			}
			return fileName;
		}
		
		public File getModelFile() {
			return new File( getModelFileName() );
		}

		
		
		public String getModelName() {
			return modelNameFieldInput.getText();
		}
		
	}
*/
		/*
		 * Inner class that implements IModelObjectFilter
		 */
		private class PhysicalTableFilter implements IModelObjectFilter{

			List<PhysicalTable> tablesTrue;
			public PhysicalTableFilter(List<PhysicalTable> tablesToMantain){
				tablesTrue = tablesToMantain;
			}
			@Override
			public boolean filter(ModelObject o) {
				if (tablesTrue.contains((PhysicalTable)o))
					return false;
				else
					return true;
			}		
		}
}