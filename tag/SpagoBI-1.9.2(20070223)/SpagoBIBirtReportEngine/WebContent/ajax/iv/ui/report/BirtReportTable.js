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
 *	BirtReportTable
 *	...
 */
BirtReportTable = Class.create( );

BirtReportTable.prototype = Object.extend( new BirtReportBase( "BirtReportTable" ),
{
	/**
	 *	Closures
	 */
	__neh_selectColumn_closure: null,
	__neh_sort_closure: null,
	
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *
	 *	@return, void
	 */
	initialize : function( )
	{
		this.superClass = new BirtReportBase( "BirtReportTable.initialize" );
			
//		this.__neh_contextMenu_closure = this.__neh_contextMenu.bindAsEventListener( this );
//		this.__neh_select_closure = this.__neh_selectElement.bindAsEventListener( this );
//		this.__neh_selectColumn_closure = this.__neh_selectColumn.bindAsEventListener( this );
//		this.__neh_sort_closure = this.__neh_sort.bindAsEventListener( this );
		
		this.componentContextItems.test = { displayName: "Table Test Action", action: this.__neh_testAction, target: this };
		
  		birtGetUpdatedObjectsResponseHandler.addAssociation( "Table", this );
	},

	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, table object id
	 *	@return, void
	 */
	__cb_installEventHandlers : function( id, children )
	{
		var container = $( id );
		
//		Event.observe( container, 'contextmenu', this.__neh_contextMenu_closure, false );
//		Event.observe( container, 'click', this.__neh_select_closure, false );
//		var table = container.getElementsByTagName( "table" )[0];
//		if( table )
//		{
//			this.activateTable( table, true );
//			this.createColumnOverlays( container, table );
//		}
		
//		this.createOverlay( container );	
	
//		birtEventDispatcher.registerEventHandler( birtEvent.__E_COLLAPSEEXPAND, id, this.__beh_collpaseExpand );
//		birtEventDispatcher.registerEventHandler( birtEvent.__E_SORT, id, this.__beh_sort );
//		birtEventDispatcher.registerEventHandler( birtEvent.__E_ACTIONDELETECOL, id, this.__beh_deleteSelectedColumn );
//		birtEventDispatcher.registerEventHandler( birtEvent.__E_ACTIONADDCOL, id, this.__beh_addColumn );
//		birtEventDispatcher.registerEventHandler( birtEvent.__E_QUERY_FONT, id, this.__beh_queryFont );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_BLUR, id, this.__beh_blur );
//		birtEventDispatcher.registerEventHandler( birtEvent.__E_QUERY_EXPORT, id, this.__beh_queryExport );

		this.superClass.__cb_installEventHandlers( id, children );
	},
	
	/**
	 *	Unregister any birt event handlers.
	 *	Remove local event listeners
	 *
	 *	@id, object id
	 *	@return, void
	 */
	__cb_disposeEventHandlers : function( id )
	{
		var container = $( id );
		
		try
		{
//			Event.stopObserving( container, 'contextmenu', this.__neh_contextMenu_closure, false );
//			Event.stopObserving( container, 'click', this.__neh_select_closure, false );
		}
		catch( error )
		{
			if( error )
			{
				debug( "ERROR BirtReportTable.__cb_disposeEventHandlers : " + error );
				throw error;
			}
      	}
      	
      	//TODO remove local event listeners on table, table headers and column overlays
		
//		birtEventDispatcher.unregisterEventHandler( birtEvent.__E_COLLAPSEEXPAND, id);
//		birtEventDispatcher.unregisterEventHandler( birtEvent.__E_SORT, id );
//		birtEventDispatcher.unregisterEventHandler( birtEvent.__E_ACTIONDELETECOL, id );
//		birtEventDispatcher.unregisterEventHandler( birtEvent.__E_ACTIONADDCOL, id );
//		birtEventDispatcher.unregisterEventHandler( birtEvent.__E_QUERY_FONT, id );
		birtEventDispatcher.unregisterEventHandler( birtEvent.__E_BLUR, id );
		birtEventDispatcher.unregisterEventHandler( birtEvent.__E_QUERY_EXPORT, id );
		
		this.superClass.__cb_disposeEventHandlers( id );
	},
	
	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_collapse : function( event )
	{
		// If operation can be handled in local, do it in local.
		birtEventDispatcher.fireEvent( birtEvent.__E_COLLAPSEEXPAND );
	},
		
	
	/**
	 *	Handles selection of table column
	 *	@param event
	 */
	__neh_selectColumn : function( event )
	{
		debug("BirtReportTable.__neh_selectColumn");
		debug("ShiftKey " + event.shiftKey);
		debug("ControlKey " + event.ctrlKey);
		
		Event.stop( event );
		var element = Event.element( event );
		
		var colNum = this.getSelectionIndex( element );
		
		var tableContainer = element;
		
		while( !tableContainer[Constants.reportBase] ) //TODO error check
		{
			tableContainer = tableContainer.parentNode;
		}
			
			//if there is already a column selection, no need to blur
		if(!this.anyColAlreadySelected(tableContainer.selectedColumns))
		{
			birtEventDispatcher.fireEvent( birtEvent.__E_BLUR );
			birtEventDispatcher.setFocusElement( tableContainer );
			this.addToColSelection( tableContainer.selectedColumns, colNum, tableContainer.columnOverlays );					
		}
		else
		{
			if(event.shiftKey)
			{
				//NOT CURRENTLY IMPLEMENTED
			}
			else if(event.ctrlKey)
			{
					//ctrlKey toggles if already selected
				if(this.isColAlreadySelected( tableContainer.selectedColumns, colNum ))
				{
					this.deselectColumn( tableContainer.selectedColumns, colNum, tableContainer.columnOverlays);
				}
				else
				{
					this.addToColSelection( tableContainer.selectedColumns, colNum, tableContainer.columnOverlays );
				}
			}
			else
			{
				this.deselectAllColumns( tableContainer.selectedColumns, tableContainer.columnOverlays );
				this.addToColSelection( tableContainer.selectedColumns, colNum, tableContainer.columnOverlays );	
			}
		}		
	},

	/**
	 *	Triggered by click to sort.
	 */
	__neh_sort : function( event )
	{	
		Event.stop( event );
		var targ = Event.element( event );
		
		while( targ.className.toLowerCase() != "sortlink" ) //TODO this will come from emitter
		{	
			if( !targ )
			{
				//TODO throw exception
				return;
			}
			
			targ = targ.parentNode;
		}
		
		var colName;
		colName = targ.firstChild.nodeValue;
		var dataColName = colName.replace( /\s+/g, '' ); //TODO need real column name or index
		birtEventDispatcher.fireEvent( birtEvent.__E_BLUR );
		birtEventDispatcher.fireEvent( birtEvent.__E_SORT, dataColName );
	},
	
	/**
	 *	Test function for context menu
	 */
	__neh_testAction : function( event )
	{
		debug("Table testAction ");
	},
	
	/**
	 *	Birt event hander for "blur" event.
	 *
	 *	@id, table id
	 *	@return, void
	 */
	__beh_blur : function( id )
	{	
		var container = $( id );
		
//		birtReportTable.deselectAllColumns( container.selectedColumns, container.columnOverlays );
		birtReportTable.visuallyDeselectTable( container );
				
		return false;
	},
	
	/**
	 *	Birt event hander for "sort column" event.
	 *
	 *	@id, table id
	 *	@return, void
	 */
	__beh_sort : function ( id, colName )
	{
		birtSoapRequest.setMessage( '<?xml version="1.0" encoding="UTF-8" standalone="no" ?><UpdateRequest><Operation><Target>'
			+ id 
			+ '</Target><Operator>Sort</Operator><Parameter><ParameterName>ColumnName</ParameterName><ParameterValue>'
			+ colName 
			+ '</ParameterValue> </Parameter></Operation></UpdateRequest>' );
		return true;
	},
	

	/**
	 *	Birt event hander for "collpaseexpand" event.
	 *
	 *	@id, table id
	 *	@return, void
	 */
	__beh_collpaseExpand : function( id )
	{
		//TODO
		return true;
	},

	__beh_deleteSelectedColumn : function( id )
	{
		//TODO
		return true;
	},
	
	__beh_addColumn : function( id, colName )
	{	
		birtSoapRequest.setMessage('<?xml version="1.0" encoding="UTF-8" standalone="no" ?><UpdateRequest><Operation><Target>'
				+ id 
				+ '</Target><Operator>Add</Operator><Parameter><ParameterName>ColumnName</ParameterName><ParameterValue>'
				+ colName 
				+ '</ParameterValue> </Parameter></Operation></UpdateRequest>');
		return true;
	},

	__beh_queryFont : function( id )
	{
		birtSoapRequest.setURL( document.location );
		
		var t = "<?xml version='1.0' encoding='UTF-8'?>"
		t += "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' ";
		t += "xmlns:xsd='http://www.w3.org/2001/XMLSchema' ";
		t += "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>";
		t += "<soapenv:Body>";
		t += "<GetUpdatedObjects xmlns='http://schemas.eclipse.org/birt'>";
		t += "<Operation>";
		t += "<Target>Table</Target>";
		t += "<Operator>QueryFont</Operator>";
		
		t += "<Oprand xsi:nil='true'/>";
		
		/*
		t += "<Oprand>";
		t += "<Name>pageno</Name>";
		t += "<Value>" + object['Page'] + "</Value>";
		t += "</Oprand>";
		*/
		
		t += "<Data xsi:nil='true'/>";
		t += "</Operation>";
		t += "</GetUpdatedObjects>";
		t += "</soapenv:Body>";
		t += "</soapenv:Envelope> \n";
		
		birtSoapRequest.setMessage( t );
		return true;
	},
	
	/**
	 *	Birt event handler that handle the query export event.
	 */
	__beh_queryExport : function( id )
	{
		birtSoapRequest.setURL( document.location );
		var oTable = $( id );
		var oIid = oTable.getAttributeNode( "iid" );
		
		if ( oIid )
		{
			birtSoapRequest.addOperation( id, Constants.Table,
										  "QueryExport", null,
										  { name : "iid", value : oIid.value } );
		}
		else
		{
			birtSoapRequest.addOperation( id, Constants.Table, "QueryExport", null );
		}

		return true;
	},
	
	/**
	 * Climbs the DOM tree upwards in search of an element with a property "index".
	 *
	 * @return the index number of the element
	 */
	getSelectionIndex: function( element )
	{
		while( element.index === undefined ) //TODO error check
		{
			element = element.parentNode;
		}
		
		return element.index;
	},
	
	/**
	@return Array of zero-based index numbers of selected columns
	*/
	getColumnSelections: function( container )
	{
		return container.selectedColumns;
	},
	
	/**
	@return Array of column selection visual overlays
	*/
	getColumnOverlays: function( container )
	{
		return container.columnOverlays;
	},
	
	/**
	@return true if any column is already selcted
	*/
	anyColAlreadySelected: function( selectedColumns )
	{
		for(var i = 0; i < selectedColumns.length; i++)
		{
			if( selectedColumns[i] )
			{
				return true;
			}
		}
		return false;
	},
	
	/**
	@return true if column is already selected, false otherwise
	*/
	isColAlreadySelected: function( selectedColumns, colNum )
	{
		return selectedColumns[colNum];
	},
	
	/**
	Add a column to selected columns
	*/
	addToColSelection: function( selectedColumns, colNum, columnOverlays )
	{
		selectedColumns[colNum] = true;
		this.visuallySelectCol(columnOverlays, colNum);
	},
	
	deselectColumn: function( selectedColumns, colNum, columnOverlays)
	{
		selectedColumns[colNum] = false;
		this.visuallyDeselectCol(columnOverlays, colNum);
	},
	
	/**
	Deselect all columns
	*/
	deselectAllColumns: function( selectedColumns, columnOverlays )
	{
		for(var i = 0; i < selectedColumns.length; i++)
		{
			if( selectedColumns[i] )
			{
				this.visuallyDeselectCol(columnOverlays, i);
				selectedColumns[i] = false;
			}
		}			
	},
		
	/**
	 *	Highlights selected column.
	 */
	visuallySelectCol : function( columnOverlays, colNum )
	{
		columnOverlays[colNum].style.display = "block";
	},
	
	/**
	 *	Removes highlights from selected column.
	 */
	visuallyDeselectCol : function( columnOverlays, colNum )
	{
		columnOverlays[colNum].style.display = "none";
	},
	
	/**
	 *	Highlights table element
	 */
	selectElement : function( tableDiv )
	{
//		for( var i = 0; i < tableDiv.columnOverlay.length; i++ )
//		{
//			tableDiv.columnOverlay[i].style.display = "block";	
//		}

		tableDiv.oldColor = tableDiv.style.backgroundColor;
		tableDiv.style.backgroundColor = "#F0F8FF";
	},
	
	/**
	 *	Removes highlights from selected Table
	 */
	visuallyDeselectTable : function( tableDiv )
	{
//		for( var i = 0; i < tableDiv.columnOverlay.length; i++ )
//		{
//			tableDiv.columnOverlay[i].style.display = "none";
//		}

		tableDiv.style.backgroundColor = tableDiv.oldColor;
		tableDiv.oldColor = null;
	},
	
	/**
	 *	Mutator
	 *	@param table dom element to activate
	 *	@param activate boolean indicating activate/deactivate
	 */
	activateTable : function( table, activate )
	{
		var header = table.getElementsByTagName( "thead" )[0];
		if( header )
		{
			this.activateThead( table, header, activate );
		}
		
		var tbody = table.getElementsByTagName( "tbody" )[0];
		if( tbody )
		{
			this.activateTbody( tbody, activate );
		}	
	},
	
	/**
	 *	Mutator
	 *	@param activate boolean indicating if this is to activate or deactivate
	 */
	activateThead : function( table, header, activate )
	{
		var headerCells = header.getElementsByTagName( "td" );
		for( var i = 0; i < headerCells.length; i++ )
		{
			if( activate )
			{	
				headerCells[i].index = i;		
				Event.observe( headerCells[i], 'click', this.__neh_selectColumn_closure , false );
			}
			else
			{
				Event.stopObserving( headerCells[i], 'click', this.__neh_selectColumn_closure , false );
			}
			
			var divs = headerCells[i].getElementsByTagName( "div" );
			if( divs.length > 0 )
			{
				var spans = divs[0].getElementsByTagName( "span" );
				if( spans && spans[0] && spans[0].className == "sortLink" )
				{
					if( activate )
					{	
						Event.observe( spans[0], 'click', this.__neh_sort_closure, false );
					}
					else
					{
						Event.stopObserving( spans[0], 'click', this.__neh_sort_closure, false );
					}
				}						
			}	
		}
	},
	
	/**
	 *	Mutator
	 *	@param activate boolean indicating if this is to activate or deactivate
	 */
	activateTbody : function( tbody, activate )
	{		
		var bodyCells = tbody.getElementsByTagName( "td" );
		for( var i = 0; i < bodyCells.length; i++ )
		{
			//activate body cells here
		}
	},
	
	/**
	 *	Creates column selection visual indicator
	 *  Initializes array of selected columns to false
	 */
	createColumnOverlays : function( container, table )
	{
		container.selectedColumns = [];
		container.columnOverlays = [];
		var firstRow = table.getElementsByTagName( "thead" )[0];
		if( !firstRow )
		{
			firstRow  = table.getElementsByTagName( "tbody" )[0];
		}
		
		var headerCells = firstRow.getElementsByTagName( "td" );
		var cells = table.getElementsByTagName( "td" );
		var lastCell = cells[cells.length - 1];
		var totalHeight = lastCell.clientHeight + lastCell.offsetTop;
		var borderWidth = 5;
		
		for( var i = 0; i < headerCells.length; i++ )
		{
			container.selectedColumns[i] = false;
			
			var width = headerCells[i].clientWidth;
			var height = headerCells[i].clientHeight;
			var left = table.offsetLeft + headerCells[i].offsetLeft;
			var top = table.offsetTop + headerCells[i].offsetTop;
			
			var div = container.ownerDocument.createElement( "div" );
			
			var topDiv = div.ownerDocument.createElement( "div" );
			topDiv.index = i;
			Event.observe( topDiv, 'click', this.__neh_selectColumn_closure , false );
			
			this.styleColumnOverlay( topDiv, height, div , totalHeight, top, left, width);
								
			container.columnOverlays.push( div );
			
			div.appendChild( topDiv );				
			container.appendChild( div );
		}	
	},
	
	/**
	 *	Mutator 
	 *	Add css styles to overlay
	 *	TODO move color styles to style sheet
	 */
	styleColumnOverlay: function( headerDiv, headerHeight, containerDiv, containerHeight, 
		containerTop, containerLeft, width )
	{
		headerDiv.style.backgroundColor = "#4682b4";
		headerDiv.style.width = width;
		headerDiv.style.height = headerHeight;
		
		containerDiv.style.backgroundColor = "#dbe4ee";
		//containerDiv.style.borderColor = "#b0c4de";
		//containerDiv.style.borderStyle = "solid";
		//containerDiv.style.borderWidth = borderWidth;
		containerDiv.style.width = width;
		containerDiv.style.height = containerHeight;
		containerDiv.style.zIndex = 999; //TODO
		containerDiv.style.display= "none";
		containerDiv.style.position = "absolute";
		containerDiv.style.top = containerTop;
		containerDiv.style.left = containerLeft;
		
		var opacity = typeof( opacity ) != "undefined" ? opacity : .5;
		containerDiv.style.opacity = opacity;
		containerDiv.style.filter = 'alpha( opacity=' + ( opacity * 100 ) + ')';		
	}
});