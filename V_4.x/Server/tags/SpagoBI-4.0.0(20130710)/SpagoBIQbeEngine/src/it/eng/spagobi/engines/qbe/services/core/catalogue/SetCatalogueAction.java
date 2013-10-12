/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.qbe.services.core.catalogue;

import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.qbe.model.structure.ModelStructure.RootEntitiesGraph;
import it.eng.qbe.query.IQueryField;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.QueryMeta;
import it.eng.qbe.query.serializer.SerializerFactory;
import it.eng.qbe.serializer.SerializationException;
import it.eng.qbe.statement.graph.DefaultCover;
import it.eng.qbe.statement.graph.ModelFieldPaths;
import it.eng.qbe.statement.graph.ModelObjectI18n;
import it.eng.qbe.statement.graph.PathChoice;
import it.eng.qbe.statement.graph.PathInspector;
import it.eng.qbe.statement.graph.QueryGraph;
import it.eng.qbe.statement.graph.QueryGraphBuilder;
import it.eng.qbe.statement.graph.Relationship;
import it.eng.qbe.statement.graph.serializer.ModelFieldPathsJSONDeserializer;
import it.eng.qbe.statement.graph.serializer.ModelObjectInternationalizedSerializer;
import it.eng.qbe.statement.graph.serializer.RelationJSONSerializer;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.engines.qbe.services.core.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;


