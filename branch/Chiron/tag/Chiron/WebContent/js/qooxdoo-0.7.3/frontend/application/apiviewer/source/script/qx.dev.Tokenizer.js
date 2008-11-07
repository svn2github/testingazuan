{type:"class",attributes:{"name":"Tokenizer","packageName":"qx.dev","superClass":"qx.core.Object","fullName":"qx.dev.Tokenizer","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>Simple JavaScript tokenizer used to print syntax highlighted\nJavaScript code.</p>\n\n<p>Based on Public Domain code by Christopher Diggins\n<a href=\"http://www.cdiggins.com/tokenizer.html\">http://www.cdiggins.com/tokenizer.html</a></p>"}},
  {type:"methods-static",children:[
    {type:"method",attributes:{"isStatic":"true","name":"javaScriptToHtml"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"javaScriptText"},children:[
          {type:"desc",attributes:{"text":"<p>String of JavaScript code to tokenize</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Create a colored <span class=\"caps\">HTML</span> string for a string of JavaScript code.\nThe colored elements are placed in <code>span</code> elements\nwith class names correponding to the token types. The returned code\nshould be placed into <code>pre</code> tags to preserve the\nindentation.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p><span class=\"caps\">HTML</span> fragment with the colored JavaScript code.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"tokenizeJavaScript"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"javaScriptText"},children:[
          {type:"desc",attributes:{"text":"<p>String of JavaScript code to tokenize</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Tokenizes a string of JavaScript code.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Array of tokens. A token is a map with the fields\n  <code>type</code> containing the token type and <code>value</code>,\n  which contains the string value of the token from the input string.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Map","dimensions":"1"}}
          ]}
        ]}
      ]}
    ]}
  ]}