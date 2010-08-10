/*
 * Created on 4-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.wizard;

import java.io.Serializable;
import java.util.List;

/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ISelectClause extends  Serializable {
	
	public List getSelectFields(); 
	
	public void setSelectFields(List aList);

	public void addSelectField(ISelectField selectField);
	
	public void delSelectField(ISelectField selectField);
	
	public void moveUp(ISelectField selectField);
	
	public void moveDown(ISelectField selectField);
	
	
	
}
