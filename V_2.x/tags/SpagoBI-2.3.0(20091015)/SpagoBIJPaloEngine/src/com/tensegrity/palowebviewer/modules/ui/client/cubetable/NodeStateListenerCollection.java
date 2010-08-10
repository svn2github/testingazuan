/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.INodeStateListener;

public class NodeStateListenerCollection {
    private final List listenerList = new ArrayList();

    public void addListener(INodeStateListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener can not be null");
        listenerList.add(listener);
    }

    public void removeListener(INodeStateListener listener) {
        listenerList.remove(listener);
    }

    public void fireNodeStateCanged(HeaderTreeNode node) {
    	Object[] listeners = listenerList.toArray();
    	for (int i = 0; i < listeners.length; i++) {
            INodeStateListener listener = (INodeStateListener)listeners[i];
            listener.nodeStateChanged(node);
        } 
    }

}