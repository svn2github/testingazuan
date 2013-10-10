/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.qbe.serializer.SerializationException;
import it.eng.qbe.statement.graph.serializer.ModelFieldPathsJSONDeserializer;
import it.eng.qbe.statement.graph.serializer.RelationJSONSerializer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ModelFieldPaths {

	private IModelField field;
	private Set<PathChoice> choices;

	public ModelFieldPaths(IModelField field, Set<GraphPath<IModelEntity, Relationship>> paths ){
		this.field = field;

		choices = new HashSet<PathChoice>();
		if(paths!=null){
			Iterator<GraphPath<IModelEntity, Relationship>> pathsiter = paths.iterator();
			while (pathsiter.hasNext()) {
				choices.add(new PathChoice(pathsiter.next()));
			}
		}
	}

	public ModelFieldPaths(IModelField field, Set<PathChoice> choices, boolean choicesBoolean){
		this.field = field;
		this.choices = choices;
	}
	

	public String getEntity(){
		return field.getParent().getUniqueName();
	}

	public String getName(){
		return field.getUniqueName();
	}

	public String getId(){
		return field.getUniqueName();
	}

	public Set<PathChoice> getChoices() {
		return choices;
	}

	@JsonIgnore
	public String getModelFieldPatsAsString() throws SerializationException{
		ObjectMapper mapper = new ObjectMapper();
		String s ="";
		try {
			SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1,0,0,null));
			simpleModule.addSerializer(Relationship.class, new RelationJSONSerializer());
			mapper.registerModule(simpleModule);
			s = mapper.writeValueAsString((ModelFieldPaths)this);


		} catch (Exception e) {

			throw new SerializationException("Error serializing the ModelFieldPaths",e);
		}

		return  s; 
	}

	public static ModelFieldPaths deserialize(String serialized, Collection<Relationship> relationShips, Graph<IModelEntity, Relationship> graph, IModelStructure modelStructure) throws SerializationException{
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1,0,0,null));
		simpleModule.addDeserializer(ModelFieldPaths.class, new ModelFieldPathsJSONDeserializer(relationShips,graph, modelStructure));
		mapper.registerModule(simpleModule);
		try {
			return mapper.readValue(serialized, ModelFieldPaths.class);
		} catch (Exception e) {
			throw new SerializationException("Error deserializing the ModelFieldPaths", e);
		}
	}
	
	public static List<ModelFieldPaths> deserializeList(String serialized, Collection<Relationship> relationShips, Graph<IModelEntity, Relationship> graph, IModelStructure modelStructure) throws SerializationException{
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1,0,0,null));
		simpleModule.addDeserializer(ModelFieldPaths.class, new ModelFieldPathsJSONDeserializer(relationShips,graph, modelStructure));
		mapper.registerModule(simpleModule);
		TypeReference<List<ModelFieldPaths>> type = new TypeReference<List<ModelFieldPaths>>() {};
		try {
			return mapper.readValue(serialized, type);
		} catch (Exception e) {
			throw new SerializationException("Error deserializing the list of ModelFieldPaths", e);
		}
	}


}
