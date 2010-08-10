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

	public void modifyModelInstance(ModelInstance modelInstance) throws EMFUserError;

	public Integer insertModelInstance(ModelInstance toCreate) throws EMFUserError;

	public ModelInstance loadModelInstanceWithChildrenById(Integer parseInt) throws EMFUserError;

	public List getCandidateModelChildren(Integer parentId) throws EMFUserError;

	/**
	 * Delete a Model Instance. 
	 * @param modelId id of the model instance to delete.
	 * @return Return true if the model is deleted.
	 * @throws EMFUserError If an Exception occurred.
	 */
	public boolean deleteModelInstance(Integer modelId)throws EMFUserError;

	public List loadModelsInstanceRoot(String fieldOrder, String typeOrder)throws EMFUserError;

}