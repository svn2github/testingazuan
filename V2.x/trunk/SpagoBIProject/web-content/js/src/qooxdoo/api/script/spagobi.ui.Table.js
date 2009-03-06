{type:"class",attributes:{"name":"Table","hasWarning":"true","packageName":"spagobi.ui","superClass":"qx.ui.table.Table","childClasses":"spagobi.ui.Roles","fullName":"spagobi.ui.Table","type":"class"},children:[{type:"desc",attributes:{"text":"<p>Class to create a list (table)</p>"}},{type:"constructor",children:[{type:"method",attributes:{"overriddenFrom":"qx.ui.table.Table","isCtor":"true","name":"ctor"},children:[{type:"params",children:[{type:"param",attributes:{"name":"controller"},children:[{type:"desc",attributes:{"text":"<p>Object which is used to set the data of the form</p>"}}]},{type:"param",attributes:{"name":"data"},children:[{type:"desc",attributes:{"text":"<p>Object containing the layout of the list and the data of the list and form</p>"}}]}]},{type:"desc",attributes:{"text":"<p>Constructor to create a list (table)</p>\n\n<p><strong>Example</strong></p>\n\n<code>\nvar obj = new spagobi.ui.custom.MasterDetailsPage();\nvar records = spagobi.app.data.DataService.loadFeatureRecords();\nvar listPage = new spagobi.ui.Table(obj, records );\n</code>"}}]}]},{type:"methods",children:[{type:"method",attributes:{"access":"protected","hasError":"true","name":"_onCellClick"},children:[{type:"params",children:[{type:"param",attributes:{"name":"e"}}]},{type:"errors",children:[{type:"error",attributes:{"msg":"Parameter <code>e</code> is not documented.","column":"14","line":"274"}},{type:"error",attributes:{"msg":"Documentation is missing.","column":"14","line":"274"}}]}]},{type:"method",attributes:{"access":"protected","hasError":"true","name":"_onChangeSelection"},children:[{type:"params",children:[{type:"param",attributes:{"name":"e"},children:[{type:"types",children:[{type:"entry",attributes:{"type":"Event"}}]}]}]},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]},{type:"errors",children:[{type:"error",attributes:{"msg":"Parameter <code>e</code> is not documented.","column":"20","line":"257"}},{type:"error",attributes:{"msg":"Documentation is missing.","column":"20","line":"257"}}]}]},{type:"method",attributes:{"hasError":"true","name":"cellRendererFactoryFunction"},children:[{type:"params",children:[{type:"param",attributes:{"name":"cellInfo"}}]},{type:"errors",children:[{type:"error",attributes:{"msg":"Parameter <code>cellInfo</code> is not documented.","column":"29","line":"202"}},{type:"error",attributes:{"msg":"Documentation is missing.","column":"29","line":"202"}}]}]},{type:"method",attributes:{"hasError":"true","name":"getUpdatedData"},children:[{type:"desc",attributes:{"text":"<p>Function used to get the data in the list</p>\n\n<p><strong>Example</strong></p>\n\n<code>\n\n</code>"}},{type:"errors",children:[{type:"error",attributes:{"msg":"Contains information for a non-existing parameter <code>data</code>.","column":"16","line":"243"}}]}]},{type:"method",attributes:{"hasError":"true","name":"propertyCellEditorFactoryFunc"},children:[{type:"params",children:[{type:"param",attributes:{"name":"cellInfo"}}]},{type:"errors",children:[{type:"error",attributes:{"msg":"Parameter <code>cellInfo</code> is not documented.","column":"31","line":"187"}},{type:"error",attributes:{"msg":"Documentation is missing.","column":"31","line":"187"}}]}]},{type:"method",attributes:{"hasError":"true","name":"propertyCellRendererFactoryFunc"},children:[{type:"params",children:[{type:"param",attributes:{"name":"cellInfo"}}]},{type:"errors",children:[{type:"error",attributes:{"msg":"Parameter <code>cellInfo</code> is not documented.","column":"33","line":"197"}},{type:"error",attributes:{"msg":"Documentation is missing.","column":"33","line":"197"}}]}]},{type:"method",attributes:{"hasError":"true","name":"propertyCellRendererFactoryFunc1"},children:[{type:"params",children:[{type:"param",attributes:{"name":"cellInfo"}}]},{type:"errors",children:[{type:"error",attributes:{"msg":"Parameter <code>cellInfo</code> is not documented.","column":"34","line":"192"}},{type:"error",attributes:{"msg":"Documentation is missing.","column":"34","line":"192"}}]}]},{type:"method",attributes:{"name":"setData"},children:[{type:"params",children:[{type:"param",attributes:{"name":"data"},children:[{type:"desc",attributes:{"text":"<p>Object containing the layout of the list and the data of the list and form</p>"}}]}]},{type:"desc",attributes:{"text":"<p>Function used to set the data in the list</p>\n\n<p><strong>Example</strong></p>\n\n<code>\n\n</code>"}}]}]}]}