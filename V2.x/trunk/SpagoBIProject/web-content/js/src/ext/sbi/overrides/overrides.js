	/**
	 * Override Ext.FormPanel so that in case we create a form without items it still has a item list.
	 * ERROR IS : this.items has no properties
	 */

	Ext.override(Ext.FormPanel, {
		// private
		initFields : function(){
			//BEGIN FIX It can happend that there is a form created without items (json)
			this.initItems();
			//END FIX
			var f = this.form;
			var formPanel = this;
			var fn = function(c){
				if(c.doLayout && c != formPanel){
					Ext.applyIf(c, {
						labelAlign: c.ownerCt.labelAlign,
						labelWidth: c.ownerCt.labelWidth,
						itemCls: c.ownerCt.itemCls
					});
					if(c.items){
						c.items.each(fn);
					}
				}else if(c.isFormField){
					f.add(c);
				}
			};
			this.items.each(fn);
		}
	});