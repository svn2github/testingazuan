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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

class AnchorFigure extends ActivityFigure {
  FixedAnchor inAnchor, outAnchor;

  public final static boolean WHITE = false;
  public final static boolean BLACK = true;
  
  private boolean fill;
  
  public AnchorFigure(boolean fill) {
	  
	    inAnchor = new FixedAnchor(this);
	    inAnchor.place = new Point(0,1);
	    targetAnchors.put("el_in", inAnchor);
	    outAnchor = new FixedAnchor(this);
	    outAnchor.place = new Point(2,1);
	    sourceAnchors.put("el_out", outAnchor);
	    
	    this.fill = fill;
  }

  public void paintFigure(Graphics g) {
    Rectangle r = bounds;
    Rectangle r1 = new Rectangle(r.x+1, r.y+1, r.width-2, r.height-2);
    if (fill) {
    	g.setBackgroundColor(ColorConstants.black);
    	g.fillOval(r1);
    }
    g.drawOval(r1);
  }

  
}