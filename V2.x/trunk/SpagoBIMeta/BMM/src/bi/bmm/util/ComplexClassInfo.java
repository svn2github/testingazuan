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
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.ui.PlatformUI;

import bi.bmm.DataPools;
import bi.bmm.figures.ActivityFigure;


public class ComplexClassInfo {
	
	private String classPathInfo;
	private String className;
	
	//mappa che contiene le relazioni nomeBC-lista di metodi derivanti dalla BC
	private HashMap<String,ArrayList<String[]>> inhBC;

	//mappa che contiene le relazioni nome WS-lista di metodi derivanti dalla WS
	private HashMap<String[],WSInfoValue> inhWS;
	
	/*FIGURE nella BMUView*/
	private ActivityFigure figure;
	private ArrayList<String[]> varsList;
	
	public ComplexClassInfo(String classPath,String className,
			HashMap<String,ArrayList<String[]>> inhBC,
			HashMap<String[],WSInfoValue> inhWS){
		if(className!=null)
		this.classPathInfo = classPath+className+"Info.xml";
		else
		this.classPathInfo = classPath;	
		this.className = className;
		this.inhBC = inhBC;
		this.inhWS = inhWS;
		this.figure = null;
		this.varsList = new ArrayList<String[]>();
	}
	
	public void printComplexClassInfo() {

			System.out.println(classPathInfo);
	
			System.out.println(className);
			
			if(!inhBC.isEmpty()) {
				Iterator<String> iter = inhBC.keySet().iterator();
				while(iter.hasNext()){
					String key = iter.next();
					
					
					ArrayList<String[]> methodList = inhBC.get(key);
					for (int i = 0; i < methodList.size(); i++){
						System.out.println(methodList.get(i)[0] + " type: " +methodList.get(i)[1]);
					}
				}
			}
			
			if(!inhWS.isEmpty()){
				Iterator<String[]> iter2 = inhWS.keySet().iterator();
				while(iter2.hasNext()){
					String[] key = iter2.next();
					WSInfoValue wsInfoValue = inhWS.get(key);
					
					System.out.println(key[0]);
					System.out.println("Conn " +wsInfoValue.getConn());
					System.out.println("Meth " +wsInfoValue.getMethodName());
					Iterator<String[]> it = wsInfoValue.getParametersMappings().iterator();
					while(it.hasNext()){
						String[] value = it.next();
						System.out.println(value[0] +" -> "+value[1]);
					}
					
					
				}
			}
	}
	
	public String getClassName(){
		return className;
	}
	
	public String getClassPathInfo(){
		return classPathInfo;
	}
	
	public HashMap<String[], WSInfoValue> getInhWS(){
		return inhWS;
	}
	
	public HashMap<String,ArrayList<String[]>> getInhBC(){
		return inhBC;
	}
	
	
	public void setFigure(ActivityFigure figure){
		this.figure = figure;
	}
	
	public ActivityFigure getFigure(){
		return this.figure;
	}

