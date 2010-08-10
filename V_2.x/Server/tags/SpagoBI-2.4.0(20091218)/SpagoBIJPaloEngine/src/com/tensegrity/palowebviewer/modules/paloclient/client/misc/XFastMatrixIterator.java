package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import java.util.List;


public class XFastMatrixIterator implements IMatrixIterator{
	
	private XResult result;
	
	private List xDimensions, yDimensions;
	private XQueryPath query;
	private final ElementIterator xElementIterator;
	private final ElementIterator yElementIterator;
	private final boolean orderReverted;

	public XFastMatrixIterator(XQueryPath query, XResult result, List xDimensions, List yDimensions) {
		this.xDimensions = xDimensions;
		this.yDimensions = yDimensions;
		this.query = query;
		this.result = result;
		assertSimpleRequest();
		XPath xDimPath = (XPath)xDimensions.get(xDimensions.size()-1);
		this.xElementIterator = new ElementIterator(query,xDimPath);
		XPath yDimPath = (XPath)yDimensions.get(yDimensions.size()-1);
		this.yElementIterator = new ElementIterator(query,yDimPath);
		List dimensions = query.getDimensions();
		int xDimIndex = dimensions.indexOf(xDimPath);
		int yDimIndex = dimensions.indexOf(yDimPath);
		int[] indexMapping = result.getIndexMapping();
		orderReverted = indexMapping[xDimIndex]>indexMapping[yDimIndex];
	}

	private void assertSimpleRequest() {
		int xSize = xDimensions.size();
		for(int i = xSize-2; i>=0; i--){
			assertHasOneElement((XPath)xDimensions.get(i));
		}
		int ySize = yDimensions.size();
		for(int i = ySize-2; i>=0; i--){
			assertHasOneElement((XPath)yDimensions.get(i));
		}
	}

	private void assertHasOneElement(XPath path) {
		int size = query.getRequestedElements(path).size();
		if(size != 1) {
			String message = "XFastMatrixIterator can not handle complex requests.";
			message += " Dimension "+path+" have to have only 1 element requested.";
			throw new IllegalArgumentException(message);
		}
	}

	public boolean hasMorePoints(){
		return yElementIterator.hasNext()|| xElementIterator.hasNext();
	}
	
	public int getX(){
		return xElementIterator.getIndex();
	}
	
	public int getY(){
		return yElementIterator.getIndex();
	}
	
	public IResultElement getValue(){
		int x = xElementIterator.getResultIndex();
		int y = yElementIterator.getResultIndex();
		
		
		int resultIndex = 0;
		if(orderReverted){
			int ySize = yElementIterator.resultSize();
			resultIndex = x*ySize + y;
		}
		else {
			int xSize = xElementIterator.resultSize();
			resultIndex = y*xSize + x;
		}
			
		return result.get(resultIndex);
	}
	
	public int sizeX(){
		return xElementIterator.size();
	}
	
	public int sizeY(){
		return yElementIterator.size();
	}
	
	public void next(){
		xElementIterator.next();
		if(getX()==0){
			yElementIterator.next();
		}
	}

	public final XQueryPath getQuery() {
		return query;
	}
	
}

class ElementIterator {
	
	private final List requestedElements;
	private final List elements;
	private final int size;
	private final int resultSize;
	private int index;
	private XPath elementPath;

	ElementIterator (XQueryPath query, XPath dimPath) {
		this.elements = query.getPoinstPath(dimPath);
		this.requestedElements = query.getRequestedElements(dimPath);
		this.size = requestedElements.size();
		this.resultSize = elements.size();
		index = -1;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getResultIndex() {
		return elements.indexOf(elementPath);
	}
	
	public void next() {
		index++;
		if(index==size) {
			index = 0;
		}
		elementPath = (XPath)requestedElements.get(index);
	}
	
	public int size () {
		return size;
	}
	
	public int resultSize () {
		return resultSize;
	}
	
	public boolean hasNext() {
		return index < (size-1);
	}
	
}
