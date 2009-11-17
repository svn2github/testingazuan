package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.Dataset;
import it.eng.spagobi.studio.geo.editors.model.geo.Datasource;

public class DatasourceBO {

	public static void addDatasource(Dataset dataset, String type,
			String driver, String url, String user, String password) {
		Datasource datasource = new Datasource();
		datasource.setDriver(driver);
		datasource.setPassword(password);
		datasource.setType(type);
		datasource.setUrl(url);
		datasource.setUser(user);
		
		dataset.setDatasource(datasource);
	}

}
