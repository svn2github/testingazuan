/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.NodeTreeModel.TreeNode;

public class RootNode extends PaloTreeNode {

    public RootNode(PaloTreeModel model, XRoot root) {
        super(model, root);
    }

    protected void loadChildren() {
    	XRoot root = (XRoot)getValue();
    	XServer[] servers = root.getServers();
    	for( int i = 0 ; i < servers.length ; i++ ) { 
    		TreeNode child = createNode(servers[i]);
    		addChild(child);
    	} 
    }

    protected XObject[] getXObjectChildren() {
        XRoot root = (XRoot)getValue();
        return  root.getServers();
    }

    protected PaloTreeNode createNode(XObject obj) {
        return new ServerNode(getPaloTreeModel(), (XServer)obj);
    }

	protected int getChildType() {
		return IXConsts.TYPE_SERVER;
	}
    
}