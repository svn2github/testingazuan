package it.eng.spagobi.studio.core.bo;

import it.eng.spagobi.sdk.datasets.bo.SDKDataStoreMetadata;

import java.util.HashMap;

public class DataStoreMetadata {

	private DataStoreMetadataField[] fieldsMetadata;

	private HashMap properties;

	public DataStoreMetadata() {
	}

	public DataStoreMetadata(
			DataStoreMetadataField[] fieldsMetadata,
			java.util.HashMap properties) {
		this.fieldsMetadata = fieldsMetadata;
		this.properties = properties;
	}

	public DataStoreMetadata(SDKDataStoreMetadata sdkDataStoreMetadata) {
		if(sdkDataStoreMetadata==null || sdkDataStoreMetadata.getFieldsMetadata()==null) return;
		for (int i = 0; i < sdkDataStoreMetadata.getFieldsMetadata().length; i++) {
			fieldsMetadata[i]=new DataStoreMetadataField(sdkDataStoreMetadata.getFieldsMetadata()[i]);
		}
		this.properties = sdkDataStoreMetadata.getProperties();
	}


	/**
	 * Gets the fieldsMetadata value for this SDKDataStoreMetadata.
	 * 
	 * @return fieldsMetadata
	 */
	public DataStoreMetadataField[] getFieldsMetadata() {
		return fieldsMetadata;
	}


	/**
	 * Sets the fieldsMetadata value for this SDKDataStoreMetadata.
	 * 
	 * @param fieldsMetadata
	 */
	public void setFieldsMetadata(DataStoreMetadataField[] fieldsMetadata) {
		this.fieldsMetadata = fieldsMetadata;
	}


	/**
	 * Gets the properties value for this SDKDataStoreMetadata.
	 * 
	 * @return properties
	 */
	public java.util.HashMap getProperties() {
		return properties;
	}


	/**
	 * Sets the properties value for this SDKDataStoreMetadata.
	 * 
	 * @param properties
	 */
	public void setProperties(java.util.HashMap properties) {
		this.properties = properties;
	}


}
