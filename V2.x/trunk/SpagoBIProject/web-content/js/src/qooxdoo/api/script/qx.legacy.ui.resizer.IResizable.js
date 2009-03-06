{type:"class",attributes:{"name":"IResizable","hasWarning":"true","packageName":"qx.legacy.ui.resizer","implementations":"qx.legacy.ui.resizer.ResizablePopup,qx.legacy.ui.resizer.Resizer","fullName":"qx.legacy.ui.resizer.IResizable","type":"interface"},children:[{type:"desc",attributes:{"text":"<p>This interface must be implemented when adding mixin MResizer to a class.</p>"}},{type:"methods",children:[{type:"method",attributes:{"access":"protected","hasError":"true","name":"_changeHeight"},children:[{type:"params",children:[{type:"param",attributes:{"name":"newHeight"},children:[{type:"types",children:[{type:"entry",attributes:{"type":"Number"}}]}]}]},{type:"desc",attributes:{"text":"<p>After resizing completes, it updates the height.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]},{type:"errors",children:[{type:"error",attributes:{"msg":"Parameter <code>newHeight</code> is not documented.","column":"15","line":"46"}}]}]},{type:"method",attributes:{"access":"protected","hasError":"true","name":"_changeWidth"},children:[{type:"params",children:[{type:"param",attributes:{"name":"newWidth"},children:[{type:"types",children:[{type:"entry",attributes:{"type":"Number"}}]}]}]},{type:"desc",attributes:{"text":"<p>After resizing completes, it updates the width.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]},{type:"errors",children:[{type:"error",attributes:{"msg":"Parameter <code>newWidth</code> is not documented.","column":"14","line":"39"}}]}]},{type:"method",attributes:{"access":"protected","name":"_getMinSizeReference"},children:[{type:"desc",attributes:{"text":"<p>Which widget determines the minimum size?</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"qx.legacy.ui.core.Widget"}}]}]}]},{type:"method",attributes:{"access":"protected","name":"_getResizeParent"},children:[{type:"desc",attributes:{"text":"<p>Respect which widget the resizing occurs?</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"qx.legacy.ui.core.Widget"}}]}]}]}]}]}