/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.groovy.test ;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 2, 2004
 * @version $Id: UserSuites.java,v 1.4 2004/10/21 15:21:50 tuan08 Exp $
 */
public class TestGroovyObject {
  public TestGroovyObject() {
    other = new OtherGroovyObject() ;
  	println 'hello , this is TestGroovyObject' 
  }
}