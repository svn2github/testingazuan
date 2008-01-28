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
package it.eng.qbe.query;

import it.eng.qbe.wizard.EntityClass;

import java.io.Serializable;


public interface IWhereField extends IField{

	String getFieldOperator();
	void setFieldOperator(String fieldOperator);
	
	String getFieldValue();	
	void setFieldValue(String fieldValue);
	
	String getNextBooleanOperator();	
	void setNextBooleanOperator(String nextBooleanOperator);
	
	void setFieldEntityClassForRightCondition(EntityClass ec);
	EntityClass getFieldEntityClassForRightCondition();
	
	void setFieldEntityClassForLeftCondition(EntityClass ec);	
	EntityClass getFieldEntityClassForLeftCondition();	
	
	int getLeftBracketsNum() ;
	void setLeftBracketsNum(int leftBracketsNum);
	
	int getRightBracketsNum();
	void setRightBracketsNum(int rightBracketsNum);
	
	IWhereField getCopy();
}
