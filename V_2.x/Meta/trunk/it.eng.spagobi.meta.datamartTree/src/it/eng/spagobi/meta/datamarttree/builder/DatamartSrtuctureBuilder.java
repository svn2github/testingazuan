package it.eng.spagobi.meta.datamarttree.builder;

import java.util.ArrayList;

import it.eng.qbe.dao.DAOFactory;
import it.eng.qbe.datasource.jpa.JPADataSource;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.model.structure.builder.DataMartStructureBuilderFactory;
import it.eng.qbe.model.structure.builder.IDataMartStructureBuilder;

public class DatamartSrtuctureBuilder {

	public static DataMartModelStructure build(){

		JPADataSource dataSource = new JPADataSource("foodmart");
		dataSource.setDatamartName("foodmart");	
		ArrayList<String> s = new ArrayList<String>();
		s.add("foodmart");
		dataSource.setName("foodmart@java:comp/env/jdbc/foodmart_DS");
		dataSource.setDatamartNames(s);
		dataSource.setProperties( DAOFactory.getDatamartPropertiesDAO().loadDatamartProperties( "foodmart" ) );
			
		//get the factory  
		IDataMartStructureBuilder builder = DataMartStructureBuilderFactory.getDataMartStructureBuilder(dataSource);
		
		//build of the datamart structure
		DataMartModelStructure datamartModelStructure = DataMartStructureBuilderFactory.getDataMartStructureBuilder(dataSource).build();
		
		//get the root entities
		

		
		return datamartModelStructure;

	}
	

	
	
	
}
