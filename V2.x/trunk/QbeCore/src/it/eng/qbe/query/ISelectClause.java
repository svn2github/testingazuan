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

import it.eng.qbe.utility.CalculatedField;

import java.io.Serializable;
import java.util.List;

public interface ISelectClause extends  Serializable {
	
	boolean isEmpty();
	
	List getSelectFields();	
	List getCalcuatedFields();
	

	void addSelectField(ISelectField selectField);	
	void addCalculatedField(CalculatedField calculatedField);
	
	void delSelectField(String fieldId);	
	void deleteSelectFields();
	
	void deleteCalculatedField(String calculatedField);	
	void deleteCalculatedFields();
	
	void deleteAllFields();		
	void setSelectFields(List aList);
	
	
	void moveUp(String fieldId);
	
	void moveDown(String fieldId);
	
	
	ISelectClause getCopy();	
}
