/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.generator.jpamapping;

import static org.junit.Assert.assertTrue;
import it.eng.spagobi.meta.model.util.JDBCTypeMapper;

import java.sql.Types;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JDBCTypeMapperTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetModelType() {
		String result = JDBCTypeMapper.getModelType((short)Types.DECIMAL);
		assertTrue("DECIMAL".equals(result));
	}

	@Test
	public void testGetJavaTypeName() {
		String result = JDBCTypeMapper.getJavaTypeName("LONGVARBINARY");
		assertTrue("java.lang.Byte[]".equals(result));
	}
	
	@Test
	public void testgetJavaType(){
		Class result = JDBCTypeMapper.getJavaType((short)Types.ARRAY);
		assertTrue("java.sql.Array".equals(result.getName()));		
	}

}
