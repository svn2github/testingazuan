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
public interface IOrderByClause extends  Serializable {

	public List getOrderByFields();
	
	public void setOrderByFields(List aList);
	
	public void addOrderByField(IOrderGroupByField aOrderGroupByField);
	
	public void delOrderByField(IOrderGroupByField aOrderGroupByField);

	public void moveUp(IOrderGroupByField aOrderGroupByField);
	
	public void moveDown(IOrderGroupByField aOrderGroupByField);
}
