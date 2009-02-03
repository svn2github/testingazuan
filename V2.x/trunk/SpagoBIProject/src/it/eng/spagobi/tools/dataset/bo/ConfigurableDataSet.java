/**
 * 
 */
package it.eng.spagobi.tools.dataset.bo;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.services.dataset.bo.SpagoBiDataSet;
import it.eng.spagobi.tools.dataset.common.behaviour.QuerableBehaviour;
import it.eng.spagobi.tools.dataset.common.dataproxy.AbstractDataProxy;
import it.eng.spagobi.tools.dataset.common.dataproxy.IDataProxy;
import it.eng.spagobi.tools.dataset.common.datareader.IDataReader;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia
 */
public class ConfigurableDataSet extends  AbstractDataSet {

	IDataReader dataReader;
	IDataProxy dataProxy;
	IDataStore dataStore;

	Object query;	


	IEngUserProfile userProfile;


	private static transient Logger logger = Logger.getLogger(ConfigurableDataSet.class);


	public ConfigurableDataSet(){
		super();
	}

	public ConfigurableDataSet(SpagoBiDataSet dataSetConfig){
		super(dataSetConfig);
	}

	public void loadData() throws EMFUserError, EMFInternalError{

		((AbstractDataProxy)dataProxy).bindParameters((HashMap)getParamsMap());
		((AbstractDataProxy)dataProxy).setProfile(getUserProfile());


		if( hasBehaviour(QuerableBehaviour.class.getName()) ) { // Querable Behaviour
			QuerableBehaviour querableBehaviour = (QuerableBehaviour)getBehaviour(QuerableBehaviour.class.getName()) ;
			dataStore = dataProxy.load( querableBehaviour.getStatement(), dataReader);    		
		} 
		else{
			dataStore = dataProxy.load(dataReader); 
		}

		if(hasDataStoreTransformer()) {
			getDataStoreTransformer().transform(dataStore);
		}
	}

	public void setFetchSize(int l) {
		throw new UnsupportedOperationException("metothd setFetchSize not yet implemented");
	}

	public IDataStore fetchNext() {    	
		IDataStore dataStore = null;
		throw new UnsupportedOperationException("metothd fetchNext not yet implemented");
		//return dataStore;
	}    

	public IDataStore getDataStore() {    	
		return this.dataStore;
	}    

	public Object getQuery() {
		return query;
	}

	public void setQuery(Object query) {
		this.query = query;
	}

	/**
	 * Gets the list of names of the profile attributes required.
	 * 
	 * @return list of profile attribute names
	 * 
	 * @throws Exception the exception
	 */
	public List getProfileAttributeNames() throws Exception {
		List names = new ArrayList();
		String query = (String)getQuery();
		while(query.indexOf("${")!=-1) {
			int startind = query.indexOf("${");
			int endind = query.indexOf("}", startind);
			String attributeDef = query.substring(startind + 2, endind);
			if(attributeDef.indexOf("(")!=-1) {
				int indroundBrack = query.indexOf("(", startind);
				String nameAttr = query.substring(startind+2, indroundBrack);
				names.add(nameAttr);
			} else {
				names.add(attributeDef);
			}
			query = query.substring(endind);
		}
		return names;
	}

	public IDataReader getDataReader() {
		return dataReader;
	}

	public void setDataReader(IDataReader dataReader) {
		this.dataReader = dataReader;
	}

	public IDataProxy getDataProxy() {
		return dataProxy;
	}

	public void setDataProxy(IDataProxy dataProxy) {
		this.dataProxy = dataProxy;
	}

	public IEngUserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(IEngUserProfile userProfile) {
		this.userProfile = userProfile;
	}

}
