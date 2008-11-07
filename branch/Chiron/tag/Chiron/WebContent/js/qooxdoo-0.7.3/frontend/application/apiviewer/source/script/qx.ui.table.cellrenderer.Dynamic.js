{type:"class",attributes:{"name":"Dynamic","hasWarning":"true","packageName":"qx.ui.table.cellrenderer","superClass":"qx.ui.table.cellrenderer.Default","fullName":"qx.ui.table.cellrenderer.Dynamic","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>A cell renderer factory which can dynamically exchange the cell renderer\nbased on information retrieved at runtime. This is useful when different\nrows in a column should have different cell renderer based on cell content\nor row metadata. A typical example would be a spreadsheet that has different\nkind of data in one column.</p>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"cellRendererFactoryFunction"}}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"hasError":"true","overriddenFrom":"qx.ui.table.cellrenderer.Abstract","name":"createDataCellHtml"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"cellInfo"},children:[
          {type:"desc",attributes:{"text":"<p>The information about the cell.\n         See {@link #createDataCellHtml}.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Map"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"htmlArr"}}
        ]},
      {type:"desc",attributes:{"text":"<p>Overridden; called whenever the cell updates. The cell will call the\nfunction stored in the cellRendererFactoryFunction to retrieve the\ncell renderer which should be used for this particular cell</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Parameter <code>htmlArr</code> is not documented.","column":"26","line":"108"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"getCellRendererFactoryFunction","fromProperty":"cellRendererFactoryFunction"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>cellRendererFactoryFunction</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #cellRendererFactoryFunction}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>cellRendererFactoryFunction</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initCellRendererFactoryFunction","fromProperty":"cellRendererFactoryFunction"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>cellRendererFactoryFunction</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>cellRendererFactoryFunction</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #cellRendererFactoryFunction}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetCellRendererFactoryFunction","fromProperty":"cellRendererFactoryFunction"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>cellRendererFactoryFunction</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #cellRendererFactoryFunction}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setCellRendererFactoryFunction","fromProperty":"cellRendererFactoryFunction"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>cellRendererFactoryFunction</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>cellRendererFactoryFunction</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #cellRendererFactoryFunction}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"allowNull":"true","defaultValue":"null","propertyType":"new","name":"cellRendererFactoryFunction","check":"Function"},children:[
      {type:"desc",attributes:{"text":"<p>Function that returns a cellRenderer instance which will be\nused for the row that is currently being edited. The function is\ndefined like this:</p>\n\n<pre class=\"javascript\">\nmyTable.getTableColumnModel().setCellRenderer(function(cellInfo){\n  // based on the cellInfo map or other information, return the\n  // appropriate cell renderer\n  if (cellInfo.row == 5)\n    return new qx.ui.table.cellrenderer.Boolean;\n  else\n    return new qx.ui.table.cellrenderer.Default;\n});\n</pre>\n\n<p>the function <span class=\"caps\">MUST</span> return at least a qx.ui.table.cellrenderer.Default</p>"}}
      ]}
    ]}
  ]}