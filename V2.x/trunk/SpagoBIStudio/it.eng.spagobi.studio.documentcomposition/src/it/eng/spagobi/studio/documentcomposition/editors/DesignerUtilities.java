package it.eng.spagobi.studio.documentcomposition.editors;

import org.eclipse.swt.widgets.Composite;

public class DesignerUtilities {

	
	public static int getScaledImageWidth(int originalWidth, int targetWidth){
		double rapportoWidth=(double)targetWidth / (double)originalWidth;
		int newWidth=(int)(originalWidth*rapportoWidth);
		return newWidth;
	}
	
	public static int getScaledImageHeight(int originalHeight, int targetHeight){
		double rapportoHeight=(double)targetHeight / (double)originalHeight;
		int newHeight=(int)(originalHeight*rapportoHeight);
		return newHeight;
	}



	public int calculateWidth(Composite currentComposite, int containerSize){
		// if componentSIze is almost the container size round it to that!		
		int width=currentComposite.getBounds().width;
		int x=currentComposite.getBounds().x;

		// total x
		int totalX=width+x;
		int toReturn;
		if((containerSize-totalX)<=DocContainer.ALIGNMENT_MARGIN){
			// add difference between actual width and remaining with limit
			toReturn=width+((containerSize-totalX));
		}
		else{		// else round it to less
			toReturn=width/DocContainer.ALIGNMENT_MARGIN;
			toReturn=toReturn*DocContainer.ALIGNMENT_MARGIN;			
		}
		return toReturn;
	}
	
	
	public int calculateHeight(Composite currentComposite, int containerSize){
		// if componentSIze is almost the container size round it to that!		
		int height=currentComposite.getBounds().height;
		int y=currentComposite.getBounds().y;

		// total y
		int totalY=height+y;
		int toReturn;
		if((containerSize-totalY)<=DocContainer.ALIGNMENT_MARGIN){
			// add difference between actual width and remaining with limit
			toReturn=height+((containerSize-totalY));
		}
		else{		// else round it to less
			toReturn=height/DocContainer.ALIGNMENT_MARGIN;
			toReturn=toReturn*DocContainer.ALIGNMENT_MARGIN;			
		}
		return toReturn;
	}


}
