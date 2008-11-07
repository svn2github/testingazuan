{type:"class",attributes:{"name":"LOVDetailsForm","hasWarning":"true","packageName":"spagobi.ui.custom","superClass":"spagobi.ui.Form","fullName":"spagobi.ui.custom.LOVDetailsForm","type":"class"},children:[{type:"desc",attributes:{"text":"<p>This class defines the Predefined List of Values Form.</p>"}},{type:"constructor",children:[{type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[{type:"desc",attributes:{"text":"<p>When the constructor is called it returns an object of form type.</p>\n\n<p>To this form is associated the following fields :-</p>\n\n<p>Label -> dataindex: &#8216;label&#8217;</p>\n\n<p>Name  -> dataIndex: &#8216;name&#8217;</p>\n\n<p>Description -> dataIndex: &#8216;description&#8217;,</p>\n\n<p>Type -> dataIndex: &#8216;type&#8217;</p>\n\n<p>Form -> dataIndex: &#8216;querystmt&#8217;. It contains the following fields:</p>\n\n<p>&nbsp;&nbsp;&nbsp;&nbsp; Data Source label -> &#8216;datasourcelabel&#8217;</p>\n\n<p>&nbsp;&nbsp;&nbsp;&nbsp; Query Definition -> &#8216;querydef&#8217;</p>\n\n<p>Form -> dataIndex: &#8216;scriptloadvalues&#8217;. It contains the following fields:</p>\n\n<p>&nbsp;&nbsp;&nbsp;&nbsp; Script -> &#8216;script&#8217;</p>\n\n<p>Form -> dataIndex: &#8216;fixedlov&#8217;. It contains the following fields:</p>\n\n<p>&nbsp;&nbsp;&nbsp;&nbsp; Value -> &#8216;value&#8217;</p>\n\n<p>&nbsp;&nbsp;&nbsp;&nbsp; Description -> &#8216;description2&#8217;</p>\n\n<p>Form -> dataIndex: &#8216;javaclass&#8217;. It contains the following fields:</p>\n\n<p>&nbsp;&nbsp;&nbsp;&nbsp; Java Class Name -> &#8216;classname&#8217;</p>\n\n<p>*Example :- *\n  var predefLoVform = new spagobi.ui.custom.PredefinedLoVForm();\n  predefLoVform.setData({\n 						label: &#8216;Label&#8217;,\n  						name: &#8216;Name&#8217;,\n						description: &#8216;Description&#8217;,\n  						type: &#8216;Type&#8217;,\n 						querystmt: {\n 									datasourcelabel: &#8216;Data Source label&#8217;,\n 									querydef: &#8216;Query Definition&#8217;\n 									},\n 						scriptloadvalues: {\n 									script: &#8216;Script&#8217;,\n 									},\n 						fixedlov: 	{\n 									value: &#8216;Value&#8217;,\n 									description2: &#8216;Description&#8217;,\n 								  	},	\n 						javaclass: {\n 									classname: &#8216;Java Class Name&#8217;\n 									}\n  });</p>"}}]}]},{type:"methods",children:[{type:"method",attributes:{"access":"protected","hasError":"true","name":"_documentTypeChangeValueHandler"},children:[{type:"params",children:[{type:"param",attributes:{"name":"e"}}]},{type:"errors",children:[{type:"error",attributes:{"msg":"Documentation is missing.","column":"37","line":"201"}}]}]}]}]}