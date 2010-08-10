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
public class WhereClauseSourceBeanImpl implements IWhereClause{

	
	
	
	
	private List whereFields = null;
	
	
	
	public WhereClauseSourceBeanImpl(){
		this.whereFields = new ArrayList();
	}

	public List getWhereFields() {
		return this.whereFields;
	}

	public void setWhereFields(List aList) {
		this.whereFields = aList;
		
	}

	public void addWhereField(IWhereField whereField) {
		this.whereFields.add(whereField);	
	}
	
	public void delWhereField(IWhereField whereField) {
		int positionOfElement = -1;
		
		IWhereField tmp = null;
		for (int i=0; i < this.whereFields.size(); i++){
			tmp = (IWhereField)this.whereFields.get(i);
			if (tmp.getId().equalsIgnoreCase(whereField.getId())){
				positionOfElement = i;
				break;
			}
		}
		if (positionOfElement > -1){
			this.whereFields.remove(positionOfElement);
		}
	}
	
	
	public void moveUp(IWhereField whereField) {
		int positionOfElement = -1;
		
		IWhereField tmp = null;
		for (int i=0; i < this.whereFields.size(); i++){
			tmp = (IWhereField)this.whereFields.get(i);
			if (tmp.getId().equalsIgnoreCase(whereField.getId())){
				positionOfElement = i;
				break;
			}
		}
		
		if (positionOfElement == 0){
			Logger.debug(WhereClauseSourceBeanImpl.class,"Cannot Move Up Position is 0");
		}else{
			int newPosition = positionOfElement - 1;
			
			IWhereField swap = (IWhereField)this.whereFields.set(newPosition, tmp);
			this.whereFields.set(positionOfElement, swap);
		}
		
	}

	public void moveDown(IWhereField whereField) {
		
			int positionOfElement = -1;
			
			IWhereField tmp = null;
			for (int i=0; i < this.whereFields.size(); i++){
				tmp = (IWhereField)this.whereFields.get(i);
				if (tmp.getId().equalsIgnoreCase(whereField.getId())){
					positionOfElement = i;
					break;
				}
			}
			
			if (positionOfElement == this.whereFields.size() -1){
				Logger.debug(WhereClauseSourceBeanImpl.class,"Cannot Move Element is at last position "+ (this.whereFields.size() - 1));
			}else{
				int newPosition = positionOfElement + 1;
				
				IWhereField swap = (IWhereField)this.whereFields.set(newPosition, tmp);
				this.whereFields.set(positionOfElement, swap);
			}
			
		
		
	}

	
	
	
	
}
