/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.constants;

/**
 * Defines all SpagoBI's constants.
 * 
 * @author sulis
 */
public class SpagoBIConstants {

	public static final String EXECUTION_MODALITY = "EXECUTION_MODALITY";
	public static final String TEST_MODALITY = "TEST_MODALITY";
	public static final String DEVELOPMENT_MODALITY = "DEVELOPMENT_MODALITY";
	public static final String ADMIN_MODALITY = "ADMIN_MODALITY";
	public static final String OBJECT_TREE_MODALITY = "OBJECT_TREE_MODALITY";
	
	public static final String ACTOR = "ACTOR";
	public static final String TESTER_ACTOR = "TESTER_ACTOR";
	public static final String USER_ACTOR = "USER_ACTOR";
	public static final String ADMIN_ACTOR = "ADMIN_ACTOR";
	public static final String DEV_ACTOR = "DEV_ACTOR";
	
	public static final String OPERATION = "OPERATION";
	public static final String FUNCTIONALITIES_OPERATION = "FUNCTIONALITIES_OPERATION";

	public static final String OBJECTS_VIEW = "OBJECTS_VIEW";
	public static final String VIEW_OBJECTS_AS_LIST = "VIEW_OBJECTS_AS_LIST";
	public static final String VIEW_OBJECTS_AS_TREE = "VIEW_OBJECTS_AS_TREE";
	
	public static final String LIST_PAGE = "LIST_PAGE";
	
	public static final String MODALITY = "MODALITY";
	public static final String MESSAGEDET = "MESSAGEDET";
	
	public static final String DETAIL_SELECT = "DETAIL_SELECT";
	public static final String DETAIL_NEW = "DETAIL_NEW";
	public static final String DETAIL_MOD = "DETAIL_MOD";
	public static final String DETAIL_INS = "DETAIL_INS";
	public static final String DETAIL_DEL = "DETAIL_DEL";
	public static final String DETAIL_INS_WIZARD_QUERY = "DETAIL_INS_WIZARD_QUERY";
	public static final String DETAIL_MOD_WIZARD_QUERY = "DETAIL_MOD_WIZARD_QUERY";
	public static final String DETAIL_VIEW_WIZARD_QUERY = "DETAIL_VIEW_WIZARD_QUERY";
	public static final String DETAIL_INS_WIZARD_SCRIPT = "DETAIL_INS_WIZARD_SCRIPT";
	public static final String DETAIL_MOD_WIZARD_SCRIPT = "DETAIL_MOD_WIZARD_SCRIPT";
	public static final String DETAIL_VIEW_WIZARD_SCRIPT = "DETAIL_VIEW_WIZARD_SCRIPT";
	public static final String DETAIL_VIEW_WIZARD_SCRIPT_AFTER_TEST = "DETAIL_VIEW_WIZARD_SCRIPT_AFTER_TEST";
	public static final String DETAIL_ADD_WIZARD_LOV = "DETAIL_ADD_WIZARD_LOV";
	public static final String DETAIL_DEL_WIZARD_LOV = "DETAIL_DEL_WIZARD_LOV";
	public static final String DETAIL_INS_WIZARD_FIX_LOV = "DETAIL_INS_WIZARD_FIX_LOV";
	public static final String DETAIL_MOD_WIZARD_FIX_LOV = "DETAIL_MOD_WIZARD_FIX_LOV";
	public static final String DETAIL_VIEW_WIZARD_FIX_LOV = "DETAIL_VIEW_WIZARD_FIX_LOV";
	
	public static final String LIST_INPUT_TYPE = "LIST_INPUT_TYPE";
	public static final String MODALITY_VALUE_OBJECT = "MODALITY_VALUE_OBJ";

	public static final String WIZARD = "WIZARD";
	public static final String WIZARD_QUERY = "WIZARD_QUERY";
	public static final String WIZARD_FIX_LOV = "WIZARD_FIX_LOV";
	public static final String WIZARD_SCRIPT = "WIZARD_SCRIPT";
	
	public static final String ID_MODALITY_VALUE = "ID_MODALITY_VALUE";

	public static final String NAME_MODULE = "SpagoBI";
	
	public static final String TYPE_FILTER = "typeFilter";
	public static final String VALUE_FILTER = "valueFilter";
	public static final String COLUMN_FILTER = "columnFilter";
	public static final String START_FILTER = "start";
	public static final String END_FILTER = "end";
	public static final String EQUAL_FILTER = "equal";
	public static final String CONTAIN_FILTER= "contain";
	
	public static final String ERASE_VERSION = "ERASE_VERSION";
	
	public static final String VERSION = "VERSION";
	public static final String PATH = "PATH";
	
	public static final String PROFILE_ATTRS = "PROFILE_ATTRS";
	
	public static final String INPUT_TYPE = "INPUT_TYPE";
	public static final String INPUT_TYPE_QUERY_CODE = "QUERY";
	public static final String INPUT_TYPE_FIX_LOV_CODE = "FIX_LOV";
	public static final String INPUT_TYPE_MAN_IN_CODE = "MAN_IN";
	public static final String INPUT_TYPE_SCRIPT_CODE = "SCRIPT";
	
	
	public static final String BIOBJECT_TYPE_CODE = "BIOBJECT_TYPE_CODE";
	public static final String REPORT_TYPE_CODE = "REPORT";
	public static final String DATAMART_TYPE_CODE = "DATAMART";
	public static final String OLAP_TYPE_CODE = "OLAP";
	public static final String DATA_MINING_TYPE_CODE = "DATA_MINING";
	public static final String DASH_TYPE_CODE = "DASH";
	public static final String LOW_FUNCTIONALITY_TYPE_CODE = "LOW_FUNCT";
	
	public static final String SUBOBJECT_LIST = "SUBOBJECT_NAMES_LIST";
	
	public static final String ROLE = "ROLE";
	
	public static final String SINGLE_OBJECT_EXECUTION_MODALITY = "SINGLE_OBJECT_EXECUTION_MODALITY";
	public static final String FILTER_TREE_MODALITY = "FILTER_TREE_MODALITY";
	public static final String ENTIRE_TREE_MODALITY = "ENTIRE_TREE_MODALITY";
	
	public static final String HEIGHT_OUTPUT_AREA = "HEIGHT_OUTPUT_AREA";
	
	public static final String PUBLISHER_NAME = "PUBLISHER_NAME";
	public static final String PUBLISHER_LOOPBACK_AFTER_DEL_SUBOBJECT = "loopbackAfterSubObjectDeletion";
	public static final String PUBLISHER_LOOPBACK_SINGLE_OBJECT_EXEC = "loopbackSingleObjectExecution";
	
	/**
	 * if the response has an attribute with key RESPONSE_COMPLETE valued
	 * "true" it means that the response is complete for a correct visualization
	*/
	public static final String RESPONSE_COMPLETE = "RESPONSE_COMPLETE"; 

}
