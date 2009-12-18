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

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import bi.bmm.Activator;

class ClassFigure extends ActivityFigure {
  public FixedAnchor inAnchor, outAnchor;

  public ClassFigure() {
	  setAnchor("", new Point(0,1), new Point(2,1));
  }

  public void paintFigure(Graphics g) {
    Rectangle r = bounds;
    ArrayList<String[]> keys = data.getKeys();
    ArrayList<String[]> attributes = data.getAttributes();
    ArrayList<String[]> relationships = data.getRelationships();
    
    Rectangle r1 = new Rectangle(r.x+1, r.y+1, r.width-2, r.height-2);
    Rectangle r2 =new Rectangle (r.x + 10,r.y + 30,r.width-20,r.height -40);
    //outer rectangle
    g.setBackgroundColor(ColorConstants.button);
    g.fillRoundRectangle(r1,20,20);
    g.drawText(data.getClassName().toString(), r.x + 3 * r.width / 8, r.y + 10);
    //*
    Image img = Activator.getImageDescriptor("icons/ELUniverse/BC_20x20.png").createImage();
    g.drawImage(img, r.x + 3 * r.width / 8 - 30, r.y + 7);
	//*/
    //inner rectangle

    g.setBackgroundColor(ColorConstants.buttonLightest);
    g.fillRoundRectangle(r2, 20, 20);
    g.drawRoundRectangle(r2 ,20,20); 
    g.drawRoundRectangle(r1,20,20);
    
    int textX= r.x + r.width / 4;
    int textY= r.y  + 30;
    int incr = (r.height - 40)/(keys.size()+attributes.size()+relationships.size()+1);
    for(int i=0;i<keys.size();i++)
    {
    	g.drawText(keys.get(i)[0], textX, textY);
    	textY += incr;
    	//*
        Image image = Activator.getImageDescriptor("icons/ELUniverse/key.png").createImage();
    	g.drawImage(image, textX - r.width/4 +r.width/8,textY - incr);
    	//*/
    }
    for(int i=0;i<attributes.size();i++)
    {
    	g.drawText(attributes.get(i)[0], textX, textY);
    	textY += incr;
    	//*
    	Image image = Activator.getImageDescriptor("icons/ELUniverse/Expert.gif").createImage();
        g.drawImage(image, textX - r.width/4 +r.width/8,textY - incr);
    	//*/  
               
    }
    for(int i=0;i<relationships.size();i++)
    {
    	g.drawText(relationships.get(i)[0], textX, textY);
    	textY += incr;
    	//*
    	Image image = Activator.getImageDescriptor("icons/ELUniverse/anchor_16x16.png").createImage();
        g.drawImage(image, textX - r.width/4 +r.width/8,textY - incr);
    	//*/  
               
    }
  
  }

private void setAnchor(String name,Point in,Point out) {

    inAnchor = new FixedAnchor(this);
    inAnchor.place = in;
    targetAnchors.put("el_in", inAnchor);
    outAnchor = new FixedAnchor(this);
    outAnchor.place = out;
    sourceAnchors.put("el_out", outAnchor);
	System.out.println(name);
}
  
}