{type:"class",attributes:{"name":"Filtered","hasWarning":"true","packageName":"qx.ui.table.model","superClass":"qx.ui.table.model.Simple","fullName":"qx.ui.table.model.Filtered","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>A filtered table model to provide support for hiding and filtering table\nrows.</p>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"}}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","hasError":"true","name":"_js_in_array"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"the_needle"}},
        {type:"param",attributes:{"name":"the_haystack"}}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"20","line":"49"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"addBetweenFilter"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"filter"},children:[
          {type:"desc",attributes:{"text":"<p>The type of filter. Accepted strings are &#8220;between&#8221; and &#8221;!between&#8221;.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"value1"},children:[
          {type:"desc",attributes:{"text":"<p>The first value to compare against.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"value2"},children:[
          {type:"desc",attributes:{"text":"<p>The second value to compare against.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"target"},children:[
          {type:"desc",attributes:{"text":"<p>The text value of the column to compare against.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>The addBetweenFilter method is used to add a between filter to the\ntable model.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"addNumericFilter"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"filter"},children:[
          {type:"desc",attributes:{"text":"<p>The type of filter. Accepted strings are:\n   &#8221;==&#8221;, &#8221;!=&#8221;, &#8221;>&#8221;, &#8221;<\", \">=&#8221;, and &#8221;<=&#8221;.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"value1"},children:[
          {type:"desc",attributes:{"text":"<p>The value to compare against.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"target"},children:[
          {type:"desc",attributes:{"text":"<p>The text value of the column to compare against.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>The addNumericFilter method is used to add a basic numeric filter to\nthe table model.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"addRegex"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"regex"},children:[
          {type:"desc",attributes:{"text":"<p>The regular expression to match against.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"target"},children:[
          {type:"desc",attributes:{"text":"<p>The text value of the column to compare against.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>The addRegex method is used to add a regular expression filter to the\ntable model.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"applyFilters"},children:[
      {type:"desc",attributes:{"text":"<p>The applyFilters method is called to apply filters to the table model.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"hasError":"true","name":"hideRows"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"rowNum"},children:[
          {type:"desc",attributes:{"text":"<p>Index of the first row to be hidden in the table.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"numOfRows"},children:[
          {type:"desc",attributes:{"text":"<p>The number of rows to be hidden sequentially after rowNum.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"dispatchEvent"}}
        ]},
      {type:"desc",attributes:{"text":"<p>Hides a specified number of rows.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Parameter <code>dispatchEvent</code> is not documented.","column":"16","line":"302"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetHiddenRows"},children:[
      {type:"desc",attributes:{"text":"<p>Return the table to the original state with all rows shown and clears\nall filters.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]}
    ]}
  ]}