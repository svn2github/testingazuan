package it.eng.qbe.statement.graph;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.UndirectedGraph;

public class GraphValidatorInspector {

	
	public static boolean isValid(UndirectedGraph G){

		List<IGraphValidator> validators = getValidatorsList();
		for(int i=0; i<validators.size(); i++){
			if(!validators.get(i).validate(G)){
				return false;
			}
		}
		
		return true;
		
	}
	
	private static List<IGraphValidator> getValidatorsList(){
		List<IGraphValidator> validators;
		validators = new ArrayList<IGraphValidator>();
		validators.add(new ConnectionValidator());
		return validators;
	}
	
}
