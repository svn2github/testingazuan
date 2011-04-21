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
import java.util.HashMap;
import java.util.Iterator;

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

public class BMQEditorView extends ViewPart {


	public static final String ID = "bi.bmm.views.bmq.bmqeditor";
	public Composite composite;
	public Group bmGroup;
	
	private Table selectTable;
	private Table whereTable;
	protected ArrayList<String> selectList;
	protected ArrayList<String> fromList;
	protected ArrayList<String> whereList;
	private Table fromTable;
	private ArrayList<String> joinList;
	private ArrayList<String> justFromList;
	
	public BMQEditorView() {
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
		  *   ADD ITEMS -> index at [0]
		  */
		 ToolItem itemAdd = new ToolItem(tb, SWT.PUSH , 0);
		 itemAdd.setToolTipText("Add element");
		 itemAdd.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		      //TODO: GESTIRE INSERIMENTO ELEMENTO
		      }
		 });
		 image = Activator.getImageDescriptor("icons/ToolBarBMQ/add.png").createImage();
		 if (image!=null)  itemAdd.setImage(image);
		
		 
		 /*
		  *   REMOVE ITEMS -> index at [1]
		  */
		 ToolItem itemRem= new ToolItem(tb, SWT.PUSH , 1);
		 itemRem.setToolTipText("Remove.");
		 itemRem.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	//TODO: GESTIONE EVENTO DI REMOVE
		      }
		 });
		 
		 image = Activator.getImageDescriptor("icons/ToolBarBMQ/remove.png").createImage();
		 if (image!=null)  itemRem.setImage(image);
		 
		 /*
		  * SEPARATOR
		  */
		 ToolItem itemS1 = new ToolItem(tb, SWT.SEPARATOR , 2);
		 itemS1.setWidth(40);
		 
			
		 /*
		  *   View JPQL   -> index at [0]
		  */
		 ToolItem itemViewJPQL = new ToolItem(tb, SWT.PUSH , 3);
		 itemViewJPQL.setToolTipText("View the current JPQL query.");
		 itemViewJPQL.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  
		    	  MessageDialog.openInformation(new Shell(), "Current JPQL query", generateJPQL());
		    	  
		      }
		 });
		 
		 image = Activator.getImageDescriptor("icons/ToolBarBMQ/viewJPQL.png").createImage();
		 if (image!=null)  itemViewJPQL.setImage(image);
		 
		 /*
		  *   Execute JPQL-> index at [6]
		  */
		 ToolItem itemExecute = new ToolItem(tb, SWT.PUSH , 4);
		 itemExecute.setToolTipText("Execute the JPQL query.");
		 itemExecute.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
		      //TODO: GEASTIONE DELL'ESEGUZIONE DELLA QUERY
		    	  //creo la JPQL query
		    	  String query = generateJPQL();
		    	  
		    	  
		    	  if (query != null){
			    	  //creo un service con la jpql
			    	  QueryUtil qu = new QueryUtil(query,selectList,fromList,whereList);
			    	  
			    	  BMQResourceView bmResource = (BMQResourceView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().findView("bi.bmm.views.bmq.bmqresource");
			    	  
			    	  qu.createService(bmResource.getBCList().get(0).getRootPath());
			    	  
			    	  MessageDialog.openInformation(new Shell(), "Query Status", "Query executes successfully!");
			    	  
				      ripristine();
		    	  }
		      }
			

		 });
		 
		 image = Activator.getImageDescriptor("icons/ToolBarBMQ/executeJPQL.png").createImage();
		 if (image!=null)  itemExecute.setImage(image);
		 /*
		  * SEPARATOR
		  */
		 ToolItem itemS3 = new ToolItem(tb, SWT.SEPARATOR , 5);
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
		
		fromTable.removeAll();
		fromTable.clearAll();
	}

	private void createPartEditor() {
		 
		 bmGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		 bmGroup.setText("BM JPQL");
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
						    	  if(tables.length!=2) return;
						    	  
					    	  if (!fromList.contains(tables[0])){
					        	  //se non è presente vedo se esiste un modo per navigare 
				    			  //tale classe
				    			 
				    			  // richiamo la bmq view
				    			  BMQResourceView bmResource = (BMQResourceView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				  				  .getActivePage().findView("bi.bmm.views.bmq.bmqresource");
				    			  
				    			  String[] from = bmResource.hasNavigation(event.data.toString(),fromList);
				    			  if ( from != null){
				    				  //posso navigare la classe da qui
				    				  if (!selectList.contains(from[0]+"."+from[1]+"."+tables[1]))
						    		  {
								    	  selectList.add(from[0]+"."+from[1]+"."+tables[1]);
								    	  
							    		  TableItem ti = new TableItem(selectTable, 0);
								    	  ti.setText(from[0]+"."+from[1]+"."+tables[1]);
								      }
				    			  }
				    		  else{
				    			  
					    			  if (MessageDialog.openConfirm(new Shell(), "From Missed","This field can't be derived from existing tables, do you want import " +
					    			  		"the table "+tables[0]+"?")){
					    				  if (!selectList.contains(tables[0]+"."+tables[1]))
							    		  {
									    	  selectList.add(tables[0]+"."+tables[1]);
								    		  
									    	  TableItem ti = new TableItem(selectTable, 0);
									    	  ti.setText(tables[0]+"."+tables[1]);
							    		  
					    				  fromList.add(tables[0]);
							        	  /*
					    				  ti = new TableItem(fromTable, 0);
							        	  ti.setText(tables[0]);
							        	  */
							    		  }
					    			  
					    			  }
				    		  		}
				    			  }
				    		
				    	      else
				    	      {
				    	    	  if (!selectList.contains(tables[0]+"."+tables[1]))
					    		  {
							    	  selectList.add(tables[0]+"."+tables[1]);
						    		  
							    	  TableItem ti = new TableItem(selectTable, 0);
							    	  ti.setText(tables[0]+"."+tables[1]);
	
					    		  }
				    	      }
				    		  
				    		  
				    	  }
				    	  
				      }


				    });
				    
		/*	OBSOLETE	    
		//-------FROM
		Group fromGroup = new Group(bmGroup,SWT.SHADOW_ETCHED_IN);
		fromGroup.setText("FROM: ");
		glDet.numColumns = 1;
		fromGroup.setLayout(glDet);
		fromGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
					 
					 fromTable = new Table(fromGroup, SWT.BORDER);
					 fromTable.setLayout(glDet);
					 fromTable.setLayoutData(gd);
					 // Create the drop target on the button
					    DropTarget dt3 = new DropTarget(fromTable, DND.DROP_MOVE);
					    dt3.setTransfer(new Transfer[] { TextTransfer.getInstance() });
					    dt3.addDropListener(new DropTargetAdapter() {
					      public void drop(DropTargetEvent event) {
					       
					    	  if (event.data != null){
						    	  String table = event.data.toString();
						    	  String[] tables= table.split("#");
					    		  if (!fromList.contains(tables[0])){
						        	  fromList.add(tables[0]);
						        	  TableItem ti = new TableItem(fromTable, 0);
						        	  ti.setText(tables[0]);
						          }
						    						    		  
					    	  }
					    	  
					      }
					    });
		*/
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
					    	  if(tables.length!=2) return;
					    	  
				    		  if (!fromList.contains(tables[0])){
					        	  //se non è presente vedo se esiste un modo per navigare 
				    			  //tale classe
				    			 
				    			  // richiamo la bmq view
				    			  BMQResourceView bmResource = (BMQResourceView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				  				  .getActivePage().findView("bi.bmm.views.bmq.bmqresource");
				    			  
				    			  String[] from = bmResource.hasNavigation(event.data.toString(),fromList);
				    			  if ( from != null){
				    				  //posso navigare la classe da qui

						    		  WhereWizard wizard = new WhereWizard(whereList, whereTable,  from[0]+"."+from[1]+"."+tables[1]);
							  	      WizardDialog dialog = new WizardDialog(new Shell(), wizard);
							  	      dialog.open();
				    			  }
				    			  else{
				    				  /*OBSOLETE
					    			  if (MessageDialog.openConfirm(new Shell(), "From Missed","This field can't be derived from existing tables, do you want import " +
					    			  		"the table"+tables[0]+"?")){
					    					*/
					    				  WhereWizard wizard = new WhereWizard(whereList, whereTable,  tables[0]+"."+tables[1]);
								  	      WizardDialog dialog = new WizardDialog(new Shell(), wizard);
								  	      dialog.open();
								  	      
					    				  fromList.add(tables[0]);
							        	 /*OBS
					    				  TableItem ti = new TableItem(fromTable, 0);
							        	  ti.setText(tables[0]);
							        	}
							        	*/
				    			  }
					          }
					    	
				    		  else{
				    		  
					    		  WhereWizard wizard = new WhereWizard(whereList, whereTable,  tables[0]+"."+tables[1]);
						  	      WizardDialog dialog = new WizardDialog(new Shell(), wizard);
						  	      dialog.open();
					    	  }
				    	
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
	/*obs
	fromTable.addListener(SWT.MouseDown, new Listener () {
		public void handleEvent (Event event) {
			Point point = new Point (event.x, event.y);
			TableItem item = fromTable.getItem(point);
			if (item != null) {
			Shell shell = composite.getParent().getParent().getShell();
			Menu menu = new Menu (shell, SWT.POP_UP);
			createDelMenu(menu,item,fromTable,fromList,point);
			menu.setVisible(true);
			shell.setMenu (menu);
			shell.open();
			}
		}
	});
	*/
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
				
				/*
				//NEL CASO SI TRATTASSE DI UNA FROM DEVO RIMUOVERE ANCHE QUELLI CHE CI SONO ASSOCIATI
				if (table.equals(fromTable)){
					//ricostruisco la SELECT LIST
					int max = selectList.size();
					ArrayList<String> remove = new ArrayList<String>();
					for (int i = 0 ; i < max ; i++){
						if(selectList.get(i).contains(ti.getText()))
							remove.add(selectList.get(i));
					}
					for (int i = 0; i<remove.size(); i++){
						selectList.remove(remove.get(i));
					}
					
					//ricostruisco la where LIST
					max = whereList.size();
					remove = new ArrayList<String>();
					for (int i = 0 ; i < max ; i++){
						if(whereList.get(i).contains(ti.getText()))
							remove.add(whereList.get(i));
					}
					for (int i = 0; i<remove.size(); i++){
						whereList.remove(remove.get(i));
					}
					
					//pulisco le tabelle
					selectTable.removeAll();
					selectTable.clearAll();
					
					whereTable.removeAll();
					whereTable.clearAll();
					//inserisco i dati
					if(!selectList.isEmpty()){
						for(int i =0 ; i < selectList.size(); i++){
							TableItem tit = new TableItem(selectTable, 0);
							tit.setText(selectList.get(i));
						}
					}
					if(!whereList.isEmpty()){
						for(int i =0 ; i < whereList.size(); i++){
							TableItem tit = new TableItem(whereTable, 0);
							tit.setText(whereList.get(i));
						}
					}
					//rimuovo dalla from
					max = fromList.size();
					remove = new ArrayList<String>();
					for (int i = 0 ; i < max ; i++){
						if(fromList.get(i).contains(ti.getText()))
							remove.add(fromList.get(i));
					}
					for (int i = 0; i<remove.size(); i++){
						fromList.remove(remove.get(i));
					}
					
					fromTable.removeAll();
					fromTable.clearAll();
					//inserisco i dati
					if(!fromList.isEmpty()){
						for(int i =0 ; i < fromList.size(); i++){
							TableItem tit = new TableItem(fromTable, 0);
							tit.setText(fromList.get(i));
						}
					}
				}	
				*/
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

	

	/**
	 * METODO DI GENERAZIONE DELLA QUERY
	 * 
	 * */
	private String generateJPQL() {
		
	if (selectList.isEmpty())
  	  {
  		  MessageDialog.openError(new Shell(), "NO Current JPQL query available", "Drag and Drop elements to build a jpql query.");
  		  return null;
			     
  	  }
  	  else
  	  {
  		//pulisco la fromList
  		  ArrayList<String> newFromList = new ArrayList<String>();
  		  justFromList = new ArrayList<String>();
	  		//se ci sono tabelle che non sono ne in select ne in where le elimino
	  		Iterator<String> itFrom = fromList.iterator();
	  		while (itFrom.hasNext()){
	  			String from = itFrom.next();
	  			Iterator<String> itSel = selectList.iterator();
	  			while (itSel.hasNext()){
	  				String select = itSel.next();
	  				if (select.split("\\.")[0].equals(from)){
	  					if (!newFromList.contains(from))
	  					{
	  						newFromList.add(from);
	  						justFromList.add(from);
	  					}
	  				}
	  			}
	  			Iterator<String> itWhere = whereList.iterator();
	  			while (itWhere.hasNext()){
	  				String where = itWhere.next();
	  				if (where.split("\\.")[0].equals(from)){
	  					if (!newFromList.contains(from))
	  						newFromList.add(from);
	  				}
	  			}
	  		}
	  		fromList.clear();
	  		fromList = newFromList;
	    	  //assegno le hash map
	    	  HashMap<String, String> aliasMap = new HashMap<String, String>();
	    	  
	    	  for(int i =0; i< fromList.size(); i++){
	    		  aliasMap.put(fromList.get(i), "e"+i);
	    	  }
	    	  
	    	  
	    	  
	    	 	  //trovo quali sono i campi del from che sono differenti 
	    		  joinList = new ArrayList<String>();
	    		  Iterator<String> it = fromList.iterator();
	    		  while(it.hasNext()){
	    			String from = it.next();
	    				joinList.add(from);
	    		  }
	    		//Interpreto i join
		    	  // richiamo la bmq view
				  BMQResourceView bmResource = (BMQResourceView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					  .getActivePage().findView("bi.bmm.views.bmq.bmqresource");
				  if(!justFromList.equals(joinList)){
					  for (int i = 0; i < justFromList.size(); i++){
			    		  for(int j = 0; j < joinList.size();j++){
			    			  if(bmResource.hasRelation(justFromList.get(i),joinList.get(j))){
			    				     String[] relations = bmResource.getRelation(justFromList.get(i),joinList.get(j));
			    				     //cambio nell'alias map aggiungendo . e l'elemento della relazione
			    					 if (aliasMap.containsKey(justFromList.get(i))){
			    						 aliasMap.remove(justFromList.get(i));
			    						 String value = aliasMap.get(joinList.get(j))+"."+relations[1];
			    						 aliasMap.put(justFromList.get(i), value);
			    						 justFromList.remove(i);
			    						 justFromList.add(joinList.get(j));
			    					 }
			    					}
			    		  }
			    	  }
				  }
				  else{
					  for (int i = 0; i < justFromList.size()-1; i++){
			    		  for(int j = 1; j < joinList.size();j++){
			    			  if(bmResource.hasRelation(justFromList.get(i),joinList.get(j))){
			    				     String[] relations = bmResource.getRelation(justFromList.get(i),joinList.get(j));
			    				     //cambio nell'alias map aggiungendo . e l'elemento della relazione
			    					 if (aliasMap.containsKey(justFromList.get(i))){
			    						 aliasMap.remove(justFromList.get(i));
			    						 String value = aliasMap.get(joinList.get(j))+"."+relations[1];
			    						 aliasMap.put(justFromList.get(i), value);
			    						 justFromList.remove(i);
			    						 if (!justFromList.contains(joinList.get(j)))
			    						 justFromList.add(joinList.get(j));
			    					 }
			    					}
			    		  }
			    	  }
				  }
	    	  
	    	//Creo la query
	    	//preparo la select  
	    	  String query = "SELECT ";
		      
	    	  String select = aliasMap.get(selectList.get(0).split("\\.")[0])  ;
	    	  for(int i = 1; i< selectList.get(0).split("\\.").length;i++){
	    		  select +="."+ selectList.get(0).split("\\.")[i];
	    	  }
	    	  query+=""+select+" ";
	    	  for(int i =1; i< selectList.size(); i++){
	    		  select = aliasMap.get(selectList.get(i).split("\\.")[0]);
	    		  for(int j = 1; j< selectList.get(i).split("\\.").length;j++){
		    		  select += "."+selectList.get(i).split("\\.")[j];
		    	  }
	    		  query+=", "+select+" ";
	    	  }
	    	  	    	  		
	    	  //preparo la from
	    	  query+="FROM ";
	    	  query+=""+justFromList.get(0)+" "+aliasMap.get(justFromList.get(0))+" ";
	    	  for(int i =1; i< justFromList.size(); i++){
	    		  query+=", "+justFromList.get(i)+" "+aliasMap.get(justFromList.get(i))+" ";
	    	  }
	    	  //preparo la where
	    	  if (!whereList.isEmpty())
	    	  {	    	  
		    	  query+="WHERE ";
		    	  
		    	  String where = aliasMap.get(whereList.get(0).split("\\.")[0]);
		    	  for(int i = 1; i< whereList.get(0).split("\\.").length;i++){
		    		 where += "."+ whereList.get(0).split("\\.")[i];
		    	  }
		    	  
		    	  query+=""+where+" ";
		    	  for(int i =1; i< whereList.size(); i++){
		    		  where = aliasMap.get(whereList.get(i).split("\\.")[0]);
		    		  for(int j = 1; j< whereList.get(i).split("\\.").length;j++){
			    		 where += "." + whereList.get(i).split("\\.")[j];
			    	  }
		    		  query+=" AND "+where+" ";
		    	  }
	    	  }
	    	  
	    	  
	    	  return query;
  	  }
	}
}
