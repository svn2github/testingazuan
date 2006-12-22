/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.datamart;

import java.util.ArrayList;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DatamartObject{

    private ArrayList elementIdList = null;

    private ArrayList valueList = null;
    /**
     * 
     */
    public DatamartObject() {
        super();
    }
    public ArrayList getElementIdList() {
        return elementIdList;
    }
    public void setElementIdList(ArrayList elementIdList) {
        this.elementIdList = elementIdList;
    }
    public ArrayList getValueList() {
        return valueList;
    }
    public void setValueList(ArrayList valueList) {
        this.valueList = valueList;
    }
}