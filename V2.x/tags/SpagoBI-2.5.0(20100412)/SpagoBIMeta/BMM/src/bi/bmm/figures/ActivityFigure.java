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

@Author Marco Cortella

**/
package bi.bmm.figures;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import bi.bmm.util.ClassInfo;
import bi.bmm.util.ComplexClassInfo;

public abstract class ActivityFigure extends Figure {
	  Rectangle r = new Rectangle();

	  Hashtable targetAnchors = new Hashtable();

	  Hashtable sourceAnchors = new Hashtable();

	  String message = new String();
	  ClassInfo data;
	  ComplexClassInfo complexData;

	  public void setName(String msg) {
	    message = msg;
	    repaint();
	  }

	  public void setData(ClassInfo ci) {
		    data = ci;
		    repaint();
		  }
	  
	  public void setComplexData(ComplexClassInfo cci) {
		    complexData = cci;
		    repaint();
		  }
	  
	  public ConnectionAnchor ConnectionAnchorAt(Point p) {
	    ConnectionAnchor closest = null;
	    long min = Long.MAX_VALUE;
	    Hashtable conn = getSourceConnectionAnchors();
	    conn.putAll(getTargetConnectionAnchors());
	    Enumeration e = conn.elements();
	    while (e.hasMoreElements()) {
	      ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
	      Point p2 = c.getLocation(null);
	      long d = p.getDistance2(p2);
	      if (d < min) {
	        min = d;
	        closest = c;
	      }
	    }
	    return closest;
	  }

	  public ConnectionAnchor getSourceConnectionAnchor(String name) {
	    return (ConnectionAnchor) sourceAnchors.get(name);
	  }

	  public ConnectionAnchor getTargetConnectionAnchor(String name) {
	    return (ConnectionAnchor) targetAnchors.get(name);
	  }

	  public String getSourceAnchorName(ConnectionAnchor c) {
	    Enumeration e = sourceAnchors.keys();
	    String name;
	    while (e.hasMoreElements()) {
	      name = (String) e.nextElement();
	      if (sourceAnchors.get(name).equals(c))
	        return name;
	    }
	    return null;
	  }

	  public String getTargetAnchorName(ConnectionAnchor c) {
	    Enumeration e = targetAnchors.keys();
	    String name;
	    while (e.hasMoreElements()) {
	      name = (String) e.nextElement();
	      if (targetAnchors.get(name).equals(c))
	        return name;
	    }
	    return null;
	  }

	  public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
	    ConnectionAnchor closest = null;
	    long min = Long.MAX_VALUE;
	    Enumeration e = getSourceConnectionAnchors().elements();
	    while (e.hasMoreElements()) {
	      ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
	      Point p2 = c.getLocation(null);
	      long d = p.getDistance2(p2);
	      if (d < min) {
	        min = d;
	        closest = c;
	      }
	    }
	    return closest;
	  }

	  public Hashtable getSourceConnectionAnchors() {
	    return sourceAnchors;
	  }

	  public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
	    ConnectionAnchor closest = null;
	    long min = Long.MAX_VALUE;
	    Enumeration e = getTargetConnectionAnchors().elements();
	    while (e.hasMoreElements()) {
	      ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
	      Point p2 = c.getLocation(null);
	      long d = p.getDistance2(p2);
	      if (d < min) {
	        min = d;
	        closest = c;
	      }
	    }
	    return closest;
	  }

	  public Hashtable getTargetConnectionAnchors() {
	    return targetAnchors;
	  }
	}