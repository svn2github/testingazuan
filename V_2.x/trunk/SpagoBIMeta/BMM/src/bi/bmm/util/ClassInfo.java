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

import java.io.IOException;
import java.util.ArrayList;

import bi.bmm.figures.ActivityFigure;


public class ClassInfo {
	
	private String classPathInfo;
	private String className;
	private String classTable;
	private ArrayList<String[]> classKeys;
	private ArrayList<String[]> classAttributes;
	private ArrayList<String[]> classMappings;
	private ArrayList<String[]> classRelationships;
	
	/*connection details*/
	private String name;
	private String driver;
	private String server;
	private String database;
	private String user;
	private String password;
	private String port;
	
	/*DB CONN*/
	private DBConnection conn;
 	
	/*FIGURE nella BMUView*/
	private ActivityFigure figure;
	
	public ClassInfo(String classPath,DBConnection conn){
		this.classPathInfo = classPath;
		this.classKeys = new ArrayList<String[]>();
		this.classAttributes = new ArrayList<String[]>();
		this.classMappings = new ArrayList<String[]>();
		this.classRelationships = new ArrayList<String[]>();
		this.conn = conn;
		this.figure = null;
	}

	public void buildClassInfo() {
		
		try {
			String buffer =  HunkIO.readEntireFile(classPathInfo, "UTF-8" );
			
			String connectionInfoPath=buffer.split("<connection>")[1].split("</connection>")[0];
			if (connectionInfoPath.equals("null") && conn!=null){
				name = conn.getName();
				driver = conn.getDriver();
				server = conn.getServer();
				database = conn.getDBName();
				user = conn.getUser();
				password = conn.getPassword();
				port = conn.getPort();
			}
			else
			{
			String connectionbuffer =  HunkIO.readEntireFile(connectionInfoPath, "UTF-8" );
			
			name = connectionbuffer.split("<name>")[1].split("</name>")[0];
			driver = connectionbuffer.split("<driver>")[1].split("</driver>")[0];
			server = connectionbuffer.split("<server>")[1].split("</server>")[0];
			database = connectionbuffer.split("<database>")[1].split("</database>")[0];
			user = connectionbuffer.split("<user>")[1].split("</user>")[0];
			password = connectionbuffer.split("<password>")[1].split("</password>")[0];
			port = connectionbuffer.split("<port>")[1].split("</port>")[0];
			}
			
			classTable = buffer.split("<classTable>")[1].split("</classTable>")[0];
			
			className = buffer.split("<className>")[1].split("</className>")[0];
			String[] keysBuffer = buffer.split("<key>");
			for (int k=1;k<keysBuffer.length;k++){
				String attr = keysBuffer[k].split("<name>")[1].split("</name>")[0];
				String attrType = keysBuffer[k].split("<type>")[1].split("</type>")[0];
				String attrMap = keysBuffer[k].split("<mapping>")[1].split("</mapping>")[0];
				classKeys.add(new String[]{attr,attrType});
				classMappings.add(new String[]{attr,attrMap,attrType});
				
			}
			String[] attrBuffer = buffer.split("<attribute>");
			for (int k=1;k<attrBuffer.length;k++){
				String attr = attrBuffer[k].split("<name>")[1].split("</name>")[0];
				String attrType = attrBuffer[k].split("<type>")[1].split("</type>")[0];
				String attrMap = attrBuffer[k].split("<mapping>")[1].split("</mapping>")[0];
				classAttributes.add(new String[]{attr,attrType});
				classMappings.add(new String[]{attr,attrMap,attrType});
				
			}
			
			if(buffer.contains("<relationships>")){
			String relsBuffer = buffer.split("<relationships>")[1].split("</relationships>")[0];
				
				if(relsBuffer.contains("<relation>")){
					String[] relBuffer = relsBuffer.split("<relation>");
					for (int k=1;k<relBuffer.length;k++){
						String relType = relBuffer[k].split("<type>")[1].split("</type>")[0];
						String relField = relBuffer[k].split("<relField>")[1].split("</relField>")[0];
						String relClass = relBuffer[k].split("<relClass>")[1].split("</relClass>")[0];
						String relName = relBuffer[k].split("<relName>")[1].split("</relName>")[0];
						if (!classRelationships.contains(new String[]{relName,relClass,relField,relType}))
						classRelationships.add(new String[]{relName,relClass,relField,relType});
						
					}
				}
			}
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void printClassInfo() {
			System.out.println(className);
			
			System.out.println("Keys:");
			
			for (int i=0; i < classKeys.size();i++){
				System.out.println(classKeys.get(i)[0]+" - "+classKeys.get(i)[1]);
			}
			
			System.out.println("Attributes:");
			
			for (int i=0; i < classAttributes.size();i++){
				System.out.println(classAttributes.get(i)[0]+" - "+classAttributes.get(i)[1]);
			}
			
			System.out.println("Mappings:");
			
			for (int i=0; i <  classMappings.size();i++){
				System.out.println(classMappings.get(i)[0]+" -> "+classMappings.get(i)[1] +"("+classMappings.get(i)[2]+")");
			}
			
			for (int i=0; i < classRelationships.size(); i++ ){
				System.out.println(classRelationships.get(i)[3]+" -> "
						+classRelationships.get(i)[0] + " , "
						+classRelationships.get(i)[1] + " , "
						+classRelationships.get(i)[2]);
			}
	}
	
	public String getClassName(){
		return className;
	}
	
	public String getClassPathInfo(){
		return classPathInfo;
	}
	
	public ArrayList<String[]> getKeys(){
		return classKeys;
	}
	
	public ArrayList<String[]> getAttributes(){
		return classAttributes;
	}

	public DBConnection getConnection() {
		
		return new DBConnection(name, driver, server, database, user, password, port);
	}

	public ArrayList<String[]> getMappings() {
		return classMappings;
	}
	
	public void setFigure(ActivityFigure figure){
		this.figure = figure;
	}
	
	public ActivityFigure getFigure(){
		return this.figure;
	}

	public boolean addOneToManyRelation(String mappedBy, String className) {
		
		if(!classRelationships.contains(new String[]{mappedBy,className,"","ONE_TO_MANY"})){
			classRelationships.add(new String[]{mappedBy,className,"","ONE_TO_MANY"});

			//TODO:REMOVE MARKER LINE
			System.err.println(mappedBy+" "+className+" "+"ONE_TO_MANY");
			
			return true;
			}
		return false;
			
		
	}
	
	public boolean addManyToOneRelation(String mappedBy, String className) {
		String mappingField = "";
		for (int i = 0; i < classMappings.size(); i++){
			if (classMappings.get(i)[0].equals(mappedBy)){
				mappingField = classMappings.get(i)[1];
				break;
			}
			if (classMappings.get(i)[1].equals(mappedBy)){
				mappingField = classMappings.get(i)[1];
				mappedBy = classMappings.get(i)[0];
				break;
			}
		}
		
		if (!mappingField.equals("")){
			
			
				//cerco dov'è l'elemento e lo rimuovo
				
				for(int i = 0; i < classAttributes.size(); i++){
					if (classAttributes.get(i)[0].equals(mappedBy)){
						System.err.println(mappedBy+" should REMOVED "+classAttributes.get(i)[0]+" frm ATTRBS");
						classAttributes.remove(i);
						}
									
				}
				
				for(int i = 0; i < classKeys.size(); i++){
					if (classKeys.get(i)[0].equals(mappedBy)){
						System.err.println(mappedBy+" should REMOVED "+classAttributes.get(i)[0]+" frm ATTRBS");
						classKeys.remove(i);
					}
					
				}
				
				if(!classRelationships.contains(new String[]{mappedBy,className,mappingField,"MANY_TO_ONE"})){
					classRelationships.add(new String[]{mappedBy,className,mappingField,"MANY_TO_ONE"});

				}
				
				return true;
		}
		else{
			System.err.println("ERRORE: il campo "+mappedBy+" non può essere trovato.");
			return false;
		}
		
	}

	public ArrayList<String[]> getRelationships() {
		return this.classRelationships;
	}

	public DBConnection rigenerateConnection(){
		DBConnection conn;
		String buffer;
		try {
			buffer = HunkIO.readEntireFile(classPathInfo, "UTF-8" );
			String connectionInfoPath=buffer.split("<connection>")[1].split("</connection>")[0];
			
			String connectionbuffer =  HunkIO.readEntireFile(connectionInfoPath, "UTF-8" );
			
			name = connectionbuffer.split("<name>")[1].split("</name>")[0];
			driver = connectionbuffer.split("<driver>")[1].split("</driver>")[0];
			server = connectionbuffer.split("<server>")[1].split("</server>")[0];
			database = connectionbuffer.split("<database>")[1].split("</database>")[0];
			user = connectionbuffer.split("<user>")[1].split("</user>")[0];
			password = connectionbuffer.split("<password>")[1].split("</password>")[0];
			port = connectionbuffer.split("<port>")[1].split("</port>")[0];
			
			conn = new DBConnection(name, driver, server, database, user, password, port);
			conn.setPath(connectionInfoPath);
			return conn;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
 public String getClassTable(){
	 return this.classTable;
 }
 public void setClassTable(String classTable){
	 this.classTable = classTable;
 }

public String getJavaPath() {
	String result;
	result = classPathInfo.replaceFirst(className+"Info.xml", className+".java");
	return result;
}

public String getClassPath() {
	String result;
	result = getJavaPath().replaceFirst(".java", ".class");
	return result;
}

public String getPath() {
	String result;
	result = classPathInfo.replaceFirst(className+"Info.xml", "");
	return result;
}

public String getRootPath() {
	String result;
	result = getPath().replaceFirst("src/model/", "");
	return result;
}

public boolean isTarget(String tab, String arg) {
	if (!classTable.equals(tab)) return false;
	for (int i = 0; i < classMappings.size(); i++){
		if (classMappings.get(i)[1].equals(arg)) return true;
	}
	return false;
}

}
