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
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import bi.bmm.util.ConnectionsPool;
import bi.bmm.util.WSConnection;
import bi.bmm.util.WSPool;
import bi.bmm.wizards.NewConnectionWizard;
import bi.bmm.wizards.NewWSWizard;


public class DataPools extends ViewPart {
	
	public static final String ID = "bi.bmm.views.bme.datapools";
	public ConnectionsPool activeConn;
	public WSPool activeWS;
	private Table connTable;
	
	public DataPools() {
		// TODO Auto-generated constructor stub
    	activeConn = new ConnectionsPool();
    	activeWS = new WSPool();
		}

	@Override
	public void createPartControl(final Composite parent) {
		//Permette di creare una nuova connessione ad una sorgente dati
		
		Composite container = new Composite(parent, SWT.NONE);
		GridData data = new GridData(GridData.GRAB_HORIZONTAL);
		GridData dataBoth = new GridData(GridData.FILL_BOTH);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.numColumns = 2; 
		gridLayout.makeColumnsEqualWidth = true;
		container.setLayout(gridLayout); 
		container.setLayoutData(dataBoth);
		GridLayout butLayout = new GridLayout();
		butLayout.numColumns = 4;
		butLayout.makeColumnsEqualWidth = true;
		Composite buttContainer = new Composite(container, SWT.NONE);
		buttContainer.setLayout(butLayout);
		buttContainer.setLayoutData(data);  
		//Bottone per una nuova connessione
		final Button buttonCreate = new Button(buttContainer, SWT.NONE);
		Image image = Activator.getImageDescriptor("icons/connNew.png").createImage();
		if (image!=null)buttonCreate.setImage(image);
		buttonCreate.setText("Create a new RDB connection");
		//Label di riempimento;
		Label l2 = new Label(buttContainer, SWT.NULL);
		l2.setText("");
		Label l3 = new Label(buttContainer, SWT.NULL);
		l3.setText("");
		Label l4 = new Label(buttContainer, SWT.NULL);
		l4.setText("");
		//Bottone per un nuovo WS
		final Button buttonWS = new Button(buttContainer, SWT.NONE);
		image = Activator.getImageDescriptor("icons/wsNew.png").createImage();
		if (image!=null)buttonWS.setImage(image);
		buttonWS.setText("Create a new WS connection ");
		
		//viewer per le connessioni attive
		connTable = new Table(container, SWT.BORDER);
		connTable.setLayoutData(dataBoth);
		
		//Creo un nuovo listener x la connessione
		Listener listenerCreate = new Listener() {
		      public void handleEvent(Event event) {
		        if (event.widget == buttonCreate) {
		        	//aprire il wizard di una nuova connessione
		        	NewConnectionWizard wizard = new NewConnectionWizard(activeConn);
		        	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		    		dialog.open();
		    		if(dialog.getReturnCode() == WizardDialog.OK){
		    			repaintTable();
		    		}
		        	
		        } else {
		        	//TODO: altra condizione
		        }
		      }
		    };
		    
		    
		  //Creo un nuovo listener x il ws
			Listener listenerWS = new Listener() {

			      public void handleEvent(Event event) {
			        if (event.widget == buttonWS) {
			        	//aprire il wizard di una nuova connessione

			        	NewWSWizard wizard = new NewWSWizard(activeWS);
			        	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
			    		dialog.open();
			    		if(dialog.getReturnCode() == WizardDialog.OK){
			    			//ridisegno la tanella con le nuove conn attive
			    			repaintTable();
			    			
			    		}
			        	
			        } else {
			        	//TODO: altra condizione
			        }
			      }
			    };
	//creo e associo un listener per ogni elemento della tabella delle connessioni
		
	connTable.addListener(SWT.MouseDown, new Listener () {
		public void handleEvent (Event event) {
			Point point = new Point (event.x, event.y);
			TableItem item = connTable.getItem(point);
			if (item != null) {
				Shell shell = parent.getShell();
				Menu menu = new Menu (shell, SWT.POP_UP);
				createConnMenu(menu,item);
				menu.setVisible(true);
				shell.setMenu (menu);
				shell.open();

			}
		}

private void createConnMenu(Menu menu, final TableItem item) {
		
		if(item.getText().contains("@")){
			/*
			 * EXTRACT: permette di estrarre oggetti da una conn
			 * 
			 * genera automaticamente un file persistence
			 */
			MenuItem extract = new MenuItem (menu, SWT.PUSH);
			extract.setText ("Extract Item");
			Image image = Activator.getImageDescriptor("icons/connExtract.png").createImage();
			if (image!=null) extract.setImage(image);
			extract.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event event) {
				//genero il file persistence.xml
					String ttText = item.getText();
					String ttDDot[] = ttText.split(":");
					String ttName = ttDDot[0];
					
					
				//recupero la view el
					ELUniverseView elUniverse = (ELUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findView("bi.bmm.views.bme.eluniverse");
				
				
				//inserisco i dati nella view el
					
					//chiedo conferma 
					if (MessageDialog.openConfirm(new Shell(), "Extract Procedure","Would you start the extract procedure from "+
							ttName+" connection?")){
							elUniverse.addTables(activeConn.getConnection(ttName));
					}
				
				}
			});
			
			/*
			 * DELETE: permette di eliminare una connessione
			 * */
			MenuItem delete = new MenuItem (menu, SWT.PUSH);
			delete.setText ("Delete");
			image = Activator.getImageDescriptor("icons/connDelete.png").createImage();
			if (image!=null) delete.setImage(image);
			delete.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event event) {
					//elimino la connessione
					String ttText = item.getText();
					String ttDDot[] = ttText.split(":");
					String ttName = ttDDot[0];
					activeConn.deleteConnection(ttName);
					//ridisegno nella tabella le connessioni attive
					repaintTable();
				}
			});
			/*
			 * SPACE
			 * */
			 new MenuItem(menu, SWT.SEPARATOR);
			
