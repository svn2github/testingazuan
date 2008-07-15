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

**/
package it.eng.spagobi.container;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Context {
	
	private Calendar _creationDate;
	private Map _container;
	
	public Context() {
		_creationDate = new GregorianCalendar();
		_container = new HashMap();
	}
	
	public Calendar getCreationDate() {
		return _creationDate;
	}
	
	public Object get(String key) {
		return _container.get(key);
	}
	
	public List getKeys() {
		List toReturn = new ArrayList();
		Set keys = _container.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			toReturn.add(key);
		}
		return toReturn;
	}
	
	public void set(String key, Object object) {
		_container.put(key, object);
	}
	
	public void remove(String key) {
		_container.remove(key);
	}
	
	public boolean isOlderThan(int minutes) {
		Calendar now = new GregorianCalendar();
    	Calendar someTimeAgo = new GregorianCalendar();
    	someTimeAgo.set(Calendar.MINUTE, now.get(Calendar.MINUTE) - minutes);
    	return _creationDate.before(someTimeAgo);
	}
	
}
