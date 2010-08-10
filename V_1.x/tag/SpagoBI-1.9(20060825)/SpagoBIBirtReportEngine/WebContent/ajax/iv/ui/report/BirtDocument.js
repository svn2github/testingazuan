/******************************************************************************
 *	Copyright (c) 2004 Actuate Corporation and others.
 *	All rights reserved. This program and the accompanying materials 
 *	are made available under the terms of the Eclipse Public License v1.0
 *	which accompanies this distribution, and is available at
 *		http://www.eclipse.org/legal/epl-v10.html
 *	
 *	Contributors:
 *		Actuate Corporation - Initial implementation.
 *****************************************************************************/
 
/**
 *	BirtReportDocument
 *	...
 */
BirtReportDocument = Class.create( );

BirtReportDocument.prototype = Object.extend( new BirtReportBase( "BirtReportDocument" ),
{
	__instance : null,
	__has_svg_support : false,
	
	__neh_resize_closure : null,
	__beh_toc_closure : null,
	__beh_getPage_closure : null,
	__beh_changeParameter_closure : null,
		
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *
	 *	@return, void
	 */
	initialize : function( id )
	{
		this.__instance = $( id );
		
		this.__neh_resize( );

		this.superClass = new BirtReportBase( "BirtReportDocument.initialize" );
			
		this.componentContextItems.test = { displayName: "Document Test Action", action: this.__neh_testAction, target: this };
		
		this.__has_svg_support = hasSVGSupport;
		
		//	Prepare closures.
//		this.__neh_select_closure = this.__neh_click.bindAsEventListener( this );
		this.__neh_contextMenu_closure = this.__neh_contextMenu.bindAsEventListener( this );
		this.__neh_resize_closure = this.__neh_resize.bindAsEventListener( this );
		this.__beh_getPage_closure = this.__beh_getPage.bind( this );
		this.__beh_changeParameter_closure = this.__beh_changeParameter.bind( this );

		this.__beh_toc_closure = this.__beh_toc.bindAsEventListener( this );
		
		// TODO: group these in a method. they don't need to be unregistered.
//		Event.observe( this.__instance, 'click', this.__neh_select_closure, false );
//		Event.observe( this.__instance, 'contextmenu', this.__neh_contextMenu_closure, false );
		
		Event.observe( window, 'resize', this.__neh_resize_closure, false );
		
		birtEventDispatcher.registerEventHandler( birtEvent.__E_GETPAGE, this.__instance.id, this.__beh_getPage_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_PARAMETER, this.__instance.id, this.__beh_parameter );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_CHANGE_PARAMETER, this.__instance.id, this.__beh_changeParameter_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_CASCADING_PARAMETER, this.__instance.id, this.__beh_cascadingParameter );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_TOC, this.__instance.id, this.__beh_toc_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_PRINT, this.__instance.id, this.__beh_print );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_QUERY_EXPORT, this.__instance.id, this.__beh_export );
		
  		birtGetUpdatedObjectsResponseHandler.addAssociation( "Docum", this );
  		
		// TODO: rename it to birt event
		this.__cb_installEventHandlers( id );
	},

	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, document object object id (optional since there is only one document instance)
	 *	@return, void
	 */
	__cb_installEventHandlers : function( id, children, bookmark )
	{
		this.superClass.__cb_installEventHandlers( id, children );
		
		// jump to bookmark.
		if ( bookmark )
		{
			var obj = $( bookmark );
			if ( obj && obj.scrollIntoView )
			{
				obj.scrollIntoView( true );
			}
		}
	},
	
	/**
	 *	Unregister any birt event handlers.
	 *
	 *	@id, object id
	 *	@return, void
	 */
	__cb_disposeEventHandlers : function( id )
	{
		this.superClass.__cb_disposeEventHandlers( id );
	},

	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_click : function( event )
	{
		debug( "BirtReportDocument.__neh_click" );

		birtEventDispatcher.broadcastEvent( birtEvent.__E_BLUR );
		birtEventDispatcher.setFocusId( this.__instance.id );
		
//		Event.stop( event );
	},
	
	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_selectElement : function( event )
	{
		// TODO:
	},
	
	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_resize : function( event )
	{
		var width = BirtPosition.viewportWidth( ) -  ( this.__instance.offsetLeft >= 250 ? 250 : 0 ) - 3;
		this.__instance.style.width = width + "px";
		var height = BirtPosition.viewportHeight( ) - this.__instance.offsetTop - 2;
		this.__instance.style.height = height + "px";
	},
	
	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_testAction : function( event )
	{
		debug( "testAction " );
	},
	
	/**
	 *	Birt event handler for "getpage" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_parameter : function( id )
	{
		birtParameterDialog.__cb_bind( );
	},

	/**
	 *	Birt event handler for "change parameter" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_changeParameter : function( id )
	{
        birtParameterDialog.__parameter.length = birtParameterDialog.__parameter.length - 1;
        birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
        							  "ChangeParameter", null, birtParameterDialog.__parameter,
									  { name : "svg", value : this.__has_svg_support? "true" : "false" } );
		birtSoapRequest.setURL( document.location );
		birtEventDispatcher.setFocusId( null );	// Clear out current focusid.
		return true;
	},
	
	/**
	 *	Handle change cascade parameter.
	 */
	__beh_cascadingParameter : function( id, object )
	{
	    birtSoapRequest.addOperation( Constants.documentId, Constants.Document, "GetCascadingParameter", null, object );
		birtSoapRequest.setURL( document.location );
		birtEventDispatcher.setFocusId( null );	// Clear out current focusid.
		return true;
	},

	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__beh_toc : function( id )
	{
		var width = BirtPosition.viewportWidth( ) -  ( this.__instance.offsetLeft < 250 ? 250 : 0 ) - 3;
		this.__instance.style.width = width + "px";
		return true;
	},

	/**
	 *	Birt event handler for "getpage" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_getPage : function( id, object )
	{
		birtSoapRequest.setURL( document.location );
		if ( object )
		{
			birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
										  "GetPage", null,
										  object,
										  { name : "svg", value : this.__has_svg_support? "true" : "false" } );
		}
		else
		{
			birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
										  "GetPage", null,
										  { name : "svg", value : this.__has_svg_support? "true" : "false" } );
		}

		birtEventDispatcher.setFocusId( null );	// Clear out current focusid.
		return true;
	},
	
	/**
	 *	Birt event handler for "print" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_print : function( id )
	{
		//birtPrintDialog.__cb_bind( );
	},
	
	/**
	 *	Birt event handler for "print" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_export : function( id )
	{
		birtSoapRequest.setURL( document.location);
		birtSoapRequest.addOperation( "Document", Constants.Document, "QueryExport", null );
		return true;
	}
});