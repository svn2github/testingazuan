// JavaScript Document

var getAttributeNode = function(name, entity, field) {
   var node = new Ext.tree.TreeNode({text:name});
   node.attributes = {iconCls:'attribute', entity:entity, field:field, type:'field'};
   return node;
};

var getMeasureNode = function(name, entity, field) {
   var node = new Ext.tree.TreeNode({text:name});
   node.attributes = {iconCls:'measure', entity:entity, field:field, type:'field'};
   //node.attributes = {icon:'../img/querybuilder/measure.gif', entity:entity, field:field, type:'field'};
   
    
   return node;
};

var getFoodmartTreePanel = function() {
    // datamart
    var rootNode=new Ext.tree.TreeNode({text:'Foodmart', iconCls:'database',expanded:true});
    
    
    // dimensions
    var productDimensionNode = new Ext.tree.TreeNode({text:'Product', iconCls:'dimension'});
    productDimensionNode.appendChild([
      getAttributeNode('Brand Name', 'product', 'product_brand'),
      getAttributeNode('Product Name', 'product', 'product_name'),
      getAttributeNode('Gross Weight', 'product', 'gross_weight'),
      getAttributeNode('Net Weight', 'product', 'net_weight'),
      getAttributeNode('Units Per Case', 'product', 'unit_per_case'),
      getAttributeNode('Cases Per Pallet', 'product', 'case_per_pallet')      
    ]);
    
    var customerDimensionNode = new Ext.tree.TreeNode({text:'Customer', iconCls:'dimension'});
    customerDimensionNode.appendChild([
      getAttributeNode('Name', 'customer', 'name'),
      getAttributeNode('Surname', 'customer', 'surname'),
      getAttributeNode('Gender', 'customer', 'gender'),
      getAttributeNode('Birth Date', 'customer', 'birth_date'),
      getAttributeNode('Phone Number', 'customer', 'phone_number'),
      getAttributeNode('Address', 'customer', 'address') 
    ]);
    
    var timeDimensionNode = new Ext.tree.TreeNode({text:'Time', iconCls:'dimension'});
    timeDimensionNode.appendChild([
       getAttributeNode('Year', 'time', 'year'), 
       getAttributeNode('Quarter', 'time', 'quarter'), 
       getAttributeNode('Month', 'time', 'month'),
       getAttributeNode('Week', 'time', 'week')
    ]);
    
    var viewNode = new Ext.tree.TreeNode({text:'ProfitByTimeAndBrand', iconCls:'view' });
    viewNode.appendChild([
      getAttributeNode('Year', 'profit_by_time_and_brand', 'year'), 
      getAttributeNode('Quarter', 'profit_by_time_and_brand', 'quarter'), 
      getAttributeNode('Brand', 'profit_by_time_and_brand', 'brand'), 
      getMeasureNode('Profit', 'profit_by_time_and_brand', 'profit') 
    ]);
    
    
    // cubes
    var salesCubeNode = new Ext.tree.TreeNode({text:'Sales', iconCls:'cube',expanded:true});
    salesCubeNode.appendChild([
      getMeasureNode('Store Sales', 'sales', 'store_sales'), 
      getMeasureNode('Store Costs', 'sales', 'store_costs'), 
      getMeasureNode('Unit Sales', 'sales', 'unit_sales'), 
      getMeasureNode('Profit', 'sales', 'profit'), 
      productDimensionNode,
      customerDimensionNode,
      timeDimensionNode    
    ]);
    
    var inventoryCubeNode = new Ext.tree.TreeNode({text:'Inventory', iconCls:'cube'});    
    var hrCubeNode = new Ext.tree.TreeNode({text:'HR', iconCls:'cube'});
    
    rootNode.appendChild([
     salesCubeNode,
     inventoryCubeNode,
     hrCubeNode,
     viewNode
    ]);
    
    //rootNode.on('click', this.log);
    
    //var tree = new Ext.data.Tree(rootNode);
    //tree.addListener('click', this.log);

    var menuTree=new Ext.tree.TreePanel({
      root:rootNode,
      //enableDD:true,
      expandable:true,
      collapsible:true,
      autoHeight:true ,
      bodyBorder:false ,
      width:300,
      leaf:false,
      lines:true,
      animate:true
    });
    
    menuTree.addListener('click', this.selectNode);
    
    return menuTree;
};

var selectNode = function(node, e) {
  
  if(node.attributes.field && node.attributes.type == 'field') {
    var record = new it.eng.spagobi.engines.qbe.querybuilder.selectGrid.Record({
         entity: node.attributes.entity , 
         field: node.attributes.field  
      });
      
    it.eng.spagobi.engines.qbe.querybuilder.selectGrid.addRow(record); 
  }
};