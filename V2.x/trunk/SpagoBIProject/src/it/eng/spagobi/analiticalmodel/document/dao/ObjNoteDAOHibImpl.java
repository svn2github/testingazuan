/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
/*
 * Created on 21-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.analiticalmodel.document.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.ObjNote;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjNotes;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiBinContents;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ObjNoteDAOHibImpl extends AbstractHibernateDAO implements IObjNoteDAO {

	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IObjNoteDAO#getExecutionNotes(java.lang.Integer, java.lang.String)
	 */
	public ObjNote getExecutionNotes(Integer biobjId, String execIdentif) throws Exception {
		ObjNote objNote = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			//String hql = "from SbiObjNotes son where son.sbiObject.biobjId = " + biobjId + 
			//			 " and son.execReq = '"+execIdentif+"'";
			
			String hql = "from SbiObjNotes son where son.sbiObject.biobjId = ?"  + 
			 " and son.execReq = ?";
			Query query = aSession.createQuery(hql);
			query.setInteger(0, biobjId.intValue());
			query.setString(1, execIdentif);
			
			SbiObjNotes hibObjNote = (SbiObjNotes)query.uniqueResult();
			if(hibObjNote!=null) {
				objNote = toObjNote(hibObjNote);
			}
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return objNote;
	}

	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IObjNoteDAO#saveExecutionNotes(java.lang.Integer, it.eng.spagobi.analiticalmodel.document.bo.ObjNote)
	 */
	public void saveExecutionNotes(Integer biobjId, ObjNote objNote) throws Exception {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String execReq = objNote.getExecReq();
			SbiObjects hibBIObject = (SbiObjects) aSession.load(SbiObjects.class, biobjId);
			SbiBinContents hibBinContent = new SbiBinContents();
			hibBinContent.setContent(objNote.getContent());
			Integer idBin = (Integer)aSession.save(hibBinContent);
			// recover the saved binary hibernate object
			hibBinContent = (SbiBinContents) aSession.load(SbiBinContents.class, idBin);
			// store the object note
			SbiObjNotes hibObjNote = new SbiObjNotes();
			hibObjNote.setExecReq(execReq);
			hibObjNote.setSbiBinContents(hibBinContent);
			hibObjNote.setSbiObject(hibBIObject);
			aSession.save(hibObjNote);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IObjNoteDAO#modifyExecutionNotes(it.eng.spagobi.analiticalmodel.document.bo.ObjNote)
	 */
	public void modifyExecutionNotes(ObjNote objNote) throws Exception {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjNotes hibObjNote = (SbiObjNotes)aSession.load(SbiObjNotes.class, objNote.getId());
			SbiBinContents hibBinCont = hibObjNote.getSbiBinContents();
			hibBinCont.setContent(objNote.getContent());
			hibObjNote.setExecReq(objNote.getExecReq());
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	
	
	private ObjNote toObjNote(SbiObjNotes hibnotes) {
		ObjNote objNote = new ObjNote();
		objNote.setBinId(hibnotes.getSbiBinContents().getId());
		objNote.setBiobjId(hibnotes.getSbiObject().getBiobjId());
		objNote.setContent(hibnotes.getSbiBinContents().getContent());
		objNote.setExecReq(hibnotes.getExecReq());
		objNote.setId(hibnotes.getObjNoteId());
		return objNote;
	}



	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IObjNoteDAO#eraseNotes(java.lang.Integer)
	 */
	public void eraseNotes(Integer biobjId) throws Exception {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			//String hql = "from SbiObjNotes son where son.sbiObject.biobjId = " + biobjId;
			String hql = "from SbiObjNotes son where son.sbiObject.biobjId = ?" ;
			Query query = aSession.createQuery(hql);
			query.setInteger(0, biobjId.intValue());
			List notes = query.list();
			Iterator notesIt = notes.iterator();
			while (notesIt.hasNext()) {
				SbiObjNotes note = (SbiObjNotes) notesIt.next();
				SbiBinContents noteBinContent = note.getSbiBinContents();
				aSession.delete(note);
				aSession.delete(noteBinContent);
			}
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		
	}
	
	

}
