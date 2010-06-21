package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;



/**
 * 
 * This interface wraps Object[] returned by JPalo API to iterate it handy.
 *
 */
public class XResultIterator implements Iterator {
	
	private XQueryPath query;
	private XResult result;
	//index in flat result, returned by JPalo API
	private int resultIndex = 0;
	//sizes (number of elements) of each dimension. Dimension order is server side, e.g. same as returned by Cube.getDimensions() in JPalo API 
	private int[] dimensionSizes;

	public XResultIterator(XQueryPath query, XResult result){
		this.query = query;
		this.result = result;
		if(query == null) throw new IllegalArgumentException("query can't be null");
		if(result == null) throw new IllegalArgumentException("result can't be null");
		List dimensions = query.getDimensions();
		int size = dimensions.size();
		dimensionSizes = new int[size];
		//calculating dimension order and sizes 
		for (int i = 0; i < size; ++i) {
			XPath dimensionPath = (XPath) dimensions.get(i);
			List points = query.getPoinstPath(dimensionPath);
			//order
			int index = result.getIndexMapping()[i];
			//size
			dimensionSizes[index] = points.size();
		}
	}
	
	/**
	 * checks if some points are left
	 */
	public boolean hasNext() {
		return resultIndex < result.getResultCount();
	}
	
	/* 
	 * calculating next coordinate to map index in flat Object[] (returned by JPalo API) to element coordinates
	 * 1. determine dimension (index) where coordinate can be incremented (from last to first, according to permutation order)
	 * 2. increment it.
	 * 3. zero others dimension coordinates (from index+1 to last)
	 * 
	 * So, coordinates - is vector, which size equals to number of cube(and query) dimensions);
	 * coordinates - in NUMBER of permutation.
     */
	private void increment(final int[] coordinates){
		int index = -1;
		for (int i = coordinates.length-1; i >= 0 && index == -1; --i) {
			if(coordinates[i] < (dimensionSizes[i]-1)) 
				index = i;
		}
		coordinates[index]++;
		for (int i = index+1; i < coordinates.length; i++) {
			coordinates[i] = 0;
		}
	}
	

	/**
	 * @return IXPoint
	 */
	public Object next() {
		return nextPoint();
	}
	
	/**
	 * @return IXPoint
	 */
	public IXPoint nextPoint() {
		if(!hasNext()) throw new NoSuchElementException();
		final int index = resultIndex++;
		//calculate coordinates for given index
		final int[] coordinates = new int[dimensionSizes.length];
		for(int i = 0; i < index; ++i){
			increment(coordinates);
		}
		IXPoint r = new XPointImpl(coordinates, index); 
		return r;
	}

	/**
	 * Has no sense. Throws UnsupportedOperationException.
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public XQueryPath getQuery(){
		return query;
	}
	
	private class XPointImpl implements IXPoint {
		private final int[] coordinates;
		private final int index;
		
		public XPointImpl(int[] coordinats, int index){
			this.coordinates = coordinats;
			this.index = index;
		}
		
		public XPath getElementPath(XPath dimensionPath) {
			/*
			 * 1. get index of requested dimension path
			 * 2. get index of that dimension in cubes dimension order (server/Jpalo dimension order)
			 * 3. detemite index of coordinate (element)
			 * 4. return coordinate 
			 */
			List dimensions = query.getDimensions();
			int originalIndex = dimensions.indexOf(dimensionPath);
			if(originalIndex == -1) throw new IllegalArgumentException(dimensionPath.toString());
			int resultIndex = result.getIndexMapping()[originalIndex];
			int elementIndex = coordinates[resultIndex];
			List elements = query.getPoinstPath(dimensionPath);
			XPath elementPath = (XPath) elements.get(elementIndex);
			return elementPath;
		}

		public IResultElement getValue() {
			return result.get(index);
		}

		public List getDimensions() {
			return query.getDimensions();
		}
	};

}
