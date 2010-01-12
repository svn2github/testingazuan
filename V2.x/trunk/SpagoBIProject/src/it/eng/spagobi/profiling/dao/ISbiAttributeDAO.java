package it.eng.spagobi.profiling.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.profiling.bean.SbiAttribute;
import it.eng.spagobi.profiling.bean.SbiUserAttributes;

import java.util.HashMap;
import java.util.List;

public interface ISbiAttributeDAO {
	
	public List<SbiUserAttributes> loadSbiAttributesById(Integer id) throws EMFUserError;
	
	public HashMap<Integer, String> loadSbiAttributesByIds(List<String> ids) throws EMFUserError;
	
	public SbiAttribute loadSbiAttributeByName(String name) throws EMFUserError;
	
	public List<SbiAttribute> loadSbiAttributes() throws EMFUserError;
	
	public Integer saveSbiAttribute(SbiAttribute attribute) throws EMFUserError;

	public SbiUserAttributes loadSbiAttributesByUserAndId(Integer userId, Integer id)	throws EMFUserError;

}
