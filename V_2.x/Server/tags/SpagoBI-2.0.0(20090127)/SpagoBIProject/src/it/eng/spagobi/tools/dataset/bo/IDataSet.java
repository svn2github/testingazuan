package it.eng.spagobi.tools.dataset.bo;

import java.util.Map;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.services.dataset.bo.SpagoBiDataSet;
import it.eng.spagobi.tools.dataset.common.behaviour.IDataSetBehaviour;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.transformer.IDataStoreTransformer;
import it.eng.spagobi.tools.dataset.common.transformer.IDataTransformer;

public interface IDataSet {

	
	void loadData() throws EMFUserError, EMFInternalError;
    IDataStore getDataStore();
    IDataStore fetchNext();
    void setFetchSize(int l);

	
	int getId();
	void setId(int id);

	String getParameters();
	void setParameters(String parameters);

	
	String getName();
	void setName(String name);

	String getDescription();
	void setDescription(String description);
	
	String getLabel();
	void setLabel(String label);
	
	Integer getTransformerId();
	void setTransformerId(Integer transformerId);

	String getPivotColumnName();
	void setPivotColumnName(String pivotColumnName);

	String getPivotRowName();
	void setPivotRowName(String pivotRowName);

	String getPivotColumnValue();
	void setPivotColumnValue(String pivotColumnValue);
	
	Object getQuery();
	void setQuery(Object query);
	
	Map getParamsMap();
	void setParamsMap(Map params);

	
	
	boolean hasBehaviour(String behaviourId);
	Object getBehaviour(String behaviourId);
	void addBehaviour(IDataSetBehaviour behaviour);
	

	public boolean hasDataStoreTransformer() ;
	
	public void removeDataStoreTransformer() ;

	public void setDataStoreTransformer(IDataStoreTransformer transformer);
	
	public IDataStoreTransformer getDataStoreTransformer();
    
	
	public IEngUserProfile getUserProfile();

	public void setUserProfile(IEngUserProfile userProfile);
	
	public SpagoBiDataSet toSpagoBiDataSet();
}