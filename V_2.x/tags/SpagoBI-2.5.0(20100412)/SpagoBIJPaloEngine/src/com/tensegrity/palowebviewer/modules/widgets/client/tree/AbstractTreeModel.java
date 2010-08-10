package com.tensegrity.palowebviewer.modules.widgets.client.tree;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;


/**
 * Base implementation for methods of {@link ITreeModel}. 
 *
 */
public abstract class AbstractTreeModel implements ITreeModel
{

    private final List listenerList = new ArrayList();
    private int eventLock = 0;

    protected void lockEvents(){
        eventLock ++;
    }

    protected boolean isEventLocked() {
        return eventLock>0;
    }

    protected void unlockEvents() {
        if(isEventLocked())
            eventLock--;
    }

    public void addTreeModelListener(ITreeModelListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener was null");
        listenerList.add(listener);
    }

    public void removeTreeModelListener(ITreeModelListener listener) {
        listenerList.remove(listener);
    }

    protected Object[] getChildNodes(TreePath path, int[] childIndices) {
        Object[] result = null;
        if(childIndices != null) {
            final int length = childIndices.length;
            result = new Object[length];
            Object parent = path.getLastPathComponent();
            for( int i = 0 ; i < length ; i++ ) { 
               result[i] = getChild(parent, childIndices[i]); 
            } 
        }
        return result;
    }

    protected void fireTreeStructureChanged(TreePath path) {
        final TreeModelEvent event = new TreeModelEvent(this, path, null, null);
        fireTreeStructureChanged(event);
    }

    protected void fireTreeNodesChanged(TreePath path, int[] childIndices) {
        Object[] children = getChildNodes(path, childIndices);
        final TreeModelEvent event = new TreeModelEvent(this, path, childIndices, children);
        fireTreeNodesChanged(event);
    }

    protected void fireTreeNodesInserted(TreePath path, int[] childIndices){
        Object[] children = getChildNodes(path, childIndices);
        final TreeModelEvent event = new TreeModelEvent(this, path, childIndices, children);
        fireTreeNodesInserted(event);
    }

    protected void fireTreeNodesRemoved(TreePath path, int[] childIndices) {
       
        //Object[] children = getChildNodes(path, childIndices);
        Object[] children = null;//TODO: fix it. We can not get Children after they wer removed.
        final TreeModelEvent event = new TreeModelEvent(this, path, childIndices, children);
        fireTreeNodesRemoved(event);
    }

    protected void fireTreeStructureChanged() {
        final TreeModelEvent event = new TreeModelEvent(this,(TreePath) null);
        fireTreeStructureChanged(event);
    }

    protected void fireTreeNodesChanged(TreeModelEvent event) {
        if(isEventLocked())
            return;
        PerformanceTimer timer = new PerformanceTimer(this + ".fireTreeNodesChanged("+event.getTreePath()+")");
        timer.start();
    	final Object[] listeners = listenerList.toArray();
    	for (int i = 0; i < listeners.length; i++) {
            ITreeModelListener listener = (ITreeModelListener)listeners[i];
            listener.treeNodesChanged(event);
        } 
        timer.report();
    }

    protected void fireTreeNodesInserted(TreeModelEvent event){
        if(isEventLocked())
            return;
        PerformanceTimer timer = new PerformanceTimer(this + ".fireTreeNodesInserted("+event.getTreePath()+")");
        timer.start();
        final int size = listenerList.size();
        for( int i = 0 ; i < size ; i++ ) { 
            ITreeModelListener listener = (ITreeModelListener)listenerList.get(i);
            listener.treeNodesInserted(event);
        } 
        timer.report();
    }

    protected void fireTreeNodesRemoved(TreeModelEvent event) {
        if(isEventLocked())
            return;
        PerformanceTimer timer = new PerformanceTimer(this + ".fireTreeNodesRemoved("+event.getTreePath()+")");
        timer.start();
    	final Object[] listeners = listenerList.toArray();
    	for (int i = 0; i < listeners.length; i++) {
            ITreeModelListener listener = (ITreeModelListener)listeners[i];
            listener.treeNodesRemoved(event);
        } 
        timer.report();
    }

    protected void fireTreeStructureChanged(TreeModelEvent event) {
        if(isEventLocked())
            return;
        PerformanceTimer timer = new PerformanceTimer(this + ".fireTreeStructureChanged("+event.getTreePath()+")");
        timer.start();
    	final Object[] listeners = listenerList.toArray();
    	for (int i = 0; i < listeners.length; i++) {
            ITreeModelListener listener = (ITreeModelListener)listeners[i];
            PerformanceTimer listenerTimer = new PerformanceTimer(listener+".treeStructureChanged()");
            listenerTimer.start();
            listener.treeStructureChanged(event);
            listenerTimer.report();
        } 
        timer.report();
    }

    public boolean isLoaded(Object object) {
        return true;
    }

    public void load(Object object) {
    }

}
