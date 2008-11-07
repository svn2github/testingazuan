{type:"class",attributes:{"isAbstract":"true","name":"AbstractTreeElement","hasWarning":"true","packageName":"qx.ui.tree","superClass":"qx.ui.layout.BoxLayout","childClasses":"qx.ui.tree.TreeFile,qx.ui.tree.TreeFolder","fullName":"qx.ui.tree.AbstractTreeElement","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>The AbstractTreeElement serves as a common superclass for the {@link\nTreeFile} and {@link TreeFolder} classes and is an implementation means of\nthe qooxdoo framework. It has no relevance for application developers.</p>"}},
  {type:"appearances",children:[
    {type:"appearance",attributes:{"type":"qx.ui.basic.Label","name":"tree-element-label"}},
    {type:"appearance",attributes:{"type":"qx.ui.basic.Image","name":"tree-element-icon"}},
    {type:"appearance",attributes:{"type":"qx.ui.tree.AbstractTreeElement","name":"tree-element"}}
    ]},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"treeRowStructure"}}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"defaultValue":"\"tree-element\"","propertyType":"new","overriddenFrom":"qx.ui.core.Widget","name":"appearance","refine":"true"},children:[
      {type:"desc",attributes:{"text":"<p>Controls the appearance of the tree element.</p>"}}
      ]},
    {type:"property",attributes:{"name":"icon","defaultValue":"\"icon/16/actions/document-new.png\"","allowNull":"true","propertyType":"new","apply":"_applyIcon","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>Controls the default icon for the element.</p>"}}
      ]},
    {type:"property",attributes:{"name":"iconSelected","defaultValue":"null","check":"String","allowNull":"true","propertyType":"new","apply":"_applyIcon","event":"iconSelected"},children:[
      {type:"desc",attributes:{"text":"<p>Controls the icon for the element when it is selected.</p>"}}
      ]},
    {type:"property",attributes:{"apply":"_applyLabel","propertyType":"new","name":"label","check":"Label"},children:[
      {type:"desc",attributes:{"text":"<p>The label/caption/text of the qx.ui.basic.Atom instance</p>"}}
      ]},
    {type:"property",attributes:{"defaultValue":"\"horizontal\"","propertyType":"new","overriddenFrom":"qx.ui.layout.BoxLayout","name":"orientation","refine":"true"},children:[
      {type:"desc",attributes:{"text":"<p>Controls the orientation of the tree element.</p>"}}
      ]},
    {type:"property",attributes:{"defaultValue":"false","propertyType":"new","overriddenFrom":"qx.ui.core.Widget","name":"selectable","refine":"true"},children:[
      {type:"desc",attributes:{"text":"<p>Controls whether the element is selectable.</p>"}}
      ]},
    {type:"property",attributes:{"name":"selected","defaultValue":"false","event":"changeSelected","propertyType":"new","apply":"_applySelected","check":"Boolean"},children:[
      {type:"desc",attributes:{"text":"<p>Selected property</p>"}}
      ]}
    ]},
  {type:"events",children:[
    {type:"event",attributes:{"name":"changeSelected"},children:[
      {type:"desc",attributes:{"text":"Fired on change of the property {@link #selected}."}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.ChangeEvent"}}
        ]}
      ]},
    {type:"event",attributes:{"name":"iconSelected"},children:[
      {type:"desc",attributes:{"text":"Fired on change of the property {@link #iconSelected}."}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.ChangeEvent"}}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.tree.AbstractTreeElement#icon","name":"_applyIcon"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>new value of the property</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"old"},children:[
          {type:"desc",attributes:{"text":"<p>previous value of the property (null if it was not yet set).</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Applies changes of the property value of the property <code>iconSelected</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #_applyIcon}.</p>"}}
      ]},
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.tree.AbstractTreeElement#label","name":"_applyLabel"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"old"},children:[
          {type:"desc",attributes:{"text":"<p>Previous value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.core.Widget#parent","overriddenFrom":"qx.ui.core.Widget","name":"_applyParent"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"old"},children:[
          {type:"desc",attributes:{"text":"<p>Previous value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Object"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.tree.AbstractTreeElement#selected","name":"_applySelected"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"old"},children:[
          {type:"desc",attributes:{"text":"<p>Previous value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","name":"_evalCurrentIcon"},children:[
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"24","line":"328"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_getRowStructure"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"labelOrTreeRowStructure"},children:[
          {type:"desc",attributes:{"text":"<p>Either the structure\n    defining a tree row or the label text to display for the tree.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}},
            {type:"entry",attributes:{"type":"TreeRowStructure"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"icon"},children:[
          {type:"desc",attributes:{"text":"<p>the image <span class=\"caps\">URL</span> to display for the tree</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"iconSelected"},children:[
          {type:"desc",attributes:{"text":"<p>the image <span class=\"caps\">URL</span> to display when the tree\n    is selected</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>The tree constructor understands two signatures. One compatible with the\noriginal qooxdoo tree and one compatible with the treefullcontrol widget.\nIf the first parameter if of type {@link TreeRowStructure} the tree\nelement is rendered using this structure. Otherwhise the all three\narguments are evaluated.</p>\n\n<p>This function evaluates the constructor arguments and returns a\n{@link TreeRowStructure} for the tree element.</p>"}}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","overriddenFrom":"qx.ui.core.Parent","name":"_handleDisplayableCustom"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vDisplayable"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vParent"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vHint"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Parameter <code>vDisplayable</code> is not documented.","column":"32","line":"654"}},
        {type:"error",attributes:{"msg":"Parameter <code>vParent</code> is not documented.","column":"32","line":"654"}},
        {type:"error",attributes:{"msg":"Parameter <code>vHint</code> is not documented.","column":"32","line":"654"}},
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"32","line":"654"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","name":"_onmousedown"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
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
        {type:"error",attributes:{"msg":"Parameter <code>e</code> is not documented.","column":"20","line":"707"}},
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"20","line":"707"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","name":"_onmouseup"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"}}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"18","line":"719"}}
        ]}
      ]},
    {type:"method",attributes:{"hasError":"true","name":"addToCustomQueues"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vHint"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Adds the current item to a custom queue.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Parameter <code>vHint</code> is not documented.","column":"25","line":"588"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"addToTreeQueue"},children:[
      {type:"desc",attributes:{"text":"<p>Adds the current element to the tree queue.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"destroy"},children:[
      {type:"desc",attributes:{"text":"<p>deselects, disconnects, removes and disposes the\n   current tree element and its content.</p>\n\n<p>destroys the current item (TreeFile or TreeFolder)\nand all its subitems. The destruction of the subitems\nis done by calling destroyContent. This is done if the\nsubitem has the method destroyContent which is true if the\nsubitem is a TreeFolder (or one of its subclasses).</p>\n\n<p>The method destroyContent is defined in the TreeFolder class.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"flushTree"},children:[
      {type:"desc",attributes:{"text":"<p>Flush the tree from the current element on.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getHierarchy"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vArr"},children:[
          {type:"desc",attributes:{"text":"<p>When called by the user, arr should typically be an empty array.\n      Each level from the current node upwards will push its label onto\n      the array.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"","dimensions":"4"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Obtain the entire hierarchy of labels from the root down to the current\nnode.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>array of label texts</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"","dimensions":"4"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getIcon","fromProperty":"icon"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>icon</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #icon}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>icon</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"hasError":"true","name":"getIconObject"},children:[
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"21","line":"408"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"getIconSelected","fromProperty":"iconSelected"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>iconSelected</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #iconSelected}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>iconSelected</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"hasError":"true","name":"getIndentObject"},children:[
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"23","line":"397"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"getLabel","fromProperty":"label"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>label</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #label}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>label</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"hasError":"true","name":"getLabelObject"},children:[
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"qx.ui.basic.Label"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"22","line":"419"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"getLevel"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the level of the tree element in the tree hierarchy (starting\nwith 0 at the root element).</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the level</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getParentFolder"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the parent folder of this tree element.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"AbstractTreeElement"}},
          {type:"entry",attributes:{"type":"null"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getSelected","fromProperty":"selected"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>selected</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #selected}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>selected</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getTree"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the tree from the parent folder of this element.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the tree root node</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"AbstractTreeElement"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initIcon","fromProperty":"icon"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>icon</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>icon</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #icon}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initIconSelected","fromProperty":"iconSelected"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>iconSelected</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>iconSelected</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #iconSelected}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initLabel","fromProperty":"label"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>label</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>label</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #label}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initSelected","fromProperty":"selected"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>selected</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>selected</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #selected}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isSelected","fromProperty":"selected"},children:[
      {type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>selected</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #selected}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"hasError":"true","name":"removeFromCustomQueues"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vHint"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Removes the current element from the custom queue.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Parameter <code>vHint</code> is not documented.","column":"30","line":"603"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"removeFromTreeQueue"},children:[
      {type:"desc",attributes:{"text":"<p>Removes the current element from the tree queue.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetIcon","fromProperty":"icon"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>icon</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #icon}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetIconSelected","fromProperty":"iconSelected"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>iconSelected</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #iconSelected}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetLabel","fromProperty":"label"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>label</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #label}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetSelected","fromProperty":"selected"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>selected</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #selected}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setIcon","fromProperty":"icon"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>icon</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>icon</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #icon}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setIconSelected","fromProperty":"iconSelected"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>iconSelected</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>iconSelected</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #iconSelected}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setLabel","fromProperty":"label"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>label</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>label</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #label}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setSelected","fromProperty":"selected"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>selected</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>selected</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #selected}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toggleSelected","fromProperty":"selected"},children:[
      {type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>selected</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #selected}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the new value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]}
    ]}
  ]}