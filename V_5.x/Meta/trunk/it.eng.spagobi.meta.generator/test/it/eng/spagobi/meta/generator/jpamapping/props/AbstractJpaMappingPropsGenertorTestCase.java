/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.generator.jpamapping.props;

import it.eng.spagobi.meta.generator.jpamapping.AbstractJpaMappingGeneratorTestCase;

import java.io.File;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractJpaMappingPropsGenertorTestCase extends AbstractJpaMappingGeneratorTestCase{
	public static final File OUTPUT_FOLDER = new File(AbstractJpaMappingGeneratorTestCase.OUTPUT_FOLDER, "props");

	public void doTest() {
		super.doTest();
	}
}
