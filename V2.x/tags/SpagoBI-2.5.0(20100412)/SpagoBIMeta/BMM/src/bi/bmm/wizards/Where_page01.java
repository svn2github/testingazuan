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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import bi.bmm.Activator;

public class Where_page01 extends WizardPage {
	private Boolean isCbc;
    private String arg;
	private Composite container;
	private Combo comboType;
	private Text clause;
	
	public Where_page01(String arg, Boolean isCbc) {
		super("Where clause");
		setTitle("Create a new where clause");
		setDescription("This wizard drives you to quickly create where clause " +
				"on your choosen field");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/createClause.png");
	    if (image!=null) setImageDescriptor(image);
	    this.isCbc = isCbc;
	    this.arg=arg;
	}

	@Override
	public void createControl(Composite parent) {
		
		 container = new Composite(parent, SWT.NULL);
		 GridLayout gl = new GridLayout();
		 gl.numColumns = 1;
		 gl.makeColumnsEqualWidth = true;
		 container.setLayout(gl);
		 //------------------------------------------------------------
		 Group detailGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
		 detailGroup.setText("Where Clause");
		 GridLayout glDet = new GridLayout();
		 GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		 glDet.numColumns = 3;
		 glDet.makeColumnsEqualWidth = true;
		 detailGroup.setLayout(glDet);
		 detailGroup.setLayoutData(gd);
		 		//------------------------------------------------------
		 	 	Label argLabel = new Label(detailGroup, SWT.NULL);
		 	 	argLabel.setText(arg);
		 		//------------------------------------------------------
		 	 	comboType = new Combo(detailGroup, SWT.NULL);
		 	 	if(!isCbc){
			 	 	comboType.add("=");
			 	 	comboType.add(">");
			 	 	comboType.add("<");
		 	 	}
		 	 	else{
		 	 		comboType.add("equals");
			 	 	comboType.add("not equals");
			 	 	comboType.add("contains");
			 	 	comboType.add("not contains");
		 	 	}
			 	 comboType.select(0);
		 	 	//------------------------------------------------------
		 	 	clause = new Text(detailGroup, SWT.BORDER);
		 	 	clause.setText("");
		 	 	clause.addKeyListener(new KeyListener() {
					
					@Override
					public void keyReleased(KeyEvent e) {
						if (!clause.getText().equals("") ){
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
		
		 	 	
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}


	public String getClause() {
		return clause.getText();
	}

	public String getType() {
		return comboType.getItem(comboType.getSelectionIndex());
	}
}
