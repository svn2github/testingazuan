/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.spagobi.rest.objects.serDeser;

import java.util.HashMap;
import java.util.Map;

import com.sun.org.apache.xml.internal.serialize.Serializer;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * @author Monica Franceschini
 */
public class SerDeserFactory{
	
	public static String TYPE_DATASET ="dataset";
	public static String TYPE_DATASOURCE ="datasource";
	public static String TYPE_DASHBOARD ="dashboard";
	static Map<String, ObjectsSerDeser> types;
	
	static {
		types = new HashMap();
		types.put( TYPE_DATASET, new JsonDatasetSerDeser() );
		types.put( TYPE_DATASOURCE, new JsonDatasourceSerDeser() );
		types.put( TYPE_DASHBOARD, new JsonDashboardSerDeser() );
	}
	
	public static ObjectsSerDeser getSerDeser(String type) {
		return types.get( type );
	}
}
