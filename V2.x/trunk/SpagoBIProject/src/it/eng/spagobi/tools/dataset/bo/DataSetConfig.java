/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.dataset.bo;

import org.apache.log4j.Logger;


import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.services.dataset.bo.SpagoBiDataSet;


/**
 * @author Angelo Bernabei angelo.bernabei@eng.it
 */
public class DataSetConfig {

    private static transient Logger logger = Logger.getLogger(DataSetConfig.class);
    private int dsId;
    private Integer transformerId = null;
    private String name = null;
    private String description = null;
    private String label = null;
    private String pivotColumnName = null;
    private String pivotRowName = null;
    private String pivotColumnValue = null;
    private String parameters = null;

    public DataSetConfig() {
	super();
	// TODO Auto-generated constructor stub
    }

    public static DataSetConfig createDataSet(SpagoBiDataSet ds) {

	if (ds != null) {
	    String type = ds.getType();
	    if (type.equals("SbiScriptDataSet")) {
			ScriptDataSet dsc = new ScriptDataSet();
			dsc.setDescription(ds.getDescription());
			dsc.setLabel(ds.getLabel());
			dsc.setName(ds.getName());
			dsc.setParameters(ds.getParameters());
			dsc.setScript(ds.getScript());
			dsc.setTransformerId(ds.getTransformerId());
			dsc.setPivotColumnName(ds.getPivotColumnName());
			dsc.setPivotRowName(ds.getPivotRowName());
			dsc.setPivotColumnValue(ds.getPivotColumnValue());
			
			return dsc;
	    } else if (type.equals("SbiQueryDataSet")) {
			QueryDataSet dsc = new QueryDataSet();
			dsc.setDescription(ds.getDescription());
			dsc.setLabel(ds.getLabel());
			dsc.setName(ds.getName());
			dsc.setParameters(ds.getParameters());
			dsc.setQuery(ds.getQuery());
			dsc.setTransformerId(ds.getTransformerId());
			dsc.setPivotColumnName(ds.getPivotColumnName());
			dsc.setPivotRowName(ds.getPivotRowName());
			dsc.setPivotColumnValue(ds.getPivotColumnValue());
			return dsc;
	    } else if (type.equals("SbiJClassDataSet")) {
			JClassDataSet dsc = new JClassDataSet();
			dsc.setDescription(ds.getDescription());
			dsc.setLabel(ds.getLabel());
			dsc.setName(ds.getName());
			dsc.setParameters(ds.getParameters());
			ds.setJavaClassName(ds.getJavaClassName());
			dsc.setTransformerId(ds.getTransformerId());
			dsc.setPivotColumnName(ds.getPivotColumnName());
			dsc.setPivotRowName(ds.getPivotRowName());
			dsc.setPivotColumnValue(ds.getPivotColumnValue());
			return dsc;
	    } else if (type.equals("SbiWSDataSet")) {
			WSDataSet dsc = new WSDataSet();
			dsc.setDescription(ds.getDescription());
			dsc.setLabel(ds.getLabel());
			dsc.setName(ds.getName());
			dsc.setParameters(ds.getParameters());
			dsc.setOperation(ds.getOperation());
			dsc.setExecutorClass(ds.getExecutorClass());
			dsc.setTransformerId(ds.getTransformerId());
			dsc.setPivotColumnName(ds.getPivotColumnName());
			dsc.setPivotRowName(ds.getPivotRowName());
			dsc.setPivotColumnValue(ds.getPivotColumnValue());
			return dsc;
	    } else if (type.equals("SbiFileDataSet")) {
			FileDataSet dsc = new FileDataSet();
			dsc.setDescription(ds.getDescription());
			dsc.setLabel(ds.getLabel());
			dsc.setName(ds.getName());
			dsc.setParameters(ds.getParameters());
			dsc.setFileName(ds.getFileName());
			dsc.setTransformerId(ds.getTransformerId());
			dsc.setPivotColumnName(ds.getPivotColumnName());
			dsc.setPivotRowName(ds.getPivotRowName());
			dsc.setPivotColumnValue(ds.getPivotColumnValue());
			return dsc;
	    }
	}
	return null;
    }

    public SpagoBiDataSet toSpagoBiDataSet() {
	return null;
    }

    /**
     * Gets the ds id.
     * 
     * @return the ds id
     */
    public int getDsId() {
	return dsId;
    }

    /**
     * Sets the ds id.
     * 
     * @param dsId
     *                the new ds id
     */
    public void setDsId(int dsId) {
	this.dsId = dsId;
    }

    /**
     * Gets the parameters.
     * 
     * @return the parameters
     */
    public String getParameters() {
	return parameters;
    }

    /**
     * Sets the parameters.
     * 
     * @param parameters
     *                the new parameters
     */
    public void setParameters(String parameters) {
	this.parameters = parameters;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *                the new name
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * Sets the description.
     * 
     * @param description
     *                the new description
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Gets the label.
     * 
     * @return the label
     */
    public String getLabel() {
	return label;
    }

    /**
     * Sets the label.
     * 
     * @param label
     *                the new label
     */
    public void setLabel(String label) {
	this.label = label;
    }

    /**
     * Gets the data set result.
     * 
     * @param profile
     *                the profile
     * 
     * @return the data set result
     * 
     * @throws Exception
     *                 the exception
     */
    public String getDataSetResult(IEngUserProfile profile) throws Exception {
	return null;
    }

    /**
     * Gets the pivot column name.
     * 
     * @return the pivot column name
     */
	public String getPivotColumnName() {
		return pivotColumnName;
	}
    /**
     * Sets the pivot column name.
     * 
     * @param pivotColumnName the new pivot column name
     */
	public void setPivotColumnName(String pivotColumnName) {
		this.pivotColumnName = pivotColumnName;
	}

	/**
     * Gets the pivot column value.
     * 
     * @return the pivot column value
     */
	public String getPivotColumnValue() {
		return pivotColumnValue;
	}

	/**
     * Sets the pivot column value.
     * 
     * @param pivotColumnValue the new pivot column value
     */
	public void setPivotColumnValue(String pivotColumnValue) {
		this.pivotColumnValue = pivotColumnValue;
	}

	public String getPivotRowName() {
		return pivotRowName;
	}

	public void setPivotRowName(String pivotRowName) {
		this.pivotRowName = pivotRowName;
	}

	public Integer getTransformerId() {
		return transformerId;
	}

	public void setTransformerId(Integer transformerId) {
		this.transformerId = transformerId;
	}

}
