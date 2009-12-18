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
package it.eng.spagobi.engines.geo.datamart.provider;

import java.util.Iterator;
import java.util.Map;

import it.eng.spagobi.engines.geo.dataset.provider.Link;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.FieldMetadata;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStoreMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.transformer.IDataStoreTransformer;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddLinkFieldTransformer implements IDataStoreTransformer {
	Link link;
	Map env;
	
	public AddLinkFieldTransformer(Link link, Map env){
		this.link = link;
		this.env = env;
	}
	
	public void transform(IDataStore dataStore) {
		IDataStoreMetaData dataStoreMeta = dataStore.getMetaData();
		FieldMetadata fieldMeta = new FieldMetadata();
		
		fieldMeta.setName("CrossNavigationLinlk");
		fieldMeta.setType(String.class);
		fieldMeta.setProperty("ROLE", "CROSSNAVLINK");
		
		dataStoreMeta.addFiedMeta(fieldMeta);
		
		Iterator it = dataStore.iterator();
		while(it.hasNext()) {
			IRecord record = (IRecord)it.next();
			IField field = new Field( link.toXString(record, env) );			
			record.appendField( field );
		}
		
	}

}
