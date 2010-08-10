/**
 * 
 */
package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;

public class IdFinder implements IXVisitor {

    private XObject result = null;
    private final String id;

    public IdFinder(String id){
        this.id = id;
    }

    public void visit(XObject object) {

        if(result == null && id.equals(object.getId())){
            result = object;
        }
    }

    public XObject getResult(){
        return result;
    }

	public boolean hasFinished() {
		return result != null;
	}

}