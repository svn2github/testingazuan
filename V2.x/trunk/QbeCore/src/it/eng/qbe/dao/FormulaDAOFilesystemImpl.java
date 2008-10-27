/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.dao;

import it.eng.qbe.bo.Formula;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.dom4j.DocumentException;

// TODO: Auto-generated Javadoc
/**
 * The Class FormulaDAOFilesystemImpl.
 * 
 * @author Andrea Gioia
 */
public class FormulaDAOFilesystemImpl implements FormulaDAO {

	/** The formulas dir. */
	private File formulasDir;
	
	/**
	 * Instantiates a new formula dao filesystem impl.
	 * 
	 * @param formulasDir the formulas dir
	 */
	public FormulaDAOFilesystemImpl(File formulasDir) {
		setFormulasDir(formulasDir);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.dao.FormulaDAO#loadFormula(java.lang.String)
	 */
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
			e.printStackTrace();
		}
		
		return formula;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.dao.FormulaDAO#saveFormula(java.lang.String, it.eng.qbe.bo.Formula)
	 */
	public void saveFormula(String datamartName, Formula formula) {
		// TODO Auto-generated method stub		
	}

	/**
	 * Gets the formulas dir.
	 * 
	 * @return the formulas dir
	 */
	private File getFormulasDir() {
		return formulasDir;
	}

	/**
	 * Sets the formulas dir.
	 * 
	 * @param formulasDir the new formulas dir
	 */
	private void setFormulasDir(File formulasDir) {
		this.formulasDir = formulasDir;
	}

}
