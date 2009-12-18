package org.eclipse.ui.articles.views;

import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.resources.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class ViewsPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static ViewsPlugin plugin;
	
	/**
	 * The constructor.
	 */
	public ViewsPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ViewsPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
}