	public void buildInfo() {
		if (classPathInfo != null){
			//creo il file info.
			StringBuilder sb = new StringBuilder(50000);
			sb.append("<CBC>\n");
			sb.append("<path>"+classPathInfo+"</path>"+"\n");
			sb.append("<className>"+className+"</className>"+"\n");
			//aggiungo i field delle BC
			
				sb.append("<bcFields>\n");
				Iterator<String> iter = inhBC.keySet().iterator();
				while(iter.hasNext()){
					String key = iter.next();
					//apro il tag della BC
					sb.append("<fields>\n");
					sb.append("<bcName>"+key+"</bcName>"+"\n");
					ArrayList<String[]> methodList = inhBC.get(key);
					
					for (int i = 0; i < methodList.size(); i++){
						sb.append("<field>\n");
						sb.append("<fieldName>"+methodList.get(i)[0]+"</fieldName>"+"\n");
						sb.append("<fieldType>"+methodList.get(i)[1]+"</fieldType>"+"\n");
						sb.append("</field>\n");
					}
					//chiudo il tag della BC
					sb.append("</fields>"+"\n");
				}
				sb.append("</bcFields>\n");
			//aggiungo i field delle WS
				sb.append("<wsFields>\n");
				Iterator<String[]> iter2 = inhWS.keySet().iterator();
				while(iter2.hasNext()){
					String[] key = iter2.next();
					//apro il tag della WS
					sb.append("<wsField>\n");
					sb.append("<wsName>"+key[0]+"</wsName>"+"\n");
					sb.append("<wsType>"+XmltoJava(key[1])+"</wsType>"+"\n");
					WSInfoValue wsInfoValue = inhWS.get(key);
					sb.append("<wsConnPath>"+wsInfoValue.getConn().getPath()+"</wsConnPath>"+"\n");
					sb.append("<wsMethod>"+wsInfoValue.getMethodName()+"</wsMethod>"+"\n");
					
					ArrayList<String[]> mapsList = wsInfoValue.getParametersMappings();
					Iterator<String[]> itMaps = mapsList.iterator();
					while(itMaps.hasNext()){
						String[] value = itMaps.next();
						sb.append("<mapping>\n");
						sb.append("<paramName>"+value[0].split(" - ")[0]+"</paramName>"+"\n");
						sb.append("<fieldName>"+value[1].split(" - ")[0]+"</fieldName>"+"\n");
						sb.append("</mapping>\n");					
					}
					//chiudo il tag della BC
					sb.append("</wsField>"+"\n");
				}
				sb.append("</wsFields>\n");
			
			sb.append("</CBC>"+"\n");
			
			//lo scrivo su file
			try {
				HunkIO.writeEntireFile(classPathInfo, sb.toString(), "UTF-8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	private String XmltoJava(String string) {
		if (string.contains("string")) return "java.lang.String";
		return null;
	}

	public void buildClass(String pathCCi) {
		try {
			String file = HunkIO.readEntireFile(pathCCi);
			//recupero il path
			this.classPathInfo = file.split("<path>")[1].split("</path>")[0];
			//recupero il name
			this.className = file.split("<className>")[1].split("</className")[0];
			//recupero i field delle BC
			String buffer = file.split("<bcFields>")[1].split("</bcFields>")[0];
			String[] fieldBuffer = buffer.split("<fields>");
			String wsbuffer = file.split("<wsFields>")[1].split("</wsFields>")[0];
			String[] wsfieldBuffer = wsbuffer.split("<wsField>");
			
			//creo l'hashMap
			inhBC = new HashMap<String, ArrayList<String[]>>();
			
			for (int k=1;k<fieldBuffer.length;k++){
				String bcName = fieldBuffer[k].split("<bcName>")[1].split("</bcName>")[0];
				String[] fBuffer = fieldBuffer[k].split("<field>");
				ArrayList<String[]> list = new ArrayList<String[]>();
				for(int h = 1; h<fBuffer.length; h++){
					String attr = fBuffer[h].split("<fieldName>")[1].split("</fieldName>")[0];
					String attrType = fBuffer[h].split("<fieldType>")[1].split("</fieldType>")[0];
					list.add(new String[]{attr,attrType});
				}
				inhBC.put(bcName,list);
			}
			//TODO: WS DECRYPT
			inhWS = new  HashMap<String[],WSInfoValue>();
			//TODO:REMOVE MARKE LINE
			for (int k=1;k<wsfieldBuffer.length;k++){
				String wsName = wsfieldBuffer[k].split("<wsName>")[1].split("</wsName>")[0];
				String wsType = wsfieldBuffer[k].split("<wsType>")[1].split("</wsType>")[0];
				String wsConnPath = wsfieldBuffer[k].split("<wsConnPath>")[1].split("</wsConnPath>")[0];
				String wsMethod = wsfieldBuffer[k].split("<wsMethod>")[1].split("</wsMethod>")[0];
				
				String[] mBuffer = wsfieldBuffer[k].split("<mapping>");

				ArrayList<String[]> list = new ArrayList<String[]>();
				for(int h = 1; h<mBuffer.length; h++){
					String param = mBuffer[h].split("<paramName>")[1].split("</paramName>")[0];
					String field = mBuffer[h].split("<fieldName>")[1].split("</fieldName>")[0];
					list.add(new String[]{param,field});
				}
				
				WSConnection wsConn = new WSConnection(null, null, null);
				
				wsConn.rebuild(wsConnPath);
				inhWS.put(new String[]{wsName,wsType},new WSInfoValue(wsConn, wsMethod, list));
				//TODO:REMOVE MARKE LINE
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("An error occured reading file.");
		}
	}

	public void addInhWS(String[] returns, String wsMethodName,
			String wsConnName, ArrayList<Value> swtList) {
		//RECUPERO LA CONNESSIONE
		DataPools dataPool = (DataPools) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findView("bi.bmm.views.bme.datapools");
		ArrayList<String[]> array = new ArrayList<String[]>();
		Iterator<Value> it = swtList.iterator();
		while(it.hasNext()){
			Value cValue = it.next();
			array.add(new String[]{cValue.getL(),cValue.getC()});
		}
		
		WSInfoValue wsInfoValue = new WSInfoValue(dataPool.activeWS.getConnection(wsConnName), wsMethodName, array);
		this.inhWS.put(returns, wsInfoValue);
		this.buildInfo();
	}

	public void doJava() {
		//creo il file Java
		String javaPath = this.classPathInfo.replaceAll("Info\\.xml", "\\.java");
		
		//preparo il testo
		StringBuilder sb = new StringBuilder(50000);
		doJavaHead(sb);
		doJavaVars(sb);
		doJavaVar(sb);
		doJavaConstructor(sb);
		doJavaReturn(sb);
		doJavaWSCalling(sb);
		doJavaTail(sb);
		
		try {
			HunkIO.writeEntireFile(javaPath, sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	

	private void doJavaTail(StringBuilder sb) {
		sb.append("}\n");
	}

	private void doJavaHead(StringBuilder sb) {
		//ottengo il nome del file di persistenza
		String result;
		try {
			result = HunkIO.readEntireFile( HunkIO.DEFAULT_INFO_FILE, "UTF-8" );
		
		String[] sResult =result.split("#");
		
		sb.append("package model;"+"\n");
		sb.append("import java.util.*;"+"\n");
		sb.append("import javax.persistence.*;"+"\n");
		//apache import
		sb.append("import org.apache.axiom.om.*;"+"\n");
		sb.append("import org.apache.axis2.AxisFault;"+"\n");
		sb.append("import org.apache.axis2.client.Options;"+"\n");
		sb.append("import org.apache.axis2.client.ServiceClient;"+"\n");
		sb.append("import org.apache.axis2.addressing.EndpointReference;"+"\n");

		sb.append("public class "+this.className+" {"+"\n");
		
		sb.append("@PersistenceUnit"+"\n");
		sb.append("EntityManagerFactory emf;"+"\n");
		sb.append("protected EntityManager em;"+"\n");
		sb.append("public EntityManager initEntityManager() {"+"\n");
		
		sb.append("	emf = Persistence.createEntityManagerFactory(\""+sResult[0]+"\");"+"\n");
		sb.append("	em = emf.createEntityManager();"+"\n");
		sb.append("	return em;"+"\n");
		sb.append("	}"+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void doJavaVars(StringBuilder sb) {
		
		Iterator<String> i = this.inhBC.keySet().iterator();
		while(i.hasNext()){
			String key = i.next();
			String mini_key = key.substring(0,1).toLowerCase() + key.substring(1);
			sb.append("List<"+key+"> "+mini_key+";"+"\n");
			varsList.add(new String[]{key,mini_key,"List <"+key+">"});
			
		}
		
	}

	private void doJavaConstructor(StringBuilder sb) {
		//Creo il costruttore
		sb.append("@SuppressWarnings(\"unchecked\")"+"\n");
		sb.append("public "+this.className+"(){"+"\n");
		sb.append("initEntityManager();"+"\n");
		sb.append("Query q;"+"\n");
		
		Iterator<String[]> iter = varsList.iterator();
		while(iter.hasNext()){
			String[] res = iter.next();
			sb.append("q = em.createQuery(\"SELECT e0 FROM "+res[0]+" e0\");"+"\n");
			sb.append("this."+res[1]+" = (List<"+res[0]+">)q.getResultList();"+"\n");
		}

		sb.append("loadField();"+"\n");
		//MC: chiusura dell'istanza dell'Entity Manager e dell'EM Factory
		sb.append("em.clear(); \n");
		sb.append("em.close(); \n");
		sb.append("emf.close(); \n");
		sb.append("System.out.println(\"Chiuso l'entity manager\"); \n");
		//****
		sb.append("}"+"\n");
		sb.append(""+"\n");
		
	}
	
	private void doJavaReturn(StringBuilder sb){
		sb.append("public void loadField(){"+"\n");
		
		/*Iterator<Stores> i = stores.iterator();
		while(i.hasNext()){
		Stores c = i.next();
		store_id.add(c.store_id.toString());
		store_location.add(c.store_location.toString());
		phone_number.add(c.phone_number.toString());
		city_name.add(c.city.city_name.toString());
		state.add(c.city.state.toString());
		cap.add(doWSfindCap(
							c.city.city_name.toString(),
							null,
							null,
							null
							)
							);
		}*/
		Iterator<String[]> iter = varsList.iterator();
		while(iter.hasNext()){
			String[] res = iter.next();
			String BCname = res[0];
			String BClname = res[1];
			
			//MC: stampa di prova
			sb.append("System.out.println(\"Sono dentro loadField\"); \n");
			
			sb.append("Iterator<"+BCname+"> i"+BClname+" = "+BClname+".iterator();"+"\n");
			sb.append("while(i"+BClname+".hasNext()){"+"\n");
			//MC: stampa di prova
			sb.append("System.out.println(\"Sono dentro il while\"); \n");
			//MC: inserisco blocco try-catch
			sb.append("try { \n");
			//****
			
			sb.append(BCname+" c = i"+BClname+".next();"+"\n");
						
			Iterator<String[]> i = this.inhBC.get(BCname).iterator();
			while(i.hasNext()){
				String[] field = i.next();
				String[] camp = field[0].split("\\.");
				if(camp.length > 0)
					sb.append(camp[camp.length-1] +".add(c."+field[0]+".toString());"+"\n");
				else
					sb.append(field[0] +".add(c."+field[0]+".toString());"+"\n");
			}
			//MC: blocco catch
			sb.append("} \n");
			sb.append("catch (Exception e) { e.printStackTrace(); } \n");
			
			Iterator<String[]> it = this.inhWS.keySet().iterator();
			while(it.hasNext()){
				String[] key = it.next();
				WSInfoValue ws = this.inhWS.get(key);
				sb.append(key[0]+".add(doWS"+ ws.methodName+"("+"\n");
				//mappings Study
				Iterator<String[]> j = ws.parametersMappings.iterator();
				while (j.hasNext()){
					String[] param = j.next();
					String pp;
					if (!param[1].split(" - ")[0].equals("null")){
						pp = "c."+param[1].split(" - ")[0]+".toString()";
					}
					else 
						pp =param[1].split(" - ")[0];
					if (j.hasNext())
						sb.append(pp+",\n");
					else
						sb.append(pp+"\n");
					
				}
				sb.append(")\n");
				
			}
			//MC: sb.append(");\n");
			sb.append("}"+"\n");
			//sb.append("}"+"\n");
		}
		sb.append("}"+"\n");
		
	}
	
	private void doJavaVar(StringBuilder sb){
		Iterator<String[]> iter = varsList.iterator();
		while(iter.hasNext()){
			String[] res = iter.next();
			String BCname = res[0];
			Iterator<String[]> i = this.inhBC.get(BCname).iterator();
			while(i.hasNext()){
				String[] field = i.next();
				String[] camp = field[0].split("\\.");
				if(camp.length > 0)
					sb.append("public ArrayList<String> "+camp[camp.length-1]+"= new ArrayList<String>();"+"\n");
				else
					sb.append("public ArrayList<String> "+field[0]+"= new ArrayList<String>();"+"\n");
				
			}
			Iterator<String[]> it = this.inhWS.keySet().iterator();
			while(it.hasNext()){
				String[] key = it.next();
				sb.append("public ArrayList<String> "+key[0]+"= new ArrayList<String>();"+"\n");
			}
			}
		}
	
	/*
	 *UPDATE NO STUB 
	 * */
	private void doJavaWSCalling(StringBuilder sb) {
		
		Iterator<String[]> it = this.inhWS.keySet().iterator();
		while(it.hasNext()){
			String[] key = it.next();
			WSInfoValue ws = this.inhWS.get(key);
			sb.append("\n");
			
			sb.append("private String doWS"+ ws.methodName + "("+"\n");
			WSConnection conn = ws.conn;
			
			//oltre all'intestazione preparo anche la lista di argomenti da passare al payload
			//in una striga che userò più avanti
			String payLoadArgs = "";
			String payLoadParam="";
			Iterator<String[]> j = conn.getMethods().get(ws.methodName).iterator();
			while (j.hasNext()){
				String[] param = j.next();

				if (j.hasNext()){
					sb.append(XmltoJava(param[1])+" "+param[0]+",\n");
					payLoadArgs+= param[0]+", ";
					payLoadParam+= XmltoJava(param[1])+" "+param[0]+",";
				}
				else{
					sb.append(XmltoJava(param[1])+" "+param[0]+"\n");
					payLoadArgs+= param[0]+" ";
					payLoadParam+= XmltoJava(param[1])+" "+param[0]+" ";
				}
				
			}
			
			sb.append("){"+"\n");
			
			sb.append("Options opts = new Options();"+"\n");
			sb.append("opts.setTo(new EndpointReference(\""+ws.getConn().getEndPoint()+"\"));"+"\n");
			sb.append("opts.setAction(\"urn:"+ws.getMethodName()+"\");"+"\n");
			sb.append(""+"\n");
			sb.append("ServiceClient client;"+"\n");
			sb.append("try {"+"\n");
			sb.append("\tclient = new ServiceClient();"+"\n");
			sb.append("\tclient.setOptions(opts);"+"\n");
			sb.append("\tOMElement res = client.sendReceive(createPayLoad("+payLoadArgs+"));"+"\n");
			//TODO: VERY IMPORTANT: si è supposto x rapidità che il namespace fosse quello di default (ns) e
			//che ci fosse un solo argomento ritornato, questo ovviamente dipende dal ws e le info 
			//si possono caricare dal wsdl specificato in ws.conn.getWSDLPath()
			sb.append("\treturn res.toString().split(\"<ns:return>\")[1].split(\"</ns:return>\")[0];");
			sb.append("} catch (AxisFault e) {"+"\n");
			sb.append("\te.printStackTrace();"+"\n");
			sb.append("}"+"\n");
			sb.append("return null;"+"\n");
			sb.append("}"+"\n");
			sb.append("\n");
			doJavaWSPayload(sb, payLoadArgs,payLoadParam,ws);
		}
	}


	private void doJavaWSPayload(StringBuilder sb,String args, String payLoadParam, WSInfoValue ws) {
		String[] argsStrings=new String[]{};
		if (args.contains(",")){
			argsStrings = args.split(", ");
		}
		
		
		sb.append("public static OMElement createPayLoad("+payLoadParam+") {"+"\n");
		sb.append("OMFactory fac = OMAbstractFactory.getOMFactory();"+"\n");
		//TODO: STANDARD WS DEFAULT: anche qui si sono usati i path di default x il namespace
		sb.append("OMNamespace omNs = fac.createOMNamespace(\"http://service\", \"ns\");"+"\n");
		sb.append("OMElement method = fac.createOMElement(\""+ws.getMethodName()+"\", omNs);"+"\n");
		sb.append("OMElement value;"+"\n");
		for(int i =0; i< argsStrings.length; i++){
			String arg = argsStrings[i];
			sb.append("value = fac.createOMElement(\""+arg+"\", omNs);"+"\n");
			sb.append("value.setText("+arg+");"+"\n");
			sb.append("method.addChild(value);"+"\n");
		}
		
		
		sb.append("return method;"+"\n");
		sb.append("}"+"\n");
		
	}



}
