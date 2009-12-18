package com.tensegrity.palowebviewer.modules.widgets.client.dnd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.TaskQueue;


/**
 * Provides Drag & Drop functionality.
 *
 */
public class DnDManager {


	private final static int DRAG_DELAY = 500;
    public static final String DRAGGED_STYLE_NAME = "dragged";
    private final List targets = new ArrayList();
    private final DragTask dragTask = new DragTask(); 
    private final TaskQueue taskQueue = TaskQueue.getInstance();

    private final MouseListener mouseListener = new MouseListener() {

        

        public void onMouseDown(Widget sender, int x, int y) {
        	dragTask.delayDrag(sender, x, y);
        }

        public void onMouseEnter(Widget sender) {
        }

        public void onMouseLeave(Widget sender) {
        }

        public void onMouseMove(Widget sender, int x, int y) {
            dragTask.doDrag(x, y);

        }

        public void onMouseUp(Widget sender, int x, int y)  {
        	dragTask.drop(x, y);
        }

    };

    public void addDragObject(Widget widget) {
        if(widget == null)
            throw new IllegalArgumentException("Widget can not be null.");
        if(!(widget instanceof SourcesMouseEvents))
            throw new IllegalArgumentException("Widget must implement SourcesMouseEvents interface.");
        ((SourcesMouseEvents)widget).addMouseListener(mouseListener);
    }

    public void removeDragObject(Widget widget) {
        if(widget == null)
            throw new IllegalArgumentException("Widget can not be null.");
        if(!(widget instanceof SourcesMouseEvents))
            throw new IllegalArgumentException("Widget must implement SourcesMouseEvents interface.");
        ((SourcesMouseEvents)widget).removeMouseListener(mouseListener);
    }

    public void addTarget(IDropTarget widget) {
        if(widget == null)
            throw new IllegalArgumentException("Widget can not be null.");
        if(!(widget instanceof Widget))
            throw new IllegalArgumentException("Widget must be of Widget class");
        targets.add(widget);
    }

    public void removeTarget(IDropTarget widget) {
        if(widget == null)
            throw new IllegalArgumentException("Widget can not be null.");
        if(!(widget instanceof Widget))
            throw new IllegalArgumentException("Widget must be of Widget class");
        targets.remove(widget);
    }

    private static int getOverlap(int x1, int x2,int width1, int width2){
        int dx = x1 - x2;
        int result = 0;
        if(dx<0){
            result = width1+dx;
            result = result > width2 ? width2 : result;
        }
        else{
            result = width2-dx;
            result = result > width1 ? width1 : result;
        }
        return result;
    }

    private IDropTarget getTarget(int x, int y, Widget widget) {
        int maxArea = 0;
        Widget result = null;
        int width = widget.getOffsetWidth();
        int height = widget.getOffsetHeight();
        for( Iterator it = targets.iterator () ; it.hasNext (); ) { 
            Widget target = (Widget)it.next();
            int targetW = target.getOffsetWidth();
            int targetH = target.getOffsetHeight();
            int xOverlap = getOverlap(x, target.getAbsoluteLeft(), width, targetW);
            int yOverlap = getOverlap(y, target.getAbsoluteTop(), height, targetH);
            if (xOverlap > 0 && yOverlap > 0) {
                int area = xOverlap * yOverlap;
                if (area > maxArea) {
                    maxArea = area;
                    result = target;
                }
            }
        } 
        return (IDropTarget)result;
    }
    
    private final class DragTask extends Timer{

        private int xOffset;
        private int yOffset;
        private Widget dragged;
        private IDropTarget parent;

        private Widget sender;
        private int x;
        private int y;

        public void startDrag(){
        	if(dragged == null) {
        		xOffset = x;  // i.e., x - 0
        		yOffset = y;

        		Widget parent = sender.getParent();
        		if(parent!=null && parent instanceof IDropTarget) {
        			dragged = sender;
        			int xAbs = dragged.getAbsoluteLeft();
        			int yAbs = dragged.getAbsoluteTop();

        			this.parent = (IDropTarget)parent;
        			this.parent.removeDragObject(dragged);
        			dragged.removeFromParent();
        			RootPanel.get().add(dragged);
        			RootPanel.get().setWidgetPosition(dragged, xAbs, yAbs);
        			DOM.setCapture(dragged.getElement());
        			dragged.addStyleName(DRAGGED_STYLE_NAME);
        			sender = null;
        			cancel();
        		}
        	}
        }

        public void doDrag(int x, int y){
            if(sender != null && dragged == null) {
                startDrag();
            }
            this.x = x;
            this.y = y;
            if (dragged != null) {
                // convert from local to global coordinates
                int xAbs = x + dragged.getAbsoluteLeft();
                int yAbs = y + dragged.getAbsoluteTop();
                // reposition the widget in global coordinates
                x = xAbs - xOffset;
                y = yAbs - yOffset;
                RootPanel.get().setWidgetPosition(dragged, x, y);
            }
        }

        public void delayDrag(Widget sender, int x, int y) {
            if(dragged == null) {
                this.x = x;
                this.y = y;
                drop(0,0);
                this.sender = sender;
                this.schedule(DRAG_DELAY);
            }
        }

        public void drop(int x, int y) {
        	sender = null;
            cancel();
            if(dragged != null) {
                DOM.releaseCapture(dragged.getElement());
                int xAbs = dragged.getAbsoluteLeft();
                int yAbs = dragged.getAbsoluteTop();
                IDropTarget target = getTarget(xAbs, yAbs, dragged);
                RootPanel.get().setWidgetPosition(dragged, -1, -1);
                int targetX = 0;
                int targetY = 0;

                if(target !=null) {
                    targetX = xAbs - ((Widget)target).getAbsoluteLeft();
                    targetY = yAbs - ((Widget)target).getAbsoluteTop();
                }
               
                AcceptDropTask task = new AcceptDropTask(dragged);
                task.setParent(parent);
                task.setTarget(target);
                task.setX(targetX);
                task.setY(targetY);
                taskQueue.add(task);
               
                dragged.removeStyleName(DRAGGED_STYLE_NAME);
                dragged = null;
            }
        }

        public void run() {
            startDrag();
        }
    }
    
    private final class AcceptDropTask implements ITask {
    	private IDropTarget target;
    	private IDropTarget parent;
    	private final Widget widget;
    	private int x;
    	private int y;
    	
    	public AcceptDropTask(Widget widget) {
    		this.widget = widget;
    	}


		public void execute() {
			if(target != null && target.canAcceptDrop(widget, x, y)){
                target.acceptDrop(widget, x, y);
            }
            else if(parent !=null && parent.canAcceptDrop(widget, 0, 0))
                parent.cancelDrop(widget);
		}

		public void setParent(IDropTarget parent) {
			this.parent = parent;
		}

		public void setTarget(IDropTarget target) {
			this.target = target;
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}
		
		public String getName() {
			return "AcceptDropTask";
		}
    	
    	
    }
    
}


