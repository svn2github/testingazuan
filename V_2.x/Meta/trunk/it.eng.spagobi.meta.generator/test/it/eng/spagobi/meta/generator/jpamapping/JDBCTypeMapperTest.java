package it.eng.spagobi.meta.generator.jpamapping;

import static org.junit.Assert.*;

import java.sql.Types;

import it.eng.spagobi.meta.model.util.JDBCTypeMapper;

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
