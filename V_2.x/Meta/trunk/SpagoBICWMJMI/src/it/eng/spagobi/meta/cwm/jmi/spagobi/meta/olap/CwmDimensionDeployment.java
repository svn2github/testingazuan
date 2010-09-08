/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap;

/**
 * DimensionDeployment object instance interface.
 * DimensionDeployment represents a particular implementation strategy for 
 * the dimensional/hierarchical portions of an OLAP model.  It does so by 
 * organizing a collection of StructureMaps, which in turn define a mapping 
 * to an implementation model.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface CwmDimensionDeployment extends it.eng.spagobi.meta.cwm.jmi.spagobi.meta.core.CwmClass {
    /**
     * Returns the value of reference hierarchyLevelAssociation.
     * @return Value of reference hierarchyLevelAssociation.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.CwmHierarchyLevelAssociation getHierarchyLevelAssociation();
    /**
     * Sets the value of reference hierarchyLevelAssociation. See {@link #getHierarchyLevelAssociation} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setHierarchyLevelAssociation(it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.CwmHierarchyLevelAssociation newValue);
    /**
     * Returns the value of reference valueBasedHierarchy.
     * @return Value of reference valueBasedHierarchy.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.CwmValueBasedHierarchy getValueBasedHierarchy();
    /**
     * Sets the value of reference valueBasedHierarchy. See {@link #getValueBasedHierarchy} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setValueBasedHierarchy(it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.CwmValueBasedHierarchy newValue);
    /**
     * Returns the value of reference structureMap.
     * @return Value of reference structureMap. Element type: {@link it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.CwmStructureMap}
     */
    public java.util.Collection/*<org.pentaho.pms.cwm.pentaho.meta.olap.CwmStructureMap>*/ getStructureMap();
    /**
     * Returns the value of reference listOfValues.
     * @return Value of reference listOfValues.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.CwmStructureMap getListOfValues();
    /**
     * Sets the value of reference listOfValues. See {@link #getListOfValues} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setListOfValues(it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.CwmStructureMap newValue);
    /**
     * Returns the value of reference immediateParent.
     * @return Value of reference immediateParent.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.CwmStructureMap getImmediateParent();
    /**
     * Sets the value of reference immediateParent. See {@link #getImmediateParent} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setImmediateParent(it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.CwmStructureMap newValue);
    /**
     * Returns the value of reference deploymentGroup.
     * @return Value of reference deploymentGroup.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.CwmDeploymentGroup getDeploymentGroup();
    /**
     * Sets the value of reference deploymentGroup. See {@link #getDeploymentGroup} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setDeploymentGroup(it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.CwmDeploymentGroup newValue);
}
