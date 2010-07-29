CrossTabCFWizard = function(level, horizontal) {

	this.initMainPanel();

	var c = {
			title: 'CF',
			layout: 'fit',
			width: 350,
			height: 250,
			items:[this.mainPanel],
		    buttons: [{
				text: 'OK',
			    handler: function(){
		    		var expression= this.getExpression();
		    		var cfName= this.cfNameField.getValue()
		    		if(expression!=null && expression!="" && cfName!=null && cfName!=""){
		    			this.fireEvent('applyCalculatedField', this.activeLevel, this.horizontal, expression, cfName);
		    			this.hide();
		    		}
	        	}
	        	, scope: this
		    }]
		};
	
	
	// constructor
	if(level!=null){
		this.activeLevel = level;
	}
	if(horizontal!=null){
		this.horizontal = horizontal;
	}
	
	CrossTabCFWizard.superclass.constructor.call(this, c);
	
};
	
Ext.extend(CrossTabCFWizard, Ext.Window, {
	textField: null
	,activeLevel: null
	,horizontal: null
	,cfNameField: null
	,mainPanel: null
	
	
	,addField: function(text, level, horizontal){
		if(this.activeLevel==null){
			this.activeLevel = level;
		}
		if(this.horizontal==null){
			this.horizontal = horizontal;
		}
		if(this.activeLevel != level || this.horizontal!=horizontal){
			return;
		}
		this.textField.insertAtCursor(text); 
	}


	,isActiveLevel: function(level, horizontal){
		if(this.activeLevel==null){
			return true;
		}else{
			return (this.activeLevel == level) && (this.horizontal == horizontal);
		}
	}
	
	, getExpression: function() {
		var expression;
		if(this.textField) {
	  		expression = this.textField.getValue();
	  		expression = Ext.util.Format.stripTags( expression );
	  		expression = expression.replace(/&nbsp;/g," ");
	  		expression = expression.replace(/\u200B/g,"");
	  		expression = expression.replace(/&gt;/g,">");
	  		expression = expression.replace(/&lt;/g,"<");
		}
		return expression;
	}

//	,validate: function(){
//		var error = SQLExpressionParser.module.validateInLineCalculatedField(this.getExpression());
//		if(error==""){
//			Sbi.exception.ExceptionHandler.showInfoMessage(LN('sbi.qbe.calculatedFields.validationwindow.success.text'), LN('sbi.qbe.calculatedFields.validationwindow.success.title'));
//		}else{
//			Sbi.exception.ExceptionHandler.showWarningMessage(error, LN('sbi.qbe.calculatedFields.validationwindow.fail.title'));
//		}
//	}
	
	
	
	,initTextField: function(){

		var buttonclear = new Ext.Button({
		    text:'Clear',
		    icon: 'null',
		    iconCls:'remove'
		});
		buttonclear.addListener('click', function(){this.textField.reset();}, this);

		
		var buttonvalidate = new Ext.Button({
		    text:'Validate',
		    icon: 'null',
		    iconCls:'option'
		});
		//buttons.validate.addListener('click', function(){this.validate();}, this);

	
		this.textField = new Ext.form.HtmlEditor({
    		name:'expression',
    	    enableAlignments : false,
    	    enableColors : false,
    	    enableFont :  false,
    	    enableFontSize : false, 
    	    enableFormat : false,
    	    enableLinks :  false,
    	    enableLists : false,
    	    enableSourceEdit : false,
    	    listeners:{
		    	'render': function(editor){
					var tb = editor.getToolbar();
					tb.add(buttonclear);
					tb.add(buttonvalidate);
		        },
    	        'initialize': {
		        	fn: function(){
						this.onFirstFocus();
	    	        } 
    	        } 
    	    }
    	});
	}
	
	, initMainPanel: function() {
		
		this.initTextField();
		
		this.cfNameField = new Ext.form.TextField({
			name:'name',
			allowBlank: false, 
			fieldLabel: 'Nome'
		});
		
		this.mainPanel = new Ext.Panel({
			layout: 'border',
		    items: [
		             new Ext.form.FormPanel({
		     	    	region:'north',
		     	    	height: 30,
		    		    border: true,
		    		    frame: false, 
			            items: [this.cfNameField],
			            bodyStyle: "background-color: transparent; border-color: transparent; padding-top: 2px; padding-left: 10px;"
			         }),
		             new Ext.Panel({
			            region:'center',
			            layout: "fit",
			    		border: true,
			    		frame: false, 
			            items: [this.textField]
			         })
		           ]
		 });
    }
});