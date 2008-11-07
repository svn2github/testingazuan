{type:"class",attributes:{"isStatic":"true","name":"Object","packageName":"qx.lang","fullName":"qx.lang.Object","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>Helper functions to handle Object as a Hash map.</p>"}},
  {type:"methods-static",children:[
    {type:"method",attributes:{"isStatic":"true","name":"carefullyMergeWith"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"target"},children:[
          {type:"desc",attributes:{"text":"<p>target object</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"source"},children:[
          {type:"desc",attributes:{"text":"<p>object to be merged</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"deprecated"},
      {type:"desc",attributes:{"text":"<p>Inserts all keys of the source object into the\ntarget objects.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>target with merged values from source</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Object"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"copy"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"source"},children:[
          {type:"desc",attributes:{"text":"<p>Object to copy</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Return a copy of an Object</p>\n\n<p><span class=\"caps\">TODO</span>: Rename to clone() like in prototype and python</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>copy of vObject</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Object"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"fromArray"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"array"},children:[
          {type:"desc",attributes:{"text":"<p>array to convert</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Array"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Convert an array into a map.</p>\n\n<p>All elements of the array become keys of the returned map by\ncalling &#8220;toString&#8221; on the array elements. The values of the\nmap are set to &#8220;true&#8221;</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the array converted to a map.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Map"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getKeyFromValue"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"obj"},children:[
          {type:"desc",attributes:{"text":"<p>Map to search for the key</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Value to look for</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Get the key of the given value from a map.\nIf the map has more than one key matching the value the fist match is returned.\nIf the map does not contain the value <code>null</code> is returned.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Name of the key (null if not found).</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}},
          {type:"entry",attributes:{"type":"null"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getKeysAsString"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"map"},children:[
          {type:"desc",attributes:{"text":"<p>the map</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Get the keys of a map as string</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>String of the keys of the map\n        The keys are separated by &#8221;, &#8221;</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getLength"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"map"},children:[
          {type:"desc",attributes:{"text":"<p>the map</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Get the number of objects in the map</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>number of objects in the map</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getValues"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"map"},children:[
          {type:"desc",attributes:{"text":"<p>the map</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Get the values of a map as array</p>\n\n<p><span class=\"caps\">TODO</span>: Rename to values() like in prototype and python</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>array of the values of the map</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Array"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"hasMinLength"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"map"},children:[
          {type:"desc",attributes:{"text":"<p>the map to check</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"length"},children:[
          {type:"desc",attributes:{"text":"<p>minimum number of objects in the map</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Check whether the number of objects in the maps is at least &#8220;length&#8221;</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>whether the map contains at least &#8220;length&#8221; objects.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"invert"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"map"},children:[
          {type:"desc",attributes:{"text":"<p>Map to invert</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Inverts a Map by exchanging the keys with the values.\nIf the map has the same values for different keys, information will get lost.\nThe values will be converted to Strings using the toString methos.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>inverted Map</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Object"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"isEmpty"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"map"},children:[
          {type:"desc",attributes:{"text":"<p>the map to check</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Check if the hash has any keys</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>whether the map has any keys</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"merge"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"target"},children:[
          {type:"desc",attributes:{"text":"<p>target object</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"varargs"},children:[
          {type:"desc",attributes:{"text":"<p>variable number of objects to merged with target</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Merge a number of objects.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>target with merged values from the other objects</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Object"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"mergeWith"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"target"},children:[
          {type:"desc",attributes:{"text":"<p>target object</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"source"},children:[
          {type:"desc",attributes:{"text":"<p>object to be merged</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"true","name":"overwrite"},children:[
          {type:"desc",attributes:{"text":"<p>If enabled existing keys will be overwritten</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Inserts all keys of the source object into the\ntarget objects. Attention: The target map gets modified.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Target with merged values from the source object</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Object"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"select"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"key"},children:[
          {type:"desc",attributes:{"text":"<p>name of the key to get the value from</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"map"},children:[
          {type:"desc",attributes:{"text":"<p>map to get the value from</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Selects the value with the given key from the map.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>value for the given key from the map</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]}
    ]}
  ]}