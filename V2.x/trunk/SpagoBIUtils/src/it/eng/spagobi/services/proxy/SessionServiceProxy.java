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
package it.eng.spagobi.services.proxy;

import java.rmi.RemoteException;
import java.util.HashMap;

import it.eng.spagobi.services.session.bo.Document;
import it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException;
import it.eng.spagobi.services.session.bo.DocumentParameter;
import it.eng.spagobi.services.session.stub.WSSessionServiceSoapBindingStub;

public class SessionServiceProxy implements
		it.eng.spagobi.services.session.stub.SessionService {
	private String _endpoint = null;
	private it.eng.spagobi.services.session.stub.SessionService sessionService = null;
	private Long sessionId = null;

	public SessionServiceProxy() {
		_initSessionServiceProxy();
	}

	public SessionServiceProxy(Long sessionId) {
		_initSessionServiceProxy();
		this.sessionId = sessionId;
	}

	public SessionServiceProxy(String endpoint) {
		_endpoint = endpoint;
		_initSessionServiceProxy();
	}

	private void _initSessionServiceProxy() {
		try {
			sessionService = (new it.eng.spagobi.services.session.stub.SessionServiceServiceLocator())
					.getWSSessionService();
			if (sessionService != null) {
				if (_endpoint != null)
					((javax.xml.rpc.Stub) sessionService)
							._setProperty(
									"javax.xml.rpc.service.endpoint.address",
									_endpoint);
				else
					_endpoint = (String) ((javax.xml.rpc.Stub) sessionService)
							._getProperty("javax.xml.rpc.service.endpoint.address");
			}

		} catch (javax.xml.rpc.ServiceException serviceException) {
		}
	}

	public String getEndpoint() {
		return _endpoint;
	}

	public void setEndpoint(String endpoint) {
		_endpoint = endpoint;
		if (sessionService != null)
			((javax.xml.rpc.Stub) sessionService)._setProperty(
					"javax.xml.rpc.service.endpoint.address", _endpoint);

	}

	public it.eng.spagobi.services.session.stub.SessionService getSessionService() {
		if (sessionService == null)
			_initSessionServiceProxy();
		return sessionService;
	}

	public void openSession(java.lang.String in0, java.lang.String in1)
			throws java.rmi.RemoteException,
			it.eng.spagobi.services.session.exceptions.AuthenticationException {
		if (sessionService == null)
			_initSessionServiceProxy();
		if (this.sessionId != null) {
			((WSSessionServiceSoapBindingStub) sessionService)
					.setSessionId(this.sessionId);
		}
		sessionService.openSession(in0, in1);
		this.sessionId = ((WSSessionServiceSoapBindingStub) sessionService)
				.getSessionId();
	}

	public void openSessionWithToken(java.lang.String in0, java.lang.String in1)
			throws java.rmi.RemoteException,
			it.eng.spagobi.services.session.exceptions.AuthenticationException {
		if (sessionService == null)
			_initSessionServiceProxy();
		if (this.sessionId != null) {
			((WSSessionServiceSoapBindingStub) sessionService)
					.setSessionId(this.sessionId);
		}
		sessionService.openSessionWithToken(in0, in1);
		this.sessionId = ((WSSessionServiceSoapBindingStub) sessionService)
				.getSessionId();
	}

	public void closeSession() throws java.rmi.RemoteException {
		if (sessionService == null)
			_initSessionServiceProxy();
		if (this.sessionId != null) {
			((WSSessionServiceSoapBindingStub) sessionService)
					.setSessionId(this.sessionId);
		}
		sessionService.closeSession();
		this.sessionId = null;
	}

	public HashMap getAdmissibleValues(Integer in0, String in1)
			throws RemoteException, NonExecutableDocumentException {
		if (sessionService == null)
			_initSessionServiceProxy();
		if (this.sessionId != null) {
			((WSSessionServiceSoapBindingStub) sessionService)
					.setSessionId(this.sessionId);
		}
		HashMap<String, String> values = sessionService.getAdmissibleValues(in0, in1);
		this.sessionId = ((WSSessionServiceSoapBindingStub) sessionService)
				.getSessionId();
		return values;
	}

	public String[] getCorrectRolesForExecution(Integer in0)
			throws RemoteException, NonExecutableDocumentException {
		if (sessionService == null)
			_initSessionServiceProxy();
		if (this.sessionId != null) {
			((WSSessionServiceSoapBindingStub) sessionService)
					.setSessionId(this.sessionId);
		}
		String[] roles = sessionService.getCorrectRolesForExecution(in0);
		this.sessionId = ((WSSessionServiceSoapBindingStub) sessionService)
				.getSessionId();
		return roles;
	}

	public DocumentParameter[] getDocumentParameters(Integer in0, String in1)
			throws RemoteException, NonExecutableDocumentException {
		if (sessionService == null)
			_initSessionServiceProxy();
		if (this.sessionId != null) {
			((WSSessionServiceSoapBindingStub) sessionService)
					.setSessionId(this.sessionId);
		}
		DocumentParameter[] parameters = sessionService.getDocumentParameters(in0, in1);
		this.sessionId = ((WSSessionServiceSoapBindingStub) sessionService)
				.getSessionId();
		return parameters;
	}

	public Document[] getDocuments(String in0, String in1, String in2)
			throws RemoteException {
		if (sessionService == null)
			_initSessionServiceProxy();
		if (this.sessionId != null) {
			((WSSessionServiceSoapBindingStub) sessionService)
					.setSessionId(this.sessionId);
		}
		Document[] documents = sessionService.getDocuments(in0, in1, in2);
		this.sessionId = ((WSSessionServiceSoapBindingStub) sessionService)
				.getSessionId();
		return documents;
	}

	public boolean isValidSession() throws RemoteException {
		if (sessionService == null)
			_initSessionServiceProxy();
		if (this.sessionId != null) {
			((WSSessionServiceSoapBindingStub) sessionService)
					.setSessionId(this.sessionId);
		}
		boolean isValid = sessionService.isValidSession();
		this.sessionId = ((WSSessionServiceSoapBindingStub) sessionService)
				.getSessionId();
		return isValid;
	}
	
	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

}