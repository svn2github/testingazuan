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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


import bi.bmm.util.QueryUtil;
import bi.bmm.wizards.WhereWizard;

public class CBCQEditorView extends ViewPart {


	public static final String ID = "bi.bmm.views.bmq.cbcqeditor";
	public Composite composite;
	public Group bmGroup;
	
	private Table selectTable;
	private Table whereTable;
	protected ArrayList<String> selectList;
	protected ArrayList<String> whereList;
	protected ArrayList<String> fromList;
	
	//TODO:una volta aggiunte le relazioni automatizzare le query nei join
	
	public CBCQEditorView() {
		selectList = new ArrayList<String>();
	   	whereList = new ArrayList<String>();
	   	fromList = new ArrayList<String>();
	   
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
		 Image image;

		 /*
		  * SEPARATOR
		  */
		 ToolItem itemS1 = new ToolItem(tb, SWT.SEPARATOR , 0);
		 itemS1.setWidth(40);
		 /*
		  *   Execute JPQL-> index at [1]
		  */
		 ToolItem itemExecute = new ToolItem(tb, SWT.PUSH , 1);
		 itemExecute.setToolTipText("Execute the CBC query.");
		 itemExecute.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
		      //TODO: GEASTIONE DELL'ESEGUZIONE DELLA QUERY
		    	
			    	  //creo un service con la jpql
			    	  QueryUtil qu = new QueryUtil(null,selectList,fromList,whereList);
			    	  
			    	  BMPoolsView bmPool = (BMPoolsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().findView("bi.bmm.views.bmq.bmpools");
			    	  
			    	  qu.createCBCService(bmPool.bcList.get(0).getRootPath(), bmPool.cbcList);
			    	  
			    	  MessageDialog.openInformation(new Shell(), "Query Status", "Query executes successfully!");
			    	  
				      ripristine();
				     
				
			}
			

		 });
		 
		 image = Activator.getImageDescriptor("icons/ToolBarBMQ/executeJPQL.png").createImage();
		 if (image!=null)  itemExecute.setImage(image);
		 /*
		  * SEPARATOR
		  */
		 ToolItem itemS3 = new ToolItem(tb, SWT.SEPARATOR , 2);
		 itemS3.setWidth(40);
		
		 tb.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
	}


	protected void ripristine() {
		selectList = new ArrayList<String>();
	   	whereList = new ArrayList<String>();
	   	fromList = new ArrayList<String>();
	   	//pulisco le tabelle
		selectTable.removeAll();
		selectTable.clearAll();
		
		whereTable.removeAll();
		whereTable.clearAll();
		
	}

	private void createPartEditor() {
		 
		 bmGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		 bmGroup.setText("CBC Query");
		 GridLayout glDet = new GridLayout();
		 GridData gd = new GridData(GridData.FILL_BOTH);
		 glDet.numColumns = 2;
		 glDet.makeColumnsEqualWidth =true;
		 bmGroup.setLayout(glDet);
		 bmGroup.setLayoutData(gd);
		 
		 //-------SELECT
				 Group selectGroup = new Group(bmGroup,SWT.SHADOW_ETCHED_IN);
				 selectGroup.setText("SELECT: ");
				 glDet.numColumns = 1;
				 selectGroup.setLayout(glDet);
				 selectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				 
				 selectTable = new Table(selectGroup, SWT.BORDER);
				 selectTable.setLayout(glDet);
				 selectTable.setLayoutData(gd);
				 // Create the drop target on the button
				    DropTarget dt = new DropTarget(selectTable, DND.DROP_MOVE);
				    dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
				    dt.addDropListener(new DropTargetAdapter() {

					public void drop(DropTargetEvent event) {
				       
				    	  if (event.data != null){
					    	  String table = event.data.toString();
					    	  String[] tables= table.split("#");
					    	  if(tables.length!=3) return;
					    	  
					    	  if (!fromList.contains(tables[1])){
					    		  //aggiungo la cbc
					    		  fromList.add(tables[1]);
					    	  }
				    			
				    		  if (!selectList.contains(tables[1]+"."+tables[2])){
				    			  selectList.add(tables[1]+"."+tables[2]);
				    			  TableItem ti = new TableItem(selectTable, 0);
				    			  ti.setText(tables[1]+"."+tables[2]);
				    		  } 
				    	  }
				      }
				    });
		
		 //---------WHERE		 
				 Group whereGroup = new Group(bmGroup,SWT.SHADOW_ETCHED_IN);
				 whereGroup.setText("WHERE: ");
				 glDet.numColumns = 1;
				 whereGroup.setLayout(glDet);
				 whereGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				 
				 whereTable = new Table(whereGroup, SWT.BORDER);
				 whereTable.setLayout(glDet);
				 whereTable.setLayoutData(gd);
				 
				 DropTarget dt2 = new DropTarget(whereTable, DND.DROP_MOVE);
				    dt2.setTransfer(new Transfer[] { TextTransfer.getInstance() });
				    dt2.addDropListener(new DropTargetAdapter() {
				      public void drop(DropTargetEvent event) {
				    	  
				    	  if (event.data != null){
					    	  String table = event.data.toString();
					    	  String[] tables= table.split("#");
					    	  if(tables.length!=3) return;
					    	  
				    		  if (!fromList.contains(tables[1])){
				    			  fromList.add(tables[1]);
				    		  }
				    		  
						    WhereWizard wizard = new WhereWizard(whereList, whereTable,  tables[1]+"."+tables[2]);
							WizardDialog dialog = new WizardDialog(new Shell(), wizard);
							dialog.open();
				    	  }
				    	  
				    }
				    	  
				    });
				    
	//AGGIUNGO I LISTENER X OGNI TABELLA
	selectTable.addListener(SWT.MouseDown, new Listener () {
		public void handleEvent (Event event) {
			Point point = new Point (event.x, event.y);
			TableItem item = selectTable.getItem(point);
			if (item != null) {
			Shell shell = composite.getParent().getParent().getShell();
			Menu menu = new Menu (shell, SWT.POP_UP);
			createDelMenu(menu,item,selectTable,selectList,point);
			menu.setVisible(true);
			shell.setMenu (menu);
			shell.open();
			}
		}
	});
	
	whereTable.addListener(SWT.MouseDown, new Listener () {
		public void handleEvent (Event event) {
			Point point = new Point (event.x, event.y);
			TableItem item = whereTable.getItem(point);
			if (item != null) {
			Shell shell = composite.getParent().getParent().getShell();
			Menu menu = new Menu (shell, SWT.POP_UP);
			createDelMenu(menu,item,whereTable,whereList,point);
			menu.setVisible(true);
			shell.setMenu (menu);
			shell.open();
			}
		}
	});
		
	}

	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * CREA UN MENU POPUP ASSOCIATO AD OGNI ELEMENTO DELLA TABELLA E PERMETTE DI CANCELLARLO
	 * 
	 * nel caso in cui si tratti di un item della tabella from vengono eliminate anche le clausole
	 * o le select che derivano da quella tabella
	 * @param list 
	 * @param table 
	 * @param point 
	 * 
	 * */
	private void createDelMenu(Menu menu, final TableItem ti, final Table table, final ArrayList<String> list, final Point point) {
		/*
		 * DELETE: 
		 * */
		MenuItem delete = new MenuItem (menu, SWT.PUSH);
		delete.setText ("Delete");
		Image image = Activator.getImageDescriptor("icons/cancel.png").createImage();
		if (image!=null) delete.setImage(image);
		delete.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				
				//////////////////////////////////////////////////////////////////////////////////
				if (table.equals(selectTable)){
					//RIMUOVO DALLA LISTA
					int max = list.size();
					ArrayList<String> remove = new ArrayList<String>();
					for (int i = 0 ; i < max ; i++){
						if(selectList.get(i).contains(ti.getText()))
							remove.add(selectList.get(i));
					}
					for (int i = 0; i<remove.size(); i++){
						selectList.remove(remove.get(i));
					}
					selectTable.removeAll();
					selectTable.clearAll();
					//inserisco i dati
					if(!selectList.isEmpty()){
						for(int i =0 ; i < selectList.size(); i++){
							TableItem tit = new TableItem(selectTable, 0);
							tit.setText(selectList.get(i));
						}
					}
					else{
						if (whereList.isEmpty()){
							fromList.clear();
						}
					}
					
				}
				
				if (table.equals(whereTable)){
					//RIMUOVO DALLA LISTA
					int max = list.size();
					ArrayList<String> remove = new ArrayList<String>();
					for (int i = 0 ; i < max ; i++){
						if(whereList.get(i).contains(ti.getText()))
							remove.add(whereList.get(i));
					}
					for (int i = 0; i<remove.size(); i++){
						whereList.remove(remove.get(i));
					}
					whereTable.removeAll();
					whereTable.clearAll();
					//inserisco i dati
					if(!whereList.isEmpty()){
						for(int i =0 ; i < whereList.size(); i++){
							TableItem tit = new TableItem(whereTable, 0);
							tit.setText(whereList.get(i));
						}
					}
					else{
						if(selectList.isEmpty()){
							fromList.clear();
						}
					}
					
				}
				
				
				
				
			}
				
		});
	}

	

	
}
