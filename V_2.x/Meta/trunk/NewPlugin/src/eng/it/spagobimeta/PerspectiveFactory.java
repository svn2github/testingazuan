/*
 * This class creates the SpagoBIMeta Perspective with the necessary views
 */
package eng.it.spagobimeta;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


public class PerspectiveFactory implements IPerspectiveFactory {

	
	private static final String VIEW_ID = "org.eclipse.datatools.connectivity.DataSourceExplorerNavigator";
	private static final String VIEW2_ID = "eng.it.spagobimeta.GraphicEditorView";
	private static final String VIEW3_ID = "eng.it.spagobimeta.DBStructureView";
	private static final String VIEW4_ID = "eng.it.spagobimeta.PropertiesView";
	
	@Override
	public void createInitialLayout(IPageLayout myLayout) {
		//Code to Hide Eclipse Default Toolbar
		//ActionFactory.IWorkbenchAction toggleToolbar = ActionFactory.TOGGLE_COOLBAR.create(PlatformUI.getWorkbench().getActiveWorkbenchWindow()); 
		//toggleToolbar.run();
		
		String editorAreaId = myLayout.getEditorArea();
		myLayout.setEditorAreaVisible( false );
		myLayout.setFixed( true );

		myLayout.createFolder("LEFT", IPageLayout.LEFT, 0.20f, editorAreaId ).addView(VIEW_ID);
		myLayout.createFolder("RIGHT", IPageLayout.RIGHT, 0.60f, editorAreaId ).addView(VIEW2_ID);
		myLayout.createFolder("TOP", IPageLayout.TOP, 0.60f, VIEW_ID ).addView(VIEW3_ID);
		myLayout.createFolder("BOTTOM", IPageLayout.BOTTOM, 0.60f, VIEW2_ID ).addView(VIEW4_ID);
		    
	}

}
