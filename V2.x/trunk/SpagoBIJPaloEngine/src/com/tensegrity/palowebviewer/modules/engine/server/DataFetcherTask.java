/**
 * 
 */
package com.tensegrity.palowebviewer.modules.engine.server;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XResult;

class DataFetcherTask extends DbTask {

	 private XQueryPath query;
	 private XResult result;

	 public DataFetcherTask() {
	 }

	 public void task()  throws InvalidObjectPathException{
		result = getAccessor().fetch(query, getConnection());
	}

	public XQueryPath getQuery() {
		return query;
	}

	public void setQuery(XQueryPath query) {
		this.query = query;
	}

	protected String getServer() {
		return query.getCubePath().getServer().getName();
	}

	public XResult getResult() {
		return result;
	}
}