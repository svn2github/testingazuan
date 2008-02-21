package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

public interface IMatrixIterator {

	public boolean hasMorePoints();

	public int getX();

	public int getY();

	public IResultElement getValue();

	public int sizeX();

	public int sizeY();

	public void next();

	public XQueryPath getQuery();

}