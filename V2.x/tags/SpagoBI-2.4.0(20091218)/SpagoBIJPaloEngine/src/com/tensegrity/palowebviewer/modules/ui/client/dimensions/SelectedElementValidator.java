package com.tensegrity.palowebviewer.modules.ui.client.dimensions;

import com.tensegrity.palowebviewer.modules.engine.client.AbstractServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IEngine;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IVerificationCallback;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserCallback;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessage;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.ui.client.tree.ElementNodeNode;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.ISetter;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.IValidator;

public class SelectedElementValidator implements IValidator, IVerificationCallback {
	
	private XElement element;
	private ISetter setter;
	private final IEngine engine;
	private final ITreeModel treeModel;
	private XObject contextObject;
	
	private final IPaloServerModelListener serverModelListener = new AbstractServerModelListener() {

		public void onChildArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
			setDefaultValue();
		}
		
	};
	private IUserCallback invalidItemCallback;
	private boolean checking = false;
	
	public SelectedElementValidator(IEngine engine, ITreeModel treeModel, IUserCallback invalidItemCallback) {
		this.engine = engine;
		this.treeModel = treeModel;
		this.invalidItemCallback = invalidItemCallback;
	}
	
	public void validateAndSet(Object value, ISetter setter) {
		if(checking) {
			return;
		}
		if(setter == null) {
			throw new IllegalArgumentException("Setter can not be null.");
		}
		this.setter = setter;
		
		if (value instanceof ElementNodeNode) {
			ElementNodeNode node = (ElementNodeNode) value;
			setValue(node.getElement());
			
			
		}
		else if(value instanceof XElement) {
			checkAndSetElement((XElement)value);
		}
		else if(value == null) {
			setDefaultValue();
		}
	}

	public void checkAndSetElement(XElement element) {
		checking = true;
		this.element = element;
		IPaloServerModel serverModel = getServerModel();
		contextObject = getRootObject();
		if(contextObject instanceof XDimension){
			serverModel.checkElement((XDimension)contextObject, element, this);
		}
		else if(contextObject instanceof XSubset){
			serverModel.checkElement((XSubset)contextObject, element, this);
		}
	}

	private XObject getRootObject() {
		PaloTreeNode rootNode = (PaloTreeNode)treeModel.getRoot();
		return rootNode.getXObject();
	}

	public void fail() {
		checking = false;
		Logger.debug("Verification of element '"+element.getName()+"' fail.");
		setDefaultValue();
		IUserMessage msg = new InvalidSelectedElementMessage(contextObject, element, invalidItemCallback);

		engine.getUserMessageQueue().pushMessage(msg);
	}

	private void setDefaultValue() {
		IPaloServerModel serverModel = getServerModel();
		serverModel.removeListener(serverModelListener);
		XElementNode[] nodes = null;
		XObject parent = getRootObject();
		if(parent instanceof XSubset){
			XSubset subset = (XSubset)parent;
			nodes = subset.getNodes();
		}
		else if(parent instanceof XDimension){
			XDimension dimension = (XDimension)parent;
			nodes = dimension.getNodes();
		}
		if(nodes != null) {
			if(nodes.length >0) {
				setValue(nodes[0].getElement());
			}
			else {
				setValue(null);
			}
		}
		else { 
			
			serverModel.addListener(serverModelListener);
			serverModel.loadChildren(parent, IXConsts.TYPE_ELEMENT_NODE);
		}
	}

	private IPaloServerModel getServerModel() {
		return engine.getPaloServerModel();
	}

	public void successed() {
		checking = false;
		Logger.debug("Verification of element '"+element.getName()+"' successeded.");
		setValue(element);
	}

	private void setValue(XElement element) {
		if(setter != null) {
			String elementName = null;
			if(element != null) {
				elementName = "'"+element.getName()+"'";
			}
			Logger.debug("Set selected element to "+ elementName+".");
			setter.setValue(element);
		}
	}
	
}