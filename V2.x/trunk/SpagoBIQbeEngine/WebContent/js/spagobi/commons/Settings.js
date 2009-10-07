Ext.ns("Sbi.settings");

Sbi.settings.qbe = {
		queryBuilderPanel: {
			enableTreeToolbar: true,
			enableTreeTbSaveBtn: true,
			enableTreeTbPinBtn: true,
			enableTreeTbUnpinBtn: true,
			enableQueryTbExecute: true,
			enableQueryTbSave: true,
			enableQueryTbSaveView: false,
			enableQueryTbValidate: false
		}

		, dataMartStructurePanel: {
			enableTreeContextMenu: true
		}
		
		, selectGridPanel: {
			enableTbAddCalculatedBtn: true
			, enableTbHideNonvisibleBtn: true
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
}; 