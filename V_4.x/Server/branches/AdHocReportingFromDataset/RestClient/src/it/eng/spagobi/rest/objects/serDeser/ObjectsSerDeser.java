/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.rest.objects.serDeser;

import it.eng.spago.base.SourceBeanException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;


public interface ObjectsSerDeser {
	Object deserialize(JSONObject o) throws SerializationException;
	JSONObject serialize(Object o, JSONArray objects) throws JSONException, SourceBeanException ;
}

