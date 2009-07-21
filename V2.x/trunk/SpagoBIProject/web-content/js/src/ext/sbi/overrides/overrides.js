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
	
	Ext.override(Ext.menu.Menu, {
	  render : function(){
	    if(this.el){
	      return;
	    }
	    var el = this.el = this.createEl();
	    
	    if(!this.keyNav){
	      this.keyNav = new Ext.menu.MenuNav(this);
	    }
	    if(this.plain){
	      el.addClass("x-menu-plain");
	    }
	    if(this.cls){
	      el.addClass(this.cls);
	    }
	    // generic focus element
	    this.focusEl = el.createChild({
	      tag: "a", cls: "x-menu-focus", href: "#", onclick: "return false;", tabIndex:"-1"
	    });
	    var ul = el.createChild({tag: "ul", cls: "x-menu-list"});
	    ul.on("click", this.onClick, this);
	    ul.on("mouseover", this.onMouseOver, this);
	    ul.on("mouseout", this.onMouseOut, this);
	    if (!this.topmenu) {
	      this.addEvents("mouseenter", "mouseexit");
	      this.mouseout = null;
	    }
	    el.on("mouseover", function(e, t){
	      if(this.topmenu){
	        clearTimeout(this.topmenu.mouseout);
	        this.topmenu.mouseout=null;
	      }else if (this.mouseout == null) this.fireEvent("mouseenter", this, e, t);
	      else {
	        clearTimeout(this.mouseout);
	        this.mouseout = null;
	      }
	    }, this);
	    el.on("mouseout", function(e, t){
	      if (this.topmenu) {
	        this.topmenu.mouseout = (function(){
	          this.topmenu.mouseout = null;
	          this.topmenu.fireEvent("mouseexit", this.topmenu, e, t);
	        }).defer(100, this);
	      } else {
	        this.mouseout = (function(){
	          this.mouseout = null;
	          this.fireEvent("mouseexit", this, e, t);
	        }).defer(100, this);
	      }
	    }, this);
	    el.on("mouseup", function(e, t){
	      e.stopEvent();
	    });
	    this.items.each(function(item){
	      var li = document.createElement("li");
	      li.className = "x-menu-list-item";
	      ul.dom.appendChild(li);
	      if(item.menu)item.menu.topmenu=this.topmenu||this;
	      item.render(li, this);
	    }, this);
	    this.ul = ul;
	    this.autoWidth();
	  }
	});

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