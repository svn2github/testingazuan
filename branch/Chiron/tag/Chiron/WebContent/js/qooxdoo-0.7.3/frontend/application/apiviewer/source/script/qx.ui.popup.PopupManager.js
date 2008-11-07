{type:"class",attributes:{"name":"PopupManager","packageName":"qx.ui.popup","superClass":"qx.util.manager.Object","isSingleton":"true","fullName":"qx.ui.popup.PopupManager","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>This singleton is used to manager multiple instances of popups and their state.</p>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"}}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"name":"update"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vTarget"},children:[
          {type:"desc",attributes:{"text":"<p>current widget</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.ui.popup.Popup"}},
            {type:"entry",attributes:{"type":"qx.ui.popup.ToolTip"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Updates all registered popups</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]}
    ]},
  {type:"methods-static",children:[
    {type:"method",attributes:{"isStatic":"true","name":"getInstance"},children:[
      {type:"desc",attributes:{"text":"<p>Returns a singleton instance of this class. On the first call the class\nis instantiated by calling the constructor with no arguments. All following\ncalls will return this instance.</p>\n\n<p>This method has been added by setting the &#8220;type&#8221; key in the class definition\n({@link qx.Class#define}) to &#8220;singleton&#8221;.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The singleton instance of this class.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"qx.ui.popup.PopupManager"}}
          ]}
        ]}
      ]}
    ]}
  ]}