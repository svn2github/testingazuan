/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.presentation;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.StringTokenizer;

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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import org.eclipse.swt.SWT;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import org.eclipse.ui.actions.WorkspaceModifyOperation;

import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.part.MultiEditorInput;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.ModelPackage;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.presentation.BusinessModelInput;
import it.eng.spagobi.meta.model.business.presentation.PhysicalModelInput;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalModelFactory;
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
		BusinessModel businessModel = BusinessModelFactory.eINSTANCE.createBusinessModel();
		BusinessTable businessTable = BusinessModelFactory.eINSTANCE.createBusinessTable();
		businessTable.setName("Tabella 1");
		businessModel.setName("Business model (Fodmart DWH)");
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
			final File modelFile = newModelWizardFileCreationPage.getModelFile();
			final URI fileURI = URI.createFileURI(modelFile.getAbsolutePath().toString());
			final Model spagobiModel = (Model)createInitialModel();
			
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
				
				PhysicalModelInput pInput = new PhysicalModelInput(fileURI);
				BusinessModelInput bInput = new BusinessModelInput(fileURI);
				BusinessModel targetBusinessModel =  spagobiModel.getBusinessModels().get(0);
				System.err.println(targetBusinessModel.getName());
				System.err.println(EcoreUtil.getURI(targetBusinessModel));
				bInput.setObjectURI(EcoreUtil.getURI(targetBusinessModel));
				
				page.openEditor( 
					new MultiEditorInput(
						new String[]{
							"it.eng.spagobi.meta.model.physical.presentation.PhysicalModelEditorID", 
							"it.eng.spagobi.meta.model.business.presentation.BusinessModelEditorID"},
						new IEditorInput[]{
								pInput, 
								bInput}
					) , "it.eng.spagobi.meta.model.presentation.SpagoBIModelEditorID"
					     
				);
				/*
				page.openEditor
					(new FileEditorInput(modelFile),
					 workbench.getEditorRegistry().getDefaultEditor(modelFile.getAbsolutePath()).toString()).getId());					 	 
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
		newModelWizardFileCreationPage.setTitle("Create new SpagoBI Metamodel");
		newModelWizardFileCreationPage.setDescription("Create new SpagoBI Metamodel");
		addPage(newModelWizardFileCreationPage);	
	}

	public class NewModelWizardFileCreationPage extends WizardPage {

		private Label lblName;
		private Text bmName;


		protected NewModelWizardFileCreationPage(String pageName) {
			super(pageName);
			setDescription("This wizard drives you to create a new SpagoBI Meta Business Model," +
			" please insert a name for your BM.");
			ImageDescriptor image = ExtendedImageRegistry.INSTANCE.getImageDescriptor(TestEditorPlugin.INSTANCE.getImage("full/wizban/NewBusinessModel"));;
		    if (image!=null)
		    	setImageDescriptor(image);
		}

		@Override
		public void createControl(Composite parent) {
			//Main composite
			Composite composite = new Composite(parent, SWT.NULL);
			GridLayout gl = new GridLayout();
			gl.numColumns = 1;
			gl.makeColumnsEqualWidth = true;
			composite.setLayout(gl);
			
			//Name Group
			Group nameGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
			nameGroup.setText("Name");
			GridLayout glName = new GridLayout();
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			glName.numColumns = 2;
			glName.makeColumnsEqualWidth = false;
			nameGroup.setLayout(glName);
			nameGroup.setLayoutData(gd);
			
			//Adding NameGroup elements
	        lblName = new Label(nameGroup,SWT.NONE);
	        lblName.setText("Business Model Name:");
	        setBmName(new Text(nameGroup,SWT.BORDER));
	        bmName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        
			
	        //Important: Setting page control
	 		setControl(composite);
		}

		private void setBmName(Text bmName) {
			this.bmName = bmName;
		}

		public String getModelName() {
			return bmName.getText();
		}
		
		public String getModelFileName() {
			return getModelName() + "businessmodel";
		}
		
		public File getModelFilePath() {
			return new  File("D:/Documenti/Progetti/metadati/libri/");
		}
		
		
		
		public File getModelFile() {
			//return ResourcesPlugin.getWorkspace().getRoot().getFile(getModelFilePath().append(getModelFileName()));
			return new File(getModelFilePath(), getModelFileName());
		}
	}

}