/*
 * Created on 4-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.wizard;

import java.io.Serializable;

/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ISelectField extends Serializable{
	
	public String getId();
	
	public void setId(String id);
	
	public String getFieldName();
	
	public void setFieldName(String aFieldName);
	
	public String getFieldNameWithoutOperators();

	public String getFieldAlias();
	
	public void setFieldAlias(String aFieldAlias);
	
	public void setFieldEntityClass(EntityClass ec);
	
	public EntityClass getFieldEntityClass();

	public void setFieldCompleteName(String fieldCompleteName);
	
	public String getFieldCompleteName();
	
	public void setHibType(String type);
	
	public void setPrecision(String precision);
	
	public void setScale(String scale);
	
	
	public String getHibType();
	
	public String getPrecision();
	
	public String getScale();
}
