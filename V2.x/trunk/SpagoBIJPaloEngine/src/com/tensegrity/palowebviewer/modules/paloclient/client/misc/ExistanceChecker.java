/**
 * 
 */
package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;

public class ExistanceChecker implements IXVisitor {

    private final XObject checkedObject;
    private boolean found = false;

    public boolean wasFound() {
        return found;
    }

    public ExistanceChecker(XObject checkedObject) {
        this.checkedObject = checkedObject;
    }

    public void visit(XObject object) {
        found |= object == checkedObject;
    }

    public boolean hasFinished() {
        return wasFound();
    }

}