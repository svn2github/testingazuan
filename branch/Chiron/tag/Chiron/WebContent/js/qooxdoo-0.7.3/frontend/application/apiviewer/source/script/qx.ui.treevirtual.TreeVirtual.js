{type:"class",attributes:{"name":"TreeVirtual","hasWarning":"true","packageName":"qx.ui.treevirtual","superClass":"qx.ui.table.Table","childClasses":"qx.ui.treevirtual.CheckBoxTree","fullName":"qx.ui.treevirtual.TreeVirtual","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>A &#8220;virtual&#8221; tree</p>\n\n<p><span class=\"caps\">WARNING</span>: This widget is still in development and the interface to it is\n         likely to change.  If you choose to use this widget, be aware that\n         you may need to make manual changes in accordance with interface\n         changes.</p>\n\n<p>A number of convenience methods are available in the following mixins:\n  <ul>\n    <li>{@link qx.ui.treevirtual.MNode}</li>\n    <li>{@link qx.ui.treevirtual.MFamily}</li>\n  </ul></p>"}},
  {type:"appearances",children:[
    {type:"appearance",attributes:{"type":"qx.ui.layout.HorizontalBoxLayout","name":"treevirtual-focus-indicator"}}
    ]},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"headings"},children:[
          {type:"desc",attributes:{"text":"<p>An array containing a list of strings, one for each column, representing\n  the headings for each column.  As a special case, if only one column is\n  to exist, the string representing its heading need not be enclosed in an\n  array.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Array"}},
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"null","name":"custom"},children:[
          {type:"desc",attributes:{"text":"<p>A map provided (typically) by subclasses, to override the various\n  supplemental classes allocated within this constructor.  For normal\n  usage, this parameter may be omitted.  Each property must be an object\n  instance or a function which returns an object instance, as indicated by\n  the defaults listed here:</p>\n\n<dl>\n    <dt>dataModel</dt>\n      <dd>new qx.ui.treevirtual.SimpleTreeDataModel()</dd>\n    <dt>treeDataCellRenderer</dt>\n      <dd>new qx.ui.treevirtual.SimpleTreeDataCellRenderer()</dd>\n    <dt>defaultDataCellRenderer</dt>\n      <dd>new qx.ui.treevirtual.DefaultDataCellRenderer()</dd>\n    <dt>dataRowRenderer</dt>\n      <dd>new qx.ui.treevirtual.SimpleTreeDataRowRenderer()</dd>\n    <dt>selectionManager</dt>\n      <dd>\n\n<pre class=\"javascript\">\n        function(obj)\n        {\n          return new qx.ui.treevirtual.SelectionManager(obj);\n        }\n      </pre>\n\n</dd>\n    <dt>tableColumnModel</dt>\n      <dd>\n\n<pre class=\"javascript\">\n        function(obj)\n        {\n          return new qx.ui.table.columnmodel.Resize(obj);\n        }\n      </pre>\n\n</dd>\n  </dl>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Map"}}
            ]}
          ]}
        ]}
      ]}
    ]},
  {type:"events",children:[
    {type:"event",attributes:{"hasError":"true","name":"changeSelection"},children:[
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"3","line":"231"}}
        ]},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.DataEvent"}}
        ]}
      ]},
    {type:"event",attributes:{"hasError":"true","name":"treeClose"},children:[
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"3","line":"231"}}
        ]},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.DataEvent"}}
        ]}
      ]},
    {type:"event",attributes:{"hasError":"true","name":"treeOpenWhileEmpty"},children:[
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"3","line":"231"}}
        ]},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.DataEvent"}}
        ]}
      ]},
    {type:"event",attributes:{"hasError":"true","name":"treeOpenWithContent"},children:[
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"3","line":"231"}}
        ]},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.DataEvent"}}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","name":"_calculateSelectedNodes"},children:[
      {type:"desc",attributes:{"text":"<p>Calculate and return the set of nodes which are currently selected by\nthe user, on the screen.  In the process of calculating which nodes\nare selected, the nodes corresponding to the selected rows on the\nscreen are marked as selected by setting their <i>bSelected</i>\nproperty to true, and all previously-selected nodes have their\n<i>bSelected</i> property reset to false.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>An array of nodes matching the set of rows which are selected on the\n  screen.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Array"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","overriddenFrom":"qx.ui.table.Table","name":"_onkeydown"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"evt"},children:[
          {type:"desc",attributes:{"text":"<p>The event.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Map"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Event handler. Called when a key was pressed.</p>\n\n<p>We handle the Enter key to toggle opened/closed tree state.  All\nother keydown events are passed to our superclass.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","overriddenFrom":"qx.ui.table.Table","name":"_onkeypress"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"evt"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Event"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Parameter <code>evt</code> is not documented.","column":"19","line":"845"}},
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"19","line":"845"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","overriddenFrom":"qx.ui.table.Table","name":"_onSelectionChanged"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"evt"},children:[
          {type:"desc",attributes:{"text":"<p>The event.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Map"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Event handler. Called when the selection has changed.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getAlwaysShowOpenCloseSymbol"},children:[
      {type:"desc",attributes:{"text":"<p>Set whether the open/close button should be displayed on a branch,\neven if the branch has no children.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<i>true</i> if tree lines are in use;\n  <i>false</i> otherwise."}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getDataModel"},children:[
      {type:"desc",attributes:{"text":"<p>Return the data model for this tree.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getExcludeFirstLevelTreeLines"},children:[
      {type:"desc",attributes:{"text":"<p>Get whether drawing of first-level tree lines should be disabled even\nif drawing of tree lines is enabled.\n(See also {@link #getUseTreeLines})</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<i>true</i> if tree lines are in use;\n  <i>false</i> otherwise."}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"overriddenFrom":"qx.ui.core.Parent","name":"getFirstChild"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"nodeReference"},children:[
          {type:"desc",attributes:{"text":"<p>The node for which the first child is desired.  The node can be\n  represented either by the node object, or the node id (as would have\n  been returned by addBranch(), addLeaf(), etc.)</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}},
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]}
        ]},
      {type:"deprecated",children:[
        {type:"desc",attributes:{"text":"<p>Use {@link qx.ui.treevirtual.MFamily.familyGetFirstChild} instead.</p>"}}
        ]},
      {type:"desc",attributes:{"text":"<p>Get the first child of the specified node.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The node id of the first child.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getHierarchy"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"nodeReference"},children:[
          {type:"desc",attributes:{"text":"<p>The node for which the hierarchy is desired.  The node can be\n  represented either by the node object, or the node id (as would have\n  been returned by addBranch(), addLeaf(), etc.)</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}},
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Obtain the entire hierarchy of labels from the root down to the\nspecified node.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The returned array contains one string for each label in the\n  hierarchy of the node specified by the parameter.  Element 0 of the\n  array contains the label of the root node, element 1 contains the\n  label of the node immediately below root in the specified node&#8217;s\n  hierarchy, etc., down to the last element in the array contain the\n  label of the node referenced by the parameter.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Array"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"overriddenFrom":"qx.ui.core.Parent","name":"getLastChild"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"nodeReference"},children:[
          {type:"desc",attributes:{"text":"<p>The node for which the last child is desired.  The node can be\n  represented either by the node object, or the node id (as would have\n  been returned by addBranch(), addLeaf(), etc.)</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}},
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]}
        ]},
      {type:"deprecated",children:[
        {type:"desc",attributes:{"text":"<p>Use {@link qx.ui.treevirtual.MFamily.familyGetLastChild} instead.</p>"}}
        ]},
      {type:"desc",attributes:{"text":"<p>Get the last child of the specified node.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The node id of the last child.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"overriddenFrom":"qx.ui.core.Widget","name":"getNextSibling"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"nodeReference"},children:[
          {type:"desc",attributes:{"text":"<p>The node for which the next sibling is desired.  The node can be\n  represented either by the node object, or the node id (as would have\n  been returned by addBranch(), addLeaf(), etc.)</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}},
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]}
        ]},
      {type:"deprecated",children:[
        {type:"desc",attributes:{"text":"<p>Use {@link qx.ui.treevirtual.MFamily.familyGetNextSibling} instead.</p>"}}
        ]},
      {type:"desc",attributes:{"text":"<p>Get the next sibling of the specified node.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The node id of the next sibling.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getOpenCloseClickSelectsRow","fromProperty":"openCloseClickSelectsRow"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>openCloseClickSelectsRow</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #openCloseClickSelectsRow}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>openCloseClickSelectsRow</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getPrevSibling"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"nodeReference"},children:[
          {type:"desc",attributes:{"text":"<p>The node for which the previous sibling is desired.  The node can be\n  represented either by the node object, or the node id (as would have\n  been returned by addBranch(), addLeaf(), etc.)</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}},
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]}
        ]},
      {type:"deprecated",children:[
        {type:"desc",attributes:{"text":"<p>Use {@link qx.ui.treevirtual.MFamily.familyGetPrevSibling} instead.</p>"}}
        ]},
      {type:"desc",attributes:{"text":"<p>Get the previous sibling of the specified node.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The node id of the previous sibling.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getSelectedNodes"},children:[
      {type:"desc",attributes:{"text":"<p>Return the nodes that are currently selected.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>An array containing the nodes that are currently selected.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Array"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"hasError":"true","name":"getSelectionMode"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"mode"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Get the selection mode currently in use.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>One of the values documented in {@link #setSelectionMode}</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Parameter <code>mode</code> is not documented.","column":"24","line":"522"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"getUseTreeLines"},children:[
      {type:"desc",attributes:{"text":"<p>Get whether lines linking tree children shall be drawn on the tree.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<i>true</i> if tree lines are in use;\n  <i>false</i> otherwise."}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initOpenCloseClickSelectsRow","fromProperty":"openCloseClickSelectsRow"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>openCloseClickSelectsRow</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>openCloseClickSelectsRow</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #openCloseClickSelectsRow}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isOpenCloseClickSelectsRow","fromProperty":"openCloseClickSelectsRow"},children:[
      {type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>openCloseClickSelectsRow</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #openCloseClickSelectsRow}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetOpenCloseClickSelectsRow","fromProperty":"openCloseClickSelectsRow"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>openCloseClickSelectsRow</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #openCloseClickSelectsRow}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setAlwaysShowOpenCloseSymbol"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"b"},children:[
          {type:"desc",attributes:{"text":"<i>true</i> if the open/close button should be shown;\n  <i>false</i> otherwise."}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Set whether the open/close button should be displayed on a branch,\neven if the branch has no children.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setCellFocusAttributes"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"attributes"},children:[
          {type:"desc",attributes:{"text":"<p>The set of attributes that the cell focus indicator should have.\n  This is in the format required to call the <i>set()</i> method of a\n  widget, e.g.</p>\n\n<p>{ backgroundColor: blue }</p>\n\n<p>If not otherwise specified, the opacity is set to 0.2 so that the\n  cell data can be seen &#8220;through&#8221; the cell focus indicator which\n  overlays it.</p>\n\n<p>For no visible focus indicator, use:</p>\n\n<p>{ backgroundColor : &#8220;transparent&#8221; }</p>\n\n<p>The focus indicator is a box the size of the cell, which overlays\n  the cell itself.  There is no text in the focus indicator itself,\n  so it makes no sense to set the color attribute or any other\n  attribute that affects fonts.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Map"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Set the attributes used to indicate the cell that has the focus.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setExcludeFirstLevelTreeLines"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"b"},children:[
          {type:"desc",attributes:{"text":"<i>true</i> if first-level tree lines should be disabled;\n  <i>false</i> for normal operation."}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Set whether drawing of first-level tree-node lines are disabled even\nif drawing of tree lines is enabled.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setOpenCloseClickSelectsRow","fromProperty":"openCloseClickSelectsRow"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>openCloseClickSelectsRow</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>openCloseClickSelectsRow</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #openCloseClickSelectsRow}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"hasError":"true","overriddenFrom":"qx.ui.core.Widget","name":"setOverflow"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"s"},children:[
          {type:"desc",attributes:{"text":"<p>Overflow mode.  The only allowable mode is &#8220;hidden&#8221;.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"19","line":"960"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"setSelectionMode"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"mode"},children:[
          {type:"desc",attributes:{"text":"<p>The selection mode to be used.  It may be any of:</p>\n\n<pre>\n      qx.ui.treevirtual.TreeVirtual.SelectionMode.NONE:\n         Nothing can ever be selected.\n\n      qx.ui.treevirtual.TreeVirtual.SelectionMode.SINGLE\n         Allow only one selected item.\n\n      qx.ui.treevirtual.TreeVirtual.SelectionMode.SINGLE_INTERVAL\n         Allow one contiguous interval of selected items.\n\n      qx.ui.treevirtual.TreeVirtual.SelectionMode.MULTIPLE_INTERVAL\n         Allow any selected items, whether contiguous or not.\n    </pre>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Set the selection mode.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setState"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"nodeReference"},children:[
          {type:"desc",attributes:{"text":"<p>The node for which attributes are being set.  The node can be\n  represented either by the node object, or the node id (as would have\n  been returned by addBranch(), addLeaf(), etc.)</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}},
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"attributes"},children:[
          {type:"desc",attributes:{"text":"<p>Map with the node properties to be set.  The map may contain any of\n  the properties described in\n  {@link qx.ui.treevirtual.SimpleTreeDataModel}</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Map"}}
            ]}
          ]}
        ]},
      {type:"deprecated",children:[
        {type:"desc",attributes:{"text":"<p>Use {@link qx.ui.treevirtual.MNode.nodeSetState} instead.</p>"}}
        ]},
      {type:"desc",attributes:{"text":"<p>Set state attributes of a tree node.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setUseTreeLines"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"b"},children:[
          {type:"desc",attributes:{"text":"<i>true</i> if tree lines should be shown; <i>false</i> otherwise."}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Set whether lines linking tree children shall be drawn on the tree.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toggleOpenCloseClickSelectsRow","fromProperty":"openCloseClickSelectsRow"},children:[
      {type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>openCloseClickSelectsRow</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #openCloseClickSelectsRow}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the new value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toggleOpened"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"nodeReference"},children:[
          {type:"desc",attributes:{"text":"<p>The node to have its opened/closed state toggled.  The node can be\n  represented either by the node object, or the node id (as would have\n  been returned by addBranch(), addLeaf(), etc.)</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}},
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]}
        ]},
      {type:"deprecated",children:[
        {type:"desc",attributes:{"text":"<p>Use {@link qx.ui.treevirtual.MNode.nodeSetOpened} or\n   {@link qx.ui.treevirtual.MNode.nodeSetOpened} instead.</p>"}}
        ]},
      {type:"desc",attributes:{"text":"<p>Toggle the opened state of the node: if the node is opened, close\nit; if it is closed, open it.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"defaultValue":"false","propertyType":"new","name":"openCloseClickSelectsRow","check":"Boolean"},children:[
      {type:"desc",attributes:{"text":"<p>Whether a click on the open/close button should also cause selection of\nthe row.</p>"}}
      ]}
    ]}
  ]}