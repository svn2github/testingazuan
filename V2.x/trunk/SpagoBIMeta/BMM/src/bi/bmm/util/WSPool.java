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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class WSPool {

	public HashMap<String,WSConnection> activeWSs;
	
	public WSPool(){
		this.activeWSs= new HashMap<String, WSConnection>();
	}
	
	public void addWS(WSConnection ws){
		if(this.activeWSs.containsKey(ws.getName())){
			MessageDialog.openWarning(new Shell(), "Error: Duplicate Key","A ws with name "+
					ws.getName()+" is open jet. Try to change the name.");
		}
		else{
			this.activeWSs.put(ws.getName(),ws);
		}
	}
	
	public WSConnection getConnection(String name){
		if(!this.activeWSs.containsKey(name)){
			return null;
		}
		else{
			return this.activeWSs.get(name);
		}
	}

	public ArrayList<WSConnection> getAllWS() {
		
		ArrayList<WSConnection> activeConnStr = new ArrayList<WSConnection>();
		
		Iterator<String> i = activeWSs.keySet().iterator();
		while(i.hasNext()){
			   String key = (String)(i.next());
			   WSConnection element = (WSConnection)activeWSs.get( key );
			   activeConnStr.add(element);
		}
		
		return activeConnStr;
	}

	public void deleteConnection(String name) {
		
		if(!this.activeWSs.containsKey(name)){
			MessageDialog.openWarning(new Shell(), "Error: Wrong name","A connection with name "+
					name+" does not exist.");
		}
		else{
			this.activeWSs.remove(name);
			MessageDialog.openInformation(new Shell(), "Delete Connection","The Connection "+
					name+" was deleted.");
		}
		
	}
	
}
