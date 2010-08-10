package com.tensegrity.palowebviewer.modules.ui.client;

import com.tensegrity.palowebviewer.modules.ui.client.action.IXActionListener;
import com.tensegrity.palowebviewer.modules.ui.client.action.XActionAdapter;
import com.tensegrity.palowebviewer.modules.ui.client.tree.CubeNode;
import com.tensegrity.palowebviewer.modules.ui.client.tree.ViewNode;
import com.tensegrity.palowebviewer.modules.widgets.client.IActionFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;


public class TreeActionFactory implements IActionFactory
{

    private final IAction action;

    public IAction getActionFor(Object object) {
        IAction result = null;
        if(object instanceof CubeNode){
            result = action;
        }else if(object instanceof ViewNode){
        	result = action;
        }
        return result;
    }

    public TreeActionFactory (IXActionListener listener) {
        this.action = new XActionAdapter(listener);

    }

}
