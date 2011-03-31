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
package it.eng.spagobi.meta.model.business.presentation;

import it.eng.spagobi.meta.model.editor.SpagoBIMetaModelEditorPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BusinessModelResourceChangeListener implements IResourceChangeListener {
	
	AdapterFactoryEditingDomain editingDomain;
	BusinessModelEditor editor;
	
	/**
	 * Resources that have been removed since last activation.
	 */
	protected Collection<Resource> removedResources = new ArrayList<Resource>();

	/**
	 * Resources that have been changed since last activation.
	 */
	protected Collection<Resource> changedResources = new ArrayList<Resource>();

	/**
	 * Resources that have been saved.
	 */
	protected Collection<Resource> savedResources = new ArrayList<Resource>();
	
	public Collection<Resource> getSavedResources() {
		return savedResources;
	}

	public BusinessModelResourceChangeListener(AdapterFactoryEditingDomain editingDomain, BusinessModelEditor editor) {
		this.editingDomain = editingDomain;
		this.editor = editor;
	}
	
	public boolean isDirty() {
		return ((BasicCommandStack)editingDomain.getCommandStack()).isSaveNeeded();
	}
	
	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		try {
			class ResourceDeltaVisitor implements IResourceDeltaVisitor {
				protected ResourceSet resourceSet = editingDomain.getResourceSet();
				protected Collection<Resource> changedResources = new ArrayList<Resource>();
				protected Collection<Resource> removedResources = new ArrayList<Resource>();

				public boolean visit(IResourceDelta delta) {
					if (delta.getResource().getType() == IResource.FILE) {
						if (delta.getKind() == IResourceDelta.REMOVED ||
						    delta.getKind() == IResourceDelta.CHANGED && delta.getFlags() != IResourceDelta.MARKERS) {
							Resource resource = resourceSet.getResource(URI.createPlatformResourceURI(delta.getFullPath().toString(), true), false);
							if (resource != null) {
								if (delta.getKind() == IResourceDelta.REMOVED) {
									removedResources.add(resource);
								}
								else if (!savedResources.remove(resource)) {
									changedResources.add(resource);
								}
							}
						}
					}

					return true;
				}

				public Collection<Resource> getChangedResources() {
					return changedResources;
				}

				public Collection<Resource> getRemovedResources() {
					return removedResources;
				}
			}

			final ResourceDeltaVisitor visitor = new ResourceDeltaVisitor();
			delta.accept(visitor);

			if (!visitor.getRemovedResources().isEmpty()) {
				editor.getSite().getShell().getDisplay().asyncExec
					(new Runnable() {
						 public void run() {
							 removedResources.addAll(visitor.getRemovedResources());
							 if (!isDirty()) {
								 editor.getSite().getPage().closeEditor(editor, false);
							 }
						 }
					 });
			}

			if (!visitor.getChangedResources().isEmpty()) {
				editor.getSite().getShell().getDisplay().asyncExec
					(new Runnable() {
						 public void run() {
							 changedResources.addAll(visitor.getChangedResources());
							 if (editor.getSite().getPage().getActiveEditor() == editor) {
								 handleActivate();
							 }
						 }
					 });
			}
		}
		catch (CoreException exception) {
			SpagoBIMetaModelEditorPlugin.INSTANCE.log(exception);
		}
	}
	
	/**
	 * Handles activation of the editor or it's associated views.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void handleActivate() {
		// Recompute the read only state.
		//
		if (editingDomain.getResourceToReadOnlyMap() != null) {
		  editingDomain.getResourceToReadOnlyMap().clear();

		  // Refresh any actions that may become enabled or disabled.
		  //
		  editor.setSelection(editor.getSelection());
		}

		if (!removedResources.isEmpty()) {
			if (editor.handleDirtyConflict()) {
				editor.getSite().getPage().closeEditor(editor, false);
			}
			else {
				removedResources.clear();
				changedResources.clear();
				savedResources.clear();
			}
		}
		else if (!changedResources.isEmpty()) {
			changedResources.removeAll(savedResources);
			handleChangedResources();
			changedResources.clear();
			savedResources.clear();
		}
	}
	
	/**
	 * Handles what to do with changed resources on activation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void handleChangedResources() {
		if (!changedResources.isEmpty() && (!isDirty() || editor.handleDirtyConflict())) {
			if (isDirty()) {
				changedResources.addAll(editingDomain.getResourceSet().getResources());
			}
			editingDomain.getCommandStack().flush();

			editor.setUpdateProblemIndication(false);
			for (Resource resource : changedResources) {
				if (resource.isLoaded()) {
					resource.unload();
					try {
						resource.load(Collections.EMPTY_MAP);
					}
					catch (IOException exception) {
						if (!editor.getResourceToDiagnosticMap().containsKey(resource)) {
							editor.getResourceToDiagnosticMap().put(resource, editor.analyzeResourceProblems(resource, exception));
						}
					}
				}
			}

			if (AdapterFactoryEditingDomain.isStale(editor.getEditorSelection())) {
				editor.setSelection(StructuredSelection.EMPTY);
			}

			editor.setUpdateProblemIndication(true);
			editor.updateProblemIndication();
		}
	}
  
}

