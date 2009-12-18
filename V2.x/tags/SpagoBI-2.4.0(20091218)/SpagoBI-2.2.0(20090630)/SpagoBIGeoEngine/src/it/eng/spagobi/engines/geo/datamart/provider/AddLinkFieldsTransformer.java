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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import it.eng.spagobi.engines.geo.dataset.provider.Hierarchy;
import it.eng.spagobi.engines.geo.dataset.provider.Link;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.FieldMetadata;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStoreMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IFieldMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.transformer.IDataStoreTransformer;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddLinkFieldsTransformer implements IDataStoreTransformer {
	String[] measureColumnNames;
	Hierarchy.Level level;
	Map env;
	
	public static transient Logger logger = Logger.getLogger(AddLinkFieldsTransformer.class);
	
	public AddLinkFieldsTransformer(String[] measureColumnNames, Hierarchy.Level level, Map env){
		this.measureColumnNames = measureColumnNames;
		this.level = level;
		this.env = env;
	}
	
	public void transform(IDataStore dataStore) {
		List fieldsMeta = dataStore.getMetaData().findFieldMeta("ROLE", "MEASURE");
		logger.debug("found " + fieldsMeta.size() + " measure column in dataset");
		for(int i = 0; i < fieldsMeta.size(); i++) {
			
			IFieldMetaData fieldMeta = (IFieldMetaData)fieldsMeta.get(i);
			String measureFiledName = fieldMeta.getName();
		
			String linkFiledName = measureFiledName + "_LINK";
			Link link = level.getLink(measureFiledName);
			addLinkField(linkFiledName, link, dataStore);
		}
		
	}
	
	
	public void addLinkField(String fieldName, Link link, IDataStore dataStore) {
		IDataStoreMetaData dataStoreMeta = dataStore.getMetaData();
		FieldMetadata fieldMeta = new FieldMetadata();
		
		logger.debug("add link column " + fieldName + ": " + link);
		
		fieldMeta.setName(fieldName);
		fieldMeta.setType(String.class);
		fieldMeta.setProperty("ROLE", "CROSSNAVLINK");
		
		dataStoreMeta.addFiedMeta(fieldMeta);
		
		Iterator it = dataStore.iterator();
		while(it.hasNext()) {
			IRecord record = (IRecord)it.next();
			IField field;
			if(link != null) {
				logger.debug("link added: " + link.toXString(record, env));
				field = new Field( link.toXString(record, env) );	
			} else {
				field = new Field( Link.DEFAULT_BASE_URL );				
			}
					
			record.appendField( field );
		}
		
	}

}