/**
 * Commit all the modifications made to the catalogue on the client side
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class SetCatalogueAction extends AbstractQbeEngineAction {
	
	public static final String SERVICE_NAME = "SET_CATALOGUE_ACTION";
	public String getActionName(){return SERVICE_NAME;}
	
	// INPUT PARAMETERS
	public static final String CATALOGUE = "catalogue";
	public static final String CURRENT_QUERY_ID = "currentQueryId";
	public static final String AMBIGUOUS_FIELDS_PATHS = "ambiguousFieldsPaths";
	
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(SetCatalogueAction.class);
    
	
	public void service(SourceBean request, SourceBean response)  {				
		
		Monitor totalTimeMonitor = null;
		Monitor errorHitsMonitor = null;
		
		String jsonEncodedCatalogue = null;
		JSONArray queries;
		JSONObject queryJSON;
		Query query;
		
		logger.debug("IN");
		
		try {
			
			totalTimeMonitor = MonitorFactory.start("QbeEngine.setCatalogueAction.totalTime");
		
			super.service(request, response);		
			
			jsonEncodedCatalogue = getAttributeAsString( CATALOGUE );			
			logger.debug(CATALOGUE + " = [" + jsonEncodedCatalogue + "]");
			
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			Assert.assertNotNull(jsonEncodedCatalogue, "Input parameter [" + CATALOGUE + "] cannot be null in oder to execute " + this.getActionName() + " service");
			
			
			try {
				queries = new JSONArray( jsonEncodedCatalogue );
				for(int i = 0; i < queries.length(); i++) {
					queryJSON = queries.getJSONObject(i);
					query = deserializeQuery(queryJSON);
					
					getEngineInstance().getQueryCatalogue().addQuery(query);
					getEngineInstance().resetActiveQuery();
				}				
				
			} catch (SerializationException e) {
				String message = "Impossible to syncronize the query with the server. Query passed by the client is malformed";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
			
			Set<ModelFieldPaths> ambiguousFields = this.getAmbiguousFields();
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1,0,0,null));
			simpleModule.addSerializer(Relationship.class, new RelationJSONSerializer(getDataSource(), getLocale()));
			simpleModule.addSerializer(ModelObjectI18n.class, new ModelObjectInternationalizedSerializer(getDataSource(), getLocale()));
			
			mapper.registerModule(simpleModule);
			String serialized = mapper.writeValueAsString((Set<ModelFieldPaths>) ambiguousFields);
			
			try {
				writeBackToClient( serialized );
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
		
		} catch(Throwable t) {
			errorHitsMonitor = MonitorFactory.start("QbeEngine.errorHits");
			errorHitsMonitor.stop();
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			if(totalTimeMonitor != null) totalTimeMonitor.stop();
			logger.debug("OUT");
		}
		
		
	}


	public Set<ModelFieldPaths> getAmbiguousFields() {				
		Query query = null;

		logger.debug("IN");

		try {

			query = this.getCurrentQuery();
			if (query == null) {
				return new HashSet<ModelFieldPaths>();
			}
			QueryGraph queryGraph = this.getQueryGraph(query);
			query.setQueryGraph(queryGraph);
			String modelName = getDataSource().getConfiguration().getModelName();
			
			Map<IModelField, Set<IQueryField>> modelFieldsMap = query.getQueryFields(getDataSource());
			Set<IModelField> modelFields = modelFieldsMap.keySet();
			
			Assert.assertNotNull(modelFields, "No field specified in teh query");
			Set<ModelFieldPaths> ambiguousModelField = new HashSet<ModelFieldPaths>();
			if(modelFields!=null){
				Set<IModelEntity> modelEntities = getQueryEntities(modelFields);
				UndirectedGraph<IModelEntity, Relationship> graph = getDataSource().getModelStructure().getRootEntitiesGraph(modelName, false).getRootEntitiesGraph();
				
				PathInspector pathInspector = new PathInspector(graph, modelEntities);
				Map<IModelEntity, Set<GraphPath<IModelEntity, Relationship> >> ambiguousMap = pathInspector.getAmbiguousEntitiesAllPathsMap();
				
				Iterator<IModelField> modelFieldsIter = modelFields.iterator();

				
				while (modelFieldsIter.hasNext()) {
					IModelField iModelField = (IModelField) modelFieldsIter.next();
					IModelEntity me = iModelField.getParent();
					Set<GraphPath<IModelEntity, Relationship>> paths = ambiguousMap.get(me);
					if(paths!=null){
						Set<IQueryField> queryFields = modelFieldsMap.get(iModelField);
						if(queryFields!=null){
							Iterator<IQueryField> queryFieldsIter  = queryFields.iterator();
							while (queryFieldsIter.hasNext()) {
								ambiguousModelField.add(new ModelFieldPaths(queryFieldsIter.next(),iModelField, paths));
							}
						}
					}
				}
				
				DefaultCover.applyDefault(ambiguousModelField, graph, modelEntities);
			}
			
			// per Albi: da RIMUOVERE
			String serialized = this.getAttributeAsString(AMBIGUOUS_FIELDS_PATHS);
			if (StringUtilities.isNotEmpty(serialized) && !serialized.trim().equals("[]")) {
				return new HashSet<ModelFieldPaths>();
			}
			
			return ambiguousModelField;

		} catch (Throwable t) {
			throw new SpagoBIRuntimeException("Error while getting ambiguous fields", t);
		} finally {
			logger.debug("OUT");
		}	
	}

	private QueryGraph getQueryGraph(Query query) {
		List<Relationship> toReturn = new ArrayList<Relationship>();
		IModelStructure modelStructure = getDataSource().getModelStructure();
		logger.debug("IModelStructure retrieved");
		RootEntitiesGraph rootEntitiesGraph = modelStructure.getRootEntitiesGraph(getDataSource().getConfiguration().getModelName(), false);
		logger.debug("RootEntitiesGraph retrieved");
		UndirectedGraph<IModelEntity, Relationship> graph = rootEntitiesGraph.getRootEntitiesGraph();
		logger.debug("UndirectedGraph retrieved");
		Set<Relationship> relationships = rootEntitiesGraph.getRelationships();
		logger.debug("Set<Relationship> retrieved");
		String serialized = this.getAttributeAsString(AMBIGUOUS_FIELDS_PATHS);
		LogMF.debug(logger, AMBIGUOUS_FIELDS_PATHS + "is {0}", serialized);
		List<ModelFieldPaths> list;
		try {
			list = deserializeList(serialized, relationships, modelStructure, query);
		} catch (SerializationException e) {
			throw new SpagoBIEngineRuntimeException("Error while deserializing list of relationships", e);
		}
		logger.debug("Paths deserialized");
		QueryGraph queryGraph = null;
		if (list != null && !list.isEmpty()) {
			Iterator<ModelFieldPaths> it = list.iterator();
			while (it.hasNext()) {
				ModelFieldPaths modelFieldPaths = it.next();
				Set<PathChoice> set = modelFieldPaths.getChoices();
				Iterator<PathChoice> pathChoiceIterator = set.iterator();
				while (pathChoiceIterator.hasNext()) {
					PathChoice choice = pathChoiceIterator.next();
					toReturn.addAll(choice.getRelations());
				}
			}
			QueryGraphBuilder builder = new QueryGraphBuilder();
			queryGraph = builder.buildGraphFromEdges(toReturn);
		} else {
			Set<IModelEntity> entities = query.getQueryEntities( getDataSource() );
			queryGraph = DefaultCover.getCoverGraph(graph, entities);
		}
		logger.debug("QueryGraph created");
		return queryGraph;
	}

	private Query getCurrentQuery() {
		String queryId = this.getAttributeAsString(CURRENT_QUERY_ID);
		Query query = null;
		if (StringUtilities.isNotEmpty(queryId)) {
			query = this.getEngineInstance().getQueryCatalogue().getQuery(queryId);
		}
		return query;
	}


	private Set<IModelEntity> getQueryEntities(Set<IModelField> mf){
		Set<IModelEntity> me = new HashSet<IModelEntity>();
		Iterator<IModelField> mfi = mf.iterator();
		while (mfi.hasNext()) {
			IModelField iModelField = (IModelField) mfi.next();
			me.add(iModelField.getParent());
			
		}
		return me;
	}

	private QueryMeta deserializeMeta(JSONObject metaJSON) throws JSONException {
		QueryMeta meta = new QueryMeta();
		meta.setId( metaJSON.getString("id") );
		meta.setName( metaJSON.getString("name") );
		return null;
	}


	private Query deserializeQuery(JSONObject queryJSON) throws SerializationException, JSONException {
		//queryJSON.put("expression", queryJSON.get("filterExpression"));
		return SerializerFactory.getDeserializer("application/json").deserializeQuery(queryJSON.toString(), getEngineInstance().getDataSource());
	}

	public static List<ModelFieldPaths> deserializeList(String serialized, Collection<Relationship> relationShips,  IModelStructure modelStructure, Query query) throws SerializationException{
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1,0,0,null));
		simpleModule.addDeserializer(ModelFieldPaths.class, new ModelFieldPathsJSONDeserializer(relationShips, modelStructure, query));
		mapper.registerModule(simpleModule);
		TypeReference<List<ModelFieldPaths>> type = new TypeReference<List<ModelFieldPaths>>() {};
		try {
			return mapper.readValue(serialized, type);
		} catch (Exception e) {
			throw new SerializationException("Error deserializing the list of ModelFieldPaths", e);
		}
	}
	
}
