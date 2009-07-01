package com.tensegrity.palowebviewer.modules.ui.client.action;

import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.AbstractAction;


public class XActionAdapter extends AbstractAction
{

    private final IXActionListener listenr;

    public IXActionListener getListener() {
        return listenr;
    }

    public XActionAdapter(IXActionListener listenr) {
        if(listenr == null)
            throw new IllegalArgumentException("Listener can not be null.");
        this.listenr = listenr;
    } 

    public void onActionPerformed(Object arg) {
        if(arg instanceof PaloTreeNode && arg != null) {
            PaloTreeNode node = (PaloTreeNode) arg;
            listenr.onAction(node.getXObject());
        }
        else{
            //TODO: warn of wrong object type
        }
    }

}
