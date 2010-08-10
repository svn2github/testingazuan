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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
//MC: aggiunta
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class QueryUtil {
	
	
	private String query;
	private ArrayList<String> selectList;
	private ArrayList<String> fromList;
	private ArrayList<String> whereList;
	private String path;
	private String bmName;
	private ArrayList<String> res;
	private HashMap<String, String> alias;
	private ArrayList<String[]> resCBC;
	private String path_lib;

	public QueryUtil(String query, ArrayList<String> selectList, ArrayList<String> fromList, ArrayList<String> whereList){
		this.query = query;
		this.selectList = selectList;
		this.fromList = fromList;
		this.whereList = whereList;
		this.alias = new HashMap<String, String>();
		//MC: ricavo directory del progetto corrente
		URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
	    this.path_lib = url.toExternalForm().substring(6).replace("/", "//")+"lib";
	    //MessageDialog.openInformation(new Shell(), "Path_lib", path_lib);
	    
		//Workaround per l'export come RCP Application
		//URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
		//this.path_lib = System.getProperty("user.dir").replace("\\", "//")+"//lib";
		//MessageDialog.openInformation(new Shell(), "Path_lib", path_lib);

	}
	

	
	public void createService(String path){
		//creo il file Service.java con la query
		this.path = path;
		try {
			createServiceJava(query,path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//compilo i file 
		compileServiceJava();
		//richiamo il file Service.class
		//richiamo il suo metodo che ritorna un array di stringhe con i risultati della query
	}

	private void createServiceJava(String query2, String path) throws IOException {
		String pathService = path +"src/util/Service.java";
		
		for(int i=0;i<path.split("/").length-1;i++)
		{
		bmName = path.split("/")[i];
		}
		
		File file = new File(path +"src/util/");
		if(!file.exists()){
			file.mkdir();
		}
		
		
		StringBuilder sb = new StringBuilder(10000);
		
		//package
		sb.append("package util;" + "\n");
		//import
		stdImport(sb);
		entityImport(sb);
		//head
		doHead(sb);
		//method
		doMethod(sb);
		//ender
		doEnder(sb);
		
		//scivo il file
		
		try {
			HunkIO.writeEntireFile(pathService, sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	private void doMethod(StringBuilder sb) {
		sb.append("@SuppressWarnings(\"unchecked\")" + "\n");
		sb.append("public List<String> execute()" + "\n");
		sb.append("{" + "\n");
		if(selectList.size() > 1)
		{
				sb.append("List<Object[]> result;" + "\n");
				sb.append("Query q = em.createQuery(\"" +
						query
						+"\");" + "\n");
				sb.append("result = q.getResultList();" + "\n");
				sb.append("List<String> resultToString = new ArrayList<String>();" + "\n");
				sb.append("Iterator<Object[]> iterator = result.iterator();" + "\n");
				sb.append("while ( iterator.hasNext() ){" + "\n");
				sb.append("Object[] c = iterator.next();" + "\n");
				sb.append("String s=new String(\"\");" + "\n");
				sb.append("for(int i =0;i< c.length;i++)" + "\n");
				sb.append("{" + "\n");
				//MC: aggiunto controllo su campi null
				sb.append("if (c[i]!=null) " + "\n");
				//****
				sb.append("s+=c[i].toString()+\" ; \";" + "\n");
				//condizione per il null
				sb.append("else s+=\"NULL\"+\" ; \";" + "\n");
				//****
				sb.append("}" + "\n");
				sb.append("resultToString.add(s);" + "\n");
				sb.append("}" + "\n");

				sb.append("em.clear();" + "\n");
				sb.append("em.close();" + "\n");
				sb.append("emf.close();" + "\n");
				//MC: Stampa di prova
				sb.append("System.out.println(\"Chisura Entity Manager\"); \n");
				sb.append("return resultToString;" + "\n");
		}
		else
		{
			sb.append("List<Object> result;" + "\n");
			sb.append("Query q = em.createQuery(\"" +
					query
					+"\");" + "\n");
			sb.append("result = q.getResultList();" + "\n");
			sb.append("List<String> resultToString = new ArrayList<String>();" + "\n");
			sb.append("Iterator<Object> iterator = result.iterator();" + "\n");
			sb.append("while ( iterator.hasNext() ){" + "\n");
			sb.append("Object c = iterator.next();" + "\n");
			sb.append("String s=new String(\"\");" + "\n");
			//MC: aggiunto controllo su campi null
			sb.append("if (c!=null) " + "\n");
			//****
			sb.append("s+=c.toString()+\" ; \";" + "\n");
			//condizione per il null
			sb.append("else s+=\"NULL\"+\" ; \";" + "\n");
			//****
			sb.append("resultToString.add(s);" + "\n");
			sb.append("}" + "\n");
			sb.append("em.clear();" + "\n");
			sb.append("em.close();" + "\n");
			sb.append("emf.close();" + "\n");
			//MC: Stampa di prova
			sb.append("System.out.println(\"Chisura Entity Manager\"); \n");
			sb.append("return resultToString;" + "\n");
		}
		
		sb.append("}" + "\n");
		
		
		//
	}

	private void doEnder(StringBuilder sb) {
		sb.append("}\n");
	}

	private void doHead(StringBuilder sb) {
		sb.append("public class Service {" + "\n");
		sb.append("@PersistenceUnit" + "\n");
		sb.append("EntityManagerFactory emf;" + "\n");
		sb.append("EntityManager em;" + "\n");
		sb.append("public Service(){" + "\n");
		sb.append("this.initEntityManager();" + "\n");
		sb.append("}" + "\n");
		sb.append("" + "\n");
		sb.append("public EntityManager initEntityManager() {" + "\n");
		sb.append("emf = Persistence.createEntityManagerFactory(\""+bmName+"\");" + "\n");
		sb.append("em = emf.createEntityManager();" + "\n");
		sb.append("return em;" + "\n");
		sb.append("}" + "\n");
		sb.append("" + "\n");
	}

	private void stdImport(StringBuilder sb) {
		sb.append("//standard import packages" + "\n");
		sb.append("import java.util.ArrayList;" + "\n");
		sb.append("import java.util.Iterator;" + "\n");
		sb.append("import java.util.List;" + "\n");
		sb.append("import javax.persistence.EntityManager;" + "\n");
		sb.append("import javax.persistence.EntityManagerFactory;" + "\n");
		sb.append("import javax.persistence.Persistence;" + "\n");
		sb.append("import javax.persistence.PersistenceUnit;" + "\n");
		sb.append("import javax.persistence.Query;" + "\n");
		sb.append("" + "\n");
	}
	
	private void entityImport(StringBuilder sb) {
		//importo solo le entità presenti nella from
		for (int i=0;i<fromList.size();i++)
		{
			sb.append("//entity import packages" + "\n");
			sb.append("import model." + fromList.get(i) + ";"+ "\n");
		}
		sb.append("" + "\n");
	}

	private void compileServiceJava() {
				
			//TODO:IMPORTANT//sposto il file persistence nella path attuale
		/*
			File f = new File("/META-INF/");
			if (!f.exists()){
				f.mkdir();
			}
			
			File newFile = new File("/META-INF/persistence.xml");
			if(!newFile.exists()){
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			System.err.println(System.getProperty("user.dir"));
			try {
				System.in.read();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			File oldFile = new File(path+"src/META-INF/persistence.xml");
			if(!oldFile.exists()){
				System.err.println("IMPOSSIBILE TROVARE IL FILE DI PERSISTENZA DEL BM!!");
			}
			else{
				oldFile.renameTo(newFile);
			}

			*/
			  
		    //apro un java compiler
			JavaCompiler jc = ToolProvider.getSystemJavaCompiler();

			//apro un diagnostic per gli errori
			DiagnosticCollector<JavaFileObject> dc =
		         new DiagnosticCollector<JavaFileObject>();
			//apro un filemanager
			StandardJavaFileManager sjfm = jc.getStandardFileManager(dc, null, null);
			File javaFile = new File(path+"src/util/Service.java");
			Iterable fileObjects = sjfm.getJavaFileObjects(javaFile);
			
			//String cp = ConstantString.ECLIPSELINK_PATH+";"+ConstantString.JAVA_PERSISTENCE_PATH+";";
			String cp = path_lib+"//eclipselink.jar"+";"+path_lib+"//javax.persistence_1.99.0.v200906021518.jar"+";";
			System.out.println("Class path generato: "+cp);
			
			String sp = path +"src/";
			
			String[] options = new String[]{"-d", path +"bin/" ,"-classpath",cp,"-sourcepath",sp};  

			jc.getTask(null, null, null, Arrays.asList(options), null, fileObjects).call(); 
			
			try {
				sjfm.close();
				
				myClassLoader classLoader = new myClassLoader( path +"bin;"+cp, "class" ) ;
				Object o ;
				String tst = "util.Service" ;

				System.out.println( "Utilizzo di myClassLoader." ) ;

				try {
					try{
						o = Class.forName(tst);
					}catch(ClassNotFoundException e){
						o = (classLoader.loadClass(tst)).newInstance() ;
					}
					
					
					Method method = o.getClass().getDeclaredMethod("execute", null);
					res = 
					(ArrayList<String>) method.invoke(o, null);

					//genera il dialogo con i risultati
					resultDialog();
					
				} catch (Exception e ) {
					e.printStackTrace() ;
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			System.out.println("Class has been successfully loaded");
		
	}

	public void resultDialog (){
		Shell shell = new Shell();
	    shell.setText("Query Result");
	    shell.setSize(300,400);
	    
		GridData dataBoth = new GridData(GridData.FILL_BOTH);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.numColumns = 1; 
		
		shell.setLayout(gridLayout);
	    
	    Composite c = new Composite(shell, SWT.BORDER);
	    
	   
		c.setLayout(gridLayout); 
		c.setLayoutData(dataBoth);
		
	    Table tb = new Table(c, SWT.BORDER);
	    for(int i = 0; i < selectList.size(); i++){
    		TableColumn tc = new TableColumn(tb, SWT.CENTER);
    		tc.setText(selectList.get(i).split("\\.")[selectList.get(i).split("\\.").length-1]);
    		tc.setWidth(100);
    	}
	    
	    tb.setHeaderVisible(true);
	    
	    for(int i=0;i<res.size();i++){
	    	String row = res.get(i);
	    	TableItem ti = new TableItem(tb, 0);
	    	/*
	    	if(res.get(i).split(";").length>0){
	    	for(int z = 0; z < res.get(i).split(";").length; z++){
	    		ti.setText(res.get(i).split(";")[z]);
	    	}
	    	}else*/	ti.setText(res.get(i).split("\\;"));
	    	
	    	
	    }
	    tb.setLayout(gridLayout);
	    tb.setLayoutData(dataBoth);
	    shell.pack();
	    shell.open();
	}

	public void resultCBCDialog (){
		Shell shell = new Shell();
	    shell.setText("Query Result");
	    shell.setSize(300,400);
	    
		GridData dataBoth = new GridData(GridData.FILL_BOTH);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.numColumns = 1; 
		
		shell.setLayout(gridLayout);
	    
	    Composite c = new Composite(shell, SWT.BORDER);
	    
	   
		c.setLayout(gridLayout); 
		c.setLayoutData(dataBoth);
		
	    Table tb = new Table(c, SWT.BORDER);
	    for(int i = 0; i < selectList.size(); i++){
    		TableColumn tc = new TableColumn(tb, SWT.CENTER);
    		tc.setText(selectList.get(i).split("\\.")[selectList.get(i).split("\\.").length-1]);
    		tc.setWidth(100);
    	}
	    
	    tb.setHeaderVisible(true);
	    
	    for(int i=0;i<resCBC.size();i++){
	    	String[] row = resCBC.get(i);
	    	TableItem ti = new TableItem(tb, 0);
	    	ti.setText(row);
	    	
	    	
	    }
	    tb.setLayout(gridLayout);
	    tb.setLayoutData(dataBoth);
	    shell.pack();
	    shell.open();
	}

	public void createCBCService(String validRootPath,ArrayList<ComplexClassInfo>cbcList){
		createCBCServiceJava(validRootPath,cbcList);
		
		compileCBCServiceJava();
	}
	
	private void compileCBCServiceJava() {
			  
	    //apro un java compiler
		JavaCompiler jc = ToolProvider.getSystemJavaCompiler();

		//apro un diagnostic per gli errori
		DiagnosticCollector<JavaFileObject> dc =
	         new DiagnosticCollector<JavaFileObject>();
		//apro un filemanager
		StandardJavaFileManager sjfm = jc.getStandardFileManager(dc, null, null);
		File javaFile = new File(path+"src/util/CBCService.java");
		
		Iterable fileObjects = sjfm.getJavaFileObjects(javaFile);
		
		//String cp = ConstantString.ECLIPSELINK_PATH+";"+ConstantString.JAVA_PERSISTENCE_PATH+";"+ConstantString.WS_STUBS_PATH+";";
		String cp = path_lib+"//eclipselink.jar"+";"+path_lib+"//javax.persistence_1.99.0.v200906021518.jar"+";"+path_lib+"//axis2-1.4.1.jar"+";"+path_lib+"//axiom-api-1.2.7.jar"+";";
		System.out.println("Class Path per CBC: "+cp);
		
		//String sp = path +"src/;"+ConstantString.WS_STUBS_PATH_SRC+"/;";
		String sp = path +"src/;";
		
		String[] options = new String[]{"-d", path +"bin/" ,"-classpath",cp,"-sourcepath",sp};  

		jc.getTask(null, null, null, Arrays.asList(options), null, fileObjects).call(); 
		
		try {
			sjfm.close();
			
			myClassLoader classLoader = new myClassLoader( path +"bin;"+cp, "class" ) ;
			Object o ;
			String tst = "util.CBCService" ;

			System.out.println( "Utilizzo di myClassLoader." ) ;

			try {
				try{
					o = Class.forName(tst);
				}catch(ClassNotFoundException e){
					o = (classLoader.loadClass(tst)).newInstance() ;
				}
				
				Method method = o.getClass().getDeclaredMethod("execute", null);
				resCBC = (ArrayList<String[]>) method.invoke(o, null);

				//genera il dialogo con i risultati
				resultCBCDialog();
				
			} catch (Exception e ) {
				e.printStackTrace() ;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		 
		
		
	}

	public void createCBCServiceJava(String validRootPath,ArrayList<ComplexClassInfo> cbcList) {
		//ottengo il path
		String pathService = validRootPath +"src/util/CBCService.java";
		path = validRootPath;
		File file = new File(path +"src/util/");
		if(!file.exists()){
			file.mkdir();
		}
		
		
		StringBuilder sb = new StringBuilder(10000);
		
		//package
		sb.append("package util;" + "\n");
		sb.append("\n");
		sb.append("import java.util.*;"+"\n");
		//import
		entityImport(sb);
		//head
		doCBCHead(sb);
		//method
		doCBCMethod(sb);
		//ender
		sb.append("}\n");
		
		//scivo il file
		
		try {
			HunkIO.writeEntireFile(pathService, sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doCBCMethod(StringBuilder sb) {
			/*
			 * public static List<String[]> execute(){
					
					ArrayList<String[]> ret = new ArrayList<String[]>();		
					
					for(int i =0; i<s.store_id.size();i++){
					if(superStore.city_name.get(i).equals("Pinerolo")){	
						String[] ss = new String[]{
								s.store_id.get(i),
								s.store_location.get(i),
								s.cap.get(i),
								s.city_name.get(i)
								};
						ret.add(ss);
					}
					}
					return ret;
					
					
				}
			 * */
		
			sb.append("public static List<String[]> execute(){" + "\n");
			sb.append("ArrayList<String[]> ret = new ArrayList<String[]>();\n");
			
			String select = selectList.get(0);
			String selectTab = select.split("\\.")[0];
			String selectArg = select.split("\\.")[1];
			sb.append("for(int i =0; i<"+
					/*s.store_id*/alias.get(selectTab)+"."+selectArg
					+".size();i++){"+"\n");
			if(!whereList.isEmpty())
			{
				//aggiungo le where condition
				Iterator<String> clauseIt =  whereList.iterator();
				sb.append("if(\n");
				while(clauseIt.hasNext()){
					String clause = clauseIt.next();
					String clauseTab =clause.split("\\.")[0];
					String clauseArg =clause.split("\\.")[1];
					String clauseCond =clause.split("\\.")[2];
					if(clauseIt.hasNext())
					sb.append(alias.get(clauseTab)+"."+clauseArg+".get(i)."+clauseCond+" &&\n");
					else
					sb.append(alias.get(clauseTab)+"."+clauseArg+".get(i)."+clauseCond+" \n");
				}
				sb.append("){\n");
			}			
			
			sb.append("\n");
			sb.append("String[] ss = new String[]{"+"\n");
				//creo la nuova stringa	
				Iterator<String> selIt = selectList.iterator();
				while(selIt.hasNext()){
					String sel = selIt.next();
					String selTab = sel.split("\\.")[0];
					String selArg = sel.split("\\.")[1];
					if(selIt.hasNext()){
						sb.append(
							/*s.store_id.get(i)*/
							alias.get(selTab)+"."+selArg+".get(i)"+
							",\n");
					}
					else{
						sb.append(
								/*s.store_id.get(i)*/
								alias.get(selTab)+"."+selArg+".get(i)"+
								"\n");
					}
					
				}
						
			sb.append("};\n");
			sb.append("ret.add(ss);\n");
			sb.append("}\n");
			if(!whereList.isEmpty())
				sb.append("}");
			sb.append("return ret;\n");
			sb.append("}\n");
			
	}

	private void doCBCHead(StringBuilder sb) {
		
			sb.append("public class CBCService {" + "\n");
			sb.append("\n");
			//riempio l'alias map
			Iterator<String> fromIt = fromList.iterator();
			while(fromIt.hasNext()){
				String from = fromIt.next();
				String fromLtl = from.substring(0,1).toLowerCase() + from.substring(1);
				alias.put(from, fromLtl);
				sb.append("static "+from + " "+ fromLtl +" = "+ " new " + from + "();" + "\n");
			}
			
	}


}
