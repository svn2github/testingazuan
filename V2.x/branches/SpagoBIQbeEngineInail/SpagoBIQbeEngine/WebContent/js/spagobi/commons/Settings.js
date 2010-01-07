Ext.ns("Sbi.settings");

Sbi.settings.qbe = {
		queryBuilderPanel: {
			enableTreeToolbar: true,
			enableTreeTbPinBtn: true,
			enableTreeTbUnpinBtn: true,
			
			enableQueryTbExecuteBtn: true,
			enableQueryTbSaveBtn: true,
			enableQueryTbValidateBtn: false,
			
			enableCatalogueTbDeleteBtn: true,
			enableCatalogueTbAddBtn: false,
			enableCatalogueTbInsertBtn: true
		}

		, selectGridPanel: {
			enableTbHideNonvisibleBtn: true
			, enableTbDeleteAllBtn: true
			, columns : {
				'entity': {hideable: true, hidden: false, sortable: false}
				, 'field': {hideable: true, hidden: false, sortable: false}
				, 'alias': {hideable: true, hidden: false, sortable: false}	
				, 'funct': {hideable: true, hidden: false, width: 50, sortable: false}
				, 'group': {hideable: true, hidden: false, width: 50, sortable: false}
				, 'order': {hideable: true, hidden: false, width: 50, sortable: false}
				, 'visible': {hideable: true, hidden: false, width: 50, sortable: false}
				, 'include': {hideable: true, hidden: false, width: 50, sortable: false}
				, 'filter': {hideable: true, hidden: false, width: 50, sortable: false}
				, 'having': {hideable: true, hidden: false, width: 50, sortable: false}			
			}
		}
		
		, filterGridPanel: {
			enableTbExpWizardBtn: true
			, enableTbRemoveAllFilterBtn: true
			, enableTbAddFilterBtn: true
			, columns : {
				'filterId': {hideable: true, hidden: false, sortable: false}
				, 'filterDescripion': {hideable: true, hidden: true, sortable: false}
				, 'leftOperandDescription': {hideable: false, hidden: false, sortable: false}
				, 'leftOperandType': {hideable: true, hidden: true, sortable: false}
				, 'operator': {hideable: false, hidden: false, sortable: false}
				, 'rightOperandDescription': {hideable: false, hidden: false, sortable: false}				
				, 'rightOperandType': {hideable: true, hidden: true, sortable: false}
				, 'booleanConnector': {hideable: true, hidden: false, sortable: false}
				, 'deleteButton': {hideable: true, hidden: true, sortable: false}
				, 'promptable': {hideable: true, hidden: false, sortable: false}				
			}
		}
		
		, dataMartStructurePanel: {
			enableTreeContextMenu: true
		}
};

Sbi.settings.formviewer = {
		staticClosedXORFiltersPanel: {
			width: 300
			, height: 150
		}
		, staticClosedOnOffFiltersPanel: {
			width: 300
			, height: 150
		}
};