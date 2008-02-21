/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class ServerNode extends PaloTreeNode {

    public ServerNode(PaloTreeModel model, XServer server) {
        super(model, server);
    }

    protected XObject[] getXObjectChildren() {
        XServer server = (XServer)getValue();
        return server.getDatabases();
    }

    protected PaloTreeNode createNode(XObject obj) {
        return new DatabaseNode(getPaloTreeModel(), (XDatabase)obj);
    }

	protected int getChildType() {
		return IXConsts.TYPE_DATABASE;
	}

}