/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization;

import org.exoplatform.services.database.XResources;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.picocontainer.Startable;

import org.exoplatform.container.groovy.test.OtherGroovyObject ;
/**
 * Jul 20, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: NewUserEventListener.java,v 1.7 2004/10/28 15:36:43 tuan08 Exp $
 */
public class GroovyNewUserEventListener extends UserEventListener implements Startable {
  
  public GroovyNewUserEventListener(OrganizationService orgService) {
    orgService.addUserEventListener(this) ;
  }
  
  public void start() { }
  
  public void stop() { }
  
  public void preSave(User user, boolean isNew, XResources xresources) {
    //println "call pre save user: " + user.userName ;
  }

  public void postSave(User user, boolean isNew, XResources xresources) {
    //println "call post save user: " + user.userName ;
  }

  public void preDelete(User user,  XResources xresources)  {
    //println "call pre delete user: " + user.userName ;
  }

  public void postDelete(User user,  XResources xresources)  {
    //println "call post delete user: " + user.userName ;
  }
}
