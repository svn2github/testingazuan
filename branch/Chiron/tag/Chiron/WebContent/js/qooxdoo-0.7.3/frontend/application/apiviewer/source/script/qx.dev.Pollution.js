{type:"class",attributes:{"isStatic":"true","name":"Pollution","packageName":"qx.dev","fullName":"qx.dev.Pollution","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>Retrieve information about global namespace pollution</p>"}},
  {type:"methods-static",children:[
    {type:"method",attributes:{"isStatic":"true","name":"extract"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"objectName"},children:[
          {type:"desc",attributes:{"text":"<p>Name of the objects to inspect. Valid names are:\n    <ul>\n      <li>window</li>\n      <li>document</li>\n      <li>body</li>\n    </ul></p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Return a list of objects which are not supposed to be in the given object.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Array of values, which are not supposed to be in the given object.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Map","dimensions":"1"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getHtmlTable"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"objectName"},children:[
          {type:"desc",attributes:{"text":"<p>Name of the objects to inspect. Valid names are:\n    <ul>\n      <li>window</li>\n      <li>document</li>\n      <li>body</li>\n    </ul></p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Format the global pollution list as a <span class=\"caps\">HTML</span> fragment</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p><span class=\"caps\">HTML</span> fragment</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getInfo"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"defaultValue":"\"window\"","name":"objectName"},children:[
          {type:"desc",attributes:{"text":"<p>name of the object to inspect. Valid Names are:\n    <ul>\n      <li>window</li>\n      <li>document</li>\n      <li>body</li>\n    </ul></p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Show the namespace pollution of a given object or the golbal namespace.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getTextList"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"objectName"},children:[
          {type:"desc",attributes:{"text":"<p>Name of the objects to inspect. Valid names are:\n    <ul>\n      <li>window</li>\n      <li>document</li>\n      <li>body</li>\n    </ul></p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Format the global pollution list as a test list</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>global pollution list</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]}
    ]}
  ]}