/*
 * Created on 4-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.wizard;

import it.eng.qbe.utility.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OrderByClauseSourceBeanImpl 
		implements IOrderByClause {

	private List orderByFields = null;

	
	public OrderByClauseSourceBeanImpl(){
		super();
		this.orderByFields = new ArrayList();
	}
	
	public List getOrderByFields() {
		return this.orderByFields;
	}

	public void setOrderByFields(List aList) {
		this.orderByFields = aList;
		
	}

	public void addOrderByField(IOrderGroupByField aOrderGroupByField) {
		this.orderByFields.add(aOrderGroupByField);
		
	}

	public void delOrderByField(IOrderGroupByField aOrderGroupByField) {
		int positionOfElement = -1;
		
		IOrderGroupByField tmp = null;
		for (int i=0; i < this.orderByFields.size(); i++){
			tmp = (IOrderGroupByField)this.orderByFields.get(i);
			if (tmp.getId().equalsIgnoreCase(aOrderGroupByField.getId())){
				positionOfElement = i;
				break;
			}
		}
		if (positionOfElement > -1){
			this.orderByFields.remove(positionOfElement);
		}
		
	}
	
	public void moveUp(IOrderGroupByField aOrderGroupByField) {
		int positionOfElement = -1;
		
		IOrderGroupByField tmp = null;
		for (int i=0; i < this.orderByFields.size(); i++){
			tmp = (IOrderGroupByField)this.orderByFields.get(i);
			if (tmp.getId().equalsIgnoreCase(aOrderGroupByField.getId())){
				positionOfElement = i;
				break;
			}
		}
		
		if (positionOfElement == 0){
			Logger.debug(OrderByClauseSourceBeanImpl.class,"Cannot Move Up Position is 0");
		}else{
			int newPosition = positionOfElement - 1;
			
			IOrderGroupByField swap = (IOrderGroupByField)this.orderByFields.set(newPosition, tmp);
			this.orderByFields.set(positionOfElement, swap);
		}
		
	}

	public void moveDown(IOrderGroupByField aOrderGroupByField) {
		
			int positionOfElement = -1;
			
			IOrderGroupByField tmp = null;
			for (int i=0; i < this.orderByFields.size(); i++){
				tmp = (IOrderGroupByField)this.orderByFields.get(i);
				if (tmp.getId().equalsIgnoreCase(aOrderGroupByField.getId())){
					positionOfElement = i;
					break;
				}
			}
			
			if (positionOfElement == this.orderByFields.size() -1){
				Logger.debug(OrderByClauseSourceBeanImpl.class,"Cannot Move Element is at last position "+ (this.orderByFields.size() - 1));
			}else{
				int newPosition = positionOfElement + 1;
				
				IOrderGroupByField swap = (IOrderGroupByField)this.orderByFields.set(newPosition, tmp);
				this.orderByFields.set(positionOfElement, swap);
			}
			
		
		
	}

	
}
