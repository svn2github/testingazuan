/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XStringType;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXVisitor;

class StringTypeFinder implements IXVisitor{

    private boolean found = false;

    public boolean hasFounded(){
        return found;
    }

    public void visit(XObject object) {
        if (object instanceof XElement) {
            XElement element = (XElement) object;
            found |= XStringType.instance.equals(element.getType());
        }
    }

    public boolean hasFinished() {
        return hasFounded();
    }

}