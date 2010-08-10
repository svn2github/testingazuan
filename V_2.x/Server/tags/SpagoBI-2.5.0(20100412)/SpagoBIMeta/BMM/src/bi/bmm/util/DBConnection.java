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
package bi.bmm.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class DBConnection {
	
	private String driver;
	private String server;
	private String database;
	private String user;
	private String password;
	private String port;
	private String name;
	private Connection db;
	private String path;
	private HashMap<String,String[]> fkeys;


	public DBConnection(
					  String name,
			 		  String driver,
					  String server,
					  String database,
					  String user,
					  String password,
					  String port) {
		this.name = name;
		this.driver = driver;
		this.server = server;
		this.database = database;
		this.user = user;
		this.password = password;
		this.port = port;
		this.fkeys = new HashMap<String,String[]>();
		
	}
	
	public boolean Start(){
		if (this.driver.equals("MySql 5.1".toString())
			|| this.driver.equals("MySql 5.0".toString())){
			try
	        {
	            try 
	            {
	                //carico i driver
	           	 Class.forName("com.mysql.jdbc.Driver");
	                //accedo al db
	           	 this.db = DriverManager.getConnection("jdbc:mysql://"+
	           			 this.server + ":"+
	           			 this.port+"/" +
	           			 this.database +
	           			 "?user="+ this.user +
	           			 "&password=" + this.password);
	            } catch (SQLException e)
	            {
	            	MessageDialog.openWarning(new Shell(), "Error when create "+this.name, e.getMessage());
		            return false;
	            }
	        } catch (ClassNotFoundException e)
	        {
	       	 System.err.println("Errore driver DB!");
		             e.printStackTrace();
		             return false;
	        }
		}
		return true;
	}
	
	public void Stop(){
		try {
			this.db.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Vector<String[]> getTables()
	{
		//Preparo la query
		String query = "SHOW TABLES";
			
		Vector<String[]> vect_risultato =  null;
		
		try {
			vect_risultato = eseguiSelect(query);
			
			/*
			for (int i=0;i<vect_risultato.size();i++)
			        {
			            String[] record = (String[]) vect_risultato.elementAt(i);
			            srRow += record[0]+"\n";
			        }*/
			} catch (SQLException e) 
			{
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (ClassNotFoundException e) 
			{
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			
		return vect_risultato;
				
	           
	}
	
	private Vector<String[]> eseguiSelect(String query) throws SQLException, ClassNotFoundException
	{
		String [] record;
		int colonne = 0;
		Vector<String[]> v;
		Statement stmt = db.createStatement();
		ResultSet rs=stmt.executeQuery(query);
		  
		v = new Vector<String[]>();
		ResultSetMetaData rsmd = rs.getMetaData();
		colonne = rsmd.getColumnCount();

		while(rs.next()) {
				record = new String[colonne];
				for (int i=0; i<colonne; i++)
					record[i] = rs.getString(i+1);
					v.add( (String[]) record.clone() );
		}
		
		rs.close();
		stmt.close();
		return v;
	}
	
	
	public String getName(){
		return this.name;
	}

	@Override
	public String toString(){
		return name+": "+
				user+"@"+server+":"+port+
				" / "+database;
	}

	public String getDBName() {
		return this.database;
	}

	public List<ColumnInfo> getColumns(String name) {

		ResultSet rsColumns = null;
	    DatabaseMetaData meta;
		List<ColumnInfo> result = new ArrayList<ColumnInfo>();

	    
	    
	    try {
			meta = db.getMetaData();
			//ottengo le PK
			Set<String> keys = new HashSet<String>();
			ResultSet rs = meta.getPrimaryKeys(null,null,name); 
			while (rs.next()) { 
				 	     keys.add(rs.getString("COLUMN_NAME"));
				    }
			ResultSet rsf = meta.getImportedKeys(null,null,name); 

			
			while (rsf.next()) { 
				
						fkeys.put(
								rsf.getString("PKTABLE_NAME"),
								new String[]{
								rsf.getString("PKCOLUMN_NAME"),
								rsf.getString("FKTABLE_NAME"),
								rsf.getString("FKCOLUMN_NAME")
						});
				    }
			
			//ottengo i dettagli della colonna
			rsColumns = meta.getColumns(null, null, name, null);
		    while (rsColumns.next()) {
		        String columnName = rsColumns.getString("COLUMN_NAME");
		        String columnType = rsColumns.getString("TYPE_NAME");
		        int size = rsColumns.getInt("COLUMN_SIZE");
		        int nullableI = rsColumns.getInt("NULLABLE");
		        boolean nullable;
		        if (nullableI == DatabaseMetaData.columnNullable) {
		        	nullable = true;
		        } else {
		          nullable = false;
		        }
		        int position = rsColumns.getInt("ORDINAL_POSITION");
		    //controllo se è nelle chiavi e lo inserisco
		    boolean key;
		    if (keys.contains(columnName)){
		    	key=true;
		    }
		    else{
		    	key=false;
		    }
		    ColumnInfo ci = new ColumnInfo(columnName,columnType, size, nullable, position, key);
		   
		   
		    result.add(ci);
		    }
		   

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		return result;

	}
	
	public  HashMap<String, String[]> getForeignKeys(String name) {

		ResultSet rsColumns = null;
	    DatabaseMetaData meta;
	    	    
	    try {
			meta = db.getMetaData();
			//ottengo le FK
			

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		return fkeys;

	}

	public String getServer() {
		return server;
	}

	public String getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getDriver() {
		return driver;
	}

	public boolean hasTable(String objName) {
		
		 Vector<String[]> v =this.getTables();
		 int max = v.size();
		 for (int i=0; i < max; i++){
					String[] vL = v.remove(0);
					if (vL[0].equals(objName)) return true;
		 }
		 return false;
	}

	public String getConnPath() {
		return path;
	}

	public void setPath(String dbPath) {
	 this.path=dbPath;
	}
	
	public HashMap<String, String[]> getFK(){
		return fkeys;
	}

	
	

}
