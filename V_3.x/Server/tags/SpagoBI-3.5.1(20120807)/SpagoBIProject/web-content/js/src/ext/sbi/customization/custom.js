	/**
	 * SUbtitle in the ParametersPannel (la p\u00E9riode d\'analyse par d\u00E9faut est la veille) (par d\u00E9faut, la date correspond au mois dernier)'
)
	 */
	Ext.override(Sbi.execution.ParametersPanel, {
		
		getHelpMessageForPage2: function(executionInstance, thereAreParametersToBeFilled) {
			var toReturn = null;
			var doc = executionInstance.document;
			if (doc.typeCode == 'DATAMART' && this.baseConfig.subobject == undefined) {
				if (Sbi.user.functionalities.contains('BuildQbeQueriesFunctionality')) {
					if (!thereAreParametersToBeFilled) {
						toReturn = LN('sbi.execution.parametersselection.message.page2.qbe.powerUserMessageWithoutParameters');
					} else {
						toReturn = LN('sbi.execution.parametersselection.message.page2.qbe.powerUserMessageWithParameters');
					}
				} else {
					if (!thereAreParametersToBeFilled) {
						toReturn = LN('sbi.execution.parametersselection.message.page2.qbe.readOnlyUserMessageWithoutParameters');
					} else {
						toReturn = LN('sbi.execution.parametersselection.message.page2.qbe.readOnlyUserMessageWithParameters');
					}
				}
			} else {
				if (!thereAreParametersToBeFilled) {
					toReturn = LN('sbi.execution.parametersselection.message.page2.execute');
				} else {
					var day=false;
					for(p in this.fields) {
						if(this.fields[p].name=='DAY'){
							day = true;
							break;
						}
					}
					if(day){
						toReturn = LN('sbi.execution.parametersselection.message.page2.fillFormAndExecute')+ LN('sbi.execution.parametersselection.message.page2.fillFormAndExecute.additionalinformation1');
					}else{
						toReturn = LN('sbi.execution.parametersselection.message.page2.fillFormAndExecute')+ LN('sbi.execution.parametersselection.message.page2.fillFormAndExecute.additionalinformation2');
					}
					
				}
			}
			return toReturn;
		}
	 
	});
	
	/*
	 * Validation fo the date.
	 * If the user write a wring date a message will be showed
	 * 
	 * */
	Ext.override(Ext.form.DateField, {
	    parseDate : function(value){
	        if(!value || value instanceof Date){
	            return value;
	        }
	        if(value!=undefined && value!=null && value.length>9  ){

	        	 var firstSeparatorIndexDATEFORMAT = this.format.indexOf('/');
	        	 var secondSeparatorIndexDATEFORMAT = this.format.indexOf('/',firstSeparatorIndexDATEFORMAT+1);
	        	 var first =  this.format.substring(0,firstSeparatorIndexDATEFORMAT);
	        	 var second =  this.format.substring(firstSeparatorIndexDATEFORMAT+1,secondSeparatorIndexDATEFORMAT);
	        	 var third =  this.format.substring(secondSeparatorIndexDATEFORMAT+1);

	        	 
	        	 var firstSeparatorIndex = value.indexOf('/');
	        	 var secondSeparatorIndex = value.indexOf('/',firstSeparatorIndex+1);
	        	 var day;
	        	 var month;
	        	 var year;

	        	 if(first=='d' || first=='D' || first=='dd' || first=='DD'){
	        		 day = (value.substring(0,firstSeparatorIndex));
	        	 }else if(first=='m' ||first=='M' ||first=='mm' || first=='MM'){
	        		 month= (value.substring(0,firstSeparatorIndex));
	        	 }else if(third=='y' ||third=='Y' ||third=='yyyy' || third=='YYYY'){
	        		 year= (value.substring(0,firstSeparatorIndex));
	        	 }
	        	 if(second=='d' || second=='D' || second=='dd' || second=='DD'){
	        		 day = (value.substring(firstSeparatorIndex+1,secondSeparatorIndex));
	        	 }else if(second=='m' ||second=='M' ||second=='mm' || second=='MM'){
	        		 month= (value.substring(firstSeparatorIndex+1,secondSeparatorIndex));
	        	 }else if(second=='y' ||second=='Y' ||second=='yyyy' || second=='YYYY'){
	        		 year= (value.substring(firstSeparatorIndex+1,secondSeparatorIndex));
	        	 }
	        	 if(third=='d' || third=='D' || third=='dd' || third=='DD'){
	        		 day = (value.substring(secondSeparatorIndex+1));
	        	 }else if(third=='m' ||third=='M' ||third=='mm' || third=='MM'){
	        		 month= (value.substring(secondSeparatorIndex+1));
	        	 }else if(third=='y' ||third=='Y' ||third=='yyyy' || third=='YYYY'){
	        		 year= (value.substring(secondSeparatorIndex+1));
	        	 }
	        	 
	        	 
	 	        var v = Date.parseDate(value, this.format);
	 	        
	 	        if(v.getDate()!=day || v.getMonth()!=month-1 || v.getFullYear()!=year){
	 	        	Sbi.exception.ExceptionHandler.showErrorMessage(LN('ext.date.dateformat.error.text'), LN('ext.date.dateformat.error.title'));
	 	        }
	

		        if(!v && this.altFormats){
		            if(!this.altFormatsArray){
		                this.altFormatsArray = this.altFormats.split("|");
		            }
		            for(var i = 0, len = this.altFormatsArray.length; i < len && !v; i++){
		                v = Date.parseDate(value, this.altFormatsArray[i]);
		            }
		        }
		        
		       
		        return v;

	        }else{
		        var v = Date.parseDate(value, this.format);
		        //alert(value);
		        var v2 = Date.parseDate(value, this.format);
		        if(!v && this.altFormats){
		            if(!this.altFormatsArray){
		                this.altFormatsArray = this.altFormats.split("|");
		            }
		            for(var i = 0, len = this.altFormatsArray.length; i < len && !v; i++){
		                v = Date.parseDate(value, this.altFormatsArray[i]);
		            }
		        }
//		        if(v!=v2){alert('FORMAT ERROR '+v+' '+v2);}
		        return v;
	        	
	        }
	        
	        
	       

	    }
	});
