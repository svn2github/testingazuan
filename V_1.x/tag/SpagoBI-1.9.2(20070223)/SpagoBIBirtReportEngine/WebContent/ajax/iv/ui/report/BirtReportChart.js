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
 *	BirtReportChart
 *	...
 */
BirtReportChart = Class.create( );

/** 
 *	Note: Object.extend will allow this class to have access to the
 *	prototype methods and vars of BirtReportBase. There will only be one instance of these.
 *	Any methods of the same name (signature?) that are declared in this class will be used instead
 *	of the BirtReportBase method (except for initialize).
 *	In order to use the superclass methods, as well as override them, it is necessary to instantiate an
 *	object of type BirtReportBase. Note that this "superclass" object is effectively a singleton
 *	(because it has defined all its methods and variables inside its prototype), and these
 *	will be shared among all its children (with the exception of "primitive type" variables and 
 *	those declared in the initialize method)
 */
 
BirtReportChart.prototype = Object.extend( new BirtReportBase( "BirtReportChart" ),
{
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *
	 *	@return, void
	 */
	initialize: function( )
	{
		this.superClass = new BirtReportBase( "BirtReportChart.initialize" );
		this.componentContextItems.test = { displayName: "Chart Test Action", action: this.__neh_testAction, target: this };
		this.__neh_select_closure = this.__neh_selectElement.bindAsEventListener( this );
		this.__neh_contextMenu_closure = this.__neh_contextMenu.bindAsEventListener( this );		
  		birtGetUpdatedObjectsResponseHandler.addAssociation( "Chart", this );
	},

	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, chart object id
	 *	@return, void
	 */
	__cb_installEventHandlers: function( id, children )
	{
		var container = $( id );
		
		//Event.observe( container, 'contextmenu', this.__neh_contextMenu_closure, false );
		Event.observe( container, 'click', this.__neh_select_closure, false );
		
		var chartDiv = container.getElementsByTagName( "div" )[0];
		if( chartDiv )
		{			
			this.activateChart( chartDiv, true );
		}				
		
		this.createOverlay( container );	
	
		birtEventDispatcher.registerEventHandler( birtEvent.__E_BLUR, id, this.__beh_blur );
		
		this.superClass.__cb_installEventHandlers( id, children );		
	},
	
	/**
	 *	Unregister any birt event handlers.
	 *	Remove local event listeners
	 *	
	 *	@id, object id
	 *	@return, void
	 */
	__cb_disposeEventHandlers: function( id )
	{
		var container = $( id );
		
		try
		{
			Event.stopObserving( container, 'contextmenu', this.__neh_contextMenu_closure, false );
			Event.stopObserving( container, 'click', this.__neh_select_closure, false );
		}
		catch( error )
		{
			if( error )
			{
				debug( "ERROR BirtReportTable.__cb_disposeEventHandlers : " + error );
				throw error;
			}
      	}
      	
		birtEventDispatcher.unregisterEventHandler( birtEvent.__E_BLUR, id );
		
		this.superClass.__cb_disposeEventHandlers( id );
	},
	
	/**
	 *	Test function for context menu
	 */
	__neh_testAction: function( event )
	{
		debug( "Chart testAction " );
	},

	/**
	 *	Birt event hander for "blur" event.
	 *
	 *	@id, table id
	 *	@return, void
	 */
	__beh_blur: function( id )
	{	
		var container = $( id );
		birtReportChart.deselectChart( container );			
		return false;
	},
	
	/**
	 *	Birt event hander for "blur" event.
	 *
	 *	@chartDiv, table id
	 *	@return, void
	 */
	selectElement: function( chartDiv )
	{
		for( var i = 0; i < chartDiv.columnOverlay.length; i++ )
		{
			chartDiv.columnOverlay[i].style.display = "block";	
		}
	},
	
	/**
	 *	Birt event hander for "blur" event.
	 *
	 *	@chartDiv, table id
	 *	@return, void
	 */
	deselectChart: function( chartDiv )
	{
		for( var i = 0; i < chartDiv.columnOverlay.length; i++ )
		{
			chartDiv.columnOverlay[i].style.display = "none";
		}
	},
	
	/**
	 *	Birt event hander for "blur" event.
	 *
	 *	@return, void
	 */
	activateChart: function( )
	{
		debug( "activateChart " );
	}
});