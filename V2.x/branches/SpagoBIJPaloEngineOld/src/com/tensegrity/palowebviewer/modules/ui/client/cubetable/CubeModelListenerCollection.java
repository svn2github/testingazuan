package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.taskchain.IChainTask;
import com.tensegrity.palowebviewer.modules.util.client.taskchain.SimpleChainTask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.TaskQueue;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;

public class CubeModelListenerCollection {

	private final List listenerList = new ArrayList();
	private final List dataListenerList = new ArrayList();
	private int eventLock = 0;
	private final static TaskQueue taskQueue= TaskQueue.getInstance();
	
	//{{{3 Event firering
	
	public boolean isEventLocked() {
        return eventLock>0;
    }

    public void lockEvents() {
    	Logger.debug("lockEvents("+eventLock+")");
        eventLock++;
    }
    
    protected void unlockEvents() {
    	Logger.debug("unlockEvents("+eventLock+")");
        if(isEventLocked())
            eventLock--;
    }

	public void clear() {
		listenerList.clear();
		dataListenerList.clear();
	}
	
	public void fireCellChanged(int row, int column, IResultElement value) {
	    if(isEventLocked())
	        return;
	    Object[] listeners = dataListenerList.toArray();
	    for (int i = 0; i < listeners.length; i++) {
	        IDataListener listener = (IDataListener)listeners[i];
	        listener.cellChanged(row, column, value);
	    } 
	}

	public void fireLeftChildrenChanged(ITreeModel model, Object parent) {
	    if(isEventLocked())
	        return;
		Object[] listeners = listenerList.toArray();
	    for (int i = 0; i < listeners.length; i++) {
	        ICubeTableModelListener listener = (ICubeTableModelListener)listeners[i];
	        listener.leftChildrenChanged(model, parent);
	    } 
	}

	public void fireLeftTreeNodeStateChanged(HeaderTreeNode node) {
	    if(isEventLocked())
	        return;
		Object[] listeners = listenerList.toArray();
	    for (int i = 0; i < listeners.length; i++) {
	        ICubeTableModelListener listener = (ICubeTableModelListener)listeners[i];
	        listener.leftTreeNodeStateChanged(node);
	    } 
	}

	public void fireModified(){
	    if(isEventLocked())
	        return;
		Object[] listeners = listenerList.toArray();
	    for (int i = 0; i < listeners.length; i++) {
	        ICubeTableModelListener listener = (ICubeTableModelListener)listeners[i];
	        listener.modified();
	    } 
	}
	
	public void fireSelectionChanged() {
		Object[] listeners = listenerList.toArray();
	    for (int i = 0; i < listeners.length; i++) {
	        ICubeTableModelListener listener = (ICubeTableModelListener)listeners[i];
	        listener.selectionChanged();
	    } 
	}

	public void fireOnDispose() {
		Object[] listeners = listenerList.toArray();
	    for (int i = 0; i < listeners.length; i++) {
	        ICubeTableModelListener listener = (ICubeTableModelListener)listeners[i];
	        listener.onModelDispose();
	    } 
	}

	public void fireStructureChanged() {
	    if(isEventLocked())
	        return;
	    Object[] listeners = listenerList.toArray();
	    for (int i = 0; i < listeners.length; i++) {
	        ICubeTableModelListener listener = (ICubeTableModelListener)listeners[i];
	        ITask task = new FireStructureChangeTask(listener);
	        taskQueue.add(task);
	    } 
	}

	public void fireTopChildrenChanged(ITreeModel model, Object parent) {
	    if(isEventLocked())
	        return;
		Object[] listeners = listenerList.toArray();
	    for (int i = 0; i < listeners.length; i++) {
	        ICubeTableModelListener listener = (ICubeTableModelListener)listeners[i];
	        listener.topChildrenChanged(model, parent);
	    } 
	}

	public void fireTopTreeNodeStateChanged(HeaderTreeNode node) {
	    if(isEventLocked())
	        return;
		Object[] listeners = listenerList.toArray();
	    for (int i = 0; i < listeners.length; i++) {
	        ICubeTableModelListener listener = (ICubeTableModelListener)listeners[i];
	        listener.topTreeNodeStateChanged(node);
	    } 
	}

	public void fireUpdateFinished() {
	    if(isEventLocked())
	        return;
		Object[] listeners = listenerList.toArray();
	    for (int i = 0; i < listeners.length; i++) {
	        ICubeTableModelListener listener = (ICubeTableModelListener)listeners[i];
	        listener.updateFinished();
	    } 
	    
	}
	
	public void fireZStateChanged(boolean value) {
	    if(isEventLocked())
	        return;
		Object[] listeners = listenerList.toArray();
	    for (int i = 0; i < listeners.length; i++) {
	        ICubeTableModelListener listener = (ICubeTableModelListener)listeners[i];
	        listener.zStateChanged(value);
	    } 
	}
	
    public void addListener(ICubeTableModelListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener can not be null.");
        listenerList.add(listener);
    }

    public void removeListener(ICubeTableModelListener listener) {
        listenerList.remove(listener);
    }
    
    public void addListener(IDataListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener can not be null.");
        dataListenerList.add(listener);
    }

    public void removeListener(IDataListener listener) {
        dataListenerList.remove(listener);
    }
    
    public boolean hasDataListeners() {
    	return !dataListenerList.isEmpty();
    }
    
    public IChainTask getFireStructureChangedTask() {
    	return new SimpleChainTask() {
    		protected void run() {
    			fireStructureChanged();
    		}
			public String getDescription() {
				return "FireStructureChanedTask";
			}
        };
    }
    
    public IChainTask getUnlockEventsTask() {
    	return new SimpleChainTask(){
    		protected void run() {
					unlockEvents();
			}
    		public String getDescription() {
				return "UnlockEventsTask("+eventLock+")";
			}
    	};
    }
    
    public IChainTask getLockEventsTask() {
    	return new SimpleChainTask(){
			protected void run() {
					lockEvents();
			}
			public String getDescription() {
				return "LockEventsTask("+eventLock+")";
			}
    	};
    }


    private static final class FireStructureChangeTask implements ITask {
    	
    	private final ICubeTableModelListener listener;

		public FireStructureChangeTask(ICubeTableModelListener listener) {
    		this.listener = listener;
    	}

		public void execute() {
			listener.structureChanged();
		}

		public String getName() {
			return "FireStructureChangeTask";
		}
    	
    }


}
