package eng.it.spagobimeta.views;

import java.io.IOException;
import java.sql.SQLException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IManagedConnection;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.datatools.connectivity.sqm.core.connection.ConnectionInfo;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.datatools.modelbase.sql.tables.*; 
import org.eclipse.emf.ecore.xmi.*;
import org.eclipse.emf.ecore.xmi.impl.*;

public class GraphicEditorView extends ViewPart {
	
	Text text;
	public GraphicEditorView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());
	    GridData gd = new GridData(100,20);
		final Button myButton = new Button(parent, SWT.PUSH);
		
		myButton.setLayoutData(gd);
		SelectionAdapter adapter = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				//myButton.setText("You clicked me!");
				
				//prova collegamento con DSE
				try{
					printConnProfs();
					IConnectionProfile myCp = connectCP("MySql1");
					queryCP(myCp, "select * from sogei.bi_admin");
					connectDBObject(myCp);
				}
				catch (SQLException e)
				{
					System.out.println("Errore SQL");
					e.printStackTrace();
					text.append("\n Errore SQL");
					text.update();
				}
				catch (IOException e)
				{
					System.out.println("Errore di IO");
					e.printStackTrace();
					text.append("\n Errore IO");
					text.update();
				}
			}
		};
		myButton.addSelectionListener(adapter);
		myButton.setText("Click");
		
		Composite child = new Composite(parent, SWT.NONE);
	    child.setLayout(new GridLayout());
	    GridData gd2 = new GridData(600,400);

		text = new Text(child, SWT.BORDER | SWT.V_SCROLL);
		text.setText("");
		text.setLayoutData(gd2);
	    child.setSize(600, 400);

	    
		
		
	}

	@Override
	public void setFocus() {
	}

	/*
	 * Print Connection Profiles in current DSE view
	 */
	public void printConnProfs(){
		IConnectionProfile[] cps = ProfileManager.getInstance().getProfiles();
		System.out.println("Connection Profiles found:");
		text.append("\n Connection Profiles found: \n");
		text.update();
		for(int i=0; i<cps.length;i++){
			System.out.println("Profile "+i+": "+cps[i].getName());
			text.append("\n Profile "+i+": "+cps[i].getName());
			text.update();
			
		}
	}
	
	/*
	 * Connect to an existing Connection Profile
	 */
	public IConnectionProfile connectCP(String cpName){
		IConnectionProfile myProfile =	ProfileManager.getInstance().getProfileByName(cpName);
		System.out.println("Connecting...");
		text.append("\n Connecting... \n");
		text.update();
		//MessageDialog.openInformation(window.getShell(),"PluginDTPTest","Connecting to "+myProfile.getName());
		IStatus status = myProfile.connect();
		if (status.getCode()== IStatus.OK) {
			// success
			System.out.println("Connection OK");
			text.append("\n Connection OK");
			text.append("\n ----------------");
			text.update();
			//MessageDialog.openInformation(window.getShell(),"PluginDTPTest","Connection OK");
			return myProfile;
		} else {
			// failure 
			System.out.println("Connection failure: "+status.getCode());
			System.out.println(status.getMessage());
			text.append("\n Connection failure: "+status.getCode());
			text.update();
			if (status.getException() != null) {
				status.getException().printStackTrace();
			}
			return null;
		}
		
	}
	
	/*
	 * Execute a JDBC SQL Query from a Connection Profile
	 */
	public void queryCP(IConnectionProfile profile, String queryString) throws SQLException{
	     IManagedConnection managedConnection = ((IConnectionProfile)profile).getManagedConnection ("java.sql.Connection");
	     java.sql.Connection myCon = null;
	     if (managedConnection != null) {
	    	System.out.println("managedConnection ok"); 
			text.append("\n managedConnection ok");
			text.update();
	    	//get raw JDBC Connection
	        myCon = (java.sql.Connection) managedConnection.getConnection().getRawConnection();
	        System.out.println("myCon = "+myCon);
			text.append("\n myCon = "+myCon);
			text.update();
	        if (myCon != null) {
	        		//Work like classic JDBC Connection
	               java.sql.Statement stmt = myCon.createStatement();
	               java.sql.ResultSet results = stmt.executeQuery(queryString);
	               System.out.println("Query results:");
	   			   text.append("\n Query results: \n");
	   			   text.update();
	               while (results.next()){
	            	   System.out.println("Row: "+results.getString(1)+" "+results.getString(2));
		   			   text.append("\n Row: "+results.getString(1)+" "+results.getString(2));
		   			   text.update();
	               }
	        }
	     }
	}
	
	/*
	 * Connect and inspect the Database Object (it's a EMF Model) from a Connection Profile
	 */
	public void connectDBObject(IConnectionProfile profile) throws IOException{
		IManagedConnection managedConnection = ((IConnectionProfile)profile).getManagedConnection(
		"org.eclipse.datatools.connectivity.sqm.core.connection.ConnectionInfo");
		if (managedConnection != null) {
			
					ConnectionInfo connectionInfo = (ConnectionInfo) managedConnection.getConnection().getRawConnection();
					if (connectionInfo != null) {
						Database database = connectionInfo.getSharedDatabase();
						// do something with the database reference…
						System.out.println("DB Vendor: "+database.getVendor());
						System.out.println("DB Version: "+database.getVersion());
						text.append("\n DB Vendor: "+database.getVendor());
						text.append("\n DB Version: "+database.getVersion());
						text.update();
						//Obtaining the database schemas
						EList<Schema> schemas = database.getSchemas();
						//Print schemas found
						System.out.println("Schemas found: "); 
						text.append("\n Schemas found: ");
						text.update();
						for(Schema sch:schemas){
							System.out.println(sch.getName()); 
							text.append("\n "+sch.getName());
							text.update();
						}
						
						//get first schema found
						Schema aSchema = schemas.get(0);
						//try to save EMF on XML
						EObject model =  aSchema.eClass();
						System.out.println("model =	"+model);
						text.append("\n model =	"+model);
						text.update();
						URI fileURI = URI.createFileURI("c:/temp/test_emf.xmi");
						Resource poResource = new XMLResourceFactoryImpl().createResource(fileURI);
						poResource.getContents().add(model);
						poResource.save(null);
						System.out.println("EMF Model saved on XML File");
						System.out.println("Schema name: "+aSchema.getName());
						text.append("\n EMF Model saved on XML File");
						text.append("\n Schema name: "+aSchema.getName());						
						text.update();
						//Print contents
						printTables(aSchema);
						printColumns((Table)aSchema.getTables().get(0));
					}	
		}	
	}
	/*
	 * Print tables inside a schema
	 */
	public void printTables(Schema sch){
		System.out.println("Schema name: "+sch.getName());
		text.append("\n Schema name: "+sch.getName());						
		text.update();
		EList<Table> tables = sch.getTables();
		
		System.out.println("Tables found inside schema: ");
		text.append("\n Tables found inside schema: ");						
		text.update();
		//Retrieve table inside schema
		for(Table tab:tables)
		{
			System.out.println("Table: "+tab.getName());
			text.append("\n Table: "+tab.getName());
			text.update();
		}
	}
	
	/*
	 * Print columns inside a table
	 */
	public void printColumns(Table tab){
		System.out.println("Table name: "+tab.getName());
		text.append("\n Table name: "+tab.getName());
		EList<Column> columns = tab.getColumns();
		
		System.out.println("Columns found inside table: ");
		text.append("\n Columns found inside table: ");
		text.update();		
		//Retrieve table inside schema
		for(Column col:columns)
		{
			System.out.println("Column: "+col.getName());
			text.append("\n Column: "+col.getName());
			text.update();			
		}
	}


}
