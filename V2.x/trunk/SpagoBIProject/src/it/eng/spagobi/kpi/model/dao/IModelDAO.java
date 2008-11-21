package it.eng.spagobi.kpi.model.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.kpi.model.bo.Model;

public interface IModelDAO {
	
	/**
	 * Returns the Model without its children of the referred id
	 * 
	 * @param id of the Model
	 * @return Model of the referred id
	 * @throws EMFUserError If an Exception occurred
	 */	
	public Model loadModelWithoutChildrenById(Integer id) throws EMFUserError;
	
	/**
	 * Modify model.
	 * 
	 * @param aModel the a model
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 */
	public void modifyModel(Model aModel) throws EMFUserError;

	/**
	 * Insert a model and return the new model.
	 * 
	 * @param aModel the model to create
	 * @param modelTypeId the id of the type of the model
	 * @return the id of the model created
	 * @throws EMFUserError the EMF user error
	 */
	public Integer insertModel(Model aModel, Integer modelTypeId) throws EMFUserError;
}