			/*
			 * ABOUT: permette di ottenere informazioni sulla connessione
			 * */
			MenuItem about = new MenuItem (menu, SWT.PUSH);
			about.setText ("About");
			image = Activator.getImageDescriptor("icons/connAbout.png").createImage();
			about.setImage(image);
			about.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event event) {
					Shell shell = parent.getShell();
					ToolTip tt = new ToolTip(shell, SWT.BALLOON);
					String ttText = item.getText();
					String ttDDot[] = ttText.split(":");
					String ttName = ttDDot[0];
					String ttDDot2[] =  ttDDot[1].split("@");
					String ttUser = ttDDot2[0];
					String ttServer = ttDDot2[1];
					String ttDDot3[] = ttDDot[2].split(" / ");
					String ttPort = ttDDot3[0];
					String ttDb = ttDDot3[1];
					tt.setText("Connection Details:");
					tt.setMessage("Name: "+ttName+
							"\nServer: "+ ttServer+ " at Port: "+ttPort+
							"\nDatabase: "+ttDb+ " User: "+ttUser);
					tt.setVisible(true);
					tt.setAutoHide(true);
				}
			});
			
		}
		//END RDB CONN CASE
		
		//START WS CONN CASE
		else{
			/*
			 * EXTRACT: permette di estrarre oggetti da una conn
			 * 
			 * genera automaticamente un file persistence
			 */
			MenuItem extract = new MenuItem (menu, SWT.PUSH);
			extract.setText ("Extract Item");
			Image image = Activator.getImageDescriptor("icons/wsExtract.png").createImage();
			if (image!=null) extract.setImage(image);
			extract.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event event) {
				//recupero il nome
					String ttText = item.getText();
					String ttDDot[] = ttText.split(" - ");
					String ttName = ttDDot[0];
					
					
				//recupero la view el
					ELUniverseView elUniverse = (ELUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findView("bi.bmm.views.bme.eluniverse");
				
				
				//inserisco i dati nella view el
					
					//chiedo conferma 
					if (MessageDialog.openConfirm(new Shell(), "Extract Procedure","Would you start the extract procedure from "+
							ttName+" connection?")){
							elUniverse.addWSTables(activeWS.getConnection(ttName));
					}
				
				}
			});
			
			/*
			 * DELETE: permette di eliminare una connessione
			 * */
			MenuItem delete = new MenuItem (menu, SWT.PUSH);
			delete.setText ("Delete");
			image = Activator.getImageDescriptor("icons/wsDelete.png").createImage();
			if (image!=null) delete.setImage(image);
			delete.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event event) {
					//elimino la connessione
					String ttText = item.getText();
					String ttDDot[] = ttText.split(" - ");
					String ttName = ttDDot[0];
					activeWS.deleteConnection(ttName);
					//ridisegno nella tabella le connessioni attive
					repaintTable();
					
				}
			});
			/*
			 * SPACE
			 * */
			 new MenuItem(menu, SWT.SEPARATOR);
			
			/*
			 * ABOUT: permette di ottenere informazioni sulla connessione
			 * */
			MenuItem about = new MenuItem (menu, SWT.PUSH);
			about.setText ("About");
			image = Activator.getImageDescriptor("icons/wsAbout.png").createImage();
			about.setImage(image);
			about.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event event) {
					Shell shell = parent.getShell();
					ToolTip tt = new ToolTip(shell, SWT.BALLOON);
					String ttText = item.getText();
					String ttDDot[] = ttText.split(" - ");
					String ttName = ttDDot[0];
					tt.setText("Connection Details:");
					tt.setMessage(activeWS.getConnection(ttName).toString());
					tt.setVisible(true);
					tt.setAutoHide(true);
				}
			});
			
		}
		//END RDB CONN CASE
		}
	});
	
	//associo il Listener
	buttonCreate.addListener(SWT.Selection, listenerCreate);
	buttonWS.addListener(SWT.Selection, listenerWS);
	//buttonCreate.setLayoutData(data);
	//connTable.setLayoutData(data);
	
	
	} 
	protected void repaintTable() {
		//Carico le connessioni nella tabella
		List<String> t = activeConn.getAllConnection();
		Iterator<String> itr = t.iterator(); 
		//pulisco la lista delle connessioni
		connTable.removeAll();
		//Creo una lista delle connessioni
		while(itr.hasNext()) {

		    Object element = itr.next(); 
		    TableItem ti = new TableItem(connTable, SWT.BORDER);
		    Image image = Activator.getImageDescriptor("icons/connActive.png").createImage();
		    if (image!=null) ti.setImage(image);
		    ti.setText(element.toString());
		} 
		//carico i WS nella tabella
		ArrayList<WSConnection> ws = activeWS.getAllWS();
		Iterator<WSConnection> iter = ws.iterator();
		while(iter.hasNext()){
			WSConnection element = iter.next();
			TableItem ti = new TableItem(connTable, SWT.BORDER);
			Image image = Activator.getImageDescriptor("icons/wsValid.png").createImage();
			if (image!=null) ti.setImage(image);
			ti.setText(element.getName()+" - "+element.getEndPoint());
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	
}
