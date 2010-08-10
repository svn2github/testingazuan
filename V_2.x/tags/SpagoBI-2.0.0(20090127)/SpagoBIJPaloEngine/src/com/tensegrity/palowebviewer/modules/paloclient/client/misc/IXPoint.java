package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import java.util.List;

/**
 *
 *  This interface wraps single point returned by JPaloApi (cube.getDataArray()).  
 *
 */
public interface IXPoint {
	
	/**
	 * @return value of point
	 */
	public IResultElement getValue();
	
	/**
	 * To get all element coordinates iterate 
	 * 	via @see {@link com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath#getDimensions()}.
	 * @param dimensionPath - interesting dimension path
	 * @return XPath to current point at current dimension. 
	 */
	public XPath getElementPath(XPath dimensionPath);
	
	/** 
	 * @return dimensions Xpath list in order they added
	 */
	public List getDimensions();

}
