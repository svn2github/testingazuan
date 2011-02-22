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
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ConnectionsPool {

	public Map<String,DBConnection> activeConnections;
	
	public ConnectionsPool(){
		this.activeConnections = new HashMap<String, DBConnection>();
	}
	
	public void addConnection(DBConnection conn){
		if(this.activeConnections.containsKey(conn.getName())){
			MessageDialog.openWarning(new Shell(), "Error: Duplicate Key","A connection with name "+
					conn.getName()+" is open jet. Try to change the name.");
		}
		else{
			this.activeConnections.put(conn.getName(),conn);
		}
	}
	
	public DBConnection getConnection(String name){
		if(!this.activeConnections.containsKey(name)){
			MessageDialog.openWarning(new Shell(), "Error: Wrong name","A connection with name "+
					name+" does not exist.");
			return null;
		}
		else{
			return this.activeConnections.get(name);
		}
	}

	public List<String> getAllConnection() {
		
		List<String> activeConnStr = new ArrayList<String>();
		
		Iterator<String> i = activeConnections.keySet().iterator();
		while(i.hasNext()){
			   String key = (String)(i.next());
			   DBConnection element = (DBConnection)activeConnections.get( key );
			   activeConnStr.add(element.toString());
		}
		
		return activeConnStr;
	}

	public void deleteConnection(String name) {
		
		if(!this.activeConnections.containsKey(name)){
			MessageDialog.openWarning(new Shell(), "Error: Wrong name","A connection with name "+
					name+" does not exist.");
		}
		else{
			this.activeConnections.remove(name);
			MessageDialog.openInformation(new Shell(), "Delete Connection","The Connection "+
					name+" was deleted.");
		}
		
	}
	
}
