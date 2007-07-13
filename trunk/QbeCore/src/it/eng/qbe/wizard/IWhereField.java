/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.qbe.wizard;

import java.io.Serializable;

/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IWhereField extends Serializable{
	
	public IWhereField getCopy();
	
	public String getId();
	
	public void setId(String id);
	
	public String getFieldName();

	public String getFieldOperator();
	
	public String getFieldValue();
	
	public String getHibernateType();
	
	public void setHibernateType(String hibernateType);
	
	public void setFieldName(String fieldName);

	public void setFieldOperator(String fieldOperator);
	
	public void setFieldValue(String fieldValue);
	
	public String getNextBooleanOperator();
	
	public void setNextBooleanOperator(String nextBooleanOperator);
	
	public void setFieldEntityClassForRightCondition(EntityClass ec);
	
	public void setFieldEntityClassForLeftCondition(EntityClass ec);
	
	public EntityClass getFieldEntityClassForRightCondition();
	
	public EntityClass getFieldEntityClassForLeftCondition();
	
	public int getLeftBracketsNum() ;
	
	public void setLeftBracketsNum(int leftBracketsNum);
	
	public int getRightBracketsNum();
	
	public void setRightBracketsNum(int rightBracketsNum);
}
