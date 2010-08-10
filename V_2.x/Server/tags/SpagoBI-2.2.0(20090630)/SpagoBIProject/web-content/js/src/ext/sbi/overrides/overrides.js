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
	
	Ext.override(Ext.Component, {hideMode: 'offsets'});
	

	Ext.override(Ext.form.ComboBox, {
    	
	    onTypeAhead : function(){
	        if(this.store.getCount() > 0){
	            var r = this.store.getAt(0);
	            var newValue = r.data[this.displayField];
	            var len = newValue.length;
	            var selStart = this.getRawValue().length;
	            if(selStart != len){
	                this.setRawValue(newValue);
	                this.selectText(selStart, newValue.length);
	            }
	            if (this.valueField) {
	            	this.setValue(r.data[this.valueField]);
	            }
	        }
	    },
		
        doQuery : function(q, forceAll){
            if(q === undefined || q === null){
                q = '';
            }
            var qe = {
                query: q,
                forceAll: forceAll,
                combo: this,
                cancel:false
            };
            if(this.fireEvent('beforequery', qe)===false || qe.cancel){
                return false;
            }
            q = qe.query;
            forceAll = qe.forceAll;
            if(forceAll === true || (q.length >= this.minChars)){
                if(this.lastQuery !== q){
                    this.lastQuery = q;
                    if(this.mode == 'local'){
                        this.selectedIndex = -1;
                        if(forceAll){
                            this.store.clearFilter();
                        }else{
                            this.store.filter(this.displayField, q);
                        }
                        this.onLoad();
                    }else{
                        this.store.baseParams[this.queryParam] = q;
                        this.store.load({
                            params: this.getParams(q)
                        });
                        this.expand();
                    }
                }else{
                    this.selectedIndex = -1;
                    this.onLoad();
                }
            }
            // if the store is empty, queries on valueField instead of displayField
            if (this.store.getCount() == 0 && this.mode == 'local' && q != '') {
                var qe = {
                    query: q,
                    forceAll: true,
                    combo: this,
                    cancel:false
                };
                if(this.fireEvent('beforequery', qe)===false || qe.cancel){
                    return false;
                }
                q = qe.query;
                this.selectedIndex = -1;
                if(forceAll){
                    this.store.clearFilter();
                }else{
                    this.store.filter(this.valueField, q);
                }
                this.onLoad();
            }
        },
        
        onLoad : function(){
            if(!this.hasFocus){
                return;
            }
            if(this.store.getCount() > 0){
                this.expand();
                this.restrictHeight();
                if(this.lastQuery == this.allQuery){
                    if(this.editable){
                        this.el.dom.select();
                    }
                    if(!this.selectByValue(this.value, true)){
                        this.select(0, true);
                    }
                }else{
                    this.selectNext();
                    if(this.typeAhead && this.lastKey != Ext.EventObject.BACKSPACE && this.lastKey != Ext.EventObject.DELETE){
                        this.taTask.delay(this.typeAheadDelay);
                    }
                }
            }else{
                this.onEmptyResults();
                // if the store is empty, the field value is the rawValue (the displayed value)
                if (this.getRawValue() !== undefined) {
            		this.setValue(this.getRawValue());
            	}
            }
            //this.el.focus();
        }
	});