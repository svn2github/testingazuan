package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;

/**
 * Class with stub for methods of {@link IPaloServerModelListener} interface.
 *
 */
public class AbstractServerModelListener implements IPaloServerModelListener {

	/**
	 * {@inheritDoc}
	 */
	public void modelChanged() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onChildArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onError(Throwable cause) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onObjectLoaded(XObject[] path, XObject object) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onUpdateComplete() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void defaultViewLoaded(XCube cube) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void objectRenamed(XObject object) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onFavoriteViewsLoaded() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void objectInvalide(XPath path) {
	}

}
