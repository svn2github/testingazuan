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
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import bi.bmm.Activator;
import bi.bmm.util.WSInfoValue;

class ComplexClassFigure extends ActivityFigure {
  public FixedAnchor inAnchor, outAnchor;

  public ComplexClassFigure() {
	  setAnchor("", new Point(0,1), new Point(2,1));
  }

  public void paintFigure(Graphics g) {
    Rectangle r = bounds;
    HashMap<String, ArrayList<String[]>> bcs = complexData.getInhBC();
    HashMap<String[], WSInfoValue> wss = complexData.getInhWS();
    
    Rectangle r1 = new Rectangle(r.x+1, r.y+1, r.width-2, r.height-2);
    Rectangle r2 =new Rectangle (r.x + 10,r.y + 30,r.width-20,r.height -40);
    //outer rectangle
    g.setBackgroundColor(ColorConstants.button);
    g.fillRoundRectangle(r1,20,20);
    g.drawText(complexData.getClassName().toString(), r.x + 3 * r.width / 8, r.y + 10);
    //*
    Image img = Activator.getImageDescriptor("icons/ELUniverse/CBC_20x20.png").createImage();
    g.drawImage(img, r.x + 3 * r.width / 8 - 30, r.y + 7);
	//*/
    //inner rectangle

    g.setBackgroundColor(ColorConstants.buttonLightest);
    g.fillRoundRectangle(r2, 20, 20);
    g.drawRoundRectangle(r2 ,20,20); 
    g.drawRoundRectangle(r1,20,20);
    
    int textX= r.x + r.width / 4;
    int textY= r.y  + 30;
    
    int fieldNumber = 0;
	if(!bcs.isEmpty()){
		Iterator<String> iter = bcs.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			fieldNumber += bcs.get(key).size();
		}
	}
	if(wss.isEmpty()){
		Iterator<String[]> it = wss.keySet().iterator();
		while(it.hasNext()){
			String[] key = it.next();
			fieldNumber += wss.get(key).getParametersMappings().size();
		}
	}
    int incr = (r.height - 40)/(fieldNumber+1);
    
  //Disegno le BCFIELDS
    Iterator<String> iter = bcs.keySet().iterator();
     while(iter.hasNext()){
     	String key = iter.next();
     	ArrayList<String[]> list = bcs.get(key);
     	for (int i =0; i< list.size();i++)
     	{
 	    		
 	    	g.drawText(list.get(i)[0], textX, textY);
 	    	textY += incr;
 	    	//*
 	        Image image = Activator.getImageDescriptor("icons/ELUniverse/dot.png").createImage();
 	    	g.drawImage(image, textX - r.width/4 +r.width/8,textY - incr);
 	    	//*/
     	}
     }
    //Disegno le WSFIELDS
    Iterator<String[]> iter2 = wss.keySet().iterator();
    while(iter2.hasNext()){
    	String[] key = iter2.next();
    			
	    	g.drawText(key[0], textX, textY);
	    	textY += incr;
	    	//*
	        Image image = Activator.getImageDescriptor("icons/ELUniverse/dot_yellow.png").createImage();
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