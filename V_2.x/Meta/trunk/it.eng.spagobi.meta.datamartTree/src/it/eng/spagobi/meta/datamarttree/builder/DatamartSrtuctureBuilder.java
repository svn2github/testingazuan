package it.eng.spagobi.meta.datamarttree.builder;

import java.util.ArrayList;
import java.util.List;

import it.eng.qbe.dao.DAOFactory;
import it.eng.qbe.datasource.jpa.JPADataSource;
import it.eng.qbe.model.structure.AbstractDataMartItem;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.model.structure.builder.DataMartStructureBuilderFactory;
import it.eng.qbe.model.structure.builder.IDataMartStructureBuilder;
import it.eng.spagobi.meta.datamarttree.bo.DatamartItem;

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
		
//		List<DatamartItem> datamartFieldEntries= new ArrayList<DatamartItem>();
//
//		//build the tree
//		for(int i=0; i<rootEntries.size(); i++){
//			datamartFieldEntries.add(toDatamartField(rootEntries.get(i)));
//		}
//		
//		return datamartFieldEntries;
	}
	
	/**
	 * Build a DatamrtField for the visualization starting from a DataMartEntity
	 * @param dataMartEntity the datamart entity to transform
	 * @return the transformed DatamartField
	 */
	public static DatamartItem toDatamartField(DataMartEntity dataMartEntity){
		DatamartItem dataMartChildField;
		DatamartItem dataMartField = new DatamartItem(dataMartEntity.getName(), dataMartEntity.getPath(), dataMartEntity.getRole(), dataMartEntity.getType());

		//get the fields and set them as children
		List<DataMartField> dataMartEntityFields = dataMartEntity.getAllFields();
		for(int i=0; i<dataMartEntityFields.size(); i++){
			dataMartChildField = new DatamartItem(dataMartEntityFields.get(i).getName());
			dataMartChildField.setParent(dataMartField);
		}
		
		//get the fields and set them as children
		List<DataMartField> dataMartEntityCalculatedFields = dataMartEntity.getCalculatedFields();
		for(int i=0; i<dataMartEntityFields.size(); i++){
			dataMartChildField = new DatamartItem(dataMartEntityFields.get(i).getName());
			dataMartChildField.setParent(dataMartField);
		}
		
		//get the subentities and build the sub tree
		List<DataMartEntity> subEntities = dataMartEntity.getSubEntities();
		for(int i=0; i<subEntities.size(); i++){
			(toDatamartField(subEntities.get(i))).setParent(dataMartField);
		}
		return dataMartField;
	}
	
	
	
}
