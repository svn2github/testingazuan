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
package it.eng.spagobi.meta.cwm.jmi.spagobi.meta;

/**
 * Meta package interface.
 * CWM Metadata Container
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface MetaPackage extends javax.jmi.reflect.RefPackage {
    /**
     * Returns nested package Core.
     * @return Proxy object related to nested package Core.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.core.CorePackage getCore();
    /**
     * Returns nested package Behavioral.
     * @return Proxy object related to nested package Behavioral.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.behavioral.BehavioralPackage getBehavioral();
    /**
     * Returns nested package Relationships.
     * @return Proxy object related to nested package Relationships.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.relationships.RelationshipsPackage getRelationships();
    /**
     * Returns nested package Instance.
     * @return Proxy object related to nested package Instance.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.instance.InstancePackage getInstance();
    /**
     * Returns nested package BusinessInformation.
     * @return Proxy object related to nested package BusinessInformation.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.businessinformation.BusinessInformationPackage getBusinessInformation();
    /**
     * Returns nested package DataTypes.
     * @return Proxy object related to nested package DataTypes.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.datatypes.DataTypesPackage getDataTypes();
    /**
     * Returns nested package Expressions.
     * @return Proxy object related to nested package Expressions.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.expressions.ExpressionsPackage getExpressions();
    /**
     * Returns nested package KeysIndexes.
     * @return Proxy object related to nested package KeysIndexes.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.keysindexes.KeysIndexesPackage getKeysIndexes();
    /**
     * Returns nested package SoftwareDeployment.
     * @return Proxy object related to nested package SoftwareDeployment.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.softwaredeployment.SoftwareDeploymentPackage getSoftwareDeployment();
    /**
     * Returns nested package TypeMapping.
     * @return Proxy object related to nested package TypeMapping.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.typemapping.TypeMappingPackage getTypeMapping();
    /**
     * Returns nested package Relational.
     * @return Proxy object related to nested package Relational.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.relational.RelationalPackage getRelational();
    /**
     * Returns nested package Multidimensional.
     * @return Proxy object related to nested package Multidimensional.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.multidimensional.MultidimensionalPackage getMultidimensional();
    /**
     * Returns nested package Transformation.
     * @return Proxy object related to nested package Transformation.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.transformation.TransformationPackage getTransformation();
    /**
     * Returns nested package Olap.
     * @return Proxy object related to nested package Olap.
     */
    public it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.OlapPackage getOlap();
}
