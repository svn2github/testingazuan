/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package org.json;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JSObject extends JSONObject {
	
	public JSObject() {
        super();
    }


 
    public JSObject(JSONObject jo, String[] names) throws JSONException {
        super(jo, names);
    }

    public JSObject(JSONTokener x) throws JSONException {
        super(x);
    }

    public JSObject(Map map) {
        super(map);
    }


    public JSObject(Object bean) {
        super(bean);
    }

    public JSObject(Object object, String names[]) {
        super(object, names);
    }

    public JSObject(String source) throws JSONException {
        this(new JSONTokener(source));
    }

    
	public String toString() {
        try {
            Iterator     keys = keys();
            StringBuffer sb = new StringBuffer("{");

            while (keys.hasNext()) {
                if (sb.length() > 1) {
                    sb.append(',');
                }
                Object o = keys.next();
                //sb.append(quote(o.toString()));
                String key = quote(o.toString()).trim();
                key = key.substring(1, key.length()-1); 
                sb.append(key);
                sb.append(':');
              
                String value = valueToString(this.myHashMap.get(o));
                if(value.charAt(0) == '"' && value.charAt(value.length()-1) == '"') {
                	value = "'" + value.substring(1, value.length()-1) + "'";
                }
                sb.append(value);
                
            }
            sb.append('}');
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
