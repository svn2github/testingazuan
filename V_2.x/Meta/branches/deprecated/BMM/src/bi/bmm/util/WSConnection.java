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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class WSConnection {
	private String name;
	private String endPoint;
	private String wsdlPath;
	private HashMap<String, ArrayList<String[]>> methods;
	private String infoPath;


	public WSConnection(String name, String endPoint, String wsdlPath) {
		this.name = name;
		this.endPoint = endPoint;
		this.wsdlPath = wsdlPath;
		this.methods = new HashMap<String, ArrayList<String[]>>();
		//creo le operazioni associate al wsdl
		if(name != null){
			makeOperation();
			makeInfoFile();
		}
	}

	private void makeInfoFile() {
		infoPath = ConstantString.PROJECT_PATH +"ws"+name+".xml";
		StringBuilder sb = new StringBuilder(3000);
		sb.append("<wsConnection>"+"\n");
		sb.append("<wsConnectionName>"+this.name+"</wsConnectionName>"+"\n");
		sb.append("<wsEndPoint>"+this.endPoint+"</wsEndPoint>"+"\n");
		sb.append("<wsdlPath>"+this.wsdlPath+"</wsdlPath>"+"\n");
		sb.append("</wsConnection>"+"\n");
		try {
			HunkIO.writeEntireFile(infoPath, sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void rebuild(String infoPath){
		//TODO:REMOVE MARKE LINE
		System.err.println("gone rebuild");
		this.infoPath = infoPath;
		try {
			String result = HunkIO.readEntireFile(infoPath);
			
			this.name = result.split("<wsConnectionName>")[1].split("</wsConnectionName>")[0];
			this.endPoint = result.split("<wsEndPoint>")[1].split("</wsEndPoint>")[0];
			this.wsdlPath = result.split("<wsdlPath>")[1].split("</wsdlPath>")[0];
			makeOperation();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	*	genero a partire dal wsdl di un ws una mappa con il nome del metodo ed i parametri
	**/
	private void makeOperation() {
		File wsdlFile = new File(wsdlPath);
		if(!wsdlFile.exists()){
			MessageDialog.openError(new Shell(), "Error: wsdl not found","Impossible to find file .wsdl at "+
					wsdlPath+".");
			return;
		}
		//memorizzo a partire dal wsdl metodi, parametri e return.
		//TODO: CONSIDERARE IL CASO CHE ALCUNI WSDL NN SONO BEN DEFINITI E NN USANO XS:
		try {
			String wsdl = HunkIO.readEntireFile(wsdlFile);
			String[] buffer = wsdl.split("<xs:element name=\"");
			for(int i = 1; i < buffer.length; i++){
				//recupero il nome del metodo
				String methodName = buffer[i].split("\">")[0];
				String sequence = buffer[i].split("<xs:sequence>")[1].split("</xs:sequence>")[0];
				//recupero i parametri
				String[] elements = sequence.split("<xs:element");
				ArrayList<String[]> parameters = new ArrayList<String[]>();
				for (int j = 1; j < elements.length; j++){
					String element = elements[j];
					String parameterName = element.split("name=\"")[1].split("\"")[0];
					String parameterType = element.split("type=\"")[1].split("\"")[0];
					parameters.add(new String[]{parameterName,parameterType});
				}
				//aggiungo un nuovo metodo alla mappa
				methods.put(methodName, parameters);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 
	 * Restituisce la mappa con i metodi del WS
	 * 
	 * */
	public HashMap<String, ArrayList<String[]>> getMethods(){
		return this.methods;
	}
	@Override
	public String toString(){
		StringBuilder ws = new StringBuilder(40000);
		ws.append("Name: "+this.name+"\n");
		ws.append("EndPoint at: "+this.endPoint+"\n");
		ws.append("WSDL file path at :"+this.wsdlPath+"\n");
		Iterator<String> iter = methods.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			ws.append("----------------------\n");
			ws.append("Method Name:"+key+"\n");
			ws.append("Parameters:\n");
			ArrayList<String[]> parameters = methods.get(key);
			for(int i = 0; i < parameters.size(); i++){
				ws.append("\t"+parameters.get(i)[0]+" - "+parameters.get(i)[1]+"\n");
			}
		}
		return ws.toString();
	}

	public String getName() {
		return this.name;
	}

	public String getEndPoint() {
		return this.endPoint;
	}
	
	public String getWSDLPath() {
		return this.wsdlPath;
	}

	public String getPath() {
		return this.infoPath;
	}
	
	
	
	
	

}
