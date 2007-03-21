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
package it.eng.spagobi.metadata;

/**
 * SbiEventRoleId generated by hbm2java
 */
public class SbiEventRoleId  implements java.io.Serializable {

    // Fields    

     private SbiExtRoles role;
     private SbiEventsLog event;


    // Constructors

    /** default constructor */
    public SbiEventRoleId() {
    }

    // Property accessors

    public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SbiEventRoleId) ) return false;
		 SbiEventRoleId castOther = ( SbiEventRoleId ) other; 
         
		 return 
		 	(this.getRole()==castOther.getRole()) || (this.getRole()!=null && castOther.getRole()!=null && this.getRole().equals(castOther.getRole()))
		 	&& 
		 	(this.getEvent()==castOther.getEvent()) || (this.getEvent()!=null && castOther.getEvent()!=null && this.getEvent().equals(castOther.getEvent()));
   }
   
   public int hashCode() {
         int result = 17;
         result = 37 * result + this.getRole().hashCode();
         result = 37 * result + this.getEvent().hashCode();
         return result;
   }   


	public SbiEventsLog getEvent() {
		return event;
	}
	public void setEvent(SbiEventsLog event) {
		this.event = event;
	}
	public SbiExtRoles getRole() {
		return role;
	}
	public void setRole(SbiExtRoles role) {
		this.role = role;
	}
}