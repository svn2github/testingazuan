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
public class GroupByClauseSourceBeanImpl 
		implements IGroupByClause {

	public List groupByFields = null;
	
	public GroupByClauseSourceBeanImpl(){
		super();
		this.groupByFields = new ArrayList();
	}
	
	public List getGroupByFields() {
		return this.groupByFields;
	}

	public void setGroupByFields(List aList) {
		this.groupByFields = aList;
		
	}

	public void addGroupByField(IOrderGroupByField aOrderGroupByField) {
		this.groupByFields.add(aOrderGroupByField);	
	}

	public void delGroupByField(IOrderGroupByField aOrderGroupByField) {
		int positionOfElement = -1;
		
		IOrderGroupByField tmp = null;
		for (int i=0; i < this.groupByFields.size(); i++){
			tmp = (IOrderGroupByField)this.groupByFields.get(i);
			if (tmp.getId().equalsIgnoreCase(aOrderGroupByField.getId())){
				positionOfElement = i;
				break;
			}
		}
		if (positionOfElement > -1){
			this.groupByFields.remove(positionOfElement);
		}
		
	}
	
	public void moveUp(IOrderGroupByField aOrderGroupByField) {
		int positionOfElement = -1;
		
		IOrderGroupByField tmp = null;
		for (int i=0; i < this.groupByFields.size(); i++){
			tmp = (IOrderGroupByField)this.groupByFields.get(i);
			if (tmp.getId().equalsIgnoreCase(aOrderGroupByField.getId())){
				positionOfElement = i;
				break;
			}
		}
		
		if (positionOfElement == 0){
			Logger.debug(GroupByClauseSourceBeanImpl.class,"Cannot Move Up Position is 0");
		}else{
			int newPosition = positionOfElement - 1;
			
			IOrderGroupByField swap = (IOrderGroupByField)this.groupByFields.set(newPosition, tmp);
			this.groupByFields.set(positionOfElement, swap);
		}
		
	}

	public void moveDown(IOrderGroupByField aOrderGroupByField) {
		
			int positionOfElement = -1;
			
			IOrderGroupByField tmp = null;
			for (int i=0; i < this.groupByFields.size(); i++){
				tmp = (IOrderGroupByField)this.groupByFields.get(i);
				if (tmp.getId().equalsIgnoreCase(aOrderGroupByField.getId())){
					positionOfElement = i;
					break;
				}
			}
			
			if (positionOfElement == this.groupByFields.size() -1){
				Logger.debug(GroupByClauseSourceBeanImpl.class,"Cannot Move Element is at last position "+ (this.groupByFields.size() - 1));
			}else{
				int newPosition = positionOfElement + 1;
				
				IOrderGroupByField swap = (IOrderGroupByField)this.groupByFields.set(newPosition, tmp);
				this.groupByFields.set(positionOfElement, swap);
			}
			
		
		
	}
}


