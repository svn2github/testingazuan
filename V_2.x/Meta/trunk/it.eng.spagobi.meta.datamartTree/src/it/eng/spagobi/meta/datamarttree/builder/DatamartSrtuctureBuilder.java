package it.eng.spagobi.meta.datamarttree.builder;

import java.util.ArrayList;
import java.util.List;

import it.eng.qbe.dao.DAOFactory;
import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.jpa.JPADataSource;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.model.structure.builder.DataMartStructureBuilderFactory;
import it.eng.qbe.model.structure.builder.IDataMartStructureBuilder;
import it.eng.qbe.model.structure.builder.JPADatamartStructureBuilder;
import it.eng.spagobi.meta.datamarttree.bo.DatamartField;

public class DatamartSrtuctureBuilder {

	public static List<DatamartField> build(){
		DBConnection connection = new DBConnection();			
		connection.setName( "FoodMart" );
		connection.setDialect( "org.hibernate.dialect.MySQLInnoDBDialect" );			
		connection.setJndiName("java:comp/env/jdbc/foodmart");			
		connection.setDriverClass( null);			
		connection.setPassword( null );
		connection.setUrl( null );
		connection.setUsername( null );				
		
		JPADataSource jpads = new JPADataSource("foodmart");
		jpads.setDatamartName("foodmart");	
		ArrayList<String> s = new ArrayList<String>();
		s.add("foodmart");
		jpads.setName("foodmart@java:comp/env/jdbc/foodmart_DS");
		jpads.setDatamartNames(s);
		jpads.setConnection(connection);	
		jpads.setProperties( DAOFactory.getDatamartPropertiesDAO().loadDatamartProperties( "foodmart" ) );
		IDataMartStructureBuilder builder = new JPADatamartStructureBuilder(jpads);
		DataMartModelStructure dms = DataMartStructureBuilderFactory.getDataMartStructureBuilder(jpads).build();
		List<DataMartEntity> rootEntries = dms.getRootEntities("foodmart");
		List<DatamartField> datamartFieldEntries= new ArrayList<DatamartField>();

		for(int i=0; i<rootEntries.size(); i++){
			datamartFieldEntries.add(toDatamartField(rootEntries.get(i)));
		}
		
		return datamartFieldEntries;
		
		
	}
	
	public static DatamartField toDatamartField(DataMartEntity dme){
		DatamartField dmf = new DatamartField(dme.getName());
		List<DataMartEntity> subEntities = dme.getSubEntities();
		List<DataMartField> dmfs = dme.getAllFields();
		for(int i=0; i<dmfs.size(); i++){
			DatamartField dmfc = new DatamartField(dmfs.get(i).getName());
			dmfc.setParent(dmf);
		}
		for(int i=0; i<subEntities.size(); i++){
			(toDatamartField(subEntities.get(i))).setParent(dmf);
		}
		return dmf;
	}
	
	
}
