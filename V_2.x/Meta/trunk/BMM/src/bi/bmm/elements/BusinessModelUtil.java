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
package bi.bmm.elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

import bi.bmm.BMUniverseView;
import bi.bmm.util.ClassInfo;
import bi.bmm.util.DBConnection;
import bi.bmm.util.HunkIO;

public class BusinessModelUtil {
	private BusinessModel bm;
	
	private String classPath;
	private String classPathInfo;

	
	public BusinessModelUtil(String name,String dir){
		bm=new BusinessModel(name, dir);
	}
	
	public boolean creaDir() {
		try {
			String d = bm.getDir()+"/"+bm.getName();
			//creo la dir <MIA DIR>/<NOME BM>/
			d += "/";
			File dir = new File(d);
			if (!dir.exists()){
			dir.mkdir();
			}
			
			}
			catch (Exception ex) {
			ex.printStackTrace();
			return false;
			} 
			return true;
	}
	
	public boolean creaDirPersistence(String name){
		try {
			String d = bm.getDir()+"/"+bm.getName()+"/"+name+"/";
			File dir = new File(d);
			if (!dir.exists()){
			dir.mkdir();
			}
			String d2 = d;
			d += "src/";
			//creo la dir <MIA DIR>/<NOME BM>/src
			dir = new File(d);
			if (!dir.exists()){
			dir.mkdir();
			}
			//creo la dir <MIA DIR>/<NOME BM>/src/META-INF/
			dir = new File(d+"META-INF/");
			if (!dir.exists()){
			dir.mkdir();
			}
			//creo la dir <MIA DIR>/<NOME BM>/src/model/
			dir = new File(d+"model/");
			if (!dir.exists()){
			dir.mkdir();
			}
			//creo la dir <MIA DIR>/<NOME BM>/bin/
			dir = new File(d2+"bin/");
			if (!dir.exists()){
			dir.mkdir();
			}
			
			}
			catch (Exception ex) {
			ex.printStackTrace();
			return false;
			} 
			return true;
	}

	public boolean creaBMInfo() {
		try{
			
			HunkIO.writeEntireFile( HunkIO.DEFAULT_INFO_FILE,
	                 bm.toString(),
	                 "UTF-8" );
	         /*
	          * TESTING
	          * */
	         //String result = HunkIO.readEntireFile( HunkIO.DEFAULT_INFO_FILE, "UTF-8" );
	         //System.out.println( result );
	     }
	     catch ( IOException e )
	     {
	         System.err.println( e );
	         return false;
	     }
	     return true;
	}
	
