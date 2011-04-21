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

public class WSInfoValue {
	WSConnection conn;
	String methodName;
	ArrayList<String[]> parametersMappings;
	public WSInfoValue(WSConnection conn, String methodName,ArrayList<String[]> list) {
		this.conn = conn;
		this.methodName = methodName;
		this.parametersMappings =list;
	}
	public WSConnection getConn() {
		return conn;
	}
	public String getMethodName() {
		return methodName;
	}
	public ArrayList<String[]> getParametersMappings() {
		return parametersMappings;
	}
	public String getClassName() {
		String classres;
		try {
			classres = HunkIO.readEntireFile(conn.getWSDLPath());
			if (classres.split("/service/").length>0)
				return classres.split("/services/")[1].split("\"/>")[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	
}
