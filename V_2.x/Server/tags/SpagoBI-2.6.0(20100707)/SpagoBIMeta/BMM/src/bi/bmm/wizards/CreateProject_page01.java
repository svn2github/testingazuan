/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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

@Author Marco Cortella

**/
package bi.bmm.wizards;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FillLayout;

import bi.bmm.Activator;

public class CreateProject_page01 extends WizardPage {
	
	private Composite container;
	private Text text1;
	private Text text2;

	public CreateProject_page01() {
		super("Create a new BM");
		setTitle("Project Details");
		setDescription("This wizard drives you to crate a new BM project," +
				" please insert a name and choose a directory to save your BM.");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/createBM.png");
	    if (image!=null) setImageDescriptor(image);
	}

	@Override
	public void createControl(Composite parent) {
		
		container = new Composite(parent, SWT.NULL);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.numColumns = 1; 
		container.setLayout(gridLayout); 
		Group bmName = new Group(container, SWT.SHADOW_ETCHED_IN);
		bmName.setText("Name");
		bmName.setLayout(new FillLayout());
		Group bmDir	= new Group(container, SWT.SHADOW_ETCHED_IN);
		bmDir.setText("Directory");
		bmDir.setLayout(new FillLayout());
		
		//--------------------------------------------
		Composite innerUp = new Composite(bmName,SWT.NULL);
		GridLayout glInnerUp = new GridLayout(); 
		glInnerUp.numColumns = 2; 
		innerUp.setLayout(glInnerUp);
		
		Label label1 = new Label(innerUp, SWT.NULL);
		label1.setText("BM Name: ");
		
		text1 = new Text(innerUp, SWT.BORDER);
		text1.setText("");
		
		text1.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (!text1.getText().equals("") && !text2.getText().equals("")){
					setPageComplete(true);
				}
				else{
					setPageComplete(false);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//---------------------------------------------
		Composite innerDown = new Composite(bmDir,SWT.NULL);
		GridLayout glInnerDown = new GridLayout(); 
		glInnerDown.numColumns = 2; 
		innerDown.setLayout(glInnerUp);
		
		Button buttonDir = new Button(innerDown, SWT.NULL);
		buttonDir.setText("Choose a Directory");
		buttonDir.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(new Shell());
			    text2.setText(dialog.open());
				if (!text2.getText().equals("") && !text1.getText().equals("")){
					setPageComplete(true);
				}
				else{
					setPageComplete(false);
				}
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		text2 = new Text(innerDown, SWT.BORDER);
		text2.setText("");
		text2.setEditable(false);
		
		//---------------------------------------------
		
		//---------------------------------------------


		GridData gd = new GridData(GridData.FILL_BOTH);
		GridData gdH = new GridData(GridData.FILL_HORIZONTAL);
		
		bmName.setLayoutData(gd);
		bmDir.setLayoutData(gd);
		text1.setLayoutData(gdH);
		text2.setLayoutData(gdH);
		
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);

	}

	public String getBMName() {
		return text1.getText();
	}
	
	public String getBMDir(){
		return text2.getText();
	}
}
