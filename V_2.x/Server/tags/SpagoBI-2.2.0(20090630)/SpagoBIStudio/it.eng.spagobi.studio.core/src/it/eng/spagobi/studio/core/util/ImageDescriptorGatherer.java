package it.eng.spagobi.studio.core.util;

import it.eng.spagobi.studio.core.Activator;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ImageDescriptorGatherer {

	public static ImageDescriptor getImageDesc(String imageName) {
		String imagePath = "icons" + File.separator + imageName;
		ImageDescriptor imagePathDesc = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, imagePath);
		return imagePathDesc;
	}
	


}
