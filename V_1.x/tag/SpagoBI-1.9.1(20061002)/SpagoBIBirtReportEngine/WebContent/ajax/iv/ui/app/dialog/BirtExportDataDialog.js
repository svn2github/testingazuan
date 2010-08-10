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
 *	Birt export report dialog.
 */
BirtExportDataDialog = Class.create( );

BirtExportDataDialog.prototype = Object.extend( new BirtTabedDialogBase( ),
{
	__neh_click_exchange_closure : null,
	__neh_click_addremove_closure : null,

	uniqueCriteriaID : 0,
	
	communicated : 0,
	
	totalColumns : [],
	availableColumns : [],
	selectedColumns : [],

	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	__initialize : function( id )
	{
		this.__neh_click_exchange_closure = this.__neh_click_exchange.bindAsEventListener( this );
		this.__neh_click_addremove_closure = this.__neh_click_addremove.bindAsEventListener( this );
		this.__cb_installEventHandlers( id );
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, toolbar id (optional since there is only one toolbar)
	 *	@return, void
	 */
	__installEventHandlers : function( id )
	{
		// Initialise exchange buttons
		var oInputs = this.__instance.getElementsByTagName( 'input' );
		for ( var i = 0; i < oInputs.length - 2; i++ )
		{
			Event.observe( oInputs[i], 'click', this.__neh_click_exchange_closure, false );
		}
		
		// Initialise add and remove imges
		var oImgs = this.__instance.getElementsByTagName( "img" );
		if ( oImgs )
		{
			for ( var i = 0; i < oImgs.length; i++ )
			{
				Event.observe( oImgs[i], 'click', this.__neh_click_addremove_closure, false );
			}
		}
	},
	
	/**
	 *	Native event handler for selection item movement.
	 */
	__neh_click_exchange : function( event )
	{
		var oSelects = this.__instance.getElementsByTagName( 'select' );

		switch ( Event.element( event ).value )
		{
			case '>>':
			{
				this.moveAllItems( oSelects[0], oSelects[1] );
				break;
			}
			case '>':
			{
				this.moveSingleItem( oSelects[0], oSelects[1] );
				break;
			}
			case '<':
			{
				this.moveSingleItem( oSelects[1], oSelects[0] );
				break;
			}
			case '<<':
			{
				this.moveAllItems( oSelects[1], oSelects[0] );
				break;
			}
		}
	},
	
	/**
	 *	Move single selection item.
	 */
	moveSingleItem : function( sel_source, sel_dest )
	{
		if ( sel_source.selectedIndex == -1 )
		{
			return;
		}
     	var SelectedText = sel_source.options[sel_source.selectedIndex].text;
   		sel_dest.options.add( new Option( SelectedText ) );
   		sel_source.options[sel_source.selectedIndex] = null;
	},
	
	/**
	 *	Move all selection items.
	 */
	moveAllItems : function( sel_source, sel_dest )
	{
   		for ( var i = 0; i < sel_source.length; i++ )
   		{
     		var SelectedText = sel_source.options[i].text;
     		sel_dest.options.add( new Option( SelectedText ) );
   		}
   		
   		sel_source.length = 0;
	},	

	/**
	 *	Native event handler for filter adding and removing.
	 */
	__neh_click_addremove : function( event )
	{
		var oBtn = Event.element( event );
		
		if ( oBtn )
		{
			switch ( oBtn.name )
			{
				case 'add':
				{
					this.addOneCriteriaTableRow( );
					break;
				}
				case 'remove':
				{
					var tablerowID = oBtn.id.replace( "removeIMG", "criteriaTableRow" );
					this.removeOneCriteriaTableRow( tablerowID );
					break;
				}
			}
		}
	},
	
	/**
	 *	Adding filter criteria row.
	 */
	addOneCriteriaTableRow: function( )
	{		
        var tbody = $( "ExportCriteriaTBODY" );
        var newTableRow = document.createElement( "tr" );
		newTableRow.id = "criteriaTableRow" + this.uniqueCriteriaID;
 
        var columnNameTD = document.createElement( "td" );
        var columnNameSelect = document.createElement( "select" );
        columnNameSelect.id = "columnNameSelect" + this.uniqueCriteriaID;
        columnNameSelect.style.width = "180px";
        columnNameSelect.style.fontSize = "8pt";
        
		//the select options should be the same as the datas received in bind( ) method.
		for( var i = 0; i < this.totalColumns.length; i++ )
		{
			columnNameSelect.options.add( new Option( this.totalColumns[i] ) );
		}
		
        columnNameTD.appendChild( columnNameSelect );
        newTableRow.appendChild( columnNameTD );
        
        var operatorTD = document.createElement( "td" );
        var operatorSelect = document.createElement( "select" );
        operatorSelect.id = "operatorSelect" + this.uniqueCriteriaID;
        operatorSelect.style.width = "40px";
        operatorSelect.style.fontSize = "8pt";
        operatorSelect.options.add( new Option( "=" ) );
		operatorSelect.options.add( new Option( ">" ) );
		operatorSelect.options.add( new Option( "<" ) );        
        operatorTD.appendChild( operatorSelect );
        newTableRow.appendChild( operatorTD );
        
        var valueTD = document.createElement( "td" );
        var valueINPUT = document.createElement( "input" );
        valueINPUT.id = "valueINPUT" + this.uniqueCriteriaID;
        valueINPUT.type = "text";
        valueINPUT.style.width = "150px";
        valueINPUT.style.fontSize = "8pt";
        valueTD.appendChild( valueINPUT );
        newTableRow.appendChild( valueTD );
        
        var imgTD = document.createElement( "td" );
        var removeImg = document.createElement( "img" );
        removeImg.id = "removeIMG" + this.uniqueCriteriaID;
        removeImg.name = "remove";
        removeImg.src = "images/iv/DeleteFilter.gif";
        Event.observe( removeImg, 'click', this.__neh_click_addremove_closure, false );
        imgTD.appendChild( removeImg );
        newTableRow.appendChild( imgTD );
        
        tbody.appendChild( newTableRow );
        this.uniqueCriteriaID += 1;
	},

	/**
	 *	Remove filter row from table.
	 *
	 *	@tablerowID, table row id
	 *	@return, void
	 */
	removeOneCriteriaTableRow : function( tablerowID )
	{
    	var table = document.getElementById( "ExportCriteriaTBODY" );
    	var rowToDelete = $( tablerowID );
    	table.removeChild( rowToDelete );
	},

	/**
	 *	Binding data to the dialog UI. Data includes zoom scaling factor.
	 *
	 *	@data, data DOM tree (schema TBD)
	 *	@return, void
	 */
	 __bind : function( data )
	 {
	 	if ( !data )
	 	{
	 		return;
	 	}
	 	
	 	var columns = data.getElementsByTagName( 'Column' );
	 	var columnNames = data.getElementsByTagName( 'Name' );
	 	var index = 0;
	 	for( var i = 0; i < columns.length; i++ )
	 	{
	 		var column = columns[i];
	 		var columnName = column.getElementsByTagName( 'Name' );
	 		for( var j = 0; j < columnName.length; j++ )
	 		{
	 			var name = columnName[j];
				this.availableColumns[index] = name.firstChild.data;
				this.totalColumns[index] = this.availableColumns[index];
				index += 1;
	 		}
	 	}
	 	
	 	var oSelects = this.__instance.getElementsByTagName( 'select' );
		oSelects[0].options.length = 0;
		oSelects[1].options.length = 0;	
		for ( var i = 0; i < this.availableColumns.length; i++ )
		{	
			oSelects[0].options.add( new Option( this.availableColumns[i] ) );
		}

		if ( this.communicated == 0 )
		{
			this.addOneCriteriaTableRow( );
			this.communicated = 1;
		}
	 },
	 
	/**
	 *	Handle clicking on ok.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__okPress : function( )
	{
		var oSelects = this.__instance.getElementsByTagName( 'select' );
		for( var i = 0; i < oSelects[1].options.length; i++ )
		{
			this.selectedColumns[i] = oSelects[1].options[i].text;
		}
		
		this.__constructForm( );
		this.__l_hide( );
	},
	
	/**
	 *	Construct extract data form. Post it to server.
	 */
	__constructForm : function( )
	{
		var dialogContent = $( 'ExportCriteriaTBODY' );
		var hiddenDiv = document.createElement( 'div' );
		hiddenDiv.style.display = 'none';

		var hiddenForm = document.createElement( 'form' );
		hiddenForm.method = 'post';
		hiddenForm.target = '_blank';
		var url = document.location.href;
		var index = url.indexOf( "frameset" );
		url = url.substring( 0, index ) + "download" + url.substring( index + 8, url.length - 1 );
		hiddenForm.action = url;
		
		// Pass over current element's iid.
		var focusedElement = $( birtEventDispatcher.getFocusId( ) );
		var focusedElementInput = document.createElement( 'input' );
		focusedElementInput.type = 'hidden';
		focusedElementInput.name = "iid";
		focusedElementInput.value = focusedElement.iid;
		hiddenForm.appendChild( focusedElementInput );

		// data of selected columns.
		for( var i = 0; i < this.selectedColumns.length; i++ )
		{
			var hiddenSelectedColumns = document.createElement( 'input' );
			hiddenSelectedColumns.type = 'hidden';
			hiddenSelectedColumns.name = "SelectedColumns";
			hiddenSelectedColumns.value = this.selectedColumns[i];
			hiddenForm.appendChild( hiddenSelectedColumns );
		}
		
		// data of filtering.
		var ops = new Array( );
		ops[0] = '=';
		ops[1] = '>';
		ops[2] = '<';	
		
		for( var i = 0; i < this.uniqueCriteriaID; i++ )
		{
			var selectId = 'columnNameSelect' + i;
			var columnOption = $( selectId );
			if ( !columnOption )
			{
				continue;
			}
			var leftOp = this.totalColumns[columnOption.selectedIndex];
			var opId = 'operatorSelect' + i;
			var opEle = $( opId );
			var op = ops[opEle.selectedIndex];
			var inputId = 'valueINPUT' + i;
			var inputEle = $( inputId );
			var rightOp = inputEle.value;
			
			var operation = leftOp + op + rightOp;
			var hiddenOperation = document.createElement( 'input' );
			hiddenOperation.type = 'hidden';
			hiddenOperation.name = 'Operations';
			hiddenOperation.value = operation;
			hiddenForm.appendChild( hiddenOperation );
		}
		
		var tmpSubmit = document.createElement( 'input' );
		tmpSubmit.type = 'submit';
		tmpSubmit.value = 'TmpSubmit';
		hiddenForm.appendChild( tmpSubmit );
		
		hiddenDiv.appendChild( hiddenForm );
		dialogContent.appendChild( hiddenDiv );
		tmpSubmit.click( );
		dialogContent.removeChild( hiddenDiv );
	}
} );