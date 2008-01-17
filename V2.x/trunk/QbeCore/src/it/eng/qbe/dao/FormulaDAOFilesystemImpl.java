/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.qbe.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.dom4j.DocumentException;

import it.eng.qbe.bo.Formula;

/**
 * @author Andrea Gioia
 *
 */
public class FormulaDAOFilesystemImpl implements FormulaDAO {

	private File formulasDir;
	
	public FormulaDAOFilesystemImpl(File formulasDir) {
		setFormulasDir(formulasDir);
	}
	
	public Formula loadFormula(String datamartName) {
		
		Formula formula = null;
		File targetFormulaDir = null;
		File targetFormulaFile = null;
		
		targetFormulaDir = new File(getFormulasDir(), datamartName);
		targetFormulaFile = new File(targetFormulaDir, "formula.xml");
		
		
		try {
			formula = new Formula( new FileInputStream(targetFormulaFile) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return formula;
	}

	public void saveFormula(String datamartName, Formula formula) {
		// TODO Auto-generated method stub		
	}

	private File getFormulasDir() {
		return formulasDir;
	}

	private void setFormulasDir(File formulasDir) {
		this.formulasDir = formulasDir;
	}

}
