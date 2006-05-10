package it.eng.spagobi.importexport;

import it.eng.spagobi.metadata.SbiChecks;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.metadata.SbiLov;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.metadata.SbiParameters;
import it.eng.spagobi.metadata.SbiParuse;
import it.eng.spagobi.metadata.SbiParuseCk;

import java.util.HashMap;
import java.util.Map;

public class ExistingMetadata {

	private Map parameterIDAssociation = new HashMap();
	private Map parameterAssociation = new HashMap();
	private Map roleIDAssociation = new HashMap();
	private Map roleAssociation = new HashMap();
	private Map biobjIDAssociation = new HashMap();
	private Map biobjAssociation = new HashMap();
	private Map lovIDAssociation = new HashMap();
	private Map lovAssociation = new HashMap();
	private Map functIDAssociation = new HashMap();
	private Map functAssociation = new HashMap();
	private Map engineIDAssociation = new HashMap();
	private Map engineAssociation = new HashMap();
	private Map checkIDAssociation = new HashMap();
	private Map checkAssociation = new HashMap();
	private Map paruseIDAssociation = new HashMap();
	private Map paruseAssociation = new HashMap();
	
	
	public boolean isEmpty() {
		if(!parameterIDAssociation.keySet().isEmpty())
			return false;
		if(!roleIDAssociation.keySet().isEmpty())
			return false;
		if(!biobjIDAssociation.keySet().isEmpty())
			return false;
		if(!lovIDAssociation.keySet().isEmpty())
			return false;
		if(!functIDAssociation.keySet().isEmpty())
			return false;
		if(!engineIDAssociation.keySet().isEmpty())
			return false;
		if(!checkIDAssociation.keySet().isEmpty())
			return false;
		if(!paruseIDAssociation.keySet().isEmpty())
			return false;
		return true;
	}
	
	public boolean isParameterAssEmpty(){
		return parameterIDAssociation.keySet().isEmpty();
	}
	
	public boolean isRoleAssEmpty(){
		return roleIDAssociation.keySet().isEmpty();
	}
	
	public boolean isBIObjAssEmpty(){
		return biobjIDAssociation.keySet().isEmpty();
	}
	
	public boolean isLovAssEmpty(){
		return lovIDAssociation.keySet().isEmpty();
	}
	
	public boolean isFunctAssEmpty(){
		return functIDAssociation.keySet().isEmpty();
	}
	
	public boolean isEngineAssEmpty(){
		return engineIDAssociation.keySet().isEmpty();
	}
	
	public boolean isCheckAssEmpty(){
		return checkIDAssociation.keySet().isEmpty();
	}
	
	public boolean isParuseAssEmpty(){
		return paruseIDAssociation.keySet().isEmpty();
	}
	
	
	public Map getParameterIDAssociation() {
		return parameterIDAssociation;
	}
	
	public Map getParameterAssociation() {
		return parameterAssociation;
	}
	
	public void insertCoupleParameter(SbiParameters exp, SbiParameters curr) {
		parameterIDAssociation.put(exp.getParId().toString(), curr.getParId().toString());
		parameterAssociation.put(exp, curr);
	}
	
	public Map getRoleIDAssociation() {
		return roleIDAssociation;
	}
	
	public Map getRoleAssociation() {
		return roleAssociation;
	}
	
	public void insertCoupleRole(SbiExtRoles exp, SbiExtRoles curr) {
		parameterIDAssociation.put(exp.getExtRoleId().toString(), curr.getExtRoleId().toString());
		parameterAssociation.put(exp, curr);
	}

	public Map getBIobjIDAssociation() {
		return biobjIDAssociation;
	}
	
	public Map getBIObjAssociation() {
		return biobjAssociation;
	}
	
	public void insertCoupleBIObj(SbiObjects exp, SbiObjects curr) {
		biobjIDAssociation.put(exp.getBiobjId().toString(), curr.getBiobjId().toString());
		biobjAssociation.put(exp, curr);
	}
	
	public Map getLovIDAssociation() {
		return lovIDAssociation;
	}
	
	public Map getLovAssociation() {
		return lovAssociation;
	}
	
	public void insertCoupleLov(SbiLov exp, SbiLov curr) {
		lovIDAssociation.put(exp.getLovId().toString(), curr.getLovId().toString());
		lovAssociation.put(exp, curr);
	}
	
	public Map getFunctIDAssociation() {
		return functIDAssociation;
	}
	
	public Map getFunctAssociation() {
		return functAssociation;
	}
	
	public void insertCoupleFunct(SbiFunctions exp, SbiFunctions curr) {
		functIDAssociation.put(exp.getFunctId().toString(), curr.getFunctId().toString());
		functAssociation.put(exp, curr);
	}

	public Map getEngineIDAssociation() {
		return engineIDAssociation;
	}
	
	public Map getEngineAssociation() {
		return engineAssociation;
	}
	
	public void insertCoupleEngine(SbiEngines exp, SbiEngines curr) {
		engineIDAssociation.put(exp.getEngineId().toString(), curr.getEngineId().toString());
		engineAssociation.put(exp, curr);
	}
	
	public Map getCheckIDAssociation() {
		return checkIDAssociation;
	}
	
	public Map getCheckAssociation() {
		return checkAssociation;
	}
	
	public void insertCoupleCheck(SbiChecks exp, SbiChecks curr) {
		checkIDAssociation.put(exp.getCheckId().toString(), curr.getCheckId().toString());
		checkAssociation.put(exp, curr);
	}
	
	public Map getParuseIDAssociation() {
		return paruseIDAssociation;
	}
	
	public Map getParuseAssociation() {
		return paruseAssociation;
	}
	
	public void insertCoupleParuse(SbiParuse exp, SbiParuse curr) {
		paruseIDAssociation.put(exp.getUseId().toString(), curr.getUseId().toString());
		paruseAssociation.put(exp, curr);
	}
	

}
