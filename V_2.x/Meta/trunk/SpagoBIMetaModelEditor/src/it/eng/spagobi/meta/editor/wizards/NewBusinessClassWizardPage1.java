package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.editor.Activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewBusinessClassWizardPage1 extends WizardPage {

	private Label lblName;
	private Text bmName;
	private Label lblPath;
	private Text dirPath;
	private Button btnSelDir;

	protected NewBusinessClassWizardPage1(String pageName) {
		super(pageName);
		setDescription("This wizard drives you to create a new SpagoBI Meta Business Model," +
		" please insert a name and choose a directory to save your BM.");
		ImageDescriptor image = Activator.getImageDescriptor("/wizards/createBM.png");
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
        
		//Directory Group
		Group dirGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		dirGroup.setText("Directory");
		GridLayout glDir = new GridLayout();
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
		glDir.numColumns = 3;
		glDir.makeColumnsEqualWidth = false;
		dirGroup.setLayout(glDir);
		dirGroup.setLayoutData(gd2);
		
		//Adding DirectoryGroup elements
        lblPath = new Label(dirGroup,SWT.NONE);
        lblPath.setText("Path");
        setDirPath(new Text(dirGroup,SWT.BORDER));
        dirPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btnSelDir = new Button(dirGroup, SWT.PUSH);
        btnSelDir.setText("Browse");
        btnSelDir.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
	              DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.NULL);
	              // Set the initial filter path according
	              // to anything they've selected or typed in
	              dialog.setFilterPath(dirPath.getText());

	              // Change the title bar text
	              dialog.setText("Business Model Directory selection");

	              // Customizable message displayed in the dialog
	              dialog.setMessage("Select a directory");
	              String path = dialog.open();
	              if (path != null) {
	            	  dirPath.setText(path);
	              }
            }
        });    
        //Important: Setting page control
 		setControl(composite);
	}

	private void setBmName(Text bmName) {
		this.bmName = bmName;
	}

	public String getBmName() {
		return bmName.getText();
	}

	private void setDirPath(Text dirPath) {
		this.dirPath = dirPath;
	}

	public String getDirPath() {
		return dirPath.getText();
	}

}
