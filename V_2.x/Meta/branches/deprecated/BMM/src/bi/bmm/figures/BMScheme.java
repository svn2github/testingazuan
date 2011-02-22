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

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import bi.bmm.util.ClassInfo;
import bi.bmm.util.ComplexClassInfo;
import bi.bmm.util.WSInfoValue;

public class BMScheme {
	
   SchemeFigure scheme;
   org.eclipse.draw2d.geometry.Point position;
   
   final static int positionX = 10;
   final static int positionY = 10;
   final static int char_width = 15;
   final static int max_width = 200;
   final static int max_height = 200;
   
   public final static int ONE_TO_ONE = 1;
   public final static int ONE_TO_MANY = 2;
   public final static int MANY_TO_ONE = 3;
   public final static int MANY_TO_MANY = 4;
   
   
   
   private static final boolean DEBUG_MODE =true;
   
  public BMScheme(Composite c) {
	  
	Canvas canvas = new Canvas(c, SWT.NULL);  
	GridLayout glDet = new GridLayout();
	GridData gd = new GridData(GridData.FILL_BOTH);
	glDet.numColumns = 1;
	canvas.setLayout(glDet);
	canvas.setLayoutData(gd);
	
	LightweightSystem lws = new LightweightSystem(canvas);
    scheme = new SchemeFigure();
    lws.setContents(scheme);

    org.eclipse.swt.graphics.Point p = c.getSize();
    c.pack();
    c.setSize(p);
    position=new org.eclipse.draw2d.geometry.Point(positionX, positionY);
  }
  
 
  
