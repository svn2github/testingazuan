package it.eng.qbe.statement.graph.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonProcessingExceptionImpl extends JsonProcessingException{

	public JsonProcessingExceptionImpl(String msg) {
		super(msg);
	}

}
