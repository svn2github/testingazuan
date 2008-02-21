package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XConsolidatedElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.TypeCastVisitor;

/**
 * Helper class that is used to retirve children of some type of the object without knowing the different methods for this.
 * Primary method of the class is {@link ChildrenGetter#getChildren(XObject, int)}.
 *
 */
/**
 * @author sonic
 *
 */
public class ChildrenGetter extends TypeCastVisitor {

	private XObject[] result;

	private int type;

	/**
	 * Returns Children of the Object of tahe type.
	 * @param object - object to take children of
	 * @param type - type of children to get (the constants are teken form {@link IXConsts}).
	 * @return	- an array of children of the <code>object</code> of type <code>type</code>.
	 */
	public XObject[] getChildrenArray(XObject object, int type) {
		this.type = type;
		result = null;
		visit(object);
		return result;
	}
	/**
	 * Returns Children of the Object of tahe type.
	 * @param object - object to take children of
	 * @param type - type of children to get (the constants are teken form {@link IXConsts}).
	 * @return	- an array of children of the <code>object</code> of type <code>type</code>.
	 */
	
	public static XObject[] getChildren(XObject object, int type) {
		return new ChildrenGetter().getChildrenArray(object, type);
	}	

	/**
	 * {@inheritDoc}
	 */
	public void visitAxis(XAxis axis) {
		if (type == IXConsts.TYPE_DIMENSION) {
			result = axis.getDimensions();
		}
		if (type == IXConsts.TYPE_SUBSET) {
			result = axis.getSubsets();
		}
		if (type == IXConsts.TYPE_ELEMENT) {
			result = axis.getSelectedElements();
		}
	}
	/**
	 * {@inheritDoc}
	 */

	public void visitConsolidatedElement(
			XConsolidatedElement consolidatedElement) {
		throw new RuntimeException(
				"there is no children in consolidatedElement");
	}
	/**
	 * {@inheritDoc}
	 */

	public void visitCube(XCube cube) {
		if (type == TYPE_DIMENSION) {
			result = cube.getDimensions();
		} else if (type == TYPE_VIEW) {
			result = cube.getViews();
		}
	}
	/**
	 * {@inheritDoc}
	 */

	public void visitDatabase(XDatabase database) {
		if (type == IXConsts.TYPE_DIMENSION) {
			result = database.getDimensions();// ??????
		} else if (type == IXConsts.TYPE_CUBE) {
			result = database.getCubes();
		}
	}
	/**
	 * {@inheritDoc}
	 */

	public void visitDimension(XDimension dimension) {
		if (type == IXConsts.TYPE_ELEMENT_NODE) {
			result = dimension.getNodes();// ??????
		} else if (type == IXConsts.TYPE_SUBSET) {
			result = dimension.getSubsets();
		}
	}
	/**
	 * {@inheritDoc}
	 */

	public void visitElement(XElement element) {

	}

	/**
	 * {@inheritDoc}
	 */
	public void visitElementNode(XElementNode node) {
		if (type == IXConsts.TYPE_ELEMENT_NODE) {
			result = node.getChildren();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visitRoot(XRoot root) {
		if (type == IXConsts.TYPE_SERVER)
			result = root.getServers();
	}

	/**
	 * {@inheritDoc}
	 */
	public void visitServer(XServer server) {
		if (type == IXConsts.TYPE_DATABASE) {
			result = server.getDatabases();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visitSubset(XSubset subset) {
		if (type == IXConsts.TYPE_ELEMENT_NODE) {
			result = subset.getNodes();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visitView(XView view) {
		if (type == IXConsts.TYPE_AXIS) {
			result = view.getAxises();// ????
		}
	}

}