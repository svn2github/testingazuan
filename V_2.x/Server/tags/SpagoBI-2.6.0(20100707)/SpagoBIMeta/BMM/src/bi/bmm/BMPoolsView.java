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


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import bi.bmm.util.ClassInfo;
import bi.bmm.util.ComplexClassInfo;
import bi.bmm.util.ConstantString;
import bi.bmm.util.HunkIO;
import bi.bmm.util.QueryUtil;


public class BMPoolsView extends ViewPart {
	
	public static final String ID = "bi.bmm.views.bmq.bmpools";
	
	
	protected String bmName;


	protected String bmPath;


	public ArrayList<ClassInfo> bcList;

	public ArrayList<ComplexClassInfo>cbcList;
	public ArrayList<String[]> relList;
	
		
	public BMPoolsView() {
		
		bcList = new ArrayList<ClassInfo>();
		relList = new ArrayList<String[]>();
		cbcList = new ArrayList<ComplexClassInfo>();
	}

	@Override
	public void createPartControl(final Composite parent) {
		//Permette di aprire un nuovo BM
		
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
		
		//Bottone per aprire un nuovo BM
		final Button buttonCreate = new Button(buttContainer, SWT.NONE);
		Image image = Activator.getImageDescriptor("icons/bmOpen.png").createImage();
		if (image!=null)buttonCreate.setImage(image);
		buttonCreate.setText("Open a BM");
		
		//viewer per le connessioni attive
		final Table connTable = new Table(container, SWT.BORDER);

	    connTable.setLayoutData(dataBoth);
	    

		TableColumn tc1 = new TableColumn(connTable, SWT.CENTER);
		TableColumn tc2 = new TableColumn(connTable, SWT.CENTER);
		TableColumn tc4 = new TableColumn(connTable, SWT.CENTER);
		TableColumn tc5 = new TableColumn(connTable, SWT.CENTER);
			  tc1.setText("Name");
		    tc2.setText("Path");
		    tc4.setText("BC #");
		    tc5.setText("CBC #");
			tc1.setWidth(90);
		    tc2.setWidth(130);
		    tc4.setWidth(50);
		    tc5.setWidth(50);
		connTable.setHeaderVisible(true);
			
		
		//Creo un nuovo listener
		Listener listenerCreate = new Listener() {
		      public void handleEvent(Event event) {
		        if (event.widget == buttonCreate) {
		        	
		        	FileDialog fileDialog = new FileDialog(new Shell());
		    		// Set the text
		    		fileDialog.setText("Select a Business Model");
		    		// Set filter on .txt files
		    		fileDialog.setFilterExtensions(new String[] { "*.bm" });
		    		// Put in a readable name for the filter
		    		fileDialog.setFilterNames(new String[] { "BusinessModel(*.bm)" });
		    		// Open Dialog and save result of selection
		    		String selected = fileDialog.open();
		    		

		    		
		    		try {
		    			
		    			String result = HunkIO.readEntireFile(selected,
		    			         "UTF-8" );
		    			String defInfoFile = result.split("<DEFAULT_INFO_FILE>")[1].split("</DEFAULT_INFO_FILE>")[0];
		    			
		    			bmName = defInfoFile.split("#")[0];
		    			bmPath = defInfoFile.split("#")[1];
		    			
		    			HunkIO.writeEntireFile(HunkIO.DEFAULT_INFO_FILE, defInfoFile, "UTF-8");
		    			
		    			//MC: ricavo directory del progetto corrente
		    			URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
		    		    String path_project = url.toExternalForm().substring(6).replace("/", "//");
		    		    System.out.println("BMPoolsView path proj: "+path_project);
		    		    //MessageDialog.openInformation(new Shell(), "BMPoolsView path proj", path_project);
		    		    
		    			//Workaround per export come RCP Application
		    			//String path_project = System.getProperty("user.dir").replace("\\", "//");
		    			//MessageDialog.openInformation(new Shell(), "BMPoolsView", path_project);
		    			//URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
		    			//String file_name = url.toExternalForm().substring(6).replace("/", "//");
		    			//MessageDialog.openInformation(new Shell(), "Filename", file_name);
		    			
		    			
		    			//Ottengo la vista
		    			openFile(result.split("<bmUniverse>\n")[1].split("</bmUniverse>")[0]);
		    			
		    			//TODO: VERIFICARE LA CORRETTEZZA DELLA POSISZIONE
		    			//importo IL PERSISTENCE nella cartella d'installazione
		    			if (!bcList.isEmpty()){
		    				ClassInfo ci =bcList.get(0);
		    				File f = new File(ci.getRootPath()+"src/META-INF/persistence.xml");
		    				if (!f.exists()){
		    					System.err.println("IMPOSSIBILE TROVARE IL FILE DI PERSISTENZA");
		    					return;
		    				}
		    				
		    				
		    				//LO SPOSTO NELLA CARTELLA META-INF DELL'INSTALLAZIONE
		    				//f = new File(ConstantString.BMM_INSTALL_PATH+"/META-INF/");
		    				f = new File(path_project+"/META-INF/");
		    				if(!f.exists()){
		    					f.mkdir();
		    				}
		    				
		    				String buffer = HunkIO.readEntireFile(ci.getRootPath()+"src/META-INF/persistence.xml");
		    				//HunkIO.writeEntireFile(ConstantString.BMM_INSTALL_PATH+"/META-INF/persistence.xml", buffer);
		    				HunkIO.writeEntireFile(path_project+"/META-INF/persistence.xml", buffer);
		    				

		    			}

		    			//aggiorno i dati nella tabella
		    			TableItem ti = new TableItem(connTable, 0);
		    			Image imageBM = Activator.getImageDescriptor("icons/bm.png").createImage();
		    			
		    			if (imageBM!=null)ti.setImage(imageBM);
		    			
		    			ti.setText( new String[]{bmName,
		    						bmPath,""+bcList.size(),""+cbcList.size()});
		    			
		    			
		    			
		    			} catch (IOException e) {
		    				e.printStackTrace();
		    			}
		        }
		      }

			private void openFile(String result) {
				
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
				
				for (int k=1;k<bcBuffer.length;k++){
					String bcPath = bcBuffer[k].split("<bcPath>")[1].split("</bcPath>")[0];
					ClassInfo ci = new ClassInfo(bcPath, null);
					ci.buildClassInfo();
					bcList.add(ci);
				}
				
				for (int k=1;k<relBuffer.length;k++){
					
					String c1 = relBuffer[k].split("<c1>")[1].split("</c1>")[0];
					String c2 = relBuffer[k].split("<c2>")[1].split("</c2>")[0];
					
					
					/* PUO' SERVIRE IN FUTURO
					 * 
					 * 
					ClassInfo ci1 = null,ci2 = null;
					
					for (int i = 0; i < bcList.size(); i++){
						if (bcList.get(i).getClassPathInfo().equals(c1)){
							ci1 = bcList.get(i); 
							break;
						}
					}
					
					for (int i = 0; i < bcList.size(); i++){
						if (bcList.get(i).getClassPathInfo().equals(c2)){
							ci2 = bcList.get(i); 
							break;
						}
					}
					*/
					
					String type = relBuffer[k].split("<type>")[1].split("</type>")[0];
					
					relList.add(new String[]{c1,c2,type});
					
				}
				if(result.contains("<cbcList>\n<cbc>")){
					for (int k=1;k<cbcBuffer.length;k++){
						String cbcPath = cbcBuffer[k].split("<cbcPath>")[1].split("</cbcPath>")[0];
						ComplexClassInfo cci = new ComplexClassInfo(cbcPath, null, null, null);
						cci.buildClass(cbcPath);
						cbcList.add(cci);
					}
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
	});
	
	//associo il Listener
	buttonCreate.addListener(SWT.Selection, listenerCreate);
	//buttonCreate.setLayoutData(data);
	//connTable.setLayoutData(data);
	
	
	}
	
		
		
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	
	private void createConnMenu(Menu menu, final TableItem item) {
		/*
		 * EXTRACT: permette di estrarre oggetti da una conn
		 * 
		 * genera automaticamente un file persistence
		 */
		
		MenuItem extract = new MenuItem (menu, SWT.PUSH);
		extract.setText ("Extract BM");
		Image image = Activator.getImageDescriptor("icons/bmExtract.png").createImage();
		if (image!=null) extract.setImage(image);
		
		extract.addListener(SWT.Selection, new Listener() {
		
			@Override
			public void handleEvent(Event event) {
			//genero il file persistence.xml
				String ttText = item.getText();
				
				
			//recupero la view el
				BMQResourceView bmResource = (BMQResourceView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView("bi.bmm.views.bmq.bmqresource");
			
			
			//inserisco i dati nella view el
				
				//chiedo conferma 
				if (MessageDialog.openConfirm(new Shell(), "Extract BM","Would you extract entities from this BM?")){
					bmResource.addResource(bcList,cbcList,ttText);
				}
			
			}
		});
		
		
		/*
		 * DELETE: permette di eliminare una connessione
		 * */
		/*
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
			}
		});
		/*
		 * SPACE
		 * */
		/*
		 new MenuItem(menu, SWT.SEPARATOR);
		
		/*
		 * ABOUT: permette di ottenere informazioni sulla connessione
		 * */
		/*
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
		*/
		
	}
	
}
