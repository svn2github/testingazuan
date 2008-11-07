{type:"class",attributes:{"name":"Target","packageName":"qx.core","superClass":"qx.core.Object","childClasses":"qx.application.Basic,qx.application.Gui,qx.client.Command,qx.client.History,qx.client.NativeWindow,qx.client.Timer,qx.core.Init,qx.event.handler.EventHandler,qx.event.handler.FocusHandler,qx.event.handler.KeyEventHandler,qx.io.image.Manager,qx.io.image.Preloader,qx.io.image.PreloaderSystem,qx.io.remote.AbstractRemoteTransport,qx.io.remote.Exchange,qx.io.remote.Request,qx.io.remote.RequestQueue,qx.io.remote.Rpc,qx.theme.manager.Icon,qx.theme.manager.Meta,qx.theme.manager.Widget,qx.ui.core.Widget,qx.ui.selection.RadioManager,qx.ui.selection.SelectionManager,qx.ui.table.celleditor.CheckBox,qx.ui.table.celleditor.ComboBox,qx.ui.table.celleditor.Dynamic,qx.ui.table.celleditor.PasswordField,qx.ui.table.celleditor.TextField,qx.ui.table.columnmodel.Basic,qx.ui.table.model.Abstract,qx.ui.table.pane.Model,qx.ui.table.rowrenderer.Default,qx.ui.table.selection.Model,qx.util.GuiBuilder,qx.util.fsm.FiniteStateMachine,qx.util.manager.Object,qx.util.manager.Value,qx.util.range.Range","fullName":"qx.core.Target","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>This is the main constructor for all objects that need to be connected to qx.event.type.Event objects.</p>\n\n<p>In objects created with this constructor, you find functions to addEventListener or\nremoveEventListener to or from the created object. Each event to connect to has a type in\nform of an identification string. This type could be the name of a regular dom event like &#8220;click&#8221; or\nsomething self-defined like &#8220;ready&#8221;.</p>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"}}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","name":"_dispatchEvent"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"evt"},children:[
          {type:"desc",attributes:{"text":"<p>event to dispatch</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Internal event dispatch method</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"addEventListener"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"type"},children:[
          {type:"desc",attributes:{"text":"<p>name of the event type</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"func"},children:[
          {type:"desc",attributes:{"text":"<p>event callback function</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Function"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"window","name":"obj"},children:[
          {type:"desc",attributes:{"text":"<p>reference to the &#8216;this&#8217; variable inside the callback</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Add event listener to an object.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"createDispatchChangeEvent"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"type"},children:[
          {type:"desc",attributes:{"text":"<p>name of the event type</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>property value attached to the event object</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"old"},children:[
          {type:"desc",attributes:{"text":"<p>old property value attached to the event object</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Checks if the event is registered. If so it creates an event object and dispatches it.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"createDispatchDataEvent"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"type"},children:[
          {type:"desc",attributes:{"text":"<p>name of the event type</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"data"},children:[
          {type:"desc",attributes:{"text":"<p>user defined data attached to the event object</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Checks if the event is registered. If so it creates an event object and dispatches it.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"createDispatchEvent"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"type"},children:[
          {type:"desc",attributes:{"text":"<p>name of the event type</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Checks if the event is registered. If so it creates an event object and dispatches it.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"dispatchEvent"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"evt"},children:[
          {type:"desc",attributes:{"text":"<p>event to dispatch</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"dispose"},children:[
          {type:"desc",attributes:{"text":"<p>whether the event object should be disposed after all event handlers run.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Dispatch an event</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>whether the event default was prevented or not. Returns true, when the event was <span class=\"caps\">NOT</span> prevented.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"hasEventListeners"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"type"},children:[
          {type:"desc",attributes:{"text":"<p>name of the event type</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Check if there are one or more listeners for an event type.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"removeEventListener"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"type"},children:[
          {type:"desc",attributes:{"text":"<p>name of the event type</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"func"},children:[
          {type:"desc",attributes:{"text":"<p>event callback function</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Function"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"window","name":"obj"},children:[
          {type:"desc",attributes:{"text":"<p>reference to the &#8216;this&#8217; variable inside the callback</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Remove event listener from object</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]}
    ]}
  ]}