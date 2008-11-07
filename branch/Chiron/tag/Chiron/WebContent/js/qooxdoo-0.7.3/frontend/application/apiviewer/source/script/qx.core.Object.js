{type:"class",attributes:{"name":"Object","packageName":"qx.core","mixins":"qx.locale.MTranslation,qx.log.MLogging,qx.core.MUserData","childClasses":"qx.core.Target,qx.dev.TimeTracker,qx.dev.Tokenizer,qx.event.message.Message,qx.event.type.Event,qx.io.image.PreloaderManager,qx.locale.LocalizedString,qx.log.Filter,qx.log.LogEventProcessor,qx.ui.core.Border,qx.ui.core.Font,qx.ui.layout.impl.LayoutImpl,qx.ui.selection.Selection,qx.ui.table.cellrenderer.Abstract,qx.ui.table.columnmodel.resizebehavior.Abstract,qx.ui.table.headerrenderer.Default,qx.ui.table.selection.Manager,qx.ui.tree.TreeRowStructure,qx.util.Version,qx.util.format.Format,qx.util.fsm.State,qx.util.fsm.Transition,qx.util.range.IntegerRange","fullName":"qx.core.Object","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>The qooxdoo root class. All other classes are direct or indirect subclasses of this one.</p>\n\n<p>This class contains methods for:</p>\n\n<ul>\n<li>object management (creation and destruction)</li>\n<li>generic setter support</li>\n<li>user friendly OO interfaces like {@link #self} or {@link #base}</li>\n</ul>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"desc",attributes:{"text":"<p>Create a new instance</p>"}}
      ]}
    ]},
  {type:"methods-static",children:[
    {type:"method",attributes:{"isStatic":"true","name":"dispose"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"defaultValue":"false","name":"unload"},children:[
          {type:"desc",attributes:{"text":"<p>Whether the dispose is fired through the page unload event</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Destructor. This method is called by qooxdoo on object destruction.</p>\n\n<p>Any class that holds resources like links to <span class=\"caps\">DOM</span> nodes must override\nthis method and free these resources.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"internal","isInternal":"true","isStatic":"true","name":"getDb"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the database created, but not yet disposed elements.\nPlease be sure to not modify the given array!</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The database</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Array"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"inGlobalDispose"},children:[
      {type:"desc",attributes:{"text":"<p>Returns whether a global dispose is currently taking place.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>whether a global dispose is taking place.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"isPageUnload"},children:[
      {type:"desc",attributes:{"text":"<p>Returns whether a global unload (page unload) is currently taking place.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>whether a global unload is taking place.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"toHashCode"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"obj"},children:[
          {type:"desc",attributes:{"text":"<p>the Object to get the hashcode for</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Returns an unique identifier for the given object. If such an identifier\ndoes not yet exist, create it.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>unique identifier for the given object</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"private","name":"__disposeObjectsDeepRecurser"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"obj"},children:[
          {type:"desc",attributes:{"text":"<p>object to dispose</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"deep"},children:[
          {type:"desc",attributes:{"text":"<p>how deep to following sub objects. Deep=0 means\n  just the object and all its keys. Deep=1 also dispose deletes the\n  objects object content.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Number"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Helper method for _disposeObjectDeep. Do the recursive work.</p>"}}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_disposeFields"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"varargs"},children:[
          {type:"desc",attributes:{"text":"<p>fields to dispose</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"arguments"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Disconnects given fields from instance.</p>"}}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_disposeObjectDeep"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"name"},children:[
          {type:"desc",attributes:{"text":"<p>field name to dispose</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"deep"},children:[
          {type:"desc",attributes:{"text":"<p>how deep to following sub objects. Deep=0 means\n  just the object and all its keys. Deep=1 also dispose deletes the\n  objects object content.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Number"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Disconnects and disposes given objects (deeply) from instance.\nWorks with arrays, maps and qooxdoo objects.</p>"}}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_disposeObjects"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"varargs"},children:[
          {type:"desc",attributes:{"text":"<p>fields to dispose</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"arguments"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Disconnects and disposes given objects from instance.\nOnly works with qx.core.Object based objects e.g. Widgets.</p>"}}
      ]},
    {type:"method",attributes:{"name":"base"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"args"},children:[
          {type:"desc",attributes:{"text":"<p>the arguments variable of the calling method</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"arguments"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"varags"},children:[
          {type:"desc",attributes:{"text":"<p>variable number of arguments passed to the overwritten function</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Call the same method of the super class.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the return value of the method of the base class.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"dispose"},children:[
      {type:"desc",attributes:{"text":"<p>Dispose this object</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"get"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"prop"},children:[
          {type:"desc",attributes:{"text":"<p>Name of the property.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Returns the value of the given property.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The value of the value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"internal","isInternal":"true","name":"getDbKey"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the key of the object used in the objects DB\nreceived by {@link #getDb()}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The key in the db for the current object.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getDisposed"},children:[
      {type:"desc",attributes:{"text":"<p>Returns true if the object is disposed.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>whether the object has been disposed</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isDisposed"},children:[
      {type:"desc",attributes:{"text":"<p>Returns true if the object is disposed.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>whether the object has been disposed</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"reset"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"prop"},children:[
          {type:"desc",attributes:{"text":"<p>Name of the property.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Resets the value of the given property.</p>"}}
      ]},
    {type:"method",attributes:{"name":"self"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"args"},children:[
          {type:"desc",attributes:{"text":"<p>the arguments variable of the calling method</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"arguments"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Returns the static class (to access static members of this class)</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the return value of the method of the base class.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"set"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"data"},children:[
          {type:"desc",attributes:{"text":"<p>a map of property values. The key is the name of the property.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Map"}},
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"","name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>the value, only used when <code>data</code> is a string.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets multiple properties at once by using a property list or\nsets one property and its value by the first and second argument.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>this instance.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Object"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toHashCode"},children:[
      {type:"desc",attributes:{"text":"<p>Return unique hash code of object</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>unique hash code of the object</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toString"},children:[
      {type:"desc",attributes:{"text":"<p>Returns a string represantation of the qooxdoo object.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>string representation of the object</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]}
    ]}
  ]}