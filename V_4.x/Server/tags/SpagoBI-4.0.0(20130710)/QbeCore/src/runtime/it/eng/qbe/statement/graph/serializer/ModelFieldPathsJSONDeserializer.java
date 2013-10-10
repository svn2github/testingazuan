package it.eng.qbe.statement.graph.serializer;

import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.qbe.model.structure.ModelStructure.RootEntitiesGraph.Relationship;
import it.eng.qbe.statement.graph.ModelFieldPaths;
import it.eng.qbe.statement.graph.PathChoice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphPathImpl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class ModelFieldPathsJSONDeserializer extends JsonDeserializer<ModelFieldPaths>{
	private Collection<Relationship> relationShips;
	private Graph<IModelEntity, DefaultEdge> graph;
	private IModelStructure modelStructure;
	
	private static final int defaultPathWeight = 1;
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String ENTITY = "entity";
	private static final String CHOICES = "choices";
	private static final String NODES = "nodes";
	private static final String START = "start";
	private static final String END = "end";
	private static final String ACTIVE ="active";
	

	public ModelFieldPathsJSONDeserializer(Collection<Relationship> relationShips, Graph<IModelEntity, DefaultEdge> graph, IModelStructure modelStructure){
		this.relationShips = relationShips;
		this.graph = graph;
		this.modelStructure = modelStructure;
	}
	
	
	@Override
	public ModelFieldPaths deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Set<PathChoice> choices = new HashSet<PathChoice>();
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        TextNode id = (TextNode)node.get(ID);
        TextNode name = (TextNode)node.get(NAME);
        TextNode entity = (TextNode)node.get(ENTITY);
        ArrayNode choicesJson = (ArrayNode) node.get(CHOICES);
        if(choicesJson!=null && name!=null){
        	for(int i=0; i<choicesJson.size(); i++){
        		choices.add(deserializePath(choicesJson.get(i)));
        	}
        	IModelField field = modelStructure.getField(name.textValue());
        	return new ModelFieldPaths(field, choices, true);
        }
        throw new JsonProcessingExceptionImpl("Can not deserialize the ModelFieldPaths. The mandatory fields are name and paths "+node.toString());
	}
	
	
	public PathChoice deserializePath(JsonNode node) throws  JsonProcessingException {
		
		ArrayNode nodes = (ArrayNode) node.get(NODES);
		TextNode start = (TextNode)node.get(START);
		TextNode end = (TextNode)node.get(END);
		BooleanNode active = (BooleanNode)node.get(ACTIVE);
		boolean activebool = active!=null && active.asBoolean();
        if(nodes!=null && start!=null && end!=null){
        	List<DefaultEdge> relations = new ArrayList<DefaultEdge>();
        	for(int i=0; i<nodes.size(); i++){
        		relations.add(deserializeRelationship(nodes.get(i)));
        	}
        	return new PathChoice(relations, activebool);
        }else{
        	throw new JsonProcessingExceptionImpl("The nodes, start and end values of a path must be valorized. Error processing node "+node.toString());
        }
        	
	}
	
	public Relationship deserializeRelationship(JsonNode node) throws  JsonProcessingException {
        TextNode source = (TextNode)node.get(RelationJSONSerializer.SOURCE);
        TextNode target = (TextNode)node.get(RelationJSONSerializer.TARGET);
        TextNode name = (TextNode)node.get(RelationJSONSerializer.RELATIONSHIP);
        if(name!=null){
        	return getRelationship(name.textValue(), source, target);
        }else{
        	throw new JsonProcessingExceptionImpl("The relation name is mandatory in the relation definition "+node.toString());
        }
	}
	
	public Relationship getRelationship(String name, Object source, Object target) throws  JsonProcessingException {
		if(relationShips!=null){
			Iterator<Relationship> iter = relationShips.iterator();
			while (iter.hasNext()) {
				Relationship relationship = (Relationship) iter.next();
				if(relationship.getName().equals(name)){
					return relationship;
				}
			}
		}
		throw new JsonProcessingExceptionImpl("Can not find the relation with name "+name+" in the graph");
	}
}
