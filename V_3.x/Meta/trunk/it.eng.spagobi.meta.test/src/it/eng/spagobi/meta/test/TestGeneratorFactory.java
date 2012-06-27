/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.test;

import it.eng.spagobi.meta.generator.jpamapping.JpaMappingClassesGenerator;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingCodeGenerator;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;

import java.io.File;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class TestGeneratorFactory {
	public static JpaMappingCodeGenerator createCodeGeneraor() {
		File generatorProjectRootFolder = new File(TestCostants.workspaceFolder, "it.eng.spagobi.meta.generator");
		File generatorProjectTemplateFolder = new File(generatorProjectRootFolder, "templates");
		JpaMappingCodeGenerator.defaultTemplateFolderPath = generatorProjectTemplateFolder.toString();
		JpaMappingCodeGenerator jpaMappingCodeGenerator = new JpaMappingCodeGenerator();
		return jpaMappingCodeGenerator;	
	}
	
	public static JpaMappingClassesGenerator createClassesGeneraor() {
		File generatorProjectRootFolder = new File(TestCostants.workspaceFolder, "it.eng.spagobi.meta.generator");
		File generatorProjectTemplateFolder = new File(generatorProjectRootFolder, "templates");
		File generatorProjectLibFolder = new File(generatorProjectRootFolder, "libs/eclipselink");
		
		JpaMappingClassesGenerator jpaMappingClassesGenerator;
		JpaMappingClassesGenerator.defaultTemplateFolderPath = generatorProjectTemplateFolder.toString();
		jpaMappingClassesGenerator = new JpaMappingJarGenerator();
		
		jpaMappingClassesGenerator.setLibDir( generatorProjectLibFolder );
		jpaMappingClassesGenerator.setLibs(new String[]{
				"org.eclipse.persistence.core_2.1.2.jar",
				"javax.persistence_2.0.1.jar"
		});	
		return jpaMappingClassesGenerator;
	}
	
	public static JpaMappingJarGenerator createJarGeneraor() {
		JpaMappingJarGenerator jpaMappingJarGenerator;
		JpaMappingJarGenerator.defaultTemplateFolderPath = "D:/Documenti/Sviluppo/workspaces/helios/metadata/it.eng.spagobi.meta.generator/templates";
		jpaMappingJarGenerator = new JpaMappingJarGenerator();
		File projectRootFolder = new File("D:/Documenti/Sviluppo/workspaces/helios/metadata/it.eng.spagobi.meta.generator");
		jpaMappingJarGenerator.setLibDir(new File(projectRootFolder, "libs/eclipselink"));
		jpaMappingJarGenerator.setLibs(new String[]{
				"org.eclipse.persistence.core_2.1.2.jar",
				"javax.persistence_2.0.1.jar"
		});	
		return jpaMappingJarGenerator;
	}
}
