/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.importexport;

import it.eng.spagobi.metadata.SbiChecks;
import it.eng.spagobi.metadata.SbiDomains;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.metadata.SbiLov;
import it.eng.spagobi.metadata.SbiObjPar;
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
	private Map objparIDAssociation = new HashMap();
	private Map objparAssociation = new HashMap();
	private Map connectionsAssociation = new HashMap();
	
	/**
	 * Checks if the metadata association is empty
	 * @return boolean, true is associations are empty false otherwise
	 */
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
	
	/**
	 * Clears all the inforamtion about associations
	 */
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
	
	/**
	 * Checks if Associations for the specific object are empty
	 * @return boolean, true if associations are empty, false otherwise
	 */
	public boolean isParameterAssEmpty(){
		return parameterAssociation.keySet().isEmpty();
	}
	
	/**
	 * Checks if Associations for the specific object are empty
	 * @return boolean, true if associations are empty, false otherwise
	 */
	public boolean isRoleAssEmpty(){
		return roleAssociation.keySet().isEmpty();
	}
	
	/**
	 * Checks if Associations for the specific object are empty
	 * @return boolean, true if associations are empty, false otherwise
	 */
	public boolean isBIObjAssEmpty(){
		return biobjAssociation.keySet().isEmpty();
	}
	
	/**
	 * Checks if Associations for the specific object are empty
	 * @return boolean, true if associations are empty, false otherwise
	 */
	public boolean isLovAssEmpty(){
		return lovAssociation.keySet().isEmpty();
	}
	
	/**
	 * Checks if Associations for the specific object are empty
	 * @return boolean, true if associations are empty, false otherwise
	 */
	public boolean isFunctAssEmpty(){
		return functAssociation.keySet().isEmpty();
	}
	
	/**
	 * Checks if Associations for the specific object are empty
	 * @return boolean, true if associations are empty, false otherwise
	 */
	public boolean isEngineAssEmpty(){
		return engineAssociation.keySet().isEmpty();
	}
	
	/**
	 * Checks if Associations for the specific object are empty
	 * @return boolean, true if associations are empty, false otherwise
	 */
	public boolean isCheckAssEmpty(){
		return checkAssociation.keySet().isEmpty();
	}
	
	/**
	 * Checks if Associations for the specific object are empty
	 * @return boolean, true if associations are empty, false otherwise
	 */
	public boolean isParuseAssEmpty(){
		return paruseAssociation.keySet().isEmpty();
	}
	
	
	/**
	 * Gets the Map of associations between current and exported parameter ids
	 * @return Map of assocaitions
	 */
	public Map getParameterIDAssociation() {
		return parameterIDAssociation;
	}
	
	/**
	 * Gets the Map of associations between current and exported parameters
	 * @return Map of associations
	 */
	public Map getParameterAssociation() {
		return parameterAssociation;
	}
	
	/**
	 * Inserts a couple of parameters into the associations
	 */
	public void insertCoupleParameter(SbiParameters exp, SbiParameters curr) {
		//parameterIDAssociation.put(exp.getParId().toString(), curr.getParId().toString());
		parameterAssociation.put(exp, curr);
	}
	
	/**
	 * Inserts a couple of parameter ids into the associations 
	 */
	public void insertCoupleParameter(Integer exp, Integer curr) {
		parameterIDAssociation.put(exp, curr);
	}
	
	/**
	 * Gets the Map of associations between current and exported role ids
	 * @return Map of associations
	 */
	public Map getRoleIDAssociation() {
		return roleIDAssociation;
	}
	
	/**
	 * Gets the Map of associations between current and exported roles
	 * @return Map of associations
	 */
	public Map getRoleAssociation() {
		return roleAssociation;
	}
	
	/**
	 * Inserts a couple of roles into the associations 
	 */
	public void insertCoupleRole(SbiExtRoles exp, SbiExtRoles curr) {
		//roleIDAssociation.put(exp.getExtRoleId().toString(), curr.getExtRoleId().toString());
		roleAssociation.put(exp, curr);
	}
	
	/**
	 * Inserts a couple of role ids into the associations 
	 */
	public void insertCoupleRole(Integer exp, Integer curr) {
		roleIDAssociation.put(exp, curr);
	}

	/**
	 * Gets the Map of associations between current and exported biobject ids
	 * @return Map of associations
	 */
	public Map getBIobjIDAssociation() {
		return biobjIDAssociation;
	}
	
	/**
	 * Gets the Map of associations between current and exported biobjects
	 * @return Map of associations
	 */
	public Map getBIObjAssociation() {
		return biobjAssociation;
	}
	
	/**
	 * Inserts a couple of biobjects into the associations 
	 */
	public void insertCoupleBIObj(SbiObjects exp, SbiObjects curr) {
		//biobjIDAssociation.put(exp.getBiobjId().toString(), curr.getBiobjId().toString());
		biobjAssociation.put(exp, curr);
	}
	
	/**
	 * Inserts a couple of biobject ids into the associations 
	 */
	public void insertCoupleBIObj(Integer exp, Integer curr) {
		biobjIDAssociation.put(exp, curr);
	}
	
	/**
	 * Gets the Map of associations between current and exported lovs ids
	 * @return Map of associations
	 */
	public Map getLovIDAssociation() {
		return lovIDAssociation;
	}
	
	/**
	 * Gets the Map of associations between current and exported lovs
	 * @return Map of associations
	 */
	public Map getLovAssociation() {
		return lovAssociation;
	}
	
	/**
	 * Inserts a couple of lovs into the associations 
	 */
	public void insertCoupleLov(SbiLov exp, SbiLov curr) {
		//lovIDAssociation.put(exp.getLovId().toString(), curr.getLovId().toString());
		lovAssociation.put(exp, curr);
	}
	
	/**
	 * Inserts a couple of lov ids into the associations 
	 */
	public void insertCoupleLov(Integer exp, Integer curr) {
		lovIDAssociation.put(exp, curr);
	}
	
	/**
	 * Gets the Map of associations between current and exported functionality ids
	 * @return Map of associations
	 */
	public Map getFunctIDAssociation() {
		return functIDAssociation;
	}
	
	/**
	 * Gets the Map of associations between current and exported functionalities
	 * @return Map of associations
	 */
	public Map getFunctAssociation() {
		return functAssociation;
	}
	
	/**
	 * Inserts a couple of functionalities into the associations 
	 */
	public void insertCoupleFunct(SbiFunctions exp, SbiFunctions curr) {
		//functIDAssociation.put(exp.getFunctId().toString(), curr.getFunctId().toString());
		functAssociation.put(exp, curr);
	}
	
	/**
	 * Inserts a couple of functionality ids into the associations 
	 */
	public void insertCoupleFunct(Integer exp, Integer curr) {
		functIDAssociation.put(exp, curr);
	}

	/**
	 * Gets the Map of associations between current and exported engine ids
	 * @return Map of associations
	 */
	public Map getEngineIDAssociation() {
		return engineIDAssociation;
	}
	
	/**
	 * Gets the Map of associations between current and exported engines
	 * @return Map of associations
	 */
	public Map getEngineAssociation() {
		return engineAssociation;
	}
	
	/**
	 * Inserts a couple of engines into the associations 
	 */
	public void insertCoupleEngine(SbiEngines exp, SbiEngines curr) {
		//engineIDAssociation.put(exp.getEngineId().toString(), curr.getEngineId().toString());
		engineAssociation.put(exp, curr);
	}
	
	/**
	 * Inserts a couple of engine ids into the associations 
	 */
	public void insertCoupleEngine(Integer exp, Integer curr) {
		engineIDAssociation.put(exp, curr);
	}
	
	/**
	 * Gets the Map of associations between current and exported check ids
	 * @return Map of associations
	 */
	public Map getCheckIDAssociation() {
		return checkIDAssociation;
	}
	
	/**
	 * Gets the Map of associations between current and exported checks
	 * @return Map of associations
	 */
	public Map getCheckAssociation() {
		return checkAssociation;
	}
	
	/**
	 * Inserts a couple of checks into the associations 
	 */
	public void insertCoupleCheck(SbiChecks exp, SbiChecks curr) {
		//checkIDAssociation.put(exp.getCheckId().toString(), curr.getCheckId().toString());
		checkAssociation.put(exp, curr);
	}
	
	/**
	 * Inserts a couple of check ids into the associations 
	 */
	public void insertCoupleCheck(Integer exp, Integer curr) {
		checkIDAssociation.put(exp, curr);
	}
	
	/**
	 * Gets the Map of associations between current and exported parameter use ids
	 * @return Map of associations
	 */
	public Map getParuseIDAssociation() {
		return paruseIDAssociation;
	}
	
	/**
	 * Gets the Map of associations between current and exported parameter uses 
	 * @return Map of associations
	 */
	public Map getParuseAssociation() {
		return paruseAssociation;
	}
	
	/**
	 * Inserts a couple of parameter uses into the associations 
	 */
	public void insertCoupleParuse(SbiParuse exp, SbiParuse curr) {
		//paruseIDAssociation.put(exp.getUseId().toString(), curr.getUseId().toString());
		paruseAssociation.put(exp, curr);
	}
	
	/**
	 * Inserts a couple of parameter use ids into the associations 
	 */
	public void insertCoupleParuse(Integer exp, Integer curr) {
		paruseIDAssociation.put(exp, curr);
	}
	
	/**
	 * Gets the Map of associations between current and exported domain id
	 * @return Map of associations
	 */
	public Map getDomainIDAssociation() {
		return domainIDAssociation;
	}
	
	/**
	 * Gets the Map of associations between current and exported domains
	 * @return Map of associations
	 */
	public Map getDomainAssociation() {
		return domainAssociation;
	}
	
	/**
	 * Inserts a couple of domains into the associations 
	 */
	public void insertCoupleDomain(SbiDomains exp, SbiDomains curr) {
		domainAssociation.put(exp, curr);
	}
	
	/**
	 * Inserts a couple of domain id into the associations 
	 */
	public void insertCoupleDomain(Integer exp, Integer curr) {
		domainIDAssociation.put(exp, curr);
	}

	
	/**
	 * Gets the Map of associations between current and exported objpar id
	 * @return Map of associations
	 */
	public Map getObjparIDAssociation() {
		return objparIDAssociation;
	}
	
	/**
	 * Gets the Map of associations between current and exported objpars
	 * @return Map of associations
	 */
	public Map getObjparAssociation() {
		return objparAssociation;
	}
	
	/**
	 * Inserts a couple of objpar into the associations 
	 */
	public void insertCoupleObjpar(SbiObjPar exp, SbiObjPar curr) {
		objparAssociation.put(exp, curr);
	}
	
	/**
	 * Inserts a couple of objpar id into the associations 
	 */
	public void insertCoupleObjpar(Integer exp, Integer curr) {
		objparIDAssociation.put(exp, curr);
	}
	
	/**
	 * Gets the Map of associations between current and exported connections
	 * @return Map of associations
	 */
	public Map getConnectionAssociation() {
		return connectionsAssociation;
	}
	
	/**
	 * Inserts a couple of connection names into the associations 
	 */
	public void insertCoupleConnections(String exp, String curr) {
		connectionsAssociation.put(exp, curr);
	}
	
	
}
