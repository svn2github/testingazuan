package com.tensegrity.palowebviewer.modules.paloclient.client;


public interface IXConsts{
	
	public static final int TYPE_ROOT 					= 1;
    public static final int TYPE_SERVER 				= 2;
	public static final int TYPE_DATABASE 				= 3;
	public static final int TYPE_CUBE 				 	= 4;
	public static final int TYPE_DIMENSION 				= 5;
    public static final int TYPE_ELEMENT 				= 6;
    public static final int TYPE_CONSOLIDATED_ELEMENT 	= 7;
    public static final int TYPE_VIEW 					= 8;
    public static final int TYPE_SUBSET 				= 9;
	public static final int TYPE_AXIS 				 	= 10;
	public static final int TYPE_ELEMENT_NODE 			= 11;
	public static final int MAX_TYPE_ID     			= 11;
	
	public static final String TYPE_NAME_ROOT 					="Root";
    public static final String TYPE_NAME_SERVER 				="Server";
	public static final String TYPE_NAME_DATABASE 				="Database";
	public static final String TYPE_NAME_CUBE 				 	="Cube";
	public static final String TYPE_NAME_DIMENSION 				="Dimension";
    public static final String TYPE_NAME_ELEMENT 				="Element";
    public static final String TYPE_NAME_CONSOLIDATED_ELEMENT 	="ConsolidatedElement";
    public static final String TYPE_NAME_VIEW 					="View";
    public static final String TYPE_NAME_SUBSET 				="Subset";
	public static final String TYPE_NAME_AXIS 				 	="Axis";
	public static final String TYPE_NAME_ELEMENT_NODE 			="ElementNode";
	
	public static final String XPATH_FIELD_SEPARATOR = ":";
	public static final String XPATH_SEPARATOR = "/";

}
