/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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

**/
package it.eng.spagobi.meta.generator;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class GeneratorFactory {
	private static List<GeneratorDescriptor> generatorDescriptorsCache = null;	
	
	public static Collection<GeneratorDescriptor> getGeneratorDescriptors() {
		if(generatorDescriptorsCache == null) {
			initGeneratorDescriptorsCache();
		}
		
		return new ArrayList<GeneratorDescriptor>(generatorDescriptorsCache);
	}
	
	public static GeneratorDescriptor getGeneratorDescriptorById(String id){
		if(generatorDescriptorsCache == null) {
			initGeneratorDescriptorsCache();
		}
		
		for(GeneratorDescriptor descriptor : generatorDescriptorsCache) {
			if(descriptor.getId().equals(id)) return descriptor;
		}
		return null;
	}
	
	private static void initGeneratorDescriptorsCache() {
		generatorDescriptorsCache = new ArrayList<GeneratorDescriptor>();
		
		IExtension[] extensions = Platform.getExtensionRegistry()
		.getExtensionPoint(SpagoBIMetaGeneratorPlugin.PLUGIN_ID, "generator")
		.getExtensions();
		
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				if (!configElements[i].getName().equals("generator")) continue;
				GeneratorDescriptor generatorDescriptor = buildGeneratorDescriptor(configElements[i]);
				if(generatorDescriptor != null) {
					generatorDescriptorsCache.add(generatorDescriptor);
				}
			}
		}
	}

	private static GeneratorDescriptor buildGeneratorDescriptor(IConfigurationElement configElement) {
		try {
			return new GeneratorDescriptor(configElement);
		} catch (Exception e) {
			String name = configElement.getAttribute("name");
			if (name == null) name = "[missing name attribute]";
			String msg =
				"Failed to load generator named "
				+ name
				+ " in "
				+ configElement.getDeclaringExtension().getNamespaceIdentifier();
				
			IStatus status = new Status(IStatus.ERROR, SpagoBIMetaGeneratorPlugin.PLUGIN_ID, IStatus.OK, msg, e);
			Bundle plugin = Platform.getBundle(SpagoBIMetaGeneratorPlugin.PLUGIN_ID);
			Platform.getLog(plugin).log(status);
			return null;
		}
	}
}
