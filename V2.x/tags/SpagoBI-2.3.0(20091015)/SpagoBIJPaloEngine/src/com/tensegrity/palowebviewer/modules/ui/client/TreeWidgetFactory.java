package com.tensegrity.palowebviewer.modules.ui.client;

import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XConsolidatedElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.TypeCastVisitor;
import com.tensegrity.palowebviewer.modules.ui.client.tree.FolderNode;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;
import com.tensegrity.palowebviewer.modules.widgets.client.LabelWidgetFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.LabeledImage;


public class TreeWidgetFactory extends LabelWidgetFactory
{

    private class XObjectWidgetFactory extends TypeCastVisitor {

        private Widget result;

        public Widget getResult () {
            return result;
        }
        
        public void visit(XObject object) {
        	setResult(null);
        	super.visit(object);
        }

        protected void setResult(Widget value) {
            result = value;
        }


        public void visitAxis(XAxis axis) {

        }

        public void visitConsolidatedElement(XConsolidatedElement consolidatedElement) {
        	visitElement(consolidatedElement);
        }

        public void visitCube(XCube cube) {
            String style = getCubeNodeStyle();
            setResult(new LabeledImage(style, cube.getName()));
        }

        public void visitDatabase(XDatabase database) {
            String style = getDatabaseNodeStyle();
            setResult(new LabeledImage(style, database.getName()));
        }

        public void visitDimension(XDimension dimension) {
            String style = getDimensionNodeStyle();
            setResult(new LabeledImage(style, dimension.getName()));
        }

        public void visitElement(XElement element) {
            String style = null;
            if(XElementType.isString(element)){
                style = getElementStringNodeStyle();
            }
            else if(XElementType.isNumeric(element)){
                style = getElementNumericNodeStyle();
            }
            else if(XElementType.isConsolidated(element)) {
            	style = getElementNodeStyle();
            }
            setResult(new LabeledImage(style, element.getName()));
        }

        public void visitRoot(XRoot root) {
        }

        public void visitServer(XServer server) {
            String style = getServerNodeStyle();
            
            String text = server.getDispName();
            if(text == null){
            	text = server.getHost();
            	text += "/"+server.getService();
            }
            setResult(new LabeledImage(style, text));		
        }

        public void visitSubset(XSubset subset) {
            String style = getSubsetNodeStyle();
            setResult(new LabeledImage(style, subset.getName()));
        }

        public void visitView(XView view) {
            String style = getViewNodeStyle();
            setResult(new LabeledImage(style, view.getName()));
        }

		public void visitElementNode(XElementNode node) {
			XElement element = node.getElement();
            String style = null;
            if(XElementType.isString(element)){
                style = getElementStringNodeStyle();
            }
            else if(XElementType.isNumeric(element)){
                style = getElementNumericNodeStyle();
            }
            else if(XElementType.isConsolidated(element)) {
            	style = getElementNodeStyle();
            }
            setResult(new LabeledImage(style, node.getName()));

		}

    }

    private final XObjectWidgetFactory xobjectWidgetFactory = new XObjectWidgetFactory();

    public Widget createWidgetFor(Object object) {
        Widget result = null;
        XObject value = typeCastObject(object);

        if(object instanceof FolderNode){
            result = createFolderWidget((FolderNode)object);
        }
        else {
            xobjectWidgetFactory.visit(value);
            result = xobjectWidgetFactory.getResult();
        } 

        if(result == null)//default
            result =  super.createWidgetFor(object);
        return result;
    }

	private Widget createFolderWidget(FolderNode node) {
		Widget result;
		String style = getFolderNodeStyle();
		result =  new LabeledImage(style, node.getFolderName());
		return result;
	}

	private XObject typeCastObject(Object object) {
		XObject value = null;
        if(object instanceof PaloTreeNode)
            value = ((PaloTreeNode)object).getXObject();
        else if(object instanceof XObject)
            value = (XObject)object;
		return value;
	}

    protected String getDatabaseNodeStyle() { 
        return "tensegrity-gwt-tree-node-database";
    }

    protected String getServerNodeStyle() { 
        return "tensegrity-gwt-tree-node-server";
    }

    protected String getDimensionNodeStyle() { 
        return "tensegrity-gwt-tree-node-dimension";
    }

    protected String getCubeNodeStyle() { 
        return "tensegrity-gwt-tree-node-cube";
    }

    protected String getSubsetNodeStyle() { 
        return "tensegrity-gwt-tree-node-subset";
    }

    protected String getElementNodeStyle() { 
        return "tensegrity-gwt-tree-node-element";
    }

    protected String getElementStringNodeStyle() { 
        return "tensegrity-gwt-tree-node-element-string";
    }

    protected String getElementNumericNodeStyle() { 
        return "tensegrity-gwt-tree-node-element-numeric";
    }

    protected String getViewNodeStyle() { 
        return "tensegrity-gwt-tree-node-view";
    }

    protected String getFolderNodeStyle() { 
        return "tensegrity-gwt-tree-node-folder";
    }

}
