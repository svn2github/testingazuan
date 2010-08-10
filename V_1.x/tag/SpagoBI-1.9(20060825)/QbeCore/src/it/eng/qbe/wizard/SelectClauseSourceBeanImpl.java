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
public class SelectClauseSourceBeanImpl
		implements ISelectClause {

	private List selectFields = null;
	
	
	public SelectClauseSourceBeanImpl() {
		super();
		selectFields = new ArrayList();
	}

	public List getSelectFields() {
		return this.selectFields;
	}

	public void setSelectFields(List aList) {
		this.selectFields = aList;
		
	}

	public void addSelectField(ISelectField selectField) {
		this.selectFields.add(selectField);
		
	}

	public void delSelectField(ISelectField selectField) {
		int positionOfElement = -1;
		
		ISelectField tmp = null;
		for (int i=0; i < this.selectFields.size(); i++){
			tmp = (ISelectField)this.selectFields.get(i);
			if (tmp.getId().equalsIgnoreCase(selectField.getId())){
				positionOfElement = i;
				break;
			}
		}
		if (positionOfElement > -1){
			this.selectFields.remove(positionOfElement);
		}
		
	}

	public void moveUp(ISelectField selectField) {
		int positionOfElement = -1;
		
		ISelectField tmp = null;
		for (int i=0; i < this.selectFields.size(); i++){
			tmp = (ISelectField)this.selectFields.get(i);
			if (tmp.getId().equalsIgnoreCase(selectField.getId())){
				positionOfElement = i;
				break;
			}
		}
		
		if (positionOfElement == 0){
			Logger.debug(SelectClauseSourceBeanImpl.class,"Cannot Move Up Position is 0");
		}else{
			int newPosition = positionOfElement - 1;
			
			ISelectField swap = (ISelectField)this.selectFields.set(newPosition, tmp);
			this.selectFields.set(positionOfElement, swap);
		}
		
	}

	public void moveDown(ISelectField selectField) {
		
			int positionOfElement = -1;
			
			ISelectField tmp = null;
			for (int i=0; i < this.selectFields.size(); i++){
				tmp = (ISelectField)this.selectFields.get(i);
				if (tmp.getId().equalsIgnoreCase(selectField.getId())){
					positionOfElement = i;
					break;
				}
			}
			
			if (positionOfElement == this.selectFields.size() -1){
				Logger.debug(SelectClauseSourceBeanImpl.class,"Cannot Move Element is at last position "+ (this.selectFields.size() - 1));
			}else{
				int newPosition = positionOfElement + 1;
				
				ISelectField swap = (ISelectField)this.selectFields.set(newPosition, tmp);
				this.selectFields.set(positionOfElement, swap);
			}
			
		
		
	}

	

	

	
	
	
}
