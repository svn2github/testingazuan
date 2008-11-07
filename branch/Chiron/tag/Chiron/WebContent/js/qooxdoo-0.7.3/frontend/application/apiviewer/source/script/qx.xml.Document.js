{type:"class",attributes:{"isStatic":"true","name":"Document","packageName":"qx.xml","fullName":"qx.xml.Document","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p><span class=\"caps\">XML</span> Document</p>\n\n<p>Tested with IE6, Firefox 2.0, WebKit/Safari 3.0 and Opera 9</p>\n\n<p><a href=\"http://msdn2.microsoft.com/en-us/library/ms535918.aspx\">http://msdn2.microsoft.com/en-us/library/ms535918.aspx</a>\n<a href=\"http://developer.mozilla.org/en/docs/Parsing_and_serializing_XML\">http://developer.mozilla.org/en/docs/Parsing_and_serializing_XML</a></p>"}},
  {type:"methods-static",children:[
    {type:"method",attributes:{"isStatic":"true","name":"create"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"defaultValue":"null","name":"namespaceUri"},children:[
          {type:"desc",attributes:{"text":"<p>The namespace <span class=\"caps\">URI</span> of the document element to create or null.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}},
            {type:"entry",attributes:{"type":"null"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"null","name":"qualifiedName"},children:[
          {type:"desc",attributes:{"text":"<p>The qualified name of the document element to be created or null.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}},
            {type:"entry",attributes:{"type":"null"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Create an <span class=\"caps\">XML</span> document.\n<a href=\"http://www.w3.org/TR/DOM-Level-2-Core/core.html#i-Document\">http://www.w3.org/TR/DOM-Level-2-Core/core.html#i-Document</a></p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>empty <span class=\"caps\">XML</span> document</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Document"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"fromString"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"str"},children:[
          {type:"desc",attributes:{"text":"<p>the string to be parsed</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>The string passed in is parsed into a <span class=\"caps\">DOM</span> document.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p><span class=\"caps\">TODO</span>: move to create()</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Document"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"isDocument"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"obj"},children:[
          {type:"desc",attributes:{"text":"<p>object to check</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Check whether an object is a Document instance</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>whether the object is a Document instance</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]}
    ]}
  ]}