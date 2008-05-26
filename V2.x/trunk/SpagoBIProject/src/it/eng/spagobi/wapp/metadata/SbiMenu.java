/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.wapp.metadata;
// Generated 9-apr-2008 12.18.27 by Hibernate Tools 3.1.0 beta3

import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;

import java.util.HashSet;
import java.util.Set;


/**
 * SbiMenu generated by hbm2java
 */

public class SbiMenu  implements java.io.Serializable {


    // Fields    

     private Integer menuId;
     private SbiObjects sbiObjects;
     private String name;
     private String descr;
     private String staticPage;     
     private Integer parentId;
     private Set sbiMenuRoles = new HashSet(0);
     private Boolean homepage;
     private Boolean viewIcons;
     private Boolean hideExecBar;

     

    // Constructors

    /**
     * default constructor.
     */
    public SbiMenu() {
    }

	/**
	 * minimal constructor.
	 * 
	 * @param menuId the menu id
	 */
    public SbiMenu(Integer menuId) {
        this.menuId = menuId;
    }
    
    /**
     * full constructor.
     * 
     * @param menuId the menu id
     * @param sbiObjects the sbi objects
     * @param name the name
     * @param descr the descr
     * @param parentId the parent id
     * @param sbiMenuRoles the sbi menu roles
     */
    public SbiMenu(Integer menuId, SbiObjects sbiObjects, String name, String descr, Integer parentId, Set sbiMenuRoles) {
        this.menuId = menuId;
        this.sbiObjects = sbiObjects;
        this.name = name;
        this.descr = descr;
        this.parentId = parentId;
        this.sbiMenuRoles = sbiMenuRoles;
    }
    

   
    // Property accessors

    /**
     * Gets the menu id.
     * 
     * @return the menu id
     */
    public Integer getMenuId() {
        return this.menuId;
    }
    
    /**
     * Sets the menu id.
     * 
     * @param menuId the new menu id
     */
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    /**
     * Gets the sbi objects.
     * 
     * @return the sbi objects
     */
    public SbiObjects getSbiObjects() {
        return this.sbiObjects;
    }
    
    /**
     * Sets the sbi objects.
     * 
     * @param sbiObjects the new sbi objects
     */
    public void setSbiObjects(SbiObjects sbiObjects) {
        this.sbiObjects = sbiObjects;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Sets the name.
     * 
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the descr.
     * 
     * @return the descr
     */
    public String getDescr() {
        return this.descr;
    }
    
    /**
     * Sets the descr.
     * 
     * @param descr the new descr
     */
    public void setDescr(String descr) {
        this.descr = descr;
    }

    /**
     * Gets the parent id.
     * 
     * @return the parent id
     */
    public Integer getParentId() {
        return this.parentId;
    }
    
    /**
     * Sets the parent id.
     * 
     * @param parentId the new parent id
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * Gets the sbi menu roles.
     * 
     * @return the sbi menu roles
     */
    public Set getSbiMenuRoles() {
        return this.sbiMenuRoles;
    }
    
    /**
     * Sets the sbi menu roles.
     * 
     * @param sbiMenuRoles the new sbi menu roles
     */
    public void setSbiMenuRoles(Set sbiMenuRoles) {
        this.sbiMenuRoles = sbiMenuRoles;
    }

	public Boolean getHomepage() {
		return homepage;
	}

	public void setHomepage(Boolean homepage) {
		this.homepage = homepage;
	}

	public Boolean getViewIcons() {
		return viewIcons;
	}

	public void setViewIcons(Boolean viewIcons) {
		this.viewIcons = viewIcons;
	}

	public Boolean getHideExecBar() {
		return hideExecBar;
	}

	public void setHideExecBar(Boolean hideExecBar) {
		this.hideExecBar = hideExecBar;
	}

	public String getStaticPage() {
		return staticPage;
	}

	public void setStaticPage(String staticPage) {
		this.staticPage = staticPage;
	}
   


}
