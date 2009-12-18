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
package bi.bmm;

import java.util.ArrayList;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import bi.bmm.commands.createProjectHandler;
import bi.bmm.figures.BMScheme;
import bi.bmm.util.ClassInfo;
import bi.bmm.util.ComplexClassInfo;
import bi.bmm.util.DBConnection;
import bi.bmm.wizards.BindingCBCWizard;
import bi.bmm.wizards.DeleteBCWizard;
import bi.bmm.wizards.NewBCWizard;
import bi.bmm.wizards.NewComplexBCWizard;
import bi.bmm.wizards.NewRelWizard;

public class BMUniverseView extends ViewPart {


	public static final String ID = "bi.bmm.views.bme.bmuniverse";
	public Composite composite;
	public Group bmGroup;
	public ArrayList<ClassInfo> bcList;
	public BMScheme scheme;
	public ArrayList<String[]> relList;
	public ArrayList<ComplexClassInfo> cbcList;
	
	
	public BMUniverseView() {
		bcList = new ArrayList<ClassInfo>();
	   	relList = new ArrayList<String[]>();
	   	cbcList = new ArrayList<ComplexClassInfo>();
	}

	@Override
	
	public void createPartControl(Composite parent) {
	
		
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL |   
				  SWT.V_SCROLL | SWT.BORDER);
		
		composite = new Composite(sc, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setSize(parent.getSize());
		sc.setContent(composite);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(composite.computeSize(1000, 1000));
		createPartToolBar();
		createPartEditor();
	}

