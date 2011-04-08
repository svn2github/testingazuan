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

		DefaultResourceLocator rs = new DefaultResourceLocator("it.eng.spagobi.commons");

		String fileRelativePath = rs.getPropertyAsString("logback.configurationFile.dir");
		System.out.println("fileRelativePath: "+fileRelativePath);
		File logback = rs.getFile(fileRelativePath);
		System.out.println("Absolute path: "+logback.getAbsolutePath());
		
		String logbackConfiguration = System.setProperty("logback.configurationFile", "c:\\logback.xml");
		System.out.println("property: "+System.getProperty("logback.configurationFile"));

	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
