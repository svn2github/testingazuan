/*
 * Thi class activate the SpagoBI Meta Plugin
 */
package eng.it.spagobimeta;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import eng.it.spagobimeta.model.BMWrapper;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "eng.it.spagobimeta";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		//Create singleton class
		BMWrapper.getInstance();
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
     * Returns the image descriptor with the given relative path.
     */
    @SuppressWarnings("deprecation")
	public static ImageDescriptor getImageDescriptor(String relativePath) {
    	String iconPath = "icons/";
    	try {
    		eng.it.spagobimeta.Activator plugin = eng.it.spagobimeta.Activator.getDefault();
    		URL installURL = plugin.getDescriptor().getInstallURL();
    		URL url = new URL(installURL, iconPath + relativePath);
    		return ImageDescriptor.createFromURL(url);
    	}
    	catch (MalformedURLException e) {
    		// should not happen
    		return ImageDescriptor.getMissingImageDescriptor();
    	}
    }

}
