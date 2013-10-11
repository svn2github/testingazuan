/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.qbe.statement.graph.serializer;

import it.eng.qbe.statement.graph.Relationship;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class RelationJSONSerializer extends JsonSerializer<Relationship> {

	
	public static final String SOURCE = "source";
	public static final String TARGET = "target";
	public static final String RELATIONSHIP = "relationship";
	
	@Override
	public void serialize(Relationship value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException{
		jgen.writeStartObject();
		jgen.writeStringField(SOURCE, value.getSourceEntity().getName());
		jgen.writeStringField(TARGET, value.getTargetEntity().getName());
		jgen.writeStringField(RELATIONSHIP, value.getName());
		jgen.writeEndObject();
	}
}
