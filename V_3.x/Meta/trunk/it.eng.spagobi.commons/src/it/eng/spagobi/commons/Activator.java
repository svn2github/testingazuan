package it.eng.spagobi.commons;

import java.io.File;
import java.net.URL;

import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.commons.resource.DefaultResourceLocator;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		//System.err.println("Bundle equals to: "+Platform.getBundle("it.eng.spagobi.commons"));


		File logback = null;
		try {
			DefaultResourceLocator rs = new DefaultResourceLocator("it.eng.spagobi.commons");
			String fileRelativePath = rs.getPropertyAsString("logback.configurationFile.dir");
			//System.err.println("fileRelativePath: "+fileRelativePath);
			logback = rs.getFile(fileRelativePath);
			//System.err.println("Absolute path: "+logback.getAbsolutePath());
		} catch(Throwable t) {
			t.printStackTrace();
		}
		
		String logbackConfiguration = System.setProperty("logback.configurationFile", logback.getAbsolutePath());
		//System.err.println("property: "+System.getProperty("logback.configurationFile"));
		//System.err.println("OUT");
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