	/*
	 * PERSISTENCE.XML
	 * 
	 * */
	public boolean configuringPersistenceProvider(DBConnection conn){
		String path =bm.getDir()+"/"+bm.getName()+"/"+conn.getDBName()+"/src/META-INF/";
		String persistencePath =path+"persistence.xml";
		
		if(conn!=null){
			
			String dbPath = path+conn.getName()+".xml";
			conn.setPath(dbPath);
			
			File dbInfo = new File(dbPath);
			
			if(!dbInfo.exists()){
				//se non esiste
				try {
					dbInfo.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				StringBuilder sb = new StringBuilder( 1000 );
				createConnInfo(sb,conn);
				try {
					HunkIO.writeEntireFile( dbPath,
					         sb.toString(),
					         "UTF-8" );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 try {
							
				StringBuilder sb = new StringBuilder( 1000 );
				
				headPersistenceProv(sb);
				classPersistenceProv(sb);
				
				//a seconda del driver setto le impostazioni
				if (conn.getDriver().equals("MySql 5.1".toString())
						|| conn.getDriver().equals("MySql 5.0".toString())){
					mysqlPersistenceProv(conn,sb);
				}
				
				tailPersistenceProv(sb);
				
				//genero il file persistence
				HunkIO.writeEntireFile( persistencePath,
		                 sb.toString(),
		                 "UTF-8" );
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			 try {
					
					StringBuilder sb = new StringBuilder( 1000 );
					
					sb.append("");
					
					//genero il file persistence
					HunkIO.writeEntireFile( persistencePath,
			                 sb.toString(),
			                 "UTF-8" );
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}

		 return false;
	}

	private void createConnInfo(StringBuilder sb, DBConnection conn) {
		sb.append("<connection>");
		sb.append("<name>"+conn.getName()+"</name>");
		sb.append("<driver>"+conn.getDriver()+"</driver>");
		sb.append("<server>"+conn.getServer()+"</server>");
		sb.append("<database>"+conn.getDBName()+"</database>");
		sb.append("<user>"+conn.getUser()+"</user>");
		sb.append("<password>"+conn.getPassword()+"</password>");
		sb.append("<port>"+conn.getPort()+"</port>");
		sb.append("</connection>");
	}

	private void headPersistenceProv(StringBuilder sb) {
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<persistence version=\"1.0\" xmlns=\"http://java.sun.com/xml/ns/persistence\"\n");
		sb.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd\">\n");
		sb.append("<persistence-unit name=\""+bm.getName()+"\" transaction-type=\"RESOURCE_LOCAL\">\n");
		sb.append("	<provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>\n");
	}
	
	private void mysqlPersistenceProv(DBConnection conn, StringBuilder sb) {
			//<!-- db access setting -->
		sb.append("<properties>\n");
		sb.append("<property name=\"eclipselink.jdbc.password\" value=\""+conn.getPassword()+"\"/>\n");
		sb.append("<property name=\"eclipselink.jdbc.user\" value=\""+conn.getUser()+"\"/>\n");
		sb.append("<property name=\"eclipselink.jdbc.driver\" value=\"com.mysql.jdbc.Driver\"/>\n");
		sb.append("<property name=\"eclipselink.jdbc.url\" value=\"jdbc:mysql://"+conn.getServer()+":"+conn.getPort()+"/"+conn.getDBName()+"\"/>\n");
			//<!-- nessuna tabella verrà creata o cancellata -->
		sb.append("<property name=\"eclipselink.ddl-generation\" value=\"none\"/>\n");
		sb.append("<property name=\"eclipselink.logging.level\" value=\"INFO\"/>\n");
		sb.append("</properties>\n");
	}

	private void classPersistenceProv(StringBuilder sb) {
		
		//provo a leggere le classi della vista
		
			//recupero la vista
			//recupero la view
			BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
			
			if (bmUniverse.bcList.isEmpty()) return;
			
			for (int i =0; i<bmUniverse.bcList.size(); i++){
				sb.append("<class>model."+bmUniverse.bcList.get(i).getClassName());
				sb.append("</class>\n");
			}
		
		
		
	}
	
	private void tailPersistenceProv(StringBuilder sb) {
		sb.append("</persistence-unit>\n");
		sb.append("</persistence>\n");
	}
	
	/*
	 * CLASS.JAVA
	 * */
	public void createClassJava(String pathInfo,DBConnection conn) {
		
		ClassInfo ci = new ClassInfo(pathInfo,conn);
		ci.buildClassInfo();
		ci.printClassInfo();
		createClass(ci.getConnection(), ci.getKeys(), ci.getAttributes(),ci.getMappings(), ci.getRelationships(),ci.getClassName(),ci.getClassTable());
		
	}
	
	
	public void createClass(DBConnection conn,ArrayList<String[]> keys, ArrayList<String[]> attributes,
			ArrayList<String[]> mappings,	ArrayList<String[]> relationships, String name, String classTable) {
		try {
				classPath =bm.getDir()+"/"+bm.getName()+"/"+conn.getDBName()+"/src/model/"+name+".java";
				
				
				StringBuilder sb = new StringBuilder( 1000 );
				
				headClass(sb,name,classTable);
				keyClass(conn,keys,sb,name,mappings,relationships);
				attrClass(attributes,sb,mappings);
				relClass(sb,relationships);
				tailClass(sb);
				
				
				//genero il file java
				HunkIO.writeEntireFile( classPath,
		                 sb.toString(),
		                 "UTF-8" );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	
	/*
	 * CLASSINFO.XML
	 */
	public void createClassInfo(DBConnection conn, Table keys, Table attributes,
			String name,ArrayList<String[]>mappings,ArrayList<String[]>relationships,String classTable) {
		
		classPathInfo =bm.getDir()+"/"+bm.getName()+"/"+conn.getDBName()+"/src/model/"+name+"Info.xml";
		StringBuilder sbInfo = new StringBuilder( 1000 );
		
		
		headClassInfo(sbInfo,name,conn,classTable);
		keyClassInfo(conn,keys,sbInfo,name,mappings);
		attrClassInfo(attributes,sbInfo,mappings);
		if(!relationships.isEmpty()) relationshipClassInfo(relationships,sbInfo);
		tailClassInfo(sbInfo);
		//genero il file info.xml
		try {
			HunkIO.writeEntireFile( classPathInfo,
			         sbInfo.toString(),
			         "UTF-8" );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createClassInfoByCI(ClassInfo ci) {
		DBConnection conn = ci.rigenerateConnection();
		
		String name = ci.getClassName();
		//setto le var locali
		Table keys = new Table(new Shell(), SWT.NULL);
		for (int index = 0; index < ci.getKeys().size(); index++){
			TableItem ti = new TableItem(keys, 0);
			ti.setText(ci.getKeys().get(index)[0]+" - "+ci.getKeys().get(index)[1]);
		}
			
		Table attributes = new Table(new Shell(), SWT.NULL);
		for (int index = 0; index < ci.getAttributes().size(); index++){
			TableItem ti = new TableItem(attributes, 0);
			ti.setText(ci.getAttributes().get(index)[0]+" - "+ci.getAttributes().get(index)[1]);
		}
		
		classPathInfo =ci.getClassPathInfo();
		String classTable = ci.getClassTable();
		
		StringBuilder sbInfo = new StringBuilder( 1000 );	
		
		headClassInfo(sbInfo,name,conn,classTable);
		keyClassInfo(conn,keys,sbInfo,name,ci.getMappings());
		attrClassInfo(attributes,sbInfo,ci.getMappings());
		
		relationshipClassInfo(ci.getRelationships(),sbInfo);
		tailClassInfo(sbInfo);
		//genero il file info.xml
		try {
			HunkIO.writeEntireFile( classPathInfo,
			         sbInfo.toString(),
			         "UTF-8" );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void createEmbeddedClass(DBConnection conn, Table keys, String nameClass) {
		try {
				String classPath =bm.getDir()+"/"+bm.getName()+"/"+conn.getDBName()+"/src/model/"+nameClass+".java";
				
				ArrayList<String> vars = new ArrayList<String>();
				StringBuilder sb = new StringBuilder( 1000 );
				
				//HEADER
				//import zone
				sb.append("package model;" + "\n");
				sb.append(""+"\n");
				sb.append("import java.io.Serializable;"+"\n");
				sb.append("import java.util.*;"+"\n");
				sb.append("import javax.persistence.*;"+"\n");
				sb.append("import java.sql.*;"+"\n");
				sb.append(""+"\n");
				sb.append("@SuppressWarnings(\"unused\")"+"\n");
				sb.append("@Embeddable"+"\n");
				sb.append("public class "+nameClass+" implements Serializable {"+"\n");
				//costructor
				sb.append("private static final long serialVersionUID = 1L;"+"\n");
				sb.append("public "+nameClass+"(){"+"\n");
				sb.append("super();"+"\n");
				sb.append("}"+"\n");
				//link at column
				for(int z=0;z<keys.getItems().length;z++){
					//recupero il nome (che poi metto con 2 al fondo)
					String attrName = keys.getItem(z).getText().split(" - ")[0];
					sb.append("@Column(name=\""+attrName+"\", insertable=false, updatable=false)"+"\n");
					
					String parName=attrName;
					/*OBSOLETE
					String[] parNameV = attrName.split("_");
					parName =parNameV[0];
					for(int i=1;i<parNameV.length;i++){
						parName = parName + parNameV[i].substring(0,1).toUpperCase() + parNameV[i].substring(1);
					}
					*/
					parName += 2;
					vars.add(parName);
					String type = type2Java(keys.getItem(z).getText().split(" - ")[1]);
					//creo la variabile
					doPrivateVar(sb, type, parName);
					//recupero il tipo
					doGetterAndSetter(sb, type, parName);
				}
				//Override
				doEqualsOverride(sb,nameClass,vars);
				doHashCodeOverride(sb,nameClass,vars);
				tailClass(sb);
				
				
				//genero il file persistence
				HunkIO.writeEntireFile( classPath,
		                 sb.toString(),
		                 "UTF-8" );
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	private void relClass(StringBuilder sb, ArrayList<String[]> relationships) {
		if (relationships.size()==0){
			return;
			}
		for(int z=0;z<relationships.size();z++){

			
				String mapName= relationships.get(z)[0];
				String attrName = relationships.get(z)[2];
				String className = relationships.get(z)[1];
				String classNameLittle =className.substring(0,1).toLowerCase() + className.substring(1);
				String typeRel = relationships.get(z)[3];

				
				
				if(typeRel.contains("ONE_TO_MANY")){
					
					sb.append("@OneToMany (mappedBy=\""+mapName+"\")"+"\n");
					sb.append("java.util.Set<"+className+"> "+classNameLittle+"Collection;"+"\n");
					sb.append("\n");
					//getter&setter
					sb.append("public java.util.Set<"+className+"> get"+className+"Collection() {"+"\n");
					sb.append("return this."+classNameLittle+"Collection;"+"\n");
					sb.append("}"+"\n");
					sb.append("\n");
					sb.append("public void set"+className+"Collection(java.util.Set<"+className+"> "+classNameLittle+"Collection) {"+"\n");
					sb.append("this."+classNameLittle+"Collection = "+classNameLittle+"Collection;"+"\n");
					sb.append("}\n");
					sb.append("\n");
					
					}
				if(typeRel.contains("MANY_TO_ONE")){
					sb.append("@ManyToOne"+"\n");
					sb.append("@JoinColumn(name=\""+attrName+"\")"+"\n");
					sb.append(""+className+" "+mapName+";"+"\n");
					
					sb.append("\n");
					//getter&setter
					
					sb.append("public "+className+" get"+className+"() {"+"\n");
					sb.append("return this."+mapName+";"+"\n");
					sb.append("}"+"\n");
					sb.append("\n");
					sb.append("public void set"+className+" ("+className+" "+classNameLittle+") {"+"\n");
					sb.append("this."+mapName+" = "+classNameLittle+";"+"\n");
					sb.append("}\n");
					sb.append("\n");
					
					}
					
				}
	}

	private void attrClass(ArrayList<String[]> attributes, StringBuilder sb, ArrayList<String[]> mappings) {
		if (attributes.size()==0){
			return;
			}
		
		for(int z=0;z<attributes.size();z++){
				String mapName= "";
				String attrName = attributes.get(z)[0];
				
				for (int i =0; i< mappings.size(); i++){
					if (mappings.get(i)[0].equals(attrName)){
						mapName = mappings.get(i)[1];
						break;
					}
				}
				
				sb.append("@Column(name=\""+mapName+"\")"+"\n");
				
				String parName = attrName;
				/*OBSOLETE
				String[] parNameV = attrName.split("_");
				parName =parNameV[0];
				for(int i=1;i<parNameV.length;i++){
					parName = parName + parNameV[i].substring(0,1).toUpperCase() + parNameV[i].substring(1);
				}
				*/
				String type =attributes.get(z)[1];
				//creo la variabile privata
				doPrivateVar(sb,type,parName);
				//getter&setter
				doGetterAndSetter(sb, type, parName);
		}
	
	}

	private void tailClass(StringBuilder sb) {
		sb.append("}\n");
	}

	private void headClass(StringBuilder sb, String nameClass, String classTable) {
		//import zone
		sb.append("package model;" + "\n");
		sb.append(""+"\n");
		sb.append("import java.io.Serializable;"+"\n");
		sb.append("import java.util.*;"+"\n");
		sb.append("import javax.persistence.*;"+"\n");
		sb.append("import java.sql.*;"+"\n");
		sb.append(""+"\n");
		sb.append("@SuppressWarnings(\"unused\")"+"\n");
		sb.append("@Table (name=\""+classTable+"\")"+"\n");
		sb.append("@Entity"+"\n");
		sb.append("public class "+nameClass+" implements Serializable {"+"\n");
		//costructor
		sb.append("private static final long serialVersionUID = 1L;"+"\n");
		sb.append("public "+nameClass+"(){"+"\n");
		sb.append("super();"+"\n");
		sb.append("}"+"\n");
		
		
	}
	
	private void keyClass(DBConnection conn, ArrayList<String[]> keys, StringBuilder sb, String nameClass, ArrayList<String[]> mappings,ArrayList<String[]>relationships) {
		//TODO: CHECK IF VALID MODIFIED
		if (keys.isEmpty()){
			//aggiungere una PK embedded
			
			//creo un nome di classe (aggiungendo PK al fondo del class name)
			nameClass +="PK";
			sb.append("@EmbeddedId"+"\n");
			
			doPrivateVar(sb, nameClass, "pk");
			doGetterAndSetter(sb,nameClass,"pk");
			//creare la classe embedded PK a cui passo i parametri che poi sarebbero
			//i nomi delle mapping per le relations manytoone
			Table tb = new Table(new Shell(), SWT.NULL);
			for (int i=0; i<relationships.size();i++){
				String[] ris=relationships.get(i);
				if (/*TYPE*/ris[3].equals("MANY_TO_ONE")){
					TableItem ti =new TableItem(tb, 0);
					ti.setText(ris[2]+" - "+"INT");
				}
			}
			createEmbeddedClass(conn, tb, nameClass);
		}
		//TODO: REMOVE ENDER 
		
		for(int z = 0; z<keys.size(); z++){
			String mapName = "";
			String attrName = keys.get(z)[0];
			
			for (int i =0; i< mappings.size(); i++){
				if (mappings.get(i)[0].equals(attrName)){
					mapName = mappings.get(i)[1];
					break;
				}
			}
			
			sb.append("@Id"+"\n");
			sb.append("@Column(name=\""+mapName+"\")"+"\n");
			
			String parName = attrName;
			/*OBSOLETE
			String[] parNameV = attrName.split("_");
			parName =parNameV[0];
			for(int i=1;i<parNameV.length;i++){
				parName = parName + parNameV[i].substring(0,1).toUpperCase() + parNameV[i].substring(1);
			}
			*/
			String type = keys.get(z)[1];
			
			//creo la variabile privata
			doPrivateVar(sb,type,parName);
			
			//creo getter and Setter
			doGetterAndSetter(sb, type, parName);
			
		}
	}


	/*
	 * INFO.XML
	 * compositionClass
	 * */
	private void attrClassInfo( Table attributes, StringBuilder sbInfo, ArrayList<String[]>classMappings) {
		if (attributes.getItemCount()==0){
			return;
			}
		
		for(int z=0;z<attributes.getItems().length;z++){
				
				String attrName = attributes.getItem(z).getText().split(" - ")[0];
				String mapName=attrName;
				
				for(int i=0;i<classMappings.size();i++){
					if (classMappings.get(i)[0].equals(attrName)){
						mapName = classMappings.get(i)[1];
						break;
					}
				}
				String parName = attrName;
				/*OBSOLETE
				String[] parNameV = attrName.split("_");
				parName =parNameV[0];
				for(int i=1;i<parNameV.length;i++){
					parName = parName + parNameV[i].substring(0,1).toUpperCase() + parNameV[i].substring(1);
				}
				*/
				String typeSQL="";
				
				for(int i=0;i<classMappings.size();i++){
					if (classMappings.get(i)[0].equals(attrName)){
						typeSQL = classMappings.get(i)[2];
							break;
					}
				}
				
				String type;
				if (typeSQL.equals("")){
					typeSQL =attributes.getItem(z).getText().split(" - ")[1];
					type =type2Java(typeSQL);
					}
				else type = typeSQL;
				/*
				 * META-INF
				 * */
				sbInfo.append("<attribute>\n");
				//creo la variabile privata
				doPrivateVarInfo(sbInfo,type,parName,mapName);
				/*
				 * META-INF
				 * */
				sbInfo.append("</attribute>\n");
		}
	
	}

	private void tailClassInfo(StringBuilder sbInfo) {
		sbInfo.append("</classInfo>");
	}

	private void headClassInfo(StringBuilder sb2, String nameClass, DBConnection conn, String classTable) {
		/*
		 * META-INF
		 * 
		 * **/
		sb2.append("<classInfo>"+"\n");
		sb2.append("<connection>"+conn.getConnPath()+"</connection>"+"\n");
		sb2.append("<classTable>"+classTable+"</classTable>"+"\n");
		sb2.append("<className>"+nameClass+"</className>"+"\n");
		
	}
	
	private void keyClassInfo( DBConnection conn, Table keys, StringBuilder sbInfo, String nameClass, ArrayList<String[]> classMappings) {
		
		
		for(int z = 0; z<keys.getItemCount(); z++){
			
			String attrName = keys.getItem(z).getText().split(" - ")[0];
			String mapName=attrName;
			for(int i=0;i<classMappings.size();i++){
				if (classMappings.get(i)[0].equals(attrName)){
					mapName = classMappings.get(i)[1];
					break;
				}
			}
			
			String parName=attrName;
			/*OBSOLETE
			String[] parNameV = attrName.split("_");
			parName =parNameV[0];
			for(int i=1;i<parNameV.length;i++){
				parName = parName + parNameV[i].substring(0,1).toUpperCase() + parNameV[i].substring(1);
			}
			*/
			String typeSQL="";
			
			for(int i=0;i<classMappings.size();i++){
				if (classMappings.get(i)[0].equals(attrName)){
					typeSQL = classMappings.get(i)[2];
						break;
				}
			}
			
			String type;
			if (typeSQL.equals("")){
				typeSQL =keys.getItem(z).getText().split(" - ")[1];
				type =type2Java(typeSQL);
				}
			else type = typeSQL;
			/*
			 * META-INF
			 * */
			sbInfo.append("<key>\n");
			//creo la variabile privata
			doPrivateVarInfo(sbInfo,type,parName,mapName);
			/*
			 * META-INF
			 * */
			sbInfo.append("</key>\n");
			
			
		}
	}
	
	private void relationshipClassInfo(ArrayList<String[]> relationships, StringBuilder sbInfo) {
		if (relationships.size()==0){
			return;
			}
		sbInfo.append("<relationships>"+"\n");
		for(int z=0;z<relationships.size();z++){
			sbInfo.append("<relation>"+"\n");
				sbInfo.append("<type>");
					sbInfo.append(relationships.get(z)[3]);
				sbInfo.append("</type>"+"\n");
				sbInfo.append("<relField>");
					sbInfo.append(relationships.get(z)[2]);
				sbInfo.append("</relField>"+"\n");
				sbInfo.append("<relClass>");
					sbInfo.append(relationships.get(z)[1]);
				sbInfo.append("</relClass>"+"\n");
				sbInfo.append("<relName>");
					sbInfo.append(relationships.get(z)[0]);
				sbInfo.append("</relName>"+"\n");
			sbInfo.append("</relation>"+"\n");
			   
		}
		sbInfo.append("</relationships>"+"\n");
	}
	/**
	 * 
	 * UTILITIES
	 * @param sb 
	 * 
	 * */
	
	private void doEqualsOverride(StringBuilder sb, String nameClass, ArrayList<String> vars) {
		sb.append("@Override"+"\n");
		sb.append("public boolean equals(Object o) {"+"\n");
		sb.append("if (o == this) {"+"\n");
		sb.append("return true;"+"\n");
		sb.append("}"+"\n");
		sb.append("if ( ! (o instanceof "+nameClass+")) {"+"\n");
		sb.append("return false;"+"\n");
		sb.append("}"+"\n");
		sb.append(nameClass+" other = ("+nameClass+") o;"+"\n");
		sb.append("return (this."+vars.get(0)+" == other."+vars.get(0)+")"+"\n");
		for(int i=1;i<vars.size();i++){
			sb.append("&& (this."+vars.get(i)+" == other."+vars.get(i)+")"+"\n");
		}
		sb.append(";"+"\n"+"}"+"\n");
		
	}
	
	private void doHashCodeOverride(StringBuilder sb, String nameClass,
			ArrayList<String> vars) {
		sb.append("@Override"+"\n");
		sb.append("public int hashCode() {"+"\n");
		sb.append("final int prime = 31;"+"\n");
		sb.append("int hash = 17;"+"\n");
		for(int i=0;i<vars.size();i++){
			sb.append("hash = hash * prime + this."+vars.get(i)+";"+"\n");
		}
		sb.append("return hash;"+"\n");
		sb.append("}"+"\n");

	}
	
	private void doPrivateVar(StringBuilder sb, String type, String parName) {
		sb.append(""+type+" "+parName+";"+"\n");
	}
	
	private void doPrivateVarInfo(StringBuilder sbInfo, String type, String parName,String mapName) {
		/*
		 * META-INF
		 * */
		sbInfo.append("<name>"+parName+"</name>"+"\n");
		sbInfo.append("<type>"+type+"</type>"+"\n");
		sbInfo.append("<mapping>"+mapName+"</mapping>"+"\n");
		
	}
	
	private void doGetterAndSetter(StringBuilder sb, String type,
			String parName) {
		
		String parNameU = parName.substring(0,1).toUpperCase() + parName.substring(1);
		
		//getter&setter
		sb.append("public "+type+" get"+parNameU+"(){"+"\n");
		sb.append("return this."+parName+";"+"\n");
		sb.append("}"+"\n");
		sb.append("\n");
		sb.append("public void set"+parNameU+"("+type+" "+parName+"){"+"\n");
		sb.append("this."+parName+"= "+parName+";"+"\n");
		sb.append("}"+"\n");
		
	}

	private String type2Java(String typeSQL) {
		
		String type ="";

		if(typeSQL.equals("CHAR")) type=" java.lang.String ";
		if(typeSQL.equals("VARCHAR")) type=" java.lang.String ";
		if(typeSQL.equals("LONGVARCHAR")) type=" java.lang.String ";
		if(typeSQL.equals("NUMERIC")) type=" java.math.BigDecimal ";
		if(typeSQL.equals("DECIMAL")) type=" java.math.BigDecimal ";
		if(typeSQL.equals("BIT")) type="java.lang.Boolean ";
		if(typeSQL.equals("TINYINT")) type="java.lang.Integer ";
		if(typeSQL.equals("INTEGER")) type="java.lang.Integer ";
		if(typeSQL.equals("INT")) type="java.lang.Integer ";
		if(typeSQL.equals("SMALLINT")) type="java.lang.Integer ";
		if(typeSQL.equals("BIGINT")) type="java.lang.Long ";
		if(typeSQL.equals("REAL")) type="java.lang.Float ";
		if(typeSQL.equals("FLOAT")) type="java.lang.Double ";
		if(typeSQL.equals("DOUBLE")) type="java.lang.Double ";
		if(typeSQL.equals("BINARY")) type="byte[] ";
		if(typeSQL.equals("VARBINARY")) type="byte[] ";
		if(typeSQL.equals("LONGVARBINARY")) type="byte[] ";
		if(typeSQL.equals("DATE")) type="java.sql.Date ";
		if(typeSQL.equals("TIME")) type="java.sql.Time ";
		if(typeSQL.equals("TIMESTAMP")) type="java.sql.Timestamp ";
		//MC: mapping aggiunto, una parte del datetime viene perso
		if(typeSQL.equals("DATETIME")) type="java.sql.Date ";
		//DISTINCT	Object type of underlying type
		if(typeSQL.equals("CLOB")) type="java.lang.Clob ";
		if(typeSQL.equals("BLOB")) type="java.lang.Blob ";
		if(typeSQL.equals("ARRAY")) type="java.lang.Array ";
		if(typeSQL.equals("STRUCT")) type="java.lang.SQLData ";
		if(typeSQL.equals("REF")) type="java.lang.Ref ";
		
		//TODO: fare un combo di selezione nel caso il tipo nn sia tra quelli specificati.
		return type;
		
		
	}

	public String getClassPath() {
		return this.classPath;
	}

	public String getClassPathInfo() {
		return this.classPathInfo;
	}

	

}
