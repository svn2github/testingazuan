package it.eng.spagobi.importexport;

import it.eng.spagobi.metadata.SbiChecks;
import it.eng.spagobi.metadata.SbiDomains;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.metadata.SbiLov;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.metadata.SbiParameters;
import it.eng.spagobi.metadata.SbiParuse;

import java.util.HashMap;
import java.util.Map;

public class MetadataAssociations {

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
	private Map domainIDAssociation = new HashMap();
	private Map domainAssociation = new HashMap();
	private Map connectionsAssociation = new HashMap();
	
	
	public boolean isEmpty() {
		if(!parameterAssociation.keySet().isEmpty())
			return false;
		if(!roleAssociation.keySet().isEmpty())
			return false;
		if(!biobjAssociation.keySet().isEmpty())
			return false;
		if(!lovAssociation.keySet().isEmpty())
			return false;
		if(!functAssociation.keySet().isEmpty())
			return false;
		if(!engineAssociation.keySet().isEmpty())
			return false;
		if(!checkAssociation.keySet().isEmpty())
			return false;
		if(!paruseAssociation.keySet().isEmpty())
			return false;
		return true;
	}
	
	public void clear() {
		parameterIDAssociation = new HashMap();
		parameterAssociation = new HashMap();
		roleIDAssociation = new HashMap();
		roleAssociation = new HashMap();
		biobjIDAssociation = new HashMap();
		biobjAssociation = new HashMap();
		lovIDAssociation = new HashMap();
		lovAssociation = new HashMap();
		functIDAssociation = new HashMap();
		functAssociation = new HashMap();
		engineIDAssociation = new HashMap();
		engineAssociation = new HashMap();
		checkIDAssociation = new HashMap();
		checkAssociation = new HashMap();
		paruseIDAssociation = new HashMap();
		paruseAssociation = new HashMap();
		connectionsAssociation = new HashMap();
	}
	
	public boolean isParameterAssEmpty(){
		return parameterAssociation.keySet().isEmpty();
	}
	
	public boolean isRoleAssEmpty(){
		return roleAssociation.keySet().isEmpty();
	}
	
	public boolean isBIObjAssEmpty(){
		return biobjAssociation.keySet().isEmpty();
	}
	
	public boolean isLovAssEmpty(){
		return lovAssociation.keySet().isEmpty();
	}
	
	public boolean isFunctAssEmpty(){
		return functAssociation.keySet().isEmpty();
	}
	
	public boolean isEngineAssEmpty(){
		return engineAssociation.keySet().isEmpty();
	}
	
	public boolean isCheckAssEmpty(){
		return checkAssociation.keySet().isEmpty();
	}
	
	public boolean isParuseAssEmpty(){
		return paruseAssociation.keySet().isEmpty();
	}
	
	
	public Map getParameterIDAssociation() {
		return parameterIDAssociation;
	}
	
	public Map getParameterAssociation() {
		return parameterAssociation;
	}
	
	public void insertCoupleParameter(SbiParameters exp, SbiParameters curr) {
		//parameterIDAssociation.put(exp.getParId().toString(), curr.getParId().toString());
		parameterAssociation.put(exp, curr);
	}
	
	public void insertCoupleParameter(Integer exp, Integer curr) {
		parameterIDAssociation.put(exp, curr);
	}
	
	public Map getRoleIDAssociation() {
		return roleIDAssociation;
	}
	
	public Map getRoleAssociation() {
		return roleAssociation;
	}
	
	public void insertCoupleRole(SbiExtRoles exp, SbiExtRoles curr) {
		//roleIDAssociation.put(exp.getExtRoleId().toString(), curr.getExtRoleId().toString());
		roleAssociation.put(exp, curr);
	}
	
	public void insertCoupleRole(Integer exp, Integer curr) {
		roleIDAssociation.put(exp, curr);
	}

	public Map getBIobjIDAssociation() {
		return biobjIDAssociation;
	}
	
	public Map getBIObjAssociation() {
		return biobjAssociation;
	}
	
	public void insertCoupleBIObj(SbiObjects exp, SbiObjects curr) {
		//biobjIDAssociation.put(exp.getBiobjId().toString(), curr.getBiobjId().toString());
		biobjAssociation.put(exp, curr);
	}
	
	public void insertCoupleBIObj(Integer exp, Integer curr) {
		biobjIDAssociation.put(exp, curr);
	}
	
	public Map getLovIDAssociation() {
		return lovIDAssociation;
	}
	
	public Map getLovAssociation() {
		return lovAssociation;
	}
	
	public void insertCoupleLov(SbiLov exp, SbiLov curr) {
		//lovIDAssociation.put(exp.getLovId().toString(), curr.getLovId().toString());
		lovAssociation.put(exp, curr);
	}
	
	public void insertCoupleLov(Integer exp, Integer curr) {
		lovIDAssociation.put(exp, curr);
	}
	
	public Map getFunctIDAssociation() {
		return functIDAssociation;
	}
	
	public Map getFunctAssociation() {
		return functAssociation;
	}
	
	public void insertCoupleFunct(SbiFunctions exp, SbiFunctions curr) {
		//functIDAssociation.put(exp.getFunctId().toString(), curr.getFunctId().toString());
		functAssociation.put(exp, curr);
	}
	
	public void insertCoupleFunct(Integer exp, Integer curr) {
		functIDAssociation.put(exp, curr);
	}

	public Map getEngineIDAssociation() {
		return engineIDAssociation;
	}
	
	public Map getEngineAssociation() {
		return engineAssociation;
	}
	
	public void insertCoupleEngine(SbiEngines exp, SbiEngines curr) {
		//engineIDAssociation.put(exp.getEngineId().toString(), curr.getEngineId().toString());
		engineAssociation.put(exp, curr);
	}
	
	public void insertCoupleEngine(Integer exp, Integer curr) {
		engineIDAssociation.put(exp, curr);
	}
	
	public Map getCheckIDAssociation() {
		return checkIDAssociation;
	}
	
	public Map getCheckAssociation() {
		return checkAssociation;
	}
	
	public void insertCoupleCheck(SbiChecks exp, SbiChecks curr) {
		//checkIDAssociation.put(exp.getCheckId().toString(), curr.getCheckId().toString());
		checkAssociation.put(exp, curr);
	}
	
	public void insertCoupleCheck(Integer exp, Integer curr) {
		checkIDAssociation.put(exp, curr);
	}
	
	public Map getParuseIDAssociation() {
		return paruseIDAssociation;
	}
	
	public Map getParuseAssociation() {
		return paruseAssociation;
	}
	
	public void insertCoupleParuse(SbiParuse exp, SbiParuse curr) {
		//paruseIDAssociation.put(exp.getUseId().toString(), curr.getUseId().toString());
		paruseAssociation.put(exp, curr);
	}
	
	public void insertCoupleParuse(Integer exp, Integer curr) {
		paruseIDAssociation.put(exp, curr);
	}
	
	public Map getDomainIDAssociation() {
		return domainIDAssociation;
	}
	
	public Map getDomainAssociation() {
		return domainAssociation;
	}
	
	public void insertCoupleDomain(SbiDomains exp, SbiDomains curr) {
		domainAssociation.put(exp, curr);
	}
	
	public void insertCoupleDomain(Integer exp, Integer curr) {
		domainIDAssociation.put(exp, curr);
	}

	public Map getConnectionAssociation() {
		return connectionsAssociation;
	}
	
	public void insertCoupleConnections(String exp, String curr) {
		connectionsAssociation.put(exp, curr);
	}
	
	
}
