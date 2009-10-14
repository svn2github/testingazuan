package com.tensegrity.palowebviewer.modules.ui.client;

import com.tensegrity.palowebviewer.modules.engine.client.IEngine;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel;


public class XEditorFactory implements IXEditorFactory
{
    private final PaloTreeModel treeModel;
	private IEngine engine;

    public IXObjectEditor getEditor(XObject object) throws IllegalArgumentException{
        if(object == null)
            throw new IllegalArgumentException("XObject can not be null.");
        IXObjectEditor result = null;
        if(object instanceof XCube) {
            result = new XCubeEditor(engine, treeModel, (XCube)object);
        }
        else if(object instanceof XView){
        	XView view = (XView) object;
        	result = new XCubeEditor(engine, treeModel, (XCube)view.getParent(), view);
        }
        else{
            throw new IllegalArgumentException("XObject must be of type XCube.");
        }
        return result;
    }

    public XEditorFactory (IEngine engine, PaloTreeModel treeModel) {
        this.engine = engine;
        this.treeModel = treeModel;
    }

}
