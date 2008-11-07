{type:"class",attributes:{"name":"Basic","interfaces":"qx.application.IApplication","superClass":"qx.core.Target","fullName":"qx.application.Basic","type":"class","packageName":"qx.application"},children:[
  {type:"desc",attributes:{"text":"<p>Base class for all non <span class=\"caps\">GUI</span> qooxdoo applications.</p>\n\n<p>The {@link #main} method will be called on the document.onload event,\n{@link #close} on document.beforeunload and {@link #terminate} on document.unload.</p>"}},
  {type:"methods",children:[
    {type:"method",attributes:{"name":"close"},children:[
      {type:"desc",attributes:{"text":"<p>Called in the document.beforeunload event of the browser. If the method\nreturns a string value, the user will be asked by the browser, whether\nhe really wants to leave the page. The return string will be displayed in\nthe message box.</p>"}},
      {type:"return",attributes:{"defaultValue":"null"},children:[
        {type:"desc",attributes:{"text":"<p>message text on unloading the page</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"main"},children:[
      {type:"desc",attributes:{"text":"<p>Called in the document.onload event of the browser. This method should\nimplement the setup code of the application.</p>"}}
      ]},
    {type:"method",attributes:{"name":"terminate"},children:[
      {type:"desc",attributes:{"text":"<p>Called in the document.onunload event of the browser. This method contains the last\ncode which is run inside the page and may contain cleanup code.</p>"}}
      ]}
    ]}
  ]}