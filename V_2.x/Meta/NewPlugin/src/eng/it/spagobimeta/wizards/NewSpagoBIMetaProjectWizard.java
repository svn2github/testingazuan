/*
 * This Wizard create a new SpagoBI Meta project
 */

package eng.it.spagobimeta.wizards;



import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import eng.it.spagobimeta.Activator;
import eng.it.spagobimeta.builder.SpagoBIStudioNature;

public class NewSpagoBIMetaProjectWizard extends Wizard implements INewWizard, IExecutableExtension {

	private WizardNewProjectCreationPage one;
	private boolean createdProject;
	private IConfigurationElement configElement = null;
	
	public NewSpagoBIMetaProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Create a new Business Model");
		createdProject = false;
	}

	@Override
	public void addPages() {
		one = new WizardNewProjectCreationPage("New SpagoBI BM Project Page");
		one.setDescription("This wizard drives you to create a new Business Model project," +
		" please insert a name and choose a directory to save your BM.");
		ImageDescriptor image = Activator.getImageDescriptor("/wizards/createBM.png");
	    if (image!=null)
	    	one.setImageDescriptor(image);
		addPage(one);
	}
	
	@Override
	public boolean performFinish() {
		try {
			WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
				protected void execute(IProgressMonitor monitor) {
					createProject(monitor != null ? monitor
							: new NullProgressMonitor());
				}
			};
			getContainer().run(false, true, op);
			BasicNewProjectResourceWizard.updatePerspective(configElement);
			
	
		} catch (InvocationTargetException x) {
			reportError(x);
			return false;
		} catch (InterruptedException x) {
			reportError(x);
			return false;
		}
		return true; 
	}

	private void reportError(Exception x) {
		ErrorDialog.openError(getShell(), "Error", "Error in Creating New Project", makeStatus(x));
	}	

	protected void createProject(IProgressMonitor monitor) {
		monitor.beginTask("Creating Project",50);
	    try {
	         IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	         monitor.subTask("Creating Project Directories ");
	         IProject project = root.getProject(one.getProjectName());
	         IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(project.getName());
	         if(!Platform.getLocation().equals(one.getLocationPath()))
	            description.setLocation(one.getLocationPath());
	         // set the nature 
	         String[] natures = new String[1];
	         natures[0] = SpagoBIStudioNature.NATURE_ID;
	         description.setNatureIds(natures);
	         // create project 
	         project.create(description,monitor);
	         	//TODO: DO SOMETHING....
	         
	         //Create directory with project name
	         String path = one.getLocationPath().toString()+"/"+one.getProjectName();
	         File dir = new File(path);
			 if (!dir.exists())
			 {
				dir.mkdir();
			 }
	         monitor.worked(10);
	         project.open(monitor);
	         monitor.worked(10);
	      } catch(CoreException x) {
	         reportError(x);
	      } finally {
	         monitor.done();
	      }
	}	
	
	public boolean isCreatedProject() {
		return createdProject;
	}	
	
	public static IStatus makeStatus(Exception x){
	    return new Status(IStatus.ERROR, "", IStatus.ERROR, x.getMessage(), null);
	}	
	
	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		setNeedsProgressMonitor(true);

	}

	@Override
	public void setInitializationData(IConfigurationElement confEl, String arg1,
			Object arg2) throws CoreException {
		configElement = confEl;
		
	}

}
