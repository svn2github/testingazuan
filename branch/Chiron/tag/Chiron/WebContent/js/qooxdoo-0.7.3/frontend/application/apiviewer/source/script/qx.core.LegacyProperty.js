{type:"class",attributes:{"isStatic":"true","name":"LegacyProperty","packageName":"qx.core","fullName":"qx.core.LegacyProperty","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>Internal class for handling dynamic properties.</p>\n\n<p><span class=\"caps\">WARNING</span>: This is a legacy class to support the old-style dynamic properties\nin 0.6.x. Its much improved successor is {@link qx.core.Property}.</p>"}},
  {type:"deprecated",children:[
    {type:"desc",attributes:{"text":"<p>This class is supposed to be removed in qooxdoo 0.7</p>"}}
    ]},
  {type:"methods-static",children:[
    {type:"method",attributes:{"isStatic":"true","name":"addCachedProperty"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"config"},children:[
          {type:"desc",attributes:{"text":"<p>Configuration structure</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Map"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"proto"},children:[
          {type:"desc",attributes:{"text":"<p>Prototype where the methods should be attached</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"deprecated"},
      {type:"desc",attributes:{"text":"<p>Adds a so-named cached property to a prototype</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"addFastProperty"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"config"},children:[
          {type:"desc",attributes:{"text":"<p>Configuration structure</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Map"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"proto"},children:[
          {type:"desc",attributes:{"text":"<p>Prototype where the methods should be attached</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"deprecated"},
      {type:"desc",attributes:{"text":"<p>Adds a so-named fast property to a prototype.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"addProperty"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"config"},children:[
          {type:"desc",attributes:{"text":"<p>Configuration structure</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Map"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"proto"},children:[
          {type:"desc",attributes:{"text":"<p>Prototype where the methods should be attached</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"deprecated"},
      {type:"desc",attributes:{"text":"<p>Adds a property to a prototype</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getGetterName"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"name"},children:[
          {type:"desc",attributes:{"text":"<p>name of a property</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Converts the property name to the getter name</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>name of the setter for this property</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getResetterName"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"name"},children:[
          {type:"desc",attributes:{"text":"<p>name of a property</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Converts the property name to the resetter name</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>name of the setter for this property</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getSetterName"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"name"},children:[
          {type:"desc",attributes:{"text":"<p>name of a property</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Converts the property name to the setter name</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>name of the setter for this property</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]}
    ]}
  ]}