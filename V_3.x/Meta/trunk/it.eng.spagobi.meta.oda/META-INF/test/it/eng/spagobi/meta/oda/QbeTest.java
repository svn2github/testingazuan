/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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

package it.eng.spagobi.meta.oda;

import java.io.File;
import java.util.List;

import org.json.JSONObject;

import it.eng.qbe.conf.QbeCoreSettings;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.hibernate.BasicHibernateDataSource;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.query.Query;
import it.eng.qbe.statment.IStatement;
import it.eng.qbe.statment.QbeDatasetFactory;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datawriter.JSONDataWriter;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeTest {
	public static final String DATAMART_DIR_PATH = "D:\\Documenti\\Sviluppo\\servers\\tomcat6spagobi3\\resources\\qbe\\datamarts";
	public static final String DATAMART_NAME = "foodmart";

	static void main(String[] args) {
		File qbeDataMartDir = new File(DATAMART_DIR_PATH);
		QbeCoreSettings.getInstance().setQbeDataMartDir(qbeDataMartDir);
		IDataSource datasource = new BasicHibernateDataSource(DATAMART_NAME);
		
		Query query = new Query();
		
		DataMartModelStructure dataMartModel = datasource.getDataMartModelStructure();
		List entities = dataMartModel.getRootEntities(DATAMART_NAME);
		if(entities.size() > 0) {
			DataMartEntity entity = (DataMartEntity)entities.get(0);
			List fields = entity.getAllFields();
			for(int i = 0; i < fields.size(); i++) {
				DataMartField field = (DataMartField)fields.get(i);
				query.addSelectFiled(field.getUniqueName(), null, field.getName(), true, true, false, null, null);			
			}
		}
		
		IStatement statement = datasource.createStatement(query);
		IDataSet datsSet = QbeDatasetFactory.createDataSet(statement);
		
		datsSet.loadData();
		IDataStore dataStore = datsSet.getDataStore();
		
		JSONDataWriter dataSetWriter = new JSONDataWriter();
		JSONObject output = (JSONObject)dataSetWriter.write(dataStore);
		
		System.out.println(output);
	}

}
