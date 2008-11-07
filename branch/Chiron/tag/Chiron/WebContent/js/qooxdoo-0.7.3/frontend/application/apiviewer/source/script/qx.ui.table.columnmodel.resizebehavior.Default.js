{type:"class",attributes:{"name":"Default","hasWarning":"true","packageName":"qx.ui.table.columnmodel.resizebehavior","superClass":"qx.ui.table.columnmodel.resizebehavior.Abstract","fullName":"qx.ui.table.columnmodel.resizebehavior.Default","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>The default resize behavior.  Until a resize model is loaded, the default\nbehavior is to:\n<ol>\n  <li>\n    Upon the table initially appearing, and upon any window resize, divide\n    the table space equally between the visible columns.\n  </li>\n  <li>\n    When a column is increased in width, all columns to its right are\n    pushed to the right with no change to their widths.  This may push some\n    columns off the right edge of the table, causing a horizontal scroll\n    bar to appear.\n  </li>\n  <li>\n    When a column is decreased in width, if the total width of all columns\n    is <i>greater than</i> the table width, no additional column wiidth\n    changes are made.\n  </li>\n  <li>\n    When a column is decreased in width, if the total width of all columns\n    is <i>less than</i> the width of the table, the visible column\n    immediately to the right of the column which decreased in width has its\n    width increased to fill the remaining space.\n  </li>\n</ol></p>\n\n<p>A resize model may be loaded to provide more guidance on how to adjust\ncolumn width upon each of the events: initial appear, window resize, and\ncolumn resize. *** TO BE <span class=\"caps\">FILLED</span> IN ***</p>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"}}
    ]},
  {type:"constants",children:[
    {type:"constant",attributes:{"type":"Number","name":"MIN_WIDTH","value":"10"}}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","name":"_computeColumnsFlexWidth"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"tableColumnModel"},children:[
          {type:"desc",attributes:{"text":"<p>The table column model in use.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.ui.table.columnmodel.Resize"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"event"},children:[
          {type:"desc",attributes:{"text":"<p>The event object.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Computes the width of all flexible children (based loosely on the\nmethod of the same name in HorizontalBoxLayoutImpl).</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_extendLastColumn"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"tableColumnModel"},children:[
          {type:"desc",attributes:{"text":"<p>The table column model in use.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.ui.table.columnmodel.Resize"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"event"},children:[
          {type:"desc",attributes:{"text":"<p>The event object.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.DataEvent"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>If a column was just made invisible, extend the last column to fill any\navailable space within the inner width of the table.  This means that\nif the sum of the widths of all columns exceeds the inner width of the\ntable, no change is made.  If, on the other hand, the sum of the widths\nof all columns is less than the inner width of the table, the last\ncolumn is extended to take up the width available within the inner\nwidth of the table.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_extendNextColumn"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"tableColumnModel"},children:[
          {type:"desc",attributes:{"text":"<p>The table column model in use.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.ui.table.columnmodel.Resize"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"event"},children:[
          {type:"desc",attributes:{"text":"<p>The event object.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.DataEvent"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Extend the visible column to right of the column which just changed\nwidth, to fill any available space within the inner width of the table.\nThis means that if the sum of the widths of all columns exceeds the\ninner width of the table, no change is made.  If, on the other hand,\nthe sum of the widths of all columns is less than the inner width of\nthe table, the visible column to the right of the column which just\nchanged width is extended to take up the width available within the\ninner width of the table.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","docFrom":"qx.ui.table.columnmodel.resizebehavior.Abstract","overriddenFrom":"qx.ui.table.columnmodel.resizebehavior.Abstract","name":"_setNumColumns"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"numColumns"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"getInitializeWidthsOnEveryAppear","fromProperty":"initializeWidthsOnEveryAppear"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>initializeWidthsOnEveryAppear</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #initializeWidthsOnEveryAppear}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>initializeWidthsOnEveryAppear</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getNewResizeBehaviorColumnData","fromProperty":"newResizeBehaviorColumnData"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>newResizeBehaviorColumnData</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #newResizeBehaviorColumnData}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>newResizeBehaviorColumnData</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initInitializeWidthsOnEveryAppear","fromProperty":"initializeWidthsOnEveryAppear"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>initializeWidthsOnEveryAppear</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>initializeWidthsOnEveryAppear</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #initializeWidthsOnEveryAppear}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initNewResizeBehaviorColumnData","fromProperty":"newResizeBehaviorColumnData"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>newResizeBehaviorColumnData</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>newResizeBehaviorColumnData</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #newResizeBehaviorColumnData}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isInitializeWidthsOnEveryAppear","fromProperty":"initializeWidthsOnEveryAppear"},children:[
      {type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>initializeWidthsOnEveryAppear</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #initializeWidthsOnEveryAppear}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"docFrom":"qx.ui.table.columnmodel.resizebehavior.Abstract","overriddenFrom":"qx.ui.table.columnmodel.resizebehavior.Abstract","name":"onAppear"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"tableColumnModel"}},
        {type:"param",attributes:{"name":"event"}}
        ]}
      ]},
    {type:"method",attributes:{"docFrom":"qx.ui.table.columnmodel.resizebehavior.Abstract","overriddenFrom":"qx.ui.table.columnmodel.resizebehavior.Abstract","name":"onColumnWidthChanged"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"tableColumnModel"}},
        {type:"param",attributes:{"name":"event"}}
        ]}
      ]},
    {type:"method",attributes:{"docFrom":"qx.ui.table.columnmodel.resizebehavior.Abstract","overriddenFrom":"qx.ui.table.columnmodel.resizebehavior.Abstract","name":"onTableWidthChanged"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"tableColumnModel"}},
        {type:"param",attributes:{"name":"event"}}
        ]}
      ]},
    {type:"method",attributes:{"docFrom":"qx.ui.table.columnmodel.resizebehavior.Abstract","overriddenFrom":"qx.ui.table.columnmodel.resizebehavior.Abstract","name":"onVerticalScrollBarChanged"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"tableColumnModel"}},
        {type:"param",attributes:{"name":"event"}}
        ]}
      ]},
    {type:"method",attributes:{"docFrom":"qx.ui.table.columnmodel.resizebehavior.Abstract","overriddenFrom":"qx.ui.table.columnmodel.resizebehavior.Abstract","name":"onVisibilityChanged"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"tableColumnModel"}},
        {type:"param",attributes:{"name":"event"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetInitializeWidthsOnEveryAppear","fromProperty":"initializeWidthsOnEveryAppear"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>initializeWidthsOnEveryAppear</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #initializeWidthsOnEveryAppear}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetNewResizeBehaviorColumnData","fromProperty":"newResizeBehaviorColumnData"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>newResizeBehaviorColumnData</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #newResizeBehaviorColumnData}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"overriddenFrom":"qx.core.Object","name":"set"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"col"},children:[
          {type:"desc",attributes:{"text":"<p>The column whose attributes are to be changed</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"map"},children:[
          {type:"desc",attributes:{"text":"<p>A map containing any or all of the property names &#8220;width&#8221;, &#8220;minWidth&#8221;,\n  and &#8220;maxWidth&#8221;.  The property values are as described for\n  {@link #setWidth}, {@link #setMinWidth} and {@link #setMaxWidth}\n  respectively.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Map"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Set any or all of the width, minimum width, and maximum width of a\ncolumn in a single call.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setInitializeWidthsOnEveryAppear","fromProperty":"initializeWidthsOnEveryAppear"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>initializeWidthsOnEveryAppear</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>initializeWidthsOnEveryAppear</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #initializeWidthsOnEveryAppear}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setMaxWidth"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"col"},children:[
          {type:"desc",attributes:{"text":"<p>The column whose maximum width is to be set</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"width"},children:[
          {type:"desc",attributes:{"text":"<p>The maximum width of the specified column.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Set the maximum width of a column.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setMinWidth"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"col"},children:[
          {type:"desc",attributes:{"text":"<p>The column whose minimum width is to be set</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"width"},children:[
          {type:"desc",attributes:{"text":"<p>The minimum width of the specified column.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Set the minimum width of a column.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setNewResizeBehaviorColumnData","fromProperty":"newResizeBehaviorColumnData"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>newResizeBehaviorColumnData</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>newResizeBehaviorColumnData</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #newResizeBehaviorColumnData}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setWidth"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"col"},children:[
          {type:"desc",attributes:{"text":"<p>The column whose width is to be set</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"width"},children:[
          {type:"desc",attributes:{"text":"<p>The width of the specified column.  The width may be specified as\n  integer number of pixels (e.g. 100), a string representing percentage\n  of the inner width of the Table (e.g. &#8220;25%&#8221;), or a string\n  representing a flex width (e.g. &#8220;1*&#8221;).</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer, String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Set the width of a column.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toggleInitializeWidthsOnEveryAppear","fromProperty":"initializeWidthsOnEveryAppear"},children:[
      {type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>initializeWidthsOnEveryAppear</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #initializeWidthsOnEveryAppear}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the new value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"defaultValue":"false","propertyType":"new","hasError":"true","name":"initializeWidthsOnEveryAppear","check":"Boolean"},children:[
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"3","line":"99"}}
        ]}
      ]},
    {type:"property",attributes:{"defaultValue":"[Unsupported item type: function]","propertyType":"new","name":"newResizeBehaviorColumnData","check":"Function"},children:[
      {type:"desc",attributes:{"text":"<p>A function to instantiate a resize behavior column data object.</p>"}}
      ]}
    ]}
  ]}