	private void createPartToolBar() {
		 Composite c = new Composite(composite, SWT.BORDER);
		 c.setLayout(new GridLayout());
		 c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		 
		 ToolBar tb = new ToolBar(c, SWT.HORIZONTAL);
			
		 /*
		  *   ADD BC   -> index at [0]
		  */
		 ToolItem itemNewClass = new ToolItem(tb, SWT.PUSH , 0);
		 itemNewClass.setToolTipText("Create a new BC.");
		 itemNewClass.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	
		    	  //controllo che ci sia almeno una connessione
		    	  	//recupero la view data pool
		  			DataPools dp = (DataPools) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		  			.getActivePage().findView("bi.bmm.views.bme.datapools");
		
		  			if (dp.activeConn.activeConnections.size() > 0)
		  			{
		  			  String connName = dp.activeConn.getAllConnection().get(0).split(":")[0];
		  			  if (MessageDialog.openConfirm(new Shell(), "Connection default" , 
		  					  "For this operation will be used the dafault connection: "+connName+".\nDo you want continue?")){
			  			//lancio il wizard di creazione
				    	  NewBCWizard wizard = new NewBCWizard(dp.activeConn.getConnection(connName), null);
				    	  WizardDialog dialog = new WizardDialog(new Shell(), wizard);
				    		dialog.open();
				    		if(dialog.getReturnCode() == WizardDialog.OK){
				    			MessageDialog.openInformation(new Shell(), "Create BC","BC "+
										" was created.");
				    		}
		  			  }
		  			}
		  			else{
		  				MessageDialog.openWarning(new Shell(), "Create BC","To create a BC "+
						"  must exist at least one active connection in Data Pool.");
		  			}
		      }
		 });
		 
		 Image image = Activator.getImageDescriptor("icons/ToolBarBMU/addBC.png").createImage();
		 if (image!=null)  itemNewClass.setImage(image);
		 
		 /*
		  *   REMOVE BC -> index at [1]
		  */
		 ToolItem itemRemClass = new ToolItem(tb, SWT.PUSH , 1);
		 itemRemClass.setToolTipText("Remove a BC.");
		 itemRemClass.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	if(bcList.size()!=0){  
		    	  //lancio il wizard di eliminazione
		    	  DeleteBCWizard wizard = new DeleteBCWizard(bcList, scheme);
		    	  WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		    		dialog.open();
		    		if(dialog.getReturnCode() == WizardDialog.OK){
		    			MessageDialog.openInformation(new Shell(), "Delete BC","BC "+
								" was deleted.");
		    		}
		    	}
		      }
		 });
		 
		 image = Activator.getImageDescriptor("icons/ToolBarBMU/removeBC.png").createImage();
		 if (image!=null)  itemRemClass.setImage(image);
		 
		 /*
		  * SEPARATOR
		  */
		 ToolItem itemS1 = new ToolItem(tb, SWT.SEPARATOR , 2);
		 itemS1.setWidth(40);
		 /*
		  *   ADD REL -> index at [3]
		  */
		 ToolItem itemNewRel = new ToolItem(tb, SWT.PUSH , 3);
		 itemNewRel.setToolTipText("Create a new Relationship.");
		 itemNewRel.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	if(bcList.size()>1){  
		    	  //lancio il wizard di eliminazione
		    	 NewRelWizard wizard = new NewRelWizard(bcList, scheme);
		    	  WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		    		dialog.open();
		    		if(dialog.getReturnCode() == WizardDialog.OK){
		    			MessageDialog.openInformation(new Shell(), "Done!",""+
								" New Relationship was created.");
		    		}
		    	}
		    	else{
		    		MessageDialog.openWarning(new Shell(), "Attention",""+
					"Almost two Business Classes are needed to create a new Relationship");
		    	}
		      }
		 });
		 image = Activator.getImageDescriptor("icons/ToolBarBMU/createREL.png").createImage();
		 if (image!=null)  itemNewRel.setImage(image);
		 /*
		  *   REMOVE REL -> index at [4]
		  */
		 ToolItem itemRemRel = new ToolItem(tb, SWT.PUSH , 4);
		 itemRemRel.setToolTipText("Remove a Relationship.");
		 
		 image = Activator.getImageDescriptor("icons/ToolBarBMU/removeREL.png").createImage();
		 if (image!=null)  itemRemRel.setImage(image);
		
		 /*
		  * SEPARATOR
		  */
		 ToolItem itemS2 = new ToolItem(tb, SWT.SEPARATOR , 5);
		 itemS2.setWidth(40);
		 /*
		  *   PERSIST BM -> index at [6]
		  */
		 ToolItem itemNewBM = new ToolItem(tb, SWT.PUSH , 6);
		 itemNewBM.setToolTipText("Persist the BM in Java files.");
		 itemNewBM.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		      createProjectHandler command = new createProjectHandler();
		      try {
				command.execute(new ExecutionEvent());
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      }
		 });
		 
		 	 
		 image = Activator.getImageDescriptor("icons/ToolBarBMU/createBM.png").createImage();
		 if (image!=null)  itemNewBM.setImage(image);
		 /*
		  * SEPARATOR
		  */
		 ToolItem itemS3 = new ToolItem(tb, SWT.SEPARATOR , 7);
		 itemS3.setWidth(40);
		 
		 /*
		  *   ADD COMPLEX BC   -> index at [8]
		  */
		 ToolItem itemNewComplexClass = new ToolItem(tb, SWT.PUSH , 8);
		 itemNewComplexClass.setToolTipText("Create a new Complex BC. (you need to have BCs existing)");
		 itemNewComplexClass.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	if (!bcList.isEmpty()){
		    	 		//lancio il wizard di creazione
				    	  NewComplexBCWizard wizard = new NewComplexBCWizard(null);
				    	  WizardDialog dialog = new WizardDialog(new Shell(), wizard);
				    		dialog.open();
				    		if(dialog.getReturnCode() == WizardDialog.OK){
				    			MessageDialog.openInformation(new Shell(), "Create complex BC","BC "+
										" was created.");
				    		}
		  			}
		  			else{
		  				MessageDialog.openWarning(new Shell(), "Create BC","To create a BC "+
						"  must exist at least one BC in your BM");
		  			}
		      }
		 });
		 
		 image = Activator.getImageDescriptor("icons/ToolBarBMU/addComplexBC.png").createImage();
		 if (image!=null)  itemNewComplexClass.setImage(image);
		 
		 /*
		  *   REMOVE COMPLEX BC -> index at [9]
		  */
		 ToolItem itemRemComplexClass = new ToolItem(tb, SWT.PUSH , 9);
		 itemRemComplexClass.setToolTipText("Remove a Complex BC.");
		 itemRemComplexClass.addSelectionListener(new SelectionAdapter() {
		     
			public void widgetSelected(SelectionEvent event) {
		    	if(!cbcList.isEmpty()){  
		    		//TODO: ELIMINARE LA COMPLEX BC
		    		/*
		    	  //lancio il wizard di eliminazione
		    	  DeleteComplexBCWizard wizard = new DeleteComplexBCWizard(bcComplexList, scheme);
		    	  WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		    		dialog.open();
		    		if(dialog.getReturnCode() == WizardDialog.OK){
		    			MessageDialog.openInformation(new Shell(), "Delete Complex BC","Complex BC "+
								" was deleted.");
		    		}
		    	*/
		    	}
		    	
		      }
		 });
		 
		 image = Activator.getImageDescriptor("icons/ToolBarBMU/removeComplexBC.png").createImage();
		 if (image!=null)  itemRemComplexClass.setImage(image);
		 
		
		 tb.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
	}

	private void createPartEditor() {
		 
		 bmGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		 bmGroup.setText("BM");
		 GridLayout glDet = new GridLayout();
		 GridData gd = new GridData(GridData.FILL_BOTH);
		 glDet.numColumns = 1;
		 bmGroup.setLayout(glDet);
		 bmGroup.setLayoutData(gd);
		
		
		 
	    // Create the drop target
	    DropTarget dt = new DropTarget(bmGroup, DND.DROP_MOVE);
	    dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
	    dt.addDropListener(new DropTargetAdapter() {
	      public void drop(DropTargetEvent event) {
	    	// Set the buttons text to be the text being dropped
	    	createBC((String) event.data);
	      }
	    });
	    
	    //creo lo schema
		scheme = new BMScheme(bmGroup);
	    
	}

	private void createBC(String data) {
		//ottengo i dati del D'N'D
		/*
		 * conn.getDriver() 	[1]
		 * conn.getServer() 	[2]
		 * conn.getDBName() 	[3]
		 * conn.getUser()   	[4]
		 * conn.getPassword() 	[5]
		 * conn.getPort() 		[6]
		 * conn.getName()		[7]
		 * 
		 * */
		String[] d = data.split("#");
		if (d.length != 8){
			if(cbcList.isEmpty()){
				return;
			}
			if(data.split(" - ")[0].equals(data.split(" - ")[1])){
				return;
			}
			
			BindingCBCWizard wizard = new BindingCBCWizard(data);
			WizardDialog dialog = new WizardDialog(new Shell(), wizard);
    		dialog.open();
    		if(dialog.getReturnCode() == WizardDialog.OK){
    			MessageDialog.openInformation(new Shell(), "Done!",""+
				"The WS Method was binding to Complex BC successfully.");
    		}
			return;
		}
		String name = d[7];
		String driver = d[1];
		String server = d[2];
		String database= d[3];
		String user= d[4];
		String password= d[5];
		String port= d[6];
		String objName = d[0];
		//apro la connessione
		DBConnection conn = new DBConnection(name, driver, server, database, user, password, port);
		if(conn.Start()){
			if (conn.hasTable(objName)){
				//se è una table apro il wizard di trasformazione
				NewBCWizard wizard = new NewBCWizard(conn,objName);
	        	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
	    		dialog.open();
	    		if(dialog.getReturnCode() == WizardDialog.OK){
	    			//recupero le informazioni sulla classe creata
	    		}
			}
		conn.Stop();
		}
		
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void drawBC(ClassInfo ci) {
	
	scheme.addClassFigure(ci);
			
	}

	public StringBuilder toFile() {
		StringBuilder sb = new StringBuilder(10000);
		sb.append("<bcList>"+"\n");
		for (int i = 0; i<this.bcList.size(); i++){
			ClassInfo ci = bcList.get(i);
			sb.append("<bc>\n");
			sb.append("<bcPath>"
					+ci.getClassPathInfo()
					+"</bcPath>"+"\n");
			sb.append("</bc>\n");
			
		}
		sb.append("</bcList>"+"\n");
		
		sb.append("<relList>"+"\n");
		for (int i = 0; i<this.relList.size(); i++){
			String[] rel = relList.get(i);
			/**
			 * rel[0] -> className 1
			 * rel[1] -> className 2
			 * rel[2] -> type
			 */

			sb.append("<rel>\n"
					+"<c1>"+rel[0]+"</c1>\n"
					+"<c2>"+rel[1]+"</c2>\n"
					+"<type>"+rel[2]+"</type>\n"
					+"</rel>"+"\n");
		}
		sb.append("</relList>"+"\n");
		
		sb.append("<cbcList>"+"\n");
		for (int i = 0; i<this.cbcList.size(); i++){
			ComplexClassInfo ci = cbcList.get(i);
			sb.append("<cbc>\n");
			sb.append("<cbcPath>"
					+ci.getClassPathInfo()
					+"</cbcPath>"+"\n");
			sb.append("</cbc>\n");
			
		}
		sb.append("</cbcList>"+"\n");
		
		return sb;
	}

	public void openFile(String result) {
		String bcString = result.split("<bcList>\n")[1].split("</bcList>\n")[0];
		String[] bcBuffer = bcString.split("<bc>\n");
		String relString = result.split("<relList>\n")[1].split("</relList>\n")[0];
		String[] relBuffer = relString.split("<rel>\n");
		
		
		String cbcString = null;
		String[] cbcBuffer = null;
		if(result.contains("<cbcList>\n<cbc>"))
		{
			cbcString = result.split("<cbcList>\n")[1].split("</cbcList>\n")[0];
			cbcBuffer = cbcString.split("<cbc>\n");
		}
		
		
		bcList.clear();
		relList.clear();
		cbcList.clear();
		scheme.clear();
		
		for (int k=1;k<bcBuffer.length;k++){
			String bcPath = bcBuffer[k].split("<bcPath>")[1].split("</bcPath>")[0];
			ClassInfo ci = new ClassInfo(bcPath, null);
			ci.buildClassInfo();
			bcList.add(ci);
			this.drawBC(ci);
		}
		
		for (int k=1;k<relBuffer.length;k++){
			ClassInfo ci1 = null,ci2 = null;
			
			String c1 = relBuffer[k].split("<c1>")[1].split("</c1>")[0];
			for (int i = 0; i < bcList.size(); i++){
				if (bcList.get(i).getClassPathInfo().equals(c1)){
					ci1 = bcList.get(i); 
					break;
				}
			}
			
			String c2 = relBuffer[k].split("<c2>")[1].split("</c2>")[0];
			for (int i = 0; i < bcList.size(); i++){
				if (bcList.get(i).getClassPathInfo().equals(c2)){
					ci2 = bcList.get(i); 
					break;
				}
			}
			
			String type = relBuffer[k].split("<type>")[1].split("</type>")[0];
			
			relList.add(new String[]{c1,c2,type});
			
			if (ci1 != null && ci2 != null){
				if (type.contains("MANY_TO_MANY"))
				{
					scheme.addLinkFigure(ci1.getFigure(), ci2.getFigure(),BMScheme.MANY_TO_MANY);
				}
				if (type.contains("ONE_TO_MANY"))
				{					
					scheme.addLinkFigure(ci1.getFigure(), ci2.getFigure(),BMScheme.ONE_TO_MANY);
				}
				if (type.contains("MANY_TO_ONE"))
				{	
					scheme.addLinkFigure(ci1.getFigure(), ci2.getFigure(),BMScheme.MANY_TO_ONE);
				}
				if (type.contains("ONE_TO_ONE"))
				{
					scheme.addLinkFigure(ci1.getFigure(), ci2.getFigure(),BMScheme.ONE_TO_ONE);
				}
			}
			//TODO:Controlli su ci1 ci2 e sul type
		}
		if(result.contains("<cbcList>\n<cbc>")){
			for (int k=1;k<cbcBuffer.length;k++){
				String cbcPath = cbcBuffer[k].split("<cbcPath>")[1].split("</cbcPath>")[0];
				ComplexClassInfo cci = new ComplexClassInfo(cbcPath, null, null, null);
				cci.buildClass(cbcPath);
				cbcList.add(cci);
				this.drawCBC(cci);
			}
		}
		
		
	}

	public void drawCBC(ComplexClassInfo cci) {

		scheme.addComplexClassFigure(cci);
		
	}
}
