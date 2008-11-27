package it.eng.spagobi.kpi.model.dao;

import java.util.List;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.kpi.model.bo.ModelInstance;

public interface IModelInstanceDAO {

	/**
	 * 
	 * Returns the List of ModelInstance Root.
	 * 
	 * @return List of ModelInstance Root.
	 * @throws EMFUserError If an Exception occurred.
	 */
	public List loadModelsInstanceRoot() throws EMFUserError;

	public ModelInstance loadModelInstanceWithoutChildrenById(Integer parentId)throws EMFUserError;

	public void modifyModel(ModelInstance modelInstance) throws EMFUserError;

	public Integer insertModel(ModelInstance toCreate) throws EMFUserError;

}