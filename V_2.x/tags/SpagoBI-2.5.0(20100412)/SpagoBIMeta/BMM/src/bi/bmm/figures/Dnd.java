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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import bi.bmm.wizards.DetailBCWizard;

public class Dnd extends MouseMotionListener.Stub implements MouseListener {
  public Dnd(IFigure figure) {
    figure.addMouseMotionListener(this);
    figure.addMouseListener(this);
  }

  Point start;

  public void mouseReleased(MouseEvent e) {
  }

  public void mouseClicked(MouseEvent e) {
  }

  public void mouseDoubleClicked(MouseEvent e) {
	  ActivityFigure f = ((ActivityFigure)e.getSource());
	  DetailBCWizard wizard = new DetailBCWizard(f.data,f);
  	  WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		dialog.open();
		/*
		if(dialog.getReturnCode() == WizardDialog.OK){
			//recupero le informazioni sulla classe creata
		}*/
  }

  public void mousePressed(MouseEvent e) {
    start = e.getLocation();
  }

  public void mouseDragged(MouseEvent e) {
    Point p = e.getLocation();
    Dimension d = p.getDifference(start);
    start = p;
    Figure f = ((Figure) e.getSource());
    f.setBounds(f.getBounds().getTranslated(d.width, d.height));
    f.repaint();
  }
}