  public void addClassFigure(ClassInfo ci)
  {
	  
    ClassFigure cf = new ClassFigure();
    cf.setData(ci);
    cf.setBounds(new Rectangle(position,calculateSize(ci)));
    updatePosition();
    
    scheme.add(cf);
    
    ci.setFigure(cf);
    
    new Dnd(cf);
    /*
    ClassFigure proc = new ClassFigure();
    proc.setName("Do it!");
    proc.setBounds(new Rectangle(40, 140, 80, 40));
    ClassFigure stop = new ClassFigure();
    stop.setName("End");
    stop.setBounds(new Rectangle(40, 200, 80, 20));

    PathFigure path4 = new PathFigure();
    path4.setSourceAnchor(proc.outAnchor);
    path4.setTargetAnchor(stop.inAnchor);

    scheme.add(proc);
    scheme.add(stop);
    scheme.add(path4);

    new Dnd(proc);
    new Dnd(stop);
    */
  }


private void updatePosition() {
	
	if (position.y + 40  > 700){
		position.y=positionY;
		position.x+= (40);
	}
	else{
		position.y += (40);
	}
	
}


private Dimension calculateSize(ClassInfo ci) {
	
	Point p = new Point(max_width,max_height);
	if(char_width*ci.getClassName().length()>max_width)
		p.x = char_width*ci.getClassName().length();
		p.y=char_width*(3+(ci.getKeys().size()+
				ci.getAttributes().size())+ci.getRelationships().size()+1);
		
	return new Dimension(p);
}

/*
 * TEST ZONE
 * */

public static void main( String[] args ){
	if(DEBUG_MODE)
	{
	 Shell shell = new Shell();
	    shell.setSize(600, 800);
	    shell.open();
	    shell.setText("Flowchart");
	    
	    GridLayout glDet = new GridLayout();
		GridData gd = new GridData(GridData.FILL_BOTH);
		glDet.numColumns = 1;
		shell.setLayout(glDet);
		shell.setLayoutData(gd);
	    BMScheme bms = new BMScheme(shell);


	ClassInfo ci = new ClassInfo("C:/Users/Emylio/Desktop/Prova/foodmart/src/model/AccountInfo.xml",null);
	ci.buildClassInfo();
	ci.printClassInfo();
	bms.addClassFigure(ci);
	
	
	ClassInfo ci2 = new ClassInfo("C:/Users/Emylio/Desktop/Prova/foodmart/src/model/ProductInfo.xml",null);
	ci2.buildClassInfo();
	ci2.printClassInfo();
	bms.addClassFigure(ci2);
	

	
	bms.addLinkFigure(ci.getFigure(), ci2.getFigure(), BMScheme.MANY_TO_MANY);

	
	
	Display display = Display.getDefault();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
		}
	}



public void removeClassFigure(ActivityFigure figure) {
	 ClassFigure cf = (ClassFigure) figure;
	 scheme.remove(cf);
	 cf.removeAll();
	 cf.setVisible(false);
	 cf.erase();
}



public void addLinkFigure(ActivityFigure af1, ActivityFigure af2, int type) {
	
	 AnchorFigure an1,an2;
     PathFigure path = new PathFigure();

     switch (type){
     	case BMScheme.MANY_TO_MANY: 
     		an1 = new AnchorFigure(AnchorFigure.BLACK);
     		an2 = new AnchorFigure(AnchorFigure.BLACK);
     	break;
     	
     	case BMScheme.MANY_TO_ONE:
     		an1 = new AnchorFigure(AnchorFigure.BLACK);
     		an2 = new AnchorFigure(AnchorFigure.WHITE);
     	break;
     	
     	case BMScheme.ONE_TO_MANY:
     		an1 = new AnchorFigure(AnchorFigure.WHITE);
     		an2 = new AnchorFigure(AnchorFigure.BLACK);
     	break;
        
     	default:
     		an1 = new AnchorFigure(AnchorFigure.WHITE);
     		an2 = new AnchorFigure(AnchorFigure.WHITE);
     }
     
     an1.setBounds(new Rectangle(af1.getBounds().width-10,af1.getBounds().y,10,10));
     an2.setBounds(new Rectangle(af2.getBounds().x+10,af2.getBounds().y,10,10));
     
     af1.add(an1);
     af2.add(an2);
     
     path.setSourceAnchor(an1.outAnchor);
     path.setTargetAnchor(an2.inAnchor);
     
     scheme.add(path);
     
     new Dnd(an2);
     new Dnd(an1);
}

public void removeLinkFigure(ActivityFigure af1, ActivityFigure af2, int type) {
	
	 AnchorFigure an1,an2;
    PathFigure path = new PathFigure();

    switch (type){
    	case BMScheme.MANY_TO_MANY: 
    		an1 = new AnchorFigure(AnchorFigure.BLACK);
    		an2 = new AnchorFigure(AnchorFigure.BLACK);
    	break;
    	
    	case BMScheme.MANY_TO_ONE:
    		an1 = new AnchorFigure(AnchorFigure.BLACK);
    		an2 = new AnchorFigure(AnchorFigure.WHITE);
    	break;
    	
    	case BMScheme.ONE_TO_MANY:
    		an1 = new AnchorFigure(AnchorFigure.WHITE);
    		an2 = new AnchorFigure(AnchorFigure.BLACK);
    	break;
       
    	default:
    		an1 = new AnchorFigure(AnchorFigure.WHITE);
    		an2 = new AnchorFigure(AnchorFigure.WHITE);
    }
    
    an1.setBounds(new Rectangle(af1.getBounds().width-10,af1.getBounds().y,10,10));
    an2.setBounds(new Rectangle(af2.getBounds().x+10,af2.getBounds().y,10,10));
    
    af1.add(an1);
    af2.add(an2);
    
    path.setSourceAnchor(an1.outAnchor);
    path.setTargetAnchor(an2.inAnchor);
    
    scheme.add(path);
    
    new Dnd(an2);
    new Dnd(an1);
}


public void clear() {
	scheme.removeAll();
}



public void addComplexClassFigure(ComplexClassInfo cci) {
	  
    ComplexClassFigure cf = new ComplexClassFigure();

    cf.setComplexData(cci);
    

	
	
    cf.setBounds(new Rectangle(position,calculateComplexSize(cci)));
    updatePosition();
    
    scheme.add(cf);
   
    cci.setFigure(cf);
    
    new Dnd(cf);
	
}



private Dimension calculateComplexSize(ComplexClassInfo cci) {
	Point p = new Point(max_width,max_height);
	if(char_width*cci.getClassName().length()>max_width)
		p.x = char_width*cci.getClassName().length();
		
		int fieldNumber = 0;
	
		HashMap<String, ArrayList<String[]>> bcs = cci.getInhBC();
		Iterator<String> iter = bcs.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			fieldNumber += bcs.get(key).size();
		}
		
		HashMap<String[], WSInfoValue> wss = cci.getInhWS();
		Iterator<String[]> it = wss.keySet().iterator();
		while(it.hasNext()){
			it.next();
			fieldNumber++;
		}
		
		
		p.y=char_width*(3+(fieldNumber)+1);
		
	return new Dimension(p);
}

